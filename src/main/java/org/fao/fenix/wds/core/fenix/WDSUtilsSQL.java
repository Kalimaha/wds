package org.fao.fenix.wds.core.fenix;

import com.google.gson.Gson;
import org.fao.fenix.wds.core.bean.DatasourceBean;
import org.fao.fenix.wds.core.jdbc.JDBCIterable;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * */
public class WDSUtilsSQL implements WDSUtils {

    private Gson g = new Gson();

    public List<String> create(final DatasourceBean ds, final String documents, final String collection) throws Exception {

        /* Prepare the output. */
        List<String> ids = new ArrayList<String>();

        /* connection.setAutoCommit(false)*/
        Connection connection = null;
        boolean autocommit = connection.getAutoCommit();
        try {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            //for
            statement.addBatch("insert ...");
            //end for

            statement.executeBatch();
            connection.commit();
        } catch (Exception ex) {
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

                /* Initiate the JDBC iterable. */
                JDBCIterable it = new JDBCIterable();
                List<String> headers = new ArrayList<String>();

                try {

                    /* Query DB. */
                    it.query(ds, query);

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

}
