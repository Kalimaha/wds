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
import org.fao.fenix.wds.core.bean.cpi.CPIBean;
import org.fao.fenix.wds.core.constant.DATASOURCE;
import org.fao.fenix.wds.core.datasource.DatasourcePool;
import org.fao.fenix.wds.core.exception.WDSException;
import org.fao.fenix.wds.core.exception.WDSExceptionStreamWriter;
import org.fao.fenix.wds.core.jdbc.JDBCConnector;
import org.fao.fenix.wds.core.jdbc.JDBCIterable;
import org.fao.fenix.wds.core.sql.faostat.SQLBeansRepository;
import org.fao.fenix.wds.core.utils.Wrapper;
import org.fao.fenix.wds.core.utils.WrapperConfigurations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** 
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
@Component
@Path("/notes")
public class FAOSTATDownloadNotes {
	
	@Autowired
	private Wrapper wrapper;

    @Autowired
    DatasourcePool datasourcePool;
	
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Path("/cpinotes")
	public Response createCPINotes(@FormParam("datasource") String datasource, @FormParam("json") String json, @FormParam("lang") String lang) {
		
		try {
			
			// compute result
			Gson g = new Gson();
			CPIBean cpi = g.fromJson(json, CPIBean.class);
			DATASOURCE ds = DATASOURCE.valueOf(datasource.toUpperCase());
			SQLBean sql = new SQLBean(cpi.toString());
			DBBean db = new DBBean(ds);
			List<List<String>> table = JDBCConnector.query(db, sql, true);
			List<String> headers = new ArrayList<String>();
			headers.add("Country");
			headers.add("Year");
			headers.add("Index");
			headers.add("Index Base");
			table.add(0, headers);
			WrapperConfigurations wc = new WrapperConfigurations();
			StringBuilder html = wrapper.wrapAsHTML4FAOSTAT(table, false, wc);
			
			/* Stream result */
            return Response.status(200).entity(html.toString()).build();
					
		} catch (WDSException e) {
			return Response.status(500).entity("Error in 'getDomains' service: " + e.getMessage()).build();
		} catch (ClassNotFoundException e) {
			return Response.status(500).entity("Error in 'getDomains' service: " + e.getMessage()).build();
		} catch (SQLException e) {
			return Response.status(500).entity("Error in 'getDomains' service: " + e.getMessage()).build();
		} catch (InstantiationException e) {
			return Response.status(500).entity("Error in 'getDomains' service: " + e.getMessage()).build();
		} catch (IllegalAccessException e) {
			return Response.status(500).entity("Error in 'getDomains' service: " + e.getMessage()).build();
		}
		
	}
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("/cpi/{datasource}/{areaCode}/{lang}")
	public Response createCPITable(@PathParam("datasource") String datasource, @PathParam("areaCode") String areaCode, @PathParam("lang") String language) {
		
		try {
			
			// create HTML
			DATASOURCE ds = DATASOURCE.valueOf(datasource.toUpperCase());
			DBBean db = new DBBean(ds);
			SQLBean sql = SQLBeansRepository.getCPINotes(language, areaCode);
			WrapperConfigurations wc = new WrapperConfigurations();
			wc.setCssName("default");
			List<List<String>> table = JDBCConnector.query(db, sql, false);
			List<String> headers = new ArrayList<String>();
			headers.add("Name");
			headers.add("Value");
			table.add(0, headers);
			StringBuilder html = wrapper.wrapAsHTML(table, false, wc);
						
			/* Stream result */
            return Response.status(200).entity(html.toString()).build();
			
		} catch (WDSException e) {
			return Response.status(500).entity("Error in 'createCPITable' service: " + e.getMessage()).build();
		} catch (ClassNotFoundException e) {
			return Response.status(500).entity("Error in 'createCPITable' service: " + e.getMessage()).build();
		} catch (SQLException e) {
			return Response.status(500).entity("Error in 'createCPITable' service: " + e.getMessage()).build();
		} catch (InstantiationException e) {
			return Response.status(500).entity("Error in 'createCPITable' service: " + e.getMessage()).build();
		} catch (IllegalAccessException e) {
			return Response.status(500).entity("Error in 'createCPITable' service: " + e.getMessage()).build();
		}
		
	}
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("{domainCode}/{lang}")
	public Response createTable(@PathParam("domainCode") final String domainCode, @PathParam("lang") final String language) {

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));
                Gson g = new Gson();

                // Query
                String script = "SELECT html FROM Metadata_Domain_Text[M] WHERE M.lang = '" + language + "' AND M.name = 'Description' AND M.domain = '" + domainCode + "' ";
                SQLBean sql = new SQLBean(script);
                DatasourceBean db = datasourcePool.getDatasource(DATASOURCE.FAOSTATPROD.name());

                // Fetch data
                JDBCIterable it = new JDBCIterable();

                try {

                    // Query DB
                    it.query(db, sql.getQuery());

                } catch (IllegalAccessException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'createJSON' thrown an error: " + e.getMessage()));
                } catch (InstantiationException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'createJSON' thrown an error: " + e.getMessage()));
                } catch (SQLException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'createJSON' thrown an error: " + e.getMessage()));
                } catch (ClassNotFoundException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'createJSON' thrown an error: " + e.getMessage()));
                } catch (Exception e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getDomains' thrown an error: " + e.getMessage()));
                }

                // Build the output
                StringBuilder sb = new StringBuilder();
                sb.append("[");
                while(it.hasNext())
                    sb.append(g.toJson(it.next())).append(",");
                sb.deleteCharAt(sb.length() - 1);
                sb.append("]");
                writer.write(sb.toString());

                // Convert and write the output on the stream
                writer.flush();

            }

        };

        /* Stream result */
        return Response.status(200).entity(stream).build();
		
	}
	
}