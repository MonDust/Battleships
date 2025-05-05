package pg.edu.pl.game;

import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class GameHandler  implements Runnable {
    private final BlockingQueue<Socket> waitingPlayers = new LinkedBlockingQueue<>();
    /**
     * List of active game sessions.
     */
    private final List<GameSession> activeSessions = new CopyOnWriteArrayList<>();
    private boolean running = true;

    public void addPlayer(Socket socket) {
        System.out.println("Adding player: " + socket);
        boolean added = waitingPlayers.offer(socket);
        System.out.println("Waiting player added: " + added);
        if (!added) {
            System.err.println("Failed to add player to the waiting queue. Queue might be full.");
            // reject the player
        }
    }

    @Override
    public void run() {
        while(running) {
            try {
                Socket player1 = waitingPlayers.take();
                Socket player2 = waitingPlayers.take();

                System.out.println("Starting new game with two players...");

                // Start a new game session
                GameSession session = new GameSession(player1, player2);
                new Thread(session).start();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("GameHandler interrupted");
                break;
            }
        }
    }

    /**
     * Close client connections.
     */
    public void stop() {
        System.out.println("Stopping the game handler...");
        running = false;
        stopSessions();
    }

    private void stopSessions() {
        System.out.println("Stopping all active game sessions...");

        // Iterate through all active game sessions and stop them
        for (GameSession session : activeSessions) {
            session.stop();
        }

        activeSessions.clear();
    }

}
