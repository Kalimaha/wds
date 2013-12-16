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

import org.fao.fenix.wds.core.bean.DBBean;
import org.fao.fenix.wds.core.bean.SQLBean;
import org.fao.fenix.wds.core.constant.DATASOURCE;
import org.fao.fenix.wds.core.exception.WDSExceptionStreamWriter;
import org.fao.fenix.wds.core.jdbc.JDBCConnector;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
@Component
@Path("/faostat2")
public class FAOSTAT2NotesRESTService {
	
	@Autowired
	private Wrapper wrapper;
	
	private static Map<String, String> map = new HashMap<String, String>();
	
	static {
		map.put("B", "'B', 'BC', 'BL'");
		map.put("O", "'O', 'OA'");
		map.put("C", "'C', 'CC', 'CL', 'CS'");
		map.put("D", "'D', 'DA', 'DE', 'DO', 'DP', 'DT', 'DU', 'DV'");
		map.put("F", "'F', 'FO', 'FT'");
		map.put("I", "'I', 'CS', 'RM', 'RY'");
		map.put("D", "'D', 'DA', 'DE', 'DO', 'DP', 'DT', 'DU', 'DV'");
		map.put("G1", "'G1', 'GA', 'GE', 'GM', 'GP', 'GR', 'GT', 'GU', 'GY'");		
		map.put("G2", "'G2', 'GC', 'GF', 'GG'");
		map.put("P", "'P', 'PA', 'PI', 'PM', 'PP'");
		map.put("Q", "'Q', 'QA', 'QC', 'QD', 'QI', 'QL', 'QP', 'QV'");
		map.put("R", "'R', 'RA', 'RF', 'RL', 'RM', 'RP', 'RT', 'RV', 'RY'");
		map.put("T", "'T', 'TA', 'TI', 'TM', 'TP'");
	}
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("/{datasource}/{groupCode}/{lang}")
	public Response createCPITable(@PathParam("datasource") final String datasource, @PathParam("groupCode") final String groupCode, @PathParam("lang") final String language) {

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // Initiate utilities
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

                // create HTML
                DATASOURCE ds = DATASOURCE.valueOf(datasource.toUpperCase());
                DBBean db = new DBBean(ds);

                String in = map.get(groupCode);
                String script = "SELECT H.HTMLText" + language + " FROM Metadata_DomainHTML H WHERE H.DomainCode IN (" + in + ") ORDER BY H.DomainCode ASC ";
                SQLBean sql = new SQLBean(script);

                WrapperConfigurations wc = new WrapperConfigurations();
                wc.setCssName("default");
                List<List<String>> table = null;

                try {

                    // Query SQLServer
                    table = JDBCConnector.query(db, sql, false);

                } catch (IllegalAccessException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'createCPITable' thrown an error: " + e.getMessage()));
                } catch (InstantiationException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'createCPITable' thrown an error: " + e.getMessage()));
                } catch (SQLException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'createCPITable' thrown an error: " + e.getMessage()));
                } catch (ClassNotFoundException e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'createCPITable' thrown an error: " + e.getMessage()));
                }

                StringBuilder html = wrapper.wrapAsHTML(table, false, wc);

                // Convert and write the output on the stream
                writer.write(html.toString());
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

}