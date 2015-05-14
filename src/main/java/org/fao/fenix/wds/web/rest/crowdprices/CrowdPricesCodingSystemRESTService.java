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
package org.fao.fenix.wds.web.rest.crowdprices;

import org.fao.fenix.wds.core.bean.DBBean;
import org.fao.fenix.wds.core.bean.SQLBean;
import org.fao.fenix.wds.core.bean.WhereBean;
import org.fao.fenix.wds.core.constant.DATASOURCE;
import org.fao.fenix.wds.core.exception.WDSException;
import org.fao.fenix.wds.core.jdbc.JDBCConnector;
import org.fao.fenix.wds.core.sql.crowdprices.SQLBeansRepository;
import org.fao.fenix.wds.core.utils.Wrapper;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Component
@Path("/crowdprices/codes")
public class CrowdPricesCodingSystemRESTService {

	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{codingsystem}/{language}")
	public Response getCodes(@PathParam("codingsystem") String codingsystem, @PathParam("language") String language) {
		
		try {
			DATASOURCE ds = DATASOURCE.CROWDPRICES;
			DBBean db = new DBBean(ds);
			CrowdPricesCodesConstants c = null;
			SQLBean sql =null;
			try {
				c = CrowdPricesCodesConstants.valueOf(codingsystem.toUpperCase());
			}catch(Exception e){}
			
//			System.out.println("c: " +c);
			if ( c != null) {
				switch (c) {
					case DATE: sql = SQLBeansRepository.getDates("data", new ArrayList<WhereBean>(), language); break;
				}
			}
			else{
				sql = SQLBeansRepository.getCodingSystem(codingsystem, new ArrayList<WhereBean>(), language);
//				sql = SQLBeansRepository.getCodingSystem(codingsystem, new ArrayList<WhereBean>(), language);
			}

//			System.out.println(Bean2SQL.convert(sql));
			List<List<String>> table = JDBCConnector.query(db, sql, true);
			// to remove the headers (TODO: add it in the wrapper?)
			table.remove(0);
			String json = Wrapper.wrapAsJSON(table).toString();
			
			// wrap result
			ResponseBuilder builder = Response.ok(json);
			builder.header("Access-Control-Allow-Origin", "*");
			builder.header("Access-Control-Max-Age", "3600");
			builder.header("Access-Control-Allow-Methods", "GET");
			builder.header("Access-Control-Allow-Headers", "X-Requested-With,Host,User-Agent,Accept,Accept-Language,Accept-Encoding,Accept-Charset,Keep-Alive,Connection,Referer,Origin");
			builder.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON+"; charset=utf-8");

			// return response
			return builder.build();
		
		} catch (WDSException e) {
			return Response.status(500).entity("Error in 'Crowdprices Coding System service: " + e.getMessage()).build();
		} catch (ClassNotFoundException e) {
			return Response.status(500).entity("Error in 'Crowdprices Coding System service: " + e.getMessage()).build();
		} catch (SQLException e) {
			return Response.status(500).entity("Error in 'Crowdprices Coding System service: " + e.getMessage()).build();
		} catch (InstantiationException e) {
			return Response.status(500).entity("Error in 'Crowdprices Coding System service: " + e.getMessage()).build();
		} catch (IllegalAccessException e) {
			return Response.status(500).entity("Error in 'Crowdprices Coding System service: " + e.getMessage()).build();
		}
	}
}