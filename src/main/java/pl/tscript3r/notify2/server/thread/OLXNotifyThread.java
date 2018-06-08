package pl.tscript3r.notify2.server.thread;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.HttpStatusException;

import pl.tscript3r.notify2.server.dataflow.TaskContainer;
import pl.tscript3r.notify2.server.domain.TaskPackage;
import pl.tscript3r.notify2.server.email.EmailExceptionSender;
import pl.tscript3r.notify2.server.parser.list.OLXParser;

@SuppressWarnings("unlikely-arg-type")
public class OLXNotifyThread extends NotifyThread {
	private final static String HOST = "olx.pl";
	private List<TaskPackage> tasksList = new ArrayList<TaskPackage>();
	private List<OLXParser> parsersList = new ArrayList<OLXParser>();
	private int betweenInterval = 500;
	private int repeatInterval = 1500;

	public OLXNotifyThread() {
		super(HOST);
		log = Logger.getLogger(OLXNotifyThread.class.getName());
	}

	private void updateTasks() throws URISyntaxException {
		TaskContainer container = TaskContainer.getInstance();
		tasksList = container.getTasks(host.hashCode());
	}

	private void updateParsers() {
		if(!tasksList.isEmpty())
			tasksList.forEach(task -> {
				if (!parsersList.contains(task)) {
					OLXParser parser = new OLXParser(task.getUrl(), task.getId(), task.getRecipient());
					parsersList.add(parser);
					log.info("Created new parser for ID:\t" + task.getId());
				}
			});

		for (int i = 0; i < parsersList.size(); i++)
			if (!tasksList.contains(parsersList.get(i)))
				log.info("Removed unused parser for ID:\t" + parsersList.remove(i).getTaskId());
	}

	private void parseTasks() throws ParseException, IOException, InterruptedException {
		if (!parsersList.isEmpty())
			for (int i = 0; i < parsersList.size(); i++) {
				Thread.sleep(betweenInterval);
				parsersList.get(i).parse();
			}
	}

	public void clear() {
		tasksList.clear();
		parsersList.clear();
	}

	@Override
	public void stop() {
		clear();
		super.stop();
	}

	@Override
	public void run() {
		thread.setName("OLXThread");
		try {
			log.info("OLX thread starts");
			while (!thread.isInterrupted()) {
				updateTasks();
				updateParsers();
				if (!tasksList.isEmpty()) {
					parseTasks();
				}
				Thread.sleep(repeatInterval);
			}
		} catch (Exception e) {
			if(e instanceof HttpStatusException) {
				String message = e.getMessage();
				if(message.indexOf("Status=503") > 0) {
					log.warn("Failed to download HTTP content: " + message);
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						log.info("OLX Thread stopped");
					}
					run();
				} else
					e.printStackTrace();
			} else
				e.printStackTrace();
			EmailExceptionSender.sendException(e.getStackTrace().toString());
		}
	}

}
