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

import org.fao.fenix.wds.core.bean.*;
import org.fao.fenix.wds.core.constant.*;
import org.fao.fenix.wds.core.exception.WDSException;
import org.fao.fenix.wds.core.sql.SQLSplitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** 
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class XML2Bean {
	
	public static DBBean convertDB(String xml) throws WDSException {
		try {
			DBBean b = new DBBean();
			DATASOURCE datasource = DATASOURCE.valueOf(XMLTools.readChildTag(xml, DB.DB.name(), DB.DATASOURCE.name(), PAYLOAD.PAYLOAD.name()));
			b.setDatasource(datasource);
			if (b.getDatasource() != null && !b.getDatasource().equals("") && !b.getDatasource().equals(SQL.NONE.name())) {
//				DATASOURCE ds = DATASOURCE.valueOf(b.getDatasource().toUpperCase());
				b = new DBBean(datasource);
				return b;
			}
			b.setDriver(XMLTools.readChildTag(xml, DB.DB.name(), DB.DRIVER.name(), PAYLOAD.PAYLOAD.name()));
			b.setPassword(XMLTools.readChildTag(xml, DB.DB.name(), DB.PASSWORD.name(), PAYLOAD.PAYLOAD.name()));
			b.setUrl(XMLTools.readChildTag(xml, DB.DB.name(), DB.URL.name(), PAYLOAD.PAYLOAD.name()));
			b.setUsername(XMLTools.readChildTag(xml, DB.DB.name(), DB.USERNAME.name(), PAYLOAD.PAYLOAD.name()));
			b.setConnection(CONNECTION.valueOf(XMLTools.readChildTag(xml, DB.DB.name(), DB.CONNECTION.name(), PAYLOAD.PAYLOAD.name()).toUpperCase()));
			return b;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static SQLBean convertSQL(String xml) throws WDSException {
		try {
			SQLBean b = new SQLBean();
			String sql = XMLTools.readChildTag(xml, SQL.SQL.name(), SQL.QUERY.name(), PAYLOAD.PAYLOAD.name());
			sql = sql.replaceAll("&gt;", ">").replaceAll("&lt;", "<");
			b.setQuery(sql);
			if (b.getQuery() != null && !b.getQuery().equals("") && !b.getQuery().equals(SQL.NONE.name())) {
				Map<String, String> aliases = SQLSplitter.selectColumnAliasMap(b.getQuery());
				for (String column : aliases.keySet()) 
					b.select(null, column, aliases.get(column));
				return b;
			} else {
				b.setFroms(convertFroms(xml));
				b.setGroupBys(convertGroupBys(xml));
				b.setOrderBys(convertOrderBys(xml));
				b.setSelects(convertSelects(xml));
				b.setWheres(convertWheres(xml));
				b.setLimit(XMLTools.readChildTag(xml, SQL.SQL.name(), SQL.LIMIT.name(), PAYLOAD.PAYLOAD.name()));
				return b;
			}
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static List<SelectBean> convertSelects(String xml) throws WDSException {
		try {
			List<SelectBean> beans = new ArrayList<SelectBean>();
			String[] tags = new String[]{SQL.SQL.name(), SELECT.SELECTS.name(), SELECT.SELECT.name()};
			List<String> subs = XMLTools.subXML(xml, tags, PAYLOAD.PAYLOAD.name());
			for (String sub : subs)
				beans.add(convertSelect(sub));
			return beans;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static SelectBean convertSelect(String xml) throws WDSException {
		try {
			SelectBean b = new SelectBean();
			b.setAggregation(XMLTools.readChildTag(xml, SELECT.SELECT.name(), SELECT.AGGREGATION.name(), PAYLOAD.PAYLOAD.name()));
			b.setAlias(XMLTools.readChildTag(xml, SELECT.SELECT.name(), SELECT.ALIAS.name(), PAYLOAD.PAYLOAD.name()));
			b.setColumn(XMLTools.readChildTag(xml, SELECT.SELECT.name(), SELECT.COLUMN.name(), PAYLOAD.PAYLOAD.name()));
			return b;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static List<FromBean> convertFroms(String xml) throws WDSException {
		try {
			List<FromBean> beans = new ArrayList<FromBean>();
			String[] tags = new String[]{SQL.SQL.name(), FROM.FROMS.name(), FROM.FROM.name()};
			List<String> subs = XMLTools.subXML(xml, tags, PAYLOAD.PAYLOAD.name());
			for (String sub : subs)
				beans.add(convertFrom(sub));
			return beans;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static FromBean convertFrom(String xml) throws WDSException {
		try {
			FromBean b = new FromBean();
			b.setAlias(XMLTools.readChildTag(xml, FROM.FROM.name(), FROM.ALIAS.name(), PAYLOAD.PAYLOAD.name()));
			b.setColumn(XMLTools.readChildTag(xml, FROM.FROM.name(), FROM.COLUMN.name(), PAYLOAD.PAYLOAD.name()));
			return b;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static List<WhereBean> convertWheres(String xml) throws WDSException {
		try {
			List<WhereBean> beans = new ArrayList<WhereBean>();
			String[] tags = new String[]{SQL.SQL.name(), WHERE.WHERES.name(), WHERE.WHERE.name()};
			List<String> subs = XMLTools.subXML(xml, tags, PAYLOAD.PAYLOAD.name());
			for (String sub : subs)
				beans.add(convertWhere(sub));
			return beans;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static WhereBean convertWhere(String xml) throws WDSException {
		try {
			WhereBean b = new WhereBean();
			b.setColumn(XMLTools.readChildTag(xml, WHERE.WHERE.name(), WHERE.COLUMN.name(), PAYLOAD.PAYLOAD.name()));
			b.setDatatype(XMLTools.readChildTag(xml, WHERE.WHERE.name(), WHERE.DATATYPE.name(), PAYLOAD.PAYLOAD.name()));
			String operator = XMLTools.readChildTag(xml, WHERE.WHERE.name(), WHERE.OPERATOR.name(), PAYLOAD.PAYLOAD.name());
			operator = operator.replaceAll("&gt;", ">").replaceAll("&lt;", "<");
			b.setOperator(operator);
			b.setValue(XMLTools.readChildTag(xml, WHERE.WHERE.name(), WHERE.VALUE.name(), PAYLOAD.PAYLOAD.name()));
			b.setIns(convertIns(xml));
			return b;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static List<String> convertIns(String xml) throws WDSException {
		try {
			List<String> ins = new ArrayList<String>();
			String[] tags = new String[]{SQL.SQL.name(), WHERE.WHERE.name(), WHERE.INS.name()};
			List<String> subs = XMLTools.subXML(xml, tags, PAYLOAD.PAYLOAD.name());
			for (String sub : subs) 
				ins = XMLTools.readSubCollection(sub, new String[]{WHERE.INS.name(), WHERE.IN.name()}, PAYLOAD.PAYLOAD.name());
			return ins;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static List<GroupByBean> convertGroupBys(String xml) throws WDSException {
		try {
			List<GroupByBean> beans = new ArrayList<GroupByBean>();
			String[] tags = new String[]{SQL.SQL.name(), GROUPBY.GROUPBYS.name(), GROUPBY.GROUPBY.name()};
			List<String> subs = XMLTools.subXML(xml, tags, PAYLOAD.PAYLOAD.name());
			for (String sub : subs)
				beans.add(convertGroupBy(sub));
			return beans;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static GroupByBean convertGroupBy(String xml) throws WDSException {
		try {
			GroupByBean b = new GroupByBean();
			b.setColumn(XMLTools.readChildTag(xml, GROUPBY.GROUPBY.name(), GROUPBY.COLUMN.name(), PAYLOAD.PAYLOAD.name()));
			return b;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static List<OrderByBean> convertOrderBys(String xml) throws WDSException {
		try {
			List<OrderByBean> beans = new ArrayList<OrderByBean>();
			String[] tags = new String[]{SQL.SQL.name(), ORDERBY.ORDERBYS.name(), ORDERBY.ORDERBY.name()};
			List<String> subs = XMLTools.subXML(xml, tags, PAYLOAD.PAYLOAD.name());
			for (String sub : subs)
				beans.add(convertOrderBy(sub));
			return beans;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static OrderByBean convertOrderBy(String xml) throws WDSException {
		try {
			OrderByBean b = new OrderByBean();
			b.setColumn(XMLTools.readChildTag(xml, ORDERBY.ORDERBY.name(), ORDERBY.COLUMN.name(), PAYLOAD.PAYLOAD.name()));
			b.setDirection(XMLTools.readChildTag(xml, ORDERBY.ORDERBY.name(), ORDERBY.DIRECTION.name(), PAYLOAD.PAYLOAD.name()));
			return b;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static WSBean convertWS(String xml) throws WDSException {
		try {
			WSBean b = new WSBean();
			b.setUrl(XMLTools.readChildTag(xml, WS.WS.name(), WS.URL.name(), PAYLOAD.PAYLOAD.name()));
			String[] tags = new String[]{WS.WS.name(), WS.PARAMETERS.name(), WS.PARAMETER.name()};
			Map<String, String> m = XMLTools.readCollection(xml, tags, WS.NAME.name(), WS.VALUE.name());
			b.setParameters(m);
			return b;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static RESTBean convertREST(String xml) throws WDSException {
		try {
			RESTBean b = new RESTBean();
			b.setUrl(XMLTools.readChildTag(xml, REST.REST.name(), REST.URL.name(), PAYLOAD.PAYLOAD.name()));
			String[] tags = new String[]{REST.REST.name(), REST.PARAMETERS.name(), REST.PARAMETER.name()};
			Map<String, String> m = XMLTools.readCollection(xml, tags, REST.NAME.name(), REST.VALUE.name());
			b.setParameters(m);
			return b;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static RBean convertR(String xml) throws WDSException {
		
		try {
			
			RBean b = new RBean();
			b.setFunction(XMLTools.readChildTag(xml, R.R.name(), R.FUNCTION.name(), PAYLOAD.PAYLOAD.name()));
			boolean splitSQL = false;
			
			if (XMLTools.readChildTag(xml, R.R.name(), R.SPLITSQL.name(), PAYLOAD.PAYLOAD.name()) != null)
				splitSQL = Boolean.valueOf(XMLTools.readChildTag(xml, R.R.name(), R.SPLITSQL.name(), PAYLOAD.PAYLOAD.name()));
			
			if (splitSQL) {
				List<String> subs = XMLTools.subXML(xml, new String[]{SQL.SQLS.name(), SQL.SQL.name()}, PAYLOAD.PAYLOAD.name());
				for (String sub : subs) {
					String sqlScript = XMLTools.readChildTag(sub, SQL.SQL.name(), SQL.QUERY.name(), PAYLOAD.PAYLOAD.name());
					b.setSql(sqlScript, splitSQL);
				}
			} else {
				List<String> subs = XMLTools.subXML(xml, new String[]{SQL.SQLS.name(), SQL.SQL.name()}, PAYLOAD.PAYLOAD.name());
				for (String sub : subs) {
					SQLBean sqlBean = convertSQL(sub);
					b.addSQL(sqlBean);
				}
			}
			
			if (b.getFunction() == null || b.getFunction().equals("") || b.getFunction().equals(SQL.NONE.name()))
				throw new WDSException("Function has not been set.");
			b.setScript(XMLTools.readChildTag(xml, R.R.name(), R.SCRIPT.name(), PAYLOAD.PAYLOAD.name()));
			b.setShowCharts(Boolean.valueOf(XMLTools.readChildTag(xml, R.R.name(), R.CHARTS.name(), PAYLOAD.PAYLOAD.name())));
			String[] tags = new String[]{R.R.name(), R.PARAMETERS.name(), R.PARAMETER.name()};
			Map<String, String> m = XMLTools.readCollection(xml, tags, R.NAME.name(), R.VALUE.name(), PAYLOAD.PAYLOAD.name());
			b.setParameters(m);
			DBBean db = convertDB(xml);
			b.setDb(db);
			List<Double> rawValues = convertRawValues(xml);
			if (!rawValues.isEmpty())
				b.setRawValues(rawValues);
			return b;
			
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
		
	}
	
	public static List<Double> convertRawValues(String xml) {
		List<Double> l = new ArrayList<Double>();
		List<String> subs = XMLTools.subXML(xml, new String[]{PAYLOAD.RAWVALUES.name(), PAYLOAD.RAWVALUE.name()}, PAYLOAD.PAYLOAD.name());
		for (String s : subs) {
			String v = XMLTools.readTag(s, PAYLOAD.RAWVALUE.name(), PAYLOAD.PAYLOAD.name());
			l.add(Double.valueOf(v));
		}
		return l;
	}
	
}