package pl.tscript3r.notify.server.thread;

import org.apache.log4j.Logger;
import org.jsoup.HttpStatusException;
import pl.tscript3r.notify.server.dataflow.TaskContainer;
import pl.tscript3r.notify.server.domain.TaskPackage;
import pl.tscript3r.notify.server.email.EmailExceptionSender;
import pl.tscript3r.notify.server.parser.list.OLXParser;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unlikely-arg-type")
class OLXNotifyThread extends NotifyThread {

    private final int BETWEEN_INTERVAL = 5000;
    private final int REPEAT_INTERVAL = 15000;

    private final static String HOST = "olx.pl";
    private final List<OLXParser> parsersList = new ArrayList<>();
    private List<TaskPackage> tasksList = new ArrayList<>();

    OLXNotifyThread() {
        super(HOST);
        log = Logger.getLogger(OLXNotifyThread.class.getName());
    }

    private void updateTasks() {
        TaskContainer container = TaskContainer.getInstance();
        tasksList = container.getTasks(host.hashCode());
    }

    private void updateParsers() {
        if (!tasksList.isEmpty())
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
            for (OLXParser parser : parsersList) {
                Thread.sleep(BETWEEN_INTERVAL);
                parser.parse();
            }
    }

    private void clear() {
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

                if (!tasksList.isEmpty())
                    parseTasks();

                Thread.sleep(REPEAT_INTERVAL);
            }

        } catch (Exception e) {
            if (e instanceof HttpStatusException) {
                String message = e.getMessage();
                if (message.indexOf("Status=503") > 0) {
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
            EmailExceptionSender.sendException(e.getLocalizedMessage());
        }
    }

}
