package pl.tscript3r.notify2.server.parser.advert;

import org.jsoup.nodes.Document;

public abstract class JsoupAdvertParser {
	protected String url;
	protected Document parsedContent;
	
	public JsoupAdvertParser(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}
}
