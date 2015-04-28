package org.fao.fenix.wds.web.rest.fenix;

import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.util.JSON;
import org.fao.fenix.wds.core.bean.DatasourceBean;
import org.fao.fenix.wds.core.jdbc.JDBCIterable;
import org.fao.fenix.wds.core.jdbc.MongoDBConnectionManager;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * */
public class WDSUtils {

    public static Gson g = new Gson();

    public static StreamingOutput sqlStreamingOutput(final DatasourceBean ds, final String query) {

        return new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                /* Initiate the JDBC iterable. */
                JDBCIterable it = new JDBCIterable();

                try {
                    it.query(ds, query);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                /* Compute result. */
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

                /* Write the result of the query. */
                writer.write("[");
                while(it.hasNext()) {
                    writer.write(g.toJson(it.next()));
                    if (it.hasNext())
                        writer.write(",");
                }
                writer.write("]");

                /* Convert and write the output on the stream. */
                writer.flush();

            }

        };
    }

    public static StreamingOutput mongoStreamingOutput(final DatasourceBean ds, final String query, final String collection) {

        return new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                /* Query MongoDB. */
                MongoDBConnectionManager mgr = MongoDBConnectionManager.getInstance();
                Mongo mongo = mgr.getMongo();
                DB db = mongo.getDB(ds.getDbName());
                DBCollection dbCollection = db.getCollection(collection);
                DBObject dbobj = (DBObject) JSON.parse(query);
                DBCursor cursor = dbCollection.find(dbobj);

                /* Compute result. */
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

                try {
                    while(cursor.hasNext()) {
                        writer.write(cursor.next().toString());
                    }
                } finally {
                    cursor.close();
                }

                /* Convert and write the output on the stream. */
                writer.flush();

            }

        };

    }

}
