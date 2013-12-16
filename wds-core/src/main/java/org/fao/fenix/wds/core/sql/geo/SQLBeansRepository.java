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
package org.fao.fenix.wds.core.sql.geo;

import org.fao.fenix.wds.core.bean.GroupByBean;
import org.fao.fenix.wds.core.bean.OrderByBean;
import org.fao.fenix.wds.core.bean.SQLBean;
import org.fao.fenix.wds.core.bean.WhereBean;
import org.fao.fenix.wds.core.constant.SQL;

import java.util.List;


public class SQLBeansRepository {

	
	public static SQLBean getSQL(String polygon) {
		SQLBean sql = new SQLBean();
		
		String s = "SELECT adm0_code, faost_n ";
		
		// FROM
		s += " FROM gaul0_faostat_3857 ";
		
		// WHERES
		s += "WHERE ";
		s += "ST_Polygon(ST_GeomFromText('LINESTRING( ";
        s += ""+ polygon +"";
		s += ")'),3857), geom);";

		sql.setQuery(s);
		return sql;
	}

}