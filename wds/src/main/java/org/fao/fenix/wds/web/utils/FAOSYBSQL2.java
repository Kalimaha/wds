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

import java.util.ArrayList;
import java.util.List;

/** 
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class FAOSYBSQL2 {
	
	public static String queryByIndicator(String indicatorCode, String language) {
		if (indicatorCode.endsWith("_years")) {
			return queryByIndicatorTimeseries(indicatorCode, language);
		} else {
			return queryByIndicatorTimeless(indicatorCode, language);
		}
	}
	
	private static String queryByIndicatorTimeless(String indicatorCode, String language) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT G.label AS \"Geographic Area\", ");
		sb.append("I.geo_code AS \"Geographic Area Code\", ");
		sb.append("M.label AS \"Indicator\", ");
		sb.append("I.indicator_code AS \"Indicator Code\", ");
		sb.append("I.value AS \"Value\", ");
		sb.append("I.year AS \"Year\" ");
		sb.append("FROM	faosyb_indicator I, ");
		sb.append("faosyb_metadata M, ");
		sb.append("faosyb_metadata G ");
		sb.append("WHERE I.indicator_code = '").append(indicatorCode).append("' AND ");
		if (indicatorCode.contains("_")) {
			sb.append("M.indicator_code = substring('").append(indicatorCode).append("' from 0 for position('_' in '").append(indicatorCode).append("')) AND ");
		} else {
			sb.append("M.indicator_code = '").append(indicatorCode).append("' AND ");
		}
		sb.append("G.indicator_code = I.geo_code AND ");
		sb.append("M.language = '").append(language).append("' AND ");
		sb.append("G.language = '").append(language).append("' ");
		sb.append("GROUP BY	G.label, I.geo_code, M.label, I.indicator_code, I.value, I.year ");
		sb.append("ORDER BY G.label, I.year ");
		return sb.toString();
	}
	
	private static String queryByIndicatorTimeseries(String indicatorCode, String language) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT G.label AS \"Geographic Area\", ");
		sb.append("I.geo_code AS \"Geographic Area Code\", ");
		sb.append("M.label AS \"Indicator\", ");
		sb.append("I.indicator_code AS \"Indicator Code\", ");
		sb.append("I.value AS \"Value\", ");
		sb.append("I.year AS \"Year\" ");
		sb.append("FROM	faosyb_indicator I, ");
		sb.append("faosyb_metadata M, ");
		sb.append("faosyb_metadata G ");
		sb.append("WHERE I.indicator_code LIKE (substring('").append(indicatorCode).append("' from 0 for position('_' in '").append(indicatorCode).append("')) || '_%') AND ");
		sb.append("I.year IS NOT NULL AND ");
		sb.append("M.indicator_code = substring('").append(indicatorCode).append("' from 0 for position('_' in '").append(indicatorCode).append("')) AND ");
		sb.append("G.indicator_code = I.geo_code AND ");
		sb.append("M.language = '").append(language).append("' AND ");
		sb.append("G.language = '").append(language).append("' ");
		sb.append("GROUP BY	G.label, I.geo_code, M.label, I.indicator_code, I.value, I.year ");
		sb.append("ORDER BY G.label, I.year ");
		return sb.toString();
	}

	public static String getIndicatorCodeFromDataset(String datasetCode) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT indicator_code FROM faosyb_dataset WHERE dataset_code = '").append(datasetCode).append("' GROUP BY indicator_code ");
		return sb.toString();
	}
	
	public static String getIndicatorCodeFromObject(String objectCode) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT indicator_code FROM faosyb_object WHERE object_code = '").append(objectCode).append("' GROUP BY indicator_code  ");
		return sb.toString();
	}
	
	public static String getIndicatorCodeFromObjectSYB(String objectCodeSYB) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT indicator_code FROM faosyb_objectsyb WHERE objectsyb_code = '").append(objectCodeSYB).append("' GROUP BY indicator_code ");
		return sb.toString();
	}
	
	public static String listDatasets(String language) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT M.label AS \"Dataset Name\", ");
		sb.append("M.indicator_code AS \"Dataset Code\" ");
		sb.append("FROM	faosyb_metadata M, ");
		sb.append("faosyb_indicator I ");
		sb.append("WHERE I.indicator_code = M.indicator_code AND ");
		sb.append("M.language = '").append(language).append("' ");
		sb.append("GROUP BY M.label, M.indicator_code ");
		sb.append("ORDER BY M.label ");
		return sb.toString();
	}
	
	public static String queryByFindCodeSQL() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM faosyb_codes ORDER BY faostat_name ");
		return sb.toString();
	}
	
	public static List<String> headers() {
		List<String> headers = new ArrayList<String>();
		headers.add("Geographic Area");
		headers.add("Geographic Area Code");
		headers.add("Variable");
		headers.add("Variable Code");
		headers.add("Value");
		headers.add("Year");
		return headers;
	}
	
	public static List<String> headersForCodes() {
		List<String> headers = new ArrayList<String>();
		headers.add("FAOSTAT Name");
		headers.add("SYB Code");
		headers.add("FAOSTAT Code");
		headers.add("UN Code");
		headers.add("ISO2 Code");
		headers.add("ISO3 Code");
		return headers;
	}
	
	public static List<String> headersForDatasetsList() {
		List<String> headers = new ArrayList<String>();
		headers.add("Dataset Name");
		headers.add("Dataset Code");
		return headers;
	}
	
}