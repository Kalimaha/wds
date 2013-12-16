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
package org.fao.fenix.wds.core.json;

import com.google.gson.Gson;
import org.fao.fenix.wds.core.bean.*;

/** 
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class JSON2Bean {
	
	public static Gson gson = new Gson();
	
	public static SQLBean toSQLBean(String json) {
		return gson.fromJson(json, SQLBean.class);
	}
	
	public static SelectBean toSelectBean(String json) {
		return gson.fromJson(json, SelectBean.class);
	}
	
	public static FromBean toFromBean(String json) {
		return gson.fromJson(json, FromBean.class);
	}
	
	public static WhereBean toWhereBean(String json) {
		return gson.fromJson(json, WhereBean.class);
	}
	
	public static GroupByBean toGroupByBean(String json) {
		return gson.fromJson(json, GroupByBean.class);
	}
	
	public static OrderByBean toOrderByBean(String json) {
		return gson.fromJson(json, OrderByBean.class);
	}

}