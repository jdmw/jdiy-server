package jd.server.util;

import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

import jd.util.StrUt;

public class ParamUtils {

	public static Map<String,String> splitParameterPairs(String string,String pairSeparator,String nameValueSeparator){
		String[] sts = string.split(pairSeparator);
		Map<String,String> map = new LinkedHashMap<>();
		for(String st : sts) {
			if(st != null && !(st=st.trim()).equals("") ) {
				String[] sp = st.split("=");
				String name = sp[0].trim();
				String value = sp.length>0?sp[1].trim():"";
				if(StrUt.isNotBlank(name)) {
					map.put(name, value);
				}
			}
		}
		return map ;
	}
	
}
