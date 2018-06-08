package pl.tscript3r.notify2.server.parser.list;

import org.jsoup.nodes.Document;

import pl.tscript3r.notify2.server.dataflow.AdQueue;

public abstract class JsoupParser {

	protected String url;
	protected Document parsedContent;
	protected AdQueue adQueue = AdQueue.getInstance();
	protected Integer taskId;

	protected JsoupParser(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public Integer getTaskId() {
		return taskId;
	}	
	
}