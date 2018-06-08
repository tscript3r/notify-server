package pl.tscript3r.notify2.server;

import org.apache.log4j.Logger;

import pl.tscript3r.notify2.server.email.EmailExceptionSender;

public class Main {
	
	private static Logger log = Logger.getLogger(Main.class.getName());
	private static NotifyServer notifyServer;

	public static void main(String[] args) {
		log.info("Initialization");
		try {
			notifyServer = new NotifyServer();
			log.info("Testing DB connection...");
			log.info("Connection success with DB: " + notifyServer.dbConnect());
			log.info("Starting threads...");
			notifyServer.start();
			// notifyServer.dbDisconnect();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getClass().getName() + ": " + e.getMessage());
			EmailExceptionSender.sendException(e.getStackTrace().toString());
			System.exit(0);
		}
		
	}

}
