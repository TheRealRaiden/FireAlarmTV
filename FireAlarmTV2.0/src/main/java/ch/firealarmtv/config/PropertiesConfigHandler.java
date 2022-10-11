package ch.firealarmtv.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import ch.firealarmtv.handlers.Console;

public final class PropertiesConfigHandler {

	private String propertiesFile;
	private InputStream input;
	private Properties prop;

	/**
	 * @param propertiesFile
	 */
	public PropertiesConfigHandler(String propertiesFile) {
		super();
		this.propertiesFile = propertiesFile;
		prop = new Properties();

		// load a properties file
		try {
			input = new FileInputStream(this.propertiesFile);
			prop.load(input);
		} catch (IOException e) {
			Console.writelnError("\t - Error while Loading Config File! Exiting Application");
			System.exit(-1);
		}

	}

	public String getStr(String key) {
		return prop.getProperty(key);
	}

	public int getInt(String key) {
		return Integer.parseInt(prop.getProperty(key));
	}

	public double getDbl(String key) {
		return Double.parseDouble(prop.getProperty(key));
	}
	
	public boolean getBool(String key) {
		return Boolean.parseBoolean(prop.getProperty(key));
	}

}
