package org.fao.fenix.wds.core.crud;

import com.google.gson.Gson;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import org.fao.fenix.wds.core.bean.DatasourceBean;
import org.fao.fenix.wds.core.bean.crud.CreateSQLBean;
import org.fao.fenix.wds.core.bean.crud.RetrieveSQLBean;
import org.fao.fenix.wds.core.bean.crud.UpdateSQLBean;
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
public class CRUDSQL implements CRUD {

    private Gson g = new Gson();

    public List<String> create(final DatasourceBean ds, final String payload, final String collection) throws Exception {

        /* Prepare the output. */
        StringBuilder sb;
        List<String> addedRows = new ArrayList<String>();

        /* Fetch parameters from user request. */
        CreateSQLBean b;
        try {
            b = g.fromJson(payload, CreateSQLBean.class);
        } catch (Exception e) {
            throw new Exception(ERROR_MESSAGE);
        }

        /* Convert input. */
        Map<String, Object>[] data = b.getQuery();

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
                addedRows.add(Integer.toString(db_ids[q]) + " rows have been added.");
            connection.commit();

        } catch (BatchUpdateException e) {
            e.printStackTrace();
            connection.rollback();
        } finally {
            connection.setAutoCommit(autocommit);
            connection.close();
        }

        return addedRows;

    }

    public StreamingOutput retrieve(final DatasourceBean ds,
                                    final String query,
                                    final String collection,
                                    final String outputType) throws Exception {

        /* Fetch parameters from user request. */
        final RetrieveSQLBean b;
        try {
            b = g.fromJson(query, RetrieveSQLBean.class);
        } catch (Exception e) {
            throw new Exception(ERROR_MESSAGE);
        }

        return new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                /* Initiate the JDBC iterable. */
                JDBCIterable it = new JDBCIterable();

                try {

                    /* Query DB. */
                    it.query(ds, b.getQuery());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                /* Get column names. */
                List<String> headers = it.getColumnNames();

                /* Write the result of the query... */
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

                /* ...as an array of objects... */
                if (outputType.equalsIgnoreCase("object")) {

                    /* Initiate the writer. */
                    writer.write("[");

                    /* Write the headers. */
                    writer.write("{");
                    for (int z = 0 ; z < headers.size() ; z++) {
                        writer.write("\"header" + z + "\": \"" + headers.get(z) + "\"");
                        if (z < headers.size() - 1)
                            writer.write(",");
                    }
                    writer.write("},");

                    /* Write contents. */
                    while (it.hasNext()) {
                        String s = it.nextJSON();
                        writer.write(s);
                        if (it.hasNext())
                            writer.write(",");
                    }

                    /* Close the writer. */
                    writer.write("]");

                }

                /* ...or as an array of arrays. */
                if (outputType.equalsIgnoreCase("array")) {

                    /* Write output. */
                    writer.write(createArrayOutput(it));

                }

                /* Convert and write the output on the stream. */
                writer.flush();

            }

        };

    }

    public String createObjectOutput(JDBCIterable it) {
        StringBuilder sb = new StringBuilder();
        List<String> headers = it.getColumnNames();
        sb.append("[");
        sb.append("{");
        for (int z = 0 ; z < headers.size() ; z++) {
            sb.append("\"header").append(z).append("\": \"").append(headers.get(z)).append("\"");
            if (z < headers.size() - 1)
                sb.append(",");
        }
        sb.append("}");
        if (it.isHasNext()) {
            sb.append(",");
            while (it.hasNext()) {
                String s = it.nextJSON();
                sb.append(s);
                if (it.hasNext())
                    sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public String createArrayOutput(JDBCIterable it) {
        StringBuilder sb = new StringBuilder();
        List<String> headers = it.getColumnNames();
        sb.append("[");
        sb.append("[");
        for (int z = 0 ; z < headers.size() ; z++) {
            sb.append("\"").append(headers.get(z)).append("\"");
            if (z < headers.size() - 1)
                sb.append(",");
        }
        sb.append("]");
        if (it.isHasNext()) {
            sb.append(",");
            while (it.hasNext()) {
                String s = it.nextArray();
                sb.append(s);
                if (it.hasNext())
                    sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public List<String> update(DatasourceBean ds, String payload, String collection) throws Exception {

        /* Prepare the output. */
        List<String> out = new ArrayList<String>();
        int counter = 0;

        /* Fix collection name, if needed. */
        if (!collection.startsWith("\"") || !collection.endsWith("\""))
            collection = "\"" + collection + "\"";

        /* Fetch parameters from user request. */
        UpdateSQLBean b;
        try {
            b = g.fromJson(payload, UpdateSQLBean.class);
        } catch (Exception e) {
            throw new Exception(ERROR_MESSAGE);
        }

       /* Initiate the JDBC iterable. */
        JDBCIterable it = new JDBCIterable();

        /* Query DB. */
        it.query(ds, b.getQuery());

        /* Get column names. */
        List<String> headers = it.getColumnNames();

        /* Get column types. */
        List<String> types = it.getColumnTypes();

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

        try {

            while (it.hasNext()) {

                /* Row to be updated. */
                List<String> l = it.next();

                /* UPDATE statement. */
                StringBuilder sb = new StringBuilder("UPDATE ").append(collection).append(" SET ");
                for (String key : b.getUpdate().keySet()) {
                    sb.append(key).append(" = ");
                    if (b.getUpdate().get(key).getClass().getSimpleName().equalsIgnoreCase(String.class.getSimpleName()))
                        sb.append("'");
                    sb.append(b.getUpdate().get(key));
                    if (b.getUpdate().get(key).getClass().getSimpleName().equalsIgnoreCase(String.class.getSimpleName()))
                        sb.append("'");
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);

                /* WHERE condition. */
                sb.append(" WHERE ");
                for (int i = 0; i < l.size(); i++) {
                    sb.append(headers.get(i)).append(" = ");
                    if (types.get(i).endsWith("String"))
                        sb.append("'");
                    sb.append(l.get(i));
                    if (types.get(i).endsWith("String"))
                        sb.append("'");
                    if (i < l.size() - 1)
                        sb.append(" AND ");
                }

                /* Update DB. */
                counter += statement.executeUpdate(sb.toString());

            }

        } finally {
            statement.close();
            connection.close();
        }

        /* Return the number of deleted rows. */
        out.add(Integer.toString(counter));
        return out;

    }

    public List<String> delete(DatasourceBean ds, String query, String collection) throws Exception {

        /* Prepare the output. */
        List<String> ids = new ArrayList<String>();

        /* Fetch parameters from user request. */
        RetrieveSQLBean b;
        try {
            b = g.fromJson(query, RetrieveSQLBean.class);
        } catch (Exception e) {
            throw new Exception(ERROR_MESSAGE);
        }

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

        /* Delete record(s). */
        try {
            ids.add(Integer.toString(statement.executeUpdate(b.getQuery())));
        } finally {
            statement.close();
            connection.close();
        }


        /* Return the number of deleted rows. */
        return ids;

    }

}
