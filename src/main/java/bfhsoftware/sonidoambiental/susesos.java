package bfhsoftware.sonidoambiental;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class susesos {
    private static Logger logger=null;
    private static FileHandler fh=null;


    public susesos() {
        //logger = Logger.getAnonymousLogger();
        logger = Logger.getLogger(susesos.class.getName());

        configure();
    }

    private void configure() {
        try {
            String logsDirectoryFolder = "susesos";
            Files.createDirectories(Paths.get(logsDirectoryFolder));
            fh = new FileHandler(logsDirectoryFolder + File.separator + getCurrentTimeString() + ".log", true);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        addCloseHandlersShutdownHook();
    }

    private void addCloseHandlersShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
        {
            // Close all handlers to get rid of empty .LCK files
            for (Handler handler : logger.getHandlers()) {
                handler.close();
            }
        }));
    }

    private String getCurrentTimeString() {

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(new Date());
    }

    public void log(Exception exception) {
        logger.log(Level.SEVERE, "", exception);
    }

}
