package org.fao.fenix.wds.web.rest.amis.policies;

import org.fao.fenix.wds.core.bean.DBBean;
import org.fao.fenix.wds.core.bean.SQLBean;
import org.fao.fenix.wds.core.constant.DATASOURCE;
import org.fao.fenix.wds.core.exception.WDSException;
import org.fao.fenix.wds.core.jdbc.JDBCConnector;
import org.fao.fenix.wds.core.sql.amis.policies.SQLBeansRepository;
import org.fao.fenix.wds.core.utils.Wrapper;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Component
@Path("/amis/policies/data")
public class AmisPoliciesDataRESTService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    //http://localhost:8080/wds/rest/amis/policies/data/summary
    @Path("/summary/{countrycodes}/{commoditycodes}/{policydomaincodes}/{measuretypecodes}/{policymeasurecodes}/{subpolicymeasurecodes}")
    public Response getSummary(
            @PathParam("countrycodes") String countrycodes,
            @PathParam("commoditycodes") String commoditycodes,
            @PathParam("policydomaincodes") String policydomaincodes,
            @PathParam("measuretypecodes") String measuretypecodes,
            @PathParam("policymeasurecodes") String policymeasurecodes,
            @PathParam("subpolicymeasurecodes") String subpolicymeasurecodes )
           {

        try {

            DATASOURCE ds = DATASOURCE.AMISPOLICIES;
            DBBean db = new DBBean(ds);
            SQLBean sql = SQLBeansRepository.getSummary("\"MasterTable\"", countrycodes, commoditycodes, policydomaincodes, measuretypecodes, policymeasurecodes, subpolicymeasurecodes);
//            System.out.println("getSummary sql: " + Bean2SQL.convert(sql));
            List<List<String>> table = JDBCConnector.query(db, sql, true);
//            System.out.println("getSummary table: " + table );
           // if ( !table.isEmpty() )
             //   table.remove(0);
            String json = Wrapper.wrapAsJSON(table).toString();

//			System.out.println("json: " + json );
            // wrap result
            Response.ResponseBuilder builder = Response.ok(json, MediaType.APPLICATION_JSON);
            builder.header("Access-Control-Allow-Origin", "*");
            builder.header("Access-Control-Max-Age", "3600");
            builder.header("Access-Control-Allow-Methods", "GET");
            builder.header("Access-Control-Allow-Headers", "X-Requested-With,Host,User-Agent,Accept,Accept-Language,Accept-Encoding,Accept-Charset,Keep-Alive,Connection,Referer,Origin");
            builder.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON+"; charset=utf-8");

            // return response
            return builder.build();

        } catch (WDSException e) {
            return Response.status(500).entity("Error in 'summary' service: " + e.getMessage()).build();
        } catch (ClassNotFoundException e) {
            return Response.status(500).entity("Error in 'summary' service: " + e.getMessage()).build();
        } catch (SQLException e) {
            return Response.status(500).entity("Error in 'summary' service: " + e.getMessage()).build();
        } catch (InstantiationException e) {
            return Response.status(500).entity("Error in 'summary' service: " + e.getMessage()).build();
        } catch (IllegalAccessException e) {
            return Response.status(500).entity("Error in 'summary' service: " + e.getMessage()).build();
        }

    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    //http://localhost:8080/wds/rest/amis/policies/data/summary/policyvalues
    @Path("/summary/policyvalues/{countrycodes}/{commoditycodes}/{policydomaincodes}/{measuretypecodes}/{policymeasurecodes}/{subpolicymeasurecodes}")
    public Response getPolicyValues(
            @PathParam("countrycodes") String countrycodes,
            @PathParam("commoditycodes") String commoditycodes,
            @PathParam("policydomaincodes") String policydomaincodes,
            @PathParam("measuretypecodes") String measuretypecodes,
            @PathParam("policymeasurecodes") String policymeasurecodes,
            @PathParam("subpolicymeasurecodes") String subpolicymeasurecodes )
    {

        try {

            DATASOURCE ds = DATASOURCE.AMISPOLICIES;
            DBBean db = new DBBean(ds);

            SQLBean sql = SQLBeansRepository.gePolicyValues("\"ValueTable\"", "\"MasterTable\"", countrycodes, commoditycodes, policydomaincodes, measuretypecodes, policymeasurecodes, subpolicymeasurecodes);
//            System.out.println("getPolicyValues sql: " + Bean2SQL.convert(sql));
            List<List<String>> table = JDBCConnector.query(db, sql, true);
//            System.out.println("getPolicyValues table: " + table );
           // if ( !table.isEmpty() )
             //   table.remove(0);
            String json = Wrapper.wrapAsJSON(table).toString();

//			System.out.println("json: " + json );
            // wrap result
            Response.ResponseBuilder builder = Response.ok(json, MediaType.APPLICATION_JSON);
            builder.header("Access-Control-Allow-Origin", "*");
            builder.header("Access-Control-Max-Age", "3600");
            builder.header("Access-Control-Allow-Methods", "GET");
            builder.header("Access-Control-Allow-Headers", "X-Requested-With,Host,User-Agent,Accept,Accept-Language,Accept-Encoding,Accept-Charset,Keep-Alive,Connection,Referer,Origin");
            builder.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON+"; charset=utf-8");

            // return response
            return builder.build();

        } catch (WDSException e) {
            return Response.status(500).entity("Error in 'getPolicyValues' service: " + e.getMessage()).build();
        } catch (ClassNotFoundException e) {
            return Response.status(500).entity("Error in 'getPolicyValues' service: " + e.getMessage()).build();
        } catch (SQLException e) {
            return Response.status(500).entity("Error in 'getPolicyValues' service: " + e.getMessage()).build();
        } catch (InstantiationException e) {
            return Response.status(500).entity("Error in 'getPolicyValues' service: " + e.getMessage()).build();
        } catch (IllegalAccessException e) {
            return Response.status(500).entity("Error in 'getPolicyValues' service: " + e.getMessage()).build();
        }

    }
}
