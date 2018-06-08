package pl.tscript3r.notify2.server.factory;

import java.net.URISyntaxException;

import pl.tscript3r.notify2.server.domain.Recipient;
import pl.tscript3r.notify2.server.domain.TaskPackage;
import pl.tscript3r.notify2.server.utility.HostnameExtractor;

public class TaskFactory {
	
	public static synchronized TaskPackage getTaskPackageInstance(int taskId, String url, Recipient recipient) throws URISyntaxException {
		return new TaskPackage(taskId, HostnameExtractor.getDomainName(url).hashCode(), url, recipient);
	}
	
}
