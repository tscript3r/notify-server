package pl.tscript3r.notify.server.parser.list;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import pl.tscript3r.notify.server.constants.AdFields;
import pl.tscript3r.notify.server.domain.AdPackage;
import pl.tscript3r.notify.server.domain.Recipient;
import pl.tscript3r.notify.server.factory.AdFactory;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class OLXParser extends JsoupParser {

    private static final int MAX_STORED_ADS = 120;

    private final List<AdPackage> adsList = new ArrayList<>(130);
    private final Recipient recipient;
    private Boolean firstRun = true;

    public OLXParser(String url, int taskId, Recipient recipient) {
        super(url);
        this.taskId = taskId;
        this.recipient = recipient;
    }

    private void limitList() {
        if (adsList.size() > MAX_STORED_ADS) {
            for (int i = 0; i < 10; i++)
                adsList.remove(0);
        }
    }

    private void sendToQueue(AdPackage ad) {
        if (firstRun)
            adsList.add(ad);
        else {
            if (!adsList.contains(ad)) {
                limitList();
                adsList.add(ad);
                adQueue.add(ad);
            }
        }
    }

    private void mainLayoutParser(Elements adsElements) {
        adsElements.forEach(element -> {
            String adUrl = element.select("a[href]").attr("href");
            AdPackage ad = AdFactory.getAdPackageInstance(adUrl, recipient);
            ad.addValue(AdFields.KEY_ID, element.attr("data-id"));
            ad.addValue(AdFields.KEY_URL, adUrl);
            ad.addValue(AdFields.KEY_TITLE, element.select("strong").text());
            ad.addValue(AdFields.KEY_THUMBNAIL_URL, element
                    .select("img[src]")
                    .attr("src"));
            ad.addValue(AdFields.KEY_CATEGORY, element
                    .select("small[class]")
                    .text());
            ad.addValue(AdFields.KEY_PRICE, element.select("p")
                    .select("strong")
                    .text());
            ad.addValue(AdFields.KEY_LOCATION,
                    element.select("small[class]")
                            .attr("class", "breadcrumb x-normal")
                            .select("span").text());
            sendToQueue(ad);
        });
    }

    private String longHash(String string) {
        long h = 98764321261L;
        int l = string.length();
        char[] chars = string.toCharArray();
        for (int i = 0; i < l; i++)
            h = 31 * h + chars[i];
        return Long.toString(h);
    }

    private void workLayoutParser(Elements adsElements) {
        adsElements.forEach(element -> {
            if (StringUtils.containsIgnoreCase(element.attr("class"), "offer ")) {
                AdPackage ad;
                String adUrl = element.select("a[href]")
                        .attr("href");
                ad = AdFactory.getAdPackageInstance(adUrl, recipient);
                String title = element.select("div[class]")
                        .attr("class", "list-item__price")
                        .text();
                ad.addValue(AdFields.KEY_URL, adUrl);
                ad.addValue(AdFields.KEY_TITLE, title);
                ad.addValue(AdFields.KEY_ID, longHash(title));
                ad.addValue(AdFields.KEY_LOCATION,
                        element.select("strong[class]")
                                .attr("strong", "list-item__location")
                                .text());
                sendToQueue(ad);
            }
        });
    }

    public void parse() throws ParseException, IOException {
        parsedContent = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 Chrome/26.0.1410.64 Safari/537.31")
                .timeout(5 * 1000)
                .followRedirects(true)
                .maxBodySize(1024 * 1024 * 5)
                .get();

        Elements adsElements = parsedContent.getElementsByAttributeValueMatching("class",
                Pattern.compile("fixed breakword\\s\\sad_*"));

        if (adsElements.size() > 0)
            mainLayoutParser(adsElements);
        else {
            adsElements = parsedContent.getElementsByAttributeValueMatching("class",
                    Pattern.compile("offer\\s{0,8}"));
            if (adsElements.size() > 0)
                workLayoutParser(adsElements);
            else
                throw new ParseException("OLX.pl site has been changed - " +
                        "parser needs to be rebuild", 0);
        }

        if (firstRun)
            firstRun = false;

    }

    @Override
    public int hashCode() {
        return taskId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null)
            return (obj.hashCode() == taskId);
        return false;
    }

}
