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

import org.fao.fenix.wds.core.exception.WDSException;
import org.fao.fenix.wds.core.bean.*;
import org.fao.fenix.wds.core.constant.*;
import org.fao.fenix.wds.core.constant.SQL;

import java.util.List;
import java.util.Map;

/** 
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class Bean2XML {
	
	public static StringBuilder convert(FWDSBean b) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("<").append(PAYLOAD.PAYLOAD.name()).append(">");
			StringBuilder db = convert(b.getDb());
			sb.append(db);
			StringBuilder sql = convert(b.getSql());
			sb.append(sql);
			sb.append("<").append(PAYLOAD.CSS.name()).append(">");
			sb.append(b.getCss());
			sb.append("</").append(PAYLOAD.CSS.name()).append(">");
			sb.append("</").append(PAYLOAD.PAYLOAD.name()).append(">");
			return sb;
		} catch (Exception e) {
            e.printStackTrace();
			throw new WDSException(e.getMessage());
		}
	}
	
	public static StringBuilder convert(DBBean b) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("<").append(DB.DB.name()).append(">");
			if (b.getDatasource() != null && !b.getDatasource().equals(SQL.NONE.name())) {
				sb.append(XMLTools.wrap(b.getDatasource().name(), DB.DATASOURCE.name()));
			} else {
				sb.append(XMLTools.wrap(SQL.NONE.name(), DB.DATASOURCE.name()));
			}
			if (b.getDriver() != null && !b.getDriver().equals(SQL.NONE.name())) {
				sb.append(XMLTools.wrap(b.getDriver(), DB.DRIVER.name()));
			} else {
				sb.append(XMLTools.wrap(SQL.NONE.name(), DB.DRIVER.name()));
			}
			if (b.getPassword() != null && !b.getPassword().equals(SQL.NONE.name())) {
				sb.append(XMLTools.wrap(b.getPassword(), DB.PASSWORD.name()));
			} else {
				sb.append(XMLTools.wrap(SQL.NONE.name(), DB.PASSWORD.name()));
			}
			if (b.getUrl() != null && !b.getUrl().equals(SQL.NONE.name())) {
				sb.append(XMLTools.wrap(b.getUrl(), DB.URL.name()));
			} else {
				sb.append(XMLTools.wrap(SQL.NONE.name(), DB.URL.name()));
			}
			if (b.getUsername() != null && !b.getUsername().equals(SQL.NONE.name())) {
				sb.append(XMLTools.wrap(b.getUsername(), DB.USERNAME.name()));
			} else {
				sb.append(XMLTools.wrap(SQL.NONE.name(), DB.USERNAME.name()));
			}
			if (b.getConnection() != null && !b.getConnection().equals(SQL.NONE.name())) {
				sb.append(XMLTools.wrap(b.getConnection().name(), DB.CONNECTION.name()));
			} else {
				sb.append(XMLTools.wrap(SQL.NONE.name(), DB.CONNECTION.name()));
			}
			sb.append("</").append(DB.DB.name()).append(">");
			return sb;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static StringBuilder convert(RBean b) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("<").append(PAYLOAD.PAYLOAD.name()).append(">");
			sb.append(convert(b.getDb()));
			if (!b.getSqlBeans().isEmpty()) {
				sb.append("<").append(SQL.SQLS.name()).append(">");
				for (SQLBean sqlBean : b.getSqlBeans())
					sb.append(convert(sqlBean));
				sb.append("</").append(SQL.SQLS.name()).append(">");
			}
			sb.append("<").append(R.R.name()).append(">");
			sb.append(XMLTools.wrap(b.getScript(), R.SCRIPT.name()));
			sb.append(XMLTools.wrap(b.getFunction(), R.FUNCTION.name()));
			sb.append(XMLTools.wrap(String.valueOf(b.isSplitSQL()), R.SPLITSQL.name()));
			sb.append(XMLTools.wrap(String.valueOf(b.isShowCharts()), R.CHARTS.name()));
			sb.append(XMLTools.wrap(b.getSql(), R.SQL.name()));
			sb.append(convert(b.getParameters()));
			sb.append("</").append(R.R.name()).append(">");
			sb.append(convert(b.getRawValues()));
			sb.append("</").append(PAYLOAD.PAYLOAD.name()).append(">");
			return sb;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static StringBuilder convert(List<Double> l) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			if (!l.isEmpty()) {
				sb.append("<").append(PAYLOAD.RAWVALUES.name()).append(">");
				for (Double d : l)
					sb.append("<").append(PAYLOAD.RAWVALUE.name()).append(">").append(d).append("</").append(PAYLOAD.RAWVALUE.name()).append(">");
				sb.append("</").append(PAYLOAD.RAWVALUES.name()).append(">");
			}
			return sb;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static StringBuilder convert(Map<String, String> m) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("<").append(R.PARAMETERS.name()).append(">");
			for (String key : m.keySet()) {
				sb.append("<").append(R.PARAMETER.name()).append(">");
				sb.append(XMLTools.wrap(key, R.NAME.name()));
				sb.append(XMLTools.wrap(m.get(key), R.VALUE.name()));
				sb.append("</").append(R.PARAMETER.name()).append(">");
			}
			sb.append("</").append(R.PARAMETERS.name()).append(">");
			return sb;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}

	public static StringBuilder convert(SQLBean b) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("<").append("SQL").append(">");
			sb.append(convertSelects(b.getSelects()));
			sb.append(convertFroms(b.getFroms()));
			sb.append(convertWheres(b.getWheres()));
			sb.append(convertGroupBys(b.getGroupBys()));
			sb.append(convertOrderBys(b.getOrderBys()));
			if (b.getLimit() != null && !b.getLimit().equals(SQL.NONE.name())) {
				sb.append(XMLTools.wrap(b.getLimit(), SQL.LIMIT.name()));
			} else {
				sb.append(XMLTools.wrap(SQL.NONE.name(), SQL.LIMIT.name()));
			}
            String sql = b.getQuery().replaceAll(">", "&gt;").replaceAll("<", "&lt;");
			sql = XMLTools.wrap(sql, SQL.QUERY.name());
			sb.append(sql);
			sb.append("</").append(SQL.SQL.name()).append(">");
			return sb;
		} catch (Exception e) {
            e.printStackTrace();
            throw new WDSException(e.getMessage());
		}
	}
	
	public static StringBuilder convertOrderBys(List<OrderByBean> beans) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			if (!beans.isEmpty()) {
				sb.append("<").append(ORDERBY.ORDERBYS.name()).append(">");
				for (OrderByBean b : beans)
					sb.append(convertOrderBy(b));
				sb.append("</").append(ORDERBY.ORDERBYS.name()).append(">");
			}
			return sb;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static StringBuilder convertOrderBy(OrderByBean b) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("<").append(ORDERBY.ORDERBY.name()).append(">");
			sb.append(XMLTools.wrap(b.getColumn(), ORDERBY.COLUMN.name()));
			sb.append(XMLTools.wrap(b.getDirection(), ORDERBY.DIRECTION.name()));
			sb.append("</").append(ORDERBY.ORDERBY.name()).append(">");
			return sb;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static StringBuilder convertGroupBys(List<GroupByBean> beans) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			if (!beans.isEmpty()) {
				sb.append("<").append(GROUPBY.GROUPBYS.name()).append(">");
				for (GroupByBean b : beans)
					sb.append(convertGroupBy(b));
				sb.append("</").append(GROUPBY.GROUPBYS.name()).append(">");
			}
			return sb;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static StringBuilder convertGroupBy(GroupByBean b) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("<").append(GROUPBY.GROUPBY.name()).append(">");
			sb.append(XMLTools.wrap(b.getColumn(), GROUPBY.COLUMN.name()));
			sb.append("</").append(GROUPBY.GROUPBY.name()).append(">");
			return sb;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static StringBuilder convertWheres(List<WhereBean> beans) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			if (!beans.isEmpty()) {
				sb.append("<").append(WHERE.WHERES.name()).append(">");
				for (WhereBean b : beans)
					sb.append(convertWhere(b));
				sb.append("</").append(WHERE.WHERES.name()).append(">");
			}
			return sb;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static StringBuilder convertWhere(WhereBean b) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("<").append(WHERE.WHERE.name()).append(">");
			sb.append(XMLTools.wrap(b.getColumn(), WHERE.COLUMN.name()));
			sb.append(XMLTools.wrap(b.getDatatype(), WHERE.DATATYPE.name()));
			String operator = b.getOperator().replaceAll(">", "&gt;").replaceAll("<", "&lt;");
			sb.append(XMLTools.wrap(operator, WHERE.OPERATOR.name()));
			if (b.getValue() != null)
				sb.append(XMLTools.wrap(b.getValue(), WHERE.VALUE.name()));
			if (!b.getIns().isEmpty()) {
				sb.append("<").append(WHERE.INS.name()).append(">");
				for (String in : b.getIns()) {
					sb.append(XMLTools.wrap(in, WHERE.IN.name()));
				}
				sb.append("</").append(WHERE.INS.name()).append(">");
			}
			sb.append("</").append(WHERE.WHERE.name()).append(">");
			return sb;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static StringBuilder convertFroms(List<FromBean> beans) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			if (!beans.isEmpty()) {
				sb.append("<").append(FROM.FROMS.name()).append(">");
				for (FromBean b : beans)
					sb.append(convertFrom(b));
				sb.append("</").append(FROM.FROMS.name()).append(">");
			}
			return sb;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static StringBuilder convertFrom(FromBean b) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("<").append(FROM.FROM.name()).append(">");
			sb.append(XMLTools.wrap(b.getAlias(), FROM.ALIAS.name()));
			sb.append(XMLTools.wrap(b.getColumn(), FROM.COLUMN.name()));
			sb.append("</").append(FROM.FROM.name()).append(">");
			return sb;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static StringBuilder convertSelects(List<SelectBean> beans) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			if (!beans.isEmpty()) {
				sb.append("<").append(SELECT.SELECTS.name()).append(">");
				for (SelectBean b : beans)
					sb.append(convertSelect(b));
				sb.append("</").append(SELECT.SELECTS.name()).append(">");
			}
			return sb;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static StringBuilder convertSelect(SelectBean b) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("<").append(SELECT.SELECT.name()).append(">");
			sb.append(XMLTools.wrap(b.getAggregation(), SELECT.AGGREGATION.name()));
			sb.append(XMLTools.wrap(b.getAlias(), SELECT.ALIAS.name()));
			sb.append(XMLTools.wrap(b.getColumn(), SELECT.COLUMN.name()));
			sb.append("</").append(SELECT.SELECT.name()).append(">");
			return sb;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
}