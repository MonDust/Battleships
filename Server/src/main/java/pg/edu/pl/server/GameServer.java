package pg.edu.pl.server;

import pg.edu.pl.game.GameHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class GameServer {
    private final GameHandler handler = new GameHandler();
    private boolean running = true;

    public void start(String host, int backlog, int port) {
        System.out.println("Starting game server... ");
        try (ServerSocket serverSocket = new ServerSocket(port, backlog, InetAddress.getByName(host))) {
            System.out.println("Server started on port " + port);

            new Thread(handler).start();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    System.out.println("Server shutting down...");
                    stopServer(serverSocket);
                } catch (IOException e) {
                    System.err.println("Error during server shutdown: " + e.getMessage());
                }
            }));

            // Manual shutdown command
            new Thread(() -> {
                try (Scanner scanner = new Scanner(System.in)) {
                    while (true) {
                        String input = scanner.nextLine();
                        if ("shutdown".equalsIgnoreCase(input)) {
                            try {
                                System.out.println("Server shutting down...");
                                stopServer(serverSocket);
                            } catch (IOException e) {
                                System.err.println("Error during server shutdown: " + e.getMessage());
                            }
                            break;
                        }
                    }
                }
            }).start();

            while (running) {
                try {
                    // Wait for a player
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected");
                    handler.addPlayer(clientSocket);
                } catch (IOException e) {
                    System.err.println("Error accepting connection: " + e.getMessage());
                    // Maybe send info ??
                }
            }
        } catch (IOException e) {
            System.err.println("Error starting the server 1: " + e.getMessage());
        }
    }

    private void stopServer(ServerSocket serverSocket) throws IOException {
        running = false;
        serverSocket.close(); // Close the server socket
        handler.stop(); // Assuming GameHandler has a stop() method to stop any active game sessions
        System.out.println("Server stopped successfully.");
    }
}
