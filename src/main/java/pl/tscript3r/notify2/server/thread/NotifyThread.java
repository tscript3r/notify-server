package pl.tscript3r.notify2.server.thread;

import org.apache.log4j.Logger;

public abstract class NotifyThread implements Runnable, NotifyThreadInterface {

	protected String host;
	protected Thread thread;
	protected long lastFailureTime = 0;
	protected static Logger log;

	public NotifyThread(String host) {
		this.host = host;
	}

	public long getLastFailureTime() {
		return lastFailureTime;
	}

	public void setLastFailureTime(long lastFailureTime) {
		this.lastFailureTime = lastFailureTime;
	}

	public Boolean isAlive() {
		return (thread != null) ? thread.isAlive() : false;
	}
	
	public String getThreadName() {
		return thread.getName();
	}

	@Override
	public void start() {
		stop();
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void stop() {
		if (thread != null)
			if (thread.isAlive())
				thread.interrupt();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null)
			return (obj.hashCode() == hashCode()) ? true : false;
		return false;
	}

	@Override
	public int hashCode() {
		return host.hashCode();
	}

}