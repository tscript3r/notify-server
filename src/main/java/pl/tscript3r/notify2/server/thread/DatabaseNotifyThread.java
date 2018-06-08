package pl.tscript3r.notify2.server.thread;

import java.net.URISyntaxException;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import pl.tscript3r.notify2.server.connectivity.DBCommunicator;
import pl.tscript3r.notify2.server.email.EmailExceptionSender;

public class DatabaseNotifyThread extends NotifyThread {

	private int interval = 2500;
	private DBCommunicator dbCommunicator = DBCommunicator.getInstance();

	public DatabaseNotifyThread() {
		super("");
		
		// TODO: implement
		//dbThread.setInterval(new Integer(properties.getProperty("database.thread.interval", "5000")));
		log = Logger.getLogger(DatabaseNotifyThread.class.getName());
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int inverval) {
		this.interval = inverval;
	}

	@Override
	public void run() {
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
			EmailExceptionSender.sendException(e.getStackTrace().toString());
		} catch (URISyntaxException e) {
			e.printStackTrace();
			log.error("Database task read error: " + e.getMessage());
			EmailExceptionSender.sendException(e.getStackTrace().toString());
		} catch (InterruptedException e) {
			log.info("Database thread stoppted");
			EmailExceptionSender.sendException(e.getStackTrace().toString());
		}
	}

}
