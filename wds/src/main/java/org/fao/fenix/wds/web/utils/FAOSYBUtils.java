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
package org.fao.fenix.wds.web.utils;

import org.fao.fenix.wds.core.constant.RESTService;
import org.fao.fenix.wds.web.rest.WDSRESTService;

@SuppressWarnings("serial")
public class FAOSYBUtils extends WDSRESTService {
	
	private final static String NON_COUNTRY_BASED_MSG = "<div style='text-align: right;'>" +
														"Non-country based data is not available yet.<br>" +
														"We apologize for any inconvenience.<br>" +
														"For further information please contact Mr. Matthieu Stigler<br>" +
														"<a href='mailto:Matthieu.Stigler@fao.org'>" +
														"Matthieu.Stigler@fao.org" +
														"</a></div>";
	
	private final static String NON_COUNTRY_BASED_HEAD = "Data Not Available For Download";
	
	private RESTService tableFormat = RESTService.WIDE;
	
	/*
	public String parseFAOSYB(Map<String,String[]> parameters) throws IllegalAccessException, InstantiationException, SQLException, ClassNotFoundException, WDSException {
		String indicatorCode = null;
		String datasetCode = null;
		String objectCode = null;
		String objectSYBCode = null;
		String findCode = null;
		OUTPUT outFormat = OUTPUT.HTML;
		for(String key: parameters.keySet()) {
			RESTService p = RESTService.valueOf(key.toLowerCase());
			String value = parameters.get(p.name())[0];
			switch (p) {
				case object: objectCode = value; break;
				case dataset: datasetCode = value; break;
				case indicator: indicatorCode = value; break;
				case findcode: findCode = value; break;
				case objectsyb: objectSYBCode = value; break;
				case format: tableFormat = RESTService.valueOf(value.toUpperCase()); break;
				case out: outFormat = OUTPUT.valueOf(value.toUpperCase()); break;
			}
		}
		if (objectCode != null) {
			return queryByObject(objectCode, outFormat);
		} else if (datasetCode != null) {
			return queryByDataset(datasetCode, outFormat);
		} else if (indicatorCode != null) {
			return queryByIndicator(indicatorCode, outFormat);
		} else if (objectSYBCode != null) {
			return queryByObjectSYB(objectSYBCode, outFormat);
		} else if (findCode != null) {
			return queryByFindCode(outFormat);
		} else {
			throw new WDSException("Request is not valid. Please check your syntax.");
		}
	}
	
	private String queryByObject(String objectCode, OUTPUT outFormat) throws IllegalAccessException, InstantiationException, SQLException, ClassNotFoundException, WDSException {
		List<List<String>> table = JDBCConnector.query(new DBBean(DATASOURCE.FAOSYB), FAOSYBSQL.queryByObjectSQL(objectCode), FAOSYBSQL.headers());
		return formatOutput(table, objectCode, outFormat);
	}
	
	private String queryByDataset(String datasetCode, OUTPUT outFormat) throws IllegalAccessException, InstantiationException, SQLException, ClassNotFoundException, WDSException {
		List<List<String>> table = JDBCConnector.query(new DBBean(DATASOURCE.FAOSYB), FAOSYBSQL.queryByDatasetSQL(datasetCode), FAOSYBSQL.headers());
		return formatOutput(table, datasetCode, outFormat);
	}
	
	private String queryByIndicator(String indicatorCode, OUTPUT outFormat) throws IllegalAccessException, InstantiationException, SQLException, ClassNotFoundException, WDSException {
		List<List<String>> table = JDBCConnector.query(new DBBean(DATASOURCE.FAOSYB), FAOSYBSQL.queryByIndicatorSQL(indicatorCode), FAOSYBSQL.headers());
		boolean timeseries = indicatorCode.endsWith("_years");
		if (timeseries && tableFormat == RESTService.WIDE) {
			return formatOutput(reshape(table), indicatorCode, outFormat);
		} else {
			return formatOutput(table, indicatorCode, outFormat);
		}
	}
	
	private String queryByObjectSYB(String objectSYBCode, OUTPUT outFormat) throws IllegalAccessException, InstantiationException, SQLException, ClassNotFoundException, WDSException {
		List<List<String>> table = JDBCConnector.query(new DBBean(DATASOURCE.FAOSYB), FAOSYBSQL.queryByObjectSYBSQL(objectSYBCode), FAOSYBSQL.headers());
		return formatOutput(table, objectSYBCode, outFormat);
	}
	
	private String queryByFindCode(OUTPUT outFormat) throws IllegalAccessException, InstantiationException, SQLException, ClassNotFoundException, WDSException {
		List<List<String>> table = JDBCConnector.query(new DBBean(DATASOURCE.FAOSYB), FAOSYBSQL.queryByFindCodeSQL(), FAOSYBSQL.headers());
		return formatOutput(table, "CODES", outFormat);
	}
	
	private String formatMessage(String head, String msg) {
		List<List<String>> t = new ArrayList<List<String>>();
		List<String> h = new ArrayList<String>();
		h.add(head);
		t.add(h);
		List<String> r = new ArrayList<String>();
		r.add(msg);
		t.add(r);
		return formatOutput(t, null, OUTPUT.HTML);
	}
	
	public List<List<String>> reshape(List<List<String>> in) {
		List<List<String>> out = new ArrayList<List<String>>();
		List<String> years = getYears(in);
		List<String> countries = getCountries(in);
		List<String> headers = reshapeHeaders(in.get(0), years);
		out.add(headers);
		for (String country : countries)
			out.add(reshapeCountry(in, years, country));
		return out;
	}
	
	private List<String> reshapeCountry(List<List<String>> in, List<String> years, String country) {
		List<String> out = new ArrayList<String>();
		List<List<String>> subIN = filterCountry(in, country);
		for (int i = 0 ; i < subIN.get(0).size() - 2 ; i++)	// copy common part
			out.add(subIN.get(0).get(i));
		for (List<String> row : subIN) {
			for (String year : years) {
				String rowYear = row.get(row.size() - 1);
				if (rowYear.equalsIgnoreCase(year)); {
					out.add(row.get(row.size() - 2));
					break;
				}
			}
		}
		return out;
	}
	
	private List<List<String>> filterCountry(List<List<String>> in, String country) {
		List<List<String>> out = new ArrayList<List<String>>();
		for (int i = 1 ; i < in.size() ; i++) {				// Jump headers row
			List<String> row = in.get(i);
			if (row.get(0).equalsIgnoreCase(country)) {		// Match Country Label
				out.add(row);
			}
		}
		return out;
	}
	
	private List<String> reshapeHeaders(List<String> headers, List<String> years) {
		List<String> l = new ArrayList<String>();
		for (int i = 0 ; i < headers.size() - 2 ; i++)
			l.add(headers.get(i));
		for (String y : years)
			l.add(y);
		return l;
	}
	
	private List<String> getCountries(List<List<String>> in) {
		List<String> countries = new ArrayList<String>();
		for (int i = 1 ; i < in.size() ; i++) {				// Jump headers row
			String y = in.get(i).get(0);					// 0 for Geographic Area Name
			if (!countries.contains(y))						// 1 for Geographic Area Code
				countries.add(y);
		}
		return countries;
	}
	
	private List<String> getYears(List<List<String>> in) {
		List<String> years = new ArrayList<String>();
		int idx = in.get(0).size() - 1;
		for (int i = 1 ; i < in.size() ; i++) {				// Jump headers row
			String y = in.get(i).get(idx);
			if (!years.contains(y))
				years.add(y);
		}
		return years;
	}
	
	public boolean timeseries(List<String> variables) {
		for (String v : variables)
			if (v.trim().endsWith("_years"))
				return true;
		return false;
	}
	*/
	
}