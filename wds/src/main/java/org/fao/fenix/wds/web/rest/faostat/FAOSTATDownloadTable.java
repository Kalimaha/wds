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
import com.google.gson.JsonSyntaxException;
import com.mongodb.*;
import com.mongodb.util.JSON;
import org.fao.fenix.wds.core.bean.*;
import org.fao.fenix.wds.core.constant.DATASOURCE;
import org.fao.fenix.wds.core.constant.SQL;
import org.fao.fenix.wds.core.datasource.DatasourcePool;
import org.fao.fenix.wds.core.exception.WDSException;
import org.fao.fenix.wds.core.exception.WDSExceptionStreamWriter;
import org.fao.fenix.wds.core.jdbc.JDBCConnector;
import org.fao.fenix.wds.core.jdbc.JDBCIterable;
import org.fao.fenix.wds.core.jdbc.MongoDBConnectionManager;
import org.fao.fenix.wds.core.sql.Bean2SQL;
import org.fao.fenix.wds.core.utils.Wrapper;
import org.fao.fenix.wds.core.utils.WrapperConfigurations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 */
@Component
@Path("/table")
public class FAOSTATDownloadTable {

    @Autowired
    private Wrapper wrapper;

    @Autowired
    private DatasourcePool datasourcePool;

    private String SCHEMA = "faostat3";

    private Gson g = new Gson();

    @POST
    @Path("/excelWithQuotes")
    public Response createExcelWithQuotes(@FormParam("datasource_WQ") String datasource,
                                          @FormParam("json_WQ") String json,
                                          @FormParam("cssFilename_WQ") String cssFilename,
                                          @FormParam("valueIndex_WQ") String valueIndex,
                                          @FormParam("thousandSeparator_WQ") String thousandSeparator,
                                          @FormParam("decimalSeparator_WQ") String decimalSeparator,
                                          @FormParam("decimalNumbers_WQ") String decimalNumbers,
                                          @FormParam("quote_WQ") String quote,
                                          @FormParam("title_WQ") String title,
                                          @FormParam("subtitle_WQ") String subtitle) {

        try {

            // Log the request
            save(new Date(), "/table/excelWithQuotes", json);

            // compute result
//            Gson g = new Gson();
            DATASOURCE ds = DATASOURCE.valueOf(datasource.toUpperCase());
            SQLBean sql = g.fromJson(json, SQLBean.class);
            DBBean db = new DBBean(ds);

            // alter the query to switch from LIMIT to TOP
            if (datasource.toUpperCase().startsWith("FAOSTAT"))
                sql.setQuery(replaceLimitWithTop(sql));

            // query the DB
            List<List<String>> table = JDBCConnector.query(db, sql, true);

            List<String> headers = new ArrayList<String>();
            for (int i = 0; i < sql.getSelects().size(); i++)
                headers.add(sql.getSelects().get(i).getAlias());
            table.add(0, headers);

            // Add quote
            List<String> tmp = new ArrayList<String>();
            tmp.add("Downloaded from FAOSTAT");
            table.add(0, tmp);
            if (title != null && title.length() > 0) {
                tmp = new ArrayList<String>();
                tmp.add(title);
                table.add(1, tmp);
            }
            tmp = new ArrayList<String>();
            tmp.add("");
            if (title != null && title.length() > 0)
                table.add(2, tmp);
            else
                table.add(1, tmp);

            // configure output
            WrapperConfigurations wc = new WrapperConfigurations();
            wc.setCssName(cssFilename);
            wc.setDecimalNumbers(Integer.valueOf(decimalNumbers));
            wc.setDecimalSeparator(decimalSeparator);
            wc.setThousandSeparator(thousandSeparator);
            try {
                wc.setValueColumnIndex(Integer.valueOf(valueIndex));
            } catch (Exception e) {

            }

            // create HTML
            StringBuilder excel = wrapper.wrapAsExcel(table, UUID.randomUUID().toString() + ".xls");

            /* Stream result */
            return Response.status(200).entity(excel.toString()).build();

        } catch (WDSException e) {
            e.printStackTrace();
            return Response.status(500).entity("REST '/table/excelWithQuotes' service: " + e.getMessage()).build();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return Response.status(500).entity("REST '/table/excelWithQuotes' service: " + e.getMessage()).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(500).entity("REST '/table/excelWithQuotes' service: " + e.getMessage()).build();
        } catch (InstantiationException e) {
            e.printStackTrace();
            return Response.status(500).entity("REST '/table/excelWithQuotes' service: " + e.getMessage()).build();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return Response.status(500).entity("REST '/table/excelWithQuotes' service: " + e.getMessage()).build();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return Response.status(500).entity("REST '/table/excelWithQuotes' service: " + e.getMessage()).build();
        }

    }

    /**
     * @param datasource e.g. FAOSTAT
     * @param json       Parameters to build SQL query
     * @return HTML formatted <code>String</code>
     * <p/>
     * Produces a HTML table.
     */
    @POST
    @Produces(MediaType.TEXT_HTML)
    @Path("/html")
    public Response createTable(@FormParam("datasource") String datasource,
                                @FormParam("json") String json,
                                @FormParam("cssFilename") String cssFilename,
                                @FormParam("valueIndex") String valueIndex,
                                @FormParam("thousandSeparator") String thousandSeparator,
                                @FormParam("decimalSeparator") String decimalSeparator,
                                @FormParam("decimalNumbers") String decimalNumbers) {

        try {

            // Log the request
            save(new Date(), "/table/html", json);

            // compute result
//			Gson g = new Gson();
            DATASOURCE ds = DATASOURCE.valueOf(datasource.toUpperCase());
            SQLBean sql = g.fromJson(json, SQLBean.class);
            List<String> headers = new ArrayList<String>();

            // alter the query to switch from LIMIT to TOP
            if (sql.getLimit() != null && sql.getLimit().length() > 0) {
                for (SelectBean sel : sql.getSelects())
                    headers.add(sel.getAlias().replaceAll("_", " "));
                String script = Bean2SQL.convert(sql).toString();
                script = script.replaceAll("SELECT ",
                        "SELECT TOP " + sql.getLimit() + " ");
                int idx = script.indexOf("LIMIT");
                if (idx > -1)
                    script = script.substring(0, idx);
                sql.setQuery(script);
            }

//            System.out.println(sql.getQuery());

            // compute result
            DBBean db = new DBBean(ds);
            List<List<String>> table = JDBCConnector.query(db, sql, true);

            // add the headers
            if (sql.getLimit() != null && sql.getLimit().length() > 0) {
                table.add(0, headers);
            }

            // configure output
            WrapperConfigurations wc = new WrapperConfigurations();
            wc.setCssName(cssFilename);
            wc.setDecimalNumbers(Integer.valueOf(decimalNumbers));
            wc.setDecimalSeparator(decimalSeparator);
            wc.setThousandSeparator(thousandSeparator);

            try {
                wc.setValueColumnIndex(Integer.valueOf(valueIndex));
            } catch (Exception ignored) {

            }

            // create HTML
            StringBuilder html = wrapper.wrapAsHTML4FAOSTAT(table, true, wc);

			/* Stream result */
            return Response.status(200).entity(html.toString()).build();

        } catch (WDSException e) {
            return Response.status(500).entity("REST '/table/html' service: " + e.getMessage()).build();
        } catch (ClassNotFoundException e) {
            return Response.status(500).entity("REST '/table/html' service: " + e.getMessage()).build();
        } catch (SQLException e) {
            return Response.status(500).entity("REST '/table/html' service: " + e.getMessage()).build();
        } catch (InstantiationException e) {
            return Response.status(500).entity("REST '/table/html' service: " + e.getMessage()).build();
        } catch (IllegalAccessException e) {
            return Response.status(500).entity("REST '/table/html' service: " + e.getMessage()).build();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return Response.status(500).entity("REST '/table/html' service: " + e.getMessage()).build();
        }

    }


    /**
     * @param datasource e.g. FAOSTAT
     * @param json       Parameters to build SQL query
     * @return HTML formatted <code>String</code>
     * <p/>
     * Produces a HTML table.
     */
    @POST
    @Produces(MediaType.TEXT_HTML)
    @Path("/html2")
    public Response createTable2(@FormParam("datasource") String datasource,
                                 @FormParam("json") String json,
                                 @FormParam("cssFilename") String cssFilename,
                                 @FormParam("valuesIndex") String valuesIndex,
                                 @FormParam("thousandSeparator") String thousandSeparator,
                                 @FormParam("decimalSeparator") String decimalSeparator,
                                 @FormParam("decimalNumbers") String decimalNumbers,
                                 @FormParam("nowrap") Boolean nowrap,
                                 @FormParam("addHeaders") Boolean addHeaders) {

        try {

            // Log the request
            save(new Date(), "/table/html2", json);

            // compute result
//			Gson g = new Gson();
            DATASOURCE ds = DATASOURCE.valueOf(datasource.toUpperCase());
            SQLBean sql = g.fromJson(json, SQLBean.class);
            List<String> headers = new ArrayList<String>();

            if (addHeaders != null)
                sql.setAddHeaders(addHeaders);

            if (sql.isAddHeaders()) {
                for (SelectBean sel : sql.getSelects()) {
                    headers.add(sel.getAlias().replaceAll("_", " "));
                }
            }

            // alter the query to switch from LIMIT to TOP
            if (datasource.toUpperCase().startsWith("FAOSTAT"))
                sql.setQuery(replaceLimitWithTop(sql));

            boolean isNoWrap = true;
            if (nowrap != null) {
                isNoWrap = nowrap;
            }

            // compute result
            DBBean db = new DBBean(ds);
            List<List<String>> table = JDBCConnector.query(db, sql, isNoWrap);

            // add the headers
            if (sql.isAddHeaders()) {
                table.add(0, headers);
            }

            // configure output
            WrapperConfigurations wc = new WrapperConfigurations();
            wc.setCssName(cssFilename);
            wc.setDecimalNumbers(Integer.valueOf(decimalNumbers));
            wc.setDecimalSeparator(decimalSeparator);
            wc.setThousandSeparator(thousandSeparator);

            if (valuesIndex != null) {
                // Parsing the String (i.e. 2,4,6,8);
                List<Integer> indexes = new ArrayList<Integer>();
                try {
                    String[] tokens = valuesIndex.split(",");
                    for (String token : tokens) {
                        indexes.add(Integer.valueOf(token));
                    }
                    wc.setValuesColumnIndex(indexes);
                } catch (Exception ignored) {

                }
            }

            // create HTML
            StringBuilder html = wrapper.wrapAsHTML4FAOSTAT2(table, isNoWrap, wc);

			/* Stream result */
            return Response.status(200).entity(html.toString()).build();

        } catch (WDSException e) {
            e.printStackTrace();
            return Response.status(500).entity("REST '/table/html2' service: " + e.getMessage()).build();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return Response.status(500).entity("REST '/table/html2' service: " + e.getMessage()).build();
        } catch (SQLException e) {
            return Response.status(500).entity("REST '/table/html2' service: " + e.getMessage()).build();
        } catch (InstantiationException e) {
            e.printStackTrace();
            return Response.status(500).entity("REST '/table/html2' service: " + e.getMessage()).build();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return Response.status(500).entity("REST '/table/html2' service: " + e.getMessage()).build();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return Response.status(500).entity("REST '/table/html2' service: " + e.getMessage()).build();
        }

    }

    @POST
    @Path("/excel")
    public Response createExcel(@FormParam("datasource") String datasource,
                                @FormParam("json") String json,
                                @FormParam("cssFilename") String cssFilename,
                                @FormParam("valueIndex") String valueIndex,
                                @FormParam("thousandSeparator") String thousandSeparator,
                                @FormParam("decimalSeparator") String decimalSeparator,
                                @FormParam("decimalNumbers") String decimalNumbers) {

        try {

            // Log the request
            save(new Date(), "/table/excel", json);

            // compute result
//            Gson g = new Gson();
            DATASOURCE ds = DATASOURCE.valueOf(datasource.toUpperCase());
            SQLBean sql = g.fromJson(json, SQLBean.class);
            DBBean db = new DBBean(ds);

            // alter the query to switch from LIMIT to TOP
            if (datasource.toUpperCase().startsWith("FAOSTAT"))
                sql.setQuery(replaceLimitWithTop(sql));

            // query the DB
            List<List<String>> table = JDBCConnector.query(db, sql, true);

            List<String> headers = new ArrayList<String>();
            for (int i = 0; i < sql.getSelects().size(); i++)
                headers.add(sql.getSelects().get(i).getAlias());
            table.add(0, headers);

            // configure output
            WrapperConfigurations wc = new WrapperConfigurations();
            wc.setCssName(cssFilename);
            wc.setDecimalNumbers(Integer.valueOf(decimalNumbers));
            wc.setDecimalSeparator(decimalSeparator);
            wc.setThousandSeparator(thousandSeparator);
            try {
                wc.setValueColumnIndex(Integer.valueOf(valueIndex));
            } catch (Exception e) {

            }

            // create HTML
            StringBuilder excel = wrapper.wrapAsExcel(table, UUID.randomUUID().toString() + ".xls");

            /* Stream result */
            return Response.status(200).entity(excel.toString()).build();

        } catch (WDSException e) {
            e.printStackTrace();
            return Response.status(500).entity("REST '/table/excel' service: " + e.getMessage()).build();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return Response.status(500).entity("REST '/table/excel' service: " + e.getMessage()).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(500).entity("REST '/table/excel' service: " + e.getMessage()).build();
        } catch (InstantiationException e) {
            e.printStackTrace();
            return Response.status(500).entity("REST '/table/excel' service: " + e.getMessage()).build();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return Response.status(500).entity("REST '/table/excel' service: " + e.getMessage()).build();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return Response.status(500).entity("REST '/table/excel' service: " + e.getMessage()).build();
        }

    }

    public String replaceLimitWithTop(SQLBean sql) {
        for (NestedWhereBean nwb : sql.getNestedWheres()) {
            SQLBean sql2 = nwb.getNestedCondition();
            String script2 = Bean2SQL.convert(sql2).toString();
            if (sql2.getLimit() != null && sql2.getLimit().length() > 0) {
                if (sql.getLimit() != SQL.NONE.name()) {
                    script2 = script2.replaceFirst("SELECT ", "SELECT TOP " + sql2.getLimit() + " ");
                    script2 = script2.replaceFirst("LIMIT " + sql2.getLimit(), "");
                }
            }
            sql2.setQuery(script2);
        }
        String script = Bean2SQL.convert(sql).toString();
        if (sql.getLimit() != null && sql.getLimit().length() > 0) {
            if (sql.getLimit() != SQL.NONE.name()) {
                script = script.replaceFirst("SELECT ", "SELECT TOP " + sql.getLimit() + " ");
                script = script.replaceFirst("LIMIT " + sql.getLimit(), "");
            }
        }
        return script;
    }

    /**
     * @param datasource e.g. FAOSTAT
     * @param json       Parameters to build SQL query
     * @return HTML formatted <code>String</code>
     * <p/>
     * Produces a HTML table.
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/json")
    public Response createJSON(@FormParam("datasource") final String datasource, @FormParam("json") final String json) {

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));
//                Gson g = new Gson();
                SQLBean sql = null;
                try {
                    sql = g.fromJson(json, SQLBean.class);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                DatasourceBean db = datasourcePool.getDatasource(datasource.toUpperCase());

                // alter the query to switch from LIMIT to TOP
                if (datasource.toUpperCase().startsWith("FAOSTAT"))
                    sql.setQuery(replaceLimitWithTop(sql));

                // compute result
                JDBCIterable it = new JDBCIterable();

                try {

                    // Log the request
                    save(new Date(), "/table/json", json);

                    // Query DB
//                    System.out.println(sql.getQuery());
                    it.query(db, sql.getQuery());

                } catch (IllegalAccessException e) {
                    e.getMessage();
                    WDSExceptionStreamWriter.streamException(os, ("REST '/table/json' thrown an error: " + e.getMessage()));
                } catch (InstantiationException e) {
                    e.getMessage();
                    WDSExceptionStreamWriter.streamException(os, ("REST '/table/json' thrown an error: " + e.getMessage()));
                } catch (SQLException e) {
                    e.getMessage();
                    WDSExceptionStreamWriter.streamException(os, ("REST '/table/json' thrown an error: " + e.getMessage()));
                } catch (ClassNotFoundException e) {
                    e.getMessage();
                    WDSExceptionStreamWriter.streamException(os, ("REST '/table/json' thrown an error: " + e.getMessage()));
                } catch (Exception e) {
                    e.getMessage();
                    WDSExceptionStreamWriter.streamException(os, ("REST '/table/json' thrown an error: " + e.getMessage()));
                }

                // write the result of the query
                writer.write("[");
                while (it.hasNext()) {
                    List<String> s = it.next();
                    try {
                        writer.write(g.toJson(s));
                    } catch (Exception e) {
                        e.printStackTrace();
//                        System.out.println(s);
                    }
                    if (it.hasNext())
                        writer.write(",");
                }
                writer.write("]");

                // Convert and write the output on the stream
                writer.flush();

            }

        };

        /* Stream result */
        return Response.status(200).entity(stream).build();

    }

    private void save(Date date, String rest, String payload) throws UnknownHostException {
        MongoDBConnectionManager mgr = MongoDBConnectionManager.getInstance();
        Mongo mongo = mgr.getMongo();
        DB db = mongo.getDB(SCHEMA);
        DBCollection collection = db.getCollection("logs");
        BasicDBObject document = new BasicDBObject();
        document.put("date", date);
        document.put("rest", rest);
        DBObject dbObject = (DBObject) JSON.parse(payload);
        document.put("payload", dbObject);
        collection.insert(document);
    }

}