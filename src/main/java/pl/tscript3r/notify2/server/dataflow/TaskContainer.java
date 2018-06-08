package pl.tscript3r.notify2.server.dataflow;

import java.util.ArrayList;
import java.util.List;

import pl.tscript3r.notify2.server.domain.TaskPackage;

public class TaskContainer {
	
	private static TaskContainer taskContainer = new TaskContainer();
	private List<TaskPackage> tasksList = new ArrayList<TaskPackage>();

	private TaskContainer() {
	}

	public synchronized static TaskContainer getInstance() {
		return taskContainer;
	}

	public synchronized List<TaskPackage> getTasks(int hostId) {
		List<TaskPackage> result = new ArrayList<TaskPackage>();
		if (tasksList.size() > -1)
			for (int i = 0; i < tasksList.size(); i++)
				if (tasksList.get(i).getHostId() == hostId)
					result.add(tasksList.get(i));
		return result;
	}

	public synchronized void deleteTask(int taskId) {
		if (tasksList.size() > -1)
			for (int i = 0; i < tasksList.size(); i++)
				if (tasksList.get(i).getId() == taskId)
					tasksList.remove(i);
	}

	public synchronized void addTask(TaskPackage task) {
		tasksList.add(task);
	}

	public synchronized Boolean isTask(int taskId) {
		if(tasksList.size() > -1)
			for(int i = 0; i < tasksList.size(); i++)
				if(tasksList.get(i).getId() == taskId)
					return true;
		return false;
	}
	
}
