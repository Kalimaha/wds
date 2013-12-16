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

import com.google.gson.Gson;
import junit.framework.TestCase;
import org.fao.fenix.wds.core.bean.DBBean;
import org.fao.fenix.wds.core.bean.SQLBean;
import org.fao.fenix.wds.core.bean.olap.*;
import org.fao.fenix.wds.core.constant.DATASOURCE;
import org.fao.fenix.wds.core.exception.WDSException;
import org.fao.fenix.wds.core.jdbc.JDBCConnector;
import org.fao.fenix.wds.core.utils.olap.OLAPWrapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OLAPWrapperTest extends TestCase {

	public void _testWrap1() {
		try {
			long t0 = System.currentTimeMillis();
			DBBean db = new DBBean(DATASOURCE.FAOSTATPRODDISS);
			SQLBean sql = new SQLBean("SELECT D.AreaCode, D.ItemCode, D.ElementCode, E.UnitNameE, SUM(D.Value) " +
									  "FROM Data D, Element E " +
									  "WHERE D.AreaCode IN ('4', '68') AND D.ElementCode IN ('55', '56', '57', '58', '152', '154', '432', '434', '436', '438', '5312', '5419', '5510', '5525', '5530', '5531', '5532', '5610', '5622', '5910', '5922') AND D.ItemCode IN ('15', '31') AND E.ElementCode = D.ElementCode " +
									  "GROUP BY D.AreaCode, D.ItemCode, D.ElementCode, E.UnitNameE ");
			List<List<String>> table = JDBCConnector.query(db, sql, false);
			long t1 = System.currentTimeMillis();
			List<String> headers = new ArrayList<String>();
			headers.add("Area");
			headers.add("Item");
			headers.add("Element");
			headers.add("Unit");
			headers.add("Value");
			table.add(0, headers);
			StringBuilder sb = OLAPWrapper.wrapAsOLAPJSON(table, false, false);
			long t2 = System.currentTimeMillis();
			System.out.println("TEST 1\n" + sb + "\nQuery in " + (t1 - t0) + ", JSON in " + (t2 - t1));
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
	
	public void _testWrap2() {
		try {
			long t0 = System.currentTimeMillis();
			DBBean db = new DBBean(DATASOURCE.FAOSTATPRODDISS);
			SQLBean sql = new SQLBean("SELECT D.AreaCode, D.ItemCode, D.ElementCode, D.Year, E.UnitNameE, SUM(D.Value) " +
									  "FROM Data D, Element E " +
									  "WHERE D.AreaCode IN ('4', '68') AND D.Year IN (2000, 2001, 2002) AND D.ElementCode IN ('55', '56', '57', '58', '152', '154', '432', '434', '436', '438', '5312', '5419', '5510', '5525', '5530', '5531', '5532', '5610', '5622', '5910', '5922') AND D.ItemCode IN ('15', '31') AND E.ElementCode = D.ElementCode " +
									  "GROUP BY D.AreaCode, D.ItemCode, D.ElementCode, E.UnitNameE, D.Year ");
			List<List<String>> table = JDBCConnector.query(db, sql, false);
			long t1 = System.currentTimeMillis();
			List<String> headers = new ArrayList<String>();
			headers.add("Area");
			headers.add("Item");
			headers.add("Element");
			headers.add("Year");
			headers.add("Unit");
			headers.add("Value");
			table.add(0, headers);
			StringBuilder sb = OLAPWrapper.wrapAsOLAPJSON(table, true, true);
			long t2 = System.currentTimeMillis();
			System.out.println("TEST 2\n" + sb + "\nQuery in " + (t1 - t0) + ", JSON in " + (t2 - t1));
			System.out.println();
			System.out.println();
			System.out.println();
			for (int i = 0 ; i < table.size() ; i++) {
//				System.out.println("SIZE " + i + ": " + table.get(i).size());
				for (int j = 0 ; j < table.get(i).size() ; j++) {
					System.out.print(table.get(i).get(j) + " | ");
//					if (j < table.get(i).size() - 1)
//						System.out.println();
				}
				if (i < table.size() - 1)
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
	
	public void _testClean1() {
		StringBuilder sb = new StringBuilder("{\"header\":[\"ElementCode\",\"AreaCode\",\"ItemCode\",\"Year\",\"E.UnitNameE\",\"D.Value\"],\"d\":\"k\",\"v\":[{\"d\":\"55\",\"v\":[{\"d\":\"29\",\"v\":[{\"d\":\"15\",\"v\":[{\"d\":\"1970\",\"v\":1213.19,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":1358.77,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":1941.1,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":1941.1,\"u\":\"SLC\"}]},{\"d\":\"27\",\"v\":[{\"d\":\"1970\",\"v\":3609.78,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":7152.33,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":12874.2,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":14304.67,\"u\":\"SLC\"}]},{\"d\":\"56\",\"v\":[{\"d\":\"1970\",\"v\":39039.0,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":42042.0,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":41741.7,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":47147.1,\"u\":\"SLC\"}]},{\"d\":\"79\",\"v\":[{\"d\":\"1970\",\"v\":1450.34,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":1483.3,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":1648.11,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":1977.73,\"u\":\"SLC\"}]},{\"d\":\"83\",\"v\":[{\"d\":\"1970\",\"v\":7909.12,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":20522.67,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":19338.67,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":23285.33,\"u\":\"SLC\"}]},{\"d\":\"44\"},{\"d\":\"75\"},{\"d\":\"108\"}]},{\"d\":\"114\",\"v\":[{\"d\":\"15\",\"v\":[{\"d\":\"1970\",\"v\":4420.42,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":4304.42,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":2885.73,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":5004.17,\"u\":\"SLC\"}]},{\"d\":\"27\",\"v\":[{\"d\":\"1970\",\"v\":803.3,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":1082.77,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":1422.32,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":1091.05,\"u\":\"SLC\"}]},{\"d\":\"56\",\"v\":[{\"d\":\"1970\",\"v\":22507.17,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":24803.82,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":21772.24,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":37205.73,\"u\":\"SLC\"}]},{\"d\":\"79\",\"v\":[{\"d\":\"1970\",\"v\":4488.12,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":3107.16,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":345.24,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":2088.01,\"u\":\"SLC\"}]},{\"d\":\"83\",\"v\":[{\"d\":\"1970\",\"v\":5084.93,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":4622.67,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":2253.55,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":2773.6,\"u\":\"SLC\"}]},{\"d\":\"44\",\"v\":[{\"d\":\"1970\",\"v\":293.75,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":1082.62,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":216.52,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":216.52,\"u\":\"SLC\"}]},{\"d\":\"75\",\"v\":[{\"d\":\"1970\",\"v\":30.26,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":57.25,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":24.54,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":30.26,\"u\":\"SLC\"}]},{\"d\":\"108\"}]},{\"d\":\"129\",\"v\":[{\"d\":\"15\",\"v\":[{\"d\":\"1980\",\"v\":82.49,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":76.62,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":76.62,\"u\":\"SLC\"}]},{\"d\":\"27\",\"v\":[{\"d\":\"1970\",\"v\":774689.84,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":839586.39,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":848420.53,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":866964.68,\"u\":\"SLC\"}]},{\"d\":\"56\",\"v\":[{\"d\":\"1970\",\"v\":32244.49,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":37798.58,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":41777.84,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":41540.8,\"u\":\"SLC\"}]},{\"d\":\"79\"},{\"d\":\"83\",\"v\":[{\"d\":\"1970\",\"v\":162.94,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":84.86,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":127.3,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":117.11,\"u\":\"SLC\"}]},{\"d\":\"44\"},{\"d\":\"75\"},{\"d\":\"108\"}]},{\"d\":\"130\",\"v\":[{\"d\":\"15\",\"v\":[{\"d\":\"1970\",\"v\":74.19,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":20.45,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":67.24,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":32.19,\"u\":\"SLC\"}]},{\"d\":\"27\",\"v\":[{\"d\":\"1970\",\"v\":1822.91,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":3181.63,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":2920.91,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":2868.91,\"u\":\"SLC\"}]},{\"d\":\"56\",\"v\":[{\"d\":\"1970\",\"v\":19614.0,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":25846.89,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":30465.99,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":29534.33,\"u\":\"SLC\"}]},{\"d\":\"79\",\"v\":[{\"d\":\"1980\",\"v\":552.27,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":656.1,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":834.72,\"u\":\"SLC\"}]},{\"d\":\"83\",\"v\":[{\"d\":\"1970\",\"v\":4516.62,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":1259.87,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":898.98,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":1388.44,\"u\":\"SLC\"}]},{\"d\":\"44\"},{\"d\":\"75\"},{\"d\":\"108\"}]},{\"d\":\"137\",\"v\":[          ,{\"d\":\"27\"},{\"d\":\"56\",\"v\":[{\"d\":\"1970\",\"v\":2.86,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":3.96,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":17.66,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":26.44,\"u\":\"SLC\"}]},{\"d\":\"79\"},{\"d\":\"83\"},{\"d\":\"44\"},{\"d\":\"75\"},{\"d\":\"108\"}]},{\"d\":\"144\",\"v\":[{\"d\":\"15\",\"v\":[{\"d\":\"1970\",\"v\":63.31,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":20.64,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":41.28,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":34.4,\"u\":\"SLC\"}]},{\"d\":\"27\",\"v\":[{\"d\":\"1970\",\"v\":360.46,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":273.75,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":306.6,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":313.9,\"u\":\"SLC\"}]},{\"d\":\"56\",\"v\":[{\"d\":\"1970\",\"v\":1219.24,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":1241.33,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":1143.33,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":1306.67,\"u\":\"SLC\"}]},{\"d\":\"79\",\"v\":[{\"d\":\"1970\",\"v\":33.34,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":16.67,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":16.67,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":16.67,\"u\":\"SLC\"}]},{\"d\":\"83\",\"v\":[{\"d\":\"1970\",\"v\":661.91,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":680.0,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":748.0,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":850.0,\"u\":\"SLC\"}]},{\"d\":\"44\"},{\"d\":\"75\"},{\"d\":\"108\"}]},{\"d\":\"181\",\"v\":[{\"d\":\"15\",\"v\":[{\"d\":\"1970\",\"v\":4087851.43,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":1.4442629E7,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":7476809.93,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":1.564843453E7,\"u\":\"SLC\"}]},{\"d\":\"27\",\"v\":[{\"d\":\"1970\",\"v\":399470.23,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":29584.13,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":60774.25,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":12087.23,\"u\":\"SLC\"}]},{\"d\":\"56\",\"v\":[{\"d\":\"1970\",\"v\":1.24082248E7,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":1.727179893E7,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":1.295093386E7,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":3.233160396E7,\"u\":\"SLC\"}]},{\"d\":\"79\",\"v\":[{\"d\":\"1970\",\"v\":2515333.33,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":2058000.0,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":1372000.0,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":2561066.67,\"u\":\"SLC\"}]},{\"d\":\"83\",\"v\":[{\"d\":\"1970\",\"v\":825646.73,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":940951.9,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":634675.77,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":1520633.33,\"u\":\"SLC\"}]},{\"d\":\"44\",\"v\":[{\"d\":\"1970\",\"v\":187298.99,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":1773496.71,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":673901.78,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":1887973.86,\"u\":\"SLC\"}]},{\"d\":\"75\",\"v\":[{\"d\":\"1980\",\"v\":7268.73,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":2869.24,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":4782.06,\"u\":\"SLC\"}]},{\"d\":\"108\",\"v\":[{\"d\":\"1980\",\"v\":121931.47,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":121931.47,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":121931.47,\"u\":\"SLC\"}]}]},{\"d\":\"184\",\"v\":[{\"d\":\"15\",\"v\":[{\"d\":\"1970\",\"v\":181.66,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":315.84,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":445.8,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":726.06,\"u\":\"SLC\"}]},{\"d\":\"27\",\"v\":[{\"d\":\"1970\",\"v\":431.31,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":1404.92,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":2511.41,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":1268.55,\"u\":\"SLC\"}]},{\"d\":\"56\",\"v\":[{\"d\":\"1970\",\"v\":5779.79,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":7674.18,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":9193.61,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":8972.74,\"u\":\"SLC\"}]},{\"d\":\"79\",\"v\":[{\"d\":\"1970\",\"v\":171.38,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":163.09,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":80.16,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":48.78,\"u\":\"SLC\"}]},{\"d\":\"83\",\"v\":[{\"d\":\"1970\",\"v\":16219.41,\"u\":\"SLC\"},{\"d\":\"1980\",\"v\":18597.96,\"u\":\"SLC\"},{\"d\":\"1984\",\"v\":18982.6,\"u\":\"SLC\"},{\"d\":\"1985\",\"v\":23655.37,\"u\":\"SLC\"}]},{\"d\":\"44\"},{\"d\":\"75\"},{\"d\":\"108\"}]}]}]}");
		System.out.println(sb);
		StringBuilder sb2 = OLAPWrapper.cleanJSON(sb);
		System.out.println(sb2);
	}
	
	public void _testClean2() {
		StringBuilder sb = new StringBuilder("{\"header\":[\"ElementCode\",\"AreaCode\",\"ItemCode\",\"Year\",\"E.UnitNameE\",\"D.Value\"],\"d\":\"k\",\"v\":[{\"d\":\"55\",\"v\":[{\"d\":\"133\",\"v\":[{\"d\":\"116\",\"v\":[{\"d\":\"1990\",\"v\":9684.0,\"u\":\"SLC\"},{}]},{\"d\":\"122\",\"v\":[{\"d\":\"1990\",\"v\":996.52,\"u\":\"SLC\"},{\"d\":\"1989\",\"v\":978.62,\"u\":\"SLC\"}]}]}]}]}");
		System.out.println(sb);
		StringBuilder sb2 = OLAPWrapper.cleanJSON(sb);
		System.out.println(sb2);
	}
	
	public void testGSON() {
		
		List<List<String>> t = new ArrayList<List<String>>();
		
		List<String> h = new ArrayList<String>();
		h.add("element");
		h.add("area");
		h.add("item");
		h.add("year");
		h.add("flag");
		h.add("unit");
		h.add("value");
		t.add(h);  
		
		List<String> r1 = new ArrayList<String>();
		r1.add("7224");
		r1.add("3");
		r1.add("946");
		r1.add("2005");
		r1.add("");
		r1.add("kg");
		r1.add("120");
		t.add(r1); 
		
		List<String> r2 = new ArrayList<String>();
		r2.add("7224");
		r2.add("3");
		r2.add("946");
		r2.add("2006");
		r2.add("");
		r2.add("kg");
		r2.add("118");
		t.add(r2);
		
	}
	
	public void testGsonNode() {
		
		Gson g = new Gson();
		
		OLAPGsonE e1 = new OLAPGsonE();
		e1.setD("2005");
		e1.setF("");
		e1.setU("kg");
		e1.setV(120.0);
		
		OLAPGsonE e2 = new OLAPGsonE();
		e2.setD("2006");
		e2.setF("");
		e2.setU("kg");
		e2.setV(120.0);
		
		OLAPGsonD d = new OLAPGsonD();
		d.setD("946");
		d.getV().add(e1);
		d.getV().add(e2);
		
		OLAPGsonC c  = new OLAPGsonC();
		c.setD("3");
		c.getV().add(d);
		
		OLAPGsonB b = new OLAPGsonB();
		b.setD("7224");
		b.getV().add(c);
		
		OLAPGsonA a = new OLAPGsonA();
		a.setD("k");
		a.getHeader().add("Element");
		a.getHeader().add("Area");
		a.getHeader().add("Item");
		a.getHeader().add("Year");
		a.getHeader().add("Flag");
		a.getHeader().add("Unit");
		a.getHeader().add("Value");
		a.getV().add(b);
		
		String s1 = g.toJson(a);
		System.out.println(s1);
		
	}
	
	public void testGsonWrapper() {
		
		try {
			
			List<List<String>> t = new ArrayList<List<String>>();
			
			List<String> h = new ArrayList<String>();
			h.add("element");
			h.add("area");
			h.add("item");
			h.add("year");
			h.add("flag");
			h.add("unit");
			h.add("value");
			t.add(h);  
			
			List<String> r1 = new ArrayList<String>();
			r1.add("7224");
			r1.add("3");
			r1.add("946");
			r1.add("2005");
			r1.add("");
			r1.add("kg");
			r1.add("120");
			t.add(r1); 
			
			List<String> r2 = new ArrayList<String>();
			r2.add("7224");
			r2.add("3");
			r2.add("946");
			r2.add("2006");
			r2.add("");
			r2.add("kg");
			r2.add("118");
			t.add(r2);
			
			List<String> r3 = new ArrayList<String>();
			r3.add("1518");
			r3.add("3");
			r3.add("946");
			r3.add("2006");
			r3.add("");
			r3.add("kg");
			r3.add("118");
			t.add(r3);
			
			StringBuilder sb = OLAPWrapper.gsonWrapper(t);
			System.out.println(sb);
			
		} catch (WDSException e) {
			e.printStackTrace();
		}
		
	}
	
}