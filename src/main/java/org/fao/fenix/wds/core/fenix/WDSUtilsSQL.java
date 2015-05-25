package org.fao.fenix.wds.core.fenix;

import com.google.gson.Gson;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import org.fao.fenix.wds.core.bean.DatasourceBean;
import org.fao.fenix.wds.core.fenix.bean.RetrieveMongoDBBean;
import org.fao.fenix.wds.core.fenix.bean.RetrieveSQLBean;
import org.fao.fenix.wds.core.jdbc.JDBCIterable;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * */
public class WDSUtilsSQL implements WDSUtils {

    private Gson g = new Gson();

    public List<String> create(final DatasourceBean ds, final String documents, final String collection) throws Exception {

        /* Prepare the output. */
        StringBuilder sb;
        List<String> ids = new ArrayList<String>();

        /* Convert input. */
        Map<String, Object>[] data = g.fromJson(documents, Map[].class);

        /* Get connection. */
        Connection connection = null;
        switch (ds.getDriver()) {
            case POSTGRESQL:
                Class.forName("org.postgresql.Driver");
                break;
            case SQLSERVER2000:
                SQLServerDriver.class.newInstance();
                break;
        }
        connection = DriverManager.getConnection(ds.getUrl(), ds.getUsername(), ds.getPassword());
        boolean autocommit = connection.getAutoCommit();

        /* Insert data. */
        try {

            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            for (int i = 0; i < data.length; i++) {
                sb = new StringBuilder();
                sb.append("INSERT INTO \"").append(collection).append("\"(");
                int j = 0;
                for (String key : data[i].keySet()) {
                    sb.append(key);
                    if (j++ < data[i].keySet().size() - 1)
                        sb.append(",");
                }
                sb.append(") VALUES(");
                j = 0;
                for (String key : data[i].keySet()) {
                    if (data[i].get(key).getClass().getSimpleName().equalsIgnoreCase(String.class.getSimpleName()))
                        sb.append("'").append(data[i].get(key)).append("'");
                    else
                        sb.append(data[i].get(key));
                    if (j++ < data[i].keySet().size() - 1)
                        sb.append(",");
                }
                sb.append(")");
                statement.addBatch(sb.toString());
            }
            int[] db_ids = statement.executeBatch();
            for (int q = 0; q < db_ids.length; q++)
                ids.add(Integer.toString(db_ids[q]) + " rows added.");
            connection.commit();

        } catch (BatchUpdateException e) {
            e.printStackTrace();
            connection.rollback();
        } finally {
            connection.setAutoCommit(autocommit);
            connection.close();
        }

        return ids;

    }

    public StreamingOutput retrieve(final DatasourceBean ds,
                                    final String query,
                                    final String collection,
                                    final String outputType) {

        return new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                /* Fetch parameters from user request. */
                RetrieveSQLBean b = g.fromJson(query, RetrieveSQLBean.class);

                /* Initiate the JDBC iterable. */
                JDBCIterable it = new JDBCIterable();
                List<String> headers = new ArrayList<String>();

                try {

                    /* Query DB. */
                    it.query(ds, b.getQuery());

                    /* Get column names. */
                    headers = it.getColumnNames();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                /* Write the result of the query... */
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

                /* ...as an array of objects... */
                if (outputType.equalsIgnoreCase("object")) {

                    writer.write("[");
                    while (it.hasNext()) {
                        String s = it.nextJSON();
                        writer.write(s);
                        if (it.hasNext())
                            writer.write(",");
                    }
                    writer.write("]");

                }

                /* ...or as an array of arrays. */
                if (outputType.equalsIgnoreCase("array")) {
                    writer.write("[");
                    while(it.hasNext()) {
                        writer.write(g.toJson(it.next()));
                        if (it.hasNext())
                            writer.write(",");
                    }
                    writer.write("]");
                }

                /* Convert and write the output on the stream. */
                writer.flush();

            }

        };

    }

    public List<String> delete(DatasourceBean ds, String query, String collection) throws Exception {

        /* Prepare the output. */
        List<String> ids = new ArrayList<String>();

        /* Fetch parameters from user request. */
        RetrieveSQLBean b = g.fromJson(query, RetrieveSQLBean.class);

        /* Get connection. */
        Connection connection = null;
        switch (ds.getDriver()) {
            case POSTGRESQL:
                Class.forName("org.postgresql.Driver");
                break;
            case SQLSERVER2000:
                SQLServerDriver.class.newInstance();
                break;
        }
        connection = DriverManager.getConnection(ds.getUrl(), ds.getUsername(), ds.getPassword());
        Statement statement = connection.createStatement();
        ids.add(Integer.toString(statement.executeUpdate(b.getQuery())));

        /* Return the number of deleted rows. */
        return ids;

    }

}
