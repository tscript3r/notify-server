package pl.tscript3r.notify2.server.dataflow;

import java.util.ArrayDeque;

import org.apache.log4j.Logger;

import pl.tscript3r.notify2.server.domain.AdPackage;

public class AdQueue  extends ArrayDeque<AdPackage> {
	
	private static Logger log = Logger.getLogger(AdQueue.class.getName());
	private static final long serialVersionUID = 1L;
	
	private static AdQueue adQueue = new AdQueue();
	
	private AdQueue() {	
	}
	
	public synchronized static AdQueue getInstance() {
		return adQueue;
	}
	
	@Override
	public synchronized boolean add(AdPackage e) {
		log.info("Added new Ad. ID: " + e.getId() + " / " + e.getUrl());
		return super.add(e);
	}
	
	@Override
	public synchronized boolean isEmpty() {
		return super.isEmpty();
	}
	
	@Override
	public synchronized AdPackage pop() {
		return super.pop();
	}
	
	@Override
	public synchronized AdPackage getFirst() {
		return super.getFirst();
	}
	
	@Override
	public synchronized int size() {
		return super.size();
	}
	
}