package org.fao.fenix.wds.core.faostat;

import org.fao.fenix.wds.core.bean.DatasourceBean;
import org.fao.fenix.wds.core.bean.faostat.FAOSTATODABean;
import org.fao.fenix.wds.core.bean.faostat.FAOSTATProceduresBean;
import org.fao.fenix.wds.core.datasource.DatasourcePool;
import org.fao.fenix.wds.core.jdbc.JDBCIterable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 */
@Component
public class FAOSTATProcedures {

    @Autowired
    private DatasourcePool datasourcePool;

    public JDBCIterable getODADonors(DatasourceBean dsBean, String lang) throws Exception {
        JDBCIterable it = new JDBCIterable();
        StringBuilder sb = new StringBuilder();
        sb.append("EXEC Warehouse.dbo.usp_GetODADonors @tablelanguage='");
        sb.append(lang);
        sb.append("' ");
        it.query(dsBean, sb.toString());
        return it;
    }

    public JDBCIterable getODARecipients(DatasourceBean dsBean, String lang) throws Exception {
        JDBCIterable it = new JDBCIterable();
        StringBuilder sb = new StringBuilder();
        sb.append("EXEC Warehouse.dbo.usp_GetODARecipients @tablelanguage='");
        sb.append(lang);
        sb.append("' ");
        it.query(dsBean, sb.toString());
        return it;
    }

    public JDBCIterable getODAYear(DatasourceBean dsBean, String lang) throws Exception {
        JDBCIterable it = new JDBCIterable();
        StringBuilder sb = new StringBuilder();
        sb.append("EXEC Warehouse.dbo.usp_GetODAYear @tablelanguage='");
        sb.append(lang);
        sb.append("' ");
        it.query(dsBean, sb.toString());
        return it;
    }

    public JDBCIterable getODAPurposes(DatasourceBean dsBean, String lang) throws Exception {
        JDBCIterable it = new JDBCIterable();
        StringBuilder sb = new StringBuilder();
        sb.append("EXEC Warehouse.dbo.usp_GetODAPurposes @tablelanguage='");
        sb.append(lang);
        sb.append("' ");
        it.query(dsBean, sb.toString());
        return it;
    }

    public JDBCIterable getODAFlows(DatasourceBean dsBean, String lang) throws Exception {
        JDBCIterable it = new JDBCIterable();
        StringBuilder sb = new StringBuilder();
        sb.append("EXEC Warehouse.dbo.usp_GetODAFlows @tablelanguage='");
        sb.append(lang);
        sb.append("' ");
        it.query(dsBean, sb.toString());
        return it;
    }

    public JDBCIterable getODAElement(DatasourceBean dsBean, String lang) throws Exception {
        JDBCIterable it = new JDBCIterable();
        StringBuilder sb = new StringBuilder();
        sb.append("EXEC Warehouse.dbo.usp_GetODAElement @tablelanguage='");
        sb.append(lang);
        sb.append("' ");
        it.query(dsBean, sb.toString());
        return it;
    }

    public JDBCIterable getODAData(DatasourceBean dsBean, FAOSTATODABean b) throws Exception {
        JDBCIterable it = new JDBCIterable();
        StringBuilder sb = new StringBuilder();
        sb.append("EXEC usp_GetODAData @Donor='");
        for (int i = 0 ; i < b.getDonors().length ; i++) {
            sb.append(b.getDonors()[i]);
            if (i < b.getDonors().length - 1)
                sb.append(",");
        }
        sb.append("', @Recipient='");
        for (int i = 0 ; i < b.getRecipients().length ; i++) {
            sb.append(b.getRecipients()[i]);
            if (i < b.getRecipients().length - 1)
                sb.append(",");
        }
        sb.append("', @Purpose='");
        for (int i = 0 ; i < b.getPurposes().length ; i++) {
            sb.append(b.getPurposes()[i]);
            if (i < b.getPurposes().length - 1)
                sb.append(",");
        }
        sb.append("', @LGType='");
        for (int i = 0 ; i < b.getLgTypes().length ; i++) {
            sb.append(b.getLgTypes()[i]);
            if (i < b.getLgTypes().length - 1)
                sb.append(",");
        }
        sb.append("', @LGTerms='");
        for (int i = 0 ; i < b.getLgTerms().length ; i++) {
            sb.append(b.getLgTerms()[i]);
            if (i < b.getLgTerms().length - 1)
                sb.append(",");
        }
        sb.append("', @Year='");
        for (int i = 0 ; i < b.getYears().length ; i++) {
            sb.append(b.getYears()[i]);
            if (i < b.getYears().length - 1)
                sb.append(",");
        }
        sb.append("', @tablelanguage='");
        sb.append(b.getLang());
        sb.append("', @thousandsseparator='");
        sb.append(b.getThousandSeparator());
        sb.append("', @decimalseparator='");
        sb.append(b.getDecimalSeparator());
        sb.append("', @showDec='");
        sb.append(b.getDecimals());
        sb.append("', @page=");
        sb.append(b.getPage());
        sb.append(" ");
        it.query(dsBean, sb.toString());
        return it;
    }

    public JDBCIterable getCPINotes(DatasourceBean dsBean, String[] areaCodes, String[] yearCodes, String[] itemCodes, String lang) throws Exception {
        JDBCIterable it = new JDBCIterable();
        StringBuilder sb = new StringBuilder();
        sb.append("exec dbo.usp_GetCPINotes @lang=N'");
        sb.append(lang);
        sb.append("', @areaList=N'");
        for (int i = 0 ; i < areaCodes.length ; i++) {
            sb.append(areaCodes[i]);
            if (i < areaCodes.length - 1)
                sb.append(",");
        }
        sb.append("', @yearList=N'");
        for (int i = 0 ; i < yearCodes.length ; i++) {
            sb.append(yearCodes[i]);
            if (i < yearCodes.length - 1)
                sb.append(",");
        }
        sb.append("', @item=N'");
        for (int i = 0 ; i < itemCodes.length ; i++) {
            sb.append(itemCodes[i]);
            if (i < itemCodes.length - 1)
                sb.append(",");
        }
        sb.append("' ");
        it.query(dsBean, sb.toString());
        return it;
    }

    public JDBCIterable getCPIMetadataAreas(DatasourceBean dsBean, String lang) throws Exception {
        JDBCIterable it = new JDBCIterable();
        StringBuilder sb = new StringBuilder();
        sb.append("EXEC Warehouse.dbo.usp_GetCPIMetadataArea @lang='");
        sb.append(lang);
        sb.append("' ");
        it.query(dsBean, sb.toString());
        return it;
    }

    public JDBCIterable getCPIMetadata(DatasourceBean dsBean, String area, String lang) throws Exception {
        JDBCIterable it = new JDBCIterable();
        StringBuilder sb = new StringBuilder();
        sb.append("EXEC Warehouse.dbo.usp_GetCPIMetadata ");
        sb.append(area);
        sb.append(", '");
        sb.append(lang);
        sb.append("' ");
        it.query(dsBean, sb.toString());
        return it;
    }

    public JDBCIterable getDomainListBoxes(DatasourceBean dsBean, String domainCode, String lang) throws Exception {
        JDBCIterable it = new JDBCIterable();
        StringBuilder sb = new StringBuilder();
        sb.append("EXEC Warehouse.dbo.usp_GetDomainListBoxes @DomainCode = N'");
        sb.append(domainCode);
        sb.append("', @Lang = N'");
        sb.append(lang);
        sb.append("' ");
        it.query(dsBean, sb.toString());
        return it;
    }

    public JDBCIterable getCountries(DatasourceBean dsBean, String domainCode, String lang) throws Exception {
        JDBCIterable it = new JDBCIterable();
        StringBuilder sb = new StringBuilder();
        sb.append("EXEC Warehouse.dbo.usp_GetAreaList1 @DomainCode = N'");
        sb.append(domainCode);
        sb.append("', @Lang = N'");
        sb.append(lang);
        sb.append("' ");
        it.query(dsBean, sb.toString());
        return it;
    }

    public JDBCIterable getRegions(DatasourceBean dsBean, String domainCode, String lang) throws Exception {
        JDBCIterable it = new JDBCIterable();
        StringBuilder sb = new StringBuilder();
        sb.append("EXEC Warehouse.dbo.usp_GetAreaList2 @DomainCode = N'");
        sb.append(domainCode);
        sb.append("', @Lang = N'");
        sb.append(lang);
        sb.append("' ");
        it.query(dsBean, sb.toString());
        return it;
    }

    public JDBCIterable getAreaGroupArea(DatasourceBean dsBean, String domainCode, int areaGroupCode) throws Exception {
        JDBCIterable it = new JDBCIterable();
        StringBuilder sb = new StringBuilder();
        sb.append("EXEC Warehouse.dbo.usp_GetAreaGroupArea @DomainCode = N'");
        sb.append(domainCode);
        sb.append("', @AreaGroupCode = N'");
        sb.append(areaGroupCode);
        sb.append("' ");
        it.query(dsBean, sb.toString());
        return it;
    }

    public JDBCIterable getItemGroupItem(DatasourceBean dsBean, String domainCode, int areaGroupCode) throws Exception {
        JDBCIterable it = new JDBCIterable();
        StringBuilder sb = new StringBuilder();
        sb.append("EXEC Warehouse.dbo.usp_GetItemGroupItem @DomainCode = N'");
        sb.append(domainCode);
        sb.append("', @ItemGroupCode = N'");
        sb.append(areaGroupCode);
        sb.append("' ");
        it.query(dsBean, sb.toString());
        return it;
    }

    public JDBCIterable getSpecialGroups(DatasourceBean dsBean, String domainCode, String lang) throws Exception {
        JDBCIterable it = new JDBCIterable();
        StringBuilder sb = new StringBuilder();
        sb.append("EXEC Warehouse.dbo.usp_GetAreaList3 @DomainCode = N'");
        sb.append(domainCode);
        sb.append("', @Lang = N'");
        sb.append(lang);
        sb.append("' ");
        it.query(dsBean, sb.toString());
        return it;
    }

    public JDBCIterable getItems(DatasourceBean dsBean, String domainCode, String lang) throws Exception {
        JDBCIterable it = new JDBCIterable();
        StringBuilder sb = new StringBuilder();
        sb.append("EXEC Warehouse.dbo.usp_GetItemList1 @DomainCode = N'");
        sb.append(domainCode);
        sb.append("', @Lang = N'");
        sb.append(lang);
        sb.append("' ");
        it.query(dsBean, sb.toString());
        return it;
    }

    public JDBCIterable getItemsAggregated(DatasourceBean dsBean, String domainCode, String lang) throws Exception {
        JDBCIterable it = new JDBCIterable();
        StringBuilder sb = new StringBuilder();
        sb.append("EXEC Warehouse.dbo.usp_GetItemList2 @DomainCode = N'");
        sb.append(domainCode);
        sb.append("', @Lang = N'");
        sb.append(lang);
        sb.append("' ");
        it.query(dsBean, sb.toString());
        return it;
    }

    public JDBCIterable getElements(DatasourceBean dsBean, String domainCode, String lang) throws Exception {
        JDBCIterable it = new JDBCIterable();
        StringBuilder sb = new StringBuilder();
        sb.append("EXEC Warehouse.dbo.usp_GetElementList @DomainCode = N'");
        sb.append(domainCode);
        sb.append("', @Lang = N'");
        sb.append(lang);
        sb.append("' ");
        it.query(dsBean, sb.toString());
        return it;
    }

    public JDBCIterable getYears(DatasourceBean dsBean, String domainCode) throws Exception {
        JDBCIterable it = new JDBCIterable();
        StringBuilder sb = new StringBuilder();
        sb.append("EXEC Warehouse.dbo.usp_GetYearList @DomainCode = N'");
        sb.append(domainCode);
        sb.append("' ");
        it.query(dsBean, sb.toString());
        return it;
    }

    public JDBCIterable getData(DatasourceBean dsBean,
                                String domainCode,
                                String lang,
                                String[] areaCodes,
                                String[] itemCodes,
                                String[] elementListCodes,
                                String[] yearCodes,
                                boolean flags,
                                boolean codes,
                                boolean units,
                                boolean nullValues,
                                String thousandSeparator,
                                String decimalSeparator,
                                int decimalPlaces) throws Exception {
        JDBCIterable it = new JDBCIterable();
        String areas = buildCollection(areaCodes);
        String items = buildCollection(itemCodes);
        String elements = buildCollection(elementListCodes);
        String years = buildCollection(yearCodes);
        StringBuilder sb = new StringBuilder();
        sb.append("EXECUTE Warehouse.dbo.usp_GetData @DomainCode = '");
        sb.append(domainCode);
        sb.append("', @lang = '");
        sb.append(lang);
        sb.append("', @AreaCodes = '");
        sb.append(areas);
        sb.append("', @ItemCodes = '");
        sb.append(items);
        sb.append("', @ElementListCodes = '");
        sb.append(elements);
        sb.append("', @Years = '");
        sb.append(years);
        sb.append("', @Flags = ");
        sb.append(flags ? 1 : 0);
        sb.append(", @Codes = ");
        sb.append(codes ? 1 : 0);
        sb.append(", @Units = ");
        sb.append(units ? 1 : 0);
        sb.append(", @NullValues = ");
        sb.append(nullValues ? 1 : 0);
        sb.append(", @Thousand = '");
        sb.append(thousandSeparator);
        sb.append("', @Decimal = '");
        sb.append(decimalSeparator);
        sb.append("', @DecPlaces = ");
        sb.append(decimalPlaces);
        sb.append(" ");
        it.query(dsBean, sb.toString());
        return it;
    }

    public JDBCIterable getData(DatasourceBean dsBean, FAOSTATProceduresBean b) throws Exception {
        JDBCIterable it = new JDBCIterable();
        String areas = buildCollection(b.getAreaCodes());
        String items = buildCollection(b.getItemCodes());
        String elements = buildCollection(b.getElementListCodes());
        String years = buildCollection(b.getYears());
        StringBuilder sb = new StringBuilder();
        sb.append("EXECUTE Warehouse.dbo.usp_GetData @DomainCode = '");
        sb.append(b.getDomainCode());
        sb.append("', @lang = '");
        sb.append(b.getLang());
        sb.append("', @AreaCodes = '");
        sb.append(areas);
        sb.append("', @ItemCodes = '");
        sb.append(items);
        sb.append("', @ElementListCodes = '");
        sb.append(elements);
        sb.append("', @Years = '");
        sb.append(years);
        sb.append("', @Flags = ");
        sb.append(b.isFlags() ? 1 : 0);
        sb.append(", @Codes = ");
        sb.append(b.isCodes() ? 1 : 0);
        sb.append(", @Units = ");
        sb.append(b.isUnits() ? 1 : 0);
        sb.append(", @NullValues = ");
        sb.append(b.isNullValues() ? 1 : 0);
        sb.append(", @Thousand = '");
        sb.append(b.getThousandSeparator());
        sb.append("', @Decimal = '");
        sb.append(b.getDecimalSeparator());
        sb.append("', @DecPlaces = ");
        sb.append(b.getDecimalPlaces());
        if (b.getLimit() > -1) {
            sb.append(", @Limit = ");
            sb.append(b.getLimit());
        }
        sb.append(" ");
        it.query(dsBean, sb.toString());
        return it;
    }

    private String buildCollection(String[] a) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 0 ; i < a.length ; i++) {
            sb.append(a[i]);
            if (i < a.length - 1)
                sb.append(",");
        }
        sb.append(")");
        return sb.toString();
    }

}