package org.fao.fenix.wds.core.fenix;

import com.google.gson.Gson;
import com.orientechnologies.orient.core.db.OPartitionedDatabasePool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import org.fao.fenix.wds.core.bean.DatasourceBean;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * */
public class WDSUtilsOrientDB implements WDSUtils {

    private Gson g = new Gson();

    public List<String> create(DatasourceBean ds, String documents, String collection) throws Exception {

        /* Prepare the output. */
        List<String> ids = new ArrayList<String>();
        List<ODocument> docs = new ArrayList<ODocument>();

        /* Initiate variables. */
        String url = "remote:" + ds.getUrl() + '/' + ds.getDbName();
        OPartitionedDatabasePool pool = new OPartitionedDatabasePool(url, ds.getUsername(), ds.getPassword(), 100);
        ODatabaseDocumentTx connection = pool.acquire();

        /* Insert documents. */
        Map<String,Object>[] data = g.fromJson(documents, Map[].class);
        try {
            connection.begin();
            for (Map<String, Object> stringObjectMap : data) {
                ODocument doc = new ODocument(collection);
                doc.fromMap(stringObjectMap);
                doc.save();
                docs.add(doc);
            }
            connection.commit();

            for (ODocument odoc : docs)
                ids.add(odoc.getIdentity().toString());

        } catch(Exception e) {
            connection.rollback();
        } finally {
            connection.close();
        }

        /* Return ID's. */
        return ids;

    }

    public StreamingOutput retrieve(final DatasourceBean ds,
                                    final String query,
                                    final String collection,
                                    final String outputType) {

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

}