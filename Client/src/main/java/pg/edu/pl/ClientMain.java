package pg.edu.pl;

import pg.edu.pl.client.GameClient;
import pg.edu.pl.utils.loggersetup.LoggerSetup;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main for running the client.
 * Make sure you can run multiple instances for the Battleships to work.
 * To change properties (host, port) go to the configuration.properties file located in resources in the Gameplay module.
 */
public class ClientMain {
    private String host;
    private int port;
    private static final Logger logger = Logger.getLogger(ClientMain.class.getName());

    /**
     * Load configuration file.
     * 1. game.server.address
     * 2. game.server.port
     * If not available will be set to default.
     * @param fileName - the name of the file with configurations.
     */
    public void loadConfig(String fileName) {
        Properties properties = new Properties();
        try (var input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                logger.log(Level.WARNING,"Configuration file not found: " + fileName);
                return;
            }
            properties.load(input);
            this.host = properties.getProperty("game.server.address", "localhost");
            this.port = Integer.parseInt(properties.getProperty("game.server.port", "60009"));
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error loading configuration file: " + fileName, e);
        }
    }

    /**
     * Start the Client.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        LoggerSetup.setup();
        ClientMain mainApp = new ClientMain();
        // configure arguments for client - file available in Gameplay
        mainApp.loadConfig("config.properties");

        logger.log(Level.INFO,"Server starting... -> host: " + mainApp.host + ", port: " + mainApp.port);
        new GameClient().start(mainApp.host, mainApp.port);
    }
}