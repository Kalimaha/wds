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

    private String[] areaCodes;

    private String[] itemCodes;

    private String[] elementListCodes;

    private String[] years;

    private boolean flags;

    private boolean codes;

    private boolean units;

    private boolean nullValues;

    private String thousandSeparator;

    private String decimalSeparator;

    private int decimalPlaces;

    private int limit;

    public int getLimit() {
        return limit;
    }

    /**
     * @param limit Set this parameter to -1 to get ALL the lines
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

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

    public String[] getAreaCodes() {
        return areaCodes;
    }

    public void setAreaCodes(String[] areaCodes) {
        this.areaCodes = areaCodes;
    }

    public String[] getItemCodes() {
        return itemCodes;
    }

    public void setItemCodes(String[] itemCodes) {
        this.itemCodes = itemCodes;
    }

    public String[] getElementListCodes() {
        return elementListCodes;
    }

    public void setElementListCodes(String[] elementListCodes) {
        this.elementListCodes = elementListCodes;
    }

    public String[] getYears() {
        return years;
    }

    public void setYears(String[] years) {
        this.years = years;
    }

    public boolean isFlags() {
        return flags;
    }

    public void setFlags(boolean flags) {
        this.flags = flags;
    }

    public boolean isCodes() {
        return codes;
    }

    public void setCodes(boolean codes) {
        this.codes = codes;
    }

    public boolean isUnits() {
        return units;
    }

    public void setUnits(boolean units) {
        this.units = units;
    }

    public boolean isNullValues() {
        return nullValues;
    }

    public void setNullValues(boolean nullValues) {
        this.nullValues = nullValues;
    }

    public String getThousandSeparator() {
        return thousandSeparator;
    }

    public void setThousandSeparator(String thousandSeparator) {
        this.thousandSeparator = thousandSeparator;
    }

    public String getDecimalSeparator() {
        return decimalSeparator;
    }

    public void setDecimalSeparator(String decimalSeparator) {
        this.decimalSeparator = decimalSeparator;
    }

    public int getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(int decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    @Override
    public String toString() {
        return "FAOSTATProceduresBean{" +
                "domainCode='" + domainCode + '\'' +
                ", lang='" + lang + '\'' +
                ", areaCodes=" + Arrays.toString(areaCodes) +
                ", itemCodes=" + Arrays.toString(itemCodes) +
                ", elementListCodes=" + Arrays.toString(elementListCodes) +
                ", years=" + Arrays.toString(years) +
                ", flags=" + flags +
                ", codes=" + codes +
                ", units=" + units +
                ", nullValues=" + nullValues +
                ", thousandSeparator='" + thousandSeparator + '\'' +
                ", decimalSeparator='" + decimalSeparator + '\'' +
                ", decimalPlaces=" + decimalPlaces +
                '}';
    }

}