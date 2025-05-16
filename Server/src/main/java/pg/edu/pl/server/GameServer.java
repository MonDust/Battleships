package pg.edu.pl.server;

import pg.edu.pl.game.GameHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for running the Game server, with game sessions.
 * If it detects two players it will start the game.
 */
public class GameServer {
    private final GameHandler handler = new GameHandler();
    private boolean running = true;
    private static final Logger logger = Logger.getLogger(GameServer.class.getName());

    /**
     * Constructor for the class.
     */
    public GameServer() {}

    /**
     * Start the server - initialize it.
     * @param host - server address/host
     * @param backlog - the backlog, max number of concurrent players
     * @param port - server port
     */
    public void start(String host, int backlog, int port) {
        try (ServerSocket serverSocket = new ServerSocket(port, backlog, InetAddress.getByName(host))) {
            logger.log(Level.INFO,"Server started on port " + port);

            new Thread(handler).start();

            // shutting down server
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    logger.log(Level.INFO,"Server shutting down...");
                    stopServer(serverSocket);
                } catch (IOException e) {
                    logger.log(Level.WARNING,"Error during server shutdown: " + e.getMessage());
                }
            }));

            // manual shutdown command
            new Thread(() -> {
                try (Scanner scanner = new Scanner(System.in)) {
                    while (true) {
                        String input = scanner.nextLine();
                        if ("shutdown".equalsIgnoreCase(input)) {
                            try {
                                logger.log(Level.INFO,"Server shutting down...");
                                stopServer(serverSocket);
                            } catch (IOException e) {
                                logger.log(Level.WARNING,"Error during server shutdown: " + e.getMessage());
                            }
                            break;
                        }
                    }
                }
            }).start();

            while (running) {
                try {
                    // wait for a player
                    Socket clientSocket = serverSocket.accept();
                    logger.log(Level.INFO,"New client connected");
                    // add to waiting list (game handler)
                    handler.addPlayer(clientSocket);
                    // TODO - Maybe send info ??
                } catch (IOException e) {
                    logger.log(Level.WARNING,"Error accepting connection: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            logger.log(Level.WARNING,"Error starting the server 1: " + e.getMessage());
        }
    }

    /**
     * Safely stop the server
     * @param serverSocket the socket to close
     * @throws IOException the exception when the safe closing of server is not possible
     */
    private void stopServer(ServerSocket serverSocket) throws IOException {
        running = false;
        serverSocket.close(); // Close the server socket
        // Stop all the sessions
        handler.stop();
        logger.log(Level.INFO,"Server stopped successfully.");
    }
}
