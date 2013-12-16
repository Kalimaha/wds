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

import java.util.List;

/** 
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class WrapperConfigurations {

	private String cssName = "default";
	
	private String thousandSeparator = ",";
	
	private String decimalSeparator = ".";
	
	private int decimalNumbers = 2;
	
	private Integer valueColumnIndex;
	
	private List<Integer> valuesColumnIndex;
	
	public WrapperConfigurations() {
		
	}
	
	public WrapperConfigurations(String cssName) {
		this.setCssName(cssName);
	}
	
	public WrapperConfigurations(String cssName, Integer valueColumnIndex) {
		this.setCssName(cssName);
		this.setValueColumnIndex(valueColumnIndex);
	}

	public String getCssName() {
		return cssName;
	}

	public void setCssName(String cssName) {
		this.cssName = cssName;
	}

	public String getThousandSeparator() {
		return thousandSeparator;
	}

	public void setThousandSeparator(String thousandSeparator) {
		this.thousandSeparator = thousandSeparator;
	}

	public String getDecimalSeparator() {
		return decimalSeparator;
	}

	public void setDecimalSeparator(String decimalSeparator) {
		this.decimalSeparator = decimalSeparator;
	}

	public int getDecimalNumbers() {
		return decimalNumbers;
	}

	public void setDecimalNumbers(int decimalNumbers) {
		this.decimalNumbers = decimalNumbers;
	}

	public Integer getValueColumnIndex() {
		return valueColumnIndex;
	}

	public void setValueColumnIndex(Integer valueColumnIndex) {
		this.valueColumnIndex = valueColumnIndex;
	}

	public List<Integer> getValuesColumnIndex() {
		return valuesColumnIndex;
	}

	public void setValuesColumnIndex(List<Integer> valuesColumnIndex) {
		this.valuesColumnIndex = valuesColumnIndex;
	}
	
}