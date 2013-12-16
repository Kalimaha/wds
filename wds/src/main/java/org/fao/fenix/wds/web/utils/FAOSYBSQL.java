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

import org.fao.fenix.wds.core.bean.DBBean;
import org.fao.fenix.wds.core.bean.SQLBean;
import org.fao.fenix.wds.core.constant.DATASOURCE;
import org.fao.fenix.wds.core.exception.WDSException;
import org.fao.fenix.wds.core.jdbc.JDBCConnector;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FAOSYBSQL {
	
	private static FAOSYBParameters faosybParameters;
	
	static {
		try {
			faosybParameters = getFAOSYBParameters();
		} catch (WDSException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static FAOSYBParameters getFAOSYBParameters() throws IllegalAccessException, InstantiationException, SQLException, ClassNotFoundException, WDSException {
		FAOSYBParameters p = new FAOSYBParameters();
		List<List<String>> table = JDBCConnector.query(new DBBean(DATASOURCE.FAOSYB), FAOSYBSQL.getFAOSYBParametersSQL(), false);
		p.setDataID(Long.valueOf(table.get(0).get(0)));
		p.setMetaID(Long.valueOf(table.get(0).get(1)));
		p.setParametersID(Long.valueOf(table.get(0).get(2)));
		p.setTypesID(Long.valueOf(table.get(0).get(3)));
		return p;
	}

	public static String queryByFindCodeSQL() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM faosyb_codes ORDER BY faostat_name ");
		return sb.toString();
	}
	
	public static String queryByObjectSYBSQL(String objectSYBCode) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT	GA.firstindicator AS GeographicArea, ");
		sb.append("D.firstindicator AS GeographicAreaCode, ");
		sb.append("M.firstindicator AS Variable, ");
		sb.append("D.secondindicator AS VariableCode, ");
		sb.append("D.quantity AS Value, ");
		sb.append("date_part('year', D.date) AS Year ");
		sb.append("FROM	quantitativecorecontent D, ");
		sb.append("quantitativecorecontent P, ");
		sb.append("quantitativecorecontent M, ");
		sb.append("quantitativecorecontent GA ");
		sb.append("WHERE D.datasetid = ").append(faosybParameters.getDataID()).append(" AND ");
		sb.append("P.datasetid = ").append(faosybParameters.getParametersID()).append(" AND ");
		sb.append("M.datasetid = ").append(faosybParameters.getMetaID()).append(" AND ");
		sb.append("GA.datasetid = ").append(faosybParameters.getMetaID()).append(" AND ");
		sb.append("D.secondindicator IN (SELECT trim('_years' FROM commoditycode) ");
		sb.append("FROM quantitativecorecontent ");
		sb.append("WHERE firstindicator = '").append(objectSYBCode.toUpperCase()).append("' ");
		sb.append("AND datasetid = ").append(faosybParameters.getParametersID()).append(") AND ");
//		sb.append("M.secondindicator = (SELECT secondindicator ");
		sb.append("M.secondindicator IN (SELECT secondindicator ");
		sb.append("FROM quantitativecorecontent ");
		sb.append("WHERE firstindicator = '").append(objectSYBCode.toUpperCase()).append("' ");
		sb.append("AND datasetid = ").append(faosybParameters.getParametersID()).append(" ");
		sb.append("GROUP BY secondindicator) AND ");
		sb.append("D.firstindicator = GA.secondindicator ");
		sb.append("GROUP BY GeographicArea, GeographicAreaCode, VariableCode, Variable, Value, D.date ");
		sb.append("ORDER BY GA.firstindicator, M.firstindicator, D.date ");
		return sb.toString();
	}
	
	public static String queryByIndicatorSQL(String indicatorCode) {
		boolean timeseries = indicatorCode.endsWith("_years");
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT GA.firstindicator AS GeographicArea, ");
		sb.append("D.firstindicator AS GeographicAreaCode, ");
		sb.append("M.firstindicator AS Variable, ");
		sb.append("D.secondindicator AS VariableCode, ");
		sb.append("D.quantity AS Value, ");
		sb.append("date_part('year', D.date) AS Year ");
		sb.append("FROM	quantitativecorecontent D, ");
		sb.append("quantitativecorecontent P, ");
		sb.append("quantitativecorecontent M, ");
		sb.append("quantitativecorecontent GA ");
		sb.append("WHERE D.datasetid = ").append(faosybParameters.getDataID()).append("");
		sb.append("AND P.datasetid = ").append(faosybParameters.getParametersID()).append(" ");
		sb.append("AND M.datasetid = ").append(faosybParameters.getMetaID()).append(" ");
		sb.append("AND GA.datasetid = ").append(faosybParameters.getMetaID()).append(" ");
		sb.append("AND P.commoditycode = '").append(indicatorCode).append("' ");
		if (timeseries) {
			sb.append("AND D.secondindicator = trim('_years' from P.commoditycode) ");
		} else {
			sb.append("AND D.secondindicator = P.commoditycode ");
		}
		sb.append("AND P.secondindicator = M.secondindicator ");
		sb.append("AND D.firstindicator = GA.secondindicator ");
		sb.append("GROUP BY GeographicArea, GeographicAreaCode, VariableCode, Variable, Value, D.date ");
		sb.append("ORDER BY GA.firstindicator, M.firstindicator, D.date ");
		return sb.toString();
	}
	
	public static String queryByListDatasetSQL(String datasetCode) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT firstindicator, secondindicator ");
		sb.append("FROM quantitativecorecontent ");
		sb.append("WHERE datasetid = ").append(faosybParameters.getMetaID()).append(" AND ");
		sb.append("secondindicator IN (");
		sb.append("SELECT secondindicator ");
		sb.append("FROM quantitativecorecontent ");
		sb.append("WHERE datasetid = ").append(faosybParameters.getParametersID()).append(" ");
		sb.append("GROUP BY secondindicator) ");
		sb.append("GROUP BY firstindicator, secondindicator ");
		sb.append("ORDER BY firstindicator, secondindicator ");
		return sb.toString();
	}
	
	public static String queryByDatasetSQL(String datasetCode) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT GA.firstindicator AS GeographicArea, ");
		sb.append("D.firstindicator AS GeographicAreaCode, ");
		sb.append("M.firstindicator AS Variable, ");
		sb.append("D.secondindicator AS VariableCode, ");
		sb.append("D.quantity AS Value, ");
		sb.append("date_part('year', D.date) AS Year ");
		sb.append("FROM	quantitativecorecontent D, ");
		sb.append("quantitativecorecontent P, ");
		sb.append("quantitativecorecontent M, ");
		sb.append("quantitativecorecontent GA ");
		sb.append("WHERE D.datasetid = ").append(faosybParameters.getDataID()).append(" AND ");
		sb.append("P.datasetid = ").append(faosybParameters.getParametersID()).append(" AND ");
		sb.append("M.datasetid = ").append(faosybParameters.getMetaID()).append(" AND ");
		sb.append("GA.datasetid = ").append(faosybParameters.getMetaID()).append(" AND ");
		sb.append("D.secondindicator IN (SELECT trim('_years' FROM commoditycode) ");
		sb.append("FROM quantitativecorecontent ");
		sb.append("WHERE secondindicator = '").append(datasetCode.toUpperCase()).append("' ");
		sb.append("AND datasetid = ").append(faosybParameters.getParametersID()).append(") AND ");
		sb.append("M.secondindicator = '").append(datasetCode.toUpperCase()).append("' AND ");
		sb.append("D.firstindicator = GA.secondindicator ");
		sb.append("GROUP BY GeographicArea, GeographicAreaCode, VariableCode, Variable, Value, D.date ");
		sb.append("ORDER BY GA.firstindicator, M.firstindicator, D.date ");
		return sb.toString();
	}
	
	public static String queryByObjectSQL(String objectCode) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT GA.firstindicator AS GeographicArea, ");
		sb.append("D.firstindicator AS GeographicAreaCode, ");
		sb.append("M.firstindicator AS Variable, ");
		sb.append("D.secondindicator AS VariableCode, ");
		sb.append("D.quantity AS Value, ");
		sb.append("date_part('year', D.date) AS Year ");
		sb.append("FROM	quantitativecorecontent D, ");
		sb.append("quantitativecorecontent P, ");
		sb.append("quantitativecorecontent M, ");
		sb.append("quantitativecorecontent GA ");
		sb.append("WHERE D.datasetid = ").append(faosybParameters.getDataID()).append(" AND ");
		sb.append("P.datasetid = ").append(faosybParameters.getParametersID()).append(" AND ");
		sb.append("M.datasetid = ").append(faosybParameters.getMetaID()).append(" AND ");
		sb.append("GA.datasetid = ").append(faosybParameters.getMetaID()).append(" AND ");
		sb.append("D.secondindicator IN (SELECT commoditycode ");
		sb.append("FROM quantitativecorecontent ");
		sb.append("WHERE secondindicator = (SELECT	P.secondindicator ");
		sb.append("FROM	quantitativecorecontent P, ");
		sb.append("quantitativecorecontent T ");
		sb.append("WHERE P.datasetid = ").append(faosybParameters.getParametersID()).append(" AND ");
		sb.append("T.datasetid = ").append(faosybParameters.getTypesID()).append(" AND ");
		sb.append("P.firstindicator = T.firstindicator AND ");
		sb.append("T.secondindicator = '").append(objectCode.toUpperCase()).append("' ");
		sb.append("GROUP BY P.secondindicator) ");
		sb.append("AND datasetid = ").append(faosybParameters.getParametersID()).append(" AND ");
		sb.append("firstindicator = (SELECT T.firstindicator ");
		sb.append("FROM quantitativecorecontent T ");
		sb.append("WHERE T.datasetid = ").append(faosybParameters.getTypesID()).append(" AND ");
		sb.append("T.secondindicator = '").append(objectCode.toUpperCase()).append("' ");
		sb.append("GROUP BY T.firstindicator)) AND ");
		sb.append("M.secondindicator = (SELECT	P.secondindicator ");
		sb.append("FROM	quantitativecorecontent P, ");
		sb.append("quantitativecorecontent T ");
		sb.append("WHERE P.datasetid = ").append(faosybParameters.getParametersID()).append(" AND ");
		sb.append("T.datasetid = ").append(faosybParameters.getTypesID()).append(" AND ");
		sb.append("P.firstindicator = T.firstindicator AND ");
		sb.append("T.secondindicator = '").append(objectCode.toUpperCase()).append("' ");
		sb.append("GROUP BY P.secondindicator) AND ");
		sb.append("D.firstindicator = GA.secondindicator ");
		sb.append("GROUP BY GeographicArea, GeographicAreaCode, VariableCode, Variable, Value, D.date ");
		sb.append("ORDER BY GA.firstindicator, M.firstindicator, D.date ");
		return sb.toString();
	}
	
	public static SQLBean getFAOSYBParametersSQL() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT D.resourceid AS DATA_ID, ");
		sb.append("M.resourceid AS META_ID, ");
		sb.append("P.resourceid AS PARA_ID, ");
		sb.append("T.resourceid AS TYPE_ID ");
		sb.append("FROM coredataset D, ");
		sb.append("coredataset M, ");
		sb.append("coredataset P, ");
		sb.append("coredataset T ");
		sb.append("WHERE D.code = 'FAOSYB_DATA' AND ");
		sb.append("M.code = 'FAOSYB_META' AND ");
		sb.append("P.code = 'FAOSYB_PARAMETERS' AND ");
		sb.append("T.code = 'FAOSYB_TYPES' ");
		return new SQLBean(sb.toString());
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
	
}