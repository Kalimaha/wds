package org.fao.fenix.wds.core.jdbc;

import com.google.gson.Gson;
import junit.framework.TestCase;
import org.fao.fenix.wds.core.bean.DatasourceBean;
import org.fao.fenix.wds.core.constant.DRIVER;
import org.fao.fenix.wds.core.crud.CRUDSQL;
import org.fao.fenix.wds.core.utils.ExcelFactory;

import java.util.List;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 */
public class JDBCIterableTest extends TestCase {

    public void testRetrieveSQL() {
        JDBCIterable it = new JDBCIterable();
        DatasourceBean ds = this.getTravisTestBean();
        String query = "SELECT AreaCode, AreaNameE FROM Area";
        try {
            it.query(ds, query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> headers = it.getColumnNames();
        assertEquals(headers.size(), 2);
        int count = 0;
        while (it.hasNext()) {
            count++;
            it.next();
        }
        assertEquals(count, 1);
        query = "SELECT AreaCode, AreaNameE FROM Area WHERE AreaCode = '42'";
        try {
            it.query(ds, query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        headers = it.getColumnNames();
        assertEquals(headers.size(), 2);
        count = 0;
        while (it.hasNext()) {
            count++;
            it.next();
        }
        assertEquals(count, 0);
    }

    public void testRetrieveArrayOutput() {
        JDBCIterable it = new JDBCIterable();
        DatasourceBean ds = this.getTravisTestBean();
        String query = "SELECT AreaCode, AreaNameE FROM Area";
        try {
            it.query(ds, query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> headers = it.getColumnNames();
        assertEquals(headers.size(), 2);
        int count = 0;
        while (it.hasNext()) {
            count++;
            it.next();
        }
        assertEquals(count, 1);
        CRUDSQL crudsql = new CRUDSQL();
        Gson g = new Gson();
        String json = crudsql.createArrayOutput(it);
        try {
            g.fromJson(json, String[][].class);
        } catch (Exception e) {

        }
        query = "SELECT AreaCode, AreaNameE FROM Area WHERE AreaCode = '42'";
        try {
            it.query(ds, query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        headers = it.getColumnNames();
        assertEquals(headers.size(), 2);
        count = 0;
        while (it.hasNext()) {
            count++;
            it.next();
        }
        assertEquals(count, 0);
        json = crudsql.createArrayOutput(it);
        try {
            g.fromJson(json, String[][].class);
        } catch (Exception e) {

        }
    }

    private DatasourceBean getTravisTestBean() {
        DatasourceBean ds = new DatasourceBean();
        ds.setDriver(DRIVER.POSTGRESQL);
        ds.setId("TRAVIS_TEST");
        ds.setUrl("jdbc:postgresql://127.0.0.1:5432/travis_test");
        ds.setDbName("travis_test");
        ds.setUsername("postgres");
        ds.setPassword("");
        return ds;
    }

}