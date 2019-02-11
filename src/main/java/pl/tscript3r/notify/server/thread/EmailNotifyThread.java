package pl.tscript3r.notify.server.thread;

import org.apache.log4j.Logger;
import pl.tscript3r.notify.server.constants.MainSettings;
import pl.tscript3r.notify.server.dataflow.AdQueue;
import pl.tscript3r.notify.server.domain.AdPackage;
import pl.tscript3r.notify.server.domain.Recipient;
import pl.tscript3r.notify.server.email.EmailContentGenerator;
import pl.tscript3r.notify.server.email.EmailExceptionSender;
import pl.tscript3r.notify.server.email.EmailSender;
import pl.tscript3r.notify.server.utility.PropertiesLoader;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

class EmailNotifyThread extends NotifyThread {

    private static final Logger log = Logger.getLogger(EmailNotifyThread.class);
    private EmailSender emailSender;
    private final AdQueue adQueue = AdQueue.getInstance();
    private final List<AdPackage> adsList = new ArrayList<>();
    private final List<AdPackage> usersAdsList = new ArrayList<>();
    private EmailContentGenerator emailContentGenerator;

    EmailNotifyThread() throws IOException, ParseException {
        super("");
        emailSender = new EmailSender();
        PropertiesLoader properties = new PropertiesLoader(MainSettings.PROPERTIES_FILE);
        emailContentGenerator = new EmailContentGenerator(properties.getProperty("email.body.file"),
                properties.getProperty("email.ad.file"));
    }

    private void collectAds() {
        while (!adQueue.isEmpty())
            adsList.add(adQueue.pop());
        if (!adsList.isEmpty())
            log.info("Collected " + adsList.size() + " ads to e-send");
    }

    private void collectUsersAds() {
        usersAdsList.clear();
        usersAdsList.add(adsList.remove(0));
        Recipient recipient = usersAdsList.get(0).getRecipient();

        adsList.stream()
                .filter(ad -> ad.getRecipient().equals(recipient))
                .forEach(usersAdsList::add);

        log.info("Got " + usersAdsList.size()
                + " ads to send for user ID: " + recipient.getUserId());
    }

    private String generateMessageContent() throws ParseException {
        String result = emailContentGenerator.getMessageContent(usersAdsList);
        log.info("Sending message with " + usersAdsList.size() + " ads for user ID: "
                + usersAdsList.get(0).getRecipient().getUserId());
        return result;
    }

    private String getRecipient() {
        return (!usersAdsList.isEmpty()) ?
                usersAdsList.get(0).getRecipient().getEmail() : "alek199202@gmail.com";
    }

    private void clear() {
        adsList.clear();
        usersAdsList.clear();
    }

    @Override
    public void stop() {
        clear();
        super.stop();
    }

    @Override
    public void run() {
        int interval = 30000;

        thread.setName("EmailThread");
        log.info("Email thread starts");
        try {
            while (!thread.isInterrupted()) {
                Thread.sleep(interval);
                adsList.clear();
                collectAds();
                if (!adsList.isEmpty())

                    while (!adsList.isEmpty()) {
                        collectUsersAds();
                        try {
                            emailSender.sendHtmlEmail(getRecipient(),
                                    "New ads - Notify", generateMessageContent());
                        } catch (MessagingException e) {
                            log.warn("Failed to send E-Mail message to client id: "
                                    + adsList.get(0).getRecipient().getUserId());
                            e.printStackTrace();
                        }
                    }
            }
        } catch (InterruptedException e) {
            log.info("E-Mail Thread stopped");
        } catch (ParseException e) {
            log.error("Could not create the E-Mail message content");
            e.printStackTrace();
            EmailExceptionSender.sendException(e.getLocalizedMessage());
        }
    }

}
