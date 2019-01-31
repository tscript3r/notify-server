package pl.tscript3r.notify2.server;

import org.apache.log4j.Logger;
import pl.tscript3r.notify2.server.connectivity.DBCommunicator;
import pl.tscript3r.notify2.server.constants.MainSettings;
import pl.tscript3r.notify2.server.thread.LifeSupportThread;
import pl.tscript3r.notify2.server.utility.PropertiesLoader;

import java.sql.SQLException;

public class NotifyServer {

    private final static Logger log = Logger.getLogger(NotifyServer.class.getName());
    private final DBCommunicator dbCommunicator = DBCommunicator.INSTANCE;
    private final LifeSupportThread supportThread = new LifeSupportThread();

    public NotifyServer() throws Exception {
        PropertiesLoader properties = new PropertiesLoader(MainSettings.PROPERTIES_FILE);
        dbCommunicator.setHostname(properties.getProperty("database.host"));
        dbCommunicator.setPort(new Integer(properties.getProperty("database.port", "3306")));
        dbCommunicator.setUsername(properties.getProperty("database.username"));
        dbCommunicator.setPassword(properties.getProperty("database.password"));
        dbCommunicator.setDatabaseName(properties.getProperty("database.dbname"));
        log.info("Notify server configurations loaded");
    }

    public String dbConnect() throws Exception {
        try {
            return dbCommunicator.connect();
        } catch (Exception e) {
            throw new SQLException("SQL connection failed: " + e.getMessage());
        }
    }

    public void dbDisconnect() throws Exception {
        try {
            dbCommunicator.close();
            log.info("DB connection has been closed");
        } catch (Exception e) {
            throw new SQLException("SQL connection failed: " + e.getMessage());
        }
    }

    public void start() {
        supportThread.startThreads();
        supportThread.start();
    }

    public void stop() {
        supportThread.stopThreads();
        supportThread.stop();
    }

}
