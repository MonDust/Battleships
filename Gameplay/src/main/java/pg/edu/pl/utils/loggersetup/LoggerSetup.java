package pg.edu.pl.utils.loggersetup;

import java.util.logging.*;

public class LoggerSetup {
    public static void setup() {
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();

        for (Handler handler : handlers) {
            if (handler instanceof ConsoleHandler) {
                handler.setFormatter(new ColorFormatter());
            }
        }
    }
}
