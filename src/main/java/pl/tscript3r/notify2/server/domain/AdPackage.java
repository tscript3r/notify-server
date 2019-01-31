package pl.tscript3r.notify2.server.domain;

import java.util.HashMap;
import java.util.Map;

public class AdPackage extends Package {

    private final Map<String, String> values = new HashMap<>();

    public AdPackage(int id, long hostId, String url, Recipient recipient) {
        super(id, hostId, url, recipient);
    }

    public void addValue(String key, String value) {
        values.put(key, value);
    }

    public Boolean isValue(String key) {
        return values.containsKey(key);
    }

    public String getValue(String key) {
        return values.get(key);
    }

}
