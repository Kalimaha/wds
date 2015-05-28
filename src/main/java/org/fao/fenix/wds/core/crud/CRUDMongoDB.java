package org.fao.fenix.wds.core.crud;

import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.util.JSON;
import org.fao.fenix.wds.core.bean.DatasourceBean;
import org.fao.fenix.wds.core.bean.crud.CreateMongoDBBean;
import org.fao.fenix.wds.core.bean.crud.RetrieveMongoDBBean;
import org.fao.fenix.wds.core.bean.crud.UpdateMongoDBBean;
import org.fao.fenix.wds.core.jdbc.MongoDBConnectionManager;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * */
public class CRUDMongoDB implements CRUD {

    private Gson g = new Gson();

    public List<String> create(DatasourceBean ds, String payload, String collection) throws Exception {

        /* Fetch parameters from user request. */
        CreateMongoDBBean b;
        try {
            b = g.fromJson(payload, CreateMongoDBBean.class);
        } catch (Exception e) {
            throw new Exception(ERROR_MESSAGE);
        }

        /* Insert the documents. */
        MongoDBConnectionManager mgr = MongoDBConnectionManager.getInstance();
        Mongo mongo = mgr.getMongo(ds.getUrl());
        DB db = mongo.getDB(ds.getDbName());
        DBCollection dbCollection = db.getCollection(collection);
        List<DBObject> dbobjs = (List<DBObject>) JSON.parse(g.toJson(b.getQuery()));
        dbCollection.insert(dbobjs);

        /* Prepare the output. */
        List<String> ids = new ArrayList<String>();
        for (DBObject dbobj : dbobjs)
            ids.add(dbobj.get("_id").toString());

        /* Return the output. */
        return ids;

    }

    public StreamingOutput retrieve(final DatasourceBean ds,
                                    final String query,
                                    final String collection,
                                    final String outputType)  throws Exception {

        /* Fetch parameters from user request. */
        final RetrieveMongoDBBean b;
        try {
            b = g.fromJson(query, RetrieveMongoDBBean.class);
        } catch (Exception e) {
            throw new Exception(ERROR_MESSAGE);
        }

        return new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                /* Query MongoDB. */
                MongoDBConnectionManager mgr = MongoDBConnectionManager.getInstance();
                Mongo mongo = mgr.getMongo(ds.getUrl());
                DB db = mongo.getDB(ds.getDbName());
                DBCollection dbCollection = db.getCollection(collection);
                DBObject dbobj_query;
                DBObject dbobj_filters;
                DBObject dbobj_sort;
                DBCursor cursor;
                try {
                    dbobj_query = (DBObject) JSON.parse(g.toJson(b.getQuery()));
                    dbobj_filters = (DBObject) JSON.parse(g.toJson(b.getFilters()));
                    dbobj_sort = (DBObject) JSON.parse(g.toJson(b.getSort()));
                    cursor = dbCollection.find(dbobj_query, dbobj_filters).sort(dbobj_sort).limit(b.getLimit());
                } catch (Exception e) {
                    cursor = dbCollection.find();
                }


                /* Compute result. */
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

                /* ...as an array of objects... */
                if (outputType.equalsIgnoreCase("object")) {
                    try {
                        writer.write("[");
                        int count = 0;
                        while (cursor.hasNext()) {
                            writer.write(cursor.next().toString());
                            if (count < cursor.size() - 1)
                                writer.write(",");
                            count++;
                        }
                    } finally {
                        cursor.close();
                        writer.write("]");
                    }
                }

                /* ...or as an array of arrays. */
                if (outputType.equalsIgnoreCase("array")) {
                    StringBuilder sb = new StringBuilder();
                    try {
                        sb.append("[");
                        int row = 0;
                        while (cursor.hasNext()) {
                            DBObject tmp = cursor.next();
                            int col = 0;
                            sb.append("[");
                            for (String key : tmp.keySet()) {
                                sb.append("\"").append(tmp.get(key).toString()).append("\"");
                                if (col++ < tmp.keySet().size() - 1)
                                    sb.append(",");
                            }
                            sb.append("]");
                            if (row++ < cursor.size() - 1)
                                sb.append(",");
                        }
                        sb.append("]");
                    } finally {
                        cursor.close();
                        writer.write(sb.toString());
                    }
                }

                /* Convert and write the output on the stream. */
                writer.flush();

            }

        };

    }

    public List<String> update(DatasourceBean ds, String payload, String collection) throws Exception {

        /* Prepare the output. */
        List<String> updatedDocuments = new ArrayList<String>();

        /* Fetch parameters from user request. */
        UpdateMongoDBBean b;
        try {
            b = g.fromJson(payload, UpdateMongoDBBean.class);
        } catch (Exception e) {
            throw new Exception(ERROR_MESSAGE);
        }

        /* Update the documents. */
        MongoDBConnectionManager mgr = MongoDBConnectionManager.getInstance();
        Mongo mongo = mgr.getMongo(ds.getUrl());
        DB db = mongo.getDB(ds.getDbName());
        DBCollection dbCollection = db.getCollection(collection);
        DBObject dbobj_query = dbobj_query = (DBObject) JSON.parse(g.toJson(b.getQuery()));
        DBObject dbobj_update = dbobj_update = (DBObject) JSON.parse(g.toJson(b.getUpdate()));
        updatedDocuments.add(Integer.toString(dbCollection.update(dbobj_query, dbobj_update, b.isUpsert(), b.isMulti()).getN()));

        /* Return the output. */
        return updatedDocuments;

    }

    public List<String> delete(DatasourceBean ds, String query, String collection) throws Exception {

        /* Initiate output. */
        List<String> deletedRows = new ArrayList<String>();

        /* Fetch parameters from user request. */
        RetrieveMongoDBBean b;
        try {
            b = g.fromJson(query, RetrieveMongoDBBean.class);
        } catch (Exception e) {
            throw new Exception(ERROR_MESSAGE);
        }

        /* Query MongoDB. */
        MongoDBConnectionManager mgr = MongoDBConnectionManager.getInstance();
        Mongo mongo = mgr.getMongo(ds.getUrl());
        DB db = mongo.getDB(ds.getDbName());
        DBCollection dbCollection = db.getCollection(collection);
        DBObject dbobj_query = (DBObject) JSON.parse(g.toJson(b.getQuery()));
        WriteResult result = dbCollection.remove(dbobj_query);
        deletedRows.add(Integer.toString(result.getN()));

        /* Return output. */
        return deletedRows;

    }

}
