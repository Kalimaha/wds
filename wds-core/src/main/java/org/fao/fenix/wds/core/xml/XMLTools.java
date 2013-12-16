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

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.fao.fenix.wds.core.exception.WDSException;

import java.io.StringReader;
import java.util.*;

/** 
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class XMLTools {
	
	public static Document parse(String xml) throws WDSException {
        try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(new StringReader(xml));
			return document;
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new WDSException(e.getMessage());
		}
    }
	
	public static String readChildTag(String xml, String tagFather, String tag, String root) throws WDSException {
		try {
			Document document = parse(xml);
			String xPathExpression = buildXPathExpression(tagFather, tag, root);
			Node node = document.selectSingleNode(xPathExpression);
			return node.getText();
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	public static String readAttribute(String xml, String attribute) throws WDSException {
		try {
			Document document = parse(xml);
			Node root = document.getRootElement();
			return root.valueOf("@" + attribute);
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	public static String readTag(String xml, String tag, String root) throws WDSException {
		try {
			Document document = parse(xml);
			String xPathExpression = buildXPathExpression(tag, root);
			Node node = document.selectSingleNode(xPathExpression);
			return node.getText();
		} catch (Exception e) {
			e.printStackTrace();
			throw new WDSException(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, String> readCollection(String xml, String[] tags, String nameTag, String valueTag, String root) throws WDSException {
		Map<String, String> m = new HashMap<String, String>();
		try {
			Document document = parse(xml);
			List l = document.selectNodes(buildXPathExpression(tags, root));
			for (int i = 0 ; i < l.size() ; i++) {
				Node parameters = (Node)l.get(i);
				Node name  = parameters.selectSingleNode(nameTag);
				Node value = parameters.selectSingleNode(valueTag);
				m.put(name.getText(), value.getText());
			}
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
		return m;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, String> readCollection(String xml, String[] tags, String valueTag, String root) throws WDSException {
		Map<String, String> m = new HashMap<String, String>();
		try {
			Document document = parse(xml);
			List l = document.selectNodes(buildXPathExpression(tags, root));
			for (int i = 0 ; i < l.size() ; i++) {
				Node parameters = (Node)l.get(i);
				Node value = parameters.selectSingleNode(valueTag);
				m.put(valueTag, value.getText());
			}
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
		return m;
	}
	
	@SuppressWarnings("unchecked")
	public static String subXML(String xml) throws WDSException {
		try {
			List<String> subs = new ArrayList<String>();
			Document document = parse(xml);
			Element root = document.getRootElement();
			for (Iterator i = root.elementIterator("SQL"); i.hasNext(); ) {
	            Element foo = (Element) i.next();
	            for (Iterator j = foo.elementIterator("SELECTS"); j.hasNext(); ) {
		            Element bar = (Element) j.next();
		            for (Iterator k = bar.elementIterator("SELECT"); k.hasNext(); ) {
			            Element top = (Element) k.next();
			            subs.add(top.asXML());
			        }
		        }
	        }
			for (String sub : subs) {
				sub = "<PAYLOAD>" + sub + "</PAYLOAD>";
			}
			return "";
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> subXML(String xml, String[] tags, String rootTag) throws WDSException {
		try {
			List<String> subs = new ArrayList<String>();
			Document document = parse(xml);
			Element root = document.getRootElement();
			for (int i = 0 ; i < tags.length ; i++) {
				if (i == tags.length - 1) {
					for (Iterator it = root.elementIterator(tags[i]); it.hasNext(); ) {
			            Element el = (Element) it.next();
			            subs.add(wrap(el.asXML(), rootTag));
			        }
				} else {
					for (Iterator it = root.elementIterator(tags[i]); it.hasNext(); ) {
						root = (Element) it.next();
						break;
					}
				}
			}
			return subs;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> readSubCollection(String xml, String[] tags, String root) throws WDSException {
		try {
			List<String> values = new ArrayList<String>();
			Document document = parse(xml);
			List l = document.selectNodes(buildXPathExpression(tags, root));
			for (int i = 0 ; i < l.size() ; i++) 
				values.add(((Node)l.get(i)).getText());
			return values;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> readSubCollectionAttribute(String xml, String[] tags, String root, String attribute) throws WDSException {
		try {
			List<String> values = new ArrayList<String>();
			Document document = parse(xml);
			List l = document.selectNodes(buildXPathExpression(tags, root));
			for (int i = 0 ; i < l.size() ; i++) {
				values.add(((Node)l.get(i)).valueOf("@" + attribute));
			}
			return values;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
	public static String wrap(String text, String tag) {
		StringBuilder sb = new StringBuilder();
		sb.append("<").append(tag).append(">");
		sb.append(text);
		sb.append("</").append(tag).append(">");
		return sb.toString();
	}
	
	public static String buildXPathExpression(String tagFather, String tag, String root) throws WDSException {
		StringBuilder sb = new StringBuilder();
		sb.append("//").append(root);
		sb.append("/").append(tagFather);
		sb.append("/").append(tag);
		return sb.toString();
	}
	
	public static String buildXPathExpression(String tag, String root) throws WDSException {
		StringBuilder sb = new StringBuilder();
		sb.append("//").append(root);
		sb.append("/").append(tag);
		return sb.toString();
	}
	
	public static String buildXPathExpression(List<String> tags, String root) throws WDSException {
		StringBuilder sb = new StringBuilder();
		sb.append("//").append(root);
		for (String tag : tags) 
			sb.append("/").append(tag);
		return sb.toString();
	}
	
	public static String buildXPathExpression(String[] tags, String root) throws WDSException {
		StringBuilder sb = new StringBuilder();
		sb.append("//").append(root);
		for (String tag : tags) 
			sb.append("/").append(tag);
		return sb.toString();
	}
	
}