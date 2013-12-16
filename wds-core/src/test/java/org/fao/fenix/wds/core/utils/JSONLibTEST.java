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
package org.fao.fenix.wds.core.utils;

import junit.framework.TestCase;
import org.json.simple.JSONArray;

public class JSONLibTEST extends TestCase {
	
	public void testLib1() {
		
		JSONArray row1 = new JSONArray();
		row1.add("Pippo");
		row1.add("Pluto");
		row1.add("Paperino");
		
		JSONArray row2 = new JSONArray();
		row2.add("Tizio");
		row2.add("Caio");
		row2.add("Sempronio");
		
		JSONArray table = new JSONArray();
		table.add(row1);
		table.add(row2);
		
		System.out.println(table.toJSONString());
		
		
		
	}
	
}