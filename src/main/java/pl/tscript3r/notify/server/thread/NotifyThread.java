package pl.tscript3r.notify.server.thread;

import org.apache.log4j.Logger;

abstract class NotifyThread implements Runnable {

    final String host;
    Thread thread;
    private long lastFailureTime = 0;
    static Logger log;

    NotifyThread(String host) {
        this.host = host;
    }

    long getLastFailureTime() {
        return lastFailureTime;
    }

    void setLastFailureTime(long lastFailureTime) {
        this.lastFailureTime = lastFailureTime;
    }

    Boolean isAlive() {
        return (thread != null) && thread.isAlive();
    }

    String getThreadName() {
        return thread.getName();
    }

    void start() {
        stop();
        thread = new Thread(this);
        thread.start();
    }

    void stop() {
        if (thread != null)
            if (thread.isAlive())
                thread.interrupt();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null)
            return (obj.hashCode() == hashCode());
        return false;
    }

    @Override
    public int hashCode() {
        return host.hashCode();
    }

}