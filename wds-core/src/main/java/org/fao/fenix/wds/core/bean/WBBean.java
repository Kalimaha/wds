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
package org.fao.fenix.wds.core.bean;

import java.text.DecimalFormat;

/** 
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class WBBean {
	
	private static DecimalFormat df = new DecimalFormat("#,###");
	
	private String date = "";
	
	private String value = "";
	
	private String indicatorCode = "";
	
	private String indicatorLabel = "";
	
	private String countryCode = "";
	
	private String countryLabel = "";
	
	private boolean showDate = false;
	
	private boolean showValue = false;
	
	private boolean showIndicator = false;
	
	private boolean showCountry = false;
	
	private boolean showHeaders = false;
	
	public boolean showDate() {
		return showDate;
	}

	public void setShowDate(boolean showDate) {
		this.showDate = showDate;
	}

	public boolean showValue() {
		return showValue;
	}

	public void setShowValue(boolean showValue) {
		this.showValue = showValue;
	}

	public boolean showIndicator() {
		return showIndicator;
	}

	public void setShowIndicator(boolean showIndicator) {
		this.showIndicator = showIndicator;
	}

	public boolean showCountry() {
		return showCountry;
	}

	public void setShowCountry(boolean showCountry) {
		this.showCountry = showCountry;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public void setValue(Double value) {
		this.value = df.format(value);
	}
	
	public boolean showHeaders() {
		return showHeaders;
	}

	public void setShowHeaders(boolean showHeaders) {
		this.showHeaders = showHeaders;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryLabel() {
		return countryLabel;
	}

	public void setCountryLabel(String countryLabel) {
		this.countryLabel = countryLabel;
	}

	public String getIndicatorCode() {
		return indicatorCode;
	}

	public void setIndicatorCode(String indicatorCode) {
		this.indicatorCode = indicatorCode;
	}

	public String getIndicatorLabel() {
		return indicatorLabel;
	}

	public void setIndicatorLabel(String indicatorLabel) {
		this.indicatorLabel = indicatorLabel;
	}

}