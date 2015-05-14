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

import org.fao.fenix.wds.core.bean.*;
import org.fao.fenix.wds.core.constant.WHERE;
import org.fao.fenix.wds.core.constant.SQL;
import org.fao.fenix.wds.core.exception.WDSException;

import java.util.List;

/** 
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class Bean2SQL {

	public static StringBuilder convert(SQLBean b) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			if (b.getQuery() == null || b.getQuery().isEmpty()) {
				sb.append(convertSelects(b.getSelects()));
				sb.append(convertFroms(b.getFroms()));
				if (!b.getWheres().isEmpty() || !b.getNestedWheres().isEmpty())
					sb.append(convertWheres(b.getWheres(), b.getNestedWheres()));
				if (!b.getGroupBys().isEmpty())
					sb.append(convertGroupBys(b.getGroupBys()));
				if (!b.getOrderBys().isEmpty())
					sb.append(convertOrderBys(b.getOrderBys()));
				if (b.getLimit() != null && !b.getLimit().equals(SQL.NONE.name()))
					sb.append("LIMIT ").append(b.getLimit());
			} else {
				sb = new StringBuilder(b.getQuery());
			}
			return sb;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WDSException(e.getMessage());
		}
	}
	
	private static StringBuilder convertOrderBys(List<OrderByBean> beans) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("ORDER BY ");
			for (int i = 0 ; i < beans.size() ; i++) {
				if (!beans.get(i).getColumn().equals(SQL.NONE.name())) {
					sb.append(beans.get(i).getColumn()).append(" ").append(beans.get(i).getDirection());
					if (i < beans.size() - 1)
						sb.append(", ");
				} else {
					return new StringBuilder();
				}
			}
			sb.append(" ");
			return sb;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WDSException(e.getMessage());
		}
	}
	
	private static StringBuilder convertGroupBys(List<GroupByBean> beans) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("GROUP BY ");
			for (int i = 0 ; i < beans.size() ; i++) {
				if (!beans.get(i).getColumn().equals(SQL.NONE.name())) {
					sb.append(beans.get(i).getColumn());
					if (i < beans.size() - 1)
						sb.append(", ");
				} else {
					return new StringBuilder();
				}
			}
			sb.append(" ");
			return sb;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WDSException(e.getMessage());
		}
	}
	
	private static StringBuilder convertWheres(List<WhereBean> beans) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("WHERE ");
			for (int i = 0 ; i < beans.size() ; i++) {
				sb.append(convertWhere(beans.get(i)));
				if (i < beans.size() - 1)
					sb.append(" AND ");
			}
			sb.append(" ");
			return sb;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WDSException(e.getMessage());
		}
	}
	
	private static StringBuilder convertWheres(List<WhereBean> beans, List<NestedWhereBean> nestedBeans) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("WHERE ");
			if (!beans.isEmpty()) {
				for (int i = 0 ; i < beans.size() ; i++) {
					sb.append(convertWhere(beans.get(i)));
					if (i < beans.size() - 1)
						sb.append(" AND ");
				}
				sb.append(" ");
			}
			if (!nestedBeans.isEmpty()) {
				if (!beans.isEmpty())
					sb.append(" AND ");
				for (int i = 0 ; i < nestedBeans.size() ; i++) {
					sb.append(convertNestedWhere(nestedBeans.get(i)));
					if (i < nestedBeans.size() - 1)
						sb.append(" AND ");
				}
				sb.append(" ");
			}
//			System.out.println(sb);
			return sb;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	private static StringBuilder convertWhere(WhereBean b) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(b.getColumn()).append(" ");
			if (b.getOperator().equalsIgnoreCase(WHERE.NOTIN.name())) {
				sb.append("NOT IN ");
			} else {
				sb.append(b.getOperator()).append(" ");
			}
			if (b.getOperator().equals(WHERE.IN.name()) || b.getOperator().equals(WHERE.NOTIN.name())) {
				sb.append("(");
				for (int i = 0 ; i < b.getIns().size() ; i++) {
					sb.append(b.getIns().get(i));
					if (i < b.getIns().size() - 1)
						sb.append(", ");
				}
				sb.append(") ");
			} else {
				if (b.getDatatype().equals(SQL.TEXT.name())) {
					sb.append("'").append(b.getValue()).append("'");
				} else {
					sb.append(b.getValue());
				}
			}
			return sb;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	private static StringBuilder convertNestedWhere(NestedWhereBean b) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(b.getColumn()).append(" IN (");
			sb.append(Bean2SQL.convert(b.getNestedCondition()));
			sb.append(")");
//			System.out.println();
//			System.out.println(sb);
//			System.out.println();
			return sb;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WDSException(e.getMessage());
		}
	}
	
	private static StringBuilder convertFroms(List<FromBean> beans) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("FROM ");
			for (int i = 0 ; i < beans.size() ; i++) {
				sb.append(convertFrom(beans.get(i)));
				if (i < beans.size() - 1)
					sb.append(", ");
			}
			sb.append(" ");
			return sb;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WDSException(e.getMessage());
		}
	}
	
	private static StringBuilder convertFrom(FromBean b) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(b.getColumn());
			if (b.getAlias() != null && !b.getAlias().equals(SQL.NONE.name()))
				sb.append(" AS ").append(b.getAlias()).append(" ");
			return sb;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WDSException(e.getMessage());
		}
	}
	
	private static StringBuilder convertSelects(List<SelectBean> beans) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT ");
			for (int i = 0 ; i < beans.size() ; i++) {
				sb.append(convertSelect(beans.get(i)));
				if (i < beans.size() - 1)
					sb.append(", ");
			}
			sb.append(" ");
			return sb;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WDSException(e.getMessage());
		}
	}
	
	private static StringBuilder convertSelect(SelectBean b) throws WDSException {
		try {
			StringBuilder sb = new StringBuilder();
			if (b.getAggregation() != null && !b.getAggregation().equals(SQL.NONE.name()))
				sb.append(b.getAggregation()).append("(");
			sb.append(b.getColumn());
			if (b.getAggregation() != null && !b.getAggregation().equals(SQL.NONE.name()))
				sb.append(")");
			if (b.getAlias() != null && !b.getAlias().equals(SQL.NONE.name()))
				sb.append(" AS ").append(b.getAlias()).append(" ");
			return sb;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WDSException(e.getMessage());
		}
	}
	
}