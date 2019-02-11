package pl.tscript3r.notify.server.email;

import pl.tscript3r.notify.server.constants.MainSettings;
import pl.tscript3r.notify.server.utility.PropertiesLoader;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EmailExceptionSender {

    public synchronized static void sendException(String message) {
        try {
            PropertiesLoader propertiesLoader = new PropertiesLoader(MainSettings.PROPERTIES_FILE);
            String currentTime = new SimpleDateFormat("hh:mm dd.MM.yy").format(new Date());
            EmailSender emailSender =
                    new EmailSender(propertiesLoader.getProperty("email.exception.username"),
                            propertiesLoader.getProperty("email.exception.password"),
                            propertiesLoader.getProperty("email.exception.host"),
                            propertiesLoader.getProperty("email.exception.port"));
            emailSender.sendHtmlEmail(propertiesLoader.getProperty("email.exception.recipient"),
                    String.format(
                            propertiesLoader.getProperty("email.exception.title"),
                            currentTime), message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}