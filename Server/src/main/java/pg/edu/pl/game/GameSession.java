package pg.edu.pl.game;

import pg.edu.pl.entities.Player;
import pg.edu.pl.game_mechanics.Game;
import pg.edu.pl.player.PlayerHandler;
import pg.edu.pl.utils.Player_choice;

import java.io.IOException;
import java.net.Socket;

public class GameSession implements Runnable {
    private final Socket player1Socket;
    private final Socket player2Socket;
    private final Game game;
    private boolean running = true;

    public GameSession(Socket player1Socket, Socket player2Socket) {
        this.player1Socket = player1Socket;
        this.player2Socket = player2Socket;
        this.game = new Game();
        game.printOpponentView(game.getPlayer_1().getBoard());
    }

    @Override
    public void run() {
        try {
            while (running) {
                PlayerHandler handler1 = new PlayerHandler(player1Socket, Player_choice.PLAYER_ONE, game);
                PlayerHandler handler2 = new PlayerHandler(player2Socket, Player_choice.PLAYER_TWO, game);

                new Thread(handler1).start();
                new Thread(handler2).start();

                while (running && !player1Socket.isClosed() && !player2Socket.isClosed()) {
                    // Keep alive
                    Thread.sleep(100);
                }
            }
        } catch (Exception e) {
            System.err.println("Error in game session: " + e.getMessage());
        } finally {
            stop();
        }
    }

    public void stop() {
        System.out.println("Stopping game session...");
        running = false;

        try {
            if (player1Socket != null && !player1Socket.isClosed()) {
                player1Socket.close();
            }
            if (player2Socket != null && !player2Socket.isClosed()) {
                player2Socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing sockets in game session: " + e.getMessage());
        }
    }
}
