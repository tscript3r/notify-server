package pl.tscript3r.notify2.server.parser.list;

import org.jsoup.nodes.Document;
import pl.tscript3r.notify2.server.dataflow.AdQueue;

public abstract class JsoupParser {

    final AdQueue adQueue = AdQueue.getInstance();
    final String url;
    Document parsedContent;
    Integer taskId;

    JsoupParser(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public Integer getTaskId() {
        return taskId;
    }

}