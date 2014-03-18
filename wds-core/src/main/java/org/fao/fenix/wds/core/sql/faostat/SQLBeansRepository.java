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
package org.fao.fenix.wds.core.sql.faostat;

import org.fao.fenix.wds.core.bean.SQLBean;
import org.fao.fenix.wds.core.constant.ORDERBY;
import org.fao.fenix.wds.core.constant.WHERE;
import org.fao.fenix.wds.core.constant.SQL;

/** 
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class SQLBeansRepository {
	
	public static SQLBean getClassifications(String domainCode, String lang) {
		String s = "SELECT M.ItemCode, M.ItemName" + lang + ", M.ItemDescription" + lang + " FROM Metadata_Item AS M WHERE M.domaincode = '" + domainCode + "' ORDER BY M.ItemName" + lang + " ASC ";
		SQLBean sql = new SQLBean(s);
		return sql;
	}
	
	public static SQLBean getMethodology(String domainCode, String lang) {
		String s = "SELECT M.MethodologyNote" + lang + ", M.MethodologyCoverage" + lang + ", M.MethodologyReferences" + lang + ", M.MethodologyCollection" + lang + ", M.MethodologyEstimation" + lang + " FROM Metadata_Methodology AS M WHERE M.MethodologyCode = '" + domainCode + "' ";
//		System.out.println(s);
		SQLBean sql = new SQLBean(s);
		return sql;
	}
	
	public static SQLBean getMethodologyList(String domainCode, String lang) {
		String s = "SELECT M.MethodologyCode, M.MethodologyTitle" + lang + " FROM Metadata_Methodology AS M GROUP BY M.MethodologyCode, M.MethodologyTitle" + lang + " ORDER BY M.MethodologyTitle" + lang + " ASC ";
		SQLBean sql = new SQLBean(s);
		return sql;
	}
	
	public static SQLBean Units(String domainCode, String lang) {
		String s = "SELECT E.UnitAbbreviation" + lang + ", E.UnitTitle" + lang + " FROM Metadata_Unit AS E ORDER BY E.UnitAbbreviation" + lang + " ASC ";
		SQLBean sql = new SQLBean(s);
		return sql;
	}
	
	public static SQLBean getUnits(String domainCode, String lang) {
		String s = "SELECT E.UnitAbbreviation" + lang + ", E.UnitTitle" + lang + " FROM Metadata_Unit AS E ORDER BY E.UnitAbbreviation" + lang + " ASC ";
		SQLBean sql = new SQLBean(s);
		return sql;
	}
	
	public static SQLBean getLocalCurrencies(String domainCode, String lang) {
		String s = "";
		SQLBean sql = new SQLBean(s);
		return sql;
	}
	
	public static SQLBean getGlossary(String domainCode, String lang) {
		String s = "SELECT M.GlossaryName" + lang + ", M.GlossaryDefinition" + lang + ", M.GlossarySource" + lang + " FROM Metadata_Glossary AS M ORDER BY M.GlossaryName" + lang + " ASC ";
		SQLBean sql = new SQLBean(s);
		return sql;
	}
	
	public static SQLBean getAbbreviations(String domainCode, String lang) {
		String s = "SELECT M.AbbreviationTitle" + lang + ", AbbreviationDefinition" + lang + " FROM Metadata_Abbreviation AS M ORDER BY AbbreviationTitle" + lang + " ASC ";
		SQLBean sql = new SQLBean(s);
		return sql;
	}
	
	public static SQLBean verifyUser(String username, String password) {
		SQLBean sql = new SQLBean();
		sql.select(null, "U.username", "username");
		sql.select(null, "U.password", "password");
		sql.from("crowdpricesuser", "U");
		sql.where(SQL.TEXT.name(), "U.username", "=", username, null);
		sql.where(SQL.TEXT.name(), "U.password", "=", password, null);
//		System.out.println(Bean2SQL.convert(sql));
		return sql;
	}
	
	public static SQLBean getItemsFromElementCodes(String domainCode, String[] elementCodes, String language) {
		SQLBean sql = new SQLBean();
		sql.select(null, "AIE.ItemCode", "Code");
		sql.select(null, "I.ItemName" + lang(language), "Label");
		sql.from("domainareaitemelement", "AIE");
		sql.from("item", "I");
		sql.where(SQL.DATE.name(), "AIE.ElementCode", WHERE.IN.name(), null, elementCodes);
		sql.where(SQL.TEXT.name(), "AIE.DomainCode", "=", domainCode, null);
		sql.where(SQL.DATE.name(), "AIE.ItemCode", "=", "I.itemcode", null);
		sql.orderBy("I.ItemName" + lang(language), ORDERBY.ASC.name());
		sql.groupBy("AIE.ItemCode");
		sql.groupBy("I.ItemName" + lang(language));
//		System.out.println(Bean2SQL.convert(sql));
		return sql;
	}
	
	public static SQLBean getItemsFromCountryCodes(String domainCode, String[] elementCodes, String language) {
		SQLBean sql = new SQLBean();
		sql.select(null, "AIE.ItemCode", "Code");
		sql.select(null, "I.ItemName" + lang(language), "Label");
		sql.from("domainareaitemelement", "AIE");
		sql.from("item", "I");
		sql.where(SQL.DATE.name(), "AIE.AreaCode", WHERE.IN.name(), null, elementCodes);
		sql.where(SQL.TEXT.name(), "AIE.DomainCode", "=", domainCode, null);
		sql.where(SQL.DATE.name(), "AIE.ItemCode", "=", "I.itemcode", null);
		sql.orderBy("I.ItemName" + lang(language), ORDERBY.ASC.name());
		sql.groupBy("AIE.ItemCode");
		sql.groupBy("I.ItemName" + lang(language));
//		System.out.println(Bean2SQL.convert(sql));
		return sql;
	}
	
	public static SQLBean getElementsFromItemCodes(String domainCode, String[] itemCodes, String language) {
		SQLBean sql = new SQLBean();
		String s = "SELECT E.ElementCode, E.ElementName" + lang(language) + " " +
				   "FROM Element AS E " +
				   "WHERE E.ElementCode IN (SELECT AIE.ElementCode FROM domainareaitemelement AIE WHERE AIE.ItemCode IN (";
		for (int i = 0 ; i < itemCodes.length ; i++) {
			s += itemCodes[i];
			if (i < itemCodes.length - 1)
				s += ", ";
		}
		s += ") AND AIE.DomainCode = '" + domainCode + "' )";
		sql.setQuery(s);
//		System.out.println(Bean2SQL.convert(sql));
		return sql;
	}
	
	public static SQLBean getElementsFromCountryCodes(String domainCode, String[] itemCodes, String language) {
		SQLBean sql = new SQLBean();
		String s = "SELECT E.ElementCode, E.ElementName" + lang(language) + " " +
				   "FROM Element AS E " +
				   "WHERE E.ElementCode IN (SELECT AIE.ElementCode FROM domainareaitemelement AIE WHERE AIE.AreaCode IN (";
		for (int i = 0 ; i < itemCodes.length ; i++) {
			s += itemCodes[i];
			if (i < itemCodes.length - 1)
				s += ", ";
		}
		s += ") AND AIE.DomainCode = '" + domainCode + "' )";
		sql.setQuery(s);
		return sql;
	}
	
	public static SQLBean getCountriesFromItemCodes(String domainCode, String[] itemCodes, String language) {
		SQLBean sql = new SQLBean();
		sql.select(null, "AIE.AreaCode", "Code");
		sql.select(null, "A.AreaName" + lang(language), "Label");
		sql.from("domainareaitemelement", "AIE");
		sql.from("areagrouparea", "A");
		sql.where(SQL.DATE.name(), "AIE.ItemCode", WHERE.IN.name(), null, itemCodes);
		sql.where(SQL.TEXT.name(), "AIE.DomainCode", "=", domainCode, null);
		sql.where(SQL.DATE.name(), "AIE.AreaCode", "=", "A.areacode", null);
		sql.orderBy("A.AreaName" + lang(language), ORDERBY.ASC.name());
		sql.groupBy("AIE.AreaCode");
		sql.groupBy("A.AreaName" + lang(language));
		return sql;
	}
	
	public static SQLBean getCountriesFromElementCodes(String domainCode, String[] itemCodes, String language) {
		SQLBean sql = new SQLBean();
		sql.select(null, "AIE.AreaCode", "Code");
		sql.select(null, "A.AreaName" + lang(language), "Label");
		sql.from("domainareaitemelement", "AIE");
		sql.from("areagrouparea", "A");
		sql.where(SQL.DATE.name(), "AIE.ElementCode", WHERE.IN.name(), null, itemCodes);
		sql.where(SQL.TEXT.name(), "AIE.DomainCode", "=", domainCode, null);
		sql.where(SQL.DATE.name(), "AIE.AreaCode", "=", "A.areacode", null);
		sql.orderBy("A.AreaName" + lang(language), ORDERBY.ASC.name());
		sql.groupBy("AIE.AreaCode");
		sql.groupBy("A.AreaName" + lang(language));
		return sql;
	}
	
	public static SQLBean getElementCodes(String domainCode, String language) {
        String script = "SELECT DISTINCT e.ElementListCode AS Code, e.ElementListName" + lang(language) + " AS Label, de.[Order" + lang(language) + "] AS [Order] " +
                        "FROM Warehouse.dbo.Element e INNER JOIN Warehouse.dbo.DomainElement de ON e.ElementCode = de.ElementCode " +
                        "WHERE de.DomainCode = '" + domainCode + "' AND de.Level <> 0 " +
                        "ORDER BY de.[Order" + lang(language) + "]";
        SQLBean sql = new SQLBean(script);
        return sql;
		
	}

	public static SQLBean getAllElementCodes(String language) {
		language = lang(language);
		SQLBean sqlb = new SQLBean("SELECT E.ElementCode, E.ElementName"  + language + " " +
								   "FROM Element E, DomainElement DE " +
                                   "WHERE DE.ElementCode = E.ElementCode " +
                                   "GROUP BY E.ElementCode, E.ElementName"  + language + " " +
								   "ORDER BY E.ElementName" + language ); 
		
		// in case use the order by
//		System.out.println(Bean2SQL.convert(sqlb));
		return sqlb;
		
	}
	
	public static SQLBean getAllItemCodes(String language) {
		language = lang(language);
		SQLBean sqlb = new SQLBean("SELECT I.ItemCode, I.ItemName"  + language + " " +
								   "FROM Item I, DomainItem DI " +
                                   "WHERE DI.ItemCode = I.ItemCode " +
                                   "GROUP BY I.ItemCode, I.ItemName"  + language + " " +
								   "ORDER BY I.ItemName" + language ); 

		
		// in case use the order by
//		System.out.println(Bean2SQL.convert(sqlb));
		return sqlb;
		
	}
	
	public static SQLBean getAllCountriesByAreas(String areas, String language) {
		language = lang(language);
		String sql = "SELECT A.AreaCode, A.AreaName"  + language + " " +
				     "FROM DomainAreaGroupArea A ";
		
		if ( areas !=null ) {
			sql += "WHERE AreaGroupCode IN ( " + areas + ")";
			
		}
		if ( areas == null )
			sql += " WHERE ";
		else
			sql += " AND ";
		// QUICK FIX to countries
		sql += " GroupCode = 'Q' AND AreaEndYear is null ";
				
		sql += "GROUP BY A.AreaCode, A.AreaName"  + language + " " +
			   "ORDER BY A.AreaName" + language;
								   		
		SQLBean sqlb = new SQLBean(sql); 

		// in case use the order by
//		System.out.println(Bean2SQL.convert(sqlb));
		return sqlb;
		
	}

    public static SQLBean getAreas(String codes, String language) {

        language = lang(language);
        StringBuffer sql = new StringBuffer();

        sql.append(" SELECT A.AreaCode, A.AreaName" + language);
        sql.append(" FROM Area A ");
        sql.append(" WHERE ");
        sql.append(" A.AreaCode IN (" + codes + ") ");
        sql.append(" GROUP BY A.AreaCode, A.AreaName" + language);
        sql.append(" ORDER BY A.AreaName" + language + " ASC");

        SQLBean sqlb = new SQLBean(sql.toString());

        return sqlb;
    }
	
	public static SQLBean getItemsSearchInfo(String codes, String language) {

			language = lang(language);
			StringBuffer sql = new StringBuffer();
			
			sql.append(" SELECT I.ItemCode, I.ItemName" + language + ", D.GroupCode, D.GroupName"+language+", D.DomainCode, D.DomainName" + language);
			sql.append(" FROM Domain D, DomainItem DI, Item I  ");
			sql.append(" WHERE ");
			sql.append(" I.ItemCode IN (" + codes + ") ");
			sql.append(" AND I.ItemCode = DI.ItemCode ");
			sql.append(" AND D.DomainName"+language+" <> 'null' ");
			sql.append(" AND DI.DomainCode = D.DomainCode ");
			sql.append(" GROUP BY I.ItemCode, I.ItemName"+language+", D.GroupCode, D.GroupName"+language+", D.DomainCode, D.DomainName"+language);
			sql.append(" ORDER BY I.ItemName"+language+", I.ItemCode, D.GroupCode, D.GroupName"+language+", D.DomainCode, D.DomainName"+language);
			
			SQLBean sqlb = new SQLBean(sql.toString());

			return sqlb;
	}
	
	public static SQLBean getElementsSearchInfo(String codes, String language) {

		language = lang(language);
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT E.ElementCode, E.ElementName" + language +", D.GroupCode, D.GroupName"+language+", D.DomainCode, D.DomainName" + language); 
		sql.append(" FROM Domain D, DomainElement DE, Element E  ");
		sql.append(" WHERE "); 
		sql.append(" E.ElementCode IN (" + codes + ") ");
		sql.append(" AND E.ElementCode = DE.ElementCode ");
		sql.append(" AND D.DomainName"+language+" <> 'null' ");
		sql.append(" AND DE.DomainCode = D.DomainCode ");
		sql.append(" GROUP BY E.ElementCode, E.ElementName"+language+", D.GroupCode, D.GroupName"+language+", D.DomainCode, D.DomainName"+language);   
		sql.append(" ORDER BY E.ElementName"+language+", E.ElementCode, D.GroupCode, D.GroupName"+language+", D.DomainCode, D.DomainName"+language);
		SQLBean sqlb = new SQLBean(sql.toString());
//		System.out.println(Bean2SQL.convert(sqlb));
		return sqlb;
}
	
	
	public static SQLBean getCountryCodes(String domainCode, String language) {
        String script = "SELECT AreaCode AS Code, AreaListName" + lang(language) + " AS Label, [Order" + lang(language) + "] AS [Order], AggregateType " +
                        "FROM Warehouse.dbo.DomainAreaList " +
                        "WHERE DomainCode = '" + domainCode + "' AND [Level] IN (5) " +
                        "ORDER BY [Order" + lang(language) + "], AreaListName" + lang(language) + ", AggregateType";
        SQLBean sql = new SQLBean(script);
        return sql;
	}

    public static SQLBean getReporterCodes(String domainCode, String language) {
        String script = "SELECT AreaCode AS Code, AreaListName" + lang(language) + " AS Label, [Order" + lang(language) + "] AS [Order], AggregateType " +
                        "FROM Warehouse.dbo.DomainAreaList " +
                        "WHERE DomainCode = 'TMR' AND [Level] IN (5) " +
                        "ORDER BY [Order" + lang(language) + "], AreaListName" + lang(language) + ", AggregateType";
        SQLBean sql = new SQLBean(script);
        System.out.println("getReporterCodes");
        return sql;
    }

    public static SQLBean getReporterGeoGroupsCodes(String domainCode, String language) {
        String script = "SELECT AreaCode AS Code, AreaListName" + lang(language) + " AS Label, [Order" + lang(language) + "] AS [Order], AggregateType " +
                        "FROM Warehouse.dbo.DomainAreaList " +
                        "WHERE DomainCode = 'TM' + 'R' AND [Level] in (10, 15, 20) AND AreaGroupTypeCode <> 'p' " +
                        "ORDER BY [Order" + lang(language) + "], AreaListName" + lang(language) + ", AggregateType";
        SQLBean sql = new SQLBean(script);
        System.out.println("getReporterGeoGroupsCodes");
        return sql;
    }

    public static SQLBean getReporterSpecialGroupsCodes(String domainCode, String language) {
        String script = "SELECT AreaCode AS Code, AreaListName" + lang(language) + " AS Label, [Order" + lang(language) + "] AS [Order], AggregateType " +
                        "FROM Warehouse.dbo.DomainAreaList " +
                        "WHERE DomainCode = 'TM' + 'R' " +
                        "AND [Level] IN (10, 15, 20) " +
                        "AND AreaGroupTypeCode = 'p' " +
                        "ORDER BY [Order" + lang(language) + "], AreaListName" + lang(language) + ", AggregateType";
        SQLBean sql = new SQLBean(script);
        System.out.println("getReporterSpecialGroupsCodes");
        return sql;
    }

    public static SQLBean getPartnerCodes(String domainCode, String language) {
        String script = "SELECT AreaCode AS Code, AreaListName" + lang(language) + " AS Label, [Order" + lang(language) + "] AS [Order], AggregateType " +
                        "FROM Warehouse.dbo.DomainAreaList " +
                        "WHERE DomainCode = 'TMP' " +
                        "AND [Level] IN (5) " +
                        "ORDER BY [Order" + lang(language) + "], AreaListName" + lang(language) + ", AggregateType";
        SQLBean sql = new SQLBean(script);
        System.out.println("getPartnerCodes");
        return sql;
    }

    public static SQLBean getPartnerGeoGroupsCodes(String domainCode, String language) {
        String script = "SELECT AreaCode AS Code, AreaListName" + lang(language) + " AS Label, [Order" + lang(language) + "] AS [Order], AggregateType " +
                        "FROM Warehouse.dbo.DomainAreaList " +
                        "WHERE DomainCode = 'TM' + 'P' " +
                        "AND [Level] IN (10, 15, 20) " +
                        "AND AreaGroupTypeCode <> 'p' " +
                        "ORDER BY [Order" + lang(language) + "], AreaListName" + lang(language) + ", AggregateType";
        SQLBean sql = new SQLBean(script);
        System.out.println("getPartnerGeoGroupsCodes");
        System.out.println(script);
        return sql;
    }

    public static SQLBean getPartnerSpecialGroupsCodes(String domainCode, String language) {
        String script = "SELECT AreaCode AS Code, AreaListName" + lang(language) + " AS Label, [Order" + lang(language) + "] AS [Order], AggregateType " +
                        "FROM Warehouse.dbo.DomainAreaList " +
                        "WHERE DomainCode = 'TM' + 'P' " +
                        "AND [Level] IN (10, 15, 20) " +
                        "AND AreaGroupTypeCode = 'p' " +
                        "ORDER BY [Order" + lang(language) + "], AreaListName" + lang(language) + ", AggregateType";
        SQLBean sql = new SQLBean(script);
        System.out.println("getPartnerSpecialGroupsCodes");
        System.out.println(script);
        return sql;
    }
	
	public static SQLBean getRegionCodes(String domainCode, String language) {
        String script = "SELECT AreaCode AS Code, AreaListName" + lang(language) + " AS Label, [Order" + lang(language) + "] AS [Order], AggregateType " +
                        "FROM Warehouse.dbo.DomainAreaList " +
                        "WHERE DomainCode = '" + domainCode + "' AND [Level] IN (10,15,20) AND AreaGroupTypeCode <> 'p' " +
                        "ORDER BY [Order" + lang(language) + "], AreaListName" + lang(language) + ", AggregateType";
        SQLBean sql = new SQLBean(script);
		return sql;
	}
	
	public static SQLBean getSpecialGroupsCodes(String domainCode, String language) {
        String script = "SELECT AreaCode AS Code, AreaListName" + lang(language) + " AS Label, [Order" + lang(language) + "] AS [Order], AggregateType " +
                        "FROM Warehouse.dbo.DomainAreaList " +
                        "WHERE DomainCode = '" + domainCode + "' AND [Level] IN (10,15,20) AND AreaGroupTypeCode = 'p' " +
                        "ORDER BY [Order" + lang(language) + "], AreaListName" + lang(language) + ", AggregateType";
        SQLBean sql = new SQLBean(script);
		return sql;
	}

	public static SQLBean getItemCodes(String domainCode, String language) {
        String script = "SELECT ItemCode AS Code, ItemListName" + lang(language) + " AS Label, [Order" + lang(language) + "] AS [Order], AggregateType " +
                        "FROM Warehouse.dbo.DomainItemList " +
                        "WHERE DomainCode = '" + domainCode + "' AND [Level] = 5 " +
                        "ORDER BY [Order" + lang(language) + "], ItemListName" + lang(language) + ", AggregateType";
        SQLBean sql = new SQLBean(script);
		return sql;
	}
	
	public static SQLBean getItemAggregatedCodes(String domainCode, String language) {
        String script = "SELECT ItemCode AS Code, ItemListName" + lang(language) + " AS Label, [Order" + lang(language) + "] AS [Order], AggregateType " +
                        "FROM Warehouse.dbo.DomainItemList " +
                        "WHERE DomainCode = '" + domainCode + "' AND [Level] IN (10, 15, 20) " +
                        "ORDER BY [Order" + lang(language) + "], ItemListName" + lang(language) + ", AggregateType";
        SQLBean sql = new SQLBean(script);
        return sql;
	}
	
	/**
	 * @param domainCode
	 * @param language
	 * @param tablename 'data' or 'tradematrix'
	 * @return
	 */
	public static SQLBean getYears(String domainCode, String language, String tablename) {
        String script = "SELECT Year AS [Year], Year AS Label " +
                        "FROM Warehouse.dbo.DomainYears " +
                        "WHERE DomainCode = '" + domainCode + "' " +
                        "ORDER BY Year DESC";
        SQLBean sql = new SQLBean(script);
        return sql;
	}
	
	public static SQLBean getDoubleLinkedItemCodes(String domainCode, String language) {
		SQLBean sql = new SQLBean();
		sql.select(null, "I.ItemCode", "Code");
		sql.select(null, "I.ItemNameE", "Label");
		sql.select(null, "AIE.AreaCode", "Area");
		sql.select(null, "AIE.ElementCode", "Element");
		sql.from("item", "I");
		sql.from("domainareaitemelement", "AIE");
		sql.where(SQL.DATE.name(), "I.ItemCode", "=", "AIE.ItemCode", null);
		sql.where(SQL.TEXT.name(), "AIE.DomainCode", "=", domainCode, null);
		sql.where(SQL.TEXT.name(), "I.ItemLevel", "<=", "5", null);
		sql.orderBy("I.ItemNameE", ORDERBY.ASC.name());
		sql.orderBy("AIE.AreaCode", ORDERBY.ASC.name());
		sql.orderBy("AIE.ElementCode", ORDERBY.ASC.name());
//		System.out.println(Bean2SQL.convert(sql));
		return sql;
	}
	
	public static SQLBean getDoubleLinkedCountryCodes(String domainCode, String language) {
		SQLBean sql = new SQLBean();
		sql.select(null, "A.AreaCode", "Code");
		sql.select(null, "A.AreaNameE", "Label");
		sql.select(null, "AIE.ItemCode", "Item");
		sql.select(null, "AIE.ElementCode", "Element");
		sql.from("area", "A");
		sql.from("domainareaitemelement", "AIE");
		sql.where(SQL.DATE.name(), "A.AreaCode", "=", "AIE.AreaCode", null);
		sql.where(SQL.TEXT.name(), "AIE.DomainCode", "=", domainCode, null);
		sql.where(SQL.TEXT.name(), "A.AreaLevel", "<=", "5", null);
		sql.orderBy("A.AreaNameE", ORDERBY.ASC.name());
		sql.orderBy("AIE.ItemCode", ORDERBY.ASC.name());
		sql.orderBy("AIE.ElementCode", ORDERBY.ASC.name());
//		System.out.println(Bean2SQL.convert(sql));
		return sql;
	}
	
	public static SQLBean getDoubleLinkedElementCodes(String domainCode, String language) {
		SQLBean sql = new SQLBean();
		sql.select(null, "E.ElementCode", "Code");
		sql.select(null, "E.ElementNameE", "Label");
		sql.select(null, "AIE.AreaCode", "Area");
		sql.select(null, "AIE.ItemCode", "Item");
		sql.from("element", "E");
		sql.from("domainareaitemelement", "AIE");
		sql.where(SQL.DATE.name(), "E.ElementCode", "=", "AIE.ElementCode", null);
		sql.where(SQL.TEXT.name(), "AIE.DomainCode", "=", domainCode, null);
		sql.orderBy("E.ElementNameE", ORDERBY.ASC.name());
		sql.orderBy("AIE.AreaCode", ORDERBY.ASC.name());
		sql.orderBy("AIE.ItemCode", ORDERBY.ASC.name());
//		System.out.println(Bean2SQL.convert(sql));
		return sql;
	}
	
	public static SQLBean getDomains(String language, String groupCode) {
		SQLBean sql = new SQLBean();
		sql.select(null, "D.DomainCode", "Code");
		sql.select(null, "D.DomainName" + lang(language), "Label");
		sql.select(null, "D.DateUpdate", "Date");
		sql.select(null, "D.Ord", "Ord");
		sql.from("Domain", "D");
		sql.where(SQL.TEXT.name(), "D.GroupCode", "=", groupCode, null);
		sql.groupBy("D.Ord");
		sql.groupBy("D.DomainName" + lang(language));
		sql.groupBy("D.DomainCode");
		sql.groupBy("D.DateUpdate");
		sql.orderBy("D.Ord", ORDERBY.ASC.name());
		sql.orderBy("D.DomainName" + lang(language), ORDERBY.ASC.name());
		sql.orderBy("D.DomainCode", ORDERBY.ASC.name());
//		System.out.println(Bean2SQL.convert(sql));
		return sql;
	}
	
	public static SQLBean getGroups(String language) {
		SQLBean sql = new SQLBean();
		sql.select(null, "D.GroupCode", "Code");
		sql.select(null, "D.GroupName" + lang(language), "Label");
		sql.select(null, "D.Ord", "Ord");
		sql.from("Domain", "D");
		sql.groupBy("D.Ord");
		sql.groupBy("D.GroupName" + lang(language));
		sql.groupBy("D.GroupCode");
		sql.orderBy("D.Ord", ORDERBY.ASC.name());
		sql.orderBy("D.GroupName" + lang(language), ORDERBY.ASC.name());
		sql.orderBy("D.GroupCode", ORDERBY.ASC.name());
//		System.out.println(Bean2SQL.convert(sql));
		return sql;
	}

	public static SQLBean getBulkDownloads(String language, String domainCode) {
		SQLBean sql = new SQLBean();
		sql.select(null, "B.Domain", "DomainCode");
		sql.select(null, "B.Source", "DomainLabel");
		sql.select(null, "B.Filename", "Filename");
		sql.select(null, "B.FileContent", "Content");
		sql.select(null, "B.CreatedDate", "Date");
		sql.from("BulkDownloads", "B");
		sql.where(SQL.TEXT.name(), "B.LanguageID", "=", lang(language), null);
		sql.where(SQL.TEXT.name(), "B.Domain", "=", domainCode, null);
//		System.out.println(Bean2SQL.convert(sql));
		return sql;
	}
	
	public static SQLBean getCountriesFromRegions(String domainCode, String language, String[] countryCodes) {
		SQLBean sql = new SQLBean();
		sql.select(null, "A.AreaCode", "Code");
		sql.select(null, "A.AreaName" + lang(language), "Label");
		sql.from("AreaGroupArea", "A");
		sql.where(SQL.DATE.name(), "A.AreaGroupCode", WHERE.IN.name(), null, countryCodes);
		sql.where(SQL.TEXT.name(), "A.Type", "=", "External", null);
//		System.out.println(Bean2SQL.convert(sql));
		return sql;
	}
	
	public static SQLBean getItemsFromAggregates(String domainCode, String language, String[] itemCodes) {
		SQLBean sql = new SQLBean();
		sql.select(null, "I.ItemCode", "Code");
		sql.select(null, "I.ItemName" + lang(language), "Label");
		sql.from("DomainItemGroupItem", "I");
		sql.where(SQL.DATE.name(), "I.ItemGroupCode", WHERE.IN.name(), null, itemCodes);
		sql.groupBy("I.ItemName" + lang(language));
		sql.groupBy("I.ItemCode");
//		System.out.println(Bean2SQL.convert(sql));
		return sql;
	}
	
	/**
	 * http://fenixapps.fao.org/wds/api?out=html&db=faostatproddiss&select=M.metadatacode,M.metadatanamee,M.metadatavaluee&
	 * from=Metadata_CPI[M]&where=M.areacode%28221%29&orderby=M.metadatacode
	 * @param language
	 * @param areaCode
	 * @return
	 */
	public static SQLBean getCPINotes(String language, String areaCode) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT M.MetadataName").append(lang(language)).append(", M.MetadataValue").append(lang(language)).append(" ");
		sb.append("FROM Metadata_CPI AS M ");
		sb.append("WHERE M.AreaCode = '").append(areaCode).append("' ");
		sb.append("ORDER BY M.MetadataName").append(lang(language)).append(" ");
		SQLBean sql = new SQLBean(sb.toString());
//		System.out.println(Bean2SQL.convert(sql));
		return sql;
	}
	
	public static String lang(String language) {
		if (language.equalsIgnoreCase("fr") || language.equalsIgnoreCase("F"))
			return "F";
		else if (language.equalsIgnoreCase("es") || language.equalsIgnoreCase("S"))
			return "S";
		else if (language.equalsIgnoreCase("zh"))
			return "C";
		else if (language.equalsIgnoreCase("ru"))
			return "R";
		if (language.equalsIgnoreCase("ar"))
			return "A";
		return "E";
	}
	
}