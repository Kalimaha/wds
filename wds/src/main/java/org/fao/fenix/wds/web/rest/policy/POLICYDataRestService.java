package org.fao.fenix.wds.web.rest.policy;

import com.google.gson.Gson;
import org.fao.fenix.wds.core.bean.DatasourceBean;
import org.fao.fenix.wds.core.bean.SQLBean;
import org.fao.fenix.wds.core.bean.policy.POLICYDataObject;
import org.fao.fenix.wds.core.datasource.DatasourcePool;
import org.fao.fenix.wds.core.exception.WDSExceptionStreamWriter;
import org.fao.fenix.wds.core.jdbc.JDBCIterable;
import org.fao.fenix.wds.core.policy.JDBCIterablePolicy;
import org.fao.fenix.wds.core.policy.PolicyProcedures;
import org.fao.fenix.wds.core.sql.Bean2SQL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.lang.*;

/**
 * @author <a href="mailto:barbara.cintoli@fao.org">Barbara Cintoli</a>
 * @author <a href="mailto:barbara.cintoli@gmail.com">Barbara Cintoli</a>
 */
@Component
@Path("/policyservice")
public class POLICYDataRestService {

    @Autowired
    private DatasourcePool datasourcePool;

//    private FAOSTATProcedures fp = new FAOSTATProcedures();

    private PolicyProcedures pp = new PolicyProcedures();

    private final Gson g = new Gson();
    private final Gson g1 = new Gson();

    //Policy at a Glance Functions Start
//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path("/biofuelPoliciesTimeSeries")
//    public Response biofuelPolicies_timeSeries(@FormParam("pdObj") String pdObject) throws Exception {
//         System.out.println("biofuelPolicies_timeSeries start "+pdObject);
//        POLICYDataObject pd_obj = g.fromJson(pdObject, POLICYDataObject.class);
//        DatasourceBean dsBean = datasourcePool.getDatasource(pd_obj.getDatasource());
//        //The format of cplId is this: 'code1', 'code2', 'code3'
//        final Map<String, LinkedHashMap<String, String>> map=  pp.biofuelPolicies_timeSeries(dsBean, pd_obj);
//
//        for( String key: map.keySet())
//        {
//            System.out.println("key "+key);
//            if(key!=null)
//            {
//                Set<String> keySet2= map.get(key).keySet();
//                for(String key2: keySet2)
//                {
//                    System.out.println("time key2 "+key2);
//                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                    Date date = df.parse(key2);
////                    Date date = new Date(key2);
//                    long epoch = date.getTime();
//                    System.out.println(epoch);
//                    System.out.println("country count key "+map.get(key).get(key2));
//                }
//            }
//
//        }
//
//
//        final JDBCIterablePolicy it = new JDBCIterablePolicy();
//        //  System.out.println("getMasterFromCplId after getMasterFromCplId");
//        // Initiate the stream
//        StreamingOutput stream = new StreamingOutput() {
//
//            @Override
//            public void write(OutputStream os) throws IOException, WebApplicationException {
//
//                // compute result
//                Writer writer = new BufferedWriter(new OutputStreamWriter(os));
//
//                for( String key: map.keySet())
//                {
//                    writer.write("[");
//                    Set<String> keySet2= map.get(key).keySet();
//                    int i=0;
//                    for(String key2: keySet2) {
//                        System.out.println("time key2 "+key2);
//                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                        Date date = null;
//                        try {
//                            date = df.parse(key2);
//                        } catch (ParseException e) {
//                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                        }
////                    Date date = new Date(key2);
//                        long epoch = date.getTime();
//                        System.out.println(epoch);
//                        System.out.println("country count key "+map.get(key).get(key2));
//                        writer.write(g1.toJson("["+epoch+","+map.get(key).get(key2)+"]"));
//                        i++;
//                        if (i< keySet2.size())
//                            writer.write(",");
//                    }
//                   // writer.write("]");
//                    writer.write("]");
//                }
//
//                // Convert and write the output on the stream
//                writer.flush();
//            }
//        };
//
////        var seriesOptions = [];
////
////        seriesOptions.push({
////                name: '1',
////                data: [
////            /* May 2006 */
////        [1147651200000,67.79],
////        [1147737600000,64.98],
////        [1147824000000,65.26],
////        [1147910400000,63.18],
//
////        final JDBCIterablePolicy it = new JDBCIterablePolicy();
////        //  System.out.println("getMasterFromCplId after getMasterFromCplId");
////        // Initiate the stream
////        StreamingOutput stream = new StreamingOutput() {
////
////            @Override
////            public void write(OutputStream os) throws IOException, WebApplicationException {
////
////                // compute result
////                Writer writer = new BufferedWriter(new OutputStreamWriter(os));
////
////                writer.write("[");
////                while(it.hasNext()) {
////                    writer.write(g.toJson(it.next()));
////                    if (it.hasNext())
////                        writer.write(",");
////                }
////                writer.write("]");
////
////                // Convert and write the output on the stream
////                writer.flush();
////            }
////        };
//
//        // Stream result
//        return Response.status(200).entity(stream).build();
//    }
    //Policy at a Glance Functions Start
    //Highstock Chart
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/biofuelPoliciesTimeSeries")
    public Response biofuelPolicies_timeSeries(@FormParam("pdObj") String pdObject) throws Exception {
        // System.out.println("biofuelPolicies_timeSeries start "+pdObject);
        final POLICYDataObject pd_obj = g.fromJson(pdObject, POLICYDataObject.class);
        DatasourceBean dsBean = datasourcePool.getDatasource(pd_obj.getDatasource());
        //The format of cplId is this: 'code1', 'code2', 'code3'
        final Map<String, LinkedHashMap<String, String>> map=  pp.biofuelPolicies_timeSeries(dsBean, pd_obj);

//        for( String key: map.keySet())
//        {
//            System.out.println("key "+key);
//            if(key!=null)
//            {
//                Set<String> keySet2= map.get(key).keySet();
//                for(String key2: keySet2)
//                {
//                    System.out.println("time key2 "+key2);
//                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                    Date date = df.parse(key2);
////                    Date date = new Date(key2);
//                    long epoch = date.getTime();
//                    System.out.println(epoch);
//                    System.out.println("country count key "+map.get(key).get(key2));
//                }
//            }
//        }

        final JDBCIterablePolicy it = new JDBCIterablePolicy();
        //  System.out.println("getMasterFromCplId after getMasterFromCplId");
        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

                JSONArray jsonArrayTot = new JSONArray();
                int series_count = 1;
                //For each policy type
                for( String key: map.keySet())
                {
                    //writer.write("[");
                    JSONObject jsonobj = new JSONObject();
                    jsonobj.put("name", series_count);
                    JSONArray jsonArray = new JSONArray();
                    Set<String> keySet2= map.get(key).keySet();
                    int i=0;
                    for(String key2: keySet2) {
                        //System.out.println("time key2 "+key2);
//                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
                        Date date = null;
                        try {
                            date = df.parse(key2);
                        } catch (ParseException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
//                    Date date = new Date(key2);
                        long epoch = date.getTime();
                        //System.out.println(epoch);
                        //System.out.println("country count key "+map.get(key).get(key2));
                        //writer.write(g1.toJson("["+epoch+","+map.get(key).get(key2)+"]"));
                        JSONArray jsonArrayInside = new JSONArray();
                        jsonArrayInside.add(epoch);
                        jsonArrayInside.add(Integer.parseInt(map.get(key).get(key2)));
                        jsonArray.add(jsonArrayInside);
                        i++;
//                        if (i< keySet2.size())
//                            writer.write(",");
                    }

                    jsonobj.put("data", jsonArray);
                    // writer.write("]");
//                    writer.write("]");
                    jsonArrayTot.add(jsonobj);
                    series_count++;
                }
                JSONObject jsonobj_to_return = new JSONObject();
                System.out.println("pd_obj.getCommodity_class_code() "+pd_obj.getCommodity_class_code());
                jsonobj_to_return.put("commodityClassCode",pd_obj.getCommodity_class_code());
                jsonobj_to_return.put("dataArray",jsonArrayTot);
                writer.write(g.toJson(jsonobj_to_return));
//                writer.write(g.toJson(jsonArrayTot));

                // Convert and write the output on the stream
                writer.flush();
            }
        };
        // Stream result
        return Response.status(200).entity(stream).build();
    }

    //Highstock Chart
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/biofuelPoliciesMeasuresTimeSeries")
    public Response biofuelPolicyMeasures_timeSeries(@FormParam("pdObj") String pdObject) throws Exception {
        // System.out.println("biofuelPolicies_timeSeries start "+pdObject);
        final POLICYDataObject pd_obj = g.fromJson(pdObject, POLICYDataObject.class);
        DatasourceBean dsBean = datasourcePool.getDatasource(pd_obj.getDatasource());
        //The format of cplId is this: 'code1', 'code2', 'code3'
        final Map<String, LinkedHashMap<String, String>> map=  pp.biofuelPolicyMeasures_timeSeries(dsBean, pd_obj);

//        for( String key: map.keySet())
//        {
//            System.out.println("key "+key);
//            if(key!=null)
//            {
//                Set<String> keySet2= map.get(key).keySet();
//                for(String key2: keySet2)
//                {
//                    System.out.println("time key2 "+key2);
//                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                    Date date = df.parse(key2);
////                    Date date = new Date(key2);
//                    long epoch = date.getTime();
//                    System.out.println(epoch);
//                    System.out.println("country count key "+map.get(key).get(key2));
//                }
//            }
//        }

        final JDBCIterablePolicy it = new JDBCIterablePolicy();
        //  System.out.println("getMasterFromCplId after getMasterFromCplId");
        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

                JSONArray jsonArrayTot = new JSONArray();
                int series_count = 1;
                //For each policy type
                for( String key: map.keySet())
                {
                    //writer.write("[");
                    JSONObject jsonobj = new JSONObject();
                    jsonobj.put("name", series_count);
                    JSONArray jsonArray = new JSONArray();
                    Set<String> keySet2= map.get(key).keySet();
                    int i=0;
                    for(String key2: keySet2) {
                        //System.out.println("time key2 "+key2);
//                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
                        Date date = null;
                        try {
                            date = df.parse(key2);
                        } catch (ParseException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
//                    Date date = new Date(key2);
                        long epoch = date.getTime();
                        //System.out.println(epoch);
                        //System.out.println("country count key "+map.get(key).get(key2));
                        //writer.write(g1.toJson("["+epoch+","+map.get(key).get(key2)+"]"));
                        JSONArray jsonArrayInside = new JSONArray();
                        jsonArrayInside.add(epoch);
                        jsonArrayInside.add(Integer.parseInt(map.get(key).get(key2)));
                        jsonArray.add(jsonArrayInside);
                        i++;
//                        if (i< keySet2.size())
//                            writer.write(",");
                    }

                    jsonobj.put("data", jsonArray);
                    // writer.write("]");
//                    writer.write("]");
                    jsonArrayTot.add(jsonobj);
                    series_count++;
                }
                JSONObject jsonobj_to_return = new JSONObject();
                System.out.println("pd_obj.getCommodity_class_code() "+pd_obj.getCommodity_class_code());
                jsonobj_to_return.put("commodityClassCode",pd_obj.getCommodity_class_code());
                jsonobj_to_return.put("dataArray",jsonArrayTot);
                writer.write(g.toJson(jsonobj_to_return));
//                writer.write(g.toJson(jsonArrayTot));

                // Convert and write the output on the stream
                writer.flush();
            }
        };
        // Stream result
        return Response.status(200).entity(stream).build();
    }

    //Highstock Chart
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/exportRestrictionsPoliciesMeasuresTimeSeries")
    public Response exportRestrictionsPolicyMeasures_timeSeries(@FormParam("pdObj") String pdObject) throws Exception {
        // System.out.println("biofuelPolicies_timeSeries start "+pdObject);
        final POLICYDataObject pd_obj = g.fromJson(pdObject, POLICYDataObject.class);
        DatasourceBean dsBean = datasourcePool.getDatasource(pd_obj.getDatasource());
        //The format of cplId is this: 'code1', 'code2', 'code3'
        final Map<String, LinkedHashMap<String, String>> map=  pp.exportRestrictionsPolicyMeasures_timeSeries(dsBean, pd_obj);

        final JDBCIterablePolicy it = new JDBCIterablePolicy();
        //  System.out.println("getMasterFromCplId after getMasterFromCplId");
        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

                JSONArray jsonArrayTot = new JSONArray();
                int series_count = 1;
                //For each policy type
                for( String key: map.keySet())
                {
                    //writer.write("[");
                    JSONObject jsonobj = new JSONObject();
                    jsonobj.put("name", series_count);
                    JSONArray jsonArray = new JSONArray();
                    Set<String> keySet2= map.get(key).keySet();
                    int i=0;
                    for(String key2: keySet2) {
                        //System.out.println("time key2 "+key2);
//                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
                        Date date = null;
                        try {
                            date = df.parse(key2);
                        } catch (ParseException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
//                    Date date = new Date(key2);
                        long epoch = date.getTime();
                        //System.out.println(epoch);
                        //System.out.println("country count key "+map.get(key).get(key2));
                        //writer.write(g1.toJson("["+epoch+","+map.get(key).get(key2)+"]"));
                        JSONArray jsonArrayInside = new JSONArray();
                        jsonArrayInside.add(epoch);
                        jsonArrayInside.add(Integer.parseInt(map.get(key).get(key2)));
                        jsonArray.add(jsonArrayInside);
                        i++;
//                        if (i< keySet2.size())
//                            writer.write(",");
                    }

                    jsonobj.put("data", jsonArray);
                    // writer.write("]");
//                    writer.write("]");
                    jsonArrayTot.add(jsonobj);
                    series_count++;
                }
                JSONObject jsonobj_to_return = new JSONObject();
                System.out.println("pd_obj.getCommodity_class_code() "+pd_obj.getCommodity_class_code());
                jsonobj_to_return.put("commodityClassCode",pd_obj.getCommodity_class_code());
                jsonobj_to_return.put("dataArray",jsonArrayTot);
                writer.write(g.toJson(jsonobj_to_return));
//                writer.write(g.toJson(jsonArrayTot));

                // Convert and write the output on the stream
                writer.flush();
            }
        };
        // Stream result
        return Response.status(200).entity(stream).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/policyTypesFromDomain/{datasource}/{commodityDomainCode}")
    public Response getpolicyTypes_fromPolicyDomain(@PathParam("datasource") String datasource, @PathParam("commodityDomainCode") String commodityDomainCode) throws Exception {

        // compute result
//        DATASOURCE ds = DATASOURCE.POLICY;
//        DBBean db = new DBBean(ds);

        DatasourceBean dsBean = datasourcePool.getDatasource(datasource);
        final JDBCIterablePolicy it =  pp.getpolicyTypes_fromPolicyDomain(dsBean, commodityDomainCode);

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

                // write the result of the query
                writer.write("[");
                while(it.hasNext()) {
                    // System.out.println(it.next());
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
    @Path("/exportSubsidiesCountries/{datasource}/{policyType}/{policyMeasure}")
    public Response getcountries_fromExportSubsidies(@PathParam("datasource") String datasource, @PathParam("policyType") String policyType, @PathParam("policyMeasure") String policyMeasure) throws Exception {

        System.out.println(" getcountries_fromExportSubsidies start ");
        System.out.println(" getcountries_fromExportSubsidies policyType= "+policyType);
        System.out.println(" getcountries_fromExportSubsidies policyMeasure= "+policyMeasure);
        DatasourceBean dsBean = datasourcePool.getDatasource(datasource);
        final JDBCIterablePolicy it =  pp.getcountries_fromPolicy(dsBean, policyType, policyMeasure);

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

                // write the result of the query
                writer.write("[");
                while(it.hasNext()) {
                    // System.out.println(it.next());
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

    //Highchart Chart
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/biofuelPoliciesBarChart")
    public Response biofuelPolicies_barchart(@FormParam("pdObj") String pdObject) throws Exception {
         System.out.println("biofuelPolicies_timeSeries start New"+pdObject);
        System.out.println(pdObject);
        POLICYDataObject pd_obj = g.fromJson(pdObject, POLICYDataObject.class);
        DatasourceBean dsBean = datasourcePool.getDatasource(pd_obj.getDatasource());
        //The format of cplId is this: 'code1', 'code2', 'code3'
        System.out.println("Before biofuelPolicies_barchart ");
        final Map<String, LinkedHashMap<String, String>> map=  pp.biofuelPolicies_barchart(dsBean, pd_obj);
        System.out.println("2222222");
//        final Map<String, LinkedHashMap<String, String>> map= new HashMap<String, LinkedHashMap<String, String>>();
        //        for( String key: map.keySet())
//        {
//            System.out.println("key "+key);
//            if(key!=null)
//            {
//                Set<String> keySet2= map.get(key).keySet();
//                for(String key2: keySet2)
//                {
//                    System.out.println("time key2 "+key2);
//                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                    Date date = df.parse(key2);
////                    Date date = new Date(key2);
//                    long epoch = date.getTime();
//                    System.out.println(epoch);
//                    System.out.println("country count key "+map.get(key).get(key2));
//                }
//            }
//        }
        /*
        * series: [{
                        name: '1',
                        data: [49, 71, 106]
                    }, {
                        name: '2',
                        data: [93, 106, 84]
                    }]*/

        final JDBCIterablePolicy it = new JDBCIterablePolicy();
        //  System.out.println("getMasterFromCplId after getMasterFromCplId");
        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));
                System.out.println("333333");
                JSONArray jsonArrayTot = new JSONArray();
                int series_count = 1;
                for( String key: map.keySet())
                {
                    //One obj for each policy type
                    //writer.write("[");
                    JSONObject jsonobj = new JSONObject();
                    jsonobj.put("name", series_count);
                    JSONArray jsonArray = new JSONArray();
                    Set<String> keySet2= map.get(key).keySet();

                    //Three elements... Ethanol, Biodisel, Biofuel
                    for(String key2: keySet2) {
                        jsonArray.add(Integer.parseInt(map.get(key).get(key2)));
                    }

                    jsonobj.put("data", jsonArray);
                    // writer.write("]");
//                    writer.write("]");
                    jsonArrayTot.add(jsonobj);
                    series_count++;
                }
                writer.write(g.toJson(jsonArrayTot));
                // Convert and write the output on the stream
                writer.flush();
            }
        };
        // Stream result
        return Response.status(200).entity(stream).build();
    }

    //Highchart Chart
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/biofuelPolicyMeasuresBarChart")
    public Response biofuelPolicyMeasures_barchart(@FormParam("pdObj") String pdObject) throws Exception {
         System.out.println("biofuelPolicies_timeSeries start "+pdObject);
        System.out.println(pdObject);
        POLICYDataObject pd_obj = g.fromJson(pdObject, POLICYDataObject.class);
        DatasourceBean dsBean = datasourcePool.getDatasource(pd_obj.getDatasource());
        //The format of cplId is this: 'code1', 'code2', 'code3'
        final Map<String, LinkedHashMap<String, String>> map=  pp.biofuelMeasures_barchart(dsBean, pd_obj);
//        final Map<String, LinkedHashMap<String, String>> map= new HashMap<String, LinkedHashMap<String, String>>();
        //        for( String key: map.keySet())
//        {
//            System.out.println("key "+key);
//            if(key!=null)
//            {
//                Set<String> keySet2= map.get(key).keySet();
//                for(String key2: keySet2)
//                {
//                    System.out.println("time key2 "+key2);
//                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                    Date date = df.parse(key2);
////                    Date date = new Date(key2);
//                    long epoch = date.getTime();
//                    System.out.println(epoch);
//                    System.out.println("country count key "+map.get(key).get(key2));
//                }
//            }
//        }
        /*
        * series: [{
                        name: '1',
                        data: [49, 71, 106]
                    }, {
                        name: '2',
                        data: [93, 106, 84]
                    }]*/

        final JDBCIterablePolicy it = new JDBCIterablePolicy();
        //  System.out.println("getMasterFromCplId after getMasterFromCplId");
        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

                JSONArray jsonArrayTot = new JSONArray();
                int series_count = 1;
                for( String key: map.keySet())
                {
                    //One obj for each policy type
                    //writer.write("[");
                    JSONObject jsonobj = new JSONObject();
                    jsonobj.put("name", series_count);
                    JSONArray jsonArray = new JSONArray();
                    Set<String> keySet2= map.get(key).keySet();

                    //Three elements... Ethanol, Biodisel, Biofuel
                    for(String key2: keySet2) {
                        jsonArray.add(Integer.parseInt(map.get(key).get(key2)));
                    }

                    jsonobj.put("data", jsonArray);
                    // writer.write("]");
//                    writer.write("]");
                    jsonArrayTot.add(jsonobj);
                    series_count++;
                }
                writer.write(g.toJson(jsonArrayTot));
                // Convert and write the output on the stream
                writer.flush();
            }
        };
        // Stream result
        return Response.status(200).entity(stream).build();
    }

    //Highchart Chart
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/exportRestrictionsPolicyMeasuresBarChart")
    public Response exportRestrictionsPolicyMeasures_barchart(@FormParam("pdObj") String pdObject) throws Exception {
        //System.out.println("biofuelPolicies_timeSeries start "+pdObject);
        //System.out.println(pdObject);
        POLICYDataObject pd_obj = g.fromJson(pdObject, POLICYDataObject.class);
        DatasourceBean dsBean = datasourcePool.getDatasource(pd_obj.getDatasource());
        //The format of cplId is this: 'code1', 'code2', 'code3'
        final Map<String, LinkedHashMap<String, String>> map=  pp.exportRestrictionsMeasures_barchart(dsBean, pd_obj);
//        final Map<String, LinkedHashMap<String, String>> map= new HashMap<String, LinkedHashMap<String, String>>();
        //        for( String key: map.keySet())
//        {
//            System.out.println("key "+key);
//            if(key!=null)
//            {
//                Set<String> keySet2= map.get(key).keySet();
//                for(String key2: keySet2)
//                {
//                    System.out.println("time key2 "+key2);
//                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                    Date date = df.parse(key2);
////                    Date date = new Date(key2);
//                    long epoch = date.getTime();
//                    System.out.println(epoch);
//                    System.out.println("country count key "+map.get(key).get(key2));
//                }
//            }
//        }
        /*
        * series: [{
                        name: '1',
                        data: [49, 71, 106]
                    }, {
                        name: '2',
                        data: [93, 106, 84]
                    }]*/

        final JDBCIterablePolicy it = new JDBCIterablePolicy();
        //  System.out.println("getMasterFromCplId after getMasterFromCplId");
        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

                JSONArray jsonArrayTot = new JSONArray();
                int series_count = 1;
                for( String key: map.keySet())
                {
                    //One obj for each policy type
                    //writer.write("[");
                    JSONObject jsonobj = new JSONObject();
                    jsonobj.put("name", series_count);
                    JSONArray jsonArray = new JSONArray();
                    Set<String> keySet2= map.get(key).keySet();

                    //Three elements... Ethanol, Biodisel, Biofuel
                    for(String key2: keySet2) {
                        jsonArray.add(Integer.parseInt(map.get(key).get(key2)));
                    }

                    jsonobj.put("data", jsonArray);
                    // writer.write("]");
//                    writer.write("]");
                    jsonArrayTot.add(jsonobj);
                    series_count++;
                }
                writer.write(g.toJson(jsonArrayTot));
                // Convert and write the output on the stream
                writer.flush();
            }
        };
        // Stream result
        return Response.status(200).entity(stream).build();
    }

        @POST
        @Produces(MediaType.APPLICATION_JSON)
        @Path("/exportSubsidiesPolicyElementLineChart")
        public Response exportSubsidiesPolicyElementLineChart(@FormParam("pdObj") String pdObject) throws Exception {
            System.out.println(" exportSubsidiesPolicyElementLineChart ");
            POLICYDataObject pd_obj = g.fromJson(pdObject, POLICYDataObject.class);
            System.out.println(pd_obj);

            DatasourceBean dsBean = datasourcePool.getDatasource(pd_obj.getDatasource());
            String policy_type = "";
            if (pd_obj.getPolicy_type_code() != null)
            {
                for(int i=0; i< pd_obj.getPolicy_type_code().length; i++)
                {
                    policy_type+=pd_obj.getPolicy_type_code()[i];
                    if(i<pd_obj.getPolicy_type_code().length-1)
                    {
                        policy_type+=",";
                    }
                }
            }

            String policy_measure = "";
            if (pd_obj.getPolicy_measure_code() != null)
            {
                for(int i=0; i< pd_obj.getPolicy_measure_code().length; i++)
                {
                    policy_measure+=pd_obj.getPolicy_measure_code()[i];
                    if(i<pd_obj.getPolicy_measure_code().length-1)
                    {
                        policy_measure+=",";
                    }
                }
            }

            final JDBCIterablePolicy it =  pp.getDistinctcpl_id(dsBean, pd_obj, policy_type, policy_measure);

            String cpl_id = "";
            while(it.hasNext()) {
                //[2, Domestic, 1, Agricultural]
                String val = it.next().toString();
                System.out.println("val = "+val);
                int index = val.lastIndexOf(']');
                val = val.substring(1, index);
                cpl_id +=val;
                cpl_id +=",";
//                val = val.replaceAll("\\s+", "");
            }

            if((cpl_id==null)||(cpl_id.length()==0))
            {
                //  System.out.println("(s.length()==0)");
                //It is not a share group
                // Initiate the stream
                StreamingOutput stream = new StreamingOutput() {

                    @Override
                    public void write(OutputStream os) throws IOException, WebApplicationException {

                        // compute result
                        Writer writer = new BufferedWriter(new OutputStreamWriter(os));

                        // write the result of the query
                        writer.write("[");
                        writer.write("\"NOT_FOUND\"");
                        writer.write("]");
                        // System.out.println("writer "+writer.toString());
                        // Convert and write the output on the stream
                        writer.flush();
                    }

                };
                // Stream result
                return Response.status(200).entity(stream).build();
            }
            else
            {
                System.out.println("cpl_id "+cpl_id);
                cpl_id = cpl_id.substring(0, cpl_id.length()-1);
                final Map<String, LinkedHashMap<String, String>> map=  pp.exportSubsidiesPolicyMeasures_timeSeries(dsBean, pd_obj, cpl_id);
                if((map==null)||(map.size()==0))
                {
                    //  System.out.println("(s.length()==0)");
                    //It is not a share group
                    // Initiate the stream
                    StreamingOutput stream = new StreamingOutput() {

                        @Override
                        public void write(OutputStream os) throws IOException, WebApplicationException {

                            // compute result
                            Writer writer = new BufferedWriter(new OutputStreamWriter(os));

                            // write the result of the query
                            writer.write("[");
                            writer.write("\"NOT_FOUND\"");
                            writer.write("]");
                            // System.out.println("writer "+writer.toString());
                            // Convert and write the output on the stream
                            writer.flush();
                        }

                    };
                    // Stream result
                    return Response.status(200).entity(stream).build();
                }
                else
                {
                    System.out.println("After exportSubsidiesPolicyMeasures_timeSeries ");
                    StreamingOutput stream = new StreamingOutput() {

                        @Override
                        public void write(OutputStream os) throws IOException, WebApplicationException {

                            // compute result
                            Writer writer = new BufferedWriter(new OutputStreamWriter(os));

                            JSONArray jsonArrayTot = new JSONArray();
                            int series_count = 1;
                            System.out.println("series_count =  "+series_count);
                            for( String key: map.keySet())
                            {
                                System.out.println("key "+key);
                                System.out.println("series_count =  "+series_count);
                                //One obj for each policy type
                                //writer.write("[");
                                JSONObject jsonobj = new JSONObject();
                                jsonobj.put("name", series_count);
                                JSONArray jsonArray = new JSONArray();
                                Set<String> keySet2= map.get(key).keySet();

                                //Three elements... Ethanol, Biodisel, Biofuel
                                for(String key2: keySet2) {
                                    String valueD = map.get(key).get(key2);
                                    if((valueD!=null)&&(valueD.length()>0))
                                    {
                                        jsonArray.add(Double.parseDouble(valueD));
                                    }
                                    else{
                                        jsonArray.add(null);
                                    }
                                }

                                jsonobj.put("data", jsonArray);
                                System.out.println(jsonArray);
                                jsonArrayTot.add(jsonobj);
                                series_count++;
                            }
                            writer.write(g.toJson(jsonArrayTot));
                            // Convert and write the output on the stream
                            writer.flush();
                        }
                    };
                    // Stream result
                    return Response.status(200).entity(stream).build();
                }
            }
    }

    //Highchart Chart
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/importTariffsPolicyMeasuresBarChart")
    public Response importTariffsPolicyMeasures_barchart(@FormParam("pdObj") String pdObject) throws Exception {
        System.out.println(pdObject);
        POLICYDataObject pd_obj = g.fromJson(pdObject, POLICYDataObject.class);
        DatasourceBean dsBean = datasourcePool.getDatasource(pd_obj.getDatasource());
        //The format of cplId is this: 'code1', 'code2', 'code3'
        final Map<String, LinkedHashMap<String, String>> map=  pp.importTariffs_barchart(dsBean, pd_obj);
//        final Map<String, LinkedHashMap<String, String>> map= new HashMap<String, LinkedHashMap<String, String>>();
        //        for( String key: map.keySet())
//        {
//            System.out.println("key "+key);
//            if(key!=null)
//            {
//                Set<String> keySet2= map.get(key).keySet();
//                for(String key2: keySet2)
//                {
//                    System.out.println("time key2 "+key2);
//                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                    Date date = df.parse(key2);
////                    Date date = new Date(key2);
//                    long epoch = date.getTime();
//                    System.out.println(epoch);
//                    System.out.println("country count key "+map.get(key).get(key2));
//                }
//            }
//        }
        /*
        * series: [{
                        name: '1',
                        data: [49, 71, 106]
                    }, {
                        name: '2',
                        data: [93, 106, 84]
                    }]*/

        final JDBCIterablePolicy it = new JDBCIterablePolicy();
        //  System.out.println("getMasterFromCplId after getMasterFromCplId");
        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

                JSONArray jsonArrayTot = new JSONArray();
                int series_count = 1;
                for( String key: map.keySet())
                {
                    //One obj for each policy type
                    //writer.write("[");
                    JSONObject jsonobj = new JSONObject();
                    jsonobj.put("name", series_count);
                    JSONArray jsonArray = new JSONArray();
                    Set<String> keySet2= map.get(key).keySet();

                    for(String key2: keySet2) {
                        String percentage = map.get(key).get(key2);
                        if((percentage!=null)&&(percentage.length()>0))
                        {
                            jsonArray.add(Double.parseDouble(percentage));
                        }
                        else{
                            jsonArray.add(null);
                        }
                    }

                    jsonobj.put("data", jsonArray);
                    // writer.write("]");
//                    writer.write("]");
                    jsonArrayTot.add(jsonobj);
                    series_count++;
                }
                writer.write(g.toJson(jsonArrayTot));
                // Convert and write the output on the stream
                writer.flush();
            }
        };
        // Stream result
        return Response.status(200).entity(stream).build();
    }

    //Policy at a Glance Functions End

    //Query and Download Functions Start
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/cplid/{datasource}")
    public Response getCplid(@PathParam("datasource") String datasource) throws Exception {

        DatasourceBean dsBean = datasourcePool.getDatasource(datasource);
        final JDBCIterablePolicy it =  pp.getcpl_id(dsBean);

//        DatasourceBean dsBean = datasourcePool.getDatasource(datasource);
//        final JDBCIterable it = fp.getODADonors(dsBean, lang);

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
    @Path("/commodityPolicyDomain/{datasource}")
    public Response getpolicyAndcommodityDomain(@PathParam("datasource") String datasource) throws Exception {
        // System.out.println("getpolicyAndcommodityDomain start");
        DatasourceBean dsBean = datasourcePool.getDatasource(datasource);
        final JDBCIterablePolicy it =  pp.getpolicyAndcommodityDomain(dsBean);
        //[2, Domestic, 1, Agricultural]

//        while(it.hasNext()) {
//            //[2006-01-01, India]
//            //System.out.println(it.next());
//            String time_country = it.next().toString();
//            String time = time_country.substring(1, time_country.indexOf(','));
//            // System.out.println("single_policy_type "+single_policy_type+" time_country= "+time_country + " time "+time );
//            if(countryCount_map.containsKey(time))

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                JSONArray jsonArray = new JSONArray();
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

                // write the result of the query
                // writer.write("[");
                while(it.hasNext()) {
                    //[2, Domestic, 1, Agricultural]
                    String val = it.next().toString();
                    int index= val.lastIndexOf(']');
                    val = val.substring(1,index);
                    val = val.replaceAll("\\s+","");
                    String array[]= val.split(",");
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("policy_domain_code", array[0]);
                    jsonObj.put("policy_domain_name", array[1]);
                    jsonObj.put("commoditydomain_code", array[2]);
                    jsonObj.put("commoditydomain_name", array[3]);
                    //  System.out.println("getpolicyAndcommodityDomain "+val);
                    //writer.write(g.toJson(val));
                    jsonArray.add(jsonObj);
//                    if (it.hasNext())
//                        writer.write(",");
                }
                // writer.write("]");
                writer.write(g.toJson(jsonArray));
                // Convert and write the output on the stream
                writer.flush();

                // compute result
//                Writer writer = new BufferedWriter(new OutputStreamWriter(os));
//
//                // write the result of the query
//                writer.write("[");
//                while(it.hasNext()) {
//                    String val = it.next().toString();
//                    System.out.println("getpolicyAndcommodityDomain "+val);
//                    writer.write(g.toJson(val));
//
//                    if (it.hasNext())
//                        writer.write(",");
//                }
//                writer.write("]");
//
//                // Convert and write the output on the stream
//                writer.flush();
            }
        };

        // Stream result
        return Response.status(200).entity(stream).build();
    }
//
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path("/startEndDate/{datasource}")
//    public Response getstartAndEndDate(@PathParam("datasource") String datasource) throws Exception {
//
//        DatasourceBean dsBean = datasourcePool.getDatasource(datasource);
//        final JDBCIterable it =  pp.getstartAndEndDate(dsBean);
//
//        // Initiate the stream
//        StreamingOutput stream = new StreamingOutput() {
//
//            @Override
//            public void write(OutputStream os) throws IOException, WebApplicationException {
//
//                // compute result
//                Writer writer = new BufferedWriter(new OutputStreamWriter(os));
//
//                // write the result of the query
//                writer.write("[");
//                while(it.hasNext()) {
//                    // String val = it.next();
//                    //  System.out.println(it.next());
//                    writer.write(g.toJson(it.next()));
//
//                    if (it.hasNext())
//                        writer.write(",");
//                }
//                writer.write("]");
//
//                // Convert and write the output on the stream
//                writer.flush();
//            }
//        };
//
//        // Stream result
//        return Response.status(200).entity(stream).build();
//    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/startEndDate/{datasource}")
    public Response getstartAndEndDate(@PathParam("datasource") String datasource) throws Exception {
        //  System.out.println("getstartAndEndDate start ");
        DatasourceBean dsBean = datasourcePool.getDatasource(datasource);
        final JDBCIterablePolicy it = pp.getEndDateIsNull(dsBean);
        // final JDBCIterable it =  pp.getstartAndEndDate(dsBean);
        boolean end_date_null = false;
        while(it.hasNext()) {
            it.next();
            end_date_null = true;
        }
        // System.out.println("end_date_null "+end_date_null);
        final boolean t = end_date_null;
        final JDBCIterablePolicy it2 =  pp.getstartAndEndDate(dsBean);
        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));
//                writer.write("[");
//                writer.write(g.toJson(t));
//                writer.write("],");

                // write the result of the query
                writer.write("[");
                writer.write("["+g.toJson(t)+"],");
                //  System.out.println("g.toJson(t) "+g.toJson(t));
//                //System.out.println("writer "+writer);
//                writer.write(",");
                while(it2.hasNext()) {
                    // String val = it.next();
                    //   System.out.println("it2 "+it2.next());
                    writer.write(g.toJson(it2.next()));

                    if (it2.hasNext())
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
    @Path("/policyTypes/{datasource}/{policyDomainCodes}/{commodityDomainCodes}")
    public Response getPolicyTypes(@PathParam("datasource") String datasource, @PathParam("policyDomainCodes") String policyDomainCodes, @PathParam("commodityDomainCodes") String commodityDomainCodes) throws Exception {
        //  System.out.println("getpolicyTypes policyDomainCodes Before  "+policyDomainCodes);
        //  System.out.println("getpolicyTypes commodityDomainCodes Before "+commodityDomainCodes);
        // compute result
//        DATASOURCE ds = DATASOURCE.POLICY;
//        DBBean db = new DBBean(ds);
        // System.out.println("getPolicyTypes policyDomainCodes "+policyDomainCodes+" commodityDomainCodes "+commodityDomainCodes);
        DatasourceBean dsBean = datasourcePool.getDatasource(datasource);
        final JDBCIterablePolicy it =  pp.getpolicyTypes(dsBean, policyDomainCodes, commodityDomainCodes);

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
    @Path("/downloadPreview")
    public Response getDownloadPreview(@FormParam("pdObj") String pdObject) throws Exception {
        //  System.out.println(" getDownloadPreview start");
        //Gson g = new Gson();
        //  System.out.println(" getDownloadPreview after gson");
        POLICYDataObject pd_obj = g.fromJson(pdObject, POLICYDataObject.class);
//        System.out.println(" getDownloadPreview after class");
//        System.out.println(" pd_obj datasource "+pd_obj.getDatasource());
//        System.out.println(" pd_obj policy_domain_code "+pd_obj.getPolicy_domain_code());
//        System.out.println(" pd_obj commodity_domain_code "+pd_obj.getCommodity_domain_code());
//        System.out.println(" pd_obj commodity_class_code "+pd_obj.getCommodity_class_code());
//        System.out.println(" pd_obj policy_type_code "+pd_obj.getPolicy_type_code());
//        for(int i=0; i<pd_obj.getPolicy_type_code().length;i++)
//        {
//            System.out.println(" pd_obj policy_type_code i "+i+" ="+pd_obj.getPolicy_type_code()[i]);
//        }
//        System.out.println(" pd_obj policy_measure_code "+pd_obj.getPolicy_measure_code());
//        for(int i=0; i<pd_obj.getPolicy_measure_code().length;i++)
//        {
//            System.out.println(" pd_obj policy_measure_code i "+i+" ="+pd_obj.getPolicy_measure_code()[i]);
//        }
//        System.out.println(" pd_obj country_code "+pd_obj.getCountry_code());
//        System.out.println(" pd_obj yearTab "+pd_obj.getYearTab());
//        System.out.println(" pd_obj year_list "+pd_obj.getYear_list());
//        System.out.println(" pd_obj start_date "+pd_obj.getStart_date());
//        System.out.println(" pd_obj end_date "+pd_obj.getEnd_date());
        DatasourceBean dsBean = datasourcePool.getDatasource(pd_obj.getDatasource());
        // compute result
//        DATASOURCE ds = DATASOURCE.POLICY;
//        DBBean db = new DBBean(ds);

//        DatasourceBean dsBean = datasourcePool.getDatasource(datasource);
//        Gson g = new Gson();
        //g.toJsonTree(qvo);
        //System.out.println("FIRST ");
        // AMISQueryVO vo = g.fromJson(qvo, AMISQueryVO.class);
        boolean with_commodity_id = false;
        final JDBCIterablePolicy it =  pp.getDownloadPreview(dsBean, pd_obj, with_commodity_id);
        // System.out.println("after pp.getDownloadPreview");
        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

                // write the result of the query
                writer.write("[");
                while(it.hasNext()) {
                    // System.out.println("in loop next");
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
    @Path("/streamexcel")
    //'AllData'= Master+Policy tables
    public Response streamExcel(final @FormParam("datasource_WQ") String datasource,
                                final @FormParam("json_WQ") String json,
                                final @FormParam("cssFilename_WQ") String cssFilename,
                                final @FormParam("valueIndex_WQ") String valueIndex,
                                final @FormParam("thousandSeparator_WQ") String thousandSeparator,
                                final @FormParam("decimalSeparator_WQ") String decimalSeparator,
                                final @FormParam("decimalNumbers_WQ") String decimalNumbers,
                                final @FormParam("quote_WQ") String quote,
                                final @FormParam("title_WQ") String title,
                                final @FormParam("subtitle_WQ") String subtitle) {
        //  System.out.println("streamExcel start ");
        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                //  Gson g = new Gson();
//                SQLBean sql = g.fromJson(json, SQLBean.class);
//                DatasourceBean db = datasourcePool.getDatasource(datasource.toUpperCase());
                   System.out.println("streamExcel 1 ");
                POLICYDataObject pd_obj = g.fromJson(json, POLICYDataObject.class);
                System.out.println("streamExcel 2 ");
                System.out.println(" pd_obj datasource "+pd_obj.getDatasource());
                System.out.println(" pd_obj policy_domain_code "+pd_obj.getPolicy_domain_code());
                System.out.println(" pd_obj commodity_domain_code "+pd_obj.getCommodity_domain_code());
                System.out.println(" pd_obj commodity_class_code "+pd_obj.getCommodity_class_code());
                System.out.println(" pd_obj policy_type_code "+pd_obj.getPolicy_type_code());
                for(int i=0; i<pd_obj.getPolicy_type_code().length;i++)
                {
                    System.out.println(" pd_obj policy_type_code i "+i+" ="+pd_obj.getPolicy_type_code()[i]);
                }
                System.out.println(" pd_obj policy_measure_code len  "+pd_obj.getPolicy_measure_code().length);
                for(int i=0; i<pd_obj.getPolicy_measure_code().length;i++)
                {
                    System.out.println(" pd_obj policy_measure_code i "+i+" ="+pd_obj.getPolicy_measure_code()[i]);
                }
                System.out.println(" pd_obj country_code "+pd_obj.getCountry_code());
                System.out.println(" pd_obj yearTab "+pd_obj.getYearTab());
                System.out.println(" pd_obj year_list "+pd_obj.getYear_list());
                System.out.println(" pd_obj start_date "+pd_obj.getStart_date());
                System.out.println(" pd_obj end_date "+pd_obj.getEnd_date());

                DatasourceBean dsBean = datasourcePool.getDatasource(pd_obj.getDatasource());

                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

                // compute result
                JDBCIterablePolicy it = new JDBCIterablePolicy();

                try {
                    //Master + Policy
                    it =  pp.getDownloadExport(dsBean, pd_obj);

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    WDSExceptionStreamWriter.streamException(os, ("Method 'streamExcel' thrown an error: " + e.getMessage()));
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    WDSExceptionStreamWriter.streamException(os, ("Method 'streamExcel' thrown an error: " + e.getMessage()));
                } catch (SQLException e) {
                    e.printStackTrace();
                    WDSExceptionStreamWriter.streamException(os, ("Method 'streamExcel' thrown an error: " + e.getMessage()));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    WDSExceptionStreamWriter.streamException(os, ("Method 'streamExcel' thrown an error: " + e.getMessage()));
                } catch (Exception e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getDomains' thrown an error: " + e.getMessage()));
                }

//                String[] headerArray = {"metadata_id","policy_id","cpl_id","commodity_id","hs_version","hs_code","hs_suffix","commodity_description","shared_group_code","location_condition","start_date","end_date","units","value","value_text","exemptions","value_calculated","notes","link","source","title_of_notice","legal_basis_name","measure_descr","short_description","original_dataset","type_of_change_name","type_of_change_code","product_original_hs","product_original_name","policy_element","impl","second_generation_specific","imposed_end_date", "benchmark_tax", "benchmark_product", "tax_rate_biofuel", "tax_rate_benchmark", "start_date_tax", "source_benchmark", "date_of_publication", "xs_yeartype", "notes_datepub", "country_code", "country_name", "subnational_code", "subnational_name", "commoditydomain_code", "commoditydomain_name", "commodityclass_code", "commodityclass_name", "policydomain_code", "policydomain_name", "policytype_code", "policytype_name", "policymeasure_code", "policymeasure_name", "condition"};
                //String[] headerArray = {"metadata_id","policy_id","cpl_id","country_code", "country_name", "subnational_code", "subnational_name", "commoditydomain_code", "commoditydomain_name", "commodityclass_code", "commodityclass_name", "policydomain_code", "policydomain_name", "policytype_code", "policytype_name", "policymeasure_code", "policymeasure_name", "condition","commodity_id","hs_version","hs_code","hs_suffix","commodity_description","shared_group_code","location_condition","start_date","end_date","units","value","value_text","exemptions","value_calculated","notes","link","source","title_of_notice","legal_basis_name","measure_descr","short_description","original_dataset","type_of_change_name","type_of_change_code","product_original_hs","product_original_name","policy_element","impl","second_generation_specific","imposed_end_date", "benchmark_tax", "benchmark_product", "tax_rate_biofuel", "tax_rate_benchmark", "start_date_tax", "source_benchmark", "date_of_publication", "xs_yeartype", "notes_datepub"};

//                String[] headerArray = {"metadata_id","policy_id","cpl_id","cpl_code","country_code", "country_name", "subnational_code", "subnational_name", "commoditydomain_code", "commoditydomain_name", "commodityclass_code", "commodityclass_name", "policydomain_code", "policydomain_name", "policytype_code", "policytype_name", "policymeasure_code", "policymeasure_name", "condition_code", "condition", "individualpolicy_code", "individualpolicy_name", "commodity_id","hs_version","hs_code","hs_suffix", "short_description", "shared_group_code", "policy_element", "start_date","end_date", "units","value","value_text", "value_type", "exemptions", "location_condition", "notes", "link","source","title_of_notice", "legal_basis_name", "date_of_publication", "imposed_end_date", "second_generation_specific", "benchmark_tax", "benchmark_product", "tax_rate_biofuel", "tax_rate_benchmark", "start_date_tax", "benchmark_link", "original_dataset", "type_of_change_code", "type_of_change_name", "measure_descr", "product_original_hs","product_original_name", "implementationprocedure","xs_yeartype", "link_pdf", "benchmark_link_pdf"};
                //Before removing policy_id
//                String[] headerArray = {"policy_id","cpl_id","cpl_code","country_code", "country_name", "subnational_code", "subnational_name", "commoditydomain_code", "commoditydomain_name", "commodityclass_code", "commodityclass_name", "policydomain_code", "policydomain_name", "policytype_code", "policytype_name", "policymeasure_code", "policymeasure_name", "condition_code", "condition", "individualpolicy_code", "individualpolicy_name", "commodity_id","hs_version","hs_code","hs_suffix", "short_description", "shared_group_code", "policy_element", "start_date","end_date", "units","value","value_text", "value_type", "exemptions", "location_condition", "notes", "link","source","title_of_notice", "legal_basis_name", "date_of_publication", "imposed_end_date", "second_generation_specific", "benchmark_tax", "benchmark_product", "tax_rate_biofuel", "tax_rate_benchmark", "start_date_tax", "benchmark_link", "original_dataset", "type_of_change_code", "type_of_change_name", "measure_descr", "product_original_hs","product_original_name", "implementationprocedure","xs_yeartype", "link_pdf", "benchmark_link_pdf"};
                //After removing policy_id
                //String[] headerArray = {"cpl_id","cpl_code","country_code", "country_name", "subnational_code", "subnational_name", "commoditydomain_code", "commoditydomain_name", "commodityclass_code", "commodityclass_name", "policydomain_code", "policydomain_name", "policytype_code", "policytype_name", "policymeasure_code", "policymeasure_name", "condition_code", "condition", "individualpolicy_code", "individualpolicy_name", "commodity_id","hs_version","hs_code","hs_suffix", "short_description", "shared_group_code", "policy_element", "start_date","end_date", "units","value","value_text", "value_type", "exemptions", "location_condition", "notes", "link","source","title_of_notice", "legal_basis_name", "date_of_publication", "imposed_end_date", "second_generation_specific", "benchmark_tax", "benchmark_product", "tax_rate_biofuel", "tax_rate_benchmark", "start_date_tax", "benchmark_link", "original_dataset", "type_of_change_code", "type_of_change_name", "measure_descr", "product_original_hs","product_original_name", "implementationprocedure","xs_yeartype", "link_pdf", "benchmark_link_pdf"};
                //New order
                String[] headerArray = {"policy_id","cpl_id", "country_name", "subnational_name", "commoditydomain_name", "commodityclass_name", "policydomain_name", "policytype_name", "policymeasure_name", "condition", "individualpolicy_name", "commodity_id","hs_version","hs_code","hs_suffix", "short_description", "description", "shared_group_code", "policy_element", "start_date","end_date", "units","value","value_text", "value_type", "exemptions", "location_condition", "notes", "link","source","title_of_notice", "legal_basis_name", "date_of_publication", "imposed_end_date", "second_generation_specific", "benchmark_tax", "benchmark_product", "tax_rate_biofuel", "tax_rate_benchmark", "start_date_tax", "benchmark_link", "original_dataset", "type_of_change_name", "measure_descr", "product_original_hs","product_original_name", "implementationprocedure","xs_yeartype", "link_pdf", "benchmark_link_pdf","cpl_code","country_code","subnational_code","commoditydomain_code","commodityclass_code","policydomain_code","policytype_code","policymeasure_code","condition_code","individualpolicy_code","type_of_change_code"};

                // write the result of the query
                writer.write("<html><head><meta charset=\"UTF-8\"></head><body>");
//                <html>
//                <head>
//                <meta charset="UTF-8">
//                </head>
//                <body>
//                </body>
//                </html>

                writer.write("<table border=\"1\">");
                // System.out.println("Table");
                writer.write("<tr>");
                for (int i = 0; i < headerArray.length; i++) {
                    writer.write("<td>");
                    writer.write(headerArray[i]);
                    writer.write("</td>");
                }
                writer.write("</tr>");
                //   System.out.println("it.hasNext() "+it.hasNext());
                while(it.hasNext()) {

                    List<String> l = it.next();
                    // System.out.println("loop it.hasNext() "+it.hasNext()+" "+l.size());
                    writer.write("<tr>");
                    for (int i = 0; i < l.size(); i++) {
                        if(i==16)
                        {
                            //Description.... no wrap
                            writer.write("<td nowrap>");
                        }
                        else{
                            writer.write("<td>");
                        }
                        //writer.write("<td>");
                        if((l==null)||(l.isEmpty())||(l.get(i)==null))
                        {
                            writer.write("");
                        }
                        else
                        {
                            writer.write(l.get(i));
                        }
                        writer.write("</td>");
                    }
                    writer.write("</tr>");
                }
                writer.write("</table>");
                writer.write("</body></html>");
                //System.out.println("Before writer");
                // System.out.println(writer.toString());
                // Convert and write the output on the stream
                writer.flush();
                writer.close();
            }

        };

        // Wrap result
        Response.ResponseBuilder builder = Response.ok(stream);
//        builder.header("Access-Control-Allow-Origin", "*");
//        builder.header("Access-Control-Max-Age", "3600");
//        builder.header("Access-Control-Allow-Methods", "POST");
//        builder.header("Access-Control-Allow-Headers", "X-Requested-With, Host, User-Agent, Accept, Accept-Language, Accept-Encoding, Accept-Charset, Keep-Alive, Connection, Referer,Origin");
//        builder.header("Content-Disposition", "attachment; filename=" + UUID.randomUUID().toString() + ".xls");

        builder.header("Content-Disposition", "attachment; filename= PolicyData_" + UUID.randomUUID().toString() + ".xls");
        builder.header("Content-type",  "application/vnd.ms-excel; charset=UTF-8");
        // Stream Excel
        //System.out.println("Before build");
        return builder.build();

    }

    @POST
    @Path("/streamexcel2")
    // 'ShareGroupData' = Share Group information
    public Response streamExcel2(final @FormParam("datasource_WQ2") String datasource,
                                 final @FormParam("json_WQ2") String json,
                                 final @FormParam("cssFilename_WQ2") String cssFilename,
                                 final @FormParam("valueIndex_WQ2") String valueIndex,
                                 final @FormParam("thousandSeparator_WQ2") String thousandSeparator,
                                 final @FormParam("decimalSeparator_WQ2") String decimalSeparator,
                                 final @FormParam("decimalNumbers_WQ2") String decimalNumbers,
                                 final @FormParam("quote_WQ2") String quote,
                                 final @FormParam("title_WQ2") String title,
                                 final @FormParam("subtitle_WQ2") String subtitle) {
        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                POLICYDataObject pd_obj = g.fromJson(json, POLICYDataObject.class);

                DatasourceBean dsBean = datasourcePool.getDatasource(pd_obj.getDatasource());

                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

                // compute result
                JDBCIterablePolicy it = new JDBCIterablePolicy();
                Map<String, Map<String, LinkedList<String>>> commodity_info = new  LinkedHashMap<String, Map<String, LinkedList<String>>>();
                try {
                    //Share Group
                    commodity_info =  pp.getDownloadShareGroupExport(dsBean, pd_obj);

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    WDSExceptionStreamWriter.streamException(os, ("Method 'streamExcel' thrown an error: " + e.getMessage()));
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    WDSExceptionStreamWriter.streamException(os, ("Method 'streamExcel' thrown an error: " + e.getMessage()));
                } catch (SQLException e) {
                    e.printStackTrace();
                    WDSExceptionStreamWriter.streamException(os, ("Method 'streamExcel' thrown an error: " + e.getMessage()));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    WDSExceptionStreamWriter.streamException(os, ("Method 'streamExcel' thrown an error: " + e.getMessage()));
                } catch (Exception e) {
                    WDSExceptionStreamWriter.streamException(os, ("Method 'getDomains' thrown an error: " + e.getMessage()));
                }

//                String[] headerArray = {"metadata_id","policy_id","cpl_id","commodity_id","hs_version","hs_code","hs_suffix","commodity_description","shared_group_code","location_condition","start_date","end_date","units","value","value_text","exemptions","value_calculated","notes","link","source","title_of_notice","legal_basis_name","measure_descr","short_description","original_dataset","type_of_change_name","type_of_change_code","product_original_hs","product_original_name","policy_element","impl","second_generation_specific","imposed_end_date", "benchmark_tax", "benchmark_product", "tax_rate_biofuel", "tax_rate_benchmark", "start_date_tax", "source_benchmark", "date_of_publication", "xs_yeartype", "notes_datepub", "country_code", "country_name", "subnational_code", "subnational_name", "commoditydomain_code", "commoditydomain_name", "commodityclass_code", "commodityclass_name", "policydomain_code", "policydomain_name", "policytype_code", "policytype_name", "policymeasure_code", "policymeasure_name", "condition"};
                // String[] headerArray = {"commodity_id","target_code","description"};
//                String[] headerArray = {"hs_code", "hs_suffix", "hs_version", "short_description", "commodity_id", "shared_group_code"};
                String[] headerArray = {"shared_group_commodity_id", "shared_group_code", "commodity_id", "hs_code", "hs_suffix", "hs_version", "short_description", "original_hs_version", "original_hs_code", "original_hs_suffix"};
                // write the result of the query
                writer.write("<html><head><meta charset=\"UTF-8\"></head><body>");
                writer.write("<table border=\"1\">");

                writer.write("<tr>");
                for (int i = 0; i < headerArray.length; i++) {
                    writer.write("<td>");
                    writer.write(headerArray[i]);
                    writer.write("</td>");
                }
                writer.write("</tr>");
                //All the share group
                Set<String> keySet = commodity_info.keySet();
                for(String key: keySet)
                {
                    //For each share group
                    Set<String> keySet2 = commodity_info.get(key).keySet();
                    for(String key2: keySet2)
                    {
                        //For each commodity of each share group
                        LinkedList<String> l = commodity_info.get(key).get(key2);
                        writer.write("<tr>");
                        //Share group ... commodity id
                        writer.write("<td>");
                        if((key==null)||(key.isEmpty()))
                        {
                            writer.write("");
                        }
                        else
                        {
                            writer.write(key);
                        }
                        writer.write("</td>");
                        //Share group ... code
                        writer.write("<td>");
                        if((l==null)||(l.isEmpty())||(l.get(0)==null))
                        {
                            writer.write("");
                        }
                        else
                        {
                            writer.write(l.get(0));
                        }
                        writer.write("</td>");
                        //Commodity ... commodity id
                        writer.write("<td>");
                        if((key2==null)||(key2.isEmpty()))
                        {
                            writer.write("");
                        }
                        else
                        {
                            writer.write(key2);
                        }
                        writer.write("</td>");
                        for (int i = 1; i < l.size(); i++) {
                            writer.write("<td>");
                            if((l==null)||(l.isEmpty())||(l.get(i)==null))
                            {
                                writer.write("");
                            }
                            else
                            {
                                writer.write(l.get(i));
                            }
                            writer.write("</td>");
                        }
                        writer.write("</tr>");
                    }
                }
//                for(String key: keySet)
//                {
//                    LinkedList<String> l = commodity_info.get(key);
//                    writer.write("<tr>");
////                    writer.write("<td>");
////                    if(key==null)
////                    {
////                        writer.write("");
////                    }
////                    else
////                    {
////                        writer.write(key);
////                    }
////                    writer.write("</td>");
//                    for (int i = 0; i < l.size(); i++) {
//                        writer.write("<td>");
//                        if((l==null)||(l.isEmpty())||(l.get(i)==null))
//                        {
//                            writer.write("");
//                        }
//                        else
//                        {
//                            writer.write(l.get(i));
//                        }
//                        writer.write("</td>");
//                    }
//                    writer.write("</tr>");
//                }
                writer.write("</table>");
                writer.write("</body></html>");

                // System.out.println(writer.toString());

                // Convert and write the output on the stream
                writer.flush();
                writer.close();
            }

        };

        // Wrap result
        Response.ResponseBuilder builder = Response.ok(stream);
        builder.header("Content-Disposition", "attachment; filename= SharedGroupData_" + UUID.randomUUID().toString() + ".xls");
        builder.header("Content-type",  "application/vnd.ms-excel; charset=UTF-8");

        // Stream Excel
        return builder.build();

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/masterFromCplId")
    public Response getMasterFromCplId(@FormParam("pdObj") String pdObject) throws Exception {
        // System.out.println("getMasterFromCplId start ");
        POLICYDataObject pd_obj = g.fromJson(pdObject, POLICYDataObject.class);
        DatasourceBean dsBean = datasourcePool.getDatasource(pd_obj.getDatasource());
        //The format of cplId is this: 'code1', 'code2', 'code3'
        final JDBCIterablePolicy it =  pp.getMasterFromCplId(dsBean, pd_obj);
        //  System.out.println("getMasterFromCplId after getMasterFromCplId");
        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

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
    @Path("/shareGroupInfo/{datasource}/{commodity_id}")
    public Response getShareGroupInfo(@PathParam("datasource") String datasource, @PathParam("commodity_id") String commodityId) throws Exception {
        // System.out.println("getShareGroupInfo cplId Before  "+commodityId);

        // compute result
//        DATASOURCE ds = DATASOURCE.POLICY;
//        DBBean db = new DBBean(ds);

        DatasourceBean dsBean = datasourcePool.getDatasource(datasource);
        // commodityId = "169";
        final JDBCIterablePolicy it =  pp.getshareGroupInfo(dsBean, commodityId);
        //final JDBCIterablePolicy it =  pp.getshareGroupInfo(dsBean, "169");

        while(it.hasNext()) {
            //Get the share group code associated to the commodity_id
            // String share_group_code = g.toJson(it.next());
            String valueshare_group_code = g.toJson(it.next());
            String share_group_code = valueshare_group_code.substring(1,(valueshare_group_code.length()-1));
            // System.out.println(" share_group_code *"+share_group_code+"*");
            //  System.out.println(" share_group_code "+share_group_code);
            if((share_group_code!=null)&&(!share_group_code.equals("none"))&&(!share_group_code.equals("\"none\"")))
            {
                //Get the list of commodity that belongs to this share_group_code
                final JDBCIterablePolicy it2 = pp.getSingleIdFromCommodityId(dsBean, commodityId);
                //In the Share Group Table show the info of the share group and the each commodity that belong to the share group
//                String s = ""+commodityId+",";
                String s ="";
                while(it2.hasNext()) {
                    //From [289] to 289
                    List<String> valueList = it2.next();
                    //  System.out.println("valueList "+valueList.toString());
                    //There is only one value
                    String value = valueList.toString();
//                    value = value.substring(2, value.lastIndexOf("\""));
//                   s+= "'"+g.toJson(it2.next())+"'";
//                    s+= "'"+it2.next()+"'";

                    s+= value.substring(1,(value.length()-1));
                    //  System.out.println("value "+value+" new value "+s);
                    if (it2.hasNext())
                        s+=",";
                }
                // System.out.println("After getSingleIdFromCommodityId s = "+s);
                if(s.length()==0)
                {
                    //  System.out.println("(s.length()==0)");
                    //It is not a share group
                    // Initiate the stream
                    StreamingOutput stream = new StreamingOutput() {

                        @Override
                        public void write(OutputStream os) throws IOException, WebApplicationException {

                            // compute result
                            Writer writer = new BufferedWriter(new OutputStreamWriter(os));

                            // write the result of the query
                            writer.write("[");
                            writer.write("\"NOT_FOUND\"");
                            writer.write("]");
                            // System.out.println("writer "+writer.toString());
                            // Convert and write the output on the stream
                            writer.flush();
                        }

                    };
                    // Stream result
                    return Response.status(200).entity(stream).build();
                }
                else
                {
                    //  System.out.println("(s.length()!0)");
                    //Get commodity_id, target_code, description associated with the commodityId
                    final JDBCIterablePolicy it3 = pp.getCommodityInfo(dsBean, s);
                    // Initiate the stream
                    StreamingOutput stream = new StreamingOutput() {

                        @Override
                        public void write(OutputStream os) throws IOException, WebApplicationException {

                            // compute result
                            Writer writer = new BufferedWriter(new OutputStreamWriter(os));

                            // write the result of the query
                            writer.write("[");
                            while(it3.hasNext()) {
                                //Without saving commodity_id
                                // String commodity_id = it3.next().toString();
                                writer.write(g.toJson(it3.next()));
                                if (it3.hasNext())
                                    writer.write(",");
                            }
                            writer.write("]");
                            // Convert and write the output on the stream
                            writer.flush();
                            // Stream result
                        }

                    };
                    //  System.out.println("stream "+stream);
                    return Response.status(200).entity(stream).build();
                }
            }
            else{
                //It is not a share group
                // Initiate the stream
                StreamingOutput stream = new StreamingOutput() {

                    @Override
                    public void write(OutputStream os) throws IOException, WebApplicationException {

                        // compute result
                        Writer writer = new BufferedWriter(new OutputStreamWriter(os));

                        // write the result of the query
                        writer.write("[");
                        writer.write("\"NOT_FOUND\"");
                        writer.write("]");

                        // Convert and write the output on the stream
                        writer.flush();
                    }

                };
                // Stream result
                return Response.status(200).entity(stream).build();
            }
        }
        //It never arrives here
        return Response.status(200).entity("").build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/downloadPreviewPolicyTable")
    public Response getDownloadPreviewPolicyTable(@FormParam("pdObj") String pdObject) throws Exception {
        //  System.out.println(" getDownloadPreviewPolicyTable start");

        POLICYDataObject pd_obj = g.fromJson(pdObject, POLICYDataObject.class);
//        System.out.println(" getDownloadPreviewPolicyTable after class");
//        System.out.println(" pd_obj datasource "+pd_obj.getDatasource());
//        System.out.println(" pd_obj policy_domain_code "+pd_obj.getPolicy_domain_code());
//        System.out.println(" pd_obj commodity_domain_code "+pd_obj.getCommodity_domain_code());
//        System.out.println(" pd_obj commodity_class_code "+pd_obj.getCommodity_class_code());
//        System.out.println(" pd_obj policy_type_code "+pd_obj.getPolicy_type_code());
//        for(int i=0; i<pd_obj.getPolicy_type_code().length;i++)
//        {
//            System.out.println(" pd_obj policy_type_code i "+i+" ="+pd_obj.getPolicy_type_code()[i]);
//        }
//        System.out.println(" pd_obj policy_measure_code "+pd_obj.getPolicy_measure_code());
//        for(int i=0; i<pd_obj.getPolicy_measure_code().length;i++)
//        {
//            System.out.println(" pd_obj policy_measure_code i "+i+" ="+pd_obj.getPolicy_measure_code()[i]);
//        }
//        System.out.println(" pd_obj country_code "+pd_obj.getCountry_code());
//        System.out.println(" pd_obj yearTab "+pd_obj.getYearTab());
//        System.out.println(" pd_obj year_list "+pd_obj.getYear_list());
//        System.out.println(" pd_obj start_date "+pd_obj.getStart_date());
//        System.out.println(" pd_obj end_date "+pd_obj.getEnd_date());
        DatasourceBean dsBean = datasourcePool.getDatasource(pd_obj.getDatasource());

        final JDBCIterablePolicy it =  pp.getPolicyFromCplId(dsBean, pd_obj);
        //  System.out.println("after pp.getDownloadPreviewPolicyTable");
        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));
                int i=0;
                // write the result of the query
                writer.write("[");
                while(it.hasNext()) {
                    //  System.out.println("in loop next i="+i+" elem "+it.next());

                    writer.write(g.toJson(it.next()));
                    if (it.hasNext())
                        writer.write(",");
                    i++;
                }
                writer.write("]");

                //Convert and write the output on the stream
                writer.flush();
            }
        };
        // Stream result
        return Response.status(200).entity(stream).build();
    }
    //Query and Download Functions End
}