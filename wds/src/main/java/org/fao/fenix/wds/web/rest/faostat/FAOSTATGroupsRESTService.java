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
import org.apache.log4j.Logger;
import org.fao.fenix.wds.core.bean.DBBean;
import org.fao.fenix.wds.core.bean.SQLBean;
import org.fao.fenix.wds.core.constant.DATASOURCE;
import org.fao.fenix.wds.core.exception.WDSExceptionStreamWriter;
import org.fao.fenix.wds.core.jdbc.JDBCIterable;
import org.fao.fenix.wds.core.sql.Bean2SQL;
import org.fao.fenix.wds.core.sql.faostat.SQLBeansRepository;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;
import java.sql.SQLException;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 */
@Component
@Path("/groups")
public class FAOSTATGroupsRESTService {

    private static final Logger LOGGER = Logger.getLogger(FAOSTATGroupsRESTService.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{datasource}/{language}")
    public Response getGroups(@PathParam("datasource") final String datasource, @PathParam("language") final String language) {

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // Initiate utilities
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));
                Gson g = new Gson();

                // compute result
                DATASOURCE ds = DATASOURCE.valueOf(datasource.toUpperCase());
                DBBean db = new DBBean(ds);
                SQLBean sql = SQLBeansRepository.getGroups(language);
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

}