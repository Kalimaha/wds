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

import org.fao.fenix.wds.core.exception.WDSException;

import java.util.ArrayList;
import java.util.List;

/** 
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class MatrixUtils {

	public static List<List<String>> transpose(List<List<String>> in) throws WDSException {
		try {
			List<List<String>> out = new ArrayList<List<String>>();
			int cols = in.get(0).size();
			int rows = in.size();
			String[][] tmp = new String[rows][cols];
			String[][] t = new String[cols][rows];
			for (int i = 0 ; i < in.size() ; i++)
				for (int j = 0 ; j < in.get(i).size() ; j++)
					tmp[i][j] = in.get(i).get(j);
			for (int i = 0 ; i < tmp.length ; i++)
				for (int j = 0 ; j < tmp[i].length ; j++)
					t[j][i] = tmp[i][j];
			for (int i = 0 ; i < t.length ; i++) {
				List<String> row = new ArrayList<String>();
				for (int j = 0 ; j < t[i].length ; j++)
					row.add(t[i][j]);
				out.add(row);
			}
			return out;
		} catch (Exception e) {
			throw new WDSException(e.getMessage());
		}
	}
	
}