package org.fao.fenix.wds.core.bean.olap;

import java.util.ArrayList;
import java.util.List;

/** 
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class OLAPGsonB {

	private String d;
	
	private List<OLAPGsonC> v = new ArrayList<OLAPGsonC>();
	
	public boolean contains(OLAPGsonC c) {
		for (OLAPGsonC tmp : this.v)
			if (tmp.getD().equalsIgnoreCase(c.getD()))
				return true;
		return false;
	}

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}

	public List<OLAPGsonC> getV() {
		return v;
	}

	public void setV(List<OLAPGsonC> v) {
		this.v = v;
	}
	
	@Override
	public String toString() {
		return this.getD();
	}
	
}