package pl.tscript3r.notify2.server.thread;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pl.tscript3r.notify2.server.email.EmailExceptionSender;

public class LifeSupportThread implements Runnable {

	private static Logger log = Logger.getLogger(LifeSupportThread.class.getName());
	private Thread thread;
	private List<NotifyThread> supportedThreadsList = new ArrayList<NotifyThread>();
	private long rescueTimeFrame;

	public LifeSupportThread() throws IOException, ParseException {
		supportedThreadsList.add(new DatabaseNotifyThread());
		supportedThreadsList.add(new OLXNotifyThread());
		supportedThreadsList.add(new EmailNotifyThread());
	}

	public void start() {
		stop();
		thread = new Thread(this);
		thread.start();
	}

	public void stop() {
		if (thread != null)
			if (thread.isAlive())
				thread.interrupt();
	}

	public void startThreads() {
		log.info("Starting slave threads");
		supportedThreadsList.forEach(notifyThread -> notifyThread.start());
	}

	public void stopThreads() {
		log.info("Stopping slave threads");
		supportedThreadsList.forEach(notifyThread -> notifyThread.stop());
	}

	private void restartThread(NotifyThread notifyThread) {
		notifyThread.stop();
		notifyThread.start();
	}

	private Boolean isRescueTimeFrame(NotifyThread thread) {
		long currentTime = System.currentTimeMillis();
		if (thread.getLastFailureTime() == 0)
			return true;
		return (thread.getLastFailureTime() + (rescueTimeFrame * 1000) < currentTime) ? true : false;
	}

	@Override
	public void run() {
		thread.setName("LifeSupportThread");
		log.info("Life Support thread starts");
		while (!thread.isInterrupted()) {
			try {
				Thread.sleep(5000);
				supportedThreadsList.forEach(notifyThread -> {
					if (!notifyThread.isAlive())
						if (isRescueTimeFrame(notifyThread)) {
							log.warn(notifyThread.getThreadName() + " crashed. Restarting...");
							restartThread(notifyThread);
							notifyThread.setLastFailureTime(System.currentTimeMillis());
						} else {
							log.error(notifyThread.getThreadName()
									+ " crashed second time in a short timeframe. All threads will be stopped.");
							stopThreads();
							EmailExceptionSender.sendException(notifyThread.getThreadName()
									+ " crashed second time in a short timeframe. All threads will be stopped.");
							thread.interrupt();
						}
				});
			} catch (InterruptedException e) {
				log.info("Life support thread stopped");
			}
		}
	}

}
