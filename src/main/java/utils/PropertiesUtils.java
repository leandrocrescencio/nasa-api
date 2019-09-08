package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertiesUtils {

	private PropertiesUtils() {
		throw new IllegalStateException("Utility class");
	}
	
	private static final Logger LOGGER = Logger.getLogger(PropertiesUtils.class);
	private static Properties propertie = new Properties();
	private static String path = StaticValues.PATH_PROJECT + "/src/test/resources/test.properties";

	public static String getValue(String name) {
		try {
			propertie.load(new FileInputStream(path));
		} catch (IOException e) {
			LOGGER.warn(e.getMessage());
		}
		return propertie.getProperty(name);
	}
}
