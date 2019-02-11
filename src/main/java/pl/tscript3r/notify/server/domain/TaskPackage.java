package pl.tscript3r.notify.server.domain;

public class TaskPackage extends Package {

    public TaskPackage(int taskId, long hostId, String url, Recipient recipient) {
        super(taskId, hostId, url, recipient);
    }

}
