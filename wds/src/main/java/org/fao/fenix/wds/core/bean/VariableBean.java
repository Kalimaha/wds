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

import java.util.ArrayList;
import java.util.List;

/**
 * This bean is used to split a single SQL in multiple queries through
 * <code>SQLSplitter</code> to build a matrix suitable for R analysis. A new SQL
 * is created for each entry in the SELECT clause. A new WHERE entry is created
 * based on the <code>column</code> field.
 * 
 * 
 * Example - INPUT
 * ============================================================================
 * SQL: 		SELECT Value Production, Value as Yield, Value AS AreaHarvested
 * 				FROM Data 
 * 				WHERE ItemCode = '15' 
 * 				AND DomainCode = 'QC' 
 * 				AND AreaCode = '2'
 * Column: 		ElementCode
 * Variables:	5510, 5419, 5312
 * 
 * 
 * Example - OUTPUT
 * ============================================================================
 * SQL1:		SELECT Value AS Production
 * 				WHERE ItemCode = '15' 
 * 				AND DomainCode = 'QC' 
 * 				AND AreaCode = '2'
 * 				AND ElementCode = '5510'
 * SQL2:		SELECT Value AS Yield
 * 				WHERE ItemCode = '15' 
 * 				AND DomainCode = 'QC' 
 * 				AND AreaCode = '2'
 * 				AND ElementCode = '5419'
 * SQL3:		SELECT Value AS AreaHarvested
 * 				WHERE ItemCode = '15' 
 * 				AND DomainCode = 'QC' 
 * 				AND AreaCode = '2'
 * 				AND ElementCode = '5312'
 * 
 */

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class VariableBean {

	private String column = "";

	private List<String> variables = new ArrayList<String>();

	public VariableBean() {

	}

	public VariableBean(String column) {
		this.setColumn(column);
	}

	public void addVariable(String variable) {
		if (this.getVariables() == null)
			this.setVariables(new ArrayList<String>());
		this.getVariables().add(variable);
	}

	public String toString() {
		String in = "";
		for (int i = 0; i < this.getVariables().size(); i++) {
			in += this.getVariables().get(i);
			if (i < this.getVariables().size() - 1)
				in += ", ";
		}
		return "Column: " + this.getColumn() + ", " + "Variable: " + in;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public List<String> getVariables() {
		return variables;
	}

	public void setVariables(List<String> variables) {
		this.variables = variables;
	}

}