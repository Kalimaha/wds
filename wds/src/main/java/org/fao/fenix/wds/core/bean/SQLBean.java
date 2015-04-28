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
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class SQLBean {

	private List<SelectBean> selects = new ArrayList<SelectBean>();
	
	private List<FromBean> froms = new ArrayList<FromBean>();
	
	private List<WhereBean> wheres = new ArrayList<WhereBean>();
	
	private List<NestedWhereBean> nestedWheres = new ArrayList<NestedWhereBean>();
	
	private List<GroupByBean> groupBys = new ArrayList<GroupByBean>();
	
	private List<OrderByBean> orderBys = new ArrayList<OrderByBean>();
	
	private String limit = null;
	
	private String query = null;
	
	private String frequency = null;
	
	private boolean skipCharactersReplacement = false; 
	
	private boolean addHeaders = true; 
	
	public SQLBean() {
		
	}
	
	public SQLBean(String query) {
		this.setQuery(query);
	}
	
	public void select(String aggregation, String column, String alias) {
		if (this.getSelects() == null)
			this.setSelects(new ArrayList<SelectBean>());
		SelectBean b = new SelectBean(aggregation, column, alias);
		this.getSelects().add(b);
	}
	
	public void select(SelectBean b) {
		if (this.getSelects() == null)
			this.setSelects(new ArrayList<SelectBean>());
		this.getSelects().add(b);
	}
	
	public void select(List<SelectBean> l) {
		for (SelectBean b : l)
			this.select(b);
	}
	
	public void from(String column, String alias) {
		if (this.getFroms() == null)
			this.setFroms(new ArrayList<FromBean>());
		FromBean b = new FromBean(column, alias);
		this.getFroms().add(b);
	}
	
	public void from(FromBean b) {
		if (this.getFroms() == null)
			this.setFroms(new ArrayList<FromBean>());
		this.getFroms().add(b);
	}
	
	public void from(List<FromBean> l) {
		for (FromBean b : l)
			this.from(b);
	}

	public void where(String datatype, String column, String operator, String value, String[] ins) {
		if (this.getWheres() == null)
			this.setWheres(new ArrayList<WhereBean>());
		WhereBean b = new WhereBean(datatype, column, operator, value, ins);
		this.getWheres().add(b);
	}
	
	public void where(WhereBean b) {
		if (this.getWheres() == null)
			this.setWheres(new ArrayList<WhereBean>());
		this.getWheres().add(b);
	}
	
	public void where(List<WhereBean> l) {
		for (WhereBean b : l)
			this.where(b);
	}
	
	public void nestedWhere(String column, SQLBean nestedCondition) {
		if (this.getNestedWheres() == null)
			this.setNestedWheres(new ArrayList<NestedWhereBean>());
		NestedWhereBean b = new NestedWhereBean(column, nestedCondition);
		this.getNestedWheres().add(b);
	}
	
	public void nestedWhere(NestedWhereBean b) {
		if (this.getNestedWheres() == null)
			this.setNestedWheres(new ArrayList<NestedWhereBean>());
		this.getNestedWheres().add(b);
	}
	
	public void nestedWhere(List<NestedWhereBean> l) {
		for (NestedWhereBean b : l)
			this.nestedWhere(b);
	}
	
	public void groupBy(String column) {
		if (this.getGroupBys() == null)
			this.setGroupBys(new ArrayList<GroupByBean>());
		GroupByBean b = new GroupByBean(column);
		this.getGroupBys().add(b);
	}
	
	public void groupBy(GroupByBean b) {
		if (this.getGroupBys() == null)
			this.setGroupBys(new ArrayList<GroupByBean>());
		this.getGroupBys().add(b);
	}
	
	public void groupBy(List<GroupByBean> l) {
		for (GroupByBean b : l)
			this.groupBy(b);
	}
	
	public void orderBy(String column, String direction) {
		if (this.getOrderBys() == null)
			this.setOrderBys(new ArrayList<OrderByBean>());
		OrderByBean b = new OrderByBean(column, direction);
		this.getOrderBys().add(b);
	}
	
	public void orderBy(OrderByBean b) {
		if (this.getOrderBys() == null)
			this.setOrderBys(new ArrayList<OrderByBean>());
		this.getOrderBys().add(b);
	}
	
	public void orderBy(List<OrderByBean> l) {
		for (OrderByBean b : l)
			this.orderBy(b);
	}
	
	public List<SelectBean> getSelects() {
		return selects;
	}

	public void setSelects(List<SelectBean> selects) {
		this.selects = selects;
	}

	public List<FromBean> getFroms() {
		return froms;
	}

	public void setFroms(List<FromBean> froms) {
		this.froms = froms;
	}

	public List<WhereBean> getWheres() {
		return wheres;
	}

	public void setWheres(List<WhereBean> wheres) {
		this.wheres = wheres;
	}
	
	public List<NestedWhereBean> getNestedWheres() {
		return nestedWheres;
	}

	public void setNestedWheres(List<NestedWhereBean> nestedWheres) {
		this.nestedWheres = nestedWheres;
	}

	public List<GroupByBean> getGroupBys() {
		return groupBys;
	}

	public void setGroupBys(List<GroupByBean> groupBys) {
		this.groupBys = groupBys;
	}

	public List<OrderByBean> getOrderBys() {
		return orderBys;
	}

	public void setOrderBys(List<OrderByBean> orderBys) {
		this.orderBys = orderBys;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
	public String toString() {
		return "Limit: " + this.getLimit() + ", Query: " + this.getQuery();
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public boolean isSkipCharactersReplacement() {
		return skipCharactersReplacement;
	}

	public void setSkipCharactersReplacement(boolean skipCharactersReplacement) {
		this.skipCharactersReplacement = skipCharactersReplacement;
	}

	public boolean isAddHeaders() {
		return addHeaders;
	}

	public void setAddHeaders(boolean addHeaders) {
		this.addHeaders = addHeaders;
	}
	
}