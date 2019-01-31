package pl.tscript3r.notify2.server.dataflow;

import pl.tscript3r.notify2.server.domain.TaskPackage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskContainer {

    private final static TaskContainer taskContainer = new TaskContainer();
    private final List<TaskPackage> tasksList = new ArrayList<>();

    private TaskContainer() {
    }

    public static TaskContainer getInstance() {
        return taskContainer;
    }

    public synchronized List<TaskPackage> getTasks(int hostId) {
        return tasksList.stream()
                .filter(task -> task.getHostId() == hostId)
                .collect(Collectors.toList());
    }

    public synchronized void deleteTask(int taskId) {
        if (!tasksList.isEmpty())
            tasksList.stream()
                    .filter(task -> task.getId() == taskId)
                    .forEach(tasksList::remove);
    }

    public synchronized void addTask(TaskPackage task) {
        tasksList.add(task);
    }

    public synchronized Boolean isTask(int taskId) {
        if (!tasksList.isEmpty())
            for (TaskPackage taskPackage : tasksList) {
                if (taskPackage.getId() == taskId)
                    return true;
            }
        return false;
    }

}
