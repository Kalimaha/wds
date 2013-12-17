package org.fao.fenix.wds.web.rest.faostat;

import com.google.gson.Gson;
import org.fao.fenix.wds.core.bean.DatasourceBean;
import org.fao.fenix.wds.core.bean.faostat.FAOSTATProceduresBean;
import org.fao.fenix.wds.core.datasource.DatasourcePool;
import org.fao.fenix.wds.core.exception.WDSExceptionStreamWriter;
import org.fao.fenix.wds.core.faostat.FAOSTATProcedures;
import org.fao.fenix.wds.core.jdbc.JDBCIterable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 */
@Component
@Path("/procedures")
public class FAOSTATProceduresRESTService {

    @Autowired
    private DatasourcePool datasourcePool;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/listboxes/{datasource}/{domainCode}/{lang}")
    public Response getDomainListBoxes(@PathParam("datasource") final String datasource, @PathParam("domainCode") final String domainCode, @PathParam("lang") final String lang) {

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                FAOSTATProcedures fp = new FAOSTATProcedures();
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));
                Gson g = new Gson();

                // compute result
                JDBCIterable it = new JDBCIterable();

                try {

                    // Query DB
                    it = fp.getDomainListBoxes(datasource, domainCode, lang);

                } catch (IllegalAccessException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getDomainListBoxes' thrown an error: " + e.getMessage()));
                } catch (InstantiationException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getDomainListBoxes' thrown an error: " + e.getMessage()));
                } catch (SQLException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getDomainListBoxes' thrown an error: " + e.getMessage()));
                } catch (ClassNotFoundException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getDomainListBoxes' thrown an error: " + e.getMessage()));
                }

                // write the result of the query
                writer.write("[");
                while(it.hasNext()) {
                    writer.write(g.toJson(it.next()));
                    if (it.hasNext())
                        writer.write(",");
                }
                writer.write("]");

                // Convert and write the output on the stream
                writer.flush();

            }

        };

        // Wrap result
        Response.ResponseBuilder builder = Response.ok(stream);
        builder.header("Access-Control-Allow-Origin", "*");
        builder.header("Access-Control-Max-Age", "3600");
        builder.header("Access-Control-Allow-Methods", "POST");
        builder.header("Access-Control-Allow-Headers", "X-Requested-With, Host, User-Agent, Accept, Accept-Language, Accept-Encoding, Accept-Charset, Keep-Alive, Connection, Referer,Origin");

        // Stream result
        return builder.build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/countries/{datasource}/{domainCode}/{lang}")
    public Response getCountries(@PathParam("datasource") final String datasource, @PathParam("domainCode") final String domainCode, @PathParam("lang") final String lang) {

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                FAOSTATProcedures fp = new FAOSTATProcedures();
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));
                Gson g = new Gson();

                // compute result
                JDBCIterable it = new JDBCIterable();
                DatasourceBean dsBean = datasourcePool.getDatasource(datasource);

                try {

                    // Query DB
                    it = fp.getCountries(dsBean, domainCode, lang);

                } catch (IllegalAccessException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getCountries' thrown an error: " + e.getMessage()));
                } catch (InstantiationException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getCountries' thrown an error: " + e.getMessage()));
                } catch (SQLException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getCountries' thrown an error: " + e.getMessage()));
                } catch (ClassNotFoundException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getCountries' thrown an error: " + e.getMessage()));
                }

                // write the result of the query
                writer.write("[");
                while(it.hasNext()) {
                    writer.write(g.toJson(it.next()));
                    if (it.hasNext())
                        writer.write(",");
                }
                writer.write("]");

                // Convert and write the output on the stream
                writer.flush();

            }

        };

        // Wrap result
        Response.ResponseBuilder builder = Response.ok(stream);
        builder.header("Access-Control-Allow-Origin", "*");
        builder.header("Access-Control-Max-Age", "3600");
        builder.header("Access-Control-Allow-Methods", "POST");
        builder.header("Access-Control-Allow-Headers", "X-Requested-With, Host, User-Agent, Accept, Accept-Language, Accept-Encoding, Accept-Charset, Keep-Alive, Connection, Referer,Origin");

        // Stream result
        return builder.build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/regions/{datasource}/{domainCode}/{lang}")
    public Response getRegions(@PathParam("datasource") final String datasource, @PathParam("domainCode") final String domainCode, @PathParam("lang") final String lang) {

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                FAOSTATProcedures fp = new FAOSTATProcedures();
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));
                Gson g = new Gson();

                // compute result
                JDBCIterable it = new JDBCIterable();

                try {

                    // Query DB
                    it = fp.getRegions(datasource, domainCode, lang);

                } catch (IllegalAccessException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getRegions' thrown an error: " + e.getMessage()));
                } catch (InstantiationException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getRegions' thrown an error: " + e.getMessage()));
                } catch (SQLException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getRegions' thrown an error: " + e.getMessage()));
                } catch (ClassNotFoundException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getRegions' thrown an error: " + e.getMessage()));
                }

                // write the result of the query
                writer.write("[");
                while(it.hasNext()) {
                    writer.write(g.toJson(it.next()));
                    if (it.hasNext())
                        writer.write(",");
                }
                writer.write("]");

                // Convert and write the output on the stream
                writer.flush();

            }

        };

        // Wrap result
        Response.ResponseBuilder builder = Response.ok(stream);
        builder.header("Access-Control-Allow-Origin", "*");
        builder.header("Access-Control-Max-Age", "3600");
        builder.header("Access-Control-Allow-Methods", "POST");
        builder.header("Access-Control-Allow-Headers", "X-Requested-With, Host, User-Agent, Accept, Accept-Language, Accept-Encoding, Accept-Charset, Keep-Alive, Connection, Referer,Origin");

        // Stream result
        return builder.build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/areagrouparea/{datasource}/{domainCode}/{areaGroupCode}")
    public Response getAreaGroupArea(@PathParam("datasource") final String datasource, @PathParam("domainCode") final String domainCode, @PathParam("areaGroupCode") final String areaGroupCode) {

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                FAOSTATProcedures fp = new FAOSTATProcedures();
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));
                Gson g = new Gson();

                // compute result
                JDBCIterable it = new JDBCIterable();

                try {

                    // Query DB
//                    System.out.println(areaGroupCode);
//                    System.out.println(Integer.valueOf(areaGroupCode).intValue());
                    it = fp.getAreaGroupArea(datasource, domainCode, Integer.valueOf(areaGroupCode).intValue());

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getAreaGroupArea' thrown an error: " + e.getMessage()));
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getAreaGroupArea' thrown an error: " + e.getMessage()));
                } catch (SQLException e) {
                    e.printStackTrace();
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getAreaGroupArea' thrown an error: " + e.getMessage()));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getAreaGroupArea' thrown an error: " + e.getMessage()));
                }

                // write the result of the query
                writer.write("[");
                while(it.hasNext()) {
                    writer.write(g.toJson(it.next()));
                    if (it.hasNext())
                        writer.write(",");
                }
                writer.write("]");

                // Convert and write the output on the stream
                writer.flush();

            }

        };

        // Wrap result
        Response.ResponseBuilder builder = Response.ok(stream);
        builder.header("Access-Control-Allow-Origin", "*");
        builder.header("Access-Control-Max-Age", "3600");
        builder.header("Access-Control-Allow-Methods", "POST");
        builder.header("Access-Control-Allow-Headers", "X-Requested-With, Host, User-Agent, Accept, Accept-Language, Accept-Encoding, Accept-Charset, Keep-Alive, Connection, Referer,Origin");

        // Stream result
        return builder.build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/specialgroups/{datasource}/{domainCode}/{lang}")
    public Response getSpecialGroups(@PathParam("datasource") final String datasource, @PathParam("domainCode") final String domainCode, @PathParam("lang") final String lang) {

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                FAOSTATProcedures fp = new FAOSTATProcedures();
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));
                Gson g = new Gson();

                // compute result
                JDBCIterable it = new JDBCIterable();

                try {

                    // Query DB
                    it = fp.getSpecialGroups(datasource, domainCode, lang);

                } catch (IllegalAccessException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getSpecialGroups' thrown an error: " + e.getMessage()));
                } catch (InstantiationException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getSpecialGroups' thrown an error: " + e.getMessage()));
                } catch (SQLException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getSpecialGroups' thrown an error: " + e.getMessage()));
                } catch (ClassNotFoundException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getSpecialGroups' thrown an error: " + e.getMessage()));
                }

                // write the result of the query
                writer.write("[");
                while(it.hasNext()) {
                    writer.write(g.toJson(it.next()));
                    if (it.hasNext())
                        writer.write(",");
                }
                writer.write("]");

                // Convert and write the output on the stream
                writer.flush();

            }

        };

        // Wrap result
        Response.ResponseBuilder builder = Response.ok(stream);
        builder.header("Access-Control-Allow-Origin", "*");
        builder.header("Access-Control-Max-Age", "3600");
        builder.header("Access-Control-Allow-Methods", "POST");
        builder.header("Access-Control-Allow-Headers", "X-Requested-With, Host, User-Agent, Accept, Accept-Language, Accept-Encoding, Accept-Charset, Keep-Alive, Connection, Referer,Origin");

        // Stream result
        return builder.build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/items/{datasource}/{domainCode}/{lang}")
    public Response getItems(@PathParam("datasource") final String datasource, @PathParam("domainCode") final String domainCode, @PathParam("lang") final String lang) {

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                FAOSTATProcedures fp = new FAOSTATProcedures();
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));
                Gson g = new Gson();

                // compute result
                JDBCIterable it = new JDBCIterable();

                try {

                    // Query DB
                    it = fp.getItems(datasource, domainCode, lang);

                } catch (IllegalAccessException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getItems' thrown an error: " + e.getMessage()));
                } catch (InstantiationException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getItems' thrown an error: " + e.getMessage()));
                } catch (SQLException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getItems' thrown an error: " + e.getMessage()));
                } catch (ClassNotFoundException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getItems' thrown an error: " + e.getMessage()));
                }

                // write the result of the query
                writer.write("[");
                while(it.hasNext()) {
                    writer.write(g.toJson(it.next()));
                    if (it.hasNext())
                        writer.write(",");
                }
                writer.write("]");

                // Convert and write the output on the stream
                writer.flush();

            }

        };

        // Wrap result
        Response.ResponseBuilder builder = Response.ok(stream);
        builder.header("Access-Control-Allow-Origin", "*");
        builder.header("Access-Control-Max-Age", "3600");
        builder.header("Access-Control-Allow-Methods", "POST");
        builder.header("Access-Control-Allow-Headers", "X-Requested-With, Host, User-Agent, Accept, Accept-Language, Accept-Encoding, Accept-Charset, Keep-Alive, Connection, Referer,Origin");

        // Stream result
        return builder.build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/itemsaggregated/{datasource}/{domainCode}/{lang}")
    public Response getItemsAggregated(@PathParam("datasource") final String datasource, @PathParam("domainCode") final String domainCode, @PathParam("lang") final String lang) {

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                FAOSTATProcedures fp = new FAOSTATProcedures();
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));
                Gson g = new Gson();

                // compute result
                JDBCIterable it = new JDBCIterable();

                try {

                    // Query DB
                    it = fp.getItemsAggregated(datasource, domainCode, lang);

                } catch (IllegalAccessException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getItemsAggregated' thrown an error: " + e.getMessage()));
                } catch (InstantiationException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getItemsAggregated' thrown an error: " + e.getMessage()));
                } catch (SQLException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getItemsAggregated' thrown an error: " + e.getMessage()));
                } catch (ClassNotFoundException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getItemsAggregated' thrown an error: " + e.getMessage()));
                }

                // write the result of the query
                writer.write("[");
                while(it.hasNext()) {
                    writer.write(g.toJson(it.next()));
                    if (it.hasNext())
                        writer.write(",");
                }
                writer.write("]");

                // Convert and write the output on the stream
                writer.flush();

            }

        };

        // Wrap result
        Response.ResponseBuilder builder = Response.ok(stream);
        builder.header("Access-Control-Allow-Origin", "*");
        builder.header("Access-Control-Max-Age", "3600");
        builder.header("Access-Control-Allow-Methods", "POST");
        builder.header("Access-Control-Allow-Headers", "X-Requested-With, Host, User-Agent, Accept, Accept-Language, Accept-Encoding, Accept-Charset, Keep-Alive, Connection, Referer,Origin");

        // Stream result
        return builder.build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/elements/{datasource}/{domainCode}/{lang}")
    public Response getElements(@PathParam("datasource") final String datasource, @PathParam("domainCode") final String domainCode, @PathParam("lang") final String lang) {

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                FAOSTATProcedures fp = new FAOSTATProcedures();
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));
                Gson g = new Gson();

                // compute result
                JDBCIterable it = new JDBCIterable();

                try {

                    // Query DB
                    it = fp.getElements(datasource, domainCode, lang);

                } catch (IllegalAccessException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getElements' thrown an error: " + e.getMessage()));
                } catch (InstantiationException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getElements' thrown an error: " + e.getMessage()));
                } catch (SQLException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getElements' thrown an error: " + e.getMessage()));
                } catch (ClassNotFoundException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getElements' thrown an error: " + e.getMessage()));
                }

                // write the result of the query
                writer.write("[");
                while(it.hasNext()) {
                    writer.write(g.toJson(it.next()));
                    if (it.hasNext())
                        writer.write(",");
                }
                writer.write("]");

                // Convert and write the output on the stream
                writer.flush();

            }

        };

        // Wrap result
        Response.ResponseBuilder builder = Response.ok(stream);
        builder.header("Access-Control-Allow-Origin", "*");
        builder.header("Access-Control-Max-Age", "3600");
        builder.header("Access-Control-Allow-Methods", "POST");
        builder.header("Access-Control-Allow-Headers", "X-Requested-With, Host, User-Agent, Accept, Accept-Language, Accept-Encoding, Accept-Charset, Keep-Alive, Connection, Referer,Origin");

        // Stream result
        return builder.build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/years/{datasource}/{domainCode}")
    public Response getYears(@PathParam("datasource") final String datasource, @PathParam("domainCode") final String domainCode) {

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                FAOSTATProcedures fp = new FAOSTATProcedures();
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));
                Gson g = new Gson();

                // compute result
                JDBCIterable it = new JDBCIterable();

                try {

                    // Query DB
                    it = fp.getYears(datasource, domainCode);

                } catch (IllegalAccessException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getYears' thrown an error: " + e.getMessage()));
                } catch (InstantiationException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getYears' thrown an error: " + e.getMessage()));
                } catch (SQLException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getYears' thrown an error: " + e.getMessage()));
                } catch (ClassNotFoundException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getYears' thrown an error: " + e.getMessage()));
                }

                // write the result of the query
                writer.write("[");
                while(it.hasNext()) {
                    writer.write(g.toJson(it.next()));
                    if (it.hasNext())
                        writer.write(",");
                }
                writer.write("]");

                // Convert and write the output on the stream
                writer.flush();

            }

        };

        // Wrap result
        Response.ResponseBuilder builder = Response.ok(stream);
        builder.header("Access-Control-Allow-Origin", "*");
        builder.header("Access-Control-Max-Age", "3600");
        builder.header("Access-Control-Allow-Methods", "POST");
        builder.header("Access-Control-Allow-Headers", "X-Requested-With, Host, User-Agent, Accept, Accept-Language, Accept-Encoding, Accept-Charset, Keep-Alive, Connection, Referer,Origin");

        // Stream result
        return builder.build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/itemgroupitem/{datasource}/{domainCode}/{itemGroupCode}")
    public Response getItemGroupItem(@PathParam("datasource") final String datasource, @PathParam("domainCode") final String domainCode, @PathParam("itemGroupCode") final String itemGroupCode) {

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                FAOSTATProcedures fp = new FAOSTATProcedures();
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));
                Gson g = new Gson();

                // compute result
                JDBCIterable it = new JDBCIterable();

                try {

                    // Query DB
                    it = fp.getItemGroupItem(datasource, domainCode, Integer.valueOf(itemGroupCode).intValue());

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getItemGroupItem' thrown an error: " + e.getMessage()));
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getItemGroupItem' thrown an error: " + e.getMessage()));
                } catch (SQLException e) {
                    e.printStackTrace();
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getItemGroupItem' thrown an error: " + e.getMessage()));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getItemGroupItem' thrown an error: " + e.getMessage()));
                }

                // write the result of the query
                writer.write("[");
                while(it.hasNext()) {
                    writer.write(g.toJson(it.next()));
                    if (it.hasNext())
                        writer.write(",");
                }
                writer.write("]");

                // Convert and write the output on the stream
                writer.flush();

            }

        };

        // Wrap result
        Response.ResponseBuilder builder = Response.ok(stream);
        builder.header("Access-Control-Allow-Origin", "*");
        builder.header("Access-Control-Max-Age", "3600");
        builder.header("Access-Control-Allow-Methods", "POST");
        builder.header("Access-Control-Allow-Headers", "X-Requested-With, Host, User-Agent, Accept, Accept-Language, Accept-Encoding, Accept-Charset, Keep-Alive, Connection, Referer,Origin");

        // Stream result
        return builder.build();

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/data")
    public Response getData(@FormParam("payload") final String payload) {

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                FAOSTATProcedures fp = new FAOSTATProcedures();
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));
                Gson g = new Gson();

                // Convert from string to JSON
                FAOSTATProceduresBean b = g.fromJson(payload, FAOSTATProceduresBean.class);

                // compute result
                JDBCIterable it = new JDBCIterable();

                try {

                    it = fp.getData(b);

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getData' thrown an error: " + e.getMessage()));
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getData' thrown an error: " + e.getMessage()));
                } catch (SQLException e) {
                    e.printStackTrace();
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getData' thrown an error: " + e.getMessage()));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getData' thrown an error: " + e.getMessage()));
                }

                // write the result of the query
                writer.write("[");
                while(it.hasNext()) {
                    writer.write(g.toJson(it.next()));
                    if (it.hasNext())
                        writer.write(",");
                }
                writer.write("]");

                // Convert and write the output on the stream
                writer.flush();

            }

        };

        // Wrap result
        Response.ResponseBuilder builder = Response.ok(stream);
        builder.header("Access-Control-Allow-Origin", "*");
        builder.header("Access-Control-Max-Age", "3600");
        builder.header("Access-Control-Allow-Methods", "POST");
        builder.header("Access-Control-Allow-Headers", "X-Requested-With, Host, User-Agent, Accept, Accept-Language, Accept-Encoding, Accept-Charset, Keep-Alive, Connection, Referer,Origin");

        // Stream result
        return builder.build();

    }

    @POST
    @Path("/excel")
    public Response getExcel(@FormParam("payload") final String payload) {

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                FAOSTATProcedures fp = new FAOSTATProcedures();
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));
                Gson g = new Gson();

                // Convert from string to JSON
                System.out.println(payload);
                FAOSTATProceduresBean b = g.fromJson(payload, FAOSTATProceduresBean.class);
                System.out.println(b);

                // compute result
                JDBCIterable it = new JDBCIterable();

                try {

                    it = fp.getData(b);

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getExcel' thrown an error: " + e.getMessage()));
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getExcel' thrown an error: " + e.getMessage()));
                } catch (SQLException e) {
                    e.printStackTrace();
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getExcel' thrown an error: " + e.getMessage()));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getExcel' thrown an error: " + e.getMessage()));
                }

                // write the result of the query
                writer.write("<table>");
                while(it.hasNext()) {
                    List<String> l = it.next();
                    for (int i = 0; i < l.size(); i++) {
                        String s =  l.get(i);
                        System.out.print(s + ", ");
                    }
                    System.out.println();
                    writer.write("<tr>");
                    for (int i = 0; i < l.size(); i++) {
                        writer.write("<td>");
                        writer.write(l.get(i));
                        writer.write("</td>");
                    }
                    writer.write("</tr>");
                }
                writer.write("</table>");

                // Convert and write the output on the stream
                writer.flush();
                writer.close();

            }

        };

        // Wrap result
        Response.ResponseBuilder builder = Response.ok(stream);
        builder.header("Access-Control-Allow-Origin", "*");
        builder.header("Access-Control-Max-Age", "3600");
        builder.header("Access-Control-Allow-Methods", "POST");
        builder.header("Access-Control-Allow-Headers", "X-Requested-With, Host, User-Agent, Accept, Accept-Language, Accept-Encoding, Accept-Charset, Keep-Alive, Connection, Referer,Origin");
        builder.header("Content-Disposition", "attachment; filename=" + UUID.randomUUID().toString() + ".xls");

        // Stream result
        return builder.build();

    }

}