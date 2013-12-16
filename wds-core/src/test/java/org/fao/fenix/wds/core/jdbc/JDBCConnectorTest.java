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
package org.fao.fenix.wds.core.jdbc;

import com.google.gson.Gson;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import junit.framework.TestCase;
import org.fao.fenix.wds.core.bean.DBBean;
import org.fao.fenix.wds.core.bean.NestedWhereBean;
import org.fao.fenix.wds.core.bean.SQLBean;
import org.fao.fenix.wds.core.constant.DATASOURCE;
import org.fao.fenix.wds.core.constant.SQL;
import org.fao.fenix.wds.core.sql.Bean2SQL;
import org.fao.fenix.wds.core.sql.faostat.SQLBeansRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCConnectorTest extends TestCase {
	
	public void _testQI() {
		try {
			DBBean db = new DBBean(DATASOURCE.FAOSTAT);
			String ss = "SELECT I.ItemCode AS Code , I.ItemNameE AS Label  " +
					    "FROM item AS I , domainitem AS DI  " +
					    "WHERE DI.ItemCode = I.ItemCode AND DI.DomainCode = 'QI' " +
					    "ORDER BY DI.Ord ASC ";
			SQLBean sql = SQLBeansRepository.getItemCodes("QI", "E");
			List<List<String>> table = JDBCConnector.query(db, sql, false);
			System.out.println(Bean2SQL.convert(sql));
			System.out.println(table.size());
			for (List<String> l : table) {
				for (String s : l)
					System.out.print(s + " | ");
				System.out.println();
			}
			
			SQLBean sql2 = new SQLBean(ss);
			System.out.println(ss);
			List<List<String>> table2 = JDBCConnector.query(db, sql2, false);
			System.out.println(table2.size());
			for (List<String> l : table2) {
				for (String s : l)
					System.out.print(s + " | ");
				System.out.println();
			}
			
			String a = "SELECT I.ItemCode AS Code , I.ItemNameE AS Label  FROM item AS I , domainitem AS DI  WHERE DI.ItemCode = I.ItemCode AND DI.DomainCode = 'QI' ORDER BY DI.Ord ASC ";
			String b = "SELECT I.ItemCode AS Code , I.ItemNameE AS Label  FROM item AS I , domainitem AS DI  WHERE DI.ItemCode = I.ItemCode AND DI.DomainCode = 'QI' ORDER BY DI.Ord ASC ";
			System.out.println(a.equals(b));
			
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
	
	public void _testPreparedStatement() {
		try {
			DBBean db = new DBBean(DATASOURCE.FAOSYB);
			SQLBean sql = new SQLBean("EXECUTE example2(5383650)");
			List<List<String>> table = JDBCConnector.query(db, sql, false);
			for (List<String> l : table) {
				for (String s : l)
					System.out.print(s + " | ");
				System.out.println();
			}
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
	
	public void _testQueryFENIX() {
		try {
			DBBean db = new DBBean(DATASOURCE.FAOSYB);
			SQLBean sql = new SQLBean("SELECT COUNT(*) FROM quantitativecorecontent ");
			List<List<String>> table = JDBCConnector.query(db, sql, false);
			assertEquals(1, table.size());
			assertEquals(1, table.get(0).size());
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

	public void _testQuery() {
		try {
			DBBean db = new DBBean(DATASOURCE.FAOSTAT);
			SQLBean sql = new SQLBean("SELECT TOP 100 ItemCode, ElementCode, Year FROM Data");
			List<List<String>> table = JDBCConnector.query(db, sql, false);
			assertEquals(100, table.size());
			assertEquals(3, table.get(0).size());
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
	
	public void _testValidation() {
		DBBean db = new DBBean(DATASOURCE.FAOSTAT);
		SQLBean sql = new SQLBean("INSERT INTO Data(1, 2, 3)");
		try {
			JDBCConnector.query(db, sql, false);
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
	
	public void _testGetColumnNames() {
		try {
			SQLServerDriver.class.newInstance();
			DBBean db = new DBBean(DATASOURCE.FAOSTAT);
			List<String> row = new ArrayList<String>();
			Connection c = DriverManager.getConnection(db.getUrl(), db.getUsername(), db.getPassword());
			Statement stmt = c.createStatement();
			stmt.executeQuery("SELECT COLUMN_NAME " +
							  "FROM INFORMATION_SCHEMA.COLUMNS " +
							  "WHERE TABLE_NAME IN ('Metadata_Unit') " +
							  "ORDER BY ORDINAL_POSITION ");
			ResultSet rs = stmt.getResultSet();
			while (rs.next()) {
				for (int i = 1 ; i < Integer.MAX_VALUE ; i++) {
					try {
						row.add(rs.getString(i).trim());
					} catch (SQLException e) {
						break;
					}
				}
			}
			
			System.out.println("Results: " + row.size());
			for (String name : row)
				System.out.print(name + ", ");
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void _testQuery2() {
		try {
			SQLServerDriver.class.newInstance();
			DBBean db = new DBBean(DATASOURCE.FAOSTAT);
			List<String> row = new ArrayList<String>();
			Connection c = DriverManager.getConnection(db.getUrl(), db.getUsername(), db.getPassword());
			Statement stmt = c.createStatement();
//			stmt.executeQuery("SELECT * FROM Metadata_Methodology AS M WHERE M.MethodologyTitleE = 'Emissions - Agriculture: Enteric Fermentation' ");
			stmt.executeQuery("SELECT E.UnitAbbreviationE, E.UnitTitleE FROM Metadata_Unit AS E ORDER BY E.UnitAbbreviationE ASC ");
			
			System.out.println();
			System.out.println();
			System.out.println("TEST QUERY");
			
			ResultSet rs = stmt.getResultSet();
			while (rs.next()) {
				for (int i = 1 ; i < Integer.MAX_VALUE ; i++) {
					try {
						row.add(rs.getString(i).trim());
					} catch (SQLException e) {
						row.add("n.a.");
						break;
					} catch (NullPointerException e) {
						row.add("n.a.");
						break;
					}
				}
			}
			
			System.out.println("Results: " + row.size());
			for (String name : row)
				System.out.print(name + ", ");
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void _testConnector() {
		
		try {
			
			SQLBean b1 = new SQLBean();
			b1.select("NONE", "D.Year", "Year");
			b1.select("NONE", "Value", "Export");
			b1.from("Data", "D");
			
			SQLBean b2 = new SQLBean();
			b2.select("NONE", "A.AreaCode", "Code");
			b2.from("Area", "A");
			b2.where(SQL.TEXT.name(), "A.AreaNameE", "=", "Bangladesh", null);
			
			SQLBean b3 = new SQLBean();
			b3.select("NONE", "D.EndYear", "EndYear");
			b3.from("Domain", "D");
			b3.where(SQL.TEXT.name(), "D.DomainCode", "=", "QC", null);
			
			NestedWhereBean n1 = new NestedWhereBean("D.AreaCode", b2);
			b1.nestedWhere(n1);
			
			NestedWhereBean n2 = new NestedWhereBean("D.Year", b3);
			b1.nestedWhere(n2);
			
			String sql = Bean2SQL.convert(b1).toString();
			System.out.println(sql);
			
			DBBean db = new DBBean(DATASOURCE.FAOSTAT);
			List<List<String>> table = JDBCConnector.query(db, b1, false);
			System.out.println(table.size());
			
			Gson g = new Gson();
			System.out.println(g.toJson(b1));
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public void testNotIn() {
		
		try {
			
			SQLBean b1 = new SQLBean();
			b1.select("NONE", "A.AreaCode", "Code");
			b1.select("NONE", "A.AreaNameE", "Label");
			b1.from("Area", "A");
			b1.where(SQL.TEXT.name(), "A.AreaCode", "NOTIN", null, new String[]{"2"});
			b1.setFrequency(null);
			b1.setLimit(null);
			b1.setQuery(null);
			
			String sql = Bean2SQL.convert(b1).toString();
			System.out.println(sql);
			
			DBBean db = new DBBean(DATASOURCE.FAOSTAT);
			List<List<String>> table = JDBCConnector.query(db, b1, false);
			System.out.println(table.size());
			
			Gson g = new Gson();
			System.out.println(g.toJson(b1));
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
}