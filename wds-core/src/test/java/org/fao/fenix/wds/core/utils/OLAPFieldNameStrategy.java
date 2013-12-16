package org.fao.fenix.wds.core.utils;

import com.google.gson.FieldNamingStrategy;
import org.fao.fenix.wds.core.bean.olap.OLAPGsonFather;

import java.lang.reflect.Field;

public class OLAPFieldNameStrategy implements FieldNamingStrategy {

	@Override
	public String translateName(Field f) {
		
		if (f.getDeclaringClass().getSimpleName().equalsIgnoreCase(OLAPGsonFather.class.getSimpleName())) {
			System.out.println("FATHER");
			String s = f.getName();
			System.out.println(s);
		}
		
		else {
			System.out.println("SON");
			String s = f.getName();
			System.out.println(s);
			if (s.equalsIgnoreCase("children")) {
//				return "v";
				return s;
			} else {
				return s;
			}
		}
		
		return f.toString();
		
	}
	
}