package org.fao.fenix.wds.core.bean.olap;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/** 
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class OLAPGsonNode {

	@SerializedName("d")
	private String d;
	
	@SerializedName("v")
	private String v;
	
	@SerializedName("u")
	private String u;
	
	@SerializedName("f")
	private String f;
	
	@SerializedName("v")
	private List<OLAPGsonNode> children = new ArrayList<OLAPGsonNode>();

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}

	public String getV() {
		return v;
	}

	public void setV(String v) {
		this.v = v;
	}

	public String getU() {
		return u;
	}

	public void setU(String u) {
		this.u = u;
	}

	public String getF() {
		return f;
	}

	public void setF(String f) {
		this.f = f;
	}

	public List<OLAPGsonNode> getChildren() {
		return children;
	}

	public void setChildren(List<OLAPGsonNode> children) {
		this.children = children;
	}
	
}