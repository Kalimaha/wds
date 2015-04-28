package org.fao.fenix.wds.web.rest.fenix;

import com.google.gson.Gson;
import org.fao.fenix.wds.core.bean.DatasourceBean;
import org.fao.fenix.wds.core.datasource.DatasourcePool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

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
    public Response query(@FormParam("datasource") String datasource) {

        /* Utilities. */
        Gson g = new Gson();

        /* Create datasource bean. */
        DatasourceBean dsBean = datasourcePool.getDatasource(datasource);

        /* Stream result */
        return Response.status(200).entity(g.toJson(dsBean)).build();

    }

}