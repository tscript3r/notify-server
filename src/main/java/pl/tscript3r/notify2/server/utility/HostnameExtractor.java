package pl.tscript3r.notify2.server.utility;

import java.net.URISyntaxException;

public class HostnameExtractor {
	
	public synchronized static String getDomainName(String url) throws URISyntaxException {
		  String domainName = new String(url);
		  int index = domainName.indexOf("://");
		  if (index != -1) 
		    domainName = domainName.substring(index + 3);
		  index = domainName.indexOf('/');
		  if (index != -1) 
		    domainName = domainName.substring(0, index);
		  domainName = domainName.replaceFirst("^www.*?\\.", "");
		  return domainName;
	}
	
}

