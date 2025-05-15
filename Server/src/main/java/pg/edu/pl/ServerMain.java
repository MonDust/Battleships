package pg.edu.pl;

import pg.edu.pl.server.GameServer;

import java.io.IOException;
import java.util.Properties;


public class ServerMain {
    private String host;
    private int port;
    private int backlog;

    public void loadConfig(String fileName) {
        Properties properties = new Properties();
        try (var input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                System.err.println("Configuration file not found: " + fileName);
                return;
            }
            properties.load(input);
            this.host = properties.getProperty("game.server.address", "localhost");
            this.port = Integer.parseInt(properties.getProperty("game.server.port", "60009"));
            this.backlog = Integer.parseInt(properties.getProperty("game.server.backlog", "50"));
        } catch (IOException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ServerMain mainApp = new ServerMain();
        mainApp.loadConfig("config.properties");

        System.out.println("Server starting... : host" + mainApp.host + ",port: " + mainApp.port + ",backlog: " + mainApp.backlog);
        new GameServer().start(mainApp.host, mainApp.backlog, mainApp.port);
    }
}