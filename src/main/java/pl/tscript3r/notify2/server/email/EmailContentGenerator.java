package pl.tscript3r.notify2.server.email;

import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import pl.tscript3r.notify2.server.constants.AdFields;
import pl.tscript3r.notify2.server.domain.AdPackage;
import pl.tscript3r.notify2.server.utility.ResourceLoader;

public class EmailContentGenerator {

	private String bodyTemplate;
	private String adTemplate;

	public EmailContentGenerator(String bodyFile, String adFile) throws IOException, ParseException {
		ResourceLoader resourceLoader = new ResourceLoader();
		bodyTemplate = resourceLoader.readFromJARFile(bodyFile);
		adTemplate = resourceLoader.readFromJARFile(adFile);
	}

	private String getHashedKey(String key) {
		return new StringBuilder("%").append(key).append("%").toString();

	}

	private Boolean requierdFieldsCheck(AdPackage ad) {
		for (int i = 0; i < AdFields.REQUIERD_FIELDS.length; i++)
			if (!ad.isValue(AdFields.REQUIERD_FIELDS[i]))
				return false;
		return true;
	}

	private String getMissingFields(AdPackage ad) {
		String result = null;
		for (int i = 0; i < AdFields.REQUIERD_FIELDS.length; i++)
			if (!ad.isValue(AdFields.REQUIERD_FIELDS[i]))
				if (result == null)
					result = AdFields.REQUIERD_FIELDS[i];
				else
					result = result + ", " + AdFields.REQUIERD_FIELDS[i];
		return result;
	}

	public String getMessageContent(List<AdPackage> adsList) throws ParseException {
		String result = bodyTemplate;
		StringBuilder adsBuilder = new StringBuilder("");
		if (!adsList.isEmpty()) {
			Iterator<AdPackage> it = adsList.iterator();
			while (it.hasNext()) {
				AdPackage ad = it.next();
				if (requierdFieldsCheck(ad)) {
					String adString = adTemplate;
					for (int i = 0; i < AdFields.REQUIERD_FIELDS.length; i++)
						adString = adString.replaceAll(getHashedKey(AdFields.REQUIERD_FIELDS[i]),
								ad.getValue(AdFields.REQUIERD_FIELDS[i]));
					adsBuilder.append(adString);
				} else
					throw new ParseException(
							"Not every field in the ad has been set. Missing: " + getMissingFields(ad), 1);
			}
		} else
			throw new ParseException("Tryied to generate e-mail content with an empty ads list", 0);

		return result.replace(getHashedKey(AdFields.KEY_BODY), adsBuilder.toString());
	}

}
