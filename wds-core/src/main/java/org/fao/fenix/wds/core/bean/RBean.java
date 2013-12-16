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

import org.fao.fenix.wds.core.constant.OUTPUT;
import org.fao.fenix.wds.core.constant.SQL;
import org.fao.fenix.wds.core.sql.SQLSplitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class RBean {

	private String function = "";
	
	private String script = "";
	
	private String sql = "";
	
	private Map<String, String> parameters = new HashMap<String, String>();
	
	private DBBean db = new DBBean();
	
	private List<SQLBean> sqlBeans = new ArrayList<SQLBean>();
	
	private VariableBean variables = new VariableBean();
	
	private OUTPUT output = OUTPUT.HTML;
	
	private boolean showCharts = false;
	
	private boolean splitSQL = false;
	
	private List<DBBean> dbs = new ArrayList<DBBean>();
	
	private List<Double> rawValues = new ArrayList<Double>();
	
	public RBean() {
		
	}
	
	public RBean(String sql, boolean splitSQL) {
		this.setSql(sql, splitSQL);
	}
	
	public RBean(List<DBBean> dbs, List<SQLBean> sqls) {
		this.setDbs(dbs);
		this.setSqlBeans(sqls);
	}
	
	public void addSQL(SQLBean b) { 
		if(this.getSqlBeans() == null)
			this.setSqlBeans(new ArrayList<SQLBean>());
		this.getSqlBeans().add(b);
	}
	
	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public DBBean getDb() {
		return db;
	}

	public void setDb(DBBean db) {
		this.db = db;
	}

	public List<SQLBean> getSqlBeans() {
		return sqlBeans;
	}

	public void setSqlBeans(List<SQLBean> sqls) {
		this.sqlBeans = sqls;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql, boolean splitSQL) {
		this.sql = sql;
		this.splitSQL = splitSQL;
		if (splitSQL && sql != null && !sql.equals("") && !sql.equals(SQL.NONE.name()))
			this.setSqlBeans(SQLSplitter.splitSQL(this.getSql()));
	}

	public VariableBean getVariables() {
		return variables;
	}

	public void setVariables(VariableBean variables) {
		this.variables = variables;
	}

	public OUTPUT getOutput() {
		return output;
	}

	public void setOutput(OUTPUT output) {
		this.output = output;
	}

	public boolean isShowCharts() {
		return showCharts;
	}

	public void setShowCharts(boolean showCharts) {
		this.showCharts = showCharts;
	}

	public boolean isSplitSQL() {
		return splitSQL;
	}

	public void setSplitSQL(boolean splitSQL) {
		this.splitSQL = splitSQL;
	}

	public List<DBBean> getDbs() {
		return dbs;
	}

	public void setDbs(List<DBBean> dbs) {
		this.dbs = dbs;
	}
	
	public void addDB(DBBean db) {
		if (this.dbs == null)
			this.dbs = new ArrayList<DBBean>();
		this.dbs.add(db);
	}

	public List<Double> getRawValues() {
		return rawValues;
	}

	public void setRawValues(List<Double> rawValues) {
		this.rawValues = rawValues;
	}
	
	public void addRawValue(Double v) {
		if (this.rawValues == null)
			this.rawValues = new ArrayList<Double>();
		this.rawValues.add(v);
	}
	
}