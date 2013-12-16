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
import org.fao.fenix.wds.core.bean.DBBean;
import org.fao.fenix.wds.core.bean.SQLBean;
import org.fao.fenix.wds.core.constant.DATASOURCE;
import org.fao.fenix.wds.core.exception.WDSException;
import org.fao.fenix.wds.core.jdbc.JDBCConnector;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WrapperTest extends TestCase {

	public void testWrapAsCSV() {
		try {
			DBBean db = new DBBean(DATASOURCE.FAOSTAT);
			SQLBean sql = new SQLBean("SELECT TOP 100 ItemCode, ElementCode, Year FROM Data");
			List<List<String>> table = JDBCConnector.query(db, sql, false);
//			StringBuilder sb = Wrapper.wrapAsCSV(table, ",", ";", true);
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
	
	public void testUnwrapFromCSV() {
		try {
			DBBean db = new DBBean(DATASOURCE.FAOSTAT);
			SQLBean sql = new SQLBean("SELECT TOP 100 ItemCode, ElementCode, Year FROM Data");
			List<List<String>> table = JDBCConnector.query(db, sql, false);
//			StringBuilder sb = Wrapper.wrapAsCSV(table, ",", ";", true);
//			List<List<String>> rebuiltTable = Wrapper.unwrapFromCSV(sb.toString(), ",", ";");
//			assertEquals(100, rebuiltTable.size());
//			assertEquals(3, rebuiltTable.get(0).size());
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
	
	public void testWrapAsXML() {
		try {
			DBBean db = new DBBean(DATASOURCE.FAOSTAT);
			SQLBean sql = new SQLBean("SELECT TOP 100 ItemCode, ElementCode, Year FROM Data");
			List<List<String>> table = JDBCConnector.query(db, sql, false);
			StringBuilder sb = Wrapper.wrapAsXML(table);
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
	
	public void testUnwrapFromXML() {
		try {
			DBBean db = new DBBean(DATASOURCE.FAOSTAT);
			SQLBean sql = new SQLBean("SELECT TOP 100 ItemCode, ElementCode, Year FROM Data");
			List<List<String>> table = JDBCConnector.query(db, sql, false);
			StringBuilder sb = Wrapper.wrapAsXML(table);
			List<List<String>> rebuiltTable = Wrapper.unwrapFromXML(sb.toString());
			assertEquals(100, rebuiltTable.size());
			assertEquals(3, rebuiltTable.get(0).size());
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
	
	public void testWrapAsXML2() {
		try {
			DBBean db = new DBBean(DATASOURCE.FAOSTAT);
			SQLBean sql = new SQLBean("SELECT AreaCode AS Code, AreaNameE AS Name FROM Area ");
			List<String> headers = new ArrayList<String>();
			headers.add("Code");
			headers.add("Label");
			List<List<String>> table = JDBCConnector.query(db, sql, true);
			table.add(0, headers);
			StringBuilder sb = Wrapper.wrapAsXML2(table);
			System.out.println(sb);
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
	
	public void testWrapAsJSON() {
		try {
			DBBean db = new DBBean(DATASOURCE.FAOSTAT);
			SQLBean sql = new SQLBean("SELECT AreaCode AS Code, AreaNameE AS Name FROM Area ");
			List<String> headers = new ArrayList<String>();
			headers.add("Code");
			headers.add("Label");
			List<List<String>> table = JDBCConnector.query(db, sql, true);
			table.add(0, headers);
			StringBuilder sb = Wrapper.wrapAsJSON(table);
			System.out.println(sb);
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
	
}
