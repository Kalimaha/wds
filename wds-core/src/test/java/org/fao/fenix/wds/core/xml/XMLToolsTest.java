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
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.fao.fenix.wds.core.constant.PAYLOAD;
import org.fao.fenix.wds.core.constant.R;
import org.fao.fenix.wds.core.constant.SELECT;
import org.fao.fenix.wds.core.constant.SQL;
import org.fao.fenix.wds.core.exception.WDSException;

import java.io.StringReader;
import java.util.List;
import java.util.Map;

public class XMLToolsTest extends TestCase {

	private String xml = "<PAYLOAD><DB><DRIVER>SQLServer2000</DRIVER><URL>jdbc:sqlserver://FAOSTAT-PROD\\Production;databaseName=Warehouse;</URL><USERNAME>Warehouse</USERNAME><PASSWORD>w@reh0use</PASSWORD></DB><R><FUNCTION>lm</FUNCTION><PARAMETERS><PARAMETER><NAME>method</NAME><VALUE>qr</VALUE></PARAMETER><PARAMETER><NAME>qr</NAME><VALUE>TRUE</VALUE></PARAMETER><PARAMETER><NAME>singular.ok</NAME><VALUE>TRUE</VALUE></PARAMETER><PARAMETER><NAME>contrasts</NAME><VALUE>NULL</VALUE></PARAMETER></PARAMETERS></R><WS><URL>http://localhost:8080/r4f-web/services/R4FService</URL><PARAMETERS><PARAMETER><NAME>Test Name</NAME><VALUE>Test Value</VALUE></PARAMETER></PARAMETERS></WS><REST><URL>http://localhost:8080/r4f-web/REST</URL><PARAMETERS><PARAMETER><NAME>Method</NAME><VALUE>GET</VALUE></PARAMETER></PARAMETERS></REST><SQL><SELECTS><SELECT><AGGREGATION>NONE</AGGREGATION><COLUMN>Year</COLUMN><ALIAS>NONE</ALIAS></SELECT><SELECT><AGGREGATION>SUM</AGGREGATION><COLUMN>ItemCode</COLUMN><ALIAS>NONE</ALIAS></SELECT><SELECT><AGGREGATION>AVG</AGGREGATION><COLUMN>Value</COLUMN><ALIAS>NONE</ALIAS></SELECT></SELECTS><FROMS><FROM><COLUMN>Data</COLUMN><ALIAS>NONE</ALIAS></FROM></FROMS><WHERES><WHERE><DATATYPE>TEXT</DATATYPE><COLUMN>GroupCode</COLUMN><OPERATOR>=</OPERATOR><VALUE>Production</VALUE></WHERE><WHERE><DATATYPE>TEXT</DATATYPE><COLUMN>DomainCode</COLUMN><OPERATOR>=</OPERATOR><VALUE>Crops</VALUE></WHERE><WHERE><DATATYPE>DATE</DATATYPE><COLUMN>Year</COLUMN><OPERATOR>IN</OPERATOR><INS><IN>2001</IN><IN>2002</IN><IN>2003</IN><IN>2004</IN><IN>2005</IN></INS></WHERE></WHERES><GROUPBYS><GROUPBY><COLUMN>NONE</COLUMN></GROUPBY></GROUPBYS><ORDERBYS><ORDERBY><COLUMN>Year</COLUMN><DIRECTION>ASC</DIRECTION></ORDERBY></ORDERBYS><LIMIT>NONE</LIMIT></SQL></PAYLOAD>";
	
	private String wb = "﻿<wb:data page=\"1\" pages=\"1\" per_page=\"50\" total=\"1\" xmlns:wb=\"http://www.worldbank.org\"><wb:data><wb:indicator id=\"NY.GDP.MKTP.CD\">GDP (current US$)</wb:indicator><wb:country id=\"BR\">Brazil</wb:country><wb:date>2009</wb:date><wb:value>1594489675023.99</wb:value><wb:decimal>0</wb:decimal></wb:data></wb:data>";
	
	public void testParse() {
		try {
			Document document = XMLTools.parse(xml);
			assertNotNull(document);
		} catch (WDSException e) {
			e.printStackTrace();
		}
	}
	
	public void testReadTag() {
		try {
			String tagContent = XMLTools.readChildTag(xml, "DB", "DRIVER", PAYLOAD.PAYLOAD.name());
			assertEquals("SQLServer2000", tagContent);
			tagContent = XMLTools.readChildTag(xml, "DB", "URL", PAYLOAD.PAYLOAD.name());
			assertEquals("jdbc:sqlserver://FAOSTAT-PROD\\Production;databaseName=Warehouse;", tagContent);
			tagContent = XMLTools.readChildTag(xml, "DB", "USERNAME", PAYLOAD.PAYLOAD.name());
			assertEquals("Warehouse", tagContent);
			tagContent = XMLTools.readChildTag(xml, "DB", "PASSWORD", PAYLOAD.PAYLOAD.name());
			assertEquals("w@reh0use", tagContent);
		} catch (WDSException e) {
			e.printStackTrace();
		}
	}
	
	public void testReadNullTag() {
		try {
			String tagContent = XMLTools.readChildTag(xml, "DB", "DATASOURCE", PAYLOAD.PAYLOAD.name());
			assertNull(tagContent);
		} catch (WDSException e) {
			e.printStackTrace();
		}
	}
	
	public void testReadCollection() {
		try {
			String[] tags = new String[]{R.R.name(), R.PARAMETERS.name(), R.PARAMETER.name()};
			Map<String, String> m = XMLTools.readCollection(xml, tags, R.NAME.name(), R.VALUE.name(), PAYLOAD.PAYLOAD.name());
			assertEquals(4, m.size());
			assertEquals("qr", m.get("method"));
			assertEquals("TRUE", m.get("qr"));
			assertEquals("TRUE", m.get("singular.ok"));
			assertEquals("NULL", m.get("contrasts"));
		} catch (WDSException e) {
			e.printStackTrace();
		}
	}
	
	public void testSubXML() {
		try {
			String[] tags = new String[]{SQL.SQL.name(), SELECT.SELECTS.name(), SELECT.SELECT.name()};
			List<String> subs = XMLTools.subXML(xml, tags, PAYLOAD.PAYLOAD.name());
			assertEquals(3, subs.size());
			assertEquals("<PAYLOAD><SELECT><AGGREGATION>NONE</AGGREGATION><COLUMN>Year</COLUMN><ALIAS>NONE</ALIAS></SELECT></PAYLOAD>", subs.get(0));
			assertEquals("<PAYLOAD><SELECT><AGGREGATION>SUM</AGGREGATION><COLUMN>ItemCode</COLUMN><ALIAS>NONE</ALIAS></SELECT></PAYLOAD>",  subs.get(1));
			assertEquals("<PAYLOAD><SELECT><AGGREGATION>AVG</AGGREGATION><COLUMN>Value</COLUMN><ALIAS>NONE</ALIAS></SELECT></PAYLOAD>",  subs.get(2));
		} catch (WDSException e) {
			e.printStackTrace();
		}
	}
	
	public void testParseWB() {
		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(new StringReader(wb));
//			System.out.println(((Node)((Element)((Element)((Element)document.node(0)).node(1)).node(1)).node(0)).getText());
//			System.out.println(((Node)((Element)((Element)((Element)document.node(0)).node(1)).node(1)).node(1)).getText());
//			System.out.println(((Node)((Element)((Element)((Element)document.node(0)).node(1)).node(1)).node(2)).getText());
//			System.out.println(((Node)((Element)((Element)((Element)document.node(0)).node(1)).node(1)).node(3)).getText());
//			System.out.println(((Node)((Element)((Element)((Element)document.node(0)).node(1)).node(1)).node(4)).getText());
//			System.out.println();
//			System.out.println(document.node(0));
//			System.out.println(((Node)((Element)document.node(0)).node(1)).getPath());
//			System.out.println(((Node)((Element)((Element)document.node(0)).node(1)).node(1)).getPath());
//			System.out.println(((Node)((Element)((Element)((Element)document.node(0)).node(1)).node(1)).node(2)).getPath());
//			System.out.println(((Node)((Element)((Element)((Element)document.node(0)).node(1)).node(1)).node(2)).getUniquePath());
//			System.out.println();
//			document = reader.read(new StringReader(wb));
//			String xPathExpression = "/PAYLOAD";
//			Node node = document.selectSingleNode(xPathExpression);
//			System.out.println("Node? " + (node == null));
//			System.out.println(((Node)((Element)node).node(1)).selectSingleNode("wb:data/wb:indicator"));
			
			Element root = document.getRootElement();
			Node tmp = root.selectSingleNode("wb:data/wb:indicator");
			System.out.println(tmp.getText());
			tmp = root.selectSingleNode("wb:data/wb:value");
			System.out.println(tmp.getText());
			tmp = root.selectSingleNode("wb:data/wb:date");
			System.out.println(tmp.getText());
			
			String xPathExpression = "wb:data/wb:date";
			Node node = document.getRootElement().selectSingleNode(xPathExpression);
			System.out.println("Node: " + node.getText());
			
			xPathExpression = "//wb:data/wb:data/wb:date";
			node = document.selectSingleNode(xPathExpression);
			System.out.println("Node: " + node.getText());
			
			System.out.println("XMLTools: " + XMLTools.readChildTag(wb, "wb:data", "wb:date", "wb:data"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testReadAttribute() {
		String total = XMLTools.readAttribute(wb, "total");
		assertEquals("1", total);
	}
	
}