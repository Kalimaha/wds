package org.fao.fenix.wds.web.rest.fenix;

import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.util.JSON;
import com.orientechnologies.orient.core.db.OPartitionedDatabasePool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import org.fao.fenix.wds.core.bean.DatasourceBean;
import org.fao.fenix.wds.core.jdbc.JDBCIterable;
import org.fao.fenix.wds.core.jdbc.MongoDBConnectionManager;
import org.w3c.dom.Document;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;
import java.sql.Connection;
import java.sql.Statement;
import java.util.*;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * */
public class WDSUtils {

    public static Gson g = new Gson();

    public static StreamingOutput sqlStreamingOutputObject(final DatasourceBean ds, final String query) {

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

                /* Write the result of the query. */
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));
                writer.write("[");
                while (it.hasNext()) {
                    String s = it.nextJSON();
                    writer.write(s);
                    if (it.hasNext())
                        writer.write(",");
                }
                writer.write("]");

                /* Convert and write the output on the stream. */
                writer.flush();

            }

        };
    }

    public static List<String> sqlInsert(final DatasourceBean ds, final String documents, final String collection) throws Exception {

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

    public static StreamingOutput sqlStreamingOutputArray(final DatasourceBean ds, final String query) {

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
                DBObject dbobj;
                DBCursor cursor;
                try {
                    dbobj = (DBObject) JSON.parse(query);
                    cursor = dbCollection.find(dbobj);
                } catch (Exception e) {
                    cursor = dbCollection.find();
                }


                /* Compute result. */
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

                try {
                    writer.write("[");
                    int count = 0;
                    while(cursor.hasNext()) {
                        writer.write(cursor.next().toString());
                        if (count < cursor.size() - 1)
                            writer.write(",");
                        count++;
                    }
                } finally {
                    cursor.close();
                    writer.write("]");
                }

                /* Convert and write the output on the stream. */
                writer.flush();

            }

        };

    }

    public static List<String> mongoInsert(final DatasourceBean ds, final String documents, final String collection) throws Exception {

        /* Insert the documents. */
        MongoDBConnectionManager mgr = MongoDBConnectionManager.getInstance();
        Mongo mongo = mgr.getMongo();
        DB db = mongo.getDB(ds.getDbName());
        DBCollection dbCollection = db.getCollection(collection);
        List<DBObject> dbobjs = (List<DBObject>) JSON.parse(documents);
        dbCollection.insert(dbobjs);

        /* Prepare the output. */
        List<String> ids = new ArrayList<String>();
        for (DBObject dbobj : dbobjs)
            ids.add(dbobj.get("_id").toString());

        /* Return the output. */
        return ids;

    }

    public static StreamingOutput orientStreamingOutput(final DatasourceBean ds, final String query) {

        return new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                /* Initiate variables. */
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));
                String url = "remote:" + ds.getUrl() + '/' + ds.getDbName();
                OPartitionedDatabasePool pool = new OPartitionedDatabasePool(url, ds.getUsername(), ds.getPassword(), 100);
                ODatabaseDocumentTx connection = pool.acquire();

                /* Compute result. */
                try {
                    writer.write("[");
                    List<ODocument> rawData = connection.query(new OSQLSynchQuery(query));
                    for (int i = 0 ; i < rawData.size() ; i++) {
                        ODocument document = rawData.get(i);
                        writer.write(document.toJSON());
                        if (i < rawData.size() - 1)
                            writer.write(",");
                    }
                } finally {
                    connection.close();
                    writer.write("]");
                }

                /* Convert and write the output on the stream. */
                writer.flush();

            }

        };

    }

    public static List<String> orientInsert(final DatasourceBean ds, final String documents, final String collection) throws Exception {

        /* Prepare the output. */
        List<String> ids = new ArrayList<String>();
        List<ODocument> docs = new ArrayList<ODocument>();

        /* Initiate variables. */
        String url = "remote:" + ds.getUrl() + '/' + ds.getDbName();
        OPartitionedDatabasePool pool = new OPartitionedDatabasePool(url, ds.getUsername(), ds.getPassword(), 100);
        ODatabaseDocumentTx connection = pool.acquire();

        Gson g = new Gson();
        Map<String,Object>[] data = g.fromJson(documents, Map[].class);
        try {
            connection.begin();
            for (int i = 0; i < data.length; i++) {
                Map<String, Object> stringObjectMap = data[i];
                for (String key : stringObjectMap.keySet())
                    System.out.println(key + ": " + stringObjectMap.get(key) + ", " + stringObjectMap.get(key).getClass().getSimpleName());
                ODocument odoc = new ODocument(collection);
                odoc.fromMap(stringObjectMap);
                System.out.println(odoc.getIdentity().toString());
                odoc.save();
                docs.add(odoc);
                System.out.println(odoc);
                System.out.println(odoc.getIdentity().toString());
            }
            connection.commit();

            /* TODO ids */

        } catch(Exception e) {
            connection.rollback();
        } finally {
            connection.close();
        }

//        ODocument odoc = new ODocument();
//        odoc.fromMap()

        return ids;

    }

}
