package org.fao.fenix.wds.core.bean.olap;

import java.util.List;

/** 
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 * */
public class OLAPGsonD {

	private String d;
	
	private List<OLAPGsonE> v;
	
	public boolean contains(OLAPGsonE e) {
		for (OLAPGsonE tmp : this.v)
			if (tmp.getD().equalsIgnoreCase(e.getD()))
				return true;
		return false;
	}

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}

	public List<OLAPGsonE> getV() {
		return v;
	}

	public void setV(List<OLAPGsonE> v) {
		this.v = v;
	}
	
}