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
package org.fao.fenix.wds.core.faostat.sql;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.fao.fenix.wds.core.bean.DBBean;
import org.fao.fenix.wds.core.bean.SQLBean;
import org.fao.fenix.wds.core.constant.DATASOURCE;
import org.fao.fenix.wds.core.jdbc.JDBCConnector;
import org.fao.fenix.wds.core.sql.faostat.SQLBeansRepository;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 */
public class StoredQueriesStepsDefinition {

    SQLBean sql;

    DATASOURCE ds;

    DBBean db;

    List<List<String>> table;

    @Given("^a JDBC connection$")
    public void a_JDBC_connection() throws Throwable {
        ds = DATASOURCE.FAOSTAT;
        db = new DBBean(ds);
    }

    @When("^I send the domain code \"([^\"]*)\"$")
    public void I_send_the_domain_code(String domainCode) throws Throwable {
        sql = SQLBeansRepository.getYears(domainCode, null, null);
        table = JDBCConnector.query(db, sql, false);
    }

    @Then("^I get a list of (\\d+) years$")
    public void I_get_a_list_of_years(int arg1) throws Throwable {
        assertEquals(arg1, table.size());
    }

}