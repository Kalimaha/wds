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

public class HTTP2SQLTest extends TestCase {
	
	public void testConvertSelect() {
		String sql  = "Value[Production],Value[Yield],Value[AreaHarvested]";
		String out = HTTP2SQL.convertSelect(sql).toString();
		System.out.print(out);
//		System.out.println();
	}
	
	public void testConvertFrom() {
		String sql  = "data[D]";
		String out = HTTP2SQL.convertFrom(sql).toString();
		System.out.print(out);
//		System.out.println();
	}
	
//	public void testConvertWhere() {
//		String sql  = "D.ItemCode(15),D.DomainCode('QC'),D.AreaCode(2:4)";
//		String out = HTTP2SQL.convertWhere(sql).toString();
//		System.out.print(out);
//		System.out.println();
//	}
	
	public void testConvertWhere_JOIN() {
		String sql  = "D.ItemCode(15),D.DomainCode('QC'),JOIN(A.AreaCode:D.AreaCode),D.AreaCode(2:4)";
		String out = HTTP2SQL.convertWhere(sql).toString();
		System.out.print(out);
//		System.out.println();
	}
	
	public void testConvertVariables() {
		String sql  = "ElementCode(5510:5419:5312)";
		String out = HTTP2SQL.convertVariables(sql).toString();
		System.out.print(out);
//		System.out.println();
	}
	
	public void testConvertGroupBy() {
		String sql  = "AreaCode,ElementCode";
		String out = HTTP2SQL.convertGroupBy(sql).toString();
		System.out.print(out);
//		System.out.println();
	}
	
	public void testConvertOrderBy() {
		String sql  = "Year[DESC]";
		String out = HTTP2SQL.convertOrderBy(sql).toString();
		System.out.print(out);
//		System.out.println();
	}
	
}