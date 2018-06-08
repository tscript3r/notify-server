package pl.tscript3r.notify2.server.thread;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;

import pl.tscript3r.notify2.server.constants.MainSettings;
import pl.tscript3r.notify2.server.dataflow.AdQueue;
import pl.tscript3r.notify2.server.domain.AdPackage;
import pl.tscript3r.notify2.server.domain.Recipient;
import pl.tscript3r.notify2.server.email.EmailContentGenerator;
import pl.tscript3r.notify2.server.email.EmailExceptionSender;
import pl.tscript3r.notify2.server.email.EmailSender;
import pl.tscript3r.notify2.server.utility.PropertiesLoader;

public class EmailNotifyThread extends NotifyThread {

	private static Logger log = Logger.getLogger(EmailNotifyThread.class.getName());
	private EmailSender emailSender;
	private AdQueue adQueue = AdQueue.getInstance();
	private List<AdPackage> adsList = new ArrayList<AdPackage>();
	private List<AdPackage> usersAdsList = new ArrayList<AdPackage>();
	private EmailContentGenerator emailContentGenerator;
	private int interval = 30000;

	public EmailNotifyThread() throws IOException, ParseException {
		super("");
		emailSender = new EmailSender();
		PropertiesLoader properties = new PropertiesLoader(MainSettings.PROPERTIES_FILE);
		emailContentGenerator = new EmailContentGenerator(properties.getProperty("email.body.file"),
				properties.getProperty("email.ad.file"));
	}

	private void collectAds() {
		while (!adQueue.isEmpty())
			adsList.add(adQueue.pop());
		if (adsList.size() > 0)
			log.info("Collected " + adsList.size() + " ads to e-send");
	}

	private void collectUsersAds() throws IOException {
		usersAdsList.clear();
		usersAdsList.add(adsList.remove(0));
		Recipient recipient = usersAdsList.get(0).getRecipient();
		Iterator<AdPackage> it = adsList.iterator();
		while (it.hasNext()) {
			AdPackage ad = it.next();
			if (ad.getRecipient().equals(recipient)) {
				usersAdsList.add(ad);
				it.remove();
			}
		}
		log.info("Got " + usersAdsList.size() + " ads to send for user ID: " + recipient.getUserId());
	}

	private String generateMessageContent() throws ParseException, IOException {
		String result = emailContentGenerator.getMessageContent(usersAdsList);
		log.info("Sending message with " + usersAdsList.size() + " ads for user ID: "
				+ usersAdsList.get(0).getRecipient().getUserId());
		return result;
	}

	private String getRecipient() {
		return (!usersAdsList.isEmpty()) ? usersAdsList.get(0).getRecipient().getEmail() : "alek199202@gmail.com";
	}

	public void clear() {
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
							emailSender.sendHtmlEmail(getRecipient(), "New ads - Notify", generateMessageContent());
						} catch (MessagingException e) {
							log.warn("Failed to send E-Mail message to client id: " + adsList.get(0).getRecipient().getUserId());
							e.printStackTrace();
						}
					}
			}
		} catch (InterruptedException e) {
			log.info("E-Mail Thread stopped");
		} catch (IOException e) {
			log.error("Could not create the E-Mail message content");
			e.printStackTrace();
			EmailExceptionSender.sendException(e.getStackTrace().toString());
		} catch (ParseException e) {
			log.error("Could not create the E-Mail message content");
			e.printStackTrace();
			EmailExceptionSender.sendException(e.getStackTrace().toString());
		}
	}

}
