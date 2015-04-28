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

import org.fao.fenix.wds.core.constant.SQL;
import org.fao.fenix.wds.core.constant.VARIABLES;
import org.fao.fenix.wds.core.exception.WDSException;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/** 
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class HTTP2SQL {
	
	public static StringBuilder convertOrderBy(String sql) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("ORDER BY ");
			StringTokenizer st1 = new StringTokenizer(sql, ",");
			int counter = 0;
			int max = st1.countTokens();
			while (st1.hasMoreTokens()) {
				String t1 = st1.nextToken();
				if (t1.contains("[") || t1.contains("]")) {
					sb.append(CSV2Bean.clean(t1)).append(" ").append(CSV2Bean.unwrap(t1, "[", "]"));
				} else {
					sb.append(t1);
				}
				if (counter++ < max - 1) {
					sb.append(", ");
				} else {
					sb.append(" ");
				}
			}
			return sb;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static StringBuilder convertGroupBy(String sql) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("GROUP BY ");
			StringTokenizer st1 = new StringTokenizer(sql, ",");
			int counter = 0;
			int max = st1.countTokens();
			while (st1.hasMoreTokens()) {
				String t1 = st1.nextToken();
				sb.append(t1);
				if (counter++ < max - 1) {
					sb.append(", ");
				} else {
					sb.append(" ");
				}
			}
			return sb;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static StringBuilder convertFrom(String sql) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("FROM ");
			StringTokenizer st1 = new StringTokenizer(sql, ",");
			int counter = 0;
			int max = st1.countTokens();
			while (st1.hasMoreTokens()) {
				String t1 = st1.nextToken();
				if (t1.contains("[") || t1.contains("]")) {
					sb.append(CSV2Bean.clean(t1)).append(" AS ").append(CSV2Bean.unwrap(t1, "[", "]"));
				} else {
					sb.append(t1);
				}
				if (counter++ < max - 1) {
					sb.append(", ");
				} else {
					sb.append(" ");
				}
			}
			return sb;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static StringBuilder convertWhere(String sql) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("WHERE ");
			StringTokenizer st1 = new StringTokenizer(sql, ",");
			int counter = 0;
			int max = st1.countTokens();
			while (st1.hasMoreTokens()) {
				String t1 = st1.nextToken();
				if (t1.contains(SQL.JOIN.name())) {
					int start = "JOIN(".length() + t1.indexOf("JOIN(");
					int end   = t1.indexOf(")");
					if (start > -1 && end > -1) {
						String s = t1.substring(start, end);
						StringTokenizer st2 = new StringTokenizer(s, ":");
						sb.append(st2.nextToken()).append(" = ").append(st2.nextToken());
					}
				} else {
					sb.append(CSV2Bean.clean(t1)).append(" IN (");
					StringTokenizer st2 = new StringTokenizer(CSV2Bean.unwrap(t1, "(", ")"), ":");
					int max2 = st2.countTokens();
					int counter2 = 0;
					while (st2.hasMoreTokens()) {
						sb.append(st2.nextToken());
						if (counter2++ < max2 - 1) {
							sb.append(", ");
						} else {
							sb.append(")");
						}
					}
				}
				if (counter++ < max - 1) {
					sb.append(" AND ");
				} else {
					sb.append(" ");
				}
			}
			return sb;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static StringBuilder convertVariables(String sql) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(VARIABLES.VARIABLES.name()).append(" ");
			StringTokenizer st1 = new StringTokenizer(sql, ",");
			int counter = 0;
			int max = st1.countTokens();
			while (st1.hasMoreTokens()) {
				String t1 = st1.nextToken();
				sb.append(CSV2Bean.clean(t1)).append(" IN (");
				StringTokenizer st2 = new StringTokenizer(CSV2Bean.unwrap(t1, "(", ")"), ":");
				int max2 = st2.countTokens();
				int counter2 = 0;
				while (st2.hasMoreTokens()) {
					sb.append(st2.nextToken());
					if (counter2++ < max2 - 1) {
						sb.append(", ");
						} else {
							sb.append(")");
						}
					}
				if (counter++ < max - 1) {
					sb.append(", ");
				} else {
					sb.append(" ");
				}
			}
			return sb;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}

	public static StringBuilder convertSelect(String sql) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT ");
			StringTokenizer st1 = new StringTokenizer(sql, ",");
			int counter = 0;
			int max = st1.countTokens();
			while (st1.hasMoreTokens()) {
				String t1 = st1.nextToken();
				if (t1.contains("[") || t1.contains("]")) {
					sb.append(CSV2Bean.clean(t1)).append(" AS ").append(CSV2Bean.unwrap(t1, "[", "]"));
				} else {
					sb.append(t1);
				}
				if (counter++ < max - 1) {
					sb.append(", ");
				} else {
					sb.append(" ");
				}
			}
			return sb;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static Map<String, String> convertParameters(String s) throws WDSException {
		try {
			Map<String, String> m = new HashMap<String, String>();
			StringTokenizer st1 = new StringTokenizer(s, ",");
			while (st1.hasMoreTokens()) {
				String t1 = st1.nextToken();
				m.put(CSV2Bean.clean(t1), CSV2Bean.unwrap(t1, "(", ")"));
			}
			return m;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
}