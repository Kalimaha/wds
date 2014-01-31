/**
 *
 * FENIX (Food security and Early warning Network and Information Exchange)
 *
 * Copyright (c) 2011, by FAO of UN under the EC-FAO Food Security
Information for Action Programme
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.fao.fenix.wds.web.rest.faostat;

import com.google.gson.Gson;
import org.fao.fenix.wds.core.bean.DBBean;
import org.fao.fenix.wds.core.bean.DatasourceBean;
import org.fao.fenix.wds.core.bean.SQLBean;
import org.fao.fenix.wds.core.constant.DATASOURCE;
import org.fao.fenix.wds.core.datasource.DatasourcePool;
import org.fao.fenix.wds.core.exception.WDSExceptionStreamWriter;
import org.fao.fenix.wds.core.jdbc.JDBCConnector;
import org.fao.fenix.wds.core.jdbc.JDBCIterable;
import org.fao.fenix.wds.core.sql.Bean2SQL;
import org.fao.fenix.wds.core.sql.faostat.SQLBeansRepository;
import org.fao.fenix.wds.core.utils.Wrapper;
import org.fao.fenix.wds.core.utils.WrapperConfigurations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** 
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
@Component
@Path("/mes")
public class FAOSTATMethodsAndStandards {
	
	@Autowired
	private Wrapper wrapper;

    @Autowired
    DatasourcePool datasourcePool;

    @GET
	@Path("/{code}/excel/{datasource}/{domainCode}/{lang}")
    @Produces("application/msexcel")
	public Response getExcel(@PathParam("code") final String code, @PathParam("datasource") final String datasource, @PathParam("domainCode") final String domainCode, @PathParam("lang") final String lang) {

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // initiate variable
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));
                DatasourceBean db = datasourcePool.getDatasource(datasource.toUpperCase());
                SQLBean sql = null;
                List<String> headers = new ArrayList<String>();

                // get SQL script
                if (code.equalsIgnoreCase("classifications")) {
                    sql = SQLBeansRepository.getClassifications(domainCode, lang);
                    headers.add("Item Code");
                    headers.add("Item Name");
                    headers.add("Definition");
                } else if (code.equalsIgnoreCase("abbreviations")) {
                    sql = SQLBeansRepository.getAbbreviations(domainCode, lang);
                    headers.add("Acronym");
                    headers.add("Definition");
                } else if (code.equalsIgnoreCase("glossary")) {
                    sql = SQLBeansRepository.getGlossary(domainCode, lang);
                    headers.add("Title");
                    headers.add("Definition");
                    headers.add("Sources");
                } else if (code.equalsIgnoreCase("methodology_list")) {
                    sql = SQLBeansRepository.getMethodologyList(domainCode, lang);
                    headers.add("Code");
                    headers.add("Methodology");
                } else if (code.equalsIgnoreCase("methodology")) {
                    sql = SQLBeansRepository.getMethodology(domainCode, lang);
                    headers.add("Note");
                    headers.add("Coverage");
                    headers.add("References");
                    headers.add("Collection");
                    headers.add("Estimation");
                } else if (code.equalsIgnoreCase("units")) {
                    sql = SQLBeansRepository.getUnits(domainCode, lang);
                    headers.add("Abbreviation");
                    headers.add("Title");
                }

                JDBCIterable it = new JDBCIterable();

                try {

                    // Query DB
                    it.query(db, Bean2SQL.convert(sql).toString());

                } catch (IllegalAccessException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getDomains' thrown an error: " + e.getMessage()));
                } catch (InstantiationException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getDomains' thrown an error: " + e.getMessage()));
                } catch (SQLException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getDomains' thrown an error: " + e.getMessage()));
                } catch (ClassNotFoundException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getDomains' thrown an error: " + e.getMessage()));
                } catch (Exception e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getDomains' thrown an error: " + e.getMessage()));
                }

                StringBuilder sb = new StringBuilder();
                sb.append("<table>");

                sb.append("<tr>");
                for (String s : headers)
                    sb.append("<td>").append(s).append("</td>");
                sb.append("</tr>");

                while(it.hasNext()) {
                    sb.append("<tr>");
                    List<String> row = it.next();
                    for (String s : row)
                        sb.append("<td>").append(s).append("</td>");
                    sb.append("</tr>");
                }
                sb.append("</table>");

                writer.write(sb.toString());
                writer.flush();

            }

        };

        // wrap result
        ResponseBuilder builder = Response.ok(stream);
        builder.header("Access-Control-Allow-Origin", "*");
        builder.header("Access-Control-Max-Age", "3600");
        builder.header("Access-Control-Allow-Methods", "POST");
        builder.header("Cache-Control", "public");
        builder.header("Pragma", "public");
        builder.header("Access-Control-Allow-Headers", "X-Requested-With, Host, User-Agent, Accept, Accept-Language, Accept-Encoding, Accept-Charset, Keep-Alive, Connection, Referer,Origin");
        builder.header("Content-Disposition", "attachment; filename=" + UUID.randomUUID().toString() +".xls");

        return builder.build();
		
	}
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("/{code}/html/{datasource}/{domainCode}/{lang}")
	public Response getHTML(@PathParam("code") String code, @PathParam("datasource") String datasource, @PathParam("domainCode") String domainCode, @PathParam("lang") String lang) {
		
		try {
			
			// logging
			long t0 = System.currentTimeMillis();
			String id = UUID.randomUUID().toString();
//			System.out.println("[START][" + id + "] - FAOSTATMethodsAndStandards.getHTML");
			
			// create HTML
			List<List<String>> table = createTable(code, datasource, domainCode, lang);
			WrapperConfigurations wc = new WrapperConfigurations();
			wc.setCssName("");
			StringBuilder html = wrapper.wrapAsHTML4FAOSTAT(table, false, wc);
			
			// wrap result
			ResponseBuilder builder = Response.ok(html.toString());
			builder.header("Access-Control-Allow-Origin", "*");
			builder.header("Access-Control-Max-Age", "3600");
			builder.header("Access-Control-Allow-Methods", "POST");
			builder.header("Access-Control-Allow-Headers", "X-Requested-With,Host,User-Agent,Accept,Accept-Language,Accept-Encoding,Accept-Charset,Keep-Alive,Connection,Referer,Origin");
								
			// return response
			//System.out.println("[END][" + id + "] - FAOSTATMethodsAndStandards.getHTML - " + (System.currentTimeMillis() - t0) + " millis.");
			return builder.build();
			
		} catch (Exception e) {
			return Response.status(500).entity("Error in 'getClassifications' service: " + e.getMessage()).build();
		}
		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{code}/json/{datasource}/{domainCode}/{lang}")
	public Response getJSON(@PathParam("code") final String code, @PathParam("datasource") final String datasource, @PathParam("domainCode") final String domainCode, @PathParam("lang") final String lang) {

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // Initiate utilities
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));
                Gson g = new Gson();
                List<String> headers = new ArrayList<String>();

                // compute result
                DatasourceBean db = datasourcePool.getDatasource(datasource.toUpperCase());
                SQLBean sql = null;

                // get SQL script
                if (code.equalsIgnoreCase("classifications")) {
                    sql = SQLBeansRepository.getClassifications(domainCode, lang);
                    headers.add("Item Code");
                    headers.add("Item Name");
                    headers.add("Definition");
                } else if (code.equalsIgnoreCase("abbreviations")) {
                    sql = SQLBeansRepository.getAbbreviations(domainCode, lang);
                    headers.add("Acronym");
                    headers.add("Definition");
                } else if (code.equalsIgnoreCase("glossary")) {
                    sql = SQLBeansRepository.getGlossary(domainCode, lang);
                    headers.add("Title");
                    headers.add("Definition");
                    headers.add("Sources");
                } else if (code.equalsIgnoreCase("methodology_list")) {
                    sql = SQLBeansRepository.getMethodologyList(domainCode, lang);
                    headers.add("Code");
                    headers.add("Methodology");
                } else if (code.equalsIgnoreCase("methodology")) {
                    sql = SQLBeansRepository.getMethodology(domainCode, lang);
                    headers.add("Note");
                    headers.add("Coverage");
                    headers.add("References");
                    headers.add("Collection");
                    headers.add("Estimation");
                } else if (code.equalsIgnoreCase("units")) {
                    sql = SQLBeansRepository.getUnits(domainCode, lang);
                    headers.add("Abbreviation");
                    headers.add("Title");
                }

                // Query the DB
                JDBCIterable it = new JDBCIterable();

                try {

                    // Query DB
                    it.query(db, Bean2SQL.convert(sql).toString());

                } catch (IllegalAccessException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getDomains' thrown an error: " + e.getMessage()));
                } catch (InstantiationException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getDomains' thrown an error: " + e.getMessage()));
                } catch (SQLException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getDomains' thrown an error: " + e.getMessage()));
                } catch (ClassNotFoundException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getDomains' thrown an error: " + e.getMessage()));
                } catch (Exception e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getDomains' thrown an error: " + e.getMessage()));
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

	private List<List<String>> createTable(String code, String datasource, String domainCode, String lang) throws IllegalAccessException, InstantiationException, SQLException, ClassNotFoundException {

		// initiate variable
		DATASOURCE ds = DATASOURCE.valueOf(datasource.toUpperCase());
		DBBean db = new DBBean(ds);
		SQLBean sql = null;
		List<String> headers = new ArrayList<String>();

		// get SQL script
		if (code.equalsIgnoreCase("classifications")) {
			sql = SQLBeansRepository.getClassifications(domainCode, lang);
			headers.add("Item Code");
			headers.add("Item Name");
			headers.add("Definition");
		} else if (code.equalsIgnoreCase("abbreviations")) {
			sql = SQLBeansRepository.getAbbreviations(domainCode, lang);
			headers.add("Acronym");
			headers.add("Definition");
		} else if (code.equalsIgnoreCase("glossary")) {
			sql = SQLBeansRepository.getGlossary(domainCode, lang);
			headers.add("Title");
			headers.add("Definition");
			headers.add("Sources");
		} else if (code.equalsIgnoreCase("methodology_list")) {
			sql = SQLBeansRepository.getMethodologyList(domainCode, lang);
			headers.add("Code");
			headers.add("Methodology");
		} else if (code.equalsIgnoreCase("methodology")) {
			sql = SQLBeansRepository.getMethodology(domainCode, lang);
			headers.add("Note");
			headers.add("Coverage");
			headers.add("References");
			headers.add("Collection");
			headers.add("Estimation");
		} else if (code.equalsIgnoreCase("units")) {
			sql = SQLBeansRepository.getUnits(domainCode, lang);
			headers.add("Abbreviation");
			headers.add("Title");
		}

		List<List<String>> table = JDBCConnector.query(db, sql, false);
		table.add(0, headers);

		return table;

	}

}