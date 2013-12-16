package org.fao.fenix.wds.web.rest.geo;

import com.google.gson.Gson;
import org.fao.fenix.wds.core.bean.DBBean;
import org.fao.fenix.wds.core.bean.SQLBean;
import org.fao.fenix.wds.core.constant.DATASOURCE;
import org.fao.fenix.wds.core.exception.WDSException;
import org.fao.fenix.wds.core.jdbc.JDBCConnector;
import org.springframework.stereotype.Component;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vortex
 * Date: 5/3/13
 * Time: 3:16 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
@Path("/geo")
public class GEORestService {


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/spatial")
    public Response get(
            @FormParam("datasource") final String datasource,
            @FormParam("select") final String select,
            @FormParam("layername") final String layername,
            @FormParam("srs") final String srs,
            @FormParam("geocolumn") final String geocolumn,
            @FormParam("polygon") final String polygon
            ) {

        /**
         * TODO: How to get
         * **/

//        System.out.println("[spatial]");

        try {

            // compute result

            DATASOURCE ds = DATASOURCE.valueOf(datasource.toUpperCase());

           // String s = "SELECT adm0_code, faost_n ";
           String s = "SELECT " + select.toLowerCase();

            // FROM
            //s += " FROM gaul0_faostat_3857 ";
            s += " FROM "+ layername.toLowerCase() +" ";


            // WHERES
            s += "WHERE ";
            // s += "ST_Contains( ST_Polygon(ST_GeomFromText('LINESTRING(156543.03392804097 6887893.4928338, 6809621.975869781 7083572.285243855, 6809621.975869781 1448023.0638343797, -958826.0828092508 1663269.7354854352, 156543.03392804097 6887893.4928338)'),3857),  geom) ";

            s += "ST_Contains( ST_Polygon(ST_GeomFromText('LINESTRING(" + polygon + ")')";

            if ( srs == null )
                s += ",3857), ";
            else
                s += "," + srs + "), ";



            if ( geocolumn == null )
                s += " geom) ";
            else
                s += geocolumn +") ";




            SQLBean sql = new SQLBean();
            sql.setQuery(s);

//            System.out.println("getPoints sql: " + sql );


            // SQLBean sql = SQLBeansRepository.getSQL("156543.03392804097 6887893.4928338, 6809621.975869781 7083572.285243855, 6809621.975869781 1448023.0638343797, -958826.0828092508 1663269.7354854352, 156543.03392804097 6887893.4928338");


            // alter the query to switch from LIMIT to TOP
            //if (datasource.toUpperCase().startsWith("FAOSTAT"))
            // sql.setQuery(replaceLimitWithTop(sql));

            // compute result
            DBBean db = new DBBean(ds);


            List<List<String>> table = JDBCConnector.query(db, sql, true);

            Gson g = new Gson();
            String json = g.toJson(table);

            // wrap result
            Response.ResponseBuilder builder = Response.ok(json);
            builder.header("Access-Control-Allow-Origin", "*");
            builder.header("Access-Control-Max-Age", "3600");
            builder.header("Access-Control-Allow-Methods", "GET");
            builder.header("Access-Control-Allow-Headers", "X-Requested-With,Host,User-Agent,Accept,Accept-Language,Accept-Encoding,Accept-Charset,Keep-Alive,Connection,Referer,Origin");
            builder.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON+"; charset=utf-8");


            // return response
            return builder.build();


        } catch (WDSException e) {
            return Response.status(500).entity("Error in 'getPoints' service: " + e.getMessage()).build();
        } catch (ClassNotFoundException e) {
            return Response.status(500).entity("Error in 'getPoints' service: " + e.getMessage()).build();
        } catch (SQLException e) {
            return Response.status(500).entity("Error in 'getPoints' service: " + e.getMessage()).build();
        } catch (InstantiationException e) {
            return Response.status(500).entity("Error in 'getPoints' service: " + e.getMessage()).build();
        } catch (IllegalAccessException e) {
            return Response.status(500).entity("Error in 'getPoints' service: " + e.getMessage()).build();
        }

    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/sq")
    public Response get(
            @FormParam("datasource") final String datasource,
            @FormParam("select") final String select,
            @FormParam("from") final String from,
            @FormParam("where") final String where
    ) {

        /**
         * TODO: How to get
         * **/

//        System.out.println("[spatial]");

        try {

            // compute result

            DATASOURCE ds = DATASOURCE.valueOf(datasource.toUpperCase());

            // String s = "SELECT adm0_code, faost_n ";
            String s = "SELECT " + select;

            // FROM
            //s += " FROM gaul0_faostat_3857 ";
            s += " FROM "+ from +" ";


            // WHERES
            s += "WHERE " + where;

            SQLBean sql = new SQLBean();
            sql.setQuery(s);

//            System.out.println("getPoints sql: " + sql );

            // SQLBean sql = SQLBeansRepository.getSQL("156543.03392804097 6887893.4928338, 6809621.975869781 7083572.285243855, 6809621.975869781 1448023.0638343797, -958826.0828092508 1663269.7354854352, 156543.03392804097 6887893.4928338");

            // alter the query to switch from LIMIT to TOP
            //if (datasource.toUpperCase().startsWith("FAOSTAT"))
            // sql.setQuery(replaceLimitWithTop(sql));

            // compute result
            DBBean db = new DBBean(ds);


            List<List<String>> table = JDBCConnector.query(db, sql, true);

            Gson g = new Gson();
            String json = g.toJson(table);

            // wrap result
            Response.ResponseBuilder builder = Response.ok(json);
            builder.header("Access-Control-Allow-Origin", "*");
            builder.header("Access-Control-Max-Age", "3600");
            builder.header("Access-Control-Allow-Methods", "GET");
            builder.header("Access-Control-Allow-Headers", "X-Requested-With,Host,User-Agent,Accept,Accept-Language,Accept-Encoding,Accept-Charset,Keep-Alive,Connection,Referer,Origin");
            builder.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON+"; charset=utf-8");


            // return response
            return builder.build();


        } catch (WDSException e) {
            return Response.status(500).entity("Error in 'getPoints' service: " + e.getMessage()).build();
        } catch (ClassNotFoundException e) {
            return Response.status(500).entity("Error in 'getPoints' service: " + e.getMessage()).build();
        } catch (SQLException e) {
            return Response.status(500).entity("Error in 'getPoints' service: " + e.getMessage()).build();
        } catch (InstantiationException e) {
            return Response.status(500).entity("Error in 'getPoints' service: " + e.getMessage()).build();
        } catch (IllegalAccessException e) {
            return Response.status(500).entity("Error in 'getPoints' service: " + e.getMessage()).build();
        }

    }




}
