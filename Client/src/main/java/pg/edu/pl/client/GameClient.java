package pg.edu.pl.client;

import com.google.gson.*;
import pg.edu.pl.utils.Player_choice;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class GameClient {
    private Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;
    private Player_choice playerChoice;

    public GameClient() {

    }

    public void start(String host, int port) {
        try {
            socket = new Socket(host, port);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("Connected to server at " + host + ":" + port);
            listenForServerMessages();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GamePlay();
    }

    private void listenForServerMessages() {
        new Thread(() -> {
            try {
                String response;
                while ((response = reader.readLine()) != null) {
                    System.out.println("Server: " + response);
                    if (response.contains("CONNECTED")) {
                        if(response.contains("PLAYER_ONE")) {
                            playerChoice = Player_choice.PLAYER_ONE;
                        } else {
                            playerChoice = Player_choice.PLAYER_TWO;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void sendMessage(String message) {
        try {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void placeShip(int x, int y, boolean horizontal, int size) {
        JsonObject message = new JsonObject();
        message.addProperty("type", "PLACE_SHIP");
        message.addProperty("x", x);
        message.addProperty("y", y);
        message.addProperty("horizontal", horizontal);
        message.addProperty("size", size);

        sendMessage(message.toString());
    }

    public void doTurn(int x, int y) {
        JsonObject message = new JsonObject();
        message.addProperty("type", "DO_TURN");
        message.addProperty("x", x);
        message.addProperty("y", y);

        sendMessage(message.toString());
    }

    public void printMyBoard() {
        JsonObject message = new JsonObject();
        message.addProperty("type", "PRINT_MY_BOARD");

        sendMessage(message.toString());
    }

    public void exit() {
        JsonObject message = new JsonObject();
        message.addProperty("type", "EXIT");

        sendMessage(message.toString());
    }

    public void GamePlay() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Battleship! Choose an action:");

        while (true) {
            System.out.println("1. Place Ship");
            System.out.println("2. Do Turn");
            System.out.println("3. Print My Board");
            System.out.println("4. Exit");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Enter ship details (x, y, horizontal(true/false), size):");
                    int x = scanner.nextInt();
                    int y = scanner.nextInt();
                    boolean horizontal = scanner.nextBoolean();
                    int size = scanner.nextInt();
                    placeShip(x, y, horizontal, size);
                    break;
                case 2:
                    System.out.println("Enter turn coordinates (x, y):");
                    x = scanner.nextInt();
                    y = scanner.nextInt();
                    doTurn(x, y);
                    break;
                case 3:
                    printMyBoard();
                    break;

                case 4:
                    exit();
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }


}
