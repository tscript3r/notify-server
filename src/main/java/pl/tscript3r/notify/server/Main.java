package pl.tscript3r.notify.server;

import org.apache.log4j.Logger;
import pl.tscript3r.notify.server.email.EmailExceptionSender;

public class Main {

    private final static Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) {

        log.info("Initialization");
        try {
            NotifyServer notifyServer = new NotifyServer();
            log.info("Testing database connection");
            log.info("Success: " + notifyServer.dbConnect());
            log.info("Starting threads...");
            notifyServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
