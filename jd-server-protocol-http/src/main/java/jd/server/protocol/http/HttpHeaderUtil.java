package jd.server.protocol.http;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import jd.util.StrUt;

public class HttpHeaderUtil {

	public static Vector<Locale> getAcceptLocales(Enumeration<String> acceptLanguages){
		Map<Float,Locale> map = new HashMap<>();	
		if(acceptLanguages != null && acceptLanguages.hasMoreElements()) {
			String acceptLanguage;
			while(acceptLanguages.hasMoreElements()) {
				acceptLanguage = acceptLanguages.nextElement();
				map.putAll(getAcceptLocales(acceptLanguage));
			}
		}
		return new Vector<>(map.values()) ;
	}

	public static Map<Float, Locale> getAcceptLocales(String acceptLanguage){
		Map<Float,Locale> map = new HashMap<>();
		String lang ;
		float q = 1 ;
		String[] langQ ;
		Locale locale ;
		for(String item  : acceptLanguage.split(",")) {
			if(StrUt.isNotBlank(item)) {
				langQ = item.trim().split(";q=");
				lang = langQ[0];
				q = langQ.length > 1 ? Float.valueOf(langQ[1]) : 1 ;

				String[] langCountry = lang.split("-");
				locale = langCountry.length > 1
						? new Locale(langCountry[0],langCountry[1])
						: new Locale(lang) ;
				map.put(q, locale);
			}
		}
		return map ;
	}
}
