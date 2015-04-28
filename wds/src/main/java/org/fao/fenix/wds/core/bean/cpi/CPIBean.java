package org.fao.fenix.wds.core.bean.cpi;

/** 
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class CPIBean {
	
	private String lang;
	
	private String countries;
	
	private String items;
	
	private String years;

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getCountries() {
		return countries;
	}

	public void setCountries(String countries) {
		this.countries = countries;
	}

	public String getItems() {
		return items;
	}

	public void setItems(String items) {
		this.items = items;
	}

	public String getYears() {
		return years;
	}

	public void setYears(String years) {
		this.years = years;
	}
	
	@Override
	public String toString() {
		return "exec Warehouse.dbo.getCPINotes " + this.getLang() + ", " + this.getCountries() + ", " + this.getItems() + ", " + this.getYears();
	}
	

}