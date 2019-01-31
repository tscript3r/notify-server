package pl.tscript3r.notify2.server.parser.advert;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Pattern;

public class OLXAdvertParser extends JsoupAdvertParser {

    public OLXAdvertParser(String url) throws IOException {
        super(url);
        parsedContent = Jsoup.connect(url).get();
    }

    public String getDescription() {
        Elements adElements = parsedContent.getElementsByAttributeValueMatching("name",
                Pattern.compile("description"));
        String result = adElements.first().attr("content");
        result = result.substring(result.indexOf(":") + 2, result.length() - 3) + " [...]";
        result = result.replace("\r\n\r\n", "\r\n");
        return result;
    }

}
