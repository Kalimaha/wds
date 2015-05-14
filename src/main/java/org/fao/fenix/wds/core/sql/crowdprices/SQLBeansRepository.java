/**
 *
 * FENIX (Food security and Early warning Network and Information Exchange)
 *
 * Copyright (c) 2011, by FAO of UN under the EC-FAO Food Security
Information for Action Programme
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.fao.fenix.wds.core.sql.crowdprices;

import org.fao.fenix.wds.core.bean.GroupByBean;
import org.fao.fenix.wds.core.bean.OrderByBean;
import org.fao.fenix.wds.core.bean.SQLBean;
import org.fao.fenix.wds.core.bean.WhereBean;
import org.fao.fenix.wds.core.constant.SQL;

import java.util.List;


public class SQLBeansRepository {
    public static SQLBean getPoints(String marketcodes, String commoditycodes, String fromdate, String todate, String bbox, String language) {
        SQLBean sql = new SQLBean();

//		String s = "SELECT M.name, V.name, CAST( avg(D.price) as numeric(38,2)), CU.name, MU.name, M.lon, M.lat ";

        String s = "SELECT M.name, C.name, CAST( avg(D.price) as numeric(38,2)), CU.name, MU.name, M.lon, M.lat ";

        // FROM
        s += " FROM data D, market M, commodity C, measurementunit MU, currency CU ";

        // WHERES
        s += "WHERE ";
        s += "M.language = '" + language + "' ";
//		s += "AND V.language = '" + language + "' ";
        s += "AND M.language = '" + language + "' ";
        s += "AND C.language = '" + language + "' ";
        s += "AND MU.language = '" + language + "' ";
        s += "AND CU.language = '" + language + "' ";


        // JOINS
//		s += "AND D.varietycode = V.code ";
        s += "AND D.commoditycode = C.code ";
        s += "AND D.marketcode = M.code ";
        s += "AND D.currencycode = CU.code ";
        s += "AND D.measurementunitcode = MU.code ";

        s += addConditions(marketcodes, commoditycodes, fromdate, todate, bbox);

        s += " GROUP BY M.name, C.name, CU.name, MU.name, M.lon, M.lat";

        sql.setQuery(s);
        return sql;
    }

    public static SQLBean getPointsDisabled(String marketcodes, String commoditycodes, String fromdate, String todate, String bbox, String language) {
        SQLBean sql = new SQLBean();

        String s = "SELECT M1.name, M1.lon, M1.lat ";

        // FROM
        s += " FROM market M1 ";

        // WHERES
        s += "WHERE ";
        s += "M1.language = '" + language + "' ";

        // bbox
        if ( !bbox.equals("null")) {
            String[] v = getValues(bbox, false);
            String xmin = v[0];
            String ymin = v[1];
            String xmax = v[2];
            String ymax = v[3];
            s += "AND M1.lon > " + xmin + " ";
            s += "AND M1.lat > " + ymin + " ";
            s += "AND M1.lon < " + xmax + " ";
            s += "AND M1.lat < " + ymax + " ";
        }

        s += "AND M1.code NOT IN ( ";

        // nested query to get the the markets
        s += "SELECT M.code ";
        s += "FROM data D, market M ";
        s += "WHERE ";
        // JOINS
        s += "D.marketcode = M.code ";
        s += addConditions(marketcodes, commoditycodes, fromdate, todate, bbox);

        s += " )  ";

//		SELECT M1.name, M1.lat  FROM market M1 WHERE M1.language = 'fr'
//				 AND M1.code NOT IN (
//		SELECT M.code  FROM data D, market M WHERE M.language = 'fr' AND D.marketcode =
//				M.code AND D.commoditycode IN ('600') AND date(D.date) IN ('2012-08-30') AND M.lon > -76.26708984375 AND M.lat > 17.16178591271515 AND M.lon < -71.16943359375 AND M.lat < 18.989414715239334

        sql.setQuery(s);



        return sql;
    }


    public static SQLBean getSummary(String marketcodes, String commoditycodes, String fromdate, String todate, String bbox, String language) {
        SQLBean sql = new SQLBean();

        String s = "SELECT M.name, C.name, V.name, D.date_creation, CU.name, MU.name, D.price ";

        // FROM
        s += " FROM data D, market M, commodity C, variety V, currency CU, measurementunit MU ";

        // WHERES
        s += "WHERE ";
        s += "M.language = '" + language + "' ";
        s += "AND V.language = '" + language + "' ";

        // JOINS
        s += "AND D.varietycode = V.code ";
        s += "AND D.commoditycode = C.code ";
        s += "AND D.marketcode = M.code ";
        s += "AND D.currencycode = CU.code ";
        s += "AND D.measurementunitcode = MU.code ";

        s += addConditions(marketcodes, commoditycodes, fromdate, todate, bbox);

        s += "ORDER BY M.name, C.name, V.name, D.date ";

        sql.setQuery(s);

        System.out.println(sql.getQuery());

        return sql;
    }

    public static SQLBean getSummary2(String marketcodes, String commoditycodes, String fromdate, String todate, String bbox, String language) {
        SQLBean sql = new SQLBean();

//		String s = "SELECT CI.name, M.name, C.name, V.name, D.date, (CU.name || '/' || MU.name) AS unit, CAST((D.price) as numeric(38,2)) ";

//		String s = "SELECT  C.name, V.name, D.date, (CU.name || '/' || MU.name) AS unit, CAST((D.price) as numeric(38,2)) ";
        String s = "SELECT  C.name, V.name, D.date, (CU.name || '/' || MU.name) AS unit, CAST( AVG(D.price) as numeric(38,2)), CAST( min(D.price) as numeric(38,2)), CAST( max(D.price) as numeric(38,2)) ";


        // FROM
        s += " FROM data D, market M, city CI, commodity C, variety V, currency CU, measurementunit MU ";

        // WHERES
        s += "WHERE ";
        s += "M.language = '" + language + "' ";
        s += "AND V.language = '" + language + "' ";
        s += "AND C.language = '" + language + "' ";
        s += "AND CI.language = '" + language + "' ";
        s += "AND CU.language = '" + language + "' ";
        s += "AND MU.language = '" + language + "' ";

        // JOINS
        s += "AND D.varietycode = V.code ";
        s += "AND D.commoditycode = C.code ";
        s += "AND D.marketcode = M.code ";
        s += "AND D.currencycode = CU.code ";
        s += "AND D.measurementunitcode = MU.code ";
        s += "AND D.citycode = CI.code ";


        s += addConditions(marketcodes, commoditycodes, fromdate, todate, bbox);

        s += "GROUP BY C.name, V.name, D.date, unit ";
//		s += "ORDER BY CI.name, M.name, C.name, V.name, D.date ";
        s += "ORDER BY C.name, V.name, D.date ";

        sql.setQuery(s);

        return sql;
    }

    public static SQLBean getSummaryAggregated(String marketcodes, String commoditycodes, String fromdate, String todate, String bbox, String language) {
        SQLBean sql = new SQLBean();

        System.out.println("getSummaryAggregated: bbox " + bbox + " | lan:" + language);


//		String s = "SELECT CI.name, M.name, C.name, V.name, D.date, (CU.name || '/' || MU.name) AS unit, CAST( AVG(D.price) as numeric(38,2)) ";
        String s = "SELECT CI.name, M.name, C.name, V.name, (CU.name || '/' || MU.name) AS unit, CAST( AVG(D.price) as numeric(38,2)), CAST( min(D.price) as numeric(38,2)), CAST( max(D.price) as numeric(38,2))  ";



        // FROM
        s += " FROM data D, market M, city CI, commodity C, variety V, currency CU, measurementunit MU ";

        // WHERES
        s += "WHERE ";
        s += "M.language = '" + language + "' ";
        s += "AND V.language = '" + language + "' ";
        s += "AND C.language = '" + language + "' ";
        s += "AND CI.language = '" + language + "' ";
        s += "AND CU.language = '" + language + "' ";
        s += "AND MU.language = '" + language + "' ";

        // JOINS
        s += "AND D.varietycode = V.code ";
        s += "AND D.commoditycode = C.code ";
        s += "AND D.marketcode = M.code ";
        s += "AND D.currencycode = CU.code ";
        s += "AND D.measurementunitcode = MU.code ";
        s += "AND D.citycode = CI.code ";

        s += addConditions(marketcodes, commoditycodes, fromdate, todate, bbox);

//		s += "GROUP BY CI.name, M.name, C.name, V.name, D.date, unit ";
//		s += "ORDER BY CI.name, M.name, C.name, V.name, D.date ";

        s += "GROUP BY CI.name, M.name, C.name, V.name, unit ";
        s += "ORDER BY CI.name, M.name, C.name, V.name ";

        sql.setQuery(s);

        return sql;
    }

    public static SQLBean getChartData(String marketcodes, String commoditycodes, String fromdate, String todate, String language, String bbox) {
        SQLBean sql = new SQLBean();


        String s = "SELECT 'Price', V.name, CAST( AVG(D.price) as numeric(38,2)), (CU.name || '/' || MU.name) AS unit ";

        // FROM
        s += " FROM data D, variety V, market M, measurementunit MU, currency CU ";

        // WHERES
        s += "WHERE ";
        s += "M.language = '" + language + "' ";
        s += "AND V.language = '" + language + "' ";
        s += "AND MU.language = '" + language + "' ";
        s += "AND CU.language = '" + language + "' ";

        // JOINS
        s += "AND D.varietycode = V.code ";
        s += "AND D.marketcode = M.code ";
        s += "AND D.measurementunitcode = MU.code ";
        s += "AND D.currencycode = CU.code ";

        s += addConditions(marketcodes, commoditycodes, fromdate, todate, bbox);

        s += " GROUP BY V.name, unit";
        s += " ORDER BY V.name ";

        sql.setQuery(s);

        return sql;
    }

    public static SQLBean getChartData2(String marketcodes, String commoditycodes, String fromdate, String todate, String language, String bbox) {
        SQLBean sql = new SQLBean();

        String s = "SELECT D.date, V.name, CAST( AVG(D.price) as numeric(38,2)), (CU.name || '/' || MU.name) AS unit ";

        // FROM
        s += " FROM data D, variety V, market M, measurementunit MU, currency CU ";

        // WHERES
        s += "WHERE ";
        s += "M.language = '" + language + "' ";
        s += "AND V.language = '" + language + "' ";
        s += "AND MU.language = '" + language + "' ";
        s += "AND CU.language = '" + language + "' ";

        // JOINS
        s += "AND D.varietycode = V.code ";
        s += "AND D.marketcode = M.code ";
        s += "AND D.measurementunitcode = MU.code ";
        s += "AND D.currencycode = CU.code ";

        s += addConditions(marketcodes, commoditycodes, fromdate, todate, bbox);

        s += " GROUP BY D.date, V.name, unit";
        s += " ORDER BY V.name, D.date ";

        sql.setQuery(s);

        return sql;
    }




    // TODO: booleand with AND at the beginning of the query
    private static String addConditions(String marketcodes, String commoditycodes, String  fromdate, String todate, String bbox) {
        String s ="";
        System.out.println("BBOX addConditions: " + bbox);
        // filters
        if ( marketcodes !=null && !marketcodes.equals("null") ) {
            String[] v = getValues(marketcodes, false);
            s += "AND D.marketcode IN (" ;
            for( int i=0; i < v.length; i++) {
                s += "'"+ v[i]+"'";
                if ( i < v.length -1)
                    s += ",";
            }
            s += ") ";
        }
        if ( commoditycodes !=null && !commoditycodes.equals("null") ) {
            String[] v = getValues(commoditycodes, false);
            s += "AND D.commoditycode IN (" ;
            for( int i=0; i < v.length; i++) {
                s += "'"+ v[i]+"'";
                if ( i < v.length -1)
                    s += ",";
            }
            s += ") ";
        }
        if ( fromdate !=null && !fromdate.equals("null") && todate !=null && !todate.equals("null")  ) {
            s += "AND date(D.date) >= '" + fromdate + "' ";
            s += "AND date(D.date) <= '" + todate + "' ";
        }
        // bbox
        if ( bbox !=null && !bbox.equals("null")) {
            String[] v = getValues(bbox, false);
            String xmin = v[0];
            String ymin = v[1];
            String xmax = v[2];
            String ymax = v[3];
            s += "AND M.lon > " + xmin + " ";
            s += "AND M.lat > " + ymin + " ";
            s += "AND M.lon < " + xmax + " ";
            s += "AND M.lat < " + ymax + " ";
        }
        return s;
    }


    public static SQLBean getCount(List<WhereBean> whereBeans) {
        SQLBean sql = new SQLBean();
        sql.select(null, "COUNT(*)", "count");

        sql.from("data", "D");

        for(WhereBean whereBean : whereBeans ) {
            sql.where(whereBean);
        }

        return sql;
    }

    private static SQLBean getCodes(String table, List<WhereBean> whereBeans, String language) {
        SQLBean sql = new SQLBean();
        sql.select(null, "D.code","code");
        sql.select(null, "D.name", "label");
        sql.from(table, "D");

//		sql.select(null, "D.name" + language, "label");
//		sql.from(table, "D");

        for(WhereBean whereBean : whereBeans ) {
            sql.where(whereBean);
        }

        sql.where(SQL.DATE.name(), "D.language", "=", "'" + language+ "'", null);

        sql.orderBy(new OrderByBean("D.name", "ASC"));
//		sql.orderBy(new OrderByBean("D.name" + language, "ASC"));
        return sql;
    }

    public static SQLBean getCodingSystem(String table, List<WhereBean> whereBeans, String language) {
        if ( language == null || language.equals("null") || language.equals("undefined"))
            return getAllCodes(table);
        else
            return getCodes(table, whereBeans, language);
    }

    private static SQLBean getAllCodes(String table) {
        SQLBean sql = new SQLBean();
        sql.select(null, "*", null);
        sql.from(table, "D");
        return sql;
    }

    public static SQLBean getDates(String table, List<WhereBean> whereBeans, String language) {
        SQLBean sql = new SQLBean();
        sql.select(null, "date(D.date)", "date");
        sql.select(null, "date(D.date)", "date");

        sql.from(table, "D");

        sql.orderBy(new OrderByBean("date(D.date)", "DESC"));
        sql.groupBy(new GroupByBean("date(D.date)"));
        return sql;
    }

    private static void setJoins(SQLBean sql) {

        sql.where(SQL.DATE.name(), "D.marketcode", "=", "M.code", null);
        sql.where(SQL.DATE.name(), "D.commoditycode", "=", "C.code", null);
        sql.where(SQL.DATE.name(), "D.varietycode", "=", "V.code", null);
        sql.where(SQL.DATE.name(), "D.measurementunitcode", "=", "MU.code", null);
        sql.where(SQL.DATE.name(), "D.unitvaluecode", "=", "UV.code", null);
        sql.where(SQL.DATE.name(), "D.currencycode", "=", "CU.code", null);

        // commodity and varieties join
        sql.where(SQL.DATE.name(), "C.code", "=", "V.commoditycode", null);

    }

    private static String[] getValues(String values, boolean apex) {
        return extractValues(values, ",", apex);
    }

    public static String[] extractValues(String s, String separator, boolean apex) {
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