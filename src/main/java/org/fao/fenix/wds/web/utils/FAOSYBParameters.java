package org.fao.fenix.wds.web.utils;

import java.util.ArrayList;
import java.util.List;

public class FAOSYBParameters {

	private String dataKey;
	
	private List<String> variables;
	
	private Long dataID;
	
	private Long metaID;
	
	private Long parametersID;
	
	private Long typesID;
	
	private boolean countryBased;

	public String getDataKey() {
		return dataKey;
	}

	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}

	public Long getDataID() {
		return dataID;
	}

	public void setDataID(Long dataID) {
		this.dataID = dataID;
	}

	public Long getMetaID() {
		return metaID;
	}

	public void setMetaID(Long metaID) {
		this.metaID = metaID;
	}

	public List<String> getVariables() {
		return variables;
	}

	public void setVariables(List<String> variables) {
		this.variables = variables;
	}
	
	public void addVariable(String v) {
		if (this.variables == null)
			this.variables = new ArrayList<String>();
		this.variables.add(v);
	}

	public boolean isCountryBased() {
		return countryBased;
	}

	public void setCountryBased(boolean countryBased) {
		this.countryBased = countryBased;
	}

	public Long getParametersID() {
		return parametersID;
	}

	public void setParametersID(Long parametersID) {
		this.parametersID = parametersID;
	}

	public Long getTypesID() {
		return typesID;
	}

	public void setTypesID(Long typesID) {
		this.typesID = typesID;
	}

	@Override
	public String toString() {
		return "FAOSYBParameters [countryBased=" + countryBased + ", dataID=" + dataID + ", dataKey=" + dataKey + ", metaID=" + metaID + ", variables=" + variables + "]";
	}
	
}