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

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class SelectBean {

	private String aggregation = SQL.NONE.name();
	
	private String column = "";
	
	private String alias = SQL.NONE.name();
	
	public SelectBean() {
		
	}
	
	public SelectBean(String aggregation, String column, String alias) {
		super();
		if (aggregation == null) {
			this.setAggregation(SQL.NONE.name());
		} else {
			this.setAggregation(aggregation);
		}
		this.column = column;
		if (alias == null) {
			this.setAlias(SQL.NONE.name());
		} else {
			this.setAlias(alias);
		}
	}

	public String getAggregation() {
		return aggregation;
	}

	public void setAggregation(String aggregation) {
		this.aggregation = aggregation;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public String toString() {
		return "Aggregation: " + this.getAggregation() + 
			   ", Column: " + this.getColumn() + 
			   ", Alias: " + this.getAlias();
	}
	
}