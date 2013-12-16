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

import junit.framework.TestCase;
import org.fao.fenix.wds.core.bean.*;
import org.fao.fenix.wds.core.constant.SQL;

import java.util.List;

public class SQLSplitterTest extends TestCase {
	
	private final static String sql = "SELECT Year, ItemCode as Commodity, AreaCode Country " +
									  "FROM Data, Metadata as Meta, Extra X " +
									  "WHERE ItemCode = '15' " +
									  "AND AreaCode = '2' " +
									  "AND Year IN (2000, 2001, 2002, 2003, 2004) " +
									  "AND Value <= 275000 " +
									  "VARIABLES ElementCode IN ('5510', '5419', '5312') " +
									  "GROUP BY AreaCode " +
									  "ORDER BY Year DESC, ItemCode " +
									  "LIMIT 10 ";

	public void testSplitBean() {
		SQLBean b = new SQLBean();
		b.select(null, "Year", null);
		b.select(null, "AreaCode", null);
		b.select(null, "Value", null);
		b.from("Data", null);
		b.where(SQL.TEXT.name(), "ItemCode", "=", "15", null);
		b.where(SQL.TEXT.name(), "AreaCode", "=", "2", null);
		List<SQLBean> l = SQLSplitter.split(b);
		assertEquals(l.size(), 3);
		for (SQLBean sql : l) {
			assertEquals(sql.getSelects().size(), 1);
			assertEquals(sql.getFroms().size(), 1);
			assertEquals(sql.getWheres().size(), 2);
		}
	}
	
	public void testSplitSQL() {
		SQLBean b = new SQLBean(sql);
		List<SQLBean> l = SQLSplitter.split(b);
		assertEquals(3, l.size());
		for (SQLBean sql : l) {
			assertEquals(1, sql.getSelects().size());
			assertEquals(3, sql.getFroms().size());
			assertEquals(5, sql.getWheres().size());
			assertEquals(1, sql.getGroupBys().size());
			assertEquals(2, sql.getOrderBys().size());
		}
	}
	
	public void testParseLimit() {
		String limit = SQLSplitter.parseLimit(sql);
		assertEquals("10", limit);
	}
	
	public void testParseOrderBys() {
		List<OrderByBean> l = SQLSplitter.parseOrderBys(sql);
		assertEquals(l.size(), 2);
		assertEquals(l.get(0).getColumn(), "Year");
		assertEquals(l.get(0).getDirection(), "DESC");
		assertEquals(l.get(1).getColumn(), "ItemCode");
		assertEquals(l.get(1).getDirection(), "ASC");
	}
	
	public void testParseGroupBys() {
		List<GroupByBean> l = SQLSplitter.parseGroupBys(sql);
		assertEquals(l.size(), 1);
		assertEquals(l.get(0).getColumn(), "AreaCode");
	}
	
	public void testParseWheres() {
		List<WhereBean> l = SQLSplitter.parseWheres(sql);
		assertEquals(l.size(), 4);
		assertEquals(l.get(0).getColumn(), "ItemCode");
		assertEquals(l.get(0).getOperator(), "=");
		assertEquals(l.get(0).getValue(), "15");
		assertEquals(l.get(1).getColumn(), "AreaCode");
		assertEquals(l.get(1).getOperator(), "=");
		assertEquals(l.get(1).getValue(), "2");
		assertEquals(l.get(2).getColumn(), "Year");
		assertEquals(l.get(2).getOperator(), "IN");
		assertEquals(l.get(2).getIns().size(), 5);
		assertEquals(l.get(3).getColumn(), "Value");
		assertEquals(l.get(3).getOperator(), "<=");
		assertEquals(l.get(3).getValue(), "275000");
	}
	
	public void testParseSelects() {
		List<SelectBean> l = SQLSplitter.parseSelects(sql);
		assertEquals(l.size(), 3);
		assertEquals(l.get(0).getColumn(), "Year");
		assertEquals(l.get(0).getAlias(), "Year");
		assertEquals(l.get(1).getColumn(), "ItemCode");
		assertEquals(l.get(1).getAlias(), "Commodity");
		assertEquals(l.get(2).getColumn(), "AreaCode");
		assertEquals(l.get(2).getAlias(), "Country");
	}
	
	public void testParseFroms() {
		List<FromBean> l = SQLSplitter.parseFroms(sql);
		assertEquals(l.size(), 3);
		assertEquals(l.get(0).getColumn(), "Data");
		assertEquals(l.get(0).getAlias(), "Data");
		assertEquals(l.get(1).getColumn(), "Metadata");
		assertEquals(l.get(1).getAlias(), "Meta");
		assertEquals(l.get(2).getColumn(), "Extra");
		assertEquals(l.get(2).getAlias(), "X");
	}
	
	public void testParseVariables() {
		VariableBean b = SQLSplitter.parseVariables(sql);
		assertEquals("ElementCode", b.getColumn());
		assertEquals(3, b.getVariables().size());
		assertEquals("5510", b.getVariables().get(0));
	}
	
}