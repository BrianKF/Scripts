package com.adtheorent.UnitTesting.Utils;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for loading and returning properties based on filename. This class assumes properties files are on its
 * classpath, meaning you cannot invoke this across projects.
 */
public final class PropertiesUtils {

	private static final Logger logger = Logger.getLogger( PropertiesUtils.class.getName() );

	public static Properties getProperties( final String propertiesFileName ) {
		final Properties loadedProperties = new Properties();
		try {
			loadedProperties.load( PropertiesUtils.class.getResourceAsStream( "/" + propertiesFileName ) );
		} catch ( final IOException e ) {
			logger.log( Level.SEVERE, e.getMessage(), e );
		}

		return loadedProperties;
	}

}
