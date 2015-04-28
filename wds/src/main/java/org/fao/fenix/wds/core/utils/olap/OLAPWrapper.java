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
package org.fao.fenix.wds.core.utils.olap;

import com.google.gson.Gson;
import org.fao.fenix.wds.core.bean.olap.*;
import org.fao.fenix.wds.core.exception.WDSException;
import org.fao.fenix.wds.core.utils.Wrapper;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.List;

/** 
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class OLAPWrapper extends Wrapper {

	public static List<List<String>> olapDimensions;

	public OLAPWrapper(Resource excel) throws WDSException {
		super(excel);
	}
	
	private static List<String> findAllElements(List<List<String>> t) {
		List<String> l = new ArrayList<String>();
		int idx = 0;
		for (int i = 1 ; i < t.size() ; i++) 
			if (!l.contains(t.get(i).get(idx)))
				l.add(t.get(i).get(idx));
		return l;
	}
	
	private static List<String> findAllAreas(List<List<String>> t, String element) {
		List<String> l = new ArrayList<String>();
		int idx = 1;
		for (int i = 1 ; i < t.size() ; i++) 
			if (t.get(i).get(0).equalsIgnoreCase(element) && !l.contains(t.get(i).get(idx)))
				l.add(t.get(i).get(idx));
		return l;
	}
	
	private static List<String> findAllItems(List<List<String>> t, String element, String area) {
		List<String> l = new ArrayList<String>();
		int idx = 2;
		for (int i = 1 ; i < t.size() ; i++) 
			if (t.get(i).get(0).equalsIgnoreCase(element) && t.get(i).get(1).equalsIgnoreCase(area) && !l.contains(t.get(i).get(idx)))
				l.add(t.get(i).get(idx));
		return l;
	}
	
	public static StringBuilder gsonWrapper(List<List<String>> t) throws WDSException {
		
		// initiate the output
		StringBuilder sb = new StringBuilder();
		Gson g = new Gson();
		OLAPGsonA a = new OLAPGsonA();
		
		// I need a fixed structure for the table, throw exception otherwise
		if (t.get(0).size() < 7) {
			throw new WDSException("This is a custom implementation of WDS for the pivot tables " +
									"and it needs a fixed structure for the data. These are the expected " +
									"columns, and the order matters: 'element', 'area', 'item', 'year', " +
									"'flag', 'unit', 'value'.");
		}
		
		// OK, let's start
		else {
			
			// find all unique elements, areas and items
			List<String> elements = findAllElements(t);
			
			// keep track of the last index used to fetch data
			// default 1 (skip headers)
			int idx = 1;
			
			for (String e : elements) {
				
				OLAPGsonB b = new OLAPGsonB();
				b.setD(e);
				
				// check wheter the element is already in the JSON
				if (!a.contains(b)) {
					
					// find all areas for the current element
					List<String> areas = findAllAreas(t, e);
					
					for (String area : areas) {
						
						OLAPGsonC c = new OLAPGsonC();
						c.setD(area);
						
						if (!b.contains(c)) {
							
							// find all the items for the current element and area
							List<String> items = findAllItems(t, e, area);
							
							for (String item : items) {
								
								OLAPGsonD d = new OLAPGsonD();
								d.setD(item);
								
								if (!c.contains(d)) {
									
									for (int i = idx ; i < t.size() ; i++) {
										
										boolean el = t.get(i).get(0).equals(e);
										boolean ar = t.get(i).get(1).equals(area);
										boolean it = t.get(i).get(2).equals(item);
										
										if (el && ar && it) {
											OLAPGsonE data = new OLAPGsonE();
											data.setD(t.get(i).get(3));
											data.setF(t.get(i).get(4));
											data.setU(t.get(i).get(5));
											data.setV(Double.valueOf(t.get(i).get(6)));
											if (d.getV() == null)
												d.setV(new ArrayList<OLAPGsonE>());
											d.getV().add(data);
										}
										
										
										// update the index for the next round
										else {
											
											idx = i;
											break;
											
										}
										
									}
									
									
									c.getV().add(d);
									
								}
								
							}
							
							
							b.getV().add(c);
							
						}
						
					}
					
					a.getV().add(b);
					
				}
				
			}
			
			// add headers
			a.setD("k");
			for (String s : t.get(0))
				a.getHeader().add(s);
			
		}
		
		// return the JSON
//		System.out.println(g.toJson(a));
		return sb.append(g.toJson(a));
	}

	public static StringBuilder wrapAsOLAPJSON(List<List<String>> table, boolean showNullValues, boolean addFlags) throws WDSException {
		StringBuilder sb = new StringBuilder();
		List<String> headers = table.get(0);
		StringBuilder hsb = new StringBuilder();
		hsb.append("\"header\":[");
		for (int i = 0; i < headers.size(); i++) {
			hsb.append("\"").append(headers.get(i)).append("\"");
			if (i < headers.size() - 1)
				hsb.append(",");
		}
		hsb.append("],");
		sb.append("{");
		sb.append(hsb);
		sb.append("\"d\":\"k\",");
		sb.append("\"v\":[{");
		olapDimensions = new ArrayList<List<String>>();
//		for (int i = 0; i < table.get(0).size() - 2; i++)
		if (addFlags) {
			for (int i = 0; i < table.get(0).size() - 3; i++)
				olapDimensions.add(extractCodes(table, i));
		} else {
			for (int i = 0; i < table.get(0).size() - 2; i++)
				olapDimensions.add(extractCodes(table, i));
		}
		List<OLAPNode> nodes = buildNodes(olapDimensions, table, 0, null, showNullValues, addFlags);
//		List<OLAPNode> nodes = buildNodes(olapDimensions, table, 1, null, showNullValues, addFlags);
		for (int i = 0; i < nodes.size(); i++) {
			OLAPNode n = nodes.get(i);
//			System.out.println("Node " + i + ": " + n);
			sb.append(n);
			if (i < nodes.size() - 1)
//				sb.append(",");
				sb.append("},{");
		}
		sb.append("}]}");
		StringBuilder clean = cleanJSON(sb);
//		System.out.println("original\n" + sb + "\n-> " + sb.indexOf("},{") + "\n");
//		System.out.println("clean\n" + clean + "\n");
		
//		for (int i = 0 ; i < table.size() ; i++) {
//			for (int j = 0 ; j < table.get(i).size() ; j++) {
//				System.out.print(table.get(i).get(j) + " | ");
//			}
//			if (i < table.size() - 1)
//				System.out.println();
//		}
		
		return clean;
	}
	
	public static StringBuilder cleanJSON(StringBuilder sb) {
		
		String s = "{},";
		while ((sb.indexOf(s)) > -1) 
			sb.replace(sb.indexOf(s), s.length() + sb.indexOf(s), "");
		
		s = "\"v\":[{}]";
		while ((sb.indexOf(s)) > -1)  
			sb.replace(sb.indexOf(s), s.length() + sb.indexOf(s), "");
		
		s = ",}";
		while ((sb.indexOf(s)) > -1)  
			sb.replace(sb.indexOf(s), s.length() + sb.indexOf(s), "}");
		
		int idx_1 = -1;
		int idx_2 = -1;
		for (int i = 0 ; i < sb.length() ; i++) {
			if (sb.charAt(i) == '{')
				idx_1 = i;
			if (sb.charAt(i) == '}')
				idx_2 = i;
			if (idx_1 > -1 && idx_2 > -1) {
				int count = 0;
				for (int j = idx_1 ; j < idx_2 ; j++)
					if (sb.charAt(j) == '"')
						count++;
				if (count == 4) {
					for (int z = idx_2 ; z >= idx_1 ; z--) {
						sb.setCharAt(z, ' ');
					}
				}
				i = 1 + idx_2;
				idx_1 = -1;
				idx_2 = -1;
			}
		}
		
		s = " , ";
		while ((sb.indexOf(s)) > -1) 
			sb.replace(sb.indexOf(s), s.length() + sb.indexOf(s), "");
		
		s = " ,{";
		while ((sb.indexOf(s)) > -1) 
			sb.replace(sb.indexOf(s), s.length() + sb.indexOf(s), "{");
		
		s = "}]}, ";
		while ((sb.indexOf(s)) > -1) 
			sb.replace(sb.indexOf(s), s.length() + sb.indexOf(s), "}]}");
		
		idx_1 = -1;
		idx_2 = -1;
		for (int i = 0 ; i < sb.length() ; i++) {
			if (sb.charAt(i) == '}')
				idx_1 = i;
			if (sb.charAt(i) == '{' && i > idx_1)
				idx_2 = i;
			if (idx_1 > -1 && idx_2 > -1 && idx_2 > idx_1) {
				String tmp = sb.substring(idx_1, 1 + idx_2);
				int blanks = 0;
				int commas = 0;
				for (int z = 0 ; z < tmp.length() ; z++) {
					switch (tmp.charAt(z)) {
						case ' ': blanks++; break;
						case ',': commas++; break;
					}
				}
				if (blanks > 0 && commas == 0) {
					String s1 = sb.substring(0, idx_1);
					String s2 = sb.substring(1 + idx_2);
					sb = new StringBuilder(s1 + "},{" + s2);
				}
				i = 1 + idx_2;
				idx_1 = -1;
				idx_2 = -1;
			}
		}
		
		s = "{}";
		while ((sb.indexOf(s)) > -1) 
			sb.replace(sb.indexOf(s), s.length() + sb.indexOf(s), "");
		
		s = "},]";
		while ((sb.indexOf(s)) > -1) 
			sb.replace(sb.indexOf(s), s.length() + sb.indexOf(s), "}]");
		
		return sb;
	}
	
	private static List<OLAPNode> buildNodes(List<List<String>> olapDimensions, List<List<String>> table, int idx, OLAPNode father, boolean showNullValues, boolean addFlags) {
		List<OLAPNode> nodes = new ArrayList<OLAPNode>();
		if (idx < olapDimensions.size()) {
			List<String> codes = olapDimensions.get(idx);
			for (String code : codes) {
				OLAPNode n = new OLAPNode(code, table, showNullValues, addFlags);
				if (father != null) {
					father.addNode(n);
					n.setFather(father);
				}
				buildNodes(olapDimensions, table, (1 + idx), n, showNullValues, addFlags);
				nodes.add(n);
			}
		}
		return nodes;
	}

	private static List<String> extractCodes(List<List<String>> table, int idx) throws WDSException {
		try {
			List<String> l = new ArrayList<String>();
			for (int i = 1; i < table.size(); i++) {
				List<String> row = table.get(i);
				if (!l.contains(row.get(idx))) {
					l.add(row.get(idx));
				}
			}
			return l;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}

}