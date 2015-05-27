package org.fao.fenix.wds.web.rest.crud;

import org.fao.fenix.wds.core.bean.DatasourceBean;
import org.fao.fenix.wds.core.datasource.DatasourcePool;
import org.fao.fenix.wds.core.crud.CRUDMongoDB;
import org.fao.fenix.wds.core.crud.CRUDOrientDB;
import org.fao.fenix.wds.core.crud.CRUDSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 */
@Component
@Path("/crud")
public class CRUD {

    @Autowired
    private DatasourcePool datasourcePool;

    @Autowired
    private CRUDSQL crudSQL;

    @Autowired
    private CRUDOrientDB crudOrientDB;

    @Autowired
    private CRUDMongoDB crudMongoDB;

    @POST
    @Produces("application/json")
    public Response create(@FormParam("datasource") String datasource,
                           @FormParam("payload") final String payload,
                           @FormParam("collection") final String collection,
                           @DefaultValue("object") @FormParam("outputType") final String outputType) throws Exception {

        /* Create datasource bean. */
        final DatasourceBean ds = datasourcePool.getDatasource(datasource);

        /* Check permissions. */
        if (ds.isCreate()) {

            /* ID of the new resource. */
            List<String> ids = null;

            /* Handle the request according to DB type. */
            switch (ds.getDriver()) {

                case MONGODB:
                    ids = crudMongoDB.create(ds, payload, collection);
                    break;

                case ORIENTDB:
                    ids = crudOrientDB.create(ds, payload, collection);
                    break;

                default:
                    ids = crudSQL.create(ds, payload, collection);
                    break;

            }

            /* Create the output. */
            String out = "[";
            if (outputType.equalsIgnoreCase("object")) {
                for (int i = 0 ; i < ids.size() ; i++) {
                    out += "{\"id\": \"" + ids.get(i) + "\"}";
                    if (i < ids.size() - 1)
                        out += ",";
                }
            } else {
                for (int i = 0 ; i < ids.size() ; i++) {
                    out += "\"" + ids.get(i) + "\"";
                    if (i < ids.size() - 1)
                        out += ",";
                }
            }
            out += "]";

            /* Stream result */
            return Response.status(200).entity(out).build();

        }

        /* Return message error otherwise. */
        else {
            throw new Exception("This datasource has no CREATE privilege.");
        }

    }

    @GET
    @Produces("application/json")
    public Response retrieve(@QueryParam("datasource") String datasource,
                             @QueryParam("payload") final String payload,
                             @QueryParam("collection") final String collection,
                             @DefaultValue("object") @QueryParam("outputType") final String outputType) throws Exception {



        /* Create datasource bean. */
        final DatasourceBean ds = datasourcePool.getDatasource(datasource);

        /* Check permissions. */
        if (ds.isRetrieve()) {

            /* Output stream. */
            StreamingOutput stream = null;

            /* Handle the request according to DB type. */
            switch (ds.getDriver()) {

                case MONGODB:
                    stream = crudMongoDB.retrieve(ds, payload, collection, outputType);
                    break;

                case ORIENTDB:
                    stream = crudOrientDB.retrieve(ds, payload, collection, outputType);
                    break;

                default:
                    stream = crudSQL.retrieve(ds, payload, collection, outputType);
                    break;

            }

            /* Stream result */
            return Response.status(200).entity(stream).build();

        }

        /* Return message error otherwise. */
        else {
            throw new Exception("This datasource has no RETRIEVE privilege.");
        }

    }

    @PUT
    @Produces("application/json")
    public Response update(@FormParam("datasource") String datasource,
                           @FormParam("payload") final String payload,
                           @FormParam("collection") final String collection,
                           @DefaultValue("object") @FormParam("outputType") final String outputType) throws Exception {

        /* Create datasource bean. */
        final DatasourceBean ds = datasourcePool.getDatasource(datasource);

        /* Check permissions. */
        if (ds.isCreate()) {

            /* Number of deleted rows. */
            List<String> deletedRows = null;

            /* Handle the request according to DB type. */
            switch (ds.getDriver()) {

                case MONGODB:
                    deletedRows = crudMongoDB.update(ds, payload, collection);
                    break;

                case ORIENTDB:
                    deletedRows = crudOrientDB.update(ds, payload, collection);
                    break;

                default:
                    deletedRows = crudSQL.update(ds, payload, collection);
                    break;

            }

            /* Create the output. */
            String out = "[";
            if (outputType.equalsIgnoreCase("object")) {
                for (int i = 0 ; i < deletedRows.size() ; i++) {
                    out += "{\"updated_rows\": \"" + deletedRows.get(i) + "\"}";
                    if (i < deletedRows.size() - 1)
                        out += ",";
                }
            } else {
                for (int i = 0 ; i < deletedRows.size() ; i++) {
                    out += "\"" + deletedRows.get(i) + "\"";
                    if (i < deletedRows.size() - 1)
                        out += ",";
                }
            }
            out += "]";

            /* Stream result */
            return Response.status(200).entity(out).build();

        }

        /* Return message error otherwise. */
        else {
            throw new Exception("This datasource has no UPDATE privilege.");
        }

    }

    @DELETE
    @Produces("application/json")
    public Response delete(@FormParam("datasource") String datasource,
                           @FormParam("payload") final String payload,
                           @FormParam("collection") final String collection,
                           @DefaultValue("object") @FormParam("outputType") final String outputType) throws Exception {

        /* Create datasource bean. */
        final DatasourceBean ds = datasourcePool.getDatasource(datasource);

        /* Check permissions. */
        if (ds.isCreate()) {

            /* Number of deleted rows. */
            List<String> deletedRows = null;

            /* Handle the request according to DB type. */
            switch (ds.getDriver()) {

                case MONGODB:
                    deletedRows = crudMongoDB.delete(ds, payload, collection);
                    break;

                case ORIENTDB:
                    deletedRows = crudOrientDB.delete(ds, payload, collection);
                    break;

                default:
                    deletedRows = crudSQL.delete(ds, payload, collection);
                    break;

            }

            /* Create the output. */
            String out = "[";
            if (outputType.equalsIgnoreCase("object")) {
                for (int i = 0 ; i < deletedRows.size() ; i++) {
                    out += "{\"deleted_rows\": \"" + deletedRows.get(i) + "\"}";
                    if (i < deletedRows.size() - 1)
                        out += ",";
                }
            } else {
                for (int i = 0 ; i < deletedRows.size() ; i++) {
                    out += "\"" + deletedRows.get(i) + "\"";
                    if (i < deletedRows.size() - 1)
                        out += ",";
                }
            }
            out += "]";

            /* Stream result */
            return Response.status(200).entity(out).build();

        }

        /* Return message error otherwise. */
        else {
            throw new Exception("This datasource has no DELETE privilege.");
        }

    }

}