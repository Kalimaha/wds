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

import org.fao.fenix.wds.core.bean.*;
import org.fao.fenix.wds.core.constant.SQL;
import org.fao.fenix.wds.core.constant.WHERE;
import org.fao.fenix.wds.core.exception.WDSException;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/** 
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class CSV2Bean {

	public static List<SelectBean> convertSelect(String csv) throws WDSException {
		try {
			List<SelectBean> l = new ArrayList<SelectBean>();
			StringTokenizer st1 = new StringTokenizer(csv, ",");
			while (st1.hasMoreTokens()) {
				String t1 = st1.nextToken();
				if ((t1.contains("[") || t1.contains("]")) && (t1.contains("{") || t1.contains("}"))) {
					String agg = unwrap(t1, "{", "}");
					String column = clean(t1);
					String alias = unwrap(t1, "[", "]");
					l.add(new SelectBean(agg, column, alias));
				} else {	
					if (t1.contains("[") || t1.contains("]")) {
						l.add(new SelectBean(null, clean(t1), unwrap(t1, "[", "]")));
					} else {
						if (t1.contains("{") || t1.contains("}")) {
							l.add(new SelectBean(unwrap(t1, "{", "}"), clean(t1), null));
						} else {
							l.add(new SelectBean(null, t1, null));
						}
					}
				}
			}
			return l;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static List<FromBean> convertFrom(String csv) throws WDSException {
		try {
			List<FromBean> l = new ArrayList<FromBean>();
			StringTokenizer st1 = new StringTokenizer(csv, ",");
			while (st1.hasMoreTokens()) {
				String t1 = st1.nextToken();
				if (t1.contains("[") || t1.contains("]")) {
					l.add(new FromBean(clean(t1), unwrap(t1, "[", "]")));
				} else {
					l.add(new FromBean(t1, null));
				}
			}
			return l;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static List<WhereBean> convertWhere(String csv) throws WDSException {
		try {
			List<WhereBean> l = new ArrayList<WhereBean>();
			StringTokenizer t = new StringTokenizer(csv, ",");
			while (t.hasMoreTokens()) {
				String value = t.nextToken();
				List<String> ins = new ArrayList<String>();
				String datatype = SQL.TEXT.name();
				if (value.contains(SQL.JOIN.name())) { // JOIN(DI.ItemCode,I.ItemCode)
					int start = 1 + SQL.JOIN.name().length() + value.indexOf(SQL.JOIN.name());
					int end =   value.indexOf(")");
					value = value.substring(start, end);
					StringTokenizer st1 = new StringTokenizer(value, ":");
					// datatype set to DATE to avoid '
					l.add(new WhereBean(SQL.DATE.name(), st1.nextToken(), "=", st1.nextToken(), new ArrayList<String>()));
				} else {
					if (value.contains("(") || value.contains(")")) {
						String value2 = unwrap(value, "(", ")");
						StringTokenizer t2 = new StringTokenizer(value2, ":");
						while (t2.hasMoreTokens())
							ins.add(t2.nextToken());
					}
					if (value.contains("[") || value.contains("]")) {
						datatype = SQL.valueOf(unwrap(value, "[", "]").toUpperCase()).name();
					}
					l.add(new WhereBean(datatype, clean(value), WHERE.IN.name(), null, ins));
				}
			}
			return l;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static List<GroupByBean> convertGroupBy(String csv) throws WDSException {
		try {
			List<GroupByBean> l = new ArrayList<GroupByBean>();
			StringTokenizer t = new StringTokenizer(csv, ",");
			while (t.hasMoreTokens()) 
				l.add(new GroupByBean(t.nextToken()));
			return l;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static List<OrderByBean> convertOrderBy(String csv) throws WDSException {
		try {
			List<OrderByBean> l = new ArrayList<OrderByBean>();
			StringTokenizer t = new StringTokenizer(csv, ",");
			while (t.hasMoreTokens()) {
				String value = t.nextToken();
				String direction = SQL.ASC.name();
				if (value.contains("[") || value.contains("]")) {
					direction = SQL.valueOf(unwrap(value, "[", "]").toUpperCase()).name();
				}
				l.add(new OrderByBean(clean(value), direction));
			}
			return l;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static String unwrap(String s, String open, String close) {
		int a = 1 + s.indexOf(open);
		int z = s.indexOf(close);
		if (a > -1 && z > -1) {
			return s.substring(a, z);
		} else {
			return s;
		}
	}
	
	public static String clean(String s) {
		if (s.contains("(") || s.contains(")")) {
			int a = s.indexOf("(");
			if (a > -1) {
				s = s.substring(0, a);
			}
		}
		if (s.contains("[") || s.contains("]")) {
			int a = s.indexOf("[");
			if (a > -1) {
				s = s.substring(0, a);
			}
		}
		if (s.contains("{") || s.contains("}")) {
			int a = s.indexOf("{");
			if (a > -1) {
				s = s.substring(0, a);
			}
		}
		return s;
	}
	
}
