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

import org.fao.fenix.wds.core.constant.CONNECTION;
import org.fao.fenix.wds.core.constant.DATASOURCE;
import org.fao.fenix.wds.core.datasource.*;

/** 
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class DBBean {

	private String driver = "";
	
	private String url = "";
	
	private String baseurl = "";
	
	private String username = "";
	
	private String password = "";
	
	private DATASOURCE datasource;
	
	private String databaseName = "";
	
	private CONNECTION connection = CONNECTION.JDBC;
	
	public DBBean() {
		
	}
	
	public DBBean(DATASOURCE datasource) {
		super();
		switch (datasource) {
			case FAOSTAT:
				this.setDatasource(DATASOURCE.FAOSTAT);
				this.setDriver(FAOSTAT.DRIVER);
				this.setPassword(FAOSTAT.PASSWORD);
				this.setUrl(FAOSTAT.URL);
				this.setUsername(FAOSTAT.USERNAME);
				this.setDatabaseName(FAOSTAT.DATABASE_NAME);
				this.setConnection(CONNECTION.JDBC);
			break;
            case FAOSTAT2:
                this.setDatasource(DATASOURCE.FAOSTAT2);
                this.setDriver(FAOSTAT2.DRIVER);
                this.setPassword(FAOSTAT2.PASSWORD);
                this.setUrl(FAOSTAT2.URL);
                this.setUsername(FAOSTAT2.USERNAME);
                this.setDatabaseName(FAOSTAT2.DATABASE_NAME);
                this.setConnection(CONNECTION.JDBC);
                break;
			case FAOSTATPROD:
				this.setDatasource(DATASOURCE.FAOSTATPROD);
				this.setDriver(FAOSTATPROD.DRIVER);
				this.setPassword(FAOSTATPROD.PASSWORD);
				this.setUrl(FAOSTATPROD.URL);
				this.setUsername(FAOSTATPROD.USERNAME);
				this.setDatabaseName(FAOSTATPROD.DATABASE_NAME);
				this.setConnection(CONNECTION.JDBC);
			break;
			case FAOSTATPRODDISS:
				this.setDatasource(DATASOURCE.FAOSTATPRODDISS);
				this.setDriver(FAOSTATPRODDISS.DRIVER);
				this.setPassword(FAOSTATPRODDISS.PASSWORD);
				this.setUrl(FAOSTATPRODDISS.URL);
				this.setUsername(FAOSTATPRODDISS.USERNAME);
				this.setDatabaseName(FAOSTATPRODDISS.DATABASE_NAME);
				this.setConnection(CONNECTION.JDBC);
			break;
            case FAOSTATTESTDISS:
                this.setDatasource(DATASOURCE.FAOSTATTESTDISS);
                this.setDriver(FAOSTATTESTDISS.DRIVER);
                this.setPassword(FAOSTATTESTDISS.PASSWORD);
                this.setUrl(FAOSTATTESTDISS.URL);
                this.setUsername(FAOSTATTESTDISS.USERNAME);
                this.setDatabaseName(FAOSTATTESTDISS.DATABASE_NAME);
                this.setConnection(CONNECTION.JDBC);
                break;
			case FAOSTATGLBL:
				this.setDatasource(DATASOURCE.FAOSTATGLBL);
				this.setDriver(FAOSTATGLBL.DRIVER);
				this.setPassword(FAOSTATGLBL.PASSWORD);
				this.setUrl(FAOSTATGLBL.URL);
				this.setUsername(FAOSTATGLBL.USERNAME);
				this.setDatabaseName(FAOSTATGLBL.DATABASE_NAME);
				this.setConnection(CONNECTION.JDBC);
			break;
			case FAOSYB:
				this.setDatasource(DATASOURCE.FAOSYB);
				this.setDriver(FAOSYB.DRIVER);
				this.setPassword(FAOSYB.PASSWORD);
				this.setUrl(FAOSYB.URL);
				this.setUsername(FAOSYB.USERNAME);
				this.setDatabaseName(FAOSYB.DATABASE_NAME);
				this.setConnection(CONNECTION.JDBC);
			break;
			case FENIXDATAMANAGER:
				this.setDatasource(DATASOURCE.FENIXDATAMANAGER);
				this.setDriver(FENIXDATAMANAGER.DRIVER);
				this.setPassword(FENIXDATAMANAGER.PASSWORD);
				this.setUrl(FENIXDATAMANAGER.URL);
				this.setUsername(FENIXDATAMANAGER.USERNAME);
				this.setDatabaseName(FENIXDATAMANAGER.DATABASE_NAME);
				this.setConnection(CONNECTION.JDBC);
			break;
			case CROWDPRICES:
				this.setDatasource(DATASOURCE.CROWDPRICES);
				this.setDriver(CROWDPRICES.DRIVER);
				this.setPassword(CROWDPRICES.PASSWORD);
				this.setUrl(CROWDPRICES.URL);
				this.setUsername(CROWDPRICES.USERNAME);
				this.setDatabaseName(CROWDPRICES.DATABASE_NAME);
				this.setConnection(CONNECTION.JDBC);
			break;
			case STAGINGAREA:
				this.setDatasource(DATASOURCE.STAGINGAREA);
				this.setDriver(STAGINGAREA.DRIVER);
				this.setPassword(STAGINGAREA.PASSWORD);
				this.setUrl(STAGINGAREA.URL);
				this.setUsername(STAGINGAREA.USERNAME);
				this.setDatabaseName(STAGINGAREA.DATABASE_NAME);
				this.setConnection(CONNECTION.JDBC);
			break;
			case FENIX:
				this.setDatasource(DATASOURCE.FENIX);
				this.setDriver(FENIX.DRIVER);
				this.setPassword(FENIX.PASSWORD);
				this.setUrl(FENIX.URL);
				this.setUsername(FENIX.USERNAME);
				this.setDatabaseName(FENIX.DATABASE_NAME);
				this.setConnection(CONNECTION.JDBC);
			break;
			case WORLDBANK:
				this.setDatasource(DATASOURCE.WORLDBANK);
				this.setBaseurl(WORLDBANK.BASEURL);
				this.setConnection(CONNECTION.WS);
			break;
			case USDA:
				this.setDatasource(DATASOURCE.USDA);
				this.setBaseurl(USDA.BASEURL);
				this.setConnection(CONNECTION.WS);
			break;
            case AMISPOLICIES:
                this.setDatasource(DATASOURCE.AMISPOLICIES);
                this.setDriver(AMISPOLICIES.DRIVER);
                this.setPassword(AMISPOLICIES.PASSWORD);
                this.setUrl(AMISPOLICIES.URL);
                this.setUsername(AMISPOLICIES.USERNAME);
                this.setDatabaseName(AMISPOLICIES.DATABASE_NAME);
                this.setConnection(CONNECTION.JDBC);
             break;
            case POLICY:
                this.setDatasource(DATASOURCE.POLICY);
                this.setDriver(POLICY.DRIVER);
                this.setPassword(POLICY.PASSWORD);
                this.setUrl(POLICY.URL);
                this.setUsername(POLICY.USERNAME);
                this.setDatabaseName(POLICY.DATABASE_NAME);
                this.setConnection(CONNECTION.JDBC);
                break;
		}
	}
	
	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public DATASOURCE getDatasource() {
		return datasource;
	}

	public void setDatasource(DATASOURCE datasource) {
		this.datasource = datasource;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBaseurl() {
		return baseurl;
	}

	public void setBaseurl(String baseurl) {
		this.baseurl = baseurl;
	}

	public CONNECTION getConnection() {
		return connection;
	}

	public void setConnection(CONNECTION connection) {
		this.connection = connection;
	}
	
}