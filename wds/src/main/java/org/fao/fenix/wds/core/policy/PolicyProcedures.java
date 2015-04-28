package org.fao.fenix.wds.core.policy;


import com.google.gson.Gson;
import org.fao.fenix.wds.core.bean.DatasourceBean;
import org.fao.fenix.wds.core.datasource.DatasourcePool;
import org.fao.fenix.wds.core.jdbc.JDBCIterable;
import org.fao.fenix.wds.core.bean.policy.POLICYDataObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

/**
 * @author <a href="mailto:barbara.cintoli@fao.org">Barbara Cintoli</a>
 * @author <a href="mailto:barbara.cintoli@gmail.com">Barbara Cintoli</a>
 */
@Component
public class PolicyProcedures {

    @Autowired
    private DatasourcePool datasourcePool;

    private final Gson g = new Gson();

    public JDBCIterablePolicy getcpl_id(DatasourceBean dsBean) throws Exception {
        JDBCIterablePolicy it = new JDBCIterablePolicy();
        StringBuilder sb = new StringBuilder();
//        sb.append("EXEC Warehouse.dbo.usp_GetODADonors @tablelanguage='");
//        sb.append(lang);
//        sb.append("' ");
        sb.append("SELECT DISTINCT(cpl_id) FROM mastertable");

        // sb.append("SELECT cpl_id, commodity_id  FROM mastertable");
        sb.append("");
        it.query(dsBean, sb.toString());
        return it;
    }

    public JDBCIterablePolicy getDistinctcpl_id(DatasourceBean dsBean, POLICYDataObject policyDataObject, String policy_type, String policy_measure) throws Exception {
        System.out.println("getDistinctcpl_id START");
        JDBCIterablePolicy it = new JDBCIterablePolicy();
        StringBuilder sb = new StringBuilder();
//        sb.append("EXEC Warehouse.dbo.usp_GetODADonors @tablelanguage='");
//        sb.append(lang);
//        sb.append("' ");
        sb.append("SELECT DISTINCT(cpl_id) FROM mastertable where country_code =" +policyDataObject.getCountry_code() +" and commodityclass_code ="+policyDataObject.getCommodity_class_code());
        sb.append(" and policytype_code IN ("+policy_type+") and policymeasure_code IN ("+policy_measure+")");
        System.out.println("getDistinctcpl_id sb.toString() = "+sb.toString());

        it.query(dsBean, sb.toString());
        return it;
    }

    public JDBCIterablePolicy getcountries_fromPolicy(DatasourceBean dsBean, String policyType, String policyMeasure)throws Exception {
        System.out.println("getcountries_fromPolicy START");
        //SELECT country_code, country_name FROM mastertable where commodityclass_code =3 and policytype_code IN (1) and policymeasure_code IN (3);
        JDBCIterablePolicy it = new JDBCIterablePolicy();
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT DISTINCT(country_code), country_name FROM mastertable where policytype_code IN ("+policyType+") and policymeasure_code IN ("+policyMeasure+") ORDER BY country_name ASC");

        System.out.println(sb.toString());
        it.query(dsBean, sb.toString());
        return it;
    }

    public JDBCIterablePolicy getpolicyAndcommodityDomain(DatasourceBean dsBean) throws Exception {
        JDBCIterablePolicy it = new JDBCIterablePolicy();
        StringBuilder sb = new StringBuilder();
//        sb.append("EXEC Warehouse.dbo.usp_GetODADonors @tablelanguage='");
//        sb.append(lang);
//        sb.append("' ");
//        sb.append("SELECT DISTINCT(cpl_id) FROM mastertable");

//        sb.append("SELECT DISTINCT(policydomain_name), commoditydomain_name from mastertable");
        sb.append("SELECT DISTINCT(policydomain_code), policydomain_name, commoditydomain_code, commoditydomain_name from mastertable");
        it.query(dsBean, sb.toString());
        return it;
    }

    public JDBCIterablePolicy getstartAndEndDate(DatasourceBean dsBean) throws Exception {
        JDBCIterablePolicy it = new JDBCIterablePolicy();
        StringBuilder sb = new StringBuilder();
//        sb.append("EXEC Warehouse.dbo.usp_GetODADonors @tablelanguage='");
//        sb.append(lang);
//        sb.append("' ");
//        sb.append("SELECT DISTINCT(cpl_id) FROM mastertable");

//        sb.append("SELECT MIN(start_date) from policytable");
//        sb.append("SELECT MAX(end_date) from policytable");
//        sb.append("SELECT MIN(start_date), MAX(end_date) from policytable");
        //sb.append("SELECT MAX(end_date), MIN(EXTRACT(day FROM start_date)), MIN(EXTRACT(month FROM start_date)), MIN(EXTRACT(year FROM start_date)), MAX(EXTRACT(day FROM start_date)), MAX(EXTRACT(month FROM start_date)), MAX(EXTRACT(year FROM start_date)) from policytable");
        sb.append("SELECT MAX(GREATEST(start_date,end_date)), MIN(EXTRACT(day FROM start_date)), MIN(EXTRACT(month FROM start_date)), MIN(EXTRACT(year FROM start_date)), EXTRACT(day FROM MAX(GREATEST(start_date,end_date))), EXTRACT(month FROM MAX(GREATEST(start_date,end_date))), EXTRACT(year FROM MAX(GREATEST(start_date,end_date))) from policytable");
        // System.out.println("getstartAndEndDate sb="+sb.toString());
        it.query(dsBean, sb.toString());
        return it;
    }

    public JDBCIterablePolicy getEndDateIsNull(DatasourceBean dsBean) throws Exception {
        JDBCIterablePolicy it = new JDBCIterablePolicy();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT DISTINCT(end_date) FROM policytable WHERE end_date IS NULL");
        it.query(dsBean, sb.toString());
        //System.out.println("getEndDateIsNull After query "+sb.toString());
        return it;
    }

    public JDBCIterablePolicy getpolicyTypes(DatasourceBean dsBean, String policyDomainCodes, String commodityDomainCodes) throws Exception {
//        System.out.println("getpolicyTypes policyDomainCodes "+policyDomainCodes);
//        System.out.println("getpolicyTypes commodityDomainCodes "+commodityDomainCodes);
        JDBCIterablePolicy it = new JDBCIterablePolicy();
        StringBuilder sb = new StringBuilder();
//        sb.append("EXEC Warehouse.dbo.usp_GetODADonors @tablelanguage='");
//        sb.append(lang);
//        sb.append("' ");
        sb.append("SELECT DISTINCT(policytype_code), policytype_name FROM mastertable where policydomain_code IN ("+policyDomainCodes+") AND commoditydomain_code IN ("+commodityDomainCodes+") order by policytype_name ASC ");
        //  System.out.println("getpolicyTypes sb "+sb);
        // sb.append("SELECT cpl_id, commodity_id  FROM mastertable");
        sb.append("");
        it.query(dsBean, sb.toString());
        return it;
    }

    public JDBCIterablePolicy getDownloadPreview(DatasourceBean dsBean, POLICYDataObject pd_obj, boolean with_commodity_id) throws Exception{
         System.out.println("getDownloadPreview start ");
//      select DISTINCT(mastertable.cpl_id) from mastertable, policytable where mastertable.cpl_id=policytable.cpl_id AND ((policytable.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policytable.end_date BETWEEN '05/05/2010' AND '05/04/2011') OR
//      (policytable.end_date IS NULL AND ((policytable.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policytable.start_date < '05/05/2010')))) AND commoditydomain_code IN () AND policydomain_code IN () AND commodityclass_code IN () AND policytype_code IN () AND policymeasure_code IN () AND country_code IN () ORDER BY mastertable.cpl_id;
        JDBCIterablePolicy it = new JDBCIterablePolicy();
        StringBuilder sb = new StringBuilder();
        StringBuilder sbApp = new StringBuilder();
        System.out.println("getDownloadPreview Before  policyMeasuresCodesArray");
        String policyMeasuresCodesArray[] = pd_obj.getPolicy_measure_code();
        System.out.println("getDownloadPreview start "+policyMeasuresCodesArray.length);
        for(int i=0; i< policyMeasuresCodesArray.length; i++)
        {
            System.out.println("getDownloadPreview policyMeasuresCodesArray[i] "+policyMeasuresCodesArray[i]);
        }
        boolean unique = true;
        if((policyMeasuresCodesArray!=null)&&(policyMeasuresCodesArray.length>0))
        {
            System.out.println("getDownloadPreview policyMeasuresCodesArray !=null");
            int unionCount = 0;
            for(int i=0; i<policyMeasuresCodesArray.length;i++)
            {
                // System.out.println("policyMeasuresCodesArray[i] "+policyMeasuresCodesArray[i]);
                String policyMeasuresCodes = policyMeasuresCodesArray[i];
                if(!policyMeasuresCodes.isEmpty())
                {
                    unique = false;
                    if(sbApp.length()>0)
                    {
                        sbApp.append(" UNION ");
                    }
                    //   System.out.println("pd_obj.getPolicy_type_code()[i] "+pd_obj.getPolicy_type_code()[i]);
                    sbApp.append("("+this.getCplId_downloadPreview(pd_obj, pd_obj.getPolicy_type_code()[i], policyMeasuresCodes, with_commodity_id)+")");
                    unionCount++;
                }
            }
            if(unique)
            {
                //There are not Policy Measures Codes... so unique query
                sb.append(this.getCplId_downloadPreview(pd_obj, "", "", with_commodity_id));
            }
            else{
                if(unionCount>1)
                {
                    sb.append("SELECT * FROM (");
                    sb.append(sbApp);
                    sb.append(") t ORDER BY t.cpl_id ASC");
                }
                else
                {
                    //Case with one policy type
                    //Removing the first and the end brackets
                    String sb_withoutBrackets = sbApp.substring(1);
                    int last_bracket_index = sb_withoutBrackets.lastIndexOf(")");
                    sb_withoutBrackets = sb_withoutBrackets.substring(0,last_bracket_index);
                    sb.append(sb_withoutBrackets);
                }
            }
            // System.out.println("unique "+unique);
               System.out.println("getDownloadPreview sb "+sb.toString());
        }
        System.out.println("getDownloadPreview Before return ");
        it.query(dsBean, sb.toString());
        System.out.println("getDownloadPreview Before return 2");
        return it;
    }

    public StringBuilder getCplId_downloadPreview(POLICYDataObject pd_obj, String policy_type_code, String policy_measure_code, boolean with_commodity_id){
        System.out.println("getCplId_downloadPreview start ");
        StringBuilder sb = new StringBuilder();
        if(with_commodity_id)
        {
            sb.append("SELECT DISTINCT(mastertable.cpl_id), mastertable.commodity_id FROM mastertable, policytable ");
        }
        else
        {
            sb.append("SELECT DISTINCT(mastertable.cpl_id) FROM mastertable, policytable ");
        }
        sb.append("WHERE mastertable.cpl_id = policytable.cpl_id AND ");
        //Mandatory Fields
        sb.append("commoditydomain_code IN ("+pd_obj.getCommodity_domain_code()+") AND ");
        sb.append("policydomain_code IN ("+pd_obj.getPolicy_domain_code()+") ");
        // System.out.println("year tab = "+pd_obj.getYearTab());
        if(pd_obj.getYearTab().equals("classic"))
        {
            if((pd_obj.getYear_list()!=null)&&(!pd_obj.getYear_list().isEmpty()))
            {
                sb.append("AND ((EXTRACT(year FROM start_date) IN ("+pd_obj.getYear_list()+")) OR (EXTRACT(year FROM end_date) IN ("+pd_obj.getYear_list()+")))");
            }
        }
        else
        {   //Format: 01/03/2014
            String start_date = pd_obj.getStart_date();
            String end_date = pd_obj.getEnd_date();
            //((policytable.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policytable.end_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policytable.end_date IS NULL AND ((policytable.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policytable.start_date < '05/05/2010'))))
            sb.append("AND ((policytable.start_date BETWEEN '"+start_date+"' AND '"+end_date+"') OR (policytable.end_date BETWEEN '"+start_date+"' AND '"+end_date+"') OR (policytable.end_date IS NULL AND ((policytable.start_date BETWEEN '"+start_date+"' AND '"+end_date+"') OR (policytable.start_date < '"+start_date+"'))))");
//            sb.append("((policytable.start_date >= '"+start_date+"' AND policytable.start_date <= '"+start_date+"') OR (policytable.end_date >= '"+end_date+"' AND policytable.end_date <= '"+end_date+"') OR (policytable.end_date IS NULL AND (policytable.start_date >= '"+start_date+"' AND policytable.start_date <= '"+start_date+"')))");
        }
        //Optional Fields
        if((pd_obj.getCommodity_class_code()!=null)&&(pd_obj.getCommodity_class_code().length()>0))
        {
            sb.append(" AND commodityclass_code IN ("+pd_obj.getCommodity_class_code()+")");
        }
        if((policy_type_code!=null)&&(policy_type_code.length()>0))
        {
            sb.append(" AND policytype_code IN ("+policy_type_code+")");
        }
        if((policy_measure_code!=null)&&(policy_measure_code.length()>0))
        {
            sb.append(" AND policymeasure_code IN ("+policy_measure_code+")");
        }
        if((pd_obj.getCountry_code()!=null)&&(pd_obj.getCountry_code().length()>0))
        {
            sb.append(" AND country_code IN ("+pd_obj.getCountry_code()+")");
        }
        sb.append(" ORDER BY mastertable.cpl_id ASC");

        System.out.println("getCplId_downloadPreview end  "+sb);
        return sb;
    }

    //This function returns all the master fields using the cpl_id
    public JDBCIterablePolicy getMasterFromCplId(DatasourceBean dsBean, POLICYDataObject pd_obj) throws Exception{
        //  System.out.println("getMasterFromCplId 2 start ");
//      select DISTINCT(mastertable.cpl_id) from mastertable, policytable where mastertable.cpl_id=policytable.cpl_id AND ((policytable.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policytable.end_date BETWEEN '05/05/2010' AND '05/04/2011') OR
//      (policytable.end_date IS NULL AND ((policytable.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policytable.start_date < '05/05/2010')))) AND commoditydomain_code IN () AND policydomain_code IN () AND commodityclass_code IN () AND policytype_code IN () AND policymeasure_code IN () AND country_code IN () ORDER BY mastertable.cpl_id;
        JDBCIterablePolicy it = new JDBCIterablePolicy();
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT * from mastertable where cpl_id IN ("+pd_obj.getCpl_id()+") order by mastertable.cpl_id ASC");
        // System.out.println("getMasterFromCplId sb "+sb.toString());
        it.query(dsBean, sb.toString());
        return it;
    }

    //This function returns the code and the label of each commodity belonging to the commodity
    // (if the commodity is a share group)
    public JDBCIterablePolicy getshareGroupInfo(DatasourceBean dsBean, String commodity_id) throws Exception{
        //  System.out.println("getshareGroupInfo ");

        JDBCIterablePolicy it = new JDBCIterablePolicy();
        StringBuilder sb = new StringBuilder();

        //Through the commodity id get the shared_group_code
        sb.append("SELECT shared_group_code FROM commlistwithid where commodity_id = '"+commodity_id+"'");
        // System.out.println("getshareGroupInfo sb "+sb.toString());
        it.query(dsBean, sb.toString());
        return it;
    }

    //This function returns the code and the label of each commodity belonging to the commodity
    // (if the commodity is a share group)
    public JDBCIterablePolicy getSingleIdFromCommodityId(DatasourceBean dsBean, String commodity_id) throws Exception{
        //   System.out.println("getSingleIdFromCommodityId ");

        JDBCIterablePolicy it = new JDBCIterablePolicy();
        StringBuilder sb = new StringBuilder();

        //Through the commodity id get the shared_group_code
        sb.append("SELECT id_single FROM sharedgroups where commodity_id = '"+commodity_id+"'");
        //  System.out.println("getSingleIdFromCommodityId sb "+sb.toString());
        it.query(dsBean, sb.toString());
        return it;
    }

//    //This function returns commodity_id, target_code, description associated to the commodity id
//    public JDBCIterablePolicy getCommodityInfo_commodity_id(DatasourceBean dsBean, String commodity_id_list) throws Exception{
//      //  System.out.println("getCommodityInfo ");
//
//        JDBCIterablePolicy it = new JDBCIterablePolicy();
//        StringBuilder sb = new StringBuilder();
//
//        //Through the commodity id get the shared_group_code
//        //sb.append("SELECT commodity_id, target_code, description FROM commlistwithid where commodity_id IN ("+commodity_id_list+")");
//
//        sb.append("SELECT commodity_id, hs_code, hs_suffix, hs_version, short_description FROM commlistwithid where commodity_id IN ("+commodity_id_list+")");
//        System.out.println("getCommodityInfo_commodity_id "+sb.toString());
//        it.query(dsBean, sb.toString());
//        return it;
//    }

    //commodity_id is the commodity_id of the Share Group
    public JDBCIterablePolicy getCommodityInfo_commodity_id(DatasourceBean dsBean, String commodity_id) throws Exception{
        //  System.out.println("getCommodityInfo ");
        //SELECT v.share_group_commodity_id, commlistwithid.shared_group_code, v.commodity_id, v.hs_code, v.hs_suffix, v.hs_version, v.short_description, v.original_hs_version, v.original_hs_code, v.original_hs_suffix from (SELECT sharedgroups.commodity_id as share_group_commodity_id, commlistwithid.commodity_id, commlistwithid.hs_code, commlistwithid.hs_suffix, commlistwithid.hs_version, commlistwithid.short_description, sharedgroups.original_hs_version, sharedgroups.original_hs_code, sharedgroups.original_hs_suffix FROM commlistwithid INNER JOIN sharedgroups ON commlistwithid.commodity_id=sharedgroups.id_single and sharedgroups.commodity_id = 1037) v where v.share_group_commodity_id = 1037;
        JDBCIterablePolicy it = new JDBCIterablePolicy();
        StringBuilder sb = new StringBuilder();

//        sb.append("SELECT commodity_id, hs_code, hs_suffix, hs_version, short_description, shared_group_code FROM commlistwithid where commodity_id IN ("+commodity_id_list+")");
        sb.append(" SELECT * from (SELECT sharedgroups.commodity_id as shared_group_commodity_id, commlistwithid.commodity_id, commlistwithid.hs_code, commlistwithid.hs_suffix, commlistwithid.hs_version, commlistwithid.short_description, sharedgroups.original_hs_version, sharedgroups.original_hs_code, sharedgroups.original_hs_suffix FROM commlistwithid INNER JOIN sharedgroups ON commlistwithid.commodity_id=sharedgroups.id_single and sharedgroups.commodity_id IN ( "+commodity_id+")) v where v.shared_group_commodity_id IN ( "+commodity_id+") ");
        System.out.println("getCommodityInfo_commodity_id "+sb.toString());
        it.query(dsBean, sb.toString());
        return it;
    }

    //This function returns commodity_id, target_code, description associated to the commodity id
    public JDBCIterablePolicy getCommodityInfo(DatasourceBean dsBean, String commodity_id_list) throws Exception{
        //  System.out.println("getCommodityInfo ");

        JDBCIterablePolicy it = new JDBCIterablePolicy();
        StringBuilder sb = new StringBuilder();

        //Through the commodity id get the shared_group_code
        //sb.append("SELECT commodity_id, target_code, description FROM commlistwithid where commodity_id IN ("+commodity_id_list+")");

        sb.append("SELECT hs_code, hs_suffix, hs_version, short_description FROM commlistwithid where commodity_id IN ("+commodity_id_list+")");
        //  System.out.println("getCommodityInfo sb "+sb.toString());
        it.query(dsBean, sb.toString());
        return it;
    }

    //This function returns all the policy fields using the cpl_id
    public JDBCIterablePolicy getPolicyFromCplId(DatasourceBean dsBean, POLICYDataObject pd_obj) throws Exception{
        //   System.out.println("getPolicyFromCplId start ");
//      select DISTINCT(mastertable.cpl_id) from mastertable, policytable where mastertable.cpl_id=policytable.cpl_id AND ((policytable.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policytable.end_date BETWEEN '05/05/2010' AND '05/04/2011') OR
//      (policytable.end_date IS NULL AND ((policytable.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policytable.start_date < '05/05/2010')))) AND commoditydomain_code IN () AND policydomain_code IN () AND commodityclass_code IN () AND policytype_code IN () AND policymeasure_code IN () AND country_code IN () ORDER BY mastertable.cpl_id;
        JDBCIterablePolicy it = new JDBCIterablePolicy();
        StringBuilder sb = new StringBuilder();

        // sb.append("SELECT * from policytable where cpl_id IN ("+pd_obj.getCpl_id()+") AND");
        sb.append("SELECT "+getSelectForPolicyTable()+" from policytable, commlistwithid where cpl_id IN ("+pd_obj.getCpl_id()+") AND commlistwithid.commodity_id IN ("+pd_obj.getCommodity_id()+") AND policytable.commodity_id=commlistwithid.commodity_id ");
        if(pd_obj.getYearTab().equals("classic"))
        {
            if((pd_obj.getYear_list()!=null)&&(!pd_obj.getYear_list().isEmpty()))
            {
                sb.append("AND ((EXTRACT(year FROM start_date) IN ("+pd_obj.getYear_list()+")) OR (EXTRACT(year FROM end_date) IN ("+pd_obj.getYear_list()+"))) ");
            }
        }
        else
        {   //Format: 01/03/2014
            String start_date = pd_obj.getStart_date();
            String end_date = pd_obj.getEnd_date();
            //((policytable.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policytable.end_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policytable.end_date IS NULL AND ((policytable.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policytable.start_date < '05/05/2010'))))
            sb.append("AND ((policytable.start_date BETWEEN '"+start_date+"' AND '"+end_date+"') OR (policytable.end_date BETWEEN '"+start_date+"' AND '"+end_date+"') OR (policytable.end_date IS NULL AND ((policytable.start_date BETWEEN '"+start_date+"' AND '"+end_date+"') OR (policytable.start_date < '"+start_date+"'))))");
//            sb.append("((policytable.start_date >= '"+start_date+"' AND policytable.start_date <= '"+start_date+"') OR (policytable.end_date >= '"+end_date+"' AND policytable.end_date <= '"+end_date+"') OR (policytable.end_date IS NULL AND (policytable.start_date >= '"+start_date+"' AND policytable.start_date <= '"+start_date+"')))");
        }
        //ORDER BY policy_element, start_date, exemptions
        sb.append(" ORDER BY policy_element, start_date, exemptions ASC");
        //  System.out.println("getPolicyFromCplId sb "+sb.toString());
        it.query(dsBean, sb.toString());
        return it;
    }

    //Select from policy table
//    public StringBuilder getSelectForPolicyTable(){
//        StringBuilder select = new StringBuilder();
////        select.append("sbPolicy.metadata_id, sbPolicy.policy_id, sbPolicy.cpl_id, mastertable.country_code, mastertable.country_name, mastertable.subnational_code, mastertable.subnational_name, mastertable.commoditydomain_code, mastertable.commoditydomain_name, mastertable.commodityclass_code, mastertable.commodityclass_name, mastertable.policydomain_code, mastertable.policydomain_name, mastertable.policytype_code, mastertable.policytype_name, mastertable.policymeasure_code, mastertable.policymeasure_name, mastertable.condition, sbPolicy.commodity_id, sbPolicy.hs_version, sbPolicy.hs_code, sbPolicy.hs_suffix, sbPolicy.commodity_description, sbPolicy.shared_group_code, sbPolicy.location_condition, sbPolicy.start_date, sbPolicy.end_date, sbPolicy.units, sbPolicy.value, sbPolicy.value_text, sbPolicy.exemptions, sbPolicy.value_calculated, sbPolicy.notes, sbPolicy.link, sbPolicy.source, sbPolicy.title_of_notice, sbPolicy.legal_basis_name, sbPolicy.measure_descr, sbPolicy.short_description, sbPolicy.original_dataset, sbPolicy.type_of_change_name, sbPolicy.type_of_change_code, sbPolicy.product_original_hs, sbPolicy.product_original_name, sbPolicy.policy_element, sbPolicy.impl, sbPolicy.second_generation_specific, sbPolicy.imposed_end_date, sbPolicy.benchmark_tax, sbPolicy.benchmark_product, sbPolicy.tax_rate_biofuel, sbPolicy.tax_rate_benchmark, sbPolicy.start_date_tax, sbPolicy.source_benchmark, sbPolicy.date_of_publication, sbPolicy.xs_yeartype, sbPolicy.notes_datepub");
//        select.append("metadata_id, policy_id, cpl_id, commodity_id, hs_version, hs_code, hs_suffix, commodity_description, shared_group_code, location_condition, ");
//        select.append(" EXTRACT(day FROM start_date) || '-' || EXTRACT(month FROM start_date) || '-' || EXTRACT(year FROM start_date), EXTRACT(day FROM end_date) || '-' || EXTRACT(month FROM end_date) || '-' || EXTRACT(year FROM end_date), ");
//        select.append(" units, value, value_text, exemptions, value_calculated, notes, link, source, title_of_notice, legal_basis_name, measure_descr, short_description, original_dataset, type_of_change_name, ");
//        select.append(" type_of_change_code, product_original_hs, product_original_name, policy_element, impl, second_generation_specific, imposed_end_date, benchmark_tax, benchmark_product, tax_rate_biofuel, tax_rate_benchmark, ");
//        select.append(" start_date_tax, source_benchmark, date_of_publication, xs_yeartype, notes_datepub");
//        return select;
//    }

    public StringBuilder getSelectForPolicyTable(){
        StringBuilder select = new StringBuilder();
        //SELECT metadata_id, policy_id, cpl_id, commlistwithid.commodity_id, hs_version, hs_code, hs_suffix, policy_element, EXTRACT(day FROM start_date) || '-' || EXTRACT(month FROM start_date) || '-' || EXTRACT(year FROM start_date), EXTRACT(day FROM end_date) || '-' || EXTRACT(month FROM end_date) || '-' || EXTRACT(year FROM end_date), units, value, value_text, value_type, exemptions, location_condition, notes, link, source, title_of_notice, legal_basis_name, date_of_publication, imposed_end_date, second_generation_specific, benchmark_tax, benchmark_product, tax_rate_biofuel, tax_rate_benchmark, start_date_tax, benchmark_link, original_dataset, type_of_change_code, type_of_change_name, measure_descr, product_original_hs, product_original_name, implementationprocedure, xs_yeartype, link_pdf, benchmark_link_pdf from policytable, commlistwithid where cpl_id IN (1) AND commlistwithid.commodity_id IN (108) AND ((policytable.start_date BETWEEN '2006-02-11' AND '2011-02-11') OR (policytable.end_date BETWEEN '2006-02-11' AND '2011-02-11') OR (policytable.end_date IS NULL AND ((policytable.start_date BETWEEN '2006-02-11' AND '2011-02-11') OR (policytable.start_date < '2006-02-11'))));
        select.append("metadata_id, policy_id, cpl_id, commlistwithid.commodity_id, hs_version, hs_code, hs_suffix, policy_element, ");
        select.append("EXTRACT(day FROM start_date) || '/' || EXTRACT(month FROM start_date) || '/' || EXTRACT(year FROM start_date), EXTRACT(day FROM end_date) || '/' || EXTRACT(month FROM end_date) || '/' || EXTRACT(year FROM end_date), ");
        select.append("units, value, value_text, value_type, exemptions, location_condition, notes, link, source, title_of_notice, legal_basis_name, EXTRACT(day FROM date_of_publication) || '/' || EXTRACT(month FROM date_of_publication) || '/' || EXTRACT(year FROM date_of_publication), imposed_end_date, ");
        select.append("second_generation_specific, benchmark_tax, benchmark_product, tax_rate_biofuel, tax_rate_benchmark, start_date_tax, benchmark_link, original_dataset, type_of_change_code, type_of_change_name, measure_descr, product_original_hs, product_original_name, ");
        select.append("implementationprocedure, xs_yeartype, link_pdf, benchmark_link_pdf, short_description, shared_group_code ");
        return select;
    }

    public JDBCIterablePolicy getDownloadExport(DatasourceBean dsBean, POLICYDataObject pd_obj) throws Exception{
        System.out.println("getDownloadExport start ");
//      select DISTINCT(mastertable.cpl_id) from mastertable, policytable where mastertable.cpl_id=policytable.cpl_id AND ((policytable.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policytable.end_date BETWEEN '05/05/2010' AND '05/04/2011') OR
//      (policytable.end_date IS NULL AND ((policytable.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policytable.start_date < '05/05/2010')))) AND commoditydomain_code IN () AND policydomain_code IN () AND commodityclass_code IN () AND policytype_code IN () AND policymeasure_code IN () AND country_code IN () ORDER BY mastertable.cpl_id;
        JDBCIterablePolicy it = new JDBCIterablePolicy();

        //Query to have the distinct on the cpl_id based on the user selection
        //StringBuilder sbCplId = getDistinctCplId_fromMaster(pd_obj);
        StringBuilder total = new StringBuilder();
        boolean with_commodity_id = true;
        System.out.println(" getDownloadExport with_commodity_id "+with_commodity_id);
        JDBCIterablePolicy itCplId = getDownloadPreview(dsBean, pd_obj, with_commodity_id);
        System.out.println(" getDownloadExport after getDownloadPreview ");
        String s_cpl_id="";
        String s_commodity_id="";
        //To avoid duplicates
        List<String> commodity_id_list = new ArrayList<String>();
        while(itCplId.hasNext()) {
            //From [299, 320] to 299, 320
            List<String> valueList = itCplId.next();
            // System.out.println("valueList "+valueList.toString());
            //There is only one value
            String value = valueList.toString();
            int last_index = value.lastIndexOf("]");
            value = value.substring(1, last_index);
            value = value.replaceAll("\\s+","");
            //From 299,320
            last_index = value.indexOf(",");
            //299
            String cpl_id = value.substring(0, last_index);
            String commodity_id = value.substring(last_index+1);
            s_cpl_id+= cpl_id;
            boolean duplicate_not_found = false;
            if(!commodity_id_list.contains(commodity_id))
            {
                if((s_commodity_id!=null)&&(s_commodity_id.length()>0))
                {
                    //In this way it will be inserted once
                    s_commodity_id+=",";
                }
                s_commodity_id+= commodity_id;
                commodity_id_list.add(commodity_id);
                duplicate_not_found = true;
            }
            //  System.out.println("value "+value+" new value "+s);
            if(itCplId.hasNext())
            {
                s_cpl_id+=",";
//                if(duplicate_not_found)
//                {
//
//                }
            }
        }
        //No cpl_id has available for this selection
        if(s_cpl_id.length()==0)
        {
            return new JDBCIterablePolicy();
        }

        //Query to have the selection on the policy based on the cpl_id, commodity_id and the time
        StringBuilder sbPolicyWithTime = getStringQueryPolicyFromCplIdTime(pd_obj, s_cpl_id, s_commodity_id);

        /*SELECT Orders.OrderID, Customers.CustomerName, Orders.OrderDate
    FROM Orders
    INNER JOIN Customers
    ON Orders.CustomerID=Customers.CustomerID;*/

        total.append("SELECT "+getSelectForJoinMasterPolicyTable_fromMaster()+" FROM ("+sbPolicyWithTime.toString()+") sbPolicy JOIN mastertable ON sbPolicy.cpl_id = mastertable.cpl_id ORDER BY sbPolicy.policy_element, sbPolicy.start_date, sbPolicy.exemptions ASC");
         System.out.println("getDownloadExport "+total.toString());
        it.query(dsBean, total.toString());
        return it;
    }

//    public Map<String, LinkedList<String>> getDownloadShareGroupExport(DatasourceBean dsBean, POLICYDataObject pd_obj) throws Exception{
//       // System.out.println("getDownloadShareGroupExport start ");
//        Map<String, LinkedList<String>> map1 = new LinkedHashMap<String, LinkedList<String>>();
//
////      select DISTINCT(mastertable.cpl_id) from mastertable, policytable where mastertable.cpl_id=policytable.cpl_id AND ((policytable.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policytable.end_date BETWEEN '05/05/2010' AND '05/04/2011') OR
////      (policytable.end_date IS NULL AND ((policytable.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policytable.start_date < '05/05/2010')))) AND commoditydomain_code IN () AND policydomain_code IN () AND commodityclass_code IN () AND policytype_code IN () AND policymeasure_code IN () AND country_code IN () ORDER BY mastertable.cpl_id;
//       // JDBCIterablePolicy it = new JDBCIterablePolicy();
//        //Query to have the distinct on the cpl_id based on the user selection
//        //StringBuilder sbCplId = getDistinctCplId_fromMaster(pd_obj);
//
//        //1-Getting all the cpli_id for the user selection
//        StringBuilder total = new StringBuilder();
//        boolean with_commodity_id = false;
//        JDBCIterablePolicy itCplId = getDownloadPreview(dsBean, pd_obj, with_commodity_id);
//        String s1="";
//        while(itCplId.hasNext()) {
//            //From [289] to 289
//            List<String> valueList = itCplId.next();
//            //  System.out.println("valueList "+valueList.toString());
//            //There is only one value
//            String value = valueList.toString();
//            s1+= value.substring(1,(value.length()-1));
//            //  System.out.println("value "+value+" new value "+s);
//            if (itCplId.hasNext())
//                s1+=",";
//        }
//        //No cpl_id has available for this selection
//        if(s1.length()==0)
//        {
//            //return new JDBCIterablePolicy();
//            return map1;
//        }
//
//        //2-Getting the commodity id associated with each cpli_id
//        //select distinct (commodity_id) from mastertable where cpl_id ='1';
//        JDBCIterablePolicy itCommodityId = getSelectCommodityIdFromCplId_fromMaster(dsBean, s1);
//        if(itCommodityId.hasNext()==false)
//        {
//            //return new JDBCIterablePolicy();
//          //  System.out.println("if(itCommodityId.hasNext()==false)");
//            return map1;
//        }
//
//        //3-For each commodity id check if it's a share group
//        String commodity_id="";
//        while(itCommodityId.hasNext())
//        {
//            //System.out.println("while(itCommodityId.hasNext())");
//            List<String> valueListCplId = itCommodityId.next();
//            //System.out.println("valueListCplId "+valueListCplId);
//            String valueCplId = valueListCplId.toString();
//            //System.out.println("valueCplId " + valueCplId);
//            commodity_id = valueCplId.substring(1,(valueCplId.length()-1));
//           // System.out.println("Commodity id "+commodity_id);
//            //For each commodity id
//
//            final JDBCIterablePolicy it =  getshareGroupInfo(dsBean, commodity_id);
//            //it.closeConnection();
//
//            while(it.hasNext()) {
//                //Get the share group code associated to the commodity_id
//                String valueshare_group_code = (it.next()).toString();
//                //System.out.println("valueshare_group_code "+valueshare_group_code);
//                String share_group_code = valueshare_group_code.substring(1,(valueshare_group_code.length()-1));
//               // System.out.println(" share_group_code "+share_group_code);
//
//                if((share_group_code!=null)&&(!share_group_code.equals("none")&&(!share_group_code.isEmpty())))
//                {
//                    //Get the list of commodity that belongs to this share_group_code
//                    final JDBCIterablePolicy it2 = getSingleIdFromCommodityId(dsBean, commodity_id);
//                    //Adding also info about the share group
//                    String s = ""+commodity_id+",";
//                    while(it2.hasNext()) {
//                        //From [289] to 289
//                        List<String> valueList = it2.next();
//                        //  System.out.println("valueList "+valueList.toString());
//                        //There is only one value
//                        String value = valueList.toString();
//    //                    value = value.substring(2, value.lastIndexOf("\""));
//    //                   s+= "'"+g.toJson(it2.next())+"'";
//    //                    s+= "'"+it2.next()+"'";
//
//                        s+= value.substring(1,(value.length()-1));
//                        //  System.out.println("value "+value+" new value "+s);
//                        if (it2.hasNext())
//                            s+=",";
//                    }
//                    it2.closeConnection();
//                    //  System.out.println("s = "+s);
//                    if(s.length()==0)
//                    {
//                        //  System.out.println("(s.length()==0)");
//                        //It is not a share group
//                        //return new JDBCIterablePolicy();
//                       // System.out.println("No commodity associated with that share group if(s.length()==0)");
//                        continue;
////                        return map1;
//                    }
//                    else
//                    {
//                       // System.out.println("FOUND!!!!!");
//                        //  System.out.println("(s.length()!0)");
//                        //Get commodity_id, target_code, description associated with the commodityId
////                        final JDBCIterablePolicy it3 = getCommodityInfo(dsBean, s);
////                        String commodity_id_value="";
////                        while(it3.hasNext()) {
////                            //From [289] to 289
////                            List<String> valueList = it3.next();
////                          //  System.out.println("after getCommodityInfo valueList "+valueList.toString());
////                            //commodity_id, target_code, description
////                            commodity_id_value = valueList.get(0).toString();
//////                            commodity_id_value = value.substring(1,(value.length()-1));
////                            String target_code = "";
////                            String description = "";
////                            if(!map1.containsKey(commodity_id_value))
////                            {
////                                //This key is not in the map
////                                //Adding..
////                                LinkedList<String> list= new LinkedList<String>();
////                                target_code = valueList.get(1).toString();
////                                //System.out.println(" targ "+target_code);
////                                list.add(0, target_code);
////                                description = valueList.get(2).toString();
////                                //System.out.println(" descr "+description);
////                                list.add(1, description);
////                             //   System.out.println("PUTTING IN MAP  id = "+commodity_id_value+" target_code = "+target_code+" description = "+description);
////                                map1.put(commodity_id_value,list);
////                            }
////                        }
//
//                        final JDBCIterablePolicy it3 = getCommodityInfo_commodity_id(dsBean, s);
//                        String commodity_id_value="";
//                        while(it3.hasNext()) {
//                            //From [289] to 289
//                            List<String> valueList = it3.next();
//                            //  System.out.println("after getCommodityInfo valueList "+valueList.toString());
//                            //hs_code, hs_suffix, hs_version, short_description
//                            commodity_id_value = valueList.get(0).toString();
//                          //  System.out.println("commodity_id_value "+commodity_id_value);
////                            hs_code = value.substring(1,(value.length()-1));
//                            String hs_code="";
//                            String hs_suffix = "";
//                            String hs_version = "";
//                            String short_description = "";
//                            String shared_group_code = "";
//                            if(!map1.containsKey(commodity_id_value))
//                            {
//                                //This key is not in the map
//                                //Adding..
//                                LinkedList<String> list= new LinkedList<String>();
//                                if(valueList.get(1)!=null)
//                                {
//                                    hs_code = valueList.get(1).toString();
//                                }
//                                if(valueList.get(2)!=null)
//                                {
//                                    hs_suffix = valueList.get(2).toString();
//                                }
//                                if(valueList.get(3)!=null)
//                                {
//                                    hs_version = valueList.get(3).toString();
//                                }
//                                if(valueList.get(4)!=null)
//                                {
//                                    short_description = valueList.get(4).toString();
//                                }
//                                if(valueList.get(5)!=null)
//                                {
//                                    shared_group_code = valueList.get(5).toString();
//                                }
//
//                               // System.out.println(" hs_code "+hs_code);
//                                list.add(0, hs_code);
//                              //  System.out.println(" hs_suffix "+hs_suffix);
//                                list.add(1, hs_suffix);
//                              //  System.out.println(" hs_version "+hs_version);
//                                list.add(2, hs_version);
//                             //   System.out.println(" short_description "+short_description);
//                                list.add(3, short_description);
//                                list.add(4, commodity_id_value);
//                                list.add(5, shared_group_code);
//                             //   System.out.println("PUTTING IN MAP  id = "+hs_code+" hs_suffix = "+hs_suffix+" hs_version = "+hs_version +" short_description "+short_description);
//                                map1.put(commodity_id_value,list);
//                            }
//                        }
//                        it3.closeConnection();
//                        // writer.write(g.toJson(it3.next()));
//                      //  return map1;
//    //                    return it3;
//                    }
//                }
//                else{
//                    //It is not a share group
//                    // Initiate the stream
//                    //return new JDBCIterablePolicy();
//                  //  System.out.println("else continue");
//                    continue;
//                    //return map1;
//                }
//            }
//            it.closeConnection();
//        }//For each commodity id
//
//        //To change
//        return map1;
//    }

    public Map<String, Map<String, LinkedList<String>>> getDownloadShareGroupExport(DatasourceBean dsBean, POLICYDataObject pd_obj) throws Exception{
        // System.out.println("getDownloadShareGroupExport start ");
        // Map<String, LinkedList<String>> map1 = new LinkedHashMap<String, LinkedList<String>>();
        //First key share group commodity id....
        //Second key commodity od of each commodity that belong to that share group
        //the list contains the field of that commodity
        Map<String, Map<String, LinkedList<String>>> map1 = new LinkedHashMap<String, Map<String, LinkedList<String>>>();

//      select DISTINCT(mastertable.cpl_id) from mastertable, policytable where mastertable.cpl_id=policytable.cpl_id AND ((policytable.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policytable.end_date BETWEEN '05/05/2010' AND '05/04/2011') OR
//      (policytable.end_date IS NULL AND ((policytable.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policytable.start_date < '05/05/2010')))) AND commoditydomain_code IN () AND policydomain_code IN () AND commodityclass_code IN () AND policytype_code IN () AND policymeasure_code IN () AND country_code IN () ORDER BY mastertable.cpl_id;
        // JDBCIterablePolicy it = new JDBCIterablePolicy();
        //Query to have the distinct on the cpl_id based on the user selection
        //StringBuilder sbCplId = getDistinctCplId_fromMaster(pd_obj);

        //1-Getting all the cpli_id for the user selection
        StringBuilder total = new StringBuilder();
        boolean with_commodity_id = false;
        JDBCIterablePolicy itCplId = getDownloadPreview(dsBean, pd_obj, with_commodity_id);
        String s1="";
        while(itCplId.hasNext()) {
            //From [289] to 289
            List<String> valueList = itCplId.next();
            //  System.out.println("valueList "+valueList.toString());
            //There is only one value
            String value = valueList.toString();
            s1+= value.substring(1,(value.length()-1));
            //  System.out.println("value "+value+" new value "+s);
            if (itCplId.hasNext())
                s1+=",";
        }
        //No cpl_id has available for this selection
        if(s1.length()==0)
        {
            //return new JDBCIterablePolicy();
            return map1;
        }

        //2-Getting the commodity id associated with each cpli_id
        //select distinct (commodity_id) from mastertable where cpl_id ='1';
        JDBCIterablePolicy itCommodityId = getSelectCommodityIdFromCplId_fromMaster(dsBean, s1);
        if(itCommodityId.hasNext()==false)
        {
            //return new JDBCIterablePolicy();
            //  System.out.println("if(itCommodityId.hasNext()==false)");
            return map1;
        }

        //3-For each commodity id check if it's a share group
        String commodity_id="";
        while(itCommodityId.hasNext())
        {
            //System.out.println("while(itCommodityId.hasNext())");
            List<String> valueListCplId = itCommodityId.next();
            //System.out.println("valueListCplId "+valueListCplId);
            String valueCplId = valueListCplId.toString();
            //System.out.println("valueCplId " + valueCplId);
            commodity_id = valueCplId.substring(1,(valueCplId.length()-1));
            // System.out.println("Commodity id "+commodity_id);
            //For each commodity id
            final JDBCIterablePolicy it =  getshareGroupInfo(dsBean, commodity_id);
            //it.closeConnection();

            while(it.hasNext()) {
                //Get the share group code associated to the commodity_id
                String valueshare_group_code = (it.next()).toString();
                //System.out.println("valueshare_group_code "+valueshare_group_code);
                String share_group_code = valueshare_group_code.substring(1,(valueshare_group_code.length()-1));
                // System.out.println(" share_group_code "+share_group_code);

                if((share_group_code!=null)&&(!share_group_code.equals("none")&&(!share_group_code.isEmpty())))
                {
                    //Get the list of commodity that belongs to this share_group_code
                    final JDBCIterablePolicy it2 = getSingleIdFromCommodityId(dsBean, commodity_id);
                    //Adding also info about the share group
                    String s = ""+commodity_id+",";
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
                    it2.closeConnection();
                    //  System.out.println("s = "+s);
                    if(s.length()==0)
                    {
                        //No commodity associated with that share group
                        // System.out.println("No commodity associated with that share group if(s.length()==0)");
                        continue;
//                        return map1;
                    }
                    else
                    {
                        //commodity_id contains the id of the share group
                        //The result is:
                        //share_group_commodity_id | commodity_id | hs_code  | hs_suffix | hs_version | short_description | original_hs_version | original_hs_code | original_hs_suffix
                        final JDBCIterablePolicy it3 = getCommodityInfo_commodity_id(dsBean, commodity_id);

                        String commodity_id_value="";
                        while(it3.hasNext()) {
                            List<String> valueList = it3.next();
                            //This is(commodity_id_value) the commodity id of each commodity that belong to the share group
                            commodity_id_value = valueList.get(1).toString();
                            if((commodity_id_value!=null)&&(commodity_id_value.length()!=0))
                            {
                                String shared_group_code = "";
                                String hs_code = "";
                                String hs_suffix = "";
                                String hs_version = "";
                                String short_description = "";
                                String original_hs_version = "";
                                String original_hs_code = "";
                                String original_hs_suffix = "";

                                //If the map of the specific share group doesn't contain the commodity id
                                if((!map1.containsKey(commodity_id))||(!map1.get(commodity_id).containsKey(commodity_id_value)))
                                {
                                    //Creation of the list for the new commodity
                                    LinkedList<String> list= new LinkedList<String>();
                                    shared_group_code = share_group_code;
                                    if(valueList.get(2)!=null)
                                    {
                                        hs_code = valueList.get(2).toString();
                                    }
                                    if(valueList.get(3)!=null)
                                    {
                                        hs_suffix = valueList.get(3).toString();
                                    }
                                    if(valueList.get(4)!=null)
                                    {
                                        hs_version = valueList.get(4).toString();
                                    }
                                    if(valueList.get(5)!=null)
                                    {
                                        short_description = valueList.get(5).toString();
                                    }
                                    if(valueList.get(6)!=null)
                                    {
                                        original_hs_version = valueList.get(6).toString();
                                    }
                                    if(valueList.get(7)!=null)
                                    {
                                        original_hs_code = valueList.get(7).toString();
                                    }
                                    if(valueList.get(8)!=null)
                                    {
                                        original_hs_suffix = valueList.get(8).toString();
                                    }
                                    list.add(0, shared_group_code);
                                    list.add(1, hs_code);
                                    list.add(2, hs_suffix);
                                    list.add(3, hs_version);
                                    list.add(4, short_description);
                                    list.add(5, original_hs_version);
                                    list.add(6, original_hs_code);
                                    list.add(7, original_hs_suffix);
                                    Map<String, LinkedList<String>> mp2 = new LinkedHashMap<String, LinkedList<String>>();
                                    mp2.put(commodity_id_value, list);

                                    if(!map1.containsKey(commodity_id))
                                    {
                                        //if the shared group was not in the map
                                        map1.put(commodity_id, mp2);
                                    }
                                    else
                                    {
                                        //get the map of the particular share group
                                        map1.get(commodity_id).put(commodity_id_value, list);
                                    }
                                }
                            }
                        }
                        it3.closeConnection();
                        // writer.write(g.toJson(it3.next()));
                        //  return map1;
                        //                    return it3;
                    }
                }
                else{
                    //It is not a share group
                    // Initiate the stream
                    //return new JDBCIterablePolicy();
                    //  System.out.println("else continue");
                    continue;
                    //return map1;
                }
            }
            it.closeConnection();
        }//For each commodity id

        //To change
        return map1;
    }

    //Select form join the master and the policy based on the cplid
    public JDBCIterablePolicy getSelectCommodityIdFromCplId_fromMaster(DatasourceBean dsBean, String cplId_string) throws Exception{
        StringBuilder s = new StringBuilder();
        JDBCIterablePolicy it = new JDBCIterablePolicy();
        s.append("select distinct(commodity_id) from mastertable where cpl_id IN ("+cplId_string+") order by commodity_id ASC");
        // System.out.println("getSelectCommodityIdFromCplId_fromMaster "+s.toString());
        it.query(dsBean, s.toString());
        return it;
    }

    //Select form join the master and the policy based on the cplid
    public StringBuilder getSelectForJoinMasterPolicyTable_fromMaster(){
        StringBuilder select = new StringBuilder();
        //SELECT sbPolicy.metadata_id, sbPolicy.policy_id, mastertable.cpl_id, mastertable.cpl_code, mastertable.country_code, mastertable.country_name, mastertable.subnational_code, mastertable.subnational_name, mastertable.commoditydomain_code, mastertable.commoditydomain_name, mastertable.commodityclass_code, mastertable.commodityclass_name, mastertable.policydomain_code, mastertable.policydomain_name, mastertable.policytype_code, mastertable.policytype_name, mastertable.policymeasure_code, mastertable.policymeasure_name, mastertable.condition_code, mastertable.condition, mastertable.individualpolicy_code, mastertable.individualpolicy_name, sbPolicy.commodity_id, sbPolicy.hs_version, sbPolicy.hs_code, sbPolicy.hs_suffix, sbPolicy.short_description, sbPolicy.shared_group_code, sbPolicy.policy_element, EXTRACT(day FROM sbPolicy.start_date) || '-' || EXTRACT(month FROM sbPolicy.start_date) || '-' || EXTRACT(year FROM sbPolicy.start_date), EXTRACT(day FROM sbPolicy.end_date) || '-' || EXTRACT(month FROM sbPolicy.end_date) || '-' || EXTRACT(year FROM sbPolicy.end_date), sbPolicy.units, sbPolicy.value, sbPolicy.value_text, sbPolicy.value_type, sbPolicy.exemptions, sbPolicy.location_condition, sbPolicy.notes, sbPolicy.link, sbPolicy.source, sbPolicy.title_of_notice, sbPolicy.legal_basis_name, sbPolicy.date_of_publication, sbPolicy.imposed_end_date, sbPolicy.second_generation_specific, sbPolicy.benchmark_tax, sbPolicy.benchmark_product, sbPolicy.tax_rate_biofuel, sbPolicy.tax_rate_benchmark, sbPolicy.start_date_tax, sbPolicy.benchmark_link, sbPolicy.original_dataset, sbPolicy.type_of_change_code, sbPolicy.type_of_change_name, sbPolicy.measure_descr, sbPolicy.product_original_hs, sbPolicy.product_original_name, sbPolicy.implementationprocedure, sbPolicy.xs_yeartype, sbPolicy.link_pdf, sbPolicy.benchmark_link_pdf
//        select.append("sbPolicy.metadata_id, sbPolicy.policy_id, sbPolicy.cpl_id, mastertable.country_code, mastertable.country_name, mastertable.subnational_code, mastertable.subnational_name, mastertable.commoditydomain_code, mastertable.commoditydomain_name, mastertable.commodityclass_code, mastertable.commodityclass_name, mastertable.policydomain_code, mastertable.policydomain_name, mastertable.policytype_code, mastertable.policytype_name, mastertable.policymeasure_code, mastertable.policymeasure_name, mastertable.condition, sbPolicy.commodity_id, sbPolicy.hs_version, sbPolicy.hs_code, sbPolicy.hs_suffix, sbPolicy.commodity_description, sbPolicy.shared_group_code, sbPolicy.location_condition, sbPolicy.start_date, sbPolicy.end_date, sbPolicy.units, sbPolicy.value, sbPolicy.value_text, sbPolicy.exemptions, sbPolicy.value_calculated, sbPolicy.notes, sbPolicy.link, sbPolicy.source, sbPolicy.title_of_notice, sbPolicy.legal_basis_name, sbPolicy.measure_descr, sbPolicy.short_description, sbPolicy.original_dataset, sbPolicy.type_of_change_name, sbPolicy.type_of_change_code, sbPolicy.product_original_hs, sbPolicy.product_original_name, sbPolicy.policy_element, sbPolicy.impl, sbPolicy.second_generation_specific, sbPolicy.imposed_end_date, sbPolicy.benchmark_tax, sbPolicy.benchmark_product, sbPolicy.tax_rate_biofuel, sbPolicy.tax_rate_benchmark, sbPolicy.start_date_tax, sbPolicy.source_benchmark, sbPolicy.date_of_publication, sbPolicy.xs_yeartype, sbPolicy.notes_datepub");
        //  select.append("sbPolicy.metadata_id, sbPolicy.policy_id, sbPolicy.cpl_id, mastertable.country_code, mastertable.country_name, mastertable.subnational_code, mastertable.subnational_name, mastertable.commoditydomain_code, mastertable.commoditydomain_name, mastertable.commodityclass_code, mastertable.commodityclass_name, mastertable.policydomain_code, mastertable.policydomain_name, mastertable.policytype_code, mastertable.policytype_name, mastertable.policymeasure_code, mastertable.policymeasure_name, mastertable.condition, sbPolicy.commodity_id, sbPolicy.hs_version, sbPolicy.hs_code, sbPolicy.hs_suffix, sbPolicy.commodity_description, sbPolicy.shared_group_code, sbPolicy.location_condition, EXTRACT(day FROM sbPolicy.start_date) || '-' || EXTRACT(month FROM sbPolicy.start_date) || '-' || EXTRACT(year FROM sbPolicy.start_date), EXTRACT(day FROM sbPolicy.end_date) || '-' || EXTRACT(month FROM sbPolicy.end_date) || '-' || EXTRACT(year FROM sbPolicy.end_date), sbPolicy.units, sbPolicy.value, sbPolicy.value_text, sbPolicy.exemptions, sbPolicy.value_calculated, sbPolicy.notes, sbPolicy.link, sbPolicy.source, sbPolicy.title_of_notice, sbPolicy.legal_basis_name, sbPolicy.measure_descr, sbPolicy.short_description, sbPolicy.original_dataset, sbPolicy.type_of_change_name, sbPolicy.type_of_change_code, sbPolicy.product_original_hs, sbPolicy.product_original_name, sbPolicy.policy_element, sbPolicy.impl, sbPolicy.second_generation_specific, sbPolicy.imposed_end_date, sbPolicy.benchmark_tax, sbPolicy.benchmark_product, sbPolicy.tax_rate_biofuel, sbPolicy.tax_rate_benchmark, sbPolicy.start_date_tax, sbPolicy.source_benchmark, sbPolicy.date_of_publication, sbPolicy.xs_yeartype, sbPolicy.notes_datepub");
//With Metadata id
//        select.append("sbPolicy.metadata_id, sbPolicy.policy_id, mastertable.cpl_id, mastertable.cpl_code, mastertable.country_code, mastertable.country_name, mastertable.subnational_code, mastertable.subnational_name, mastertable.commoditydomain_code, mastertable.commoditydomain_name, mastertable.commodityclass_code, mastertable.commodityclass_name, mastertable.policydomain_code, mastertable.policydomain_name, mastertable.policytype_code, mastertable.policytype_name, mastertable.policymeasure_code, mastertable.policymeasure_name, mastertable.condition_code, mastertable.condition, mastertable.individualpolicy_code, mastertable.individualpolicy_name, sbPolicy.commodity_id, sbPolicy.hs_version, sbPolicy.hs_code, sbPolicy.hs_suffix, sbPolicy.short_description, sbPolicy.shared_group_code, sbPolicy.policy_element, EXTRACT(day FROM sbPolicy.start_date) || '/' || EXTRACT(month FROM sbPolicy.start_date) || '/' || EXTRACT(year FROM sbPolicy.start_date), EXTRACT(day FROM sbPolicy.end_date) || '/' || EXTRACT(month FROM sbPolicy.end_date) || '/' || EXTRACT(year FROM sbPolicy.end_date), sbPolicy.units, sbPolicy.value, sbPolicy.value_text, sbPolicy.value_type, sbPolicy.exemptions, sbPolicy.location_condition, sbPolicy.notes, sbPolicy.link, sbPolicy.source, sbPolicy.title_of_notice, sbPolicy.legal_basis_name, EXTRACT(day FROM sbPolicy.date_of_publication) || '/' || EXTRACT(month FROM sbPolicy.date_of_publication) || '/' || EXTRACT(year FROM sbPolicy.date_of_publication), imposed_end_date, sbPolicy.second_generation_specific, sbPolicy.benchmark_tax, sbPolicy.benchmark_product, sbPolicy.tax_rate_biofuel, sbPolicy.tax_rate_benchmark, sbPolicy.start_date_tax, sbPolicy.benchmark_link, sbPolicy.original_dataset, sbPolicy.type_of_change_code, sbPolicy.type_of_change_name, sbPolicy.measure_descr, sbPolicy.product_original_hs, sbPolicy.product_original_name, sbPolicy.implementationprocedure, sbPolicy.xs_yeartype, sbPolicy.link_pdf, sbPolicy.benchmark_link_pdf ");
//Without Metadata id
        //Before removing policy_id
//        select.append("sbPolicy.policy_id, mastertable.cpl_id, mastertable.cpl_code, mastertable.country_code, mastertable.country_name, mastertable.subnational_code, mastertable.subnational_name, mastertable.commoditydomain_code, mastertable.commoditydomain_name, mastertable.commodityclass_code, mastertable.commodityclass_name, mastertable.policydomain_code, mastertable.policydomain_name, mastertable.policytype_code, mastertable.policytype_name, mastertable.policymeasure_code, mastertable.policymeasure_name, mastertable.condition_code, mastertable.condition, mastertable.individualpolicy_code, mastertable.individualpolicy_name, sbPolicy.commodity_id, sbPolicy.hs_version, sbPolicy.hs_code, sbPolicy.hs_suffix, sbPolicy.short_description, sbPolicy.shared_group_code, sbPolicy.policy_element, EXTRACT(day FROM sbPolicy.start_date) || '/' || EXTRACT(month FROM sbPolicy.start_date) || '/' || EXTRACT(year FROM sbPolicy.start_date), EXTRACT(day FROM sbPolicy.end_date) || '/' || EXTRACT(month FROM sbPolicy.end_date) || '/' || EXTRACT(year FROM sbPolicy.end_date), sbPolicy.units, sbPolicy.value, sbPolicy.value_text, sbPolicy.value_type, sbPolicy.exemptions, sbPolicy.location_condition, sbPolicy.notes, sbPolicy.link, sbPolicy.source, sbPolicy.title_of_notice, sbPolicy.legal_basis_name, EXTRACT(day FROM sbPolicy.date_of_publication) || '/' || EXTRACT(month FROM sbPolicy.date_of_publication) || '/' || EXTRACT(year FROM sbPolicy.date_of_publication), imposed_end_date, sbPolicy.second_generation_specific, sbPolicy.benchmark_tax, sbPolicy.benchmark_product, sbPolicy.tax_rate_biofuel, sbPolicy.tax_rate_benchmark, sbPolicy.start_date_tax, sbPolicy.benchmark_link, sbPolicy.original_dataset, sbPolicy.type_of_change_code, sbPolicy.type_of_change_name, sbPolicy.measure_descr, sbPolicy.product_original_hs, sbPolicy.product_original_name, sbPolicy.implementationprocedure, sbPolicy.xs_yeartype, sbPolicy.link_pdf, sbPolicy.benchmark_link_pdf ");
        //After removing policy_id
        //select.append("mastertable.cpl_id, mastertable.cpl_code, mastertable.country_code, mastertable.country_name, mastertable.subnational_code, mastertable.subnational_name, mastertable.commoditydomain_code, mastertable.commoditydomain_name, mastertable.commodityclass_code, mastertable.commodityclass_name, mastertable.policydomain_code, mastertable.policydomain_name, mastertable.policytype_code, mastertable.policytype_name, mastertable.policymeasure_code, mastertable.policymeasure_name, mastertable.condition_code, mastertable.condition, mastertable.individualpolicy_code, mastertable.individualpolicy_name, sbPolicy.commodity_id, sbPolicy.hs_version, sbPolicy.hs_code, sbPolicy.hs_suffix, sbPolicy.short_description, sbPolicy.shared_group_code, sbPolicy.policy_element, EXTRACT(day FROM sbPolicy.start_date) || '/' || EXTRACT(month FROM sbPolicy.start_date) || '/' || EXTRACT(year FROM sbPolicy.start_date), EXTRACT(day FROM sbPolicy.end_date) || '/' || EXTRACT(month FROM sbPolicy.end_date) || '/' || EXTRACT(year FROM sbPolicy.end_date), sbPolicy.units, sbPolicy.value, sbPolicy.value_text, sbPolicy.value_type, sbPolicy.exemptions, sbPolicy.location_condition, sbPolicy.notes, sbPolicy.link, sbPolicy.source, sbPolicy.title_of_notice, sbPolicy.legal_basis_name, EXTRACT(day FROM sbPolicy.date_of_publication) || '/' || EXTRACT(month FROM sbPolicy.date_of_publication) || '/' || EXTRACT(year FROM sbPolicy.date_of_publication), imposed_end_date, sbPolicy.second_generation_specific, sbPolicy.benchmark_tax, sbPolicy.benchmark_product, sbPolicy.tax_rate_biofuel, sbPolicy.tax_rate_benchmark, sbPolicy.start_date_tax, sbPolicy.benchmark_link, sbPolicy.original_dataset, sbPolicy.type_of_change_code, sbPolicy.type_of_change_name, sbPolicy.measure_descr, sbPolicy.product_original_hs, sbPolicy.product_original_name, sbPolicy.implementationprocedure, sbPolicy.xs_yeartype, sbPolicy.link_pdf, sbPolicy.benchmark_link_pdf ");
//
//        ·         cpl_code
//
//        ·         country_code
//
//        ·         subnational_code
//
//        ·         commoditydomain_code
//
//        ·         commodityclass_code
//
//        ·         policydomain_code
//
//        ·         policytype_code
//
//        ·         policymeasure_code
//
//        ·         condition_code
//
//        ·         individualpolicy_code
//
//        ·         type_of_change_code

        //New order
        select.append("sbPolicy.policy_id, mastertable.cpl_id, mastertable.country_name, mastertable.subnational_name, mastertable.commoditydomain_name, mastertable.commodityclass_name, mastertable.policydomain_name, mastertable.policytype_name, mastertable.policymeasure_name, mastertable.condition, mastertable.individualpolicy_name, sbPolicy.commodity_id, sbPolicy.hs_version, sbPolicy.hs_code, sbPolicy.hs_suffix, sbPolicy.short_description, sbPolicy.description, sbPolicy.shared_group_code, sbPolicy.policy_element, EXTRACT(day FROM sbPolicy.start_date) || '/' || EXTRACT(month FROM sbPolicy.start_date) || '/' || EXTRACT(year FROM sbPolicy.start_date), EXTRACT(day FROM sbPolicy.end_date) || '/' || EXTRACT(month FROM sbPolicy.end_date) || '/' || EXTRACT(year FROM sbPolicy.end_date), sbPolicy.units, sbPolicy.value, sbPolicy.value_text, sbPolicy.value_type, sbPolicy.exemptions, sbPolicy.location_condition, sbPolicy.notes, sbPolicy.link, sbPolicy.source, sbPolicy.title_of_notice, sbPolicy.legal_basis_name, EXTRACT(day FROM sbPolicy.date_of_publication) || '/' || EXTRACT(month FROM sbPolicy.date_of_publication) || '/' || EXTRACT(year FROM sbPolicy.date_of_publication), imposed_end_date, sbPolicy.second_generation_specific, sbPolicy.benchmark_tax, sbPolicy.benchmark_product, sbPolicy.tax_rate_biofuel, sbPolicy.tax_rate_benchmark, sbPolicy.start_date_tax, sbPolicy.benchmark_link, sbPolicy.original_dataset, sbPolicy.type_of_change_name, sbPolicy.measure_descr, sbPolicy.product_original_hs, sbPolicy.product_original_name, sbPolicy.implementationprocedure, sbPolicy.xs_yeartype, sbPolicy.link_pdf, sbPolicy.benchmark_link_pdf,mastertable.cpl_code, mastertable.country_code, mastertable.subnational_code, mastertable.commoditydomain_code, mastertable.commodityclass_code, mastertable.policydomain_code, mastertable.policytype_code, mastertable.policymeasure_code, mastertable.condition_code, mastertable.individualpolicy_code, sbPolicy.type_of_change_code");
        return select;
    }

    public StringBuilder getStringQueryPolicyFromCplIdTime(POLICYDataObject pd_obj, String sbCplId, String sCommodityId) throws Exception{
        //   System.out.println("getPolicyFromCplId start ");
//      select DISTINCT(mastertable.cpl_id) from mastertable, policytable where mastertable.cpl_id=policytable.cpl_id AND ((policytable.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policytable.end_date BETWEEN '05/05/2010' AND '05/04/2011') OR
//      (policytable.end_date IS NULL AND ((policytable.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policytable.start_date < '05/05/2010')))) AND commoditydomain_code IN () AND policydomain_code IN () AND commodityclass_code IN () AND policytype_code IN () AND policymeasure_code IN () AND country_code IN () ORDER BY mastertable.cpl_id;
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT policytable.metadata_id, policytable.policy_id, policytable.cpl_id, policytable.commodity_id, commlistwithid.hs_code, commlistwithid.hs_suffix, commlistwithid.hs_version, commlistwithid.short_description, commlistwithid.description, commlistwithid.shared_group_code, policytable.policy_element, policytable.start_date, policytable.end_date, policytable.units, policytable.value, policytable.value_text, policytable.value_type, policytable.exemptions, policytable.location_condition, policytable.notes, policytable.link, policytable.source, policytable.title_of_notice, policytable.legal_basis_name, policytable.date_of_publication, policytable.imposed_end_date, policytable.second_generation_specific, policytable.benchmark_tax, policytable.benchmark_product, policytable.tax_rate_biofuel, policytable.tax_rate_benchmark, policytable.start_date_tax, policytable.benchmark_link, policytable.original_dataset, policytable.type_of_change_code, policytable.type_of_change_name, policytable.measure_descr, policytable.product_original_hs, policytable.product_original_name, policytable.implementationprocedure, policytable.xs_yeartype, policytable.link_pdf, policytable.benchmark_link_pdf ");
        sb.append("from policytable, commlistwithid where cpl_id IN ("+sbCplId+") AND commlistwithid.commodity_id IN ("+sCommodityId+") AND policytable.commodity_id=commlistwithid.commodity_id ");
        if(pd_obj.getYearTab().equals("classic"))
        {
            if((pd_obj.getYear_list()!=null)&&(!pd_obj.getYear_list().isEmpty()))
            {
                sb.append("AND ((EXTRACT(year FROM start_date) IN ("+pd_obj.getYear_list()+")) OR (EXTRACT(year FROM end_date) IN ("+pd_obj.getYear_list()+")))");
            }
        }
        else
        {   //Format: 01/03/2014
            String start_date = pd_obj.getStart_date();
            String end_date = pd_obj.getEnd_date();
            //((policytable.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policytable.end_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policytable.end_date IS NULL AND ((policytable.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policytable.start_date < '05/05/2010'))))
            sb.append("AND ((policytable.start_date BETWEEN '"+start_date+"' AND '"+end_date+"') OR (policytable.end_date BETWEEN '"+start_date+"' AND '"+end_date+"') OR (policytable.end_date IS NULL AND ((policytable.start_date BETWEEN '"+start_date+"' AND '"+end_date+"') OR (policytable.start_date < '"+start_date+"'))))");
//            sb.append("((policytable.start_date >= '"+start_date+"' AND policytable.start_date <= '"+start_date+"') OR (policytable.end_date >= '"+end_date+"' AND policytable.end_date <= '"+end_date+"') OR (policytable.end_date IS NULL AND (policytable.start_date >= '"+start_date+"' AND policytable.start_date <= '"+start_date+"')))");
        }
        //   System.out.println("getStringQueryPolicyFromCplIdTime sb "+sb.toString());
        return sb;
    }

    public Map<String, LinkedHashMap<String, String>> biofuelPolicies_timeSeries(DatasourceBean dsBean, POLICYDataObject pd_obj) throws Exception{
        // System.out.println("biofuelPolicies_timeSeries start ");
        Map<String, LinkedHashMap<String, String>> pt_map = new LinkedHashMap<String, LinkedHashMap<String, String>>();
        String policy_type_code_array[]= pd_obj.getPolicy_type_code();
        for(int i=0; i<policy_type_code_array.length; i++)
        {
            String single_policy_type = policy_type_code_array[i];
            StringBuilder timeSeries_query = getStringQueryBiofuelPolicyTypes_TimeSeries(pd_obj, single_policy_type);

            final JDBCIterablePolicy it =  new JDBCIterablePolicy();
            it.query(dsBean, timeSeries_query.toString());
            //it.closeConnection();
            LinkedHashMap<String, String> countryCount_map = new LinkedHashMap<String, String>();
            while(it.hasNext()) {
                //[2006-01-01, India]
                //System.out.println(it.next());
                String time_country = it.next().toString();
                String time = time_country.substring(1, time_country.indexOf(','));
                // System.out.println("single_policy_type "+single_policy_type+" time_country= "+time_country + " time "+time );
                if(countryCount_map.containsKey(time))
                {
                    String country_cont= countryCount_map.get(time);
                    Integer count = Integer.parseInt(country_cont);
                    count++;
                    countryCount_map.put(time, ""+count);
//                    System.out.println("single_policy_type "+single_policy_type+" time_country= "+time_country + " time "+time+" count "+countryCount_map.get(time) );
                    //countryCount_map.get(time).
                }
                else
                {
                    countryCount_map.put(time, "1");

                }
                // System.out.println("single_policy_type "+single_policy_type+" time_country= "+time_country + " time "+time+" count "+countryCount_map.get(time) );
            }
            pt_map.put(policy_type_code_array[i], countryCount_map);
        }

        return pt_map;
    }

    public Map<String, LinkedHashMap<String, String>> biofuelPolicyMeasures_timeSeries(DatasourceBean dsBean, POLICYDataObject pd_obj) throws Exception{
        // System.out.println("biofuelPolicies_timeSeries start ");
        Map<String, LinkedHashMap<String, String>> pt_map = new LinkedHashMap<String, LinkedHashMap<String, String>>();
        String policy_type_code_array[]= pd_obj.getPolicy_type_code();
        String single_policy_type = policy_type_code_array[0];
        String policy_meaure_code_array[]= pd_obj.getPolicy_measure_code();
        for(int i=0; i<policy_meaure_code_array.length; i++)
        {
            String single_policy_measure = policy_meaure_code_array[i];
            StringBuilder timeSeries_query = getStringQueryBiofuelPolicyMeasures_TimeSeries(pd_obj, single_policy_type, single_policy_measure);

            final JDBCIterablePolicy it =  new JDBCIterablePolicy();
            it.query(dsBean, timeSeries_query.toString());
            //it.closeConnection();
            LinkedHashMap<String, String> countryCount_map = new LinkedHashMap<String, String>();
            while(it.hasNext()) {
                //[2006-01-01, India]
                //System.out.println(it.next());
                String time_country = it.next().toString();
                String time = time_country.substring(1, time_country.indexOf(','));
                // System.out.println("single_policy_type "+single_policy_type+" time_country= "+time_country + " time "+time );
                if(countryCount_map.containsKey(time))
                {
                    String country_cont= countryCount_map.get(time);
                    Integer count = Integer.parseInt(country_cont);
                    count++;
                    countryCount_map.put(time, ""+count);
//                    System.out.println("single_policy_type "+single_policy_type+" time_country= "+time_country + " time "+time+" count "+countryCount_map.get(time) );
                    //countryCount_map.get(time).
                }
                else
                {
                    countryCount_map.put(time, "1");

                }
                // System.out.println("single_policy_type "+single_policy_type+" time_country= "+time_country + " time "+time+" count "+countryCount_map.get(time) );
            }
            pt_map.put(policy_meaure_code_array[i], countryCount_map);
        }

        return pt_map;
    }

    public Map<String, LinkedHashMap<String, String>> exportRestrictionsPolicyMeasures_timeSeries(DatasourceBean dsBean, POLICYDataObject pd_obj) throws Exception{
        // System.out.println("biofuelPolicies_timeSeries start ");
        Map<String, LinkedHashMap<String, String>> pt_map = new LinkedHashMap<String, LinkedHashMap<String, String>>();
        String policy_type_code_array[]= pd_obj.getPolicy_type_code();
        String single_policy_type = policy_type_code_array[0];
        String policy_meaure_code_array[]= pd_obj.getPolicy_measure_code();
        for(int i=0; i<policy_meaure_code_array.length; i++)
        {
            String single_policy_measure = policy_meaure_code_array[i];
            StringBuilder timeSeries_query = getStringQueryExportRestrictionsPolicyMeasures_TimeSeries(pd_obj, single_policy_type, single_policy_measure);

            final JDBCIterablePolicy it =  new JDBCIterablePolicy();
            it.query(dsBean, timeSeries_query.toString());
            //it.closeConnection();
            LinkedHashMap<String, String> countryCount_map = new LinkedHashMap<String, String>();
            while(it.hasNext()) {
                //[2006-01-01, India]
                //System.out.println(it.next());
                String time_country = it.next().toString();
                String time = time_country.substring(1, time_country.indexOf(','));
                // System.out.println("single_policy_type "+single_policy_type+" time_country= "+time_country + " time "+time );
                if(countryCount_map.containsKey(time))
                {
                    String country_cont= countryCount_map.get(time);
                    Integer count = Integer.parseInt(country_cont);
                    count++;
                    countryCount_map.put(time, ""+count);
//                    System.out.println("single_policy_type "+single_policy_type+" time_country= "+time_country + " time "+time+" count "+countryCount_map.get(time) );
                    //countryCount_map.get(time).
                }
                else
                {
                    countryCount_map.put(time, "1");

                }
                // System.out.println("single_policy_type "+single_policy_type+" time_country= "+time_country + " time "+time+" count "+countryCount_map.get(time) );
            }
            pt_map.put(policy_meaure_code_array[i], countryCount_map);
        }

        return pt_map;
    }

    public Map<String, LinkedHashMap<String, String>> exportSubsidiesPolicyMeasures_timeSeries(DatasourceBean dsBean, POLICYDataObject pd_obj, String cpl_id) throws Exception{
         System.out.println("biofuelPolicies_timeSeries start ");
        String start_date = pd_obj.getStart_date();
        System.out.println("start_date="+start_date);
        //1995-01-01
//        int year_index = start_date.lastIndexOf('/');
//        String year = start_date.substring(year_index+1);
        String year = start_date.substring(0,4);
        Integer start_year_integer = Integer.parseInt(year);
        System.out.println("biofuelPolicies_timeSeries start_year_integer= "+start_year_integer);
        String end_date = pd_obj.getEnd_date();
//        year_index = end_date.lastIndexOf('/');
//        year = end_date.substring(year_index+1);
        year = end_date.substring(0,4);
        Integer end_year_integer = Integer.parseInt(year);
        System.out.println("biofuelPolicies_timeSeries end_year_integer= "+end_year_integer);

        Map<String, LinkedHashMap<String, String>> pelem_map = new LinkedHashMap<String, LinkedHashMap<String, String>>();

        String policy_element_array[]= pd_obj.getPolicy_element();
        System.out.println("policy_element_array= "+policy_element_array);
        System.out.println(policy_element_array);
       // String policy_element_array[]= null;

        for(int i=0; i<policy_element_array.length; i++)
        {
            String single_policy_element = "'"+policy_element_array[i]+"'";
            System.out.println("single_policy_element= "+single_policy_element);
            StringBuilder timeSeries_query = getStringQueryExportSubsidiesPolicyMeasures_TimeSeries(pd_obj, cpl_id, single_policy_element);

            final JDBCIterablePolicy it =  new JDBCIterablePolicy();
            it.query(dsBean, timeSeries_query.toString());
            //it.closeConnection();
            LinkedHashMap<String, String> policyElementValue_map = new LinkedHashMap<String, String>();
            for(int j=start_year_integer; j<=end_year_integer; j++)
            {
                policyElementValue_map.put(""+j,"");
            }
            while(it.hasNext()) {
                //[1995, 2238378.1]
                //System.out.println(it.next());
                String time_policyElem = it.next().toString();
                String time = time_policyElem.substring(1, time_policyElem.indexOf(','));
                String value = time_policyElem.substring(time_policyElem.indexOf(',')+1, time_policyElem.length()-1);
                // System.out.println("single_policy_type "+single_policy_type+" time_country= "+time_country + " time "+time );
                policyElementValue_map.put(time,value);
            }
            pelem_map.put(policy_element_array[i], policyElementValue_map);
        }

        return pelem_map;
    }

    public StringBuilder getStringQueryBiofuelPolicyTypes_TimeSeries(POLICYDataObject pd_obj, String single_policy_type) throws Exception{

        //select to_char(generate_series(tot.start_date::timestamp, CASE WHEN tot.end_date IS NULL THEN CURRENT_DATE ELSE tot.end_date END, '1 DAY'), 'YYYY-MM-DD') AS byday, tot.country_name from (select mastertable.cpl_id, mastertable.policytype_code, mastertable.policytype_name, mastertable.country_code, mastertable.country_name, policy.start_date, policy.end_date from mastertable JOIN (select policytable.cpl_id, policytable.start_date, policytable.end_date from policytable)policy ON mastertable.cpl_id = policy.cpl_id AND mastertable.commodityclass_code IN (5,6,7) AND ((policy.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.end_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.end_date IS NULL AND ((policy.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.start_date < '05/05/2010'))))) tot where tot.policytype_code=6 GROUP BY tot.cpl_id, tot.policytype_code, tot.policytype_name, tot.country_code, tot.country_name, byday ORDER BY byday limit 50;
        //select a.byday, a.country_name from (select to_char(generate_series(tot.start_date::timestamp, CASE WHEN tot.end_date IS NULL THEN CURRENT_DATE ELSE tot.end_date END, '1 DAY'), 'YYYY-MM-DD') AS byday, tot.country_name from (select mastertable.cpl_id, mastertable.policytype_code, mastertable.policytype_name, mastertable.country_code, mastertable.country_name, policy.start_date, policy.end_date from mastertable JOIN (select policytable.cpl_id, policytable.start_date, policytable.end_date from policytable)policy ON mastertable.cpl_id = policy.cpl_id AND mastertable.commodityclass_code IN (5,6,7) AND ((policy.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.end_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.end_date IS NULL AND ((policy.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.start_date < '05/05/2010'))))) tot where tot.policytype_code=9 GROUP BY tot.cpl_id, tot.policytype_code, tot.policytype_name, tot.country_code, tot.country_name, byday ORDER BY byday)a GROUP BY a.byday, a.country_name order by a.byday limit 20;
        String start_date = pd_obj.getStart_date();
        String end_date = pd_obj.getEnd_date();
        StringBuilder sb = new StringBuilder();
        sb.append("select a.byday, a.country_name from (");
        //sb.append("select to_char(generate_series(tot.start_date::timestamp, CASE WHEN tot.end_date IS NULL THEN CURRENT_DATE ELSE tot.end_date END, '1 DAY'), 'YYYY-MM-DD') AS byday, tot.country_name");
//        sb.append("select to_char(generate_series('"+start_date+"'::timestamp, CASE WHEN tot.end_date IS NULL THEN CURRENT_DATE ELSE tot.end_date END, '1 DAY'), 'YYYY-MM-DD') AS byday, tot.country_name");
//        sb.append("select to_char(generate_series('"+start_date+"'::timestamp, CASE WHEN tot.end_date IS NULL THEN CURRENT_DATE ELSE tot.end_date END, interval '1 mon'), 'YYYY-MM') AS byday, tot.country_name");
//        sb.append("select to_char(generate_series(tot.start_date::timestamp, CASE WHEN tot.end_date IS NULL THEN '2024-12-31' ELSE tot.end_date END, interval '1 mon'), 'YYYY-MM-DD') AS byday, tot.country_name");
        start_date = "2003-01-01";
//        end_date = "2020-12-31";
        end_date = "2024-12-31";
        sb.append("select to_char(generate_series('"+start_date+"'::timestamp, CASE WHEN tot.end_date IS NULL THEN '"+end_date+"' ELSE tot.end_date END, interval '1 mon'), 'YYYY-MM-DD') AS byday, tot.country_name");
        sb.append(" from (select mastertable.cpl_id, mastertable.policytype_code, mastertable.policytype_name, mastertable.country_code, mastertable.country_name, policy.start_date, policy.end_date from mastertable");
        sb.append(" JOIN (select policytable.cpl_id, policytable.start_date, policytable.end_date from policytable)policy");
        sb.append(" ON mastertable.cpl_id = policy.cpl_id AND mastertable.commodityclass_code IN ("+pd_obj.getCommodity_class_code()+")");
//        sb.append(" ((policy.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.end_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.end_date IS NULL AND ((policy.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.start_date < '05/05/2010'))))) tot ");
        sb.append(") tot ");
        sb.append(" where tot.policytype_code= "+single_policy_type+" GROUP BY tot.cpl_id, tot.policytype_code, tot.policytype_name, tot.country_code, tot.country_name, byday ORDER BY byday ASC");
        sb.append(")a GROUP BY a.byday, a.country_name order by a.byday ASC");
        //System.out.println("getStringQueryBiofuelPolicies_TimeSeries sb "+sb.toString());

        return sb;
    }

    public StringBuilder getStringQueryBiofuelPolicyMeasures_TimeSeries(POLICYDataObject pd_obj, String single_policy_type, String single_policy_measure) throws Exception{

        //select to_char(generate_series(tot.start_date::timestamp, CASE WHEN tot.end_date IS NULL THEN CURRENT_DATE ELSE tot.end_date END, '1 DAY'), 'YYYY-MM-DD') AS byday, tot.country_name from (select mastertable.cpl_id, mastertable.policytype_code, mastertable.policytype_name, mastertable.country_code, mastertable.country_name, policy.start_date, policy.end_date from mastertable JOIN (select policytable.cpl_id, policytable.start_date, policytable.end_date from policytable)policy ON mastertable.cpl_id = policy.cpl_id AND mastertable.commodityclass_code IN (5,6,7) AND ((policy.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.end_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.end_date IS NULL AND ((policy.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.start_date < '05/05/2010'))))) tot where tot.policytype_code=6 GROUP BY tot.cpl_id, tot.policytype_code, tot.policytype_name, tot.country_code, tot.country_name, byday ORDER BY byday limit 50;
        //select a.byday, a.country_name from (select to_char(generate_series(tot.start_date::timestamp, CASE WHEN tot.end_date IS NULL THEN CURRENT_DATE ELSE tot.end_date END, '1 DAY'), 'YYYY-MM-DD') AS byday, tot.country_name from (select mastertable.cpl_id, mastertable.policytype_code, mastertable.policytype_name, mastertable.country_code, mastertable.country_name, policy.start_date, policy.end_date from mastertable JOIN (select policytable.cpl_id, policytable.start_date, policytable.end_date from policytable)policy ON mastertable.cpl_id = policy.cpl_id AND mastertable.commodityclass_code IN (5,6,7) AND ((policy.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.end_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.end_date IS NULL AND ((policy.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.start_date < '05/05/2010'))))) tot where tot.policytype_code=9 GROUP BY tot.cpl_id, tot.policytype_code, tot.policytype_name, tot.country_code, tot.country_name, byday ORDER BY byday)a GROUP BY a.byday, a.country_name order by a.byday limit 20;
        String start_date = pd_obj.getStart_date();
        String end_date = pd_obj.getEnd_date();
        StringBuilder sb = new StringBuilder();
        start_date = "2003-01-01";
        //end_date = "2020-12-31";
        end_date = "2024-12-31";
        sb.append("select a.byday, a.country_name from (");
//        sb.append("select to_char(generate_series('"+start_date+"'::timestamp, CASE WHEN tot.end_date IS NULL THEN CURRENT_DATE ELSE tot.end_date END, interval '1 mon'), 'YYYY-MM-DD') AS byday, tot.country_name");
        //sb.append("select to_char(generate_series(tot.start_date::timestamp, CASE WHEN tot.end_date IS NULL THEN '2024-12-31' ELSE tot.end_date END, interval '1 mon'), 'YYYY-MM-DD') AS byday, tot.country_name");
        sb.append("select to_char(generate_series('"+start_date+"'::timestamp, CASE WHEN tot.end_date IS NULL THEN '"+end_date+"' ELSE tot.end_date END, interval '1 mon'), 'YYYY-MM-DD') AS byday, tot.country_name");
        sb.append(" from (select mastertable.cpl_id, mastertable.policytype_code, mastertable.policytype_name, mastertable.policymeasure_code, mastertable.policymeasure_name, mastertable.country_code, mastertable.country_name, policy.start_date, policy.end_date from mastertable");
        sb.append(" JOIN (select policytable.cpl_id, policytable.start_date, policytable.end_date from policytable)policy");
        sb.append(" ON mastertable.cpl_id = policy.cpl_id AND mastertable.commodityclass_code IN ("+pd_obj.getCommodity_class_code()+")");
//        sb.append(" ((policy.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.end_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.end_date IS NULL AND ((policy.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.start_date < '05/05/2010'))))) tot ");
        sb.append(") tot ");
        sb.append(" where tot.policytype_code= "+single_policy_type+" and tot.policymeasure_code= "+single_policy_measure+" GROUP BY tot.cpl_id, tot.policytype_code, tot.policytype_name, tot.policymeasure_code, tot.policymeasure_name, tot.country_code, tot.country_name, byday ORDER BY byday ASC");
        sb.append(")a GROUP BY a.byday, a.country_name order by a.byday ASC");
        System.out.println("getStringQueryBiofuelPolicyMeasures_TimeSeries sb "+sb.toString());

        return sb;
    }

    //(pd_obj, cpl_id, single_policy_element)
    public StringBuilder getStringQueryExportSubsidiesPolicyMeasures_TimeSeries(POLICYDataObject pd_obj, String cpl_id, String single_policy_element) throws Exception{
        System.out.println("getStringQueryExportSubsidiesPolicyMeasures_TimeSeries start");
        StringBuilder sb = new StringBuilder();
        sb.append("select EXTRACT(year FROM start_date) AS year_label, SUM(CAST (value AS FLOAT)) from policytable where cpl_id IN("+cpl_id+") and (EXTRACT(year FROM start_date)>=1995 and EXTRACT(year FROM start_date)<=2011) and policy_element ="+single_policy_element+" GROUP BY year_label ORDER BY year_label ASC;");
        System.out.println("getStringQueryExportSubsidiesPolicyMeasures_TimeSeries end");
        System.out.println("sb = "+sb.toString());
        return sb;
    }

    public StringBuilder getStringQueryExportRestrictionsPolicyMeasures_TimeSeries(POLICYDataObject pd_obj, String single_policy_type, String single_policy_measure) throws Exception{

        //select to_char(generate_series(tot.start_date::timestamp, CASE WHEN tot.end_date IS NULL THEN CURRENT_DATE ELSE tot.end_date END, '1 DAY'), 'YYYY-MM-DD') AS byday, tot.country_name from (select mastertable.cpl_id, mastertable.policytype_code, mastertable.policytype_name, mastertable.country_code, mastertable.country_name, policy.start_date, policy.end_date from mastertable JOIN (select policytable.cpl_id, policytable.start_date, policytable.end_date from policytable)policy ON mastertable.cpl_id = policy.cpl_id AND mastertable.commodityclass_code IN (5,6,7) AND ((policy.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.end_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.end_date IS NULL AND ((policy.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.start_date < '05/05/2010'))))) tot where tot.policytype_code=6 GROUP BY tot.cpl_id, tot.policytype_code, tot.policytype_name, tot.country_code, tot.country_name, byday ORDER BY byday limit 50;
        //select a.byday, a.country_name from (select to_char(generate_series(tot.start_date::timestamp, CASE WHEN tot.end_date IS NULL THEN CURRENT_DATE ELSE tot.end_date END, '1 DAY'), 'YYYY-MM-DD') AS byday, tot.country_name from (select mastertable.cpl_id, mastertable.policytype_code, mastertable.policytype_name, mastertable.country_code, mastertable.country_name, policy.start_date, policy.end_date from mastertable JOIN (select policytable.cpl_id, policytable.start_date, policytable.end_date from policytable)policy ON mastertable.cpl_id = policy.cpl_id AND mastertable.commodityclass_code IN (5,6,7) AND ((policy.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.end_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.end_date IS NULL AND ((policy.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.start_date < '05/05/2010'))))) tot where tot.policytype_code=9 GROUP BY tot.cpl_id, tot.policytype_code, tot.policytype_name, tot.country_code, tot.country_name, byday ORDER BY byday)a GROUP BY a.byday, a.country_name order by a.byday limit 20;
        String start_date = pd_obj.getStart_date();
        String end_date = pd_obj.getEnd_date();
        StringBuilder sb = new StringBuilder();
        sb.append("select a.byday, a.country_name from (");
        start_date = "2006-01-01";
//        end_date = "2014-12-31";
        end_date = "2024-12-31";
//        sb.append("select to_char(generate_series('"+start_date+"'::timestamp, CASE WHEN tot.end_date IS NULL THEN CURRENT_DATE ELSE tot.end_date END, interval '1 mon'), 'YYYY-MM-DD') AS byday, tot.country_name");
        //sb.append("select to_char(generate_series(tot.start_date::timestamp, CASE WHEN tot.end_date IS NULL THEN '2024-12-31' ELSE tot.end_date END, interval '1 mon'), 'YYYY-MM-DD') AS byday, tot.country_name");
        sb.append("select to_char(generate_series('"+start_date+"'::timestamp, CASE WHEN tot.end_date IS NULL THEN '"+end_date+"' ELSE tot.end_date END, interval '1 mon'), 'YYYY-MM-DD') AS byday, tot.country_name");
        sb.append(" from (select mastertable.cpl_id, mastertable.policytype_code, mastertable.policytype_name, mastertable.policymeasure_code, mastertable.policymeasure_name, mastertable.country_code, mastertable.country_name, policy.start_date, policy.end_date, policy.value_text from mastertable");
        sb.append(" JOIN (select policytable.cpl_id, policytable.start_date, policytable.end_date, policytable.value_text from policytable)policy");
        sb.append(" ON mastertable.cpl_id = policy.cpl_id AND mastertable.commodityclass_code IN ("+pd_obj.getCommodity_class_code()+")");
//        sb.append(" ((policy.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.end_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.end_date IS NULL AND ((policy.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.start_date < '05/05/2010'))))) tot ");
        sb.append(") tot ");
        sb.append(" where tot.policytype_code= "+single_policy_type+" and tot.policymeasure_code= "+single_policy_measure+" and (tot.value_text<>'elim' OR tot.value_text IS NULL) GROUP BY tot.cpl_id, tot.policytype_code, tot.policytype_name, tot.policymeasure_code, tot.policymeasure_name, tot.country_code, tot.country_name, byday ORDER BY byday ASC");
        sb.append(")a GROUP BY a.byday, a.country_name order by a.byday ASC");
        //System.out.println("getStringQueryExportRestrictionsPolicyMeasures_TimeSeries sb "+sb.toString());

        return sb;
    }

    //Old with Start and End Date Start
//    public StringBuilder getStringQueryBiofuelPolicies_TimeSeries(POLICYDataObject pd_obj, String single_policy_type) throws Exception{
//
//        //select to_char(generate_series(tot.start_date::timestamp, CASE WHEN tot.end_date IS NULL THEN CURRENT_DATE ELSE tot.end_date END, '1 DAY'), 'YYYY-MM-DD') AS byday, tot.country_name from (select mastertable.cpl_id, mastertable.policytype_code, mastertable.policytype_name, mastertable.country_code, mastertable.country_name, policy.start_date, policy.end_date from mastertable JOIN (select policytable.cpl_id, policytable.start_date, policytable.end_date from policytable)policy ON mastertable.cpl_id = policy.cpl_id AND mastertable.commodityclass_code IN (5,6,7) AND ((policy.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.end_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.end_date IS NULL AND ((policy.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.start_date < '05/05/2010'))))) tot where tot.policytype_code=6 GROUP BY tot.cpl_id, tot.policytype_code, tot.policytype_name, tot.country_code, tot.country_name, byday ORDER BY byday limit 50;
//        //select a.byday, a.country_name from (select to_char(generate_series(tot.start_date::timestamp, CASE WHEN tot.end_date IS NULL THEN CURRENT_DATE ELSE tot.end_date END, '1 DAY'), 'YYYY-MM-DD') AS byday, tot.country_name from (select mastertable.cpl_id, mastertable.policytype_code, mastertable.policytype_name, mastertable.country_code, mastertable.country_name, policy.start_date, policy.end_date from mastertable JOIN (select policytable.cpl_id, policytable.start_date, policytable.end_date from policytable)policy ON mastertable.cpl_id = policy.cpl_id AND mastertable.commodityclass_code IN (5,6,7) AND ((policy.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.end_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.end_date IS NULL AND ((policy.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.start_date < '05/05/2010'))))) tot where tot.policytype_code=9 GROUP BY tot.cpl_id, tot.policytype_code, tot.policytype_name, tot.country_code, tot.country_name, byday ORDER BY byday)a GROUP BY a.byday, a.country_name order by a.byday limit 20;
//        String start_date = pd_obj.getStart_date();
//        String end_date = pd_obj.getEnd_date();
//        StringBuilder sb = new StringBuilder();
//        sb.append("select a.byday, a.country_name from (");
//        sb.append("select to_char(generate_series(tot.start_date::timestamp, CASE WHEN tot.end_date IS NULL THEN CURRENT_DATE ELSE tot.end_date END, '1 DAY'), 'YYYY-MM-DD') AS byday, tot.country_name");
//        sb.append(" from (select mastertable.cpl_id, mastertable.policytype_code, mastertable.policytype_name, mastertable.country_code, mastertable.country_name, policy.start_date, policy.end_date from mastertable");
//        sb.append(" JOIN (select policytable.cpl_id, policytable.start_date, policytable.end_date from policytable)policy");
//        sb.append(" ON mastertable.cpl_id = policy.cpl_id AND mastertable.commodityclass_code IN ("+pd_obj.getCommodity_class_code()+") AND ");
////        sb.append(" ((policy.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.end_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.end_date IS NULL AND ((policy.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.start_date < '05/05/2010'))))) tot ");
//        sb.append(" ((policy.start_date BETWEEN '"+start_date+"' AND '"+end_date+"') OR (policy.end_date BETWEEN '"+start_date+"' AND '"+end_date+"') OR (policy.end_date IS NULL AND ((policy.start_date BETWEEN '"+start_date+"' AND '"+end_date+"') OR (policy.start_date < '"+start_date+"'))))) tot ");
//        sb.append(" where tot.policytype_code= "+single_policy_type+" GROUP BY tot.cpl_id, tot.policytype_code, tot.policytype_name, tot.country_code, tot.country_name, byday ORDER BY byday ASC");
//        sb.append(")a GROUP BY a.byday, a.country_name order by a.byday ASC");
//        System.out.println("getStringQueryBiofuelPolicies_TimeSeries sb "+sb.toString());
//
//        return sb;
//    }
    //Old with Start and End Date End

    public JDBCIterablePolicy getpolicyTypes_fromPolicyDomain(DatasourceBean dsBean, String commodityDomainCodes) throws Exception {
        //select DISTINCT(policytype_code), policytype_name from mastertable where commoditydomain_code = 2 order by policytype_code;
        JDBCIterablePolicy it = new JDBCIterablePolicy();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT DISTINCT(policytype_code), policytype_name FROM mastertable where commoditydomain_code IN ("+commodityDomainCodes+") order by policytype_name ASC ");
        // System.out.println("getpolicyTypes_fromPolicyDomain sb "+sb.toString());
        it.query(dsBean, sb.toString());
        return it;
    }

    public Map<String, LinkedHashMap<String, String>> biofuelPolicies_barchart(DatasourceBean dsBean, POLICYDataObject pd_obj) throws Exception{
        String commodity_class_code_array[]= pd_obj.getCommodity_class_code().split(",");
        System.out.println("pd_obj.getCommodity_class_code() "+pd_obj.getCommodity_class_code()+" commodity_class_code_array "+commodity_class_code_array);
        String policy_type_code_array[]= pd_obj.getPolicy_type_code();
        Map<String, LinkedHashMap<String, String>> pt_map = new LinkedHashMap<String, LinkedHashMap<String, String>>();
        for(int i=0; i<policy_type_code_array.length; i++)
        {
            String single_policy_type = policy_type_code_array[i];
            System.out.println("single_policy_type "+single_policy_type);
            LinkedHashMap<String, String> countryCount_map = new LinkedHashMap<String, String>();
            for(int j=0; j<commodity_class_code_array.length; j++)
            {
                String single_commodity_class = commodity_class_code_array[j];
                StringBuilder barchart_query = getStringQueryBiofuelPolicyTypes_barchart(pd_obj, single_policy_type, single_commodity_class);

                final JDBCIterablePolicy it =  new JDBCIterablePolicy();
                it.query(dsBean, barchart_query.toString());
                //Counter for the country
                String country_count = "0";
                //Return one value that is the number of country
                while(it.hasNext()) {
                    //[8] -> 8
                    String next = it.next().toString();
                    int lastsquare = next.lastIndexOf("]");
                    country_count = next.substring(1,lastsquare);
                }
                //It will have three values
                System.out.println("single_commodity_class "+single_commodity_class+" country_count "+country_count);
                countryCount_map.put(single_commodity_class, country_count);
            }
            pt_map.put(single_policy_type, countryCount_map);
        }
        return pt_map;
    }

    public Map<String, LinkedHashMap<String, String>> biofuelMeasures_barchart(DatasourceBean dsBean, POLICYDataObject pd_obj) throws Exception{
        String commodity_class_code_array[]= pd_obj.getCommodity_class_code().split(",");
        //System.out.println("pd_obj.getCommodity_class_code() "+pd_obj.getCommodity_class_code()+" commodity_class_code_array "+commodity_class_code_array);
        String policy_type_code_array[]= pd_obj.getPolicy_type_code();
        String policy_measures_code_array[]= pd_obj.getPolicy_measure_code();
        //The request is for all the Policy Measures of a specific Policy Type
        String single_policy_type = policy_type_code_array[0];
        Map<String, LinkedHashMap<String, String>> pt_map = new LinkedHashMap<String, LinkedHashMap<String, String>>();
        for(int i=0; i<policy_measures_code_array.length; i++)
        {
            //System.out.println("single_policy_type "+single_policy_type);
            String single_policy_measure = policy_measures_code_array[i];
            LinkedHashMap<String, String> countryCount_map = new LinkedHashMap<String, String>();
            for(int j=0; j<commodity_class_code_array.length; j++)
            {
                String single_commodity_class = commodity_class_code_array[j];
                StringBuilder barchart_query = getStringQueryBiofuelPolicyMeasures_barchart(pd_obj, single_policy_type, single_policy_measure, single_commodity_class);

                final JDBCIterablePolicy it =  new JDBCIterablePolicy();
                it.query(dsBean, barchart_query.toString());
                //Counter for the country
                String country_count = "0";
                //Return one value that is the number of country
                while(it.hasNext()) {
                    //[8] -> 8
                    String next = it.next().toString();
                    int lastsquare = next.lastIndexOf("]");
                    country_count = next.substring(1,lastsquare);
                    //System.out.println("country_count "+country_count);
                }
                //It will have three values
                //System.out.println("single_commodity_class "+single_commodity_class+" country_count "+country_count);
                countryCount_map.put(single_commodity_class, country_count);
            }
            pt_map.put(single_policy_measure, countryCount_map);
        }

        return pt_map;
    }

    public Map<String, LinkedHashMap<String, String>> importTariffs_barchart(DatasourceBean dsBean, POLICYDataObject pd_obj) throws Exception{
        String commodity_class_code_array[]= pd_obj.getCommodity_class_code().split(",");
        //System.out.println("pd_obj.getCommodity_class_code() "+pd_obj.getCommodity_class_code()+" commodity_class_code_array "+commodity_class_code_array);
        String policy_type_code_array[]= pd_obj.getPolicy_type_code();
        String policy_measures_code_array[]= pd_obj.getPolicy_measure_code();
        String policy_element[]= pd_obj.getPolicy_element();
        String year_list= pd_obj.getYear_list();
        String year_list_array[]= year_list.split(",");
        String year_list_string = "";
        for(int iYear = 0; iYear<year_list_array.length; iYear++)
        {
            year_list_string += "'"+year_list_array[iYear]+"-01-01',";
        }

        if(year_list.length()>0)
        {
            year_list_string = year_list_string.substring(0,year_list_string.length()-1);
        }
        //'Final bound tariff' and 'MFN applied tariff'

        boolean min = pd_obj.getChartType();
        String chart_type = "fbt";
        StringBuilder barchart_query_fbt = getStringQueryImportTariffs_barchart(pd_obj, min, chart_type, policy_type_code_array[0], policy_measures_code_array[0], year_list_string);
        final JDBCIterablePolicy it_fbt =  new JDBCIterablePolicy();
        chart_type = "mfn";
        StringBuilder barchart_query_mfn = getStringQueryImportTariffs_barchart(pd_obj, min, chart_type, policy_type_code_array[0], policy_measures_code_array[0], year_list_string);
        final JDBCIterablePolicy it_mfn =  new JDBCIterablePolicy();

//        LinkedHashMap<String, String> commodity_policyElem_map = new LinkedHashMap<String, String>();
        Map<String, LinkedHashMap<String, String>> commodity_policyElem_map = new LinkedHashMap<String, LinkedHashMap<String, String>>();
        for(int i=0; i<commodity_class_code_array.length; i++)
        {
            for(int j=0; j<policy_element.length; j++)
            {
                String key = ""+commodity_class_code_array[i]+"_"+policy_element[j].replaceAll("\\s+","");

                LinkedHashMap<String, String> yearPercentage_map = new LinkedHashMap<String, String>();
                for(int z=0; z<year_list_array.length; z++)
                {
                    yearPercentage_map.put(year_list_array[z], "");
                }
                commodity_policyElem_map.put(key, yearPercentage_map);
            }
        }

        it_fbt.query(dsBean, barchart_query_fbt.toString());
        while(it_fbt.hasNext()) {
            String next = it_fbt.next().toString();
            //[18, 1, Final bound tariff, 2010-01-01]
            String row = (next.substring(1, next.length()-1)).replaceAll("\\s+","");
            String row_array[] = row.split(",");
            String percentage = row_array[0];
            String commodity = row_array[1];
            String policy_element_i = row_array[2];
            String year = row_array[3];
            int year_index = year.indexOf("-01-01");
            year = year.substring(0, year_index);
            LinkedHashMap<String, String> yearPercentage_map_2 = commodity_policyElem_map.get(""+commodity+"_"+policy_element_i);
            yearPercentage_map_2.put(year, percentage);
        }

        it_mfn.query(dsBean, barchart_query_mfn.toString());
        while(it_mfn.hasNext()) {
            String next = it_mfn.next().toString();
            //[80, 2, MFN applied tariff, 2012-01-01]
            String row = (next.substring(1, next.length()-1)).replaceAll("\\s+","");
            String row_array[] = row.split(",");
            String percentage = row_array[0];
            String commodity = row_array[1];
            String policy_element_i = row_array[2];
            String year = row_array[3];
            int year_index = year.indexOf("-01-01");
            year = year.substring(0, year_index);
            LinkedHashMap<String, String> yearPercentage_map_2 = commodity_policyElem_map.get(""+commodity+"_"+policy_element_i);
            yearPercentage_map_2.put(year, percentage);
        }
        return commodity_policyElem_map;
    }

    public Map<String, LinkedHashMap<String, String>> exportRestrictionsMeasures_barchart(DatasourceBean dsBean, POLICYDataObject pd_obj) throws Exception{
        String commodity_class_code_array[]= pd_obj.getCommodity_class_code().split(",");
        //System.out.println("pd_obj.getCommodity_class_code() "+pd_obj.getCommodity_class_code()+" commodity_class_code_array "+commodity_class_code_array);
        String policy_type_code_array[]= pd_obj.getPolicy_type_code();
        String policy_measures_code_array[]= pd_obj.getPolicy_measure_code();
        //The request is for all the Policy Measures of a specific Policy Type
        String single_policy_type = policy_type_code_array[0];
        Map<String, LinkedHashMap<String, String>> pt_map = new LinkedHashMap<String, LinkedHashMap<String, String>>();
        for(int i=0; i<policy_measures_code_array.length; i++)
        {
            //System.out.println("single_policy_type "+single_policy_type);
            String single_policy_measure = policy_measures_code_array[i];
            LinkedHashMap<String, String> countryCount_map = new LinkedHashMap<String, String>();
            for(int j=0; j<commodity_class_code_array.length; j++)
            {
                String single_commodity_class = commodity_class_code_array[j];
                StringBuilder barchart_query = getStringQueryExportRestrictionsPolicyMeasures_barchart(pd_obj, single_policy_type, single_policy_measure, single_commodity_class);

                final JDBCIterablePolicy it =  new JDBCIterablePolicy();
                it.query(dsBean, barchart_query.toString());
                //Counter for the country
                String country_count = "0";
                //Return one value that is the number of country
                while(it.hasNext()) {
                    //[8] -> 8
                    String next = it.next().toString();
                    int lastsquare = next.lastIndexOf("]");
                    country_count = next.substring(1,lastsquare);
                    //System.out.println("country_count "+country_count);
                }
                //It will have three values
                //System.out.println("single_commodity_class "+single_commodity_class+" country_count "+country_count);
                countryCount_map.put(single_commodity_class, country_count);
            }
            pt_map.put(single_policy_measure, countryCount_map);
        }

        return pt_map;
    }

    public StringBuilder getStringQueryBiofuelPolicyTypes_barchart(POLICYDataObject pd_obj, String single_policy_type, String single_commodity_class) throws Exception{

        //select count(DISTINCT(tot.country_code)) from (select mastertable.cpl_id, mastertable.commodityclass_code, mastertable.commodityclass_name, mastertable.policytype_code, mastertable.policytype_name, mastertable.country_code, mastertable.country_name, policy.start_date, policy.end_date from mastertable JOIN (select policytable.cpl_id, policytable.start_date, policytable.end_date from policytable)policy ON mastertable.cpl_id = policy.cpl_id AND mastertable.commodityclass_code IN (5,6,7) AND ((policy.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.end_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.end_date IS NULL AND ((policy.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.start_date < '05/05/2010'))))) tot where tot.policytype_code=2 and tot.commodityclass_code=5;
        String start_date = pd_obj.getStart_date();
        String end_date = pd_obj.getEnd_date();
        StringBuilder sb = new StringBuilder();
        sb.append("select count(DISTINCT(tot.country_code)) from (select mastertable.cpl_id, mastertable.commodityclass_code, mastertable.commodityclass_name, mastertable.policytype_code, mastertable.policytype_name, mastertable.country_code, mastertable.country_name, policy.start_date, policy.end_date from mastertable ");
//        sb.append("JOIN (select policytable.cpl_id, policytable.start_date, policytable.end_date from policytable)policy ON mastertable.cpl_id = policy.cpl_id AND mastertable.commodityclass_code IN (5,6,7) AND ((policy.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.end_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.end_date IS NULL AND ((policy.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.start_date < '05/05/2010'))))) tot ");
        sb.append("JOIN (select policytable.cpl_id, policytable.start_date, policytable.end_date from policytable)policy ON mastertable.cpl_id = policy.cpl_id AND mastertable.commodityclass_code IN ("+pd_obj.getCommodity_class_code()+") AND ((policy.start_date BETWEEN '"+start_date+"' AND '"+end_date+"') OR (policy.end_date BETWEEN '"+start_date+"' AND '"+end_date+"') OR (policy.end_date IS NULL AND ((policy.start_date BETWEEN '"+start_date+"' AND '"+end_date+"') OR (policy.start_date < '"+start_date+"'))))) tot ");
        sb.append("where tot.policytype_code="+single_policy_type+" and tot.commodityclass_code="+single_commodity_class+"");

        System.out.println("getStringQueryBiofuelPolicies_barchart sb "+sb.toString());
        return sb;
    }

    public StringBuilder getStringQueryBiofuelPolicyMeasures_barchart(POLICYDataObject pd_obj, String single_policy_type, String single_policy_measure, String single_commodity_class) throws Exception{

        //select count(DISTINCT(tot.country_code)) from (select mastertable.cpl_id, mastertable.commodityclass_code, mastertable.commodityclass_name, mastertable.policytype_code, mastertable.policytype_name, mastertable.policymeasure_code, mastertable.policymeasure_name, mastertable.country_code, mastertable.country_name, policy.start_date, policy.end_date from mastertable JOIN (select policytable.cpl_id, policytable.start_date, policytable.end_date from policytable)policy ON mastertable.cpl_id = policy.cpl_id AND mastertable.commodityclass_code IN (5,6,7) AND ((policy.start_date BETWEEN '1/1/2013' AND '1/1/2014') OR (policy.end_date BETWEEN '1/1/2013' AND '1/1/2014') OR (policy.end_date IS NULL AND ((policy.start_date BETWEEN '1/1/2013' AND '1/1/2014') OR (policy.start_date < '1/1/2013'))))) tot where tot.policytype_code=2 and tot.policymeasure_code=8 and tot.commodityclass_code=6
        String start_date = pd_obj.getStart_date();
        String end_date = pd_obj.getEnd_date();
        StringBuilder sb = new StringBuilder();
        sb.append("select count(DISTINCT(tot.country_code)) from (select mastertable.cpl_id, mastertable.commodityclass_code, mastertable.commodityclass_name, mastertable.policytype_code, mastertable.policytype_name, mastertable.policymeasure_code, mastertable.policymeasure_name, mastertable.country_code, mastertable.country_name, policy.start_date, policy.end_date from mastertable ");
//        sb.append("JOIN (select policytable.cpl_id, policytable.start_date, policytable.end_date from policytable)policy ON mastertable.cpl_id = policy.cpl_id AND mastertable.commodityclass_code IN (5,6,7) AND ((policy.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.end_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.end_date IS NULL AND ((policy.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.start_date < '05/05/2010'))))) tot ");
        sb.append("JOIN (select policytable.cpl_id, policytable.start_date, policytable.end_date from policytable)policy ON mastertable.cpl_id = policy.cpl_id AND mastertable.commodityclass_code IN ("+pd_obj.getCommodity_class_code()+") AND ((policy.start_date BETWEEN '"+start_date+"' AND '"+end_date+"') OR (policy.end_date BETWEEN '"+start_date+"' AND '"+end_date+"') OR (policy.end_date IS NULL AND ((policy.start_date BETWEEN '"+start_date+"' AND '"+end_date+"') OR (policy.start_date < '"+start_date+"'))))) tot ");
        sb.append("where tot.policytype_code="+single_policy_type+ " and tot.policymeasure_code="+single_policy_measure+" and tot.commodityclass_code="+single_commodity_class+"");

        System.out.println("getStringQueryBiofuelPolicies_barchart sb "+sb.toString());
        return sb;
    }

    public StringBuilder getStringQueryImportTariffs_barchart(POLICYDataObject pd_obj, boolean min, String chart_type, String single_policy_type, String single_policy_measure, String year_list) throws Exception{

        /*select MAX(tot.mfn_value), tot.commodityclass_code, tot.mfn_policy_element, tot.start_date FROM (SELECT mfn_table.policy_element as mfn_policy_element, fbt_table.policy_element as fbt_policy_element, mfn_table.value as mfn_value, fbt_table.value as fbt_value, mfn_table.start_date, mfn_table.commodityclass_code from ((select * from policytable, mastertable where policytable.cpl_id = mastertable.cpl_id and policytype_code=2 and policymeasure_code=11 and commodityclass_code IN (1,2,3,4) and value_text IS NULL and (start_date>='2010-01-01' AND start_date<='2014-01-01') and policy_element='MFN applied tariff' and units='%' and value IS NOT NULL) mfn_table INNER JOIN (select * from policytable, mastertable where policytable.cpl_id = mastertable.cpl_id and policytype_code=2 and policymeasure_code=11 and commodityclass_code IN (1,2,3,4) and value_text IS NULL and (start_date>='2010-01-01' AND start_date<='2014-01-01') and policy_element='Final bound tariff' and units='%' and value IS NOT NULL) fbt_table ON mfn_table.start_date=fbt_table.start_date and mfn_table.commodityclass_code=fbt_table.commodityclass_code)) tot group by tot.commodityclass_code, tot.mfn_policy_element, tot.start_date ORDER BY tot.commodityclass_code, tot.mfn_policy_element, tot.start_date;*/
        StringBuilder sb = new StringBuilder();
//        if(min)
//        {
//            if(chart_type.equalsIgnoreCase("mfn"))
//            {
//                sb.append("select MIN(tot.mfn_value), ");
//            }
//            else{
//                sb.append("select MIN(tot.fbt_value), ");
//            }
//        }
//        else{
//            if(chart_type.equalsIgnoreCase("mfn"))
//            {
//                sb.append("select MAX(tot.mfn_value), ");
//            }
//            else{
//                sb.append("select MAX(tot.fbt_value), ");
//            }
//        }

        if(chart_type.equalsIgnoreCase("mfn"))
        {
            sb.append("select AVG(CAST (tot.mfn_value AS FLOAT)), ");
        }
        else{
            sb.append("select AVG(CAST (tot.fbt_value AS FLOAT)), ");
        }
//        sb.append("select AVG(CAST (tot.mfn_value AS FLOAT)), ");

        if(chart_type.equalsIgnoreCase("mfn"))
        {
            sb.append("tot.commodityclass_code, tot.mfn_policy_element, tot.start_date FROM ");
        }
        else{
            sb.append("tot.commodityclass_code, tot.fbt_policy_element, tot.start_date FROM ");
        }
        sb.append("(SELECT mfn_table.policy_element as mfn_policy_element, fbt_table.policy_element as fbt_policy_element, mfn_table.value as mfn_value, fbt_table.value as fbt_value, mfn_table.start_date, mfn_table.commodityclass_code from ");
        sb.append("((select * from policytable, mastertable where policytable.cpl_id = mastertable.cpl_id and policytype_code="+single_policy_type+" and policymeasure_code="+single_policy_measure+" and commodityclass_code IN ("+pd_obj.getCommodity_class_code()+") ");
        sb.append("and value_text IS NULL and start_date IN ("+year_list+") and policy_element='"+pd_obj.getPolicy_element()[0]+"' and units='"+pd_obj.getUnit()+"' and value IS NOT NULL) mfn_table ");
        sb.append("INNER JOIN (select * from policytable, mastertable where policytable.cpl_id = mastertable.cpl_id and policytype_code="+single_policy_type+" and policymeasure_code="+single_policy_measure+" and commodityclass_code IN ("+pd_obj.getCommodity_class_code()+") ");
        sb.append("and value_text IS NULL and start_date IN ("+year_list+") and policy_element='"+pd_obj.getPolicy_element()[1]+"' and units='"+pd_obj.getUnit()+"' and value IS NOT NULL) ");
        sb.append("fbt_table ON mfn_table.start_date=fbt_table.start_date and mfn_table.commodityclass_code=fbt_table.commodityclass_code)) tot ");

        if(chart_type.equalsIgnoreCase("mfn"))
        {
            sb.append("group by tot.commodityclass_code, tot.mfn_policy_element, tot.start_date ORDER BY tot.commodityclass_code, tot.mfn_policy_element, tot.start_date");
        }
        else{
            sb.append("group by tot.commodityclass_code, tot.fbt_policy_element, tot.start_date ORDER BY tot.commodityclass_code, tot.fbt_policy_element, tot.start_date");
        }

        System.out.println(sb.toString());
        return sb;
    }

    public StringBuilder getStringQueryExportRestrictionsPolicyMeasures_barchart(POLICYDataObject pd_obj, String single_policy_type, String single_policy_measure, String single_commodity_class) throws Exception{

        //select count(DISTINCT(tot.country_code)) from (select mastertable.cpl_id, mastertable.commodityclass_code, mastertable.commodityclass_name, mastertable.policytype_code, mastertable.policytype_name, mastertable.policymeasure_code, mastertable.policymeasure_name, mastertable.country_code, mastertable.country_name, policy.start_date, policy.end_date from mastertable JOIN (select policytable.cpl_id, policytable.start_date, policytable.end_date from policytable)policy ON mastertable.cpl_id = policy.cpl_id AND mastertable.commodityclass_code IN (5,6,7) AND ((policy.start_date BETWEEN '1/1/2013' AND '1/1/2014') OR (policy.end_date BETWEEN '1/1/2013' AND '1/1/2014') OR (policy.end_date IS NULL AND ((policy.start_date BETWEEN '1/1/2013' AND '1/1/2014') OR (policy.start_date < '1/1/2013'))))) tot where tot.policytype_code=2 and tot.policymeasure_code=8 and tot.commodityclass_code=6
        String start_date = pd_obj.getStart_date();
        String end_date = pd_obj.getEnd_date();
        StringBuilder sb = new StringBuilder();
        sb.append("select count(DISTINCT(tot.country_code)) from (select mastertable.cpl_id, mastertable.commodityclass_code, mastertable.commodityclass_name, mastertable.policytype_code, mastertable.policytype_name, mastertable.policymeasure_code, mastertable.policymeasure_name, mastertable.country_code, mastertable.country_name, policy.start_date, policy.end_date, policy.value_text from mastertable ");
//        sb.append("JOIN (select policytable.cpl_id, policytable.start_date, policytable.end_date from policytable)policy ON mastertable.cpl_id = policy.cpl_id AND mastertable.commodityclass_code IN (5,6,7) AND ((policy.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.end_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.end_date IS NULL AND ((policy.start_date BETWEEN '05/05/2010' AND '05/04/2011') OR (policy.start_date < '05/05/2010'))))) tot ");
        sb.append("JOIN (select policytable.cpl_id, policytable.start_date, policytable.end_date, policytable.value_text from policytable)policy ON mastertable.cpl_id = policy.cpl_id AND mastertable.commodityclass_code IN ("+pd_obj.getCommodity_class_code()+") AND ((policy.start_date BETWEEN '"+start_date+"' AND '"+end_date+"') OR (policy.end_date BETWEEN '"+start_date+"' AND '"+end_date+"') OR (policy.end_date IS NULL AND ((policy.start_date BETWEEN '"+start_date+"' AND '"+end_date+"') OR (policy.start_date < '"+start_date+"'))))) tot ");
        sb.append("where tot.policytype_code="+single_policy_type+ " and tot.policymeasure_code="+single_policy_measure+" and tot.commodityclass_code="+single_commodity_class+" AND (tot.value_text<>'elim' OR tot.value_text IS NULL)");

        //System.out.println("getStringQueryExportRestrictionsPolicyMeasures_barchart sb "+sb.toString());
        return sb;
    }

}