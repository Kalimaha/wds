package org.fao.fenix.wds.web.rest.amis.policies;

import org.fao.fenix.wds.core.bean.DBBean;
import org.fao.fenix.wds.core.bean.SQLBean;
import org.fao.fenix.wds.core.bean.WhereBean;
import org.fao.fenix.wds.core.constant.DATASOURCE;
import org.fao.fenix.wds.core.constant.SQL;
import org.fao.fenix.wds.core.constant.WHERE;
import org.fao.fenix.wds.core.exception.WDSException;
import org.fao.fenix.wds.core.jdbc.JDBCConnector;
import org.fao.fenix.wds.core.sql.amis.policies.SQLBeansRepository;
import org.fao.fenix.wds.core.utils.Wrapper;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@Path("/amis/policies/codes")
public class AmisPoliciesCodingSystemRESTService {

    //http://localhost:8080/wds/rest/amis/policies/codes/country
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{codingsystem}")
    public Response getCodes(@PathParam("codingsystem") String codingsystem) {

        try {
            DATASOURCE ds = DATASOURCE.AMISPOLICIES;
            DBBean db = new DBBean(ds);
            AmisPoliciesCodingSystemConstants c = null;
            SQLBean sql =null;
            try {
                c = AmisPoliciesCodingSystemConstants.valueOf(codingsystem.toUpperCase());
            }catch(Exception e){}

//			System.out.println("coding system type: " +c);
            if ( c != null) {
                switch (c) {
                    case START_DATE: sql = SQLBeansRepository.getStartDateYears("\"MasterTable\"", new ArrayList<WhereBean>()); break;
                    case END_DATE: sql = SQLBeansRepository.getEndDateYears("\"MasterTable\"", new ArrayList<WhereBean>()); break;
                    case COUNTRY: sql = SQLBeansRepository.getCountries("\"MasterTable\"", new ArrayList<WhereBean>()); break;
                    case POLICY_DOMAIN: sql = SQLBeansRepository.getPolicyDomains("\"MasterTable\"", new ArrayList<WhereBean>()); break;
                    case MEASURE_TYPE: sql = SQLBeansRepository.getMeasureTypes("\"MasterTable\"", new ArrayList<WhereBean>()); break;
                    case POLICY_MEASURE: sql = SQLBeansRepository.getPolicyMeasures("\"MasterTable\"", new ArrayList<WhereBean>()); break;
                    case SUB_POLICY_MEASURE: sql = SQLBeansRepository.getSubPolicyMeasures("\"MasterTable\"", new ArrayList<WhereBean>()); break;
                    case COMMODITY: sql = SQLBeansRepository.getCommodities("\"MasterTable\"", new ArrayList<WhereBean>()); break;
                }
            }

//			System.out.println(Bean2SQL.convert(sql));
            List<List<String>> table = JDBCConnector.query(db, sql, true);
            // to remove the headers (TODO: add it in the wrapper?)
            table.remove(0);
            String json = Wrapper.wrapAsJSON(table).toString();

            // wrap result
            Response.ResponseBuilder builder = Response.ok(json);
            builder.header("Access-Control-Allow-Origin", "*");
            builder.header("Access-Control-Max-Age", "3600");
            builder.header("Access-Control-Allow-Methods", "GET");
            builder.header("Access-Control-Allow-Headers", "X-Requested-With,Host,User-Agent,Accept,Accept-Language,Accept-Encoding,Accept-Charset,Keep-Alive,Connection,Referer,Origin");
            builder.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON+"; charset=utf-8");

            // return response
            return builder.build();

        } catch (WDSException e) {
            return Response.status(500).entity("Error in 'amis policies Coding System service: " + e.getMessage()).build();
        } catch (ClassNotFoundException e) {
            return Response.status(500).entity("Error in 'amis policies Coding System service: " + e.getMessage()).build();
        } catch (SQLException e) {
            return Response.status(500).entity("Error in 'amis policies Coding System service: " + e.getMessage()).build();
        } catch (InstantiationException e) {
            return Response.status(500).entity("Error in 'amis policies Coding System service: " + e.getMessage()).build();
        } catch (IllegalAccessException e) {
            return Response.status(500).entity("Error in 'amis policies Coding System service: " + e.getMessage()).build();
        }
    }

    //http://localhost:8080/wds/rest/amis/policies/codes/country/hierarchy
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{codingsystem}/hierarchy")
    public Response getCodesHierarchy(@PathParam("codingsystem") String codingsystem) {

        try {
            DATASOURCE ds = DATASOURCE.AMISPOLICIES;
            DBBean db = new DBBean(ds);
            AmisPoliciesCodingSystemConstants c = null;
            SQLBean sql =null;
            try {
                c = AmisPoliciesCodingSystemConstants.valueOf(codingsystem.toUpperCase());
            }catch(Exception e){}

//            System.out.println("coding system type: " +c);
            if ( c != null) {
                switch (c) {
                     case COMMODITY: sql = SQLBeansRepository.getCommoditiesHierarchyOriginal("\"MasterTable\"", new ArrayList<WhereBean>()); break;
                }
            }

//            System.out.println(Bean2SQL.convert(sql));
            List<List<String>> table = JDBCConnector.query(db, sql, true);
            // to remove the headers (TODO: add it in the wrapper?)
           // table.remove(0);
            String json = Wrapper.wrapAsJSON(table).toString();

            // wrap result
            Response.ResponseBuilder builder = Response.ok(json);
            builder.header("Access-Control-Allow-Origin", "*");
            builder.header("Access-Control-Max-Age", "3600");
            builder.header("Access-Control-Allow-Methods", "GET");
            builder.header("Access-Control-Allow-Headers", "X-Requested-With,Host,User-Agent,Accept,Accept-Language,Accept-Encoding,Accept-Charset,Keep-Alive,Connection,Referer,Origin");
            builder.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON+"; charset=utf-8");

            // return response
            return builder.build();

        } catch (WDSException e) {
            return Response.status(500).entity("Error in 'amis policies Coding System service: " + e.getMessage()).build();
        } catch (ClassNotFoundException e) {
            return Response.status(500).entity("Error in 'amis policies Coding System service: " + e.getMessage()).build();
        } catch (SQLException e) {
            return Response.status(500).entity("Error in 'amis policies Coding System service: " + e.getMessage()).build();
        } catch (InstantiationException e) {
            return Response.status(500).entity("Error in 'amis policies Coding System service: " + e.getMessage()).build();
        } catch (IllegalAccessException e) {
            return Response.status(500).entity("Error in 'amis policies Coding System service: " + e.getMessage()).build();
        }
    }

    //http://localhost:8080/wds/rest/amis/policies/codes/country/hierarchy/filter
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{codingsystem}/hierarchy/filter/{countrycodes}/{commoditycodes}/{policydomaincodes}/{measuretypecodes}/{policymeasurecodes}/{subpolicymeasurecodes}")
    public Response getCodesHierarchyFiltered(
            @PathParam("codingsystem") String codingsystem,
            @PathParam("countrycodes") String countrycodes,
            @PathParam("commoditycodes") String commoditycodes,
            @PathParam("policydomaincodes") String policydomaincodes,
            @PathParam("measuretypecodes") String measuretypecodes,
            @PathParam("policymeasurecodes") String policymeasurecodes,
            @PathParam("subpolicymeasurecodes") String subpolicymeasurecodes )
    {

        try {
            DATASOURCE ds = DATASOURCE.AMISPOLICIES;
            DBBean db = new DBBean(ds);
            AmisPoliciesCodingSystemConstants c = null;
            SQLBean sql =null;
            try {
                c = AmisPoliciesCodingSystemConstants.valueOf(codingsystem.toUpperCase());
            }catch(Exception e){}


//            System.out.println("coding system type: " +c);
            if ( c != null) {
                switch (c) {
                  case COMMODITY: sql = SQLBeansRepository.getCommoditiesHierarchy("\"MasterTable\"", countrycodes, commoditycodes, policydomaincodes, measuretypecodes, policymeasurecodes, subpolicymeasurecodes); break;
                }
            }

//            System.out.println(Bean2SQL.convert(sql));
            List<List<String>> table = JDBCConnector.query(db, sql, true);
            // to remove the headers (TODO: add it in the wrapper?)
           // table.remove(0);
            String json = Wrapper.wrapAsJSON(table).toString();

            // wrap result
            Response.ResponseBuilder builder = Response.ok(json);
            builder.header("Access-Control-Allow-Origin", "*");
            builder.header("Access-Control-Max-Age", "3600");
            builder.header("Access-Control-Allow-Methods", "GET");
            builder.header("Access-Control-Allow-Headers", "X-Requested-With,Host,User-Agent,Accept,Accept-Language,Accept-Encoding,Accept-Charset,Keep-Alive,Connection,Referer,Origin");
            builder.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON+"; charset=utf-8");

            // return response
            return builder.build();

        } catch (WDSException e) {
            return Response.status(500).entity("Error in 'amis policies Coding System service: " + e.getMessage()).build();
        } catch (ClassNotFoundException e) {
            return Response.status(500).entity("Error in 'amis policies Coding System service: " + e.getMessage()).build();
        } catch (SQLException e) {
            return Response.status(500).entity("Error in 'amis policies Coding System service: " + e.getMessage()).build();
        } catch (InstantiationException e) {
            return Response.status(500).entity("Error in 'amis policies Coding System service: " + e.getMessage()).build();
        } catch (IllegalAccessException e) {
            return Response.status(500).entity("Error in 'amis policies Coding System service: " + e.getMessage()).build();
        }
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{codingsystem}/filter/{countrycodes}/{commoditycodes}/{policydomaincodes}/{measuretypecodes}/{policymeasurecodes}/{subpolicymeasurecodes}")
    public Response getCodesFiltered(
            @PathParam("codingsystem") String codingsystem,
            @PathParam("countrycodes") String countrycodes,
            @PathParam("commoditycodes") String commoditycodes,
            @PathParam("policydomaincodes") String policydomaincodes,
            @PathParam("measuretypecodes") String measuretypecodes,
            @PathParam("policymeasurecodes") String policymeasurecodes,
            @PathParam("subpolicymeasurecodes") String subpolicymeasurecodes )
    {

        try {
            DATASOURCE ds = DATASOURCE.AMISPOLICIES;
            DBBean db = new DBBean(ds);
            AmisPoliciesCodingSystemConstants c = null;
            SQLBean sql =null;
            try {
                c = AmisPoliciesCodingSystemConstants.valueOf(codingsystem.toUpperCase());
            }catch(Exception e){}

            List<WhereBean> whereBeanList = getValues(countrycodes, commoditycodes, policydomaincodes, measuretypecodes, policymeasurecodes, subpolicymeasurecodes);

//            System.out.println("coding system type: " +c);
            if ( c != null) {
                switch (c) {
                    case START_DATE: sql = SQLBeansRepository.getStartDateYears("\"MasterTable\"", whereBeanList); break;
                    case END_DATE: sql = SQLBeansRepository.getEndDateYears("\"MasterTable\"", whereBeanList); break;
                    case COUNTRY: sql = SQLBeansRepository.getCountries("\"MasterTable\"", whereBeanList); break;
                    case POLICY_DOMAIN: sql = SQLBeansRepository.getPolicyDomains("\"MasterTable\"", whereBeanList); break;
                    case MEASURE_TYPE: sql = SQLBeansRepository.getMeasureTypes("\"MasterTable\"", whereBeanList); break;
                    case POLICY_MEASURE: sql = SQLBeansRepository.getPolicyMeasures("\"MasterTable\"", whereBeanList); break;
                    case SUB_POLICY_MEASURE: sql = SQLBeansRepository.getSubPolicyMeasures("\"MasterTable\"", whereBeanList); break;
                    case COMMODITY: sql = SQLBeansRepository.getCommodities("\"MasterTable\"", whereBeanList); break;
                }
            }

//            System.out.println(Bean2SQL.convert(sql));
            List<List<String>> table = JDBCConnector.query(db, sql, true);
            // to remove the headers (TODO: add it in the wrapper?)
            table.remove(0);
            String json = Wrapper.wrapAsJSON(table).toString();

            // wrap result
            Response.ResponseBuilder builder = Response.ok(json);
            builder.header("Access-Control-Allow-Origin", "*");
            builder.header("Access-Control-Max-Age", "3600");
            builder.header("Access-Control-Allow-Methods", "GET");
            builder.header("Access-Control-Allow-Headers", "X-Requested-With,Host,User-Agent,Accept,Accept-Language,Accept-Encoding,Accept-Charset,Keep-Alive,Connection,Referer,Origin");
            builder.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON+"; charset=utf-8");

            // return response
            return builder.build();

        } catch (WDSException e) {
            return Response.status(500).entity("Error in 'amis policies Coding System service: " + e.getMessage()).build();
        } catch (ClassNotFoundException e) {
            return Response.status(500).entity("Error in 'amis policies Coding System service: " + e.getMessage()).build();
        } catch (SQLException e) {
            return Response.status(500).entity("Error in 'amis policies Coding System service: " + e.getMessage()).build();
        } catch (InstantiationException e) {
            return Response.status(500).entity("Error in 'amis policies Coding System service: " + e.getMessage()).build();
        } catch (IllegalAccessException e) {
            return Response.status(500).entity("Error in 'amis policies Coding System service: " + e.getMessage()).build();
        }
    }

     private List<WhereBean> getValues(String countrycodes, String commoditycodes, String policydomaincodes, String measuretypecodes, String policymeasurecodes, String subpolicymeasurecodes){
        List<WhereBean> whereBeans = new ArrayList<WhereBean>();

         if ( !countrycodes.equals("null"))
             whereBeans.add(new WhereBean(SQL.TEXT.name(), "\"Country_Code\"", WHERE.IN.name(), null, getValues(countrycodes, true)));

         if ( !commoditycodes.equals("null"))
             whereBeans.add(new WhereBean(SQL.TEXT.name(), "\"Product_AMIS_Code\"", WHERE.IN.name(), null, getValues(commoditycodes, true)));

         if ( !policydomaincodes.equals("null"))
             whereBeans.add(new WhereBean(SQL.TEXT.name(), "\"PolicyDomain_Code\"", WHERE.IN.name(), null, getValues(policydomaincodes, true)));

         if ( !measuretypecodes.equals("null"))
             whereBeans.add(new WhereBean(SQL.TEXT.name(), "\"PolicyMeasureType_Code\"", WHERE.IN.name(), null, getValues(measuretypecodes, true)));

         if ( !policymeasurecodes.equals("null"))
             whereBeans.add(new WhereBean(SQL.TEXT.name(), "\"PolicyMeasure_Code\"", WHERE.IN.name(), null, getValues(policymeasurecodes, true)));

         if ( !subpolicymeasurecodes.equals("null"))
             whereBeans.add(new WhereBean(SQL.TEXT.name(), "\"SubPolicyMeasure_Code\"", WHERE.IN.name(), null, getValues(subpolicymeasurecodes, true)));


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

}

