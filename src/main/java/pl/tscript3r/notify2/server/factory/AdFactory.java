package pl.tscript3r.notify2.server.factory;

import java.net.URISyntaxException;

import pl.tscript3r.notify2.server.domain.AdPackage;
import pl.tscript3r.notify2.server.domain.Recipient;
import pl.tscript3r.notify2.server.email.EmailExceptionSender;
import pl.tscript3r.notify2.server.utility.HostnameExtractor;

public class AdFactory {
	
	public static synchronized AdPackage getAdPackageInstance(String url, Recipient recipient) {
		try {
			return new AdPackage(url.hashCode(), HostnameExtractor.getDomainName(url).hashCode(), url, recipient);
		} catch (URISyntaxException e) {
			EmailExceptionSender.sendException(e.getStackTrace().toString());
			e.printStackTrace();
		}
		return null;
	}
	
}
