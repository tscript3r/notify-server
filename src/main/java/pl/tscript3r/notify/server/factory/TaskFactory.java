package pl.tscript3r.notify.server.factory;

import pl.tscript3r.notify.server.domain.Recipient;
import pl.tscript3r.notify.server.domain.TaskPackage;
import pl.tscript3r.notify.server.utility.HostnameExtractor;

public class TaskFactory {

    public static synchronized TaskPackage getTaskPackageInstance
            (int taskId, String url, Recipient recipient) {
        return new TaskPackage(taskId, HostnameExtractor.getDomainName(url).hashCode(), url, recipient);
    }

}
