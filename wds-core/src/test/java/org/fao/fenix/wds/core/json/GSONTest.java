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
package org.fao.fenix.wds.core.json;

import com.google.gson.Gson;
import junit.framework.TestCase;
import org.fao.fenix.wds.core.bean.*;
import org.fao.fenix.wds.core.sql.Bean2SQL;

/** 
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class GSONTest extends TestCase {
	
	public void testGSON_SQL() {
		
		SQLBean b = new SQLBean();
		b.select(new SelectBean(null, "D.Value", "Price"));
		b.select(new SelectBean(null, "I.ItemNameE", "Item"));
		b.select(new SelectBean(null, "E.ElementNameE", "Element"));
		b.select(new SelectBean(null, "A.AreaNameE", "Area"));
		b.select(new SelectBean(null, "D.Year	", "Year"));
		b.from(new FromBean("Data", "D"));
		b.from(new FromBean("Item", "I"));
		b.from(new FromBean("Element", "E"));
		b.from(new FromBean("Area", "A"));
		b.where(new WhereBean("TEXT", "D.ItemCode", "IN", null, new String[]{"15"}));
		b.where(new WhereBean("TEXT", "D.AreaCode", "IN", null, new String[]{"2"}));
		b.where(new WhereBean("DATE", "D.AreaCode", "=", "A.AreaCode", new String[]{}));
		b.where(new WhereBean("DATE", "D.ItemCode", "=", "I.ItemCode", new String[]{}));
		b.where(new WhereBean("DATE", "D.ElementCode", "=", "E.ElementCode", new String[]{}));
		b.groupBy(new GroupByBean("I.ItemNameE"));
		b.groupBy(new GroupByBean("E.ElementNameE"));
		b.groupBy(new GroupByBean("E.AreaNameE"));
		b.groupBy(new GroupByBean("D.Year"));
		b.orderBy(new OrderByBean("D.Year", "DESC"));
		
		WhereBean wb = new WhereBean();
		wb.setInnerSQL(b);
		b.where(wb);
		
		System.out.println(wb);
		String sb = Bean2JSON.convert(b);
		System.out.println(sb);
		
	}
	
	public void _testGSON_SQL_2() {
		SQLBean b = new SQLBean();
		b.select(new SelectBean(null, "D.Value", "Price"));
		b.select(new SelectBean(null, "I.ItemNameE", "Item"));
		b.select(new SelectBean(null, "E.ElementNameE", "Element"));
		b.select(new SelectBean(null, "A.AreaNameE", "Area"));
		b.select(new SelectBean(null, "D.Year	", "Year"));
		b.from(new FromBean("Data", "D"));
		b.from(new FromBean("Item", "I"));
		b.from(new FromBean("Element", "E"));
		b.from(new FromBean("Area", "A"));
		b.where(new WhereBean("TEXT", "D.ItemCode", "IN", null, new String[]{"15"}));
		b.where(new WhereBean("TEXT", "D.AreaCode", "IN", null, new String[]{"2"}));
		b.where(new WhereBean("DATE", "D.AreaCode", "=", "A.AreaCode", new String[]{}));
		b.where(new WhereBean("DATE", "D.ItemCode", "=", "I.ItemCode", new String[]{}));
		b.where(new WhereBean("DATE", "D.ElementCode", "=", "E.ElementCode", new String[]{}));
		b.groupBy(new GroupByBean("I.ItemNameE"));
		b.groupBy(new GroupByBean("E.ElementNameE"));
		b.groupBy(new GroupByBean("E.AreaNameE"));
		b.groupBy(new GroupByBean("D.Year"));
		b.orderBy(new OrderByBean("D.Year", "DESC"));
		String sb = Bean2JSON.convert(b);
		System.out.println(sb);
	}

	public void _testGSON_Select() {
		SelectBean b = new SelectBean("AVG", "D.Value", "Price");
		String sb = Bean2JSON.convert(b);
		System.out.println(sb);
	}
	
	public void _testGSON_Select_2() {
		SelectBean b = new SelectBean("AVG", "D.Value", "Price");
		Gson g = new Gson();
		String s = g.toJson(b);
		SelectBean b2 = g.fromJson(s, SelectBean.class);
		System.out.println(b2);
	}
	
	public void _testGSON_From() {
		FromBean b = new FromBean("Data", "D");
		String sb = Bean2JSON.convert(b);
		System.out.println(sb);
	}
	
	public void _testGSON_Where() {
		WhereBean b = new WhereBean("TEXT", "D.ElementCode", "IN", null, new String[]{"15", "18"});
		String sb = Bean2JSON.convert(b);
		System.out.println(sb);
	}
	
	public void _testGSON_GroupBy() {
		GroupByBean b = new GroupByBean("D.ElementCode");
		String sb = Bean2JSON.convert(b);
		System.out.println(sb);
	}
	
	public void _testGSON_OrderBy() {
		OrderByBean b = new OrderByBean("D.ItemCode", "DESC");
		String sb = Bean2JSON.convert(b);
		System.out.println(sb);
	}
	
	public void _testAJAX() {
		String s = "{'selects':[{'aggregation':'NONE','column':'D.Value','alias':'Price'},{'aggregation':'NONE','column':'I.ItemNameE','alias':'Item'},{'aggregation':'NONE','column':'E.ElementNameE','alias':'Element'},{'aggregation':'NONE','column':'A.AreaNameE','alias':'Area'},{'aggregation':'NONE','column':'D.Year','alias':'Year'}],'froms':[{'column':'Data','alias':'D'},{'column':'Item','alias':'I'},{'column':'Element','alias':'E'},{'column':'Area','alias':'A'}],'wheres':[{'datatype':'TEXT','column':'D.ItemCode','operator':'IN','value':'','ins':['3010']},{'datatype':'DATE','column':'D.AreaCode','operator':'IN','value':'A.AreaCode','ins':['2']},{'datatype':'DATE','column':'D.ElementCode','operator':'IN','value':'E.ElementCode','ins':['571']}],'groupBys':[{'column':'I.ItemNameE'},{'column':'E.ElementNameE'},{'column':'A.AreaNameE'},{'column':'D.Year'},{'column':'D.Value'}],'orderBys':[{'column':'D.Year','direction':'DESC'}],'limit':'NONE','query':'NONE','frequency':'NONE'}";
		Gson g = new Gson();
		SQLBean sql = g.fromJson(s, SQLBean.class);
		System.out.println(sql);
		System.out.println(Bean2SQL.convert(sql));
	}
	
	public void _testAJAX2() {
		String s = "{'selects':[{'aggregation':'NONE','column':'D.year','alias':'Year'},{'aggregation':'NONE','column':'D.value','alias':'Production'}],'froms':[{'column':'Data','alias':'D'}],'wheres':[{'datatype':'TEXT','column':'D.DomainCode','operator':'IN','value':'','ins':['\"QC\"']},{'datatype':'TEXT','column':'D.AreaCode','operator':'IN','value':'','ins':['2']},{'datatype':'TEXT','column':'D.ElementCode','operator':'IN','value':'','ins':['5510']},{'datatype':'TEXT','column':'D.ItemCode','operator':'IN','value':'','ins':['800']},{'datatype':'DATE','column':'D.Year','operator':'IN','value':'','ins':[2000,2001,2002,2003,2004,2005,2006,2007,2008,2009,2010]}],'orderBys':[{'column':'D.Year','direction':'ASC'}],'limit':'NONE','query':'NONE','frequency':'NONE'}";
		Gson g = new Gson();
		SQLBean sql = g.fromJson(s, SQLBean.class);
		System.out.println(sql);
		System.out.println(Bean2SQL.convert(sql));
	}
	
}