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
package org.fao.fenix.wds.core.bean.olap;

import java.util.ArrayList;
import java.util.List;

/** 
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class OLAPNode {

	private String code;
	
	private String value;
	
	private String unit;
	
	private List<OLAPNode> children = new ArrayList<OLAPNode>();
	
	private OLAPNode father;
	
	private List<List<String>> table;
	
	private boolean showNullValues;
	
	private boolean addFlags;
	
	public OLAPNode() {
		
	}
	
	public OLAPNode(String code, List<List<String>> table, boolean showNullValues) {
		this.setCode(code);
		this.setTable(table);
		this.setShowNullValues(showNullValues);
	}
	
	public OLAPNode(String code, List<List<String>> table, boolean showNullValues, boolean addFlags) {
		this.setCode(code);
		this.setTable(table);
		this.setShowNullValues(showNullValues);
		this.setAddFlags(addFlags);
	}
	
	public OLAPNode(String code, String value, String unit, List<List<String>> table, boolean showNullValues) {
		this.setCode(code);
		this.setUnit(unit);
		this.setValue(value);
		this.setTable(table);
		this.setShowNullValues(showNullValues);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public List<OLAPNode> getChildren() {
		return children;
	}

	public void setChildren(List<OLAPNode> children) {
		this.children = children;
	}
	
	public void addNode(OLAPNode n) {
		if (this.children == null)
			this.children = new ArrayList<OLAPNode>();
		this.children.add(n);
	}
	
	public void addNodes(List<OLAPNode> n) {
		if (this.children == null)
			this.children = new ArrayList<OLAPNode>();
		for (OLAPNode o : n) 
			o.setFather(this);
		this.children.addAll(n);
	}
	
	public OLAPNode getFather() {
		return father;
	}

	public void setFather(OLAPNode father) {
		this.father = father;
	}
	
	public int ancestors(OLAPNode n, int count) {
		if (n.getFather() != null)
			return ancestors(n.getFather(), ++count);
		return count;
	}

	public List<List<String>> getTable() {
		return table;
	}

	public void setTable(List<List<String>> table) {
		this.table = table;
	}
	
	public boolean isShowNullValues() {
		return showNullValues;
	}

	public void setShowNullValues(boolean showNullValues) {
		this.showNullValues = showNullValues;
	}

	public boolean isAddFlags() {
		return addFlags;
	}

	public void setAddFlags(boolean addFlags) {
		this.addFlags = addFlags;
	}

	private List<String> buildFilters(OLAPNode n, List<String> l) {
		if (!l.contains(n.getCode()))
			l.add(n.getCode());
		if (n.getFather() != null)
			return buildFilters(n.getFather(), l);
		return l;
	}
	
	private List<String> sortFilters(List<String> l) {
		List<String> f = new ArrayList<String>();
		for (int i = l.size() - 1 ; i >= 0 ; i--)
			f.add(l.get(i));
		return f;
	}
	
	private StringBuilder buildValueString(String code, List<String> filters) {
		StringBuilder sb = new StringBuilder();
		boolean found = false;
		for (List<String> row : this.getTable()) {
			int counter = 0;
			for (int i = 0 ; i < filters.size() ; i++) {
				if (row.get(i).equalsIgnoreCase(filters.get(i))) {
					counter++;
				}
			}
			if (counter == filters.size()) {
				if (this.isAddFlags()) {
					sb.append("\"f\":\"").append(row.get(row.size() - 3)).append("\",");
				}
				sb.append("\"d\":\"").append(code).append("\",");
				sb.append("\"v\":").append(row.get(row.size() - 1)).append(",");
				sb.append("\"u\":\"").append(row.get(row.size() - 2)).append("\"\n");
				found = true;
				break;
			}
		}
		if (!found && this.isShowNullValues()) {
			sb.append("\"f\":\"n.a.\",");
			sb.append("\"d\":\"").append(code).append("\",");
			sb.append("\"v\":\"n.a.\",");
			sb.append("\"u\":\"n.a.\"\n");
		}
		return sb;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (this.getChildren().isEmpty()) {
			List<String> filters = sortFilters(buildFilters(this, new ArrayList<String>()));
			StringBuilder value = buildValueString(this.getCode(), filters);
			if (value.length() > 0) 
				sb.append(value);
		} else {
			sb.append("\"d\":\"").append(this.getCode()).append("\",");
			sb.append("\"v\":[{");
			for (int i = 0 ; i < this.getChildren().size() ; i++) {
				sb.append(this.getChildren().get(i));
				if (i < this.getChildren().size()) {
					sb.append("},{");
				}
			}
			sb.append("}]");
		}
		return sb.toString();
	}
	
}