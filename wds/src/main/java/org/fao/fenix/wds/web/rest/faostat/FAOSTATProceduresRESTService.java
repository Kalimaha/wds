package org.fao.fenix.wds.web.rest.faostat;

import com.google.gson.Gson;
import org.fao.fenix.wds.core.bean.DatasourceBean;
import org.fao.fenix.wds.core.bean.faostat.FAOSTATCPINotesBean;
import org.fao.fenix.wds.core.bean.faostat.FAOSTATProceduresBean;
import org.fao.fenix.wds.core.datasource.DatasourcePool;
import org.fao.fenix.wds.core.faostat.FAOSTATProcedures;
import org.fao.fenix.wds.core.jdbc.JDBCIterable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;
import java.util.List;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 */
@Component
@Path("/procedures")
public class FAOSTATProceduresRESTService {

    @Autowired
    private DatasourcePool datasourcePool;

    private FAOSTATProcedures fp = new FAOSTATProcedures();

    private final Gson g = new Gson();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/oda/donors/{datasource}/{lang}")
    public Response getODADonors(@PathParam("datasource") String datasource, @PathParam("lang") String lang) throws Exception {

        // compute result
        DatasourceBean dsBean = datasourcePool.getDatasource(datasource);
        final JDBCIterable it = fp.getODADonors(dsBean, lang);

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

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

        // Stream result
        return Response.status(200).entity(stream).build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/cpimetadata/{datasource}/{area}/{lang}")
    public Response getCPIMetadata(@PathParam("datasource") String datasource, @PathParam("area") String area, @PathParam("lang") String lang) throws Exception {

        // compute result
        DatasourceBean dsBean = datasourcePool.getDatasource(datasource);
        final JDBCIterable it = fp.getCPIMetadata(dsBean, area, lang);

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

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

        // Stream result
        return Response.status(200).entity(stream).build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/cpimetadataareas/{datasource}/{lang}")
    public Response getCPIMetadataAreas(@PathParam("datasource") String datasource, @PathParam("lang") String lang) throws Exception {

        // compute result
        DatasourceBean dsBean = datasourcePool.getDatasource(datasource);
        final JDBCIterable it = fp.getCPIMetadataAreas(dsBean, lang);

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

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

        // Stream result
        return Response.status(200).entity(stream).build();

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/cpinotes")
    public Response getCPINotes(@FormParam("payload") String payload) throws Exception {

        // Convert inputs
        FAOSTATCPINotesBean p = g.fromJson(payload, FAOSTATCPINotesBean.class);

        // compute result
        DatasourceBean dsBean = datasourcePool.getDatasource(p.getDatasource());
        final JDBCIterable it = fp.getCPINotes(dsBean, p.getAreaCodes(), p.getYearCodes(), p.getItemCodes(), p.getLang());

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

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

        // Stream result
        return Response.status(200).entity(stream).build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/listboxes/{datasource}/{domainCode}/{lang}")
    public Response getDomainListBoxes(@PathParam("datasource") String datasource,
                                       @PathParam("domainCode") String domainCode,
                                       @PathParam("lang") String lang) throws Exception {

        // compute result
        DatasourceBean dsBean = datasourcePool.getDatasource(datasource);
        final JDBCIterable it = fp.getDomainListBoxes(dsBean, domainCode, lang);

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

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

        // Stream result
        return Response.status(200).entity(stream).build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/countries/{datasource}/{domainCode}/{lang}")
    public Response getCountries(@PathParam("datasource") String datasource,
                                 @PathParam("domainCode") String domainCode,
                                 @PathParam("lang") String lang) throws Exception {

        // compute result
        DatasourceBean dsBean = datasourcePool.getDatasource(datasource);
        final JDBCIterable it = fp.getCountries(dsBean, domainCode, lang);

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

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

        // Stream result
        return Response.status(200).entity(stream).build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/regions/{datasource}/{domainCode}/{lang}")
    public Response getRegions(@PathParam("datasource") String datasource,
                               @PathParam("domainCode") String domainCode,
                               @PathParam("lang") String lang) throws Exception {

        // compute result
        DatasourceBean dsBean = datasourcePool.getDatasource(datasource);
        final JDBCIterable  it = fp.getRegions(dsBean, domainCode, lang);

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

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

        // Stream result
        return Response.status(200).entity(stream).build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/areagrouparea/{datasource}/{domainCode}/{areaGroupCode}")
    public Response getAreaGroupArea(@PathParam("datasource") String datasource,
                                     @PathParam("domainCode") String domainCode,
                                     @PathParam("areaGroupCode") String areaGroupCode) throws Exception {

        // compute result
        DatasourceBean dsBean = datasourcePool.getDatasource(datasource);
        final JDBCIterable it = fp.getAreaGroupArea(dsBean, domainCode, Integer.valueOf(areaGroupCode).intValue());

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

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

        // Stream result
        return Response.status(200).entity(stream).build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/specialgroups/{datasource}/{domainCode}/{lang}")
    public Response getSpecialGroups(@PathParam("datasource") String datasource,
                                     @PathParam("domainCode") String domainCode,
                                     @PathParam("lang") String lang) throws Exception {

        // compute result
        DatasourceBean dsBean = datasourcePool.getDatasource(datasource);
        final JDBCIterable it = fp.getSpecialGroups(dsBean, domainCode, lang);

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

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

        // Stream result
        return Response.status(200).entity(stream).build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/items/{datasource}/{domainCode}/{lang}")
    public Response getItems(@PathParam("datasource") String datasource,
                             @PathParam("domainCode") String domainCode,
                             @PathParam("lang") String lang) throws Exception {

        // compute result
        DatasourceBean dsBean = datasourcePool.getDatasource(datasource);
        final JDBCIterable it = fp.getItems(dsBean, domainCode, lang);

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

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

        // Stream result
        return Response.status(200).entity(stream).build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/itemsaggregated/{datasource}/{domainCode}/{lang}")
    public Response getItemsAggregated(@PathParam("datasource") String datasource,
                                       @PathParam("domainCode") String domainCode,
                                       @PathParam("lang") String lang) throws Exception {

        // compute result
        DatasourceBean dsBean = datasourcePool.getDatasource(datasource);
        final JDBCIterable it = fp.getItemsAggregated(dsBean, domainCode, lang);

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

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

        // Stream result
        return Response.status(200).entity(stream).build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/elements/{datasource}/{domainCode}/{lang}")
    public Response getElements(@PathParam("datasource") String datasource,
                                @PathParam("domainCode") String domainCode,
                                @PathParam("lang") String lang) throws Exception {

        // compute result
        DatasourceBean dsBean = datasourcePool.getDatasource(datasource);
        final JDBCIterable it = fp.getElements(dsBean, domainCode, lang);

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

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

        // Stream result
        return Response.status(200).entity(stream).build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/years/{datasource}/{domainCode}")
    public Response getYears(@PathParam("datasource") String datasource,
                             @PathParam("domainCode") String domainCode) throws Exception {

        // compute result
        DatasourceBean dsBean = datasourcePool.getDatasource(datasource);
        final JDBCIterable it = fp.getYears(dsBean, domainCode);

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

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

        // Stream result
        return Response.status(200).entity(stream).build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/itemgroupitem/{datasource}/{domainCode}/{itemGroupCode}")
    public Response getItemGroupItem(@PathParam("datasource") String datasource,
                                     @PathParam("domainCode") String domainCode,
                                     @PathParam("itemGroupCode") String itemGroupCode) throws Exception {

        // compute result
        DatasourceBean dsBean = datasourcePool.getDatasource(datasource);
        final JDBCIterable it = fp.getItemGroupItem(dsBean, domainCode, Integer.valueOf(itemGroupCode).intValue());

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

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

        // Stream result
        return Response.status(200).entity(stream).build();

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/data")
    public Response getData(@FormParam("payload") String payload) throws Exception {

        // compute result
        FAOSTATProceduresBean b = g.fromJson(payload, FAOSTATProceduresBean.class);
        DatasourceBean dsBean = datasourcePool.getDatasource(b.getDatasource());
        final JDBCIterable it = fp.getData(dsBean, b);

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

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

        // Stream result
        return Response.status(200).entity(stream).build();

    }

    @POST
    @Path("/excel")
    public Response getExcel(@FormParam("payload") String payload) throws Exception {

        // compute result
        FAOSTATProceduresBean b = g.fromJson(payload, FAOSTATProceduresBean.class);
        DatasourceBean dsBean = datasourcePool.getDatasource(b.getDatasource());
        final JDBCIterable it = fp.getData(dsBean, b);

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

                // write the result of the query
                writer.write("<table>");
                while(it.hasNext()) {
                    List<String> l = it.next();
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

        // Stream result
        return Response.status(200).entity(stream).build();

    }

}