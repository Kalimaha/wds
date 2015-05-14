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

import org.fao.fenix.wds.core.constant.DATASOURCE;
import org.fao.fenix.wds.core.constant.OUTPUT;

/** 
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class FWDSBean {

	private SQLBean sql;
	
	private DBBean db;
	
	private OUTPUT output = OUTPUT.COLLECTION;
	
	private String css = "default";
	
	private boolean showNullValues = true;
	
	private boolean addFlags = true;
	
	public FWDSBean() {
		
	}
	
	public FWDSBean(SQLBean sql, DBBean db) {
		super();
		this.setSql(sql);
		this.setDb(db);
	}
	
	public FWDSBean(String datasource, String query) {
		DATASOURCE ds = DATASOURCE.valueOf(datasource.toUpperCase());
		DBBean db = new DBBean(ds);
		SQLBean sql = new SQLBean(query);
		this.setDb(db);
		this.setSql(sql);
	}

	public SQLBean getSql() {
		return sql;
	}

	public void setSql(SQLBean sql) {
		this.sql = sql;
	}

	public DBBean getDb() {
		return db;
	}

	public void setDb(DBBean db) {
		this.db = db;
	}

	public OUTPUT getOutput() {
		return output;
	}

	public void setOutput(OUTPUT output) {
		this.output = output;
	}

	public String getCss() {
		return css;
	}

	public void setCss(String css) {
		this.css = css;
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
	
}