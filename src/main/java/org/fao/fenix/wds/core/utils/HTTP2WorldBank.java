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

import org.apache.log4j.Logger;
import org.fao.fenix.wds.core.bean.WBBean;
import org.fao.fenix.wds.core.constant.RESTService;
import org.fao.fenix.wds.core.datasource.WORLDBANK;
import org.fao.fenix.wds.core.exception.WDSException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/** 
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class HTTP2WorldBank {
	
	private final static String PER_PAGE = "10000";
	
	private final static Logger LOGGER =  Logger.getLogger(HTTP2WorldBank.class);
	
	public static WBBean convertWorldBankSelects(Map<String,String[]> parameters) throws WDSException {
		try {
			WBBean b = new WBBean();
			for(String key: parameters.keySet()) {
				RESTService p = RESTService.valueOf(key.toLowerCase());
				String value = parameters.get(p.name())[0];
				switch (p) {
					case select:  
						StringTokenizer st1 = new StringTokenizer(value, ",");
						while (st1.hasMoreTokens()) {
							String s1 = st1.nextToken();
							RESTService col = RESTService.valueOf(s1.toLowerCase());
							switch (col) {
								case value: b.setShowValue(true); break;
								case indicator: b.setShowIndicator(true); break;
								case country: b.setShowCountry(true); break;
								case year: b.setShowDate(true); break;
							}
						}
					break;
				}
			}
			return b;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static StringBuilder convertWorldBank(Map<String,String[]> parameters) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(WORLDBANK.BASEURL);
			List<String> slashes = new ArrayList<String>();
			for(String key: parameters.keySet()) {
				RESTService p = RESTService.valueOf(key.toLowerCase());
				String value = parameters.get(p.name())[0];
				switch (p) {
					case where: slashes.add(convertWhere(value).toString()); break;
				}
			}
			for (String s : slashes)
				sb.append(s);
			return sb;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static StringBuilder convertWhere(String sql) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			StringBuilder countries = new StringBuilder();
			countries.append("/countries");
			StringBuilder indicators = new StringBuilder();
			StringBuilder tmp = new StringBuilder();
			List<String> filters = new ArrayList<String>();
			filters.add("?per_page=" + PER_PAGE);
			StringTokenizer st1 = new StringTokenizer(sql, ",");
			while (st1.hasMoreTokens()) {
				String t1 = st1.nextToken();
				List<String> wraps = new ArrayList<String>();
				String clean = CSV2Bean.clean(t1);
				RESTService wb = RESTService.valueOf(clean.toLowerCase());
				if (t1.contains("(") || t1.contains(")")) {
					String wrap  = CSV2Bean.unwrap(t1, "(", ")");		
					StringTokenizer st2 = new StringTokenizer(wrap, ":");
					while (st2.hasMoreTokens()) {
						wraps.add(st2.nextToken());
					}
				}
				switch (wb) {
					case frequency:
						if (!wraps.isEmpty()) {
							tmp = new StringBuilder();
							if (wraps.size() > 1) {
								throw new WDSException("World Bank accepts only one frequency. Please verify your request.");
							} else if (wraps.size() == 1) {
								tmp.append("&frequency=").append(wraps.get(0).toUpperCase());
							}
							filters.add(tmp.toString());
						}
					break;
					case year:
						if (!wraps.isEmpty()) {
							tmp = new StringBuilder();
							if (wraps.size() > 2) {
								throw new WDSException("World Bank accepts single dates or intervals. Please verify your request.");
							} else if (wraps.size() == 1) {
								tmp.append("&date=").append(wraps.get(0));
							} else {
								int min = Integer.MAX_VALUE;
								int max = Integer.MIN_VALUE;
								for (String w : wraps) {
									Integer d = Integer.valueOf(w);
									if (d < min)
										min = d;
									if (d > max)
										max = d;
								}
								tmp.append("&date=").append(min).append(":").append(max);
							}
							filters.add(tmp.toString());
						}
					break;
					case country:
						if (!wraps.isEmpty()) {
							countries = new StringBuilder();
							countries.append("/countries/");
							for (int i = 0 ; i < wraps.size() ; i++) {
								countries.append(wraps.get(i));
								if (i < wraps.size() - 1)
									countries.append(";");
							}
						} else {
							countries = new StringBuilder();
							countries.append("/countries");
						}
					break;
					case incomelevel:
						if (!wraps.isEmpty()) {
							tmp = new StringBuilder();
							tmp.append("&incomeLevels=");
							for (int i = 0 ; i < wraps.size() ; i++) {
								tmp.append(wraps.get(i));
								if (i < wraps.size() - 1)
									tmp.append(";");
							}
						}
						filters.add(tmp.toString());
					break;
					case lendingtype:
						if (!wraps.isEmpty()) {
							tmp = new StringBuilder();
							tmp.append("&lendingtype=");
							for (int i = 0 ; i < wraps.size() ; i++) {
								tmp.append(wraps.get(i));
								if (i < wraps.size() - 1)
									tmp.append(";");
							}
						}
						filters.add(tmp.toString());
					break;
					case indicator:
						if (!wraps.isEmpty()) {
							indicators.append("/indicators");
							if (wraps.size() > 1) {
								throw new WDSException("World Bank provides only one indicator at time. Please verify your request.");
							} else {
								indicators.append("/");
								for (int i = 0 ; i < wraps.size() ; i++) {
									indicators.append(wraps.get(i));
									if (i < wraps.size() - 1)
										indicators.append(";");
								}
							}
						} else {
							indicators = new StringBuilder();
							indicators.append("/indicators");
						}
					break;
				}
			}
			sb.append(countries);
			sb.append(indicators);
			for (String f : filters)
				sb.append(f);
			return sb;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static boolean containsCountries(List<String> ss) throws WDSException {
		try {
			for (String s : ss)
				if (s.contains("/countries"))
					return true;
			return false;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
}