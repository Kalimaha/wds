package org.fao.fenix.wds.core.bean.faostat;

import java.util.Arrays;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 */
public class FAOSTATCPINotesBean {

    private String datasource;

    private String[] areaCodes;

    private String[] yearCodes;

    private String[] itemCodes;

    private String lang;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public String[] getAreaCodes() {
        return areaCodes;
    }

    public void setAreaCodes(String[] areaCodes) {
        this.areaCodes = areaCodes;
    }

    public String[] getYearCodes() {
        return yearCodes;
    }

    public void setYearCodes(String[] yearCodes) {
        this.yearCodes = yearCodes;
    }

    public String[] getItemCodes() {
        return itemCodes;
    }

    public void setItemCodes(String[] itemCodes) {
        this.itemCodes = itemCodes;
    }

    @Override
    public String toString() {
        return "FAOSTATCPINotesBean{" +
                "datasource='" + datasource + '\'' +
                ", areaCodes=" + Arrays.toString(areaCodes) +
                ", yearCodes=" + Arrays.toString(yearCodes) +
                ", itemCodes=" + Arrays.toString(itemCodes) +
                ", lang='" + lang + '\'' +
                '}';
    }

}