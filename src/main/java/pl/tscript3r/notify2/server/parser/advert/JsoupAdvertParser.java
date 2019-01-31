package pl.tscript3r.notify2.server.parser.advert;

import org.jsoup.nodes.Document;

abstract class JsoupAdvertParser {
    protected final String url;
    Document parsedContent;

    JsoupAdvertParser(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
