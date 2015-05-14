package org.fao.fenix.wds.core.bean.olap;

import java.util.ArrayList;
import java.util.List;

/** 
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class OLAPGsonA {

	private List<String> header = new ArrayList<String>();
	
	private String d;
	
	private List<OLAPGsonB> v = new ArrayList<OLAPGsonB>();
	
	public boolean contains(OLAPGsonB b) {
		for (OLAPGsonB tmp : this.v)
			if (tmp.getD().equalsIgnoreCase(b.getD()))
				return true;
		return false;
	}

	public List<String> getHeader() {
		return header;
	}

	public void setHeader(List<String> header) {
		this.header = header;
	}

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}

	public List<OLAPGsonB> getV() {
		return v;
	}

	public void setV(List<OLAPGsonB> v) {
		this.v = v;
	}
	
}