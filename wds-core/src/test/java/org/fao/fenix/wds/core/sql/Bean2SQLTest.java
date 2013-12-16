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

import com.google.gson.Gson;
import junit.framework.TestCase;
import org.fao.fenix.wds.core.bean.NestedWhereBean;
import org.fao.fenix.wds.core.bean.SQLBean;
import org.fao.fenix.wds.core.constant.WHERE;
import org.fao.fenix.wds.core.xml.XML2Bean;
import org.fao.fenix.wds.core.constant.SQL;

public class Bean2SQLTest extends TestCase {

	private String xml = "<PAYLOAD><DB><DRIVER>SQLServer2000</DRIVER><URL>jdbc:sqlserver://FAOSTAT-PROD\\Production;databaseName=Warehouse;</URL><USERNAME>Warehouse</USERNAME><PASSWORD>w@reh0use</PASSWORD></DB><R><FUNCTION>lm</FUNCTION><PARAMETERS><PARAMETER><NAME>method</NAME><VALUE>qr</VALUE></PARAMETER><PARAMETER><NAME>qr</NAME><VALUE>TRUE</VALUE></PARAMETER><PARAMETER><NAME>singular.ok</NAME><VALUE>TRUE</VALUE></PARAMETER><PARAMETER><NAME>contrasts</NAME><VALUE>NULL</VALUE></PARAMETER></PARAMETERS></R><WS><URL>http://localhost:8080/r4f-web/services/R4FService</URL><PARAMETERS><PARAMETER><NAME>Test Name</NAME><VALUE>Test Value</VALUE></PARAMETER></PARAMETERS></WS><REST><URL>http://localhost:8080/r4f-web/REST</URL><PARAMETERS><PARAMETER><NAME>Method</NAME><VALUE>GET</VALUE></PARAMETER></PARAMETERS></REST><SQL><SELECTS><SELECT><AGGREGATION>NONE</AGGREGATION><COLUMN>Year</COLUMN><ALIAS>NONE</ALIAS></SELECT><SELECT><AGGREGATION>NONE</AGGREGATION><COLUMN>ItemCode</COLUMN><ALIAS>NONE</ALIAS></SELECT><SELECT><AGGREGATION>AVG</AGGREGATION><COLUMN>Value</COLUMN><ALIAS>NONE</ALIAS></SELECT></SELECTS><FROMS><FROM><COLUMN>Data</COLUMN><ALIAS>NONE</ALIAS></FROM></FROMS><WHERES><WHERE><DATATYPE>TEXT</DATATYPE><COLUMN>GroupCode</COLUMN><OPERATOR>=</OPERATOR><VALUE>Production</VALUE></WHERE><WHERE><DATATYPE>TEXT</DATATYPE><COLUMN>DomainCode</COLUMN><OPERATOR>=</OPERATOR><VALUE>Crops</VALUE></WHERE><WHERE><DATATYPE>DATE</DATATYPE><COLUMN>Year</COLUMN><OPERATOR>IN</OPERATOR><INS><IN>2001</IN><IN>2002</IN><IN>2003</IN><IN>2004</IN><IN>2005</IN></INS></WHERE></WHERES><GROUPBYS><GROUPBY><COLUMN>NONE</COLUMN></GROUPBY></GROUPBYS><ORDERBYS><ORDERBY><COLUMN>Year</COLUMN><DIRECTION>ASC</DIRECTION></ORDERBY></ORDERBYS><LIMIT>NONE</LIMIT></SQL></PAYLOAD>";
	
	public void _testConvertFromXML() {
		SQLBean b = XML2Bean.convertSQL(xml);
		String sql = Bean2SQL.convert(b).toString();
//		System.out.println(sql);
	}
	
	public void testConvertFromBean() {
		
		SQLBean b = new SQLBean();
		b.select(null, "Year", null);
		b.select(SQL.AVG.name(), "Value", "Export");
		b.from("Data", null);
		b.from("Pipsi", "AGG");
		b.where(SQL.TEXT.name(), "ItemCode", "=", "1027", null);
		b.where(SQL.DATE.name(), "Year", WHERE.IN.name(), null, new String[]{"2000", "2001", "2002"});
		b.groupBy("Year");
		b.groupBy("ItemCode");
		b.orderBy("Year", SQL.DESC.name());
		
		SQLBean b1 = new SQLBean();
		b1.select(null, "Year", null);
		b1.select(SQL.AVG.name(), "Value", "Export");
		b1.from("Data", null);
		b1.from("Pipsi", "AGG");
		b1.where(SQL.TEXT.name(), "ItemCode", "=", "1027", null);
		b1.where(SQL.DATE.name(), "Year", WHERE.IN.name(), null, new String[]{"2000", "2001", "2002"});
		b1.groupBy("Year");
		b1.groupBy("ItemCode");
		b1.orderBy("Year", SQL.DESC.name());
		
		NestedWhereBean n = new NestedWhereBean("Pippo", b1);
		b.nestedWhere(n);
		
		String sql = Bean2SQL.convert(b).toString();
		System.out.println(sql);
		
		Gson g = new Gson();
		System.out.println(g.toJson(b));
		
		String json = "{\"selects\":[{\"aggregation\":\"NONE\",\"column\":\"Year\",\"alias\":\"NONE\"},{\"aggregation\":\"AVG\",\"column\":\"Value\",\"alias\":\"Export\"}],\"froms\":[{\"column\":\"Data\",\"alias\":\"NONE\"},{\"column\":\"Pipsi\",\"alias\":\"AGG\"}],\"wheres\":[{\"datatype\":\"TEXT\",\"column\":\"ItemCode\",\"operator\":\"\u003d\",\"value\":\"1027\",\"ins\":[]},{\"datatype\":\"DATE\",\"column\":\"Year\",\"operator\":\"IN\",\"value\":\"\",\"ins\":[\"2000\",\"2001\",\"2002\"]}],\"nestedWheres\":[{\"column\":\"Pippo\",\"nestedCondition\":{\"selects\":[{\"aggregation\":\"NONE\",\"column\":\"Year\",\"alias\":\"NONE\"},{\"aggregation\":\"AVG\",\"column\":\"Value\",\"alias\":\"Export\"}],\"froms\":[{\"column\":\"Data\",\"alias\":\"NONE\"},{\"column\":\"Pipsi\",\"alias\":\"AGG\"}],\"wheres\":[{\"datatype\":\"TEXT\",\"column\":\"ItemCode\",\"operator\":\"\u003d\",\"value\":\"1027\",\"ins\":[]},{\"datatype\":\"DATE\",\"column\":\"Year\",\"operator\":\"IN\",\"value\":\"\",\"ins\":[\"2000\",\"2001\",\"2002\"]}],\"nestedWheres\":[],\"groupBys\":[{\"column\":\"Year\"},{\"column\":\"ItemCode\"}],\"orderBys\":[{\"column\":\"Year\",\"direction\":\"DESC\"}],\"limit\":\"NONE\",\"query\":\"NONE\",\"frequency\":\"NONE\"}}],\"groupBys\":[{\"column\":\"Year\"},{\"column\":\"ItemCode\"}],\"orderBys\":[{\"column\":\"Year\",\"direction\":\"DESC\"}],\"limit\":\"NONE\",\"query\":\"NONE\",\"frequency\":\"NONE\"}";
		SQLBean test = g.fromJson(json, SQLBean.class);
		System.out.println(Bean2SQL.convert(test));
		
	}
	
}