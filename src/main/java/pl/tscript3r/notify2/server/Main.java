package pl.tscript3r.notify2.server;

import org.apache.log4j.Logger;
import pl.tscript3r.notify2.server.email.EmailExceptionSender;

public class Main {

    private final static Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) {

        log.info("Initialization");
        try {
            NotifyServer notifyServer = new NotifyServer();
            log.info("Trying to connect with DB");
            log.info("Connection success with DB: " + notifyServer.dbConnect());
            log.info("Starting threads...");
            notifyServer.start();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getClass().getName() + ": " + e.getMessage());
            EmailExceptionSender.sendException(e.getLocalizedMessage());
            System.exit(0);
        }

    }

}
