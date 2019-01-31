package pl.tscript3r.notify2.server.factory;

import pl.tscript3r.notify2.server.domain.AdPackage;
import pl.tscript3r.notify2.server.domain.Recipient;
import pl.tscript3r.notify2.server.utility.HostnameExtractor;

public class AdFactory {

    public static synchronized AdPackage getAdPackageInstance(String url, Recipient recipient) {
        return new AdPackage(url.hashCode(), HostnameExtractor.getDomainName(url).hashCode(), url, recipient);
    }

}
