package org.fao.fenix.wds.core.faostat;

import com.google.gson.Gson;
import junit.framework.TestCase;
import org.fao.fenix.wds.core.bean.faostat.FAOSTATProceduresBean;
import org.fao.fenix.wds.core.jdbc.JDBCIterable;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 */
public class FAOSTATProceduresTest extends TestCase {

    public void _testGetDomainListBoxes() {
        FAOSTATProcedures fp = new FAOSTATProcedures();
        Gson g = new Gson();
        JDBCIterable it = null;
//        try {
//            it = fp.getDomainListBoxes("faostatproddiss", "QC", "E");
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        while(it.hasNext())
            System.out.println(g.toJson(it.next()));
        System.out.println("");
    }

    public void _testGetCountries() {
        FAOSTATProcedures fp = new FAOSTATProcedures();
        Gson g = new Gson();
        JDBCIterable it = null;
//        try {
//            it = fp.getCountries("faostatproddiss", "QC", "E");
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        while(it.hasNext())
            System.out.println(g.toJson(it.next()));
        System.out.println("");
    }

    public void _testGetRegions() {
        FAOSTATProcedures fp = new FAOSTATProcedures();
        Gson g = new Gson();
        JDBCIterable it = null;
//        try {
//            it = fp.getRegions("faostatproddiss", "QC", "E");
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        while(it.hasNext())
            System.out.println(g.toJson(it.next()));
        System.out.println("");
    }

    public void testGetAreaGroupArea() {
        FAOSTATProcedures fp = new FAOSTATProcedures();
        Gson g = new Gson();
        JDBCIterable it = null;
//        try {
//            it = fp.getAreaGroupArea("faostatproddiss", "QC", 5000);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        while(it.hasNext())
            System.out.println(g.toJson(it.next()));
        System.out.println("");
    }

    public void _testGetItemGroupItem() {
        FAOSTATProcedures fp = new FAOSTATProcedures();
        Gson g = new Gson();
        JDBCIterable it = null;
//        try {
//            it = fp.getItemGroupItem("faostatproddiss", "QC", 1717);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        while(it.hasNext())
            System.out.println(g.toJson(it.next()));
        System.out.println("");
    }

    public void _testGetSpecialGroups() {
        FAOSTATProcedures fp = new FAOSTATProcedures();
        Gson g = new Gson();
        JDBCIterable it = null;
//        try {
//            it = fp.getSpecialGroups("faostatproddiss", "QC", "E");
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        while(it.hasNext())
            System.out.println(g.toJson(it.next()));
        System.out.println("");
    }

    public void _testGetItems() {
        FAOSTATProcedures fp = new FAOSTATProcedures();
        Gson g = new Gson();
        JDBCIterable it = null;
//        try {
//            it = fp.getItems("faostatproddiss", "QC", "E");
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        while(it.hasNext())
            System.out.println(g.toJson(it.next()));
        System.out.println("");
    }

    public void _testGetItemsAggregated() {
        FAOSTATProcedures fp = new FAOSTATProcedures();
        Gson g = new Gson();
        JDBCIterable it = null;
//        try {
//            it = fp.getItemsAggregated("faostatproddiss", "QC", "E");
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        while(it.hasNext())
            System.out.println(g.toJson(it.next()));
        System.out.println("");
    }

    public void _testGetElements() {
        FAOSTATProcedures fp = new FAOSTATProcedures();
        Gson g = new Gson();
        JDBCIterable it = null;
//        try {
//            it = fp.getElements("faostatproddiss", "QC", "E");
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        while(it.hasNext())
            System.out.println(g.toJson(it.next()));
        System.out.println("");
    }

    public void _testGetYears() {
        FAOSTATProcedures fp = new FAOSTATProcedures();
        Gson g = new Gson();
        JDBCIterable it = null;
//        try {
//            it = fp.getYears("faostatproddiss", "QC");
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        while(it.hasNext())
            System.out.println(g.toJson(it.next()));
        System.out.println("");
    }

    public void _testGetData() {
        FAOSTATProcedures fp = new FAOSTATProcedures();
        Gson g = new Gson();
        JDBCIterable it = null;
        String[] areaCodes = {"1", "4", "5"};
        String[] itemCodes = {"800", "221", "711", "515", "526"};
        String[] elementCodes = {"2312", "2413", "2510"};
        String[] yearCodes = {"2012", "2011", "2010"};
//        try {
//            it = fp.getData("faostatproddiss", "QC", "S", areaCodes, itemCodes, elementCodes, yearCodes, true, true, true, false, ",", ".", 1);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        while(it.hasNext())
            System.out.println(g.toJson(it.next()));
        System.out.println("");
    }

    public void testGetData() {
        FAOSTATProcedures fp = new FAOSTATProcedures();
        FAOSTATProceduresBean b = new FAOSTATProceduresBean();
        b.setLimit(-1);
//        b.setAreaCodes(new String[]{"2"});
//        b.setCodes(false);
//        b.setDatasource("faostatproddiss");
//        b.setDecimalPlaces(0);
//        b.setDecimalSeparator(".");
//        b.setDomainCode("QC");
//        b.setElementListCodes(new String[]{"2312"});
//        b.setItemCodes(new String[]{"15"});
//        b.setFlags(false);
//        b.setLang("E");
//        b.setNullValues(false);
//        b.setThousandSeparator(",");
//        b.setUnits(false);
//        b.setYears(new String[]{"2010", "2009", "2008", "2007", "2006"});
        JDBCIterable it = null;
        Gson g = new Gson();
//        try {
//            it = fp.getData(b);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        while(it.hasNext())
            System.out.println(g.toJson(it.next()));
        System.out.println("");
    }

}