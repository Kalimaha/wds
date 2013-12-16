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

import junit.framework.TestCase;
import org.fao.fenix.wds.core.bean.*;

import java.util.List;

public class CSV2BeanTest extends TestCase {

	public void testConvertSelect() {
		List<SelectBean> l = CSV2Bean.convertSelect("Year,ItemCode,Value");
		assertEquals(3, l.size());
		assertEquals("Year", l.get(0).getColumn());
		assertEquals("ItemCode", l.get(1).getColumn());
		assertEquals("Value", l.get(2).getColumn());
	}
	
	public void testConvertFrom() {
		List<FromBean> l = CSV2Bean.convertFrom("Data");
		assertEquals(1, l.size());
		assertEquals("Data", l.get(0).getColumn());
	}
	
	public void testConvertWhere() {
		List<WhereBean> l = CSV2Bean.convertWhere("ItemCode(1:23:47),Year[DATE](2001:2002:2003)");
		assertEquals(2, l.size());
		assertEquals("ItemCode", l.get(0).getColumn());
		assertEquals("Year", l.get(1).getColumn());
		assertEquals(3, l.get(0).getIns().size());
		assertEquals(3, l.get(1).getIns().size());
		assertEquals("TEXT", l.get(0).getDatatype());
		assertEquals("DATE", l.get(1).getDatatype());
	}
	
	public void testConvertGroupBy() {
		List<GroupByBean> l = CSV2Bean.convertGroupBy("ItemCode");
		assertEquals(1, l.size());
		assertEquals("ItemCode", l.get(0).getColumn());
	}
	
	public void testConvertOrderBy() {
		List<OrderByBean> l = CSV2Bean.convertOrderBy("ItemCode,Year[DESC]");
		assertEquals(2, l.size());
		assertEquals("ItemCode", l.get(0).getColumn());
		assertEquals("ASC", l.get(0).getDirection());
		assertEquals("Year", l.get(1).getColumn());
		assertEquals("DESC", l.get(1).getDirection());
	}
	
}