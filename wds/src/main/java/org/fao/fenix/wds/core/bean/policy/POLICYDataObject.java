package org.fao.fenix.wds.core.bean.policy;

/**
 * @author <a href="mailto:barbara.cintoli@fao.org">Barbara Cintoli</a>
 * @author <a href="mailto:barbara.cintoli@gmail.com">Barbara Cintoli</a>
 */
public class POLICYDataObject {

    private String datasource = "";
    private String policy_domain_code = "";
    private String commodity_domain_code = "";
    private String commodity_class_code = "";
    //It's an array
    private String[] policy_type_code;
    //It's an array
    private String[] policy_measure_code;
    private String country_code = "";
    private String yearTab = "";
    private String year_list = "";
    private String start_date = "";
    private String end_date = "";
    private String unit = "";

    private boolean chart_type = false;
    //It's an array
    private String[] policy_element;

    private String cpl_id = "";

    private String commodity_id = "";

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public String getPolicy_domain_code() {
        return policy_domain_code;
    }

    public void setPolicy_domain_code(String policy_domain_code) {
        this.policy_domain_code = policy_domain_code;
    }

    public String getCommodity_domain_code() {
        return commodity_domain_code;
    }

    public void setCommodity_domain_code(String commodity_domain_code) {
        this.commodity_domain_code = commodity_domain_code;
    }

    public String getCommodity_class_code() {
        return commodity_class_code;
    }

    public void setCommodity_class_code(String commodity_class_code) {
        this.commodity_class_code = commodity_class_code;
    }

    public String[] getPolicy_type_code() {
        return policy_type_code;
    }

    public void setPolicy_type_code(String[] policy_type_code) {
        this.policy_type_code = policy_type_code;
    }

    public String[] getPolicy_measure_code() {
        return policy_measure_code;
    }

    public void setPolicy_measure_code(String[] policy_measure_code) {
        this.policy_measure_code = policy_measure_code;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getYearTab() {
        return yearTab;
    }

    public void setYearTab(String yearTab) {
        this.yearTab = yearTab;
    }

    public String getYear_list() {
        return year_list;
    }

    public void setYear_list(String year_list) {
        this.year_list = year_list;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getCpl_id() {
        return cpl_id;
    }

    public void setCpl_id(String cpl_id) {
        this.cpl_id = cpl_id;
    }

    public String getCommodity_id() {
        return commodity_id;
    }

    public void setCommodity_id(String commodity_id) {
        this.commodity_id = commodity_id;
    }

    public String[] getPolicy_element() {return policy_element;}

    public void setPolicy_element(String[] policy_element) {this.policy_element = policy_element;}

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean getChartType() {
        return chart_type;
    }

    public void setChartType(boolean chart_type) {
        this.chart_type = chart_type;
    }
}