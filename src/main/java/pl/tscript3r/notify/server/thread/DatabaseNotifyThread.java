package pl.tscript3r.notify.server.thread;

import org.apache.log4j.Logger;
import pl.tscript3r.notify.server.connectivity.DBCommunicator;
import pl.tscript3r.notify.server.email.EmailExceptionSender;

import java.sql.SQLException;

class DatabaseNotifyThread extends NotifyThread {

    private final DBCommunicator dbCommunicator = DBCommunicator.INSTANCE;

    DatabaseNotifyThread() {
        super("");
        log = Logger.getLogger(DatabaseNotifyThread.class);
    }

    @Override
    public void run() {
        int interval = 2500;
        thread.setName("DBThread");
        log.info("Database thread starts");
        try {

            while (!thread.isInterrupted()) {
                dbCommunicator.updateTasks();
                Thread.sleep(interval);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Database error: " + e.getMessage());
            EmailExceptionSender.sendException(e.getLocalizedMessage());
        } catch (InterruptedException e) {
            log.info("Database thread stopped");
            EmailExceptionSender.sendException(e.getLocalizedMessage());
        }
    }

}
