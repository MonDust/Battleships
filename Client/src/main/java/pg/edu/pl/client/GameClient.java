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
     * Constructor for the class.
     */
    public GameClient() {}

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

    /**
     * Listen for messages sent from servers.
     * And make action based on message.
     * 1. CONNECTED
     * 2. PLACED
     * 3. BOTH_READY
     * 4. TURN_READY
     */
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
                            // ensures the result is handed off to the waiting thread
                            placementResults.put(placementSuccess);
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
                logger.log(Level.WARNING, "!IO issue - server message", e);
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
            logger.log(Level.WARNING, "!IO issue - sendMessage", e);
        }
    }

    /**
     * Message for placing ships.
     * @param x x placement
     * @param y y placement
     * @param horizontal horizontal or vertical placement
     * @param size size of the ship
     */
    private void sendPlaceShip(int x, int y, boolean horizontal, int size) {
        JsonObject message = new JsonObject();
        message.addProperty("type", "PLACE_SHIP");
        message.addProperty("x", x);
        message.addProperty("y", y);
        message.addProperty("horizontal", horizontal);
        message.addProperty("size", size);

        sendMessage(message.toString());
    }

    /**
     * Message for doing a turn.
     * @param x x placement
     * @param y y placement
     */
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

    /**
     * Message for printing opponent board.
     * Basically what you have done to the opponent board (what is the current state available to you).
     */
    private void sendPrintOpponentBoard() {
        JsonObject message = new JsonObject();
        message.addProperty("type", "PRINT_OPPONENT_BOARD");

        sendMessage(message.toString());
    }

    /**
     * exit - Stop the client.
     * Close the reader, writer, socket.
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
     * Choose the option for placing ships window.
     * Option:
     * 1 - Place the ship
     * 2 - Exit
     * @return the option
     */
    public int chooseOptionPlacingShips() {
        boolean optionsChoice = true;
        int option = 3;

        while(optionsChoice) {
            System.out.println("Options: ");
            System.out.println("1. Place ship.");
            System.out.println("2. Exit game.");
            try {
                option = scanner.nextInt();
            } catch (InputMismatchException e) {
                logger.log(Level.INFO, "Invalid option entered, try again.");
                scanner.nextLine();
                continue;
            }
            if (option == 2) {
                exit();
                return option;
            } else if (option == 1) {
                optionsChoice = false;
            } else {
                logger.log(Level.INFO,"Invalid option. Please try again.");
            }
        }
        return option;
    }

    /**
     * Get input from player about their ship placement.
     * x, y and if the ship is horizontal or vertical
     * @param shipType - type of the ship
     * @throws InputMismatchException if player gives wrong input exception will be thrown
     */
    public void scanInputsAndSentShipPlacement(Ship_type shipType) throws InputMismatchException {
        int x, y;
        boolean horizontal;

        try {
            if (!scanner.hasNextInt()) throw new InputMismatchException("Expected integer for x");
            x = scanner.nextInt();

            if (!scanner.hasNextInt()) throw new InputMismatchException("Expected integer for y");
            y = scanner.nextInt();

            if (!scanner.hasNextBoolean()) throw new InputMismatchException("Expected boolean for horizontal");
            horizontal = scanner.nextBoolean();
        } catch (InputMismatchException e) {
            scanner.nextLine();
            throw e;
        }

        logger.log(Level.INFO, String.format("Placing ship (%s - size: %d) at x=%d y=%d horiz=%s", shipType.toString(), shipType.getSize(), x, y, horizontal));
        sendPlaceShip(x, y, horizontal, shipType.getSize());
    }

    /**
     * Placing ships function.
     * Will get ship values from Ship_type enum, can be changed in Gameplay in utils.
     */
    public boolean placingShips() {
        logger.log(Level.INFO,"Placing ships on the board. ");

        for (Ship_type type : Ship_type.values()) {
            boolean placed = false;
            while (!placed) {
                // showing the board to the player
                sendPrintMyBoard();

                // player can either choose to place ship or exit
                if(chooseOptionPlacingShips() != 1) return false;

                // placing ships
                System.out.printf("Placing %s (size %d).%n", type.name(), type.getSize());
                System.out.println("Enter ship details (x, y, horizontal(true/false)):");
                try {
                    scanInputsAndSentShipPlacement(type);
                    try {
                        Boolean result = placementResults.take();
                        if (result) {
                            placed = true;
                            logger.log(Level.INFO, type.name() + " placed successfully.");
                        } else {
                            logger.log(Level.INFO, "Ship could not be placed. Try again.");
                            logger.log(Level.INFO, String.format("Placement failed for %s", type.name()));
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        logger.log(Level.SEVERE, "Interrupted while waiting for ship placement result", e);
                    }

                } catch (InputMismatchException e) {
                    logger.log(Level.INFO, "Invalid input format. Please try again.");
                    logger.log(Level.WARNING, "Invalid input type from user: " + e.getMessage());
                    // clear input
                    scanner.nextLine();
                }
            }
        }
        return true;
    }

    /**
     * Actual gameplay after the session started.
     */
    public void gamePlay() {
        scanner = new Scanner(System.in);
        logger.log(Level.INFO,"Welcome to Battleships! \nGame session started.");

        boolean isGameRunning = placingShips();
        JsonObject message = new JsonObject();
        if(isGameRunning) {
            message.addProperty("type", "READY");
            sendMessage(message.toString());
            isOpponentReady = true;
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
                            logger.log(Level.INFO, "Invalid input format. Please try again.");
                            logger.warning("Invalid input type from user: " + e.getMessage());
                            // clear input
                            scanner.nextLine();
                        }
                        break;
                    case 2:
                        exit();
                        isGameRunning = false;
                        message = new JsonObject();
                        message.addProperty("type", "EXIT");
                        sendMessage(message.toString());
                        break;
                    default:
                        logger.log(Level.INFO,"Invalid choice.");
                        isTurn = true;
                }
            }
        }
    }
}
