package pg.edu.pl.utils.loggersetup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class ColorFormatter extends Formatter {

    // colors
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String WHITE = "\u001B[37m";
    private static final String BLACK = "\u001B[30m";

    @Override
    public String format(LogRecord record) {
        String color;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy h:mm:ss a");

        if (record.getLevel() == Level.SEVERE) {
            color = RED;
        } else if (record.getLevel() == Level.WARNING) {
            color = YELLOW;
        } else if (record.getLevel() == Level.INFO) {
            color = BLACK;
        } else {
            color = WHITE;
        }

        String timestamp = dateFormat.format(new Date(record.getMillis()));
        String source = record.getSourceClassName() + " " + record.getSourceMethodName();

        if(record.getLevel() == Level.INFO) {
            return String.format("%s[%s] %s - %s%s\n",
                    color,
                    record.getLevel().getName(),
                    formatMessage(record),
                    timestamp,
                    RESET);
        }

        return String.format("%s%s -> %s\n[%s] %s%s\n",
                color,
                timestamp,
                source,
                record.getLevel().getName(),
                formatMessage(record),
                RESET);
    }
}
