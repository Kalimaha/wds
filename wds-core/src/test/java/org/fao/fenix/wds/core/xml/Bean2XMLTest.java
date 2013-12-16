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
package org.fao.fenix.wds.core.xml;

import junit.framework.TestCase;
import org.fao.fenix.wds.core.bean.DBBean;
import org.fao.fenix.wds.core.bean.FWDSBean;
import org.fao.fenix.wds.core.bean.RBean;
import org.fao.fenix.wds.core.bean.SQLBean;

public class Bean2XMLTest extends TestCase {

	private String XML = "<PAYLOAD><DB><DRIVER>SQLServer2000</DRIVER><URL>jdbc:sqlserver://FAOSTAT-PROD\\Production;databaseName=Warehouse;</URL><USERNAME>Warehouse</USERNAME><PASSWORD>w@reh0use</PASSWORD></DB><R><FUNCTION>lm</FUNCTION><PARAMETERS><PARAMETER><NAME>method</NAME><VALUE>qr</VALUE></PARAMETER><PARAMETER><NAME>qr</NAME><VALUE>TRUE</VALUE></PARAMETER><PARAMETER><NAME>singular.ok</NAME><VALUE>TRUE</VALUE></PARAMETER><PARAMETER><NAME>contrasts</NAME><VALUE>NULL</VALUE></PARAMETER></PARAMETERS></R><WS><URL>http://localhost:8080/r4f-web/services/R4FService</URL><PARAMETERS><PARAMETER><NAME>Test Name</NAME><VALUE>Test Value</VALUE></PARAMETER></PARAMETERS></WS><REST><URL>http://localhost:8080/r4f-web/REST</URL><PARAMETERS><PARAMETER><NAME>Method</NAME><VALUE>GET</VALUE></PARAMETER></PARAMETERS></REST><SQL><SELECTS><SELECT><AGGREGATION>NONE</AGGREGATION><COLUMN>Year</COLUMN><ALIAS>NONE</ALIAS></SELECT><SELECT><AGGREGATION>SUM</AGGREGATION><COLUMN>ItemCode</COLUMN><ALIAS>NONE</ALIAS></SELECT><SELECT><AGGREGATION>AVG</AGGREGATION><COLUMN>Value</COLUMN><ALIAS>NONE</ALIAS></SELECT></SELECTS><FROMS><FROM><COLUMN>Data</COLUMN><ALIAS>NONE</ALIAS></FROM></FROMS><WHERES><WHERE><DATATYPE>TEXT</DATATYPE><COLUMN>GroupCode</COLUMN><OPERATOR>=</OPERATOR><VALUE>Production</VALUE></WHERE><WHERE><DATATYPE>TEXT</DATATYPE><COLUMN>DomainCode</COLUMN><OPERATOR>=</OPERATOR><VALUE>Crops</VALUE></WHERE><WHERE><DATATYPE>DATE</DATATYPE><COLUMN>Year</COLUMN><OPERATOR>IN</OPERATOR><INS><IN>2001</IN><IN>2002</IN><IN>2003</IN><IN>2004</IN><IN>2005</IN></INS></WHERE></WHERES><GROUPBYS><GROUPBY><COLUMN>NONE</COLUMN></GROUPBY></GROUPBYS><ORDERBYS><ORDERBY><COLUMN>Year</COLUMN><DIRECTION>ASC</DIRECTION></ORDERBY></ORDERBYS><LIMIT>NONE</LIMIT></SQL><R><FUNCTION>lm</FUNCTION><SCRIPT></SCRIPT><PARAMETERS><PARAMETER><NAME>method</NAME><VALUE>qr</VALUE></PARAMETER></PARAMETERS></R></PAYLOAD>";
	
	private String XML_R = "<PAYLOAD><DB><DATASOURCE>FAOSTAT</DATASOURCE></DB><SQLS><SQL><QUERY>SELECT Value FROM Data WHERE ItemCode = '44' ORDER BY Year</QUERY></SQL><SQL><SELECTS><SELECT><AGGREGATION>NONE</AGGREGATION><COLUMN>Value</COLUMN><ALIAS>NONE</ALIAS></SELECT></SELECTS><FROMS><FROM><COLUMN>Data</COLUMN><ALIAS>NONE</ALIAS></FROM></FROMS><WHERES><WHERE><DATATYPE>TEXT</DATATYPE><COLUMN>DomainCode</COLUMN><OPERATOR>=</OPERATOR><VALUE>'QC'</VALUE></WHERE><WHERE><DATATYPE>TEXT</DATATYPE><COLUMN>ItemCode</COLUMN><OPERATOR>=</OPERATOR><VALUE>15</VALUE></WHERE><WHERE><DATATYPE>TEXT</DATATYPE><COLUMN>AreaCode</COLUMN><OPERATOR>IN</OPERATOR><INS><IN>2</IN></INS></WHERE></WHERES><ORDERBYS><ORDERBY><COLUMN>Year</COLUMN><DIRECTION>ASC</DIRECTION></ORDERBY></ORDERBYS></SQL></SQLS><R><FUNCTION>lm</FUNCTION><PARAMETERS><PARAMETER><NAME>method</NAME><VALUE>qr</VALUE></PARAMETER></PARAMETERS></R></PAYLOAD>";
	
	public void testConvertSQL() {
		SQLBean b = XML2Bean.convertSQL(XML);
		String xml = Bean2XML.convert(b).toString();
//		System.out.println(xml);
	}
	
	public void testConvertDB() {
		DBBean b = XML2Bean.convertDB(XML);
		String xml = Bean2XML.convert(b).toString();
//		System.out.println(xml);
	}
	
	public void testConvertFWDS() {
		DBBean db = XML2Bean.convertDB(XML);
		SQLBean sql = XML2Bean.convertSQL(XML);
		FWDSBean b = new FWDSBean();
		b.setDb(db);
		b.setSql(sql);
		String xml = Bean2XML.convert(b).toString();
//		System.out.println(xml);
	}
	
	public void testConvertR() {
		RBean r = XML2Bean.convertR(XML_R);
		assertEquals("lm", r.getFunction());
		assertEquals(1, r.getParameters().size());
		String xml = Bean2XML.convert(r).toString();
//		System.out.println(xml);
	}
	
}