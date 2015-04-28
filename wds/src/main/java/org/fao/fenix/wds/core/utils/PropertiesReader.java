/**
 *
 * FENIX (Food security and Early warning Network and Information Exchange)
 *
 * Copyright (c) 2011, by FAO of UN under the EC-FAO Food Security
Information for Action Programme
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.fao.fenix.wds.core.utils;

import org.fao.fenix.wds.core.exception.WDSException;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/** 
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class PropertiesReader {
	
	public static String read(String propertyName, String filename) throws WDSException {
		try {
			Properties p = new Properties();
			File f = new File("src/main/resources/settings/" + filename);
			FileInputStream fis = new FileInputStream(f);
			p.load(fis);
			fis.close();
			String propertyValue = p.getProperty(propertyName);
			return propertyValue;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}

	public static String read(String propertyName) throws WDSException {
		try {
			return read(propertyName, "fwds-tools.properties");
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static Map<String, String> read(String[] propertyNames, String filename) throws WDSException {
		try {
			Map<String, String> m = new HashMap<String, String>();
			for (String propertyName : propertyNames)
				m.put(propertyName, read(propertyName, filename));
			return m;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static Map<String, String> read(String[] propertyNames) throws WDSException {
		try {
			Map<String, String> m = new HashMap<String, String>();
			for (String propertyName : propertyNames)
				m.put(propertyName, read(propertyName));
			return m;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
}