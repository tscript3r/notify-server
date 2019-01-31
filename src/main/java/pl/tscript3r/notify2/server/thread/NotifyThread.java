package pl.tscript3r.notify2.server.thread;

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

    public Boolean isAlive() {
        return (thread != null) && thread.isAlive();
    }

    public String getThreadName() {
        return thread.getName();
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