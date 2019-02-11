package pl.tscript3r.notify.server.utility;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Properties;

public class PropertiesLoader {

    private static final String PROPERTY_KEY_NOT_FOUND = "Property key %s not found";
    private static final String PROPERTY_FILE_NOT_FOUND = "Properties file %s not exists";

    private final Properties properties = new Properties();

    public PropertiesLoader(String file) throws IOException {
        InputStream inputStream;
        inputStream = this.getClass().getClassLoader().getResourceAsStream(file);
        properties.load(inputStream);
        if (inputStream == null)
            throw new IOException(String.format(PROPERTY_FILE_NOT_FOUND, file));
    }

    public String getProperty(String key) throws ParseException {
        String result = properties.getProperty(key);
        if (result == null)
            throw new ParseException(String.format(PROPERTY_KEY_NOT_FOUND, key), 0);
        return result;
    }

    public String getProperty(String key, String defaultValue) throws ParseException {
        String result = properties.getProperty(key, defaultValue);
        if (result == null)
            throw new ParseException(String.format(PROPERTY_KEY_NOT_FOUND, key), 0);
        return result;
    }

}
