package pl.tscript3r.notify.server.thread;

import org.apache.log4j.Logger;
import pl.tscript3r.notify.server.email.EmailExceptionSender;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ManagerThread implements Runnable {

    private static final int REFRESH_TIME = 5000;

    private final static Logger log = Logger.getLogger(ManagerThread.class.getName());
    private final List<NotifyThread> threadsList = new ArrayList<>();
    private Thread thread;

    public ManagerThread() throws IOException, ParseException {
        threadsList.add(new DatabaseNotifyThread());
        threadsList.add(new OLXNotifyThread());
        threadsList.add(new EmailNotifyThread());
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
        threadsList.forEach(NotifyThread::start);
    }

    public void stopThreads() {
        log.info("Stopping slave threads");
        threadsList.forEach(NotifyThread::stop);
    }

    private void restartThread(NotifyThread notifyThread) {
        notifyThread.stop();
        notifyThread.start();
    }

    private Boolean isInRescueTimeFrame(NotifyThread thread) {
        long rescueTimeFrame = 120000L;

        long currentTime = System.currentTimeMillis();
        if (thread.getLastFailureTime() == 0)
            return true;
        return (thread.getLastFailureTime() + (rescueTimeFrame * 1000) < currentTime);
    }

    @Override
    public void run() {
        thread.setName("ManagerThread");
        log.info("Life Support thread starts");
        while (!thread.isInterrupted()) {
            try {
                Thread.sleep(REFRESH_TIME);
                threadsList.forEach(notifyThread -> {
                    if (!notifyThread.isAlive())
                        if (isInRescueTimeFrame(notifyThread)) {
                            log.warn(notifyThread.getThreadName() + " crashed. Restarting...");
                            restartThread(notifyThread);
                            notifyThread.setLastFailureTime(System.currentTimeMillis());
                        } else {
                            log.error(notifyThread.getThreadName()
                                    + " crashed second time in a short time frame. " +
                                    "All threads will be stopped.");
                            stopThreads();
                            EmailExceptionSender.sendException(notifyThread.getThreadName()
                                    + " crashed second time in a short time frame. " +
                                    "All threads will be stopped.");
                            thread.interrupt();
                        }
                });
            } catch (InterruptedException e) {
                log.info("Life support thread stopped");
            }
        }
    }

}
