package pg.edu.pl.player;

import java.io.*;
import java.net.Socket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import pg.edu.pl.entities.Field;
import pg.edu.pl.entities.Player;
import pg.edu.pl.entities.Ship;
import pg.edu.pl.entities.interfaces.IBoard;
import pg.edu.pl.entities.interfaces.IShip;
import pg.edu.pl.game_mechanics.Game;
import pg.edu.pl.utils.Player_choice;


public class PlayerHandler implements Runnable {
    private final Socket socket;
    private final Player_choice playerChoice;
    private final Game game;
    private boolean running = true;

    public PlayerHandler(Socket socket, Player_choice playerChoice, Game game) {
        this.socket = socket;
        this.playerChoice = playerChoice;
        this.game = game;
    }

    @Override
    public void run() {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            sendMessage(writer, "CONNECTED as " + playerChoice.name());
            System.out.println("Connection established with player: " + playerChoice.name());

            while (running) {
                String input = reader.readLine();
                if (input == null) {
                    System.out.println("Client disconnected: " + playerChoice.name());
                    running = false;
                    break;
                }

                JsonObject message = JsonParser.parseString(input).getAsJsonObject();
                String type = message.get("type").getAsString();

                switch (type) {
                    case "DO_TURN" -> {
//                        if (!game.getCurrentPlayer().equals(playerChoice)) {
//                            sendMessage(writer, "NOT_YOUR_TURN");
//                            break;
//                        }
                        if(playerChoice == Player_choice.PLAYER_ONE) {
                            game.setCurrentPlayer(Player_choice.PLAYER_ONE);
                        }
                        else {
                            game.setCurrentPlayer(Player_choice.PLAYER_TWO);
                        }

                        System.out.println("Game:" + game.getCurrentPlayer());

                        int x = message.get("x").getAsInt();
                        int y = message.get("y").getAsInt();
                        boolean result = game.doTurn(x, y);

                        IBoard board, opponentBoard;
                        if (playerChoice == Player_choice.PLAYER_ONE) {
                            board = game.getPlayer_1().getBoard();
                            opponentBoard = game.getPlayer_2().getBoard();
                        } else {
                            board = game.getPlayer_2().getBoard();
                            opponentBoard = game.getPlayer_1().getBoard();
                        }
                        game.printPlayerView(board);
                        game.printOpponentView(opponentBoard);

                        game.switchPlayer();
                        System.out.println("Player " + game.getCurrentPlayer().name() + " turn.");

                        sendMessage(writer, "TURN_RESULT: " + result);

                        // maybe notify the other player
                    }
                    case "PLACE_SHIP" -> {
                        int x = message.get("x").getAsInt();
                        int y = message.get("y").getAsInt();
                        boolean horizontal = message.get("horizontal").getAsBoolean();
                        int size = message.get("size").getAsInt();

                        // System.out.println("Ship: " + x + " " + y + " " + horizontal + " " + size);

                        IBoard board, opponentBoard;
                        if (playerChoice == Player_choice.PLAYER_ONE) {
                            board = game.getPlayer_1().getBoard();
                            opponentBoard = game.getPlayer_2().getBoard();
                        } else {
                            board = game.getPlayer_2().getBoard();
                            opponentBoard = game.getPlayer_1().getBoard();
                        }

                        IShip ship = new Ship(size);

                        boolean success = board.placeShip(ship, x, y, horizontal);
                        game.printPlayerView(board);
                        game.printOpponentView(opponentBoard);
                        sendMessage(writer, "PLACED: " + success);
                    }
                    case "PRINT_MY_BOARD" -> {
                        IBoard board;
                        if (playerChoice == Player_choice.PLAYER_ONE) {
                            board = game.getPlayer_1().getBoard();
                        } else {
                            board = game.getPlayer_2().getBoard();
                        }
                        sendMessage(writer, game.getPlayerView(board));
                    }
                    case "PRINT_OPPONENT_BOARD" -> {
                        IBoard board;
                        if (playerChoice == Player_choice.PLAYER_TWO) {
                            board = game.getPlayer_1().getBoard();
                        } else {
                            board = game.getPlayer_2().getBoard();
                        }
                        sendMessage(writer, game.getOpponentView(board));
                    }
                    case "EXIT" -> {
                        sendMessage(writer, "GOODBYE");
                        running = false;
                    }
                    case "READY" -> {
                        // this could be here not in game
                        game.setPlayerReady(playerChoice);
//                        sendMessage(writer, "WAITING FOR OPPONENT");
//                        try {
//                            game.waitForBothPlayersReady();
//                            sendMessage(writer, "BOTH_READY");
//                        } catch (InterruptedException e) {
//                            Thread.currentThread().interrupt();
//                            sendMessage(writer, "ERROR: Interrupted while waiting for opponent");
//                            System.err.println("Interrupted while waiting for opponent");
//                        }
                    }
                    default -> sendMessage(writer, "Unknown command: " + type);
                }
            }
        } catch (java.net.SocketException e) {
            System.out.println("Socket closed for player " + playerChoice.name() + ": " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {}
            System.out.println("Connection with player " + playerChoice.name() + " closed.");
        }
    }

    private void sendMessage(BufferedWriter writer, String msg) throws IOException {
        writer.write(msg);
        writer.newLine();
        writer.flush();
    }
}
