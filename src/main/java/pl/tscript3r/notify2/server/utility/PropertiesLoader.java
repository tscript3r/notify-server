package pl.tscript3r.notify2.server.utility;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Properties;

public class PropertiesLoader {
	
	InputStream inputStream;
	Properties properties = new Properties();
	
	public PropertiesLoader(String file) throws IOException {
		inputStream = this.getClass().getClassLoader().getResourceAsStream(file);
		properties.load(inputStream);
		if (inputStream == null) 
			throw new IOException(String.format("Properties file %s not exists", file));
	}
	
	public String getProperty(String key) throws ParseException {
		String result = properties.getProperty(key);
		if(result == null)
			throw new ParseException(String.format("Property key %s not found", key), 0);
		return result;
	}
	
	public String getProperty(String key, String defaultValue) throws ParseException {
		String result = properties.getProperty(key, defaultValue);
		if(result == null)
			throw new ParseException(String.format("Property key %s not found", key), 0);
		return result;
	}
	
}
