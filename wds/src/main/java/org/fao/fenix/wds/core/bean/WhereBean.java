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
package org.fao.fenix.wds.core.bean;

import org.fao.fenix.wds.core.constant.SQL;

import java.util.ArrayList;
import java.util.List;

/** 
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class WhereBean {

	private String datatype = SQL.TEXT.name();

	private String column = "";

	private String operator = "=";

	private String value = "";
	
	private List<String> ins = new ArrayList<String>();
	
	private SQLBean innerSQL;
	
	public WhereBean() {
		
	}

	public WhereBean(String datatype, String column, String operator, String value, List<String> ins) {
		super();
		this.setDatatype(datatype);
		this.setColumn(column);
		this.setOperator(operator);
		if (ins == null || ins.isEmpty()) {
			this.setValue(value);
		} else {
			this.setIns(ins);
		}
	}
	
	public WhereBean(String datatype, String column, String operator, String value, String[] ins) {
		super();
		this.setDatatype(datatype);
		this.setColumn(column);
		this.setOperator(operator);
		if (ins == null || ins.length < 1) {
			this.setValue(value);
		} else {
			for (String in : ins)
				this.getIns().add(in);
		}
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public List<String> getIns() {
		return ins;
	}

	public void setIns(List<String> ins) {
		this.ins = ins;
	}
	
	public void addIn(String in) {
		if (this.getIns() == null)
			this.setIns(new ArrayList<String>());
		this.getIns().add(in);
	}

	public SQLBean getInnerSQL() {
		return innerSQL;
	}

	public void setInnerSQL(SQLBean innerSQL) {
		this.innerSQL = innerSQL;
	}

	public String toString() {
		String in = "";
		for (int i = 0 ; i < this.getIns().size() ; i++) {
			in += this.getIns().get(i);
			if (i < this.getIns().size() - 1)
				in += ", ";
		}
		return "Datatype: " + this.getDatatype() + ", " +
			   "Column: " + this.getColumn() + ", " +
			   "Operator: " + this.getOperator() + ", " +
			   "Value: " + this.getValue() + ", " +
			   "In: " + in + ", " +
			   "Nested Query: " + this.getInnerSQL();
	}

}