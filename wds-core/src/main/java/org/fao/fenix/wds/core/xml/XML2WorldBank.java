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
package org.fao.fenix.wds.core.xml;

import org.fao.fenix.wds.core.bean.SelectBean;
import org.fao.fenix.wds.core.bean.WBBean;
import org.fao.fenix.wds.core.bean.WhereBean;
import org.fao.fenix.wds.core.constant.RESTService;
import org.fao.fenix.wds.core.constant.SQL;
import org.fao.fenix.wds.core.datasource.WORLDBANK;
import org.fao.fenix.wds.core.exception.WDSException;

import java.util.ArrayList;
import java.util.List;

/** 
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class XML2WorldBank {
	
	private final static String PER_PAGE = "10000";

	public static StringBuilder convertWorldBank(String xml) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(WORLDBANK.BASEURL);
			sb.append(convertWorldBankWheres(xml));
			return sb;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static StringBuilder convertWorldBankWheres(String xml) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			StringBuilder tmp = new StringBuilder();
			StringBuilder countries = new StringBuilder();
			StringBuilder indicators = new StringBuilder();
			List<String> filters = new ArrayList<String>();
			filters.add("?per_page=" + PER_PAGE);
			List<WhereBean> wheres = XML2Bean.convertWheres(xml);
			for (WhereBean w : wheres) {
				RESTService wb = RESTService.valueOf(w.getColumn().toLowerCase());
				switch (wb) {
					case country:
						countries.append("/countries");
						if (!w.getIns().isEmpty()) {
							countries.append("/");
							for (int i = 0 ; i < w.getIns().size() ; i++) {
								countries.append(w.getIns().get(i));
								if (i < w.getIns().size() - 1)
									countries.append(";");
							}
						}
					break;
					case indicator:
						indicators.append("/indicators");
						if (!w.getIns().isEmpty()) {
							indicators.append("/");
							for (int i = 0 ; i < w.getIns().size() ; i++) {
								indicators.append(w.getIns().get(i));
								if (i < w.getIns().size() - 1)
									indicators.append(";");
							}
						} else if (w.getValue() != null && !w.getValue().equals("") && !w.getValue().equals(SQL.NONE.name())) {
							indicators.append("/indicators/").append(w.getValue());
						}
					break;
					case year:
						if (!w.getIns().isEmpty()) {
							tmp = new StringBuilder();
							if (w.getIns().size() > 2) {
								throw new WDSException("World Bank accepts single dates or intervals. Please verify your request.");
							} else if (w.getIns().size() == 1) {
								tmp.append("&date=").append(w.getIns().get(0));
							} else {
								int min = Integer.MAX_VALUE;
								int max = Integer.MIN_VALUE;
								for (String s : w.getIns()) {
									Integer d = Integer.valueOf(s);
									if (d < min)
										min = d;
									if (d > max)
										max = d;
								}
								tmp.append("&date=").append(min).append(":").append(max);
							}
							filters.add(tmp.toString());
						} else if (w.getValue() != null && !w.getValue().equals("") && !w.getValue().equals(SQL.NONE.name())) {
							indicators.append("&date=").append(w.getValue());
						}
					break;
					case frequency:
						if (!w.getIns().isEmpty()) {
							tmp = new StringBuilder();
							if (w.getIns().size() > 1) {
								throw new WDSException("World Bank accepts only one frequency. Please verify your request.");
							} else if (w.getIns().size() == 1) {
								tmp.append("&frequency=").append(w.getIns().get(0).toUpperCase());
							}
							filters.add(tmp.toString());
						} else if (w.getValue() != null && !w.getValue().equals("") && !w.getValue().equals(SQL.NONE.name())) {
							indicators.append("&frequency=").append(w.getValue());
						}
					break;
					case incomelevel:
						if (!w.getIns().isEmpty()) {
							tmp = new StringBuilder();
							tmp.append("&incomeLevels=");
							for (int i = 0 ; i < w.getIns().size() ; i++) {
								tmp.append(w.getIns().get(i));
								if (i < w.getIns().size() - 1)
									tmp.append(";");
							}
							filters.add(tmp.toString());
						} else if (w.getValue() != null && !w.getValue().equals("") && !w.getValue().equals(SQL.NONE.name())) {
							indicators.append("&incomeLevels=").append(w.getValue());
						}
					break;
					case lendingtype:
						if (!w.getIns().isEmpty()) {
							tmp = new StringBuilder();
							tmp.append("&lendingtype=");
							for (int i = 0 ; i < w.getIns().size() ; i++) {
								tmp.append(w.getIns().get(i));
								if (i < w.getIns().size() - 1)
									tmp.append(";");
							}
							filters.add(tmp.toString());
						} else if (w.getValue() != null && !w.getValue().equals("") && !w.getValue().equals(SQL.NONE.name())) {
							indicators.append("&lendingtype=").append(w.getValue());
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
	
	public static WBBean convertWorldBankSelects(String xml) throws WDSException {
		try {
			WBBean b = new WBBean();
			b.setShowHeaders(false);
			List<SelectBean> selects = XML2Bean.convertSelects(xml);
			for (SelectBean s : selects) {
				RESTService col = RESTService.valueOf(s.getColumn().toLowerCase());
				switch (col) {
					case value: b.setShowValue(true); break;
					case indicator: b.setShowIndicator(true); break;
					case country: b.setShowCountry(true); break;
					case year: b.setShowDate(true); break;
				}
			}
			return b;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
}