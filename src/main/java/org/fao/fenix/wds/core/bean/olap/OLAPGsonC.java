package org.fao.fenix.wds.core.bean.olap;

import java.util.ArrayList;
import java.util.List;

/** 
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class OLAPGsonC {

	private String d;
	
	private List<OLAPGsonD> v = new ArrayList<OLAPGsonD>();
	
	public boolean contains(OLAPGsonD d) {
		for (OLAPGsonD tmp : this.v)
			if (tmp.getD().equalsIgnoreCase(d.getD()))
				return true;
		return false;
	}

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}

	public List<OLAPGsonD> getV() {
		return v;
	}

	public void setV(List<OLAPGsonD> v) {
		this.v = v;
	}
	
}