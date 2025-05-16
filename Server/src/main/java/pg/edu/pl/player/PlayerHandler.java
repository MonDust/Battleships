package pg.edu.pl.player;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import pg.edu.pl.entities.Field;
import pg.edu.pl.entities.Player;
import pg.edu.pl.entities.Ship;
import pg.edu.pl.entities.interfaces.IBoard;
import pg.edu.pl.entities.interfaces.IShip;
import pg.edu.pl.game_mechanics.GUIs.CLIPrintingGame;
import pg.edu.pl.game_mechanics.Game;
import pg.edu.pl.game_mechanics.interfaces.IGame;
import pg.edu.pl.utils.Player_choice;

/**
 * Clas for handling player/client interactions with the server, during the game session.
 * One handler for each player.
 */
public class PlayerHandler implements Runnable {
    private final Socket socket;
    private final Player_choice playerChoice;
    private final IGame game;
    private final CLIPrintingGame CLIGame;
    private boolean running = true;

    private static final Logger logger = Logger.getLogger(PlayerHandler.class.getName());

    /**
     * Constructor for the class
     * @param socket the player/client socket
     * @param playerChoice the player assigment: either Player ONE or Player TWO
     * @param game the game (session)
     */
    public PlayerHandler(Socket socket, Player_choice playerChoice, IGame game) {
        this.socket = socket;
        this.playerChoice = playerChoice;
        this.game = game;
        this.CLIGame = new CLIPrintingGame(game);
    }

    // TODO - refractor the run
    // TODO - fix the player switch - getting ready: placing ships and so it is turn based

    /**
     * Running the player handler.
     */
    @Override
    public void run() {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            sendMessage(writer, "CONNECTED as " + playerChoice.name());
            logger.log(Level.INFO, "Connection established with player: " + playerChoice.name());

            while (running) {
                String input = reader.readLine();
                if (input == null) {
                    logger.log(Level.INFO, "Client disconnected: " + playerChoice.name());
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

                        logger.log(Level.INFO, "Game:" + game.getCurrentPlayer());

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
                        CLIGame.printPlayerView(board);
                        CLIGame.printOpponentView(opponentBoard);

                        game.switchPlayer();
                        logger.log(Level.INFO, "Player " + game.getCurrentPlayer().name() + " turn.");

                        sendMessage(writer, "TURN_RESULT: " + result);

                        // TODO - maybe notify the other player
                    }
                    case "PLACE_SHIP" -> {
                        int x = message.get("x").getAsInt();
                        int y = message.get("y").getAsInt();
                        boolean horizontal = message.get("horizontal").getAsBoolean();
                        int size = message.get("size").getAsInt();

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
                        CLIGame.printPlayerView(board);
                        CLIGame.printOpponentView(opponentBoard);
                        sendMessage(writer, "PLACED: " + success);
                    }
                    case "PRINT_MY_BOARD" -> {
                        IBoard board;
                        if (playerChoice == Player_choice.PLAYER_ONE) {
                            board = game.getPlayer_1().getBoard();
                        } else {
                            board = game.getPlayer_2().getBoard();
                        }
                        sendMessage(writer, CLIGame.getPlayerView(board));
                    }
                    case "PRINT_OPPONENT_BOARD" -> {
                        IBoard board;
                        if (playerChoice == Player_choice.PLAYER_TWO) {
                            board = game.getPlayer_1().getBoard();
                        } else {
                            board = game.getPlayer_2().getBoard();
                        }
                        sendMessage(writer, CLIGame.getOpponentView(board));
                    }
                    case "EXIT" -> {
                        sendMessage(writer, "GOODBYE");
                        running = false;
                    }
                    case "READY" -> {
                        // TODO - implement what happens when client sends that they are ready
                        //  TODO - this could be here not in game (the readiness of the player, or could stay like that)
                        game.getPlayer(playerChoice).setPlayerReady(true);
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
            logger.log(Level.WARNING,"Socket closed for player " + playerChoice.name() + ": " + e.getMessage());
        } catch (IOException e) {
            logger.log(Level.WARNING,"I/O exception: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {}
            logger.log(Level.INFO,"Connection with player " + playerChoice.name() + " closed.");
        }
    }

    /**
     * Send message to the player.
     * @param writer BufferedWriter
     * @param msg message to the player
     * @throws IOException - IO exception can be thrown
     */
    private void sendMessage(BufferedWriter writer, String msg) throws IOException {
        writer.write(msg);
        writer.newLine();
        writer.flush();
    }
}
