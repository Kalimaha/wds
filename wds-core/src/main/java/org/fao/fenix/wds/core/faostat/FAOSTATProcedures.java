package org.fao.fenix.wds.core.faostat;

import org.fao.fenix.wds.core.bean.DBBean;
import org.fao.fenix.wds.core.bean.faostat.FAOSTATProceduresBean;
import org.fao.fenix.wds.core.constant.DATASOURCE;
import org.fao.fenix.wds.core.jdbc.JDBCIterable;

import java.sql.SQLException;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 */
public class FAOSTATProcedures {

    public JDBCIterable getDomainListBoxes(String datasource, String domainCode, String lang) throws IllegalAccessException, InstantiationException, SQLException, ClassNotFoundException {
        JDBCIterable it = new JDBCIterable();
        DATASOURCE ds = DATASOURCE.valueOf(datasource.toUpperCase());
        DBBean db = new DBBean(ds);
        StringBuilder sb = new StringBuilder();
        sb.append("EXEC Warehouse.dbo.usp_GetDomainListBoxes @DomainCode = N'");
        sb.append(domainCode);
        sb.append("', @Lang = N'");
        sb.append(lang);
        sb.append("' ");
        it.query(db, sb.toString());
        return it;
    }

    public JDBCIterable getCountries(String datasource, String domainCode, String lang) throws IllegalAccessException, InstantiationException, SQLException, ClassNotFoundException {
        JDBCIterable it = new JDBCIterable();
        DATASOURCE ds = DATASOURCE.valueOf(datasource.toUpperCase());
        DBBean db = new DBBean(ds);
        StringBuilder sb = new StringBuilder();
        sb.append("EXEC Warehouse.dbo.usp_GetAreaList1 @DomainCode = N'");
        sb.append(domainCode);
        sb.append("', @Lang = N'");
        sb.append(lang);
        sb.append("' ");
        it.query(db, sb.toString());
        return it;
    }

    public JDBCIterable getRegions(String datasource, String domainCode, String lang) throws IllegalAccessException, InstantiationException, SQLException, ClassNotFoundException {
        JDBCIterable it = new JDBCIterable();
        DATASOURCE ds = DATASOURCE.valueOf(datasource.toUpperCase());
        DBBean db = new DBBean(ds);
        StringBuilder sb = new StringBuilder();
        sb.append("EXEC Warehouse.dbo.usp_GetAreaList2 @DomainCode = N'");
        sb.append(domainCode);
        sb.append("', @Lang = N'");
        sb.append(lang);
        sb.append("' ");
        it.query(db, sb.toString());
        return it;
    }

    public JDBCIterable getAreaGroupArea(String datasource, String domainCode, int areaGroupCode) throws IllegalAccessException, InstantiationException, SQLException, ClassNotFoundException {
        JDBCIterable it = new JDBCIterable();
        DATASOURCE ds = DATASOURCE.valueOf(datasource.toUpperCase());
        DBBean db = new DBBean(ds);
        StringBuilder sb = new StringBuilder();
        sb.append("EXEC Warehouse.dbo.usp_GetAreaGroupArea @DomainCode = N'");
        sb.append(domainCode);
        sb.append("', @AreaGroupCode = N'");
        sb.append(areaGroupCode);
        sb.append("' ");
        it.query(db, sb.toString());
        return it;
    }

    public JDBCIterable getItemGroupItem(String datasource, String domainCode, int areaGroupCode) throws IllegalAccessException, InstantiationException, SQLException, ClassNotFoundException {
        JDBCIterable it = new JDBCIterable();
        DATASOURCE ds = DATASOURCE.valueOf(datasource.toUpperCase());
        DBBean db = new DBBean(ds);
        StringBuilder sb = new StringBuilder();
        sb.append("EXEC Warehouse.dbo.usp_GetItemGroupItem @DomainCode = N'");
        sb.append(domainCode);
        sb.append("', @ItemGroupCode = N'");
        sb.append(areaGroupCode);
        sb.append("' ");
        it.query(db, sb.toString());
        return it;
    }

    public JDBCIterable getSpecialGroups(String datasource, String domainCode, String lang) throws IllegalAccessException, InstantiationException, SQLException, ClassNotFoundException {
        JDBCIterable it = new JDBCIterable();
        DATASOURCE ds = DATASOURCE.valueOf(datasource.toUpperCase());
        DBBean db = new DBBean(ds);
        StringBuilder sb = new StringBuilder();
        sb.append("EXEC Warehouse.dbo.usp_GetAreaList3 @DomainCode = N'");
        sb.append(domainCode);
        sb.append("', @Lang = N'");
        sb.append(lang);
        sb.append("' ");
        it.query(db, sb.toString());
        return it;
    }

    public JDBCIterable getItems(String datasource, String domainCode, String lang) throws IllegalAccessException, InstantiationException, SQLException, ClassNotFoundException {
        JDBCIterable it = new JDBCIterable();
        DATASOURCE ds = DATASOURCE.valueOf(datasource.toUpperCase());
        DBBean db = new DBBean(ds);
        StringBuilder sb = new StringBuilder();
        sb.append("EXEC Warehouse.dbo.usp_GetItemList1 @DomainCode = N'");
        sb.append(domainCode);
        sb.append("', @Lang = N'");
        sb.append(lang);
        sb.append("' ");
        it.query(db, sb.toString());
        return it;
    }

    public JDBCIterable getItemsAggregated(String datasource, String domainCode, String lang) throws IllegalAccessException, InstantiationException, SQLException, ClassNotFoundException {
        JDBCIterable it = new JDBCIterable();
        DATASOURCE ds = DATASOURCE.valueOf(datasource.toUpperCase());
        DBBean db = new DBBean(ds);
        StringBuilder sb = new StringBuilder();
        sb.append("EXEC Warehouse.dbo.usp_GetItemList2 @DomainCode = N'");
        sb.append(domainCode);
        sb.append("', @Lang = N'");
        sb.append(lang);
        sb.append("' ");
        it.query(db, sb.toString());
        return it;
    }

    public JDBCIterable getElements(String datasource, String domainCode, String lang) throws IllegalAccessException, InstantiationException, SQLException, ClassNotFoundException {
        JDBCIterable it = new JDBCIterable();
        DATASOURCE ds = DATASOURCE.valueOf(datasource.toUpperCase());
        DBBean db = new DBBean(ds);
        StringBuilder sb = new StringBuilder();
        sb.append("EXEC Warehouse.dbo.usp_GetElementList @DomainCode = N'");
        sb.append(domainCode);
        sb.append("', @Lang = N'");
        sb.append(lang);
        sb.append("' ");
//        System.out.println(sb);
        it.query(db, sb.toString());
        return it;
    }

    public JDBCIterable getYears(String datasource, String domainCode) throws IllegalAccessException, InstantiationException, SQLException, ClassNotFoundException {
        JDBCIterable it = new JDBCIterable();
        DATASOURCE ds = DATASOURCE.valueOf(datasource.toUpperCase());
        DBBean db = new DBBean(ds);
        StringBuilder sb = new StringBuilder();
        sb.append("EXEC Warehouse.dbo.usp_GetYearList @DomainCode = N'");
        sb.append(domainCode);
        sb.append("' ");
        it.query(db, sb.toString());
        return it;
    }

    public JDBCIterable getData(String datasource,
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
                                int decimalPlaces) throws IllegalAccessException, InstantiationException, SQLException, ClassNotFoundException {
        JDBCIterable it = new JDBCIterable();
        DATASOURCE ds = DATASOURCE.valueOf(datasource.toUpperCase());
        DBBean db = new DBBean(ds);
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
        it.query(db, sb.toString());
        return it;
    }

    public JDBCIterable getData(FAOSTATProceduresBean b) throws IllegalAccessException, InstantiationException, SQLException, ClassNotFoundException {
        JDBCIterable it = new JDBCIterable();
        DATASOURCE ds = DATASOURCE.valueOf(b.getDatasource().toUpperCase());
        DBBean db = new DBBean(ds);
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
        it.query(db, sb.toString());
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