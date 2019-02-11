package pl.tscript3r.notify.server.email;

import org.apache.log4j.Logger;
import pl.tscript3r.notify.server.domain.AdPackage;
import pl.tscript3r.notify.server.constants.AdFields;
import pl.tscript3r.notify.server.utility.ResourceLoader;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class EmailContentGenerator {
    private static final Logger log = Logger.getLogger(EmailContentGenerator.class);
    private String bodyTemplate;
    private String adTemplate;

    public EmailContentGenerator(String bodyFile, String adFile) throws IOException {
        ResourceLoader resourceLoader = new ResourceLoader();
        bodyTemplate = resourceLoader.readFromJARFile(bodyFile);
        adTemplate = resourceLoader.readFromJARFile(adFile);
    }

    private String getTaggedKey(String key) {
        return "%" + key + "%";

    }

    private Boolean requiredFieldsCheck(AdPackage ad) {
        for (int i = 0; i < AdFields.REQUIRED_FIELDS.length; i++)
            if (!ad.hasValue(AdFields.REQUIRED_FIELDS[i]))
                return false;
        return true;
    }

    private String getMissingFields(AdPackage ad) {
        String result = null;
        for (int i = 0; i < AdFields.REQUIRED_FIELDS.length; i++)
            if (!ad.hasValue(AdFields.REQUIRED_FIELDS[i]))
                if (result == null)
                    result = AdFields.REQUIRED_FIELDS[i];
                else
                    result = result + ", " + AdFields.REQUIRED_FIELDS[i];
        return result;
    }

    public String getMessageContent(List<AdPackage> adsList) throws ParseException {
        String result = bodyTemplate;
        StringBuilder adsBuilder = new StringBuilder();


        if (!adsList.isEmpty()) {
            adsList.forEach(ad -> {
                if (requiredFieldsCheck(ad)) {
                    String adString = adTemplate;
                    for (int i = 0; i < AdFields.REQUIRED_FIELDS.length; i++)
                        adString = adString.replaceAll(getTaggedKey(AdFields.REQUIRED_FIELDS[i]),
                                ad.getValue(AdFields.REQUIRED_FIELDS[i]));
                    adsBuilder.append(adString);
                } else
                    log.info("Ignored ad id: " + ad.getId() + "; Missing fields: " + getMissingFields(ad));
            });
        } else
            throw new ParseException("Tried to generate e-mail content with an empty ads list", 0);

        return result.replace(getTaggedKey(AdFields.KEY_BODY), adsBuilder.toString());
    }

}
