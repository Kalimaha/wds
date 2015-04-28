package org.fao.fenix.wds.core.bean.faostat;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 */
public class FAOSTATODABean {

    private String[] donors;

    private String[] recipients;

    private String[] purposes;

    private String[] lgTypes;

    private String[] lgTerms;

    private String[] years;

    private String lang;

    private String thousandSeparator;

    private String decimalSeparator;

    private String decimals;

    private String page;

    private String datasource;

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public String[] getDonors() {
        return donors;
    }

    public void setDonors(String[] donors) {
        this.donors = donors;
    }

    public String[] getRecipients() {
        return recipients;
    }

    public void setRecipients(String[] recipients) {
        this.recipients = recipients;
    }

    public String[] getPurposes() {
        return purposes;
    }

    public void setPurposes(String[] purposes) {
        this.purposes = purposes;
    }

    public String[] getLgTypes() {
        return lgTypes;
    }

    public void setLgTypes(String[] lgTypes) {
        this.lgTypes = lgTypes;
    }

    public String[] getLgTerms() {
        return lgTerms;
    }

    public void setLgTerms(String[] lgTerms) {
        this.lgTerms = lgTerms;
    }

    public String[] getYears() {
        return years;
    }

    public void setYears(String[] years) {
        this.years = years;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
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

    public String getDecimals() {
        return decimals;
    }

    public void setDecimals(String decimals) {
        this.decimals = decimals;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

}