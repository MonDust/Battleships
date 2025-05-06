package pg.edu.pl;

import pg.edu.pl.client.GameClient;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientMain {
    private String host;
    private int port;
    private static final Logger logger = Logger.getLogger(ClientMain.class.getName());

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
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error loading configuration file: " + fileName, e);
        }
    }

    public static void main(String[] args) {
        ClientMain mainApp = new ClientMain();
        mainApp.loadConfig("config.properties");

        System.out.println("Server starting... -> host: " + mainApp.host + ", port: " + mainApp.port);
        new GameClient().start(mainApp.host, mainApp.port);
    }
}