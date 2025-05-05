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
        // System.out.println("Handling player");
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            System.out.println("Sending connection message to client...");
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
                        int x = message.get("x").getAsInt();
                        int y = message.get("y").getAsInt();
                        boolean result = game.doTurn(x, y);
                        sendMessage(writer, "TURN_RESULT: " + result);
                    }
                    case "PLACE_SHIP" -> {
                        int x = message.get("x").getAsInt();
                        int y = message.get("y").getAsInt();
                        boolean horizontal = message.get("horizontal").getAsBoolean();
                        int size = message.get("size").getAsInt();

                        // System.out.println("Ship: " + x + " " + y + " " + horizontal + " " + size);

                        IBoard board;
                        if (playerChoice == Player_choice.PLAYER_ONE) {
                            board = game.getPlayer_1().getBoard();
                        } else {
                            board = game.getPlayer_2().getBoard();
                        }

                        IShip ship = new Ship(size);

                        // System.out.println("Board size: " + board.getHeight() + " " + board.getWidth());
                        // System.out.println("Ship: " + ship.getSize());
                        boolean success = board.placeShip(ship, x, y, horizontal);
                        // System.out.println("Success: " + success);
                        game.printPlayerView(board);
                        game.printOpponentView(board);
                        sendMessage(writer, "PLACED: " + success);
                    }
                    case "PRINT_MY_BOARD" -> {
                        IBoard board = (playerChoice == Player_choice.PLAYER_ONE)
                                ? game.getPlayer_1().getBoard()
                                : game.getPlayer_2().getBoard();
                        sendMessage(writer, getBoardAsString(board));
                    }
                    case "EXIT" -> {
                        sendMessage(writer, "GOODBYE");
                        running = false;
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

    private String getBoardAsString(IBoard board) {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                Field f = board.getField(x, y);
                if (f.isRevealed()) {
                    if (f.getShip() != null && f.isHit()) sb.append("X ");
                    else sb.append("O ");
                } else {
                    sb.append(". ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
