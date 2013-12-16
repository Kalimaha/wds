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
import org.fao.fenix.wds.core.exception.WDSException;
import org.fao.fenix.wds.core.xml.XML2Bean;

import java.util.List;

public class XML2BeanTest extends TestCase {

	private String xml = "<PAYLOAD><DB><DRIVER>SQLServer2000</DRIVER><URL>jdbc:sqlserver://FAOSTAT-PROD\\Production;databaseName=Warehouse;</URL><USERNAME>Warehouse</USERNAME><PASSWORD>w@reh0use</PASSWORD></DB><R><FUNCTION>lm</FUNCTION><PARAMETERS><PARAMETER><NAME>method</NAME><VALUE>qr</VALUE></PARAMETER><PARAMETER><NAME>qr</NAME><VALUE>TRUE</VALUE></PARAMETER><PARAMETER><NAME>singular.ok</NAME><VALUE>TRUE</VALUE></PARAMETER><PARAMETER><NAME>contrasts</NAME><VALUE>NULL</VALUE></PARAMETER></PARAMETERS></R><WS><URL>http://localhost:8080/r4f-web/services/R4FService</URL><PARAMETERS><PARAMETER><NAME>Test Name</NAME><VALUE>Test Value</VALUE></PARAMETER></PARAMETERS></WS><REST><URL>http://localhost:8080/r4f-web/REST</URL><PARAMETERS><PARAMETER><NAME>Method</NAME><VALUE>GET</VALUE></PARAMETER></PARAMETERS></REST><SQL><SELECTS><SELECT><AGGREGATION>NONE</AGGREGATION><COLUMN>Year</COLUMN><ALIAS>NONE</ALIAS></SELECT><SELECT><AGGREGATION>SUM</AGGREGATION><COLUMN>ItemCode</COLUMN><ALIAS>NONE</ALIAS></SELECT><SELECT><AGGREGATION>AVG</AGGREGATION><COLUMN>Value</COLUMN><ALIAS>NONE</ALIAS></SELECT></SELECTS><FROMS><FROM><COLUMN>Data</COLUMN><ALIAS>NONE</ALIAS></FROM></FROMS><WHERES><WHERE><DATATYPE>TEXT</DATATYPE><COLUMN>GroupCode</COLUMN><OPERATOR>=</OPERATOR><VALUE>Production</VALUE></WHERE><WHERE><DATATYPE>TEXT</DATATYPE><COLUMN>DomainCode</COLUMN><OPERATOR>=</OPERATOR><VALUE>Crops</VALUE></WHERE><WHERE><DATATYPE>DATE</DATATYPE><COLUMN>Year</COLUMN><OPERATOR>IN</OPERATOR><INS><IN>2001</IN><IN>2002</IN><IN>2003</IN><IN>2004</IN><IN>2005</IN></INS></WHERE></WHERES><GROUPBYS><GROUPBY><COLUMN>NONE</COLUMN></GROUPBY></GROUPBYS><ORDERBYS><ORDERBY><COLUMN>Year</COLUMN><DIRECTION>ASC</DIRECTION></ORDERBY></ORDERBYS><LIMIT>NONE</LIMIT></SQL></PAYLOAD>";
	
	private String xml_faostat = "<PAYLOAD><DB><DATASOURCE>faostat</DATASOURCE></DB><SQL><QUERY>SELECT Year FROM Data WHERE ItemCode = '123'</QUERY></SQL></PAYLOAD>";
	
	private String xml_r = "<PAYLOAD><DB><DATASOURCE>FAOSTAT</DATASOURCE></DB><SQLS><SQL><QUERY>SELECT Value FROM Data WHERE ItemCode = '44' ORDER BY Year</QUERY></SQL><SQL><SELECTS><SELECT><AGGREGATION>NONE</AGGREGATION><COLUMN>Value</COLUMN><ALIAS>NONE</ALIAS></SELECT></SELECTS><FROMS><FROM><COLUMN>Data</COLUMN><ALIAS>NONE</ALIAS></FROM></FROMS><WHERES><WHERE><DATATYPE>TEXT</DATATYPE><COLUMN>DomainCode</COLUMN><OPERATOR>=</OPERATOR><VALUE>'QC'</VALUE></WHERE><WHERE><DATATYPE>TEXT</DATATYPE><COLUMN>ItemCode</COLUMN><OPERATOR>=</OPERATOR><VALUE>15</VALUE></WHERE><WHERE><DATATYPE>TEXT</DATATYPE><COLUMN>AreaCode</COLUMN><OPERATOR>IN</OPERATOR><INS><IN>2</IN></INS></WHERE></WHERES><ORDERBYS><ORDERBY><COLUMN>Year</COLUMN><DIRECTION>ASC</DIRECTION></ORDERBY></ORDERBYS></SQL></SQLS><R><FUNCTION>lm</FUNCTION><PARAMETERS><PARAMETER><NAME>method</NAME><VALUE>qr</VALUE></PARAMETER></PARAMETERS></R></PAYLOAD>";
	
	private String alias = "<PAYLOAD><DB><DATASOURCE>FAOSTAT</DATASOURCE></DB><SQLS><SQL><QUERY>SELECT Value, ItemCode IC, Year as Date FROM Data WHERE ItemCode = '44' ORDER BY Year</QUERY></SQL><SQL><SELECTS><SELECT><AGGREGATION>NONE</AGGREGATION><COLUMN>Value</COLUMN><ALIAS>NONE</ALIAS></SELECT></SELECTS><FROMS><FROM><COLUMN>Data</COLUMN><ALIAS>NONE</ALIAS></FROM></FROMS><WHERES><WHERE><DATATYPE>TEXT</DATATYPE><COLUMN>DomainCode</COLUMN><OPERATOR>=</OPERATOR><VALUE>'QC'</VALUE></WHERE><WHERE><DATATYPE>TEXT</DATATYPE><COLUMN>ItemCode</COLUMN><OPERATOR>=</OPERATOR><VALUE>15</VALUE></WHERE><WHERE><DATATYPE>TEXT</DATATYPE><COLUMN>AreaCode</COLUMN><OPERATOR>IN</OPERATOR><INS><IN>2</IN></INS></WHERE></WHERES><ORDERBYS><ORDERBY><COLUMN>Year</COLUMN><DIRECTION>ASC</DIRECTION></ORDERBY></ORDERBYS></SQL></SQLS><R><FUNCTION>lm</FUNCTION><PARAMETERS><PARAMETER><NAME>method</NAME><VALUE>qr</VALUE></PARAMETER></PARAMETERS></R></PAYLOAD>";
	
	private String xml_sql = "<PAYLOAD><DB><DATASOURCE>FAOSTAT</DATASOURCE></DB><SQL><QUERY>SELECT Value Production, Value as Yield, Value AS AreaHarvested FROM Data WHERE ItemCode = '15' AND DomainCode = 'QC' AND AreaCode = '2' VARIABLES ElementCode IN ('5510', '5419', '5312')</QUERY></SQL><R><FUNCTION>lm</FUNCTION><SCRIPT></SCRIPT><PARAMETERS><PARAMETER><NAME>method</NAME><VALUE>qr</VALUE></PARAMETER></PARAMETERS><CHARTS>true</CHARTS></R></PAYLOAD>";
	
	private String l = "<PAYLOAD><RAWVALUES><RAWVALUE>7</RAWVALUE><RAWVALUE>11</RAWVALUE></RAWVALUES></PAYLOAD>";
	
	public void testConvertRawValues() {
		XML2Bean.convertRawValues(l);
	}
	
	public void _testConvertDB() {
		try {
			DBBean b = XML2Bean.convertDB(xml);
			assertNull(b.getDatasource());
			assertEquals("SQLServer2000", b.getDriver());
			assertEquals("jdbc:sqlserver://FAOSTAT-PROD\\Production;databaseName=Warehouse;", b.getUrl());
			assertEquals("Warehouse", b.getUsername());
			assertEquals("w@reh0use", b.getPassword());
		} catch (WDSException e) {
			e.printStackTrace();
		}
	}
	
	public void _testConvertR() {
		try {
//			RBean b = XML2Bean.convertR(xml_r);
			RBean b = XML2Bean.convertR(xml_sql);
			assertEquals("lm", b.getFunction());
			assertEquals(1, b.getParameters().size());
			assertEquals("qr", b.getParameters().get("method"));
			assertEquals("SQLServer2000", b.getDb().getDriver());
			assertEquals("jdbc:sqlserver://FAOSTAT-PROD\\Production;databaseName=Warehouse;", b.getDb().getUrl());
			assertEquals("Warehouse", b.getDb().getUsername());
			assertEquals("w@reh0use", b.getDb().getPassword());
			assertEquals(3, b.getSqlBeans().size());
//			assertEquals("SELECT Value FROM Data WHERE ItemCode = '44' ORDER BY Year", b.getSqls().get(0).getQuery());
		} catch (WDSException e) {
			e.printStackTrace();
		}
	}
	
	public void _testConvertWS() {
		try {
			WSBean b = XML2Bean.convertWS(xml);
			assertEquals("http://localhost:8080/r4f-web/services/R4FService", b.getUrl());
			assertEquals(1, b.getParameters().size());
			assertEquals("Test Value", b.getParameters().get("Test Name"));
		} catch (WDSException e) {
			e.printStackTrace();
		}
	}
	
	public void _testConvertREST() {
		try {
			RESTBean b = XML2Bean.convertREST(xml);
			assertEquals("http://localhost:8080/r4f-web/REST", b.getUrl());
			assertEquals(1, b.getParameters().size());
			assertEquals("GET", b.getParameters().get("Method"));
		} catch (WDSException e) {
			e.printStackTrace();
		}
	}
	
	public void _testConvertSelects() {
		try {
			List<SelectBean> beans = XML2Bean.convertSelects(xml);
			assertEquals(3, beans.size());
			assertEquals("SUM", beans.get(1).getAggregation());
			assertEquals("ItemCode", beans.get(1).getColumn());
			assertEquals("NONE", beans.get(1).getAlias());
		} catch (WDSException e) {
			e.printStackTrace();
		}
	}
	
	public void _testConvertFroms() {
		try {
			List<FromBean> beans = XML2Bean.convertFroms(xml);
			assertEquals(1, beans.size());
			assertEquals("Data", beans.get(0).getColumn());
			assertEquals("NONE", beans.get(0).getAlias());
		} catch (WDSException e) {
			e.printStackTrace();
		}
	}
	
	public void _testConvertWheres() {
		try {
			List<WhereBean> beans = XML2Bean.convertWheres(xml);
			assertEquals(3, beans.size());
			assertEquals(5, beans.get(2).getIns().size());
			assertEquals("Year", beans.get(2).getColumn());
		} catch (WDSException e) {
			e.printStackTrace();
		}
	}
	
	public void _testConvertGroupBys() {
		try {
			List<GroupByBean> beans = XML2Bean.convertGroupBys(xml);
			assertEquals(1, beans.size());
			assertEquals("NONE", beans.get(0).getColumn());
		} catch (WDSException e) {
			e.printStackTrace();
		}
	}
	
	public void _testConvertOrderBys() {
		try {
			List<OrderByBean> beans = XML2Bean.convertOrderBys(xml);
			assertEquals(1, beans.size());
			assertEquals("Year", beans.get(0).getColumn());
			assertEquals("ASC", beans.get(0).getDirection());
		} catch (WDSException e) {
			e.printStackTrace();
		}
	}
	
	public void _testSQL() {
		try {
			SQLBean b = XML2Bean.convertSQL(xml);
			assertEquals("NONE", b.getLimit());
		} catch (WDSException e) {
			e.printStackTrace();
		}
	}
	
	public void _testConvertDBWithDatasource() {
		try {
			DBBean b = XML2Bean.convertDB(xml_faostat);
			assertNotNull(b.getDatasource());
			assertEquals("SQLServer2000", b.getDriver());
			assertEquals("jdbc:sqlserver://FAOSTAT-PROD\\Production;databaseName=Warehouse;", b.getUrl());
			assertEquals("Warehouse", b.getUsername());
			assertEquals("w@reh0use", b.getPassword());
		} catch (WDSException e) {
			e.printStackTrace();
		}
	}
	
	public void _testConvertSQLWithQuery() {
		try {
			SQLBean b = XML2Bean.convertSQL(xml_faostat);
			assertEquals("SELECT Year FROM Data WHERE ItemCode = '123'", b.getQuery());
			assertEquals(1, b.getSelects().size());
			assertEquals(0, b.getFroms().size());
			assertEquals(0, b.getWheres().size());
			assertEquals(0, b.getGroupBys().size());
			assertEquals(0, b.getOrderBys().size());
		} catch (WDSException e) {
			e.printStackTrace();
		}
	}
	
	public void _testConvertRWithAliases() {
		try {
			RBean b = XML2Bean.convertR(alias);
			assertEquals(2, b.getSqlBeans().size());
			assertEquals(3, b.getSqlBeans().get(0).getSelects().size());
			assertEquals(1, b.getSqlBeans().get(1).getSelects().size());
		} catch (WDSException e) {
			e.printStackTrace();
		}
	}
	
	public void testSQLs() {
		
		try {
			
			String PAYLOAD = "<?xml version='1.0' encoding='UTF-8'?><PAYLOAD><DB><CONNECTION>JDBC</CONNECTION><DATASOURCE>FAOSTAT</DATASOURCE><DRIVER></DRIVER><PASSWORD></PASSWORD><URL></URL><USERNAME></USERNAME></DB><SQLS><SQL><QUERY></QUERY><SELECTS><SELECT><AGGREGATION>NONE</AGGREGATION><COLUMN>Value</COLUMN><ALIAS>Production</ALIAS></SELECT></SELECTS><FROMS><FROM><COLUMN>Data</COLUMN><ALIAS>NONE</ALIAS></FROM></FROMS><WHERES><WHERE><DATATYPE>TEXT</DATATYPE><COLUMN>ItemCode</COLUMN><OPERATOR>=</OPERATOR><VALUE>15</VALUE></WHERE><WHERE><DATATYPE>TEXT</DATATYPE><COLUMN>DomainCode</COLUMN><OPERATOR>=</OPERATOR><VALUE>QC</VALUE></WHERE><WHERE><DATATYPE>TEXT</DATATYPE><COLUMN>ElementCode</COLUMN><OPERATOR>=</OPERATOR><VALUE>5510</VALUE></WHERE></WHERES><ORDERBYS><ORDERBY><COLUMN>Year</COLUMN><DIRECTION>ASC</DIRECTION></ORDERBY></ORDERBYS></SQL><SQL><QUERY></QUERY><SELECTS><SELECT><AGGREGATION>NONE</AGGREGATION><COLUMN>Value</COLUMN><ALIAS>Yield</ALIAS></SELECT></SELECTS><FROMS><FROM><COLUMN>Data</COLUMN><ALIAS>NONE</ALIAS></FROM></FROMS><WHERES><WHERE><DATATYPE>TEXT</DATATYPE><COLUMN>ItemCode</COLUMN><OPERATOR>=</OPERATOR><VALUE>15</VALUE></WHERE><WHERE><DATATYPE>TEXT</DATATYPE><COLUMN>DomainCode</COLUMN><OPERATOR>=</OPERATOR><VALUE>QC</VALUE></WHERE><WHERE><DATATYPE>TEXT</DATATYPE><COLUMN>ElementCode</COLUMN><OPERATOR>=</OPERATOR><VALUE>5419</VALUE></WHERE></WHERES><ORDERBYS><ORDERBY><COLUMN>Year</COLUMN><DIRECTION>ASC</DIRECTION></ORDERBY></ORDERBYS></SQL></SQLS><R><FUNCTION>lm</FUNCTION>" +
							 "<SPLITSQL>false</SPLITSQL>" +
							 "</R></PAYLOAD>";
			RBean r = XML2Bean.convertR(PAYLOAD);
			assertEquals(2, r.getSqlBeans().size());
			
			PAYLOAD = "<?xml version='1.0' encoding='UTF-8'?><PAYLOAD><DB><CONNECTION>JDBC</CONNECTION><DATASOURCE>FAOSTAT</DATASOURCE><DRIVER></DRIVER><PASSWORD></PASSWORD><URL></URL><USERNAME></USERNAME></DB>" +
					  "<SQLS>" +
					  "<SQL><QUERY>" +
					  "SELECT Value AS Production, Value AS Yield FROM Data WHERE ItemCode = 15 AND DomainCode = 'QC' VARIABLES ElementCode IN ('5510', '5419') ORDER BY Year " +
					  "</QUERY></SQL>" +
					  "</SQLS><R><FUNCTION>lm</FUNCTION>" +
					  "<SPLITSQL>true</SPLITSQL>" +
					  "</R></PAYLOAD>";
			r = XML2Bean.convertR(PAYLOAD);
			assertEquals(2, r.getSqlBeans().size());
			
		} catch (WDSException e) {
			e.printStackTrace();
		}
		
	}
	
}