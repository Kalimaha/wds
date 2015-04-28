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

import com.google.gson.Gson;
import org.fao.fenix.wds.core.bean.DBBean;
import org.fao.fenix.wds.core.bean.SQLBean;
import org.fao.fenix.wds.core.bean.WhereBean;
import org.fao.fenix.wds.core.constant.DATASOURCE;
import org.fao.fenix.wds.core.constant.SQL;
import org.fao.fenix.wds.core.constant.WHERE;
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
import java.util.LinkedHashMap;
import java.util.List;



/** 
 * @author <a href="mailto:simone.murzilli@fao.org">Simone Murzilli</a>
 * @author <a href="mailto:simone.murzilli@gmail.com">Simone Murzilli</a>
 * */
@Component
@Path("/crowdprices/data")
public class CrowdPricesDataRESTService {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	// TODO: first parameter as switch? (i.e. getPoints) output=html/js etc
	// http://localhost:8080/wds/rest/crowdprices/data/points/null/null/null/null/en
	@Path("/points/{marketcode}/{commoditycode}/{fromdate}/{todate}/{bbox}/{language}/{type}")
	public Response getPoints(@PathParam("marketcode") String marketcode,
			@PathParam("commoditycode") String commoditycode, 
			@PathParam("fromdate") String fromdate, 
			@PathParam("todate") String todate, 
			@PathParam("bbox") String bbox, 
			@PathParam("language") String language,
			@PathParam("type") String type) {

//        System.out.println("getPoints!!!!!!!!!!!1 "  );
		
		try {
//			System.out.println("getPoints: " );
			DATASOURCE ds = DATASOURCE.CROWDPRICES;
			DBBean db = new DBBean(ds);
			
			SQLBean sql = null;
			String geoJson = null;
			List<List<String>> table = null;
			if ( type.toLowerCase().equals("disabled")) {
				sql = SQLBeansRepository.getPointsDisabled(marketcode, commoditycode, fromdate, todate, bbox, language);
//				System.out.println("getPoints sql: " + sql );
				table = JDBCConnector.query(db, sql, true);
//                System.out.println("table: " + table );
				geoJson = createGeoJsonDisabled(table);
			} 
			else {
				sql = SQLBeansRepository.getPoints(marketcode, commoditycode, fromdate, todate, bbox, language);
//				System.out.println("getPoints sql: " + sql );
				table = JDBCConnector.query(db, sql, true);
//                System.out.println("table: " + table );
				geoJson = createGeoJson(table);
			}


//            System.out.println("geoJson: " + geoJson );
			
			// wrap result
			ResponseBuilder builder = Response.ok(geoJson);
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
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	// TODO: first parameter as switch? (i.e. getPoints) output=html/js etc
	// http://localhost:8080/wds/rest/crowdprices/data/points/null/null/null/null/en
	@Path("/raw/{marketcode}/{commoditycode}/{fromdate}/{todate}/{bbox}/{language}")
	public Response getRawData(@PathParam("marketcode") String marketcode, 
			@PathParam("commoditycode") String commoditycode, 
			@PathParam("fromdate") String fromdate, 
			@PathParam("todate") String todate, 
			@PathParam("bbox") String bbox, 
			@PathParam("language") String language) {
		
		try {
			
			DATASOURCE ds = DATASOURCE.CROWDPRICES;
			DBBean db = new DBBean(ds);
			
			SQLBean sql = SQLBeansRepository.getSummary(marketcode, commoditycode, fromdate, todate, bbox, language);
			
//			System.out.println(sql.getQuery());
			
			List<List<String>> table = JDBCConnector.query(db, sql, true);
//			table.remove(0);
//			System.out.println("table: " + table );
			
			String json = Wrapper.wrapAsJSON(table).toString();
//			System.out.println("json: " + json );
			
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
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	// TODO: first parameter as switch? (i.e. getPoints) output=html/js etc
	// http://localhost:8080/wds/rest/crowdprices/data/summary/null/null/en
	@Path("/summary/{commoditycode}/{fromdate}/{todate}/{bbox}/{language}")
	public Response getSummary(
			@PathParam("commoditycode") String commoditycode, 
			@PathParam("fromdate") String fromdate, 
			@PathParam("todate") String todate, 
			@PathParam("bbox") String bbox, 
			@PathParam("language") String language) {
		
		try {
			
			DATASOURCE ds = DATASOURCE.CROWDPRICES;
			DBBean db = new DBBean(ds);
			SQLBean sql = SQLBeansRepository.getSummary(null, commoditycode, fromdate, todate, bbox, language);
//			System.out.println("getSummary sql: " +Bean2SQL.convert(sql));
			List<List<String>> table = JDBCConnector.query(db, sql, true);
//			System.out.println("getSummary table: " + table );
			String json = Wrapper.wrapAsJSON(table).toString();

//			System.out.println("json: " + json );
			// wrap result
			ResponseBuilder builder = Response.ok(json, MediaType.APPLICATION_JSON);
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
	// http://localhost:8080/wds/rest/crowdprices/data/updates/null/null
	@Path("/count/{commoditycode}/{date}")
	public Response CheckUpdates(@PathParam("commoditycode") String commoditycode, @PathParam("date") String date) {
		
		try {
			DATASOURCE ds = DATASOURCE.CROWDPRICES;
			DBBean db = new DBBean(ds);
			List<WhereBean> whereBeans = getValues(commoditycode, date);
			SQLBean sql = SQLBeansRepository.getCount(whereBeans);
//			System.out.println(Bean2SQL.convert(sql));
			List<List<String>> table = JDBCConnector.query(db, sql, true);
//			System.out.println("table: " + table );
			String json = Wrapper.wrapAsJSON(table).toString();
//			String json = Wrapper.wrapAsJSONCrowdpricesSummary(table).toString();

//			System.out.println("json: " + json );
			// wrap result
			ResponseBuilder builder = Response.ok(json, MediaType.APPLICATION_JSON);
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
	// TODO: first parameter as switch? (i.e. getPoints) output=html/js etc
	// http://localhost:8080/wds/rest/crowdprices/data/summary/null/en
	@Path("/chart/{commoditycode}/{fromdate}/{todate}/{bbox}/{language}")
	public Response getChartData(
			@PathParam("commoditycode") String commoditycode, 
			@PathParam("fromdate") String fromdate, 
			@PathParam("todate") String todate, 
			@PathParam("bbox") String bbox, 
			@PathParam("language") String language) {
		
		try {
			
//			System.out.println("BBOCX" + bbox);
			DATASOURCE ds = DATASOURCE.CROWDPRICES;
			DBBean db = new DBBean(ds);
			SQLBean sql = SQLBeansRepository.getChartData(null, commoditycode, fromdate, todate, language, bbox);
//			System.out.println("chart: " + sql);
			List<List<String>> table = JDBCConnector.query(db, sql, true);
			
			String json = Wrapper.wrapAsJSON(table).toString();

			// wrap result
			ResponseBuilder builder = Response.ok(json, MediaType.APPLICATION_JSON);
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
	// TODO: first parameter as switch? (i.e. getPoints) output=html/js etc
	// http://localhost:8080/wds/rest/crowdprices/data/summary/null/en
	@Path("/chart2/{commoditycode}/{fromdate}/{todate}/{bbox}/{language}")
	public Response getChartData2(
			@PathParam("commoditycode") String commoditycode,
			@PathParam("fromdate") String fromdate, 
			@PathParam("todate") String todate, 
			@PathParam("bbox") String bbox, 
			@PathParam("language") String language) {
		
		try {
			
//			System.out.println("BBOCX" + bbox);
			DATASOURCE ds = DATASOURCE.CROWDPRICES;
			DBBean db = new DBBean(ds);
			SQLBean sql = SQLBeansRepository.getChartData2(null, commoditycode, fromdate, todate, language, bbox);
//			System.out.println("chart2: " + sql);
			List<List<String>> table = JDBCConnector.query(db, sql, true);

			
//			String json = Wrapper.wrapAsJSON(table).toString();
			
			// create JSON
			// compute result
			Gson g = new Gson();
			String json = g.toJson(table);
			
//			System.out.println("chart2: " + json);

			// wrap result
			ResponseBuilder builder = Response.ok(json, MediaType.APPLICATION_JSON);
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
	
	private List<WhereBean> getValues(String marketcode, String commoditycode, String bbox, String date){
		List<WhereBean> whereBeans = new ArrayList<WhereBean>();
		
		if ( !marketcode.equals("null"))
			whereBeans.add(new WhereBean(SQL.TEXT.name(), "D.marketcode", WHERE.IN.name(), null, getValues(marketcode, true)));
		if ( !commoditycode.equals("null"))
			whereBeans.add(new WhereBean(SQL.TEXT.name(), "D.commoditycode", WHERE.IN.name(), null, getValues(commoditycode, true)));
		if ( !date.equals("null"))
			whereBeans.add(new WhereBean(SQL.TEXT.name(), "date(D.date)", WHERE.IN.name(), null, getValues(date, true)));
		if ( !bbox.equals("null")) {
			String[] v =  getValues(bbox, false); 
			String xmin = v[0];
			String ymin = v[1];
			String xmax = v[2];
			String ymax = v[3];
			whereBeans.add(new WhereBean(SQL.DATE.name(),  "M.lon", ">", xmin, new ArrayList<String>()));
			whereBeans.add(new WhereBean(SQL.DATE.name(),  "M.lat", ">", ymin, new ArrayList<String>()));
			whereBeans.add(new WhereBean(SQL.DATE.name(),  "M.lon", "<", xmax, new ArrayList<String>()));
			whereBeans.add(new WhereBean(SQL.DATE.name(),  "M.lat", "<", ymax, new ArrayList<String>()));
		}
		
		return whereBeans;
	}
	
	private List<WhereBean> getValues(String commoditycode, String date){
		List<WhereBean> whereBeans = new ArrayList<WhereBean>();
		
		if ( !date.equals("null"))
			whereBeans.add(new WhereBean(SQL.TEXT.name(), "D.date", WHERE.IN.name(), null, getValues(date, true)));
		if ( !commoditycode.equals("null"))
			whereBeans.add(new WhereBean(SQL.TEXT.name(), "D.commoditycode", WHERE.IN.name(), null, getValues(commoditycode, true)));

		
		return whereBeans;
	}
	
	private String[] getValues(String values, boolean apex) {
		return extractValues(values, ",", apex);
	}
	
	public String[] extractValues(String s, String separator, boolean apex) {
		String[] tokens = s.split(separator);
		String[] l = new String[tokens.length];
		for (int i = 0 ; i < tokens.length; i++) {
			if  ( !tokens[i].equals("null") ) {
				if ( apex )
					l[i] =  "'" + tokens[i] + "'";
				else
					l[i] = tokens[i];
			}
			else {
				return null;
			}
		}
		return l;
	}
	
	private LinkedHashMap<String, List<List<String>>> getCrowdPricesPoints(List<List<String>> table) {

		LinkedHashMap<String, List<List<String>>> markets = new LinkedHashMap<String, List<List<String>>>();
		
		for (List<String> row : table) {
			String marketname = row.get(0);
			List<List<String>> rows = new ArrayList<List<String>>();
			if (markets.containsKey(marketname)) {
				rows = markets.get(marketname);
			}
			rows.add(row);
			markets.put(marketname, rows);
		}
		
		return markets;
	}
	
	private String createGeoJson(List<List<String>> table) {
		
		String s = "{ \"type\":\"FeatureCollection\",\"features\":[";
		int i = 0;
		LinkedHashMap<String, List<List<String>>> markets = getCrowdPricesPoints(table);
		for(String marketname : markets.keySet()) {
			String popupcontent = "<b>" + marketname + "</b><br>";
			String lat ="";
			String lon = "";
			for(List<String> row : markets.get(marketname)) {
				popupcontent += row.get(1).replace("_", " ") +" - "
							 + row.get(2) + " " 
							 + " "+ row.get(3).replace("_", " ") +"/" + row.get(4).replace("_", " ")+ "<br>";
				lon = row.get(5);
				lat = row.get(6);
			}
//			System.out.println("popup: " + popupcontent);
			s += "{\"type\":\"Feature\",\"properties\":{\"iconurl\":\"images/marker-icon.png\"," + "\"name\":\"Countrys\"," +
//			s += "{\"type\":\"Feature\",\"properties\":{\"iconurl\":\"http://fenixapps.fao.org/repository/js/leaflet/0.5.1/images/marker-icon-disabled.png\"," + "\"name\":\"Countrys\"," +
				 "\"popupContent\":\""+ popupcontent+" \"},\"geometry\":{\"type\":\"Point\",\"coordinates\":["
				+ lon + "," + lat + "]}}";
			if (i < markets.size() -1) {
				s += ",";
			}
			i++;
		}
		s += "]}";
		return s;
	}

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    // TODO: first parameter as switch? (i.e. getPoints) output=html/js etc
    // http://localhost:8080/wds/rest/crowdprices/data/summary/null/null/en
    @Path("/summary2/{commoditycode}/{fromdate}/{todate}/{bbox}/{language}")
    public Response getSummary2(
            @PathParam("commoditycode") String commoditycode,
            @PathParam("fromdate") String fromdate,
            @PathParam("todate") String todate,
            @PathParam("bbox") String bbox,
            @PathParam("language") String language) {

        try {


            DATASOURCE ds = DATASOURCE.CROWDPRICES;
            DBBean db = new DBBean(ds);
            SQLBean sql = SQLBeansRepository.getSummary2(null, commoditycode, fromdate, todate, bbox, language);
//			System.out.println("getSummary sql: " +Bean2SQL.convert(sql));
            List<List<String>> table = JDBCConnector.query(db, sql, true);
//			System.out.println("getSummary table: " + table );

            String json = createJsonGrid(table);

            // wrap result
            ResponseBuilder builder = Response.ok(json, MediaType.APPLICATION_JSON);
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
    // TODO: first parameter as switch? (i.e. getPoints) output=html/js etc
    // http://localhost:8080/wds/rest/crowdprices/data/summary/null/null/en
    @Path("/summaryaggregated/{commoditycode}/{fromdate}/{todate}/{bbox}/{language}")
    public Response getSummaryAggregated(
            @PathParam("commoditycode") String commoditycode,
            @PathParam("fromdate") String fromdate,
            @PathParam("todate") String todate,
            @PathParam("bbox") String bbox,
            @PathParam("language") String language) {

        try {

//            System.out.println("summaryaGG: " +bbox + " | " + language);


            DATASOURCE ds = DATASOURCE.CROWDPRICES;
            DBBean db = new DBBean(ds);
            SQLBean sql = SQLBeansRepository.getSummaryAggregated(null, commoditycode, fromdate, todate, bbox, language);
//            System.out.println("summaryaggregated sql: " +Bean2SQL.convert(sql));
            List<List<String>> table = JDBCConnector.query(db, sql, true);
//            System.out.println("getSummary table: " + table );


            String json = createJsonGrid(table);

            // wrap result
            ResponseBuilder builder = Response.ok(json, MediaType.APPLICATION_JSON);
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



	private String createGeoJsonDisabled(List<List<String>> table) {
		
		String s = "{ \"type\":\"FeatureCollection\",\"features\":[";
		int i = 0;
		LinkedHashMap<String, List<List<String>>> markets = getCrowdPricesPoints(table);
		for(String marketname : markets.keySet()) {
			String popupcontent = "<b>" + marketname + "</b><br> No available data for that market in the current selection";
			String lat ="";
			String lon = "";
			for(List<String> row : markets.get(marketname)) {
				lon = row.get(1);
				lat = row.get(2);
			}
//			System.out.println("popup: " + popupcontent);
			s += "{\"type\":\"Feature\",\"properties\":{\"iconurl\":\"images/marker-icon-disabled.png\"," + "\"name\":\"Countrys\"," +
				 "\"popupContent\":\""+ popupcontent+" \"},\"geometry\":{\"type\":\"Point\",\"coordinates\":["
				+ lon + "," + lat + "]}}";
			if (i < markets.size() -1) {
				s += ",";
			}
			i++;
		}
		s += "]}";
		return s;
	}

    private String createJsonGrid(List<List<String>> table) {


        if ( table.isEmpty() )
            return "[{}]";
        else {
            String s = "[";
            for(int i=0; i < table.size(); i++) {
                s+= "{";
                for(int j=0; j < table.get(i).size(); j++) {
                    s+= "\"" + j +"\"" + ":" + "\"" + table.get(i).get(j) +"\"";
                    if ( j != table.get(i).size() -1 ) {
                        s+= ",";
                    }
                }
                s+= "}";
                if ( i != table.size() -1 ) {
                    s+= ",";
                }
            }

            s += "]";
            return s;
        }
    }
	
}