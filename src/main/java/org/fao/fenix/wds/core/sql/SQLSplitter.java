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
package org.fao.fenix.wds.core.sql;

import org.fao.fenix.wds.core.exception.WDSException;
import org.fao.fenix.wds.core.utils.CSV2Bean;
import org.fao.fenix.wds.core.bean.*;
import org.fao.fenix.wds.core.constant.*;

import java.util.*;
import java.util.regex.Pattern;

/**
 * This class is used to split a single SQL in multiple queries using
 * <code>VariableBean</code> to build a matrix suitable for R analysis. A new SQL
 * is created for each entry in the SELECT clause. A new WHERE entry is created
 * based on the <code>VariableBean</code>'s <code>column</code> field.
 * 
 * 
 * Example - INPUT
 * ============================================================================
 * SQL: 		SELECT Value Production, Value as Yield, Value AS AreaHarvested
 * 				FROM Data 
 * 				WHERE ItemCode = '15' 
 * 				AND DomainCode = 'QC' 
 * 				AND AreaCode = '2'
 * Column: 		ElementCode
 * Variables:	5510, 5419, 5312
 * 
 * 
 * Example - OUTPUT
 * ============================================================================
 * SQL1:		SELECT Value AS Production
 * 				WHERE ItemCode = '15' 
 * 				AND DomainCode = 'QC' 
 * 				AND AreaCode = '2'
 * 				AND ElementCode = '5510'
 * SQL2:		SELECT Value AS Yield
 * 				WHERE ItemCode = '15' 
 * 				AND DomainCode = 'QC' 
 * 				AND AreaCode = '2'
 * 				AND ElementCode = '5419'
 * SQL3:		SELECT Value AS AreaHarvested
 * 				WHERE ItemCode = '15' 
 * 				AND DomainCode = 'QC' 
 * 				AND AreaCode = '2'
 * 				AND ElementCode = '5312'
 * 
 */

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class SQLSplitter {

	public static List<SQLBean> split(SQLBean b) throws WDSException {
		try {
			if (b.getQuery() != null && !b.getQuery().equals("") && !b.getQuery().equals(SQL.NONE.name())) {
				return splitSQL(b.getQuery());
			} else {
				return splitBean(b);
			}
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static List<SQLBean> splitSQL(String sql) throws WDSException {
		try {
			List<SQLBean> l = new ArrayList<SQLBean>();
//			Map<String, String> selectColumnAliasMap = selectColumnAliasMap(sql);
			String[][] selectColumnAliasMap = selectColumnAliasMatrix(sql);
			List<FromBean> froms = parseFroms(sql);
			List<WhereBean> wheres = parseWheres(sql);
			List<GroupByBean> groupBys = parseGroupBys(sql);
			List<OrderByBean> orderBys = parseOrderBys(sql);
			VariableBean v = parseVariables(sql);
			for (int i = 0 ; i < selectColumnAliasMap.length ; i++) {
				SQLBean tmp = new SQLBean();
				tmp.select(new SelectBean(null, selectColumnAliasMap[i][0], selectColumnAliasMap[i][1]));
				tmp.from(froms);
				tmp.where(wheres);
				tmp.groupBy(groupBys);
				tmp.orderBy(orderBys);
//				tmp.setLimit(parseLimit(sql)); // TODO restore me
				WhereBean w = new WhereBean();
				w.setColumn(v.getColumn());
				w.setValue(v.getVariables().get(i));
				tmp.where(w);
				l.add(tmp);
			}
			return l;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WDSException(e.getMessage());
		}
	}
	
	public static List<SQLBean> splitBean(SQLBean b) throws WDSException {
		try {
			List<SQLBean> l = new ArrayList<SQLBean>();
			for (SelectBean sb : b.getSelects()) {
				SQLBean tmp = new SQLBean();
				tmp.select(sb);
				tmp.from(b.getFroms());
				tmp.where(b.getWheres());
				tmp.groupBy(b.getGroupBys());
				tmp.orderBy(b.getOrderBys());
				tmp.setLimit(b.getLimit());
				l.add(tmp);
			}
			return l;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static String parseLimit(String sql) throws WDSException {
		try {
			String limit = null;
			int start = 1 + SQL.LIMIT.name().length() + sql.indexOf(SQL.LIMIT.name());
			if (start > -1) {
				limit = sql.substring(start).trim();
			}
			return limit;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static List<FromBean> parseFroms(String sql) throws WDSException {
		try {
			List<FromBean> l = new ArrayList<FromBean>();
			int start = 1 + FROM.FROM.name().length() + sql.indexOf(FROM.FROM.name());
			int end   = sql.indexOf(WHERE.WHERE.name());
			if (start > -1 && end > -1) {
				sql = sql.substring(start, end);
				StringTokenizer st1 = new StringTokenizer(sql, ",");
				while (st1.hasMoreTokens()) {
					String t1 = st1.nextToken().trim();
					if (t1.contains(" ") && !t1.toUpperCase().contains(SQL.AS.name())) {
						StringTokenizer st2 = new StringTokenizer(t1, " ");
						l.add(new FromBean(st2.nextToken(), st2.nextToken()));
					} else if (t1.toUpperCase().contains(SQL.AS.name())) {
						StringTokenizer st2 = new StringTokenizer(t1, " ");
						String col = st2.nextToken();
						st2.nextToken();
						String lbl = st2.nextToken();
						l.add(new FromBean(col, lbl));
					} else {
						l.add(new FromBean(t1, t1));
					}
				}
			}
			return l;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static String[][] selectColumnAliasMatrix(String sql) throws WDSException {
		try {
			List<String> cols = new ArrayList<String>();
			List<String> lbls = new ArrayList<String>();
			int start = 1 + SELECT.SELECT.name().length() + sql.indexOf(SELECT.SELECT.name());
			int end   = sql.indexOf(FROM.FROM.name());
			if (start > -1 && end > -1) {
				sql = sql.substring(start, end);
				StringTokenizer st1 = new StringTokenizer(sql, ",");
				while (st1.hasMoreTokens()) {
					String t1 = st1.nextToken().trim();
					if (t1.contains(" ") && !t1.toUpperCase().contains(SQL.AS.name())) {
						StringTokenizer st2 = new StringTokenizer(t1, " ");
						cols.add(st2.nextToken());
						lbls.add(st2.nextToken());
					} else if (t1.toUpperCase().contains(SQL.AS.name())) {
						StringTokenizer st2 = new StringTokenizer(t1, " ");
						String col = st2.nextToken();
						st2.nextToken();
						String lbl = st2.nextToken();
						cols.add(col);
						lbls.add(lbl);
					} else {
						cols.add(t1);
						lbls.add(t1);
					}
				}
			}
			String[][] m = new String[cols.size()][2];
			for (int i = 0 ; i < cols.size() ; i++) {
				m[i][0] = cols.get(i);
				m[i][1] = lbls.get(i);
			}
			return m;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static Map<String, String> selectColumnAliasMap(String sql) throws WDSException {
		try {
			Map<String, String> m = new HashMap<String, String>();
			int start = 1 + SELECT.SELECT.name().length() + sql.indexOf(SELECT.SELECT.name());
			int end   = sql.indexOf(FROM.FROM.name());
			if (start > -1 && end > -1) {
				sql = sql.substring(start, end);
				StringTokenizer st1 = new StringTokenizer(sql, ",");
				while (st1.hasMoreTokens()) {
					String t1 = st1.nextToken().trim();
					if (t1.contains(" ") && !t1.toUpperCase().contains(SQL.AS.name())) {
						StringTokenizer st2 = new StringTokenizer(t1, " ");
						m.put(st2.nextToken(), st2.nextToken());
					} else if (t1.toUpperCase().contains(SQL.AS.name())) {
						StringTokenizer st2 = new StringTokenizer(t1, " ");
						String col = st2.nextToken();
						st2.nextToken();
						String lbl = st2.nextToken();
						m.put(col, lbl);
					} else {
						m.put(t1, t1);
					}
				}
			}
			return m;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static List<SelectBean> parseSelects(String sql) throws WDSException {
		try {
			List<SelectBean> l = new ArrayList<SelectBean>();
			int start = 1 + SELECT.SELECT.name().length() + sql.indexOf(SELECT.SELECT.name());
			int end   = sql.indexOf(FROM.FROM.name());
			if (start > -1 && end > -1) {
				sql = sql.substring(start, end);
				StringTokenizer st1 = new StringTokenizer(sql, ",");
				while (st1.hasMoreTokens()) {
					String t1 = st1.nextToken().trim();
					if (t1.contains(" ") && !t1.toUpperCase().contains(SQL.AS.name())) {
						StringTokenizer st2 = new StringTokenizer(t1, " ");
						l.add(new SelectBean(null, st2.nextToken(), st2.nextToken()));
					} else if (t1.toUpperCase().contains(SQL.AS.name())) {
						StringTokenizer st2 = new StringTokenizer(t1, " ");
						String col = st2.nextToken();
						st2.nextToken();
						String lbl = st2.nextToken();
						l.add(new SelectBean(null, col, lbl));
					} else {
						l.add(new SelectBean(null, t1, t1));
					}
				}
			}
			return l;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static VariableBean parseVariables(String sql) throws WDSException {
		try {
			VariableBean b = new VariableBean();
			if (!sql.toUpperCase().contains(VARIABLES.VARIABLES.name()))
				return b;
			int start = 1 + VARIABLES.VARIABLES.name().length() + sql.indexOf(VARIABLES.VARIABLES.name());
			int end = sql.indexOf(GROUPBY.GROUP.name());
			if (end < 0) 
				end = sql.indexOf(ORDERBY.ORDER.name());
			if (end < 0) 
				end = sql.indexOf(SQL.LIMIT.name());
			if (start > -1 && end > -1) {
				sql = sql.substring(start, end);
			} else {
				sql = sql.substring(start);
			}
			String[] conditions = Pattern.compile(WHERE.IN.name()).split(sql);
			String column = conditions[0].trim();
			b.setColumn(column);
			String values = CSV2Bean.unwrap(conditions[1], "(", ")");
			StringTokenizer st1 = new StringTokenizer(values, ",");
			while (st1.hasMoreTokens()) {
				String variable = st1.nextToken().replaceAll("'", "").trim();
				b.addVariable(variable);
			}
			return b;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static List<WhereBean> parseWheres(String sql) throws WDSException {
		try {
			List<WhereBean> l = new ArrayList<WhereBean>();
			String datatype = SQL.TEXT.name();
			if (!sql.toUpperCase().contains(WHERE.WHERE.name()))
				return l;
			int start = 1 + WHERE.WHERE.name().length() + sql.indexOf(WHERE.WHERE.name());
			int end = sql.indexOf(VARIABLES.VARIABLES.name());
			if (end < 0) 
				end = sql.indexOf(GROUPBY.GROUP.name());
			if (end < 0) 
				end = sql.indexOf(ORDERBY.ORDER.name());
			if (end < 0) 
				end = sql.indexOf(SQL.LIMIT.name());
			if (start > -1 && end > -1) {
				sql = sql.substring(start, end);
			} else {
				sql = sql.substring(start);
			}
			String[] conditions = Pattern.compile(SQL.AND.name()).split(sql);
			for (String condition : conditions) {
				WhereBean b = new WhereBean();
				String f = function(condition);
				b.setOperator(f);
				b.setDatatype(datatype);
				String[] columnValue = Pattern.compile(f).split(condition);
				if (f.equals(WHERE.IN.name())) {
					b.setColumn(columnValue[0].trim());
					String ins = columnValue[1].trim();
					ins = ins.substring(1, ins.length() - 1);
					StringTokenizer st1 = new StringTokenizer(ins, ",");
					while (st1.hasMoreTokens())
						b.addIn(st1.nextToken().trim());
				} else {
					b.setColumn(columnValue[0].trim());
					b.setValue(columnValue[1].replaceAll("'", "").trim());
				}
				if (b.getColumn().equalsIgnoreCase("Year"))
					b.setDatatype(SQL.DATE.name()); // FIXME hard coded!!!
				l.add(b);
			}
			return l;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static String function(String sql) throws WDSException {
		String[] functions = new String[]{"<=", ">=", "=", ">", "<", "IN"};
		for (String f : functions)
			if (sql.contains(f))
				return f;
		throw new WDSException("Impossible to detect function for:\n\t" + sql);
	}
	
	public static List<GroupByBean> parseGroupBys(String sql) throws WDSException {
		try {
			List<GroupByBean> l = new ArrayList<GroupByBean>();
			if (!sql.toUpperCase().contains(GROUPBY.GROUP.name()))
				return l;
			int start = 1 + 3 + GROUPBY.GROUP.name().length() + sql.indexOf(GROUPBY.GROUP.name());
			int end =  sql.indexOf(ORDERBY.ORDER.name());
			if (end < 0) 
				end = sql.indexOf(SQL.LIMIT.name());
			if (start > -1 && end > -1) {
				sql = sql.substring(start, end);
			} else {
				sql = sql.substring(start);
			}
			StringTokenizer st1 = new StringTokenizer(sql, ",");
			while (st1.hasMoreTokens()) {
				String t1 = st1.nextToken();
				StringTokenizer st2 = new StringTokenizer(t1, " ");
				while (st2.hasMoreTokens())
					l.add(new GroupByBean(st2.nextToken())); // TODO test it!!!
			}
			return l;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static List<OrderByBean> parseOrderBys(String sql) throws WDSException {
		try {
			List<OrderByBean> l = new ArrayList<OrderByBean>();
			if (!sql.toUpperCase().contains(ORDERBY.ORDER.name()))
				return l;
			int start = 1 + 3 + ORDERBY.ORDER.name().length() + sql.indexOf(ORDERBY.ORDER.name());
			int end =  sql.indexOf(SQL.LIMIT.name());
			if (start > -1 && end > -1) {
				sql = sql.substring(start, end);
			} else {
				sql = sql.substring(start);
			}
			StringTokenizer st1 = new StringTokenizer(sql, ",");
			while (st1.hasMoreTokens()) {
				String t1 = st1.nextToken().trim();
				StringTokenizer st2 = new StringTokenizer(t1, " ");
				if (st2.countTokens() == 2) {
					String column = st2.nextToken();
					String direction = st2.nextToken();
					l.add(new OrderByBean(column, direction));
				} else {
					String column = st2.nextToken();
					l.add(new OrderByBean(column, ORDERBY.ASC.name()));
				}
			}
			return l;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
}