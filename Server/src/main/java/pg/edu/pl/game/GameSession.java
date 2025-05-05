package pg.edu.pl.game;

import pg.edu.pl.entities.Player;
import pg.edu.pl.game_mechanics.Game;

import java.io.IOException;
import java.net.Socket;

public class GameSession implements Runnable {
    private final Socket player1Socket;
    private final Socket player2Socket;
    private Game game;
    private boolean running = true;

    public GameSession(Socket player1Socket, Socket player2Socket) {
        this.player1Socket = player1Socket;
        this.player2Socket = player2Socket;
        this.game = new Game();
    }

    @Override
    public void run() {
        try {
            while (running) {

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
