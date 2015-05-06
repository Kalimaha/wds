package org.fao.fenix.wds.web.rest.fenix;

import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.util.JSON;
import com.orientechnologies.orient.core.db.OPartitionedDatabasePool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import org.fao.fenix.wds.core.bean.DatasourceBean;
import org.fao.fenix.wds.core.datasource.DatasourcePool;
import org.fao.fenix.wds.core.jdbc.JDBCIterable;
import org.fao.fenix.wds.core.jdbc.MongoDBConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * */
@Component
@Path("/fenix")
public class WDS {

    @Autowired
    private DatasourcePool datasourcePool;

    @POST
    @Path("/query")
    public Response query(@FormParam("datasource") String datasource,
                          @FormParam("query") final String query,
                          @FormParam("collection") final String collection,
                          @DefaultValue("object") @FormParam("outputType") final String outputType) throws Exception {

        /* Output stream. */
        StreamingOutput stream = null;

        /* Create datasource bean. */
        final DatasourceBean ds = datasourcePool.getDatasource(datasource);

        /* Handle the request according to DB type. */
        switch (ds.getDriver()) {

            case MONGODB:
                stream = WDSUtils.mongoStreamingOutput(ds, query, collection);
                break;

            case ORIENTDB:
                stream = WDSUtils.orientStreamingOutput(ds, query);
                break;

            default:
                if (outputType.equalsIgnoreCase("object"))
                    stream = WDSUtils.sqlStreamingOutputObject(ds, query);
                else if (outputType.equalsIgnoreCase("array"))
                    stream = WDSUtils.sqlStreamingOutputArray(ds, query);
                break;

        }

        /* Stream result */
        return Response.status(200).entity(stream).build();

    }

    @POST
    @Path("/insert")
    public Response insert() {

        /* Stream result */
        return Response.status(200).entity("Insert complete.").build();

    }

}