package pg.edu.pl;

import pg.edu.pl.utils.loggersetup.LoggerSetup;
import pg.edu.pl.server.GameServer;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ServerMain {
    private String host;
    private int port;
    private int backlog;
    private static final Logger logger = Logger.getLogger(ServerMain.class.getName());

    /**
     * Load configuration file.
     * 1. game.server.address
     * 2. game.server.port
     * 3. game.server.backlog
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
            this.backlog = Integer.parseInt(properties.getProperty("game.server.backlog", "50"));
        } catch (IOException e) {
            logger.log(Level.WARNING,"Error loading configuration file: " + fileName, e);
        }
    }

    /**
     * Start the Server
     * @param args command line arguments
     */
    public static void main(String[] args) {
        LoggerSetup.setup();
        ServerMain mainApp = new ServerMain();
        // configure arguments for server - file available in Gameplay
        mainApp.loadConfig("config.properties");

        logger.log(Level.INFO, "Server starting... -> host: " + mainApp.host + ", port: " + mainApp.port + ", backlog: " + mainApp.backlog);
        new GameServer().start(mainApp.host, mainApp.backlog, mainApp.port);
    }
}