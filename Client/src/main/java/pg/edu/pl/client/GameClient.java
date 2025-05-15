package pg.edu.pl.client;

import com.google.gson.*;
import pg.edu.pl.utils.Ship_type;

import java.io.*;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for running Game clients, trying to connect to the server.
 * Later granted two clients are connected to the session,
 * they can start a game.
 */
public class GameClient {
    private BufferedWriter writer;
    private BufferedReader reader;
    private Socket socket;
    private final BlockingQueue<Boolean> placementResults = new LinkedBlockingQueue<>();

    private boolean isOpponentReady = false;
    private boolean isTurn = true;

    // making a thread wait until the server confirms connection
    private final CountDownLatch sessionStartedLatch = new CountDownLatch(1);

    Scanner scanner;
    private static final Logger logger = Logger.getLogger(GameClient.class.getName());

    /**
     * Constructor for class.
     */
    public GameClient() {

    }

    /**
     * Start the client, try to connect to the server.
     * @param host - server address
     * @param port - server port
     */
    public void start(String host, int port) {
        try {
            socket = new Socket(host, port);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            logger.info("Connected to server at " + host + ":" + port);
            listenForServerMessages();

            try {
                // wait until "CONNECTED"
                sessionStartedLatch.await();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                logger.log(Level.SEVERE, "Thread was interrupted before game session started", ex);
                return;
            }

            // -> when session is connected the game will start
            gamePlay();

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error occurred while connecting to the server (host: " + host + ", port: " + port + ")", e);
        }
    }

    private void listenForServerMessages() {
        new Thread(() -> {
            try {
                String response;
                while ((response = reader.readLine()) != null) {
                    System.out.println("Server: " + response);
                    if (response.contains("CONNECTED")) {
                        // unblock the main thread -> so game can start (session started)
                        sessionStartedLatch.countDown();
                    } else if (response.contains("PLACED: ")) {
                        boolean placementSuccess = Boolean.parseBoolean(response.split(": ")[1].trim());
                        try {
                            placementResults.put(placementSuccess); // ensures the result is handed off to the waiting thread
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            logger.log(Level.SEVERE, "Listener thread interrupted while putting placement result", e);
                        }
                    } else if (response.contains("BOTH_READY")) {
                        isOpponentReady = true;
                    } else if (response.contains("TURN_READY")) {
                        isTurn = true;
                    }
                }
            } catch (IOException e) {
                logger.log(Level.WARNING, "Non-critical IO issue", e);
            }
        }).start();
    }

    /**
     * Send message to the server.
     * @param message - message for the server
     */
    private void sendMessage(String message) {
        try {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Non-critical IO issue - sendMessage", e);
        }
    }

    private void sendPlaceShip(int x, int y, boolean horizontal, int size) {
        JsonObject message = new JsonObject();
        message.addProperty("type", "PLACE_SHIP");
        message.addProperty("x", x);
        message.addProperty("y", y);
        message.addProperty("horizontal", horizontal);
        message.addProperty("size", size);

        sendMessage(message.toString());
    }

    private void sendDoTurn(int x, int y) {
        JsonObject message = new JsonObject();
        message.addProperty("type", "DO_TURN");
        message.addProperty("x", x);
        message.addProperty("y", y);

        sendMessage(message.toString());
    }

    private void sendPrintMyBoard() {
        JsonObject message = new JsonObject();
        message.addProperty("type", "PRINT_MY_BOARD");

        sendMessage(message.toString());
    }

    private void sendPrintOpponentBoard() {
        JsonObject message = new JsonObject();
        message.addProperty("type", "PRINT_OPPONENT_BOARD");

        sendMessage(message.toString());
    }

    /**
     * exit - Stop the client.
     */
    public void exit() {
        JsonObject message = new JsonObject();
        message.addProperty("type", "EXIT");

        sendMessage(message.toString());

        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error while closing resources", e);
        }
    }

    /**
     * Placing ships.
     */
    public boolean placingShips() {
        System.out.println("Placing ships on the board. ");
        int x, y;
        boolean horizontal;

        for (Ship_type type : Ship_type.values()) {

            boolean placed = false;
            while (!placed) {
                sendPrintMyBoard();

                boolean optionsChoice = true;
                while(optionsChoice) {
                    System.out.println("Options: ");
                    System.out.println("1. Place ship.");
                    System.out.println("2. Exit game.");
                    int option = scanner.nextInt();
                    if (option == 2) {
                        exit();
                        return false;
                    } else if (option == 1) {
                        optionsChoice = false;
                    } else {
                        System.out.println("Invalid option. Please try again.");
                    }
                }

                System.out.printf("Placing %s (size %d).%n", type.name(), type.getSize());
                System.out.println("Enter ship details (x, y, horizontal(true/false)):");
                try {
                    if (!scanner.hasNextInt()) throw new InputMismatchException("Expected integer for x");
                    x = scanner.nextInt();

                    if (!scanner.hasNextInt()) throw new InputMismatchException("Expected integer for y");
                    y = scanner.nextInt();

                    if (!scanner.hasNextBoolean()) throw new InputMismatchException("Expected boolean for horizontal");
                    horizontal = scanner.nextBoolean();

                    sendPlaceShip(x, y, horizontal, type.getSize());

                    try {
                        Boolean result = placementResults.take();
                        if (result) {
                            placed = true;
                            System.out.println(type.name() + " placed successfully.");
                        } else {
                            System.out.println("Ship could not be placed. Try again.");
                            logger.info(String.format("Placement failed for %s at x=%d y=%d horiz=%s", type.name(), x, y, horizontal));
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        logger.log(Level.SEVERE, "Interrupted while waiting for ship placement result", e);
                    }

                } catch (InputMismatchException e) {
                    System.out.println("Invalid input format. Please try again.");
                    logger.warning("Invalid input type from user: " + e.getMessage());
                    // clear input
                    scanner.nextLine();
                }
            }
        }
        return true;
    }

    // players do turn at the same time
    // TODO - fix that

    /**
     * Actual gameplay after the session started.
     */
    public void gamePlay() {
        scanner = new Scanner(System.in);
        logger.info("Welcome to Battleships! \nGame session started.");

        boolean isGameRunning = placingShips();
        if(isGameRunning) {
            JsonObject message = new JsonObject();
            message.addProperty("type", "READY");
            sendMessage(message.toString());
            isOpponentReady = true;
        } else {
            JsonObject message = new JsonObject();
            message.addProperty("type", "EXIT");
            sendMessage(message.toString());
        }
        while (isGameRunning) {
            if(true) {
                System.out.println("Options: ");
                System.out.println("1. Do turn.");
                System.out.println("2. Exit game.");

                int choice = scanner.nextInt();
                scanner.nextLine();

                // TODO - waiting for all the ships to be placed, and printing board during placing.
                sendPrintMyBoard();
                sendPrintOpponentBoard();

                switch (choice) {
                    case 1:
                        System.out.println("Enter turn coordinates (x, y):");
                        try {
                            if (!scanner.hasNextInt()) throw new InputMismatchException("Expected integer for x");
                            int x = scanner.nextInt();
                            if (!scanner.hasNextInt()) throw new InputMismatchException("Expected integer for y");
                            int y = scanner.nextInt();

                            sendDoTurn(x, y);
                            //isTurn = false;
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input format. Please try again.");
                            logger.warning("Invalid input type from user: " + e.getMessage());
                            // clear input
                            scanner.nextLine();
                        }
                        break;
                    case 2:
                        exit();
                        isGameRunning = false;
                        JsonObject message = new JsonObject();
                        message.addProperty("type", "EXIT");
                        sendMessage(message.toString());
                        break;
                    default:
                        System.out.println("Invalid choice.");
                        isTurn = true;
                }
            }
        }
    }


}
