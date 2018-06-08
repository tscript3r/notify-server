package pl.tscript3r.notify2.server.domain;

import pl.tscript3r.notify2.server.domain.Recipient;

public class TaskPackage extends Package {
	
	public TaskPackage(int taskId, long hostId, String url, Recipient recipient) {
		super(taskId, hostId, url, recipient);
	}
	
}
