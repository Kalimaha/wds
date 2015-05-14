package org.fao.fenix.wds.core.bean.faostat;

import java.util.Arrays;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 */
public class FAOSTATProceduresBean {

    private String datasource;

    private String domainCode;

    private String lang;

    private String[] list1Codes;

    private String[] list2Codes;

    private String[] list3Codes;

    private String[] list4Codes;

    private String[] list5Codes;

    private String[] list6Codes;

    private String[] list7Codes;

    private boolean nullValues;

    private String thousand;

    private String decimal;

    private int decPlaces;

    private int limit;

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public String getDomainCode() {
        return domainCode;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String[] getList1Codes() {
        return list1Codes;
    }

    public void setList1Codes(String[] list1Codes) {
        this.list1Codes = list1Codes;
    }

    public String[] getList2Codes() {
        return list2Codes;
    }

    public void setList2Codes(String[] list2Codes) {
        this.list2Codes = list2Codes;
    }

    public String[] getList3Codes() {
        return list3Codes;
    }

    public void setList3Codes(String[] list3Codes) {
        this.list3Codes = list3Codes;
    }

    public String[] getList4Codes() {
        return list4Codes;
    }

    public void setList4Codes(String[] list4Codes) {
        this.list4Codes = list4Codes;
    }

    public String[] getList5Codes() {
        return list5Codes;
    }

    public void setList5Codes(String[] list5Codes) {
        this.list5Codes = list5Codes;
    }

    public String[] getList6Codes() {
        return list6Codes;
    }

    public void setList6Codes(String[] list6Codes) {
        this.list6Codes = list6Codes;
    }

    public String[] getList7Codes() {
        return list7Codes;
    }

    public void setList7Codes(String[] list7Codes) {
        this.list7Codes = list7Codes;
    }

    public boolean isNullValues() {
        return nullValues;
    }

    public void setNullValues(boolean nullValues) {
        this.nullValues = nullValues;
    }

    public String getThousand() {
        return thousand;
    }

    public void setThousand(String thousand) {
        this.thousand = thousand;
    }

    public String getDecimal() {
        return decimal;
    }

    public void setDecimal(String decimal) {
        this.decimal = decimal;
    }

    public int getDecPlaces() {
        return decPlaces;
    }

    public void setDecPlaces(int decPlaces) {
        this.decPlaces = decPlaces;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "FAOSTATProceduresBean{" +
                "datasource='" + datasource + '\'' +
                ", domainCode='" + domainCode + '\'' +
                ", lang='" + lang + '\'' +
                ", list1Codes=" + Arrays.toString(list1Codes) +
                ", list2Codes=" + Arrays.toString(list2Codes) +
                ", list3Codes=" + Arrays.toString(list3Codes) +
                ", list4Codes=" + Arrays.toString(list4Codes) +
                ", list5Codes=" + Arrays.toString(list5Codes) +
                ", list6Codes=" + Arrays.toString(list6Codes) +
                ", list7Codes=" + Arrays.toString(list7Codes) +
                ", nullValues=" + nullValues +
                ", thousand='" + thousand + '\'' +
                ", decimal='" + decimal + '\'' +
                ", decPlaces=" + decPlaces +
                ", limit=" + limit +
                '}';
    }

}