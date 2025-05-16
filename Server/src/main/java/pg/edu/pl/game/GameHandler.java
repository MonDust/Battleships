package pg.edu.pl.game;

import lombok.extern.slf4j.Slf4j;

import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The handler of all the games (sessions).
 */
public class GameHandler  implements Runnable {

    // TODO - check if the waiting player queue works correctly

    private final BlockingQueue<Socket> waitingPlayers = new LinkedBlockingQueue<>();
    /**
     * List of active game sessions.
     */
    private final List<GameSession> activeSessions = new CopyOnWriteArrayList<>();
    private boolean running = true;

    private static final Logger logger = Logger.getLogger(GameHandler.class.getName());

    /**
     * Add player to the queue.
     * @param socket the socket of the player.
     */
    public void addPlayer(Socket socket) {
        logger.log(Level.INFO,"Adding player: " + socket);
        boolean added = waitingPlayers.offer(socket);
        logger.log(Level.INFO,"Waiting player added: " + added);
        if (!added) {
            logger.log(Level.WARNING, "Failed to add player to the waiting queue. Queue might be full.");
            // TODO - reject the player
        }
    }

    /**
     * Running the game handler.
     */
    @Override
    public void run() {
        while(running) {
            try {
                Socket player1 = waitingPlayers.take();
                Socket player2 = waitingPlayers.take();

                logger.log(Level.INFO,"Starting new game with two players...");

                // Start a new game session
                GameSession session = new GameSession(player1, player2);
                new Thread(session).start();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.log(Level.WARNING,"GameHandler interrupted");
                break;
            }
        }
    }

    /**
     * Close client connections.
     */
    public void stop() {
        logger.log(Level.INFO,"Stopping the game handler...");
        running = false;
        stopSessions();
    }

    /**
     * Close all the sessions.
     */
    private void stopSessions() {
        logger.log(Level.INFO,"Stopping all active game sessions...");
        // Iterate through all active game sessions and stop them
        for (GameSession session : activeSessions) {
            session.stop();
        }
        activeSessions.clear();
        // TODO - inform all the clients - also without sessions ?
    }

}
