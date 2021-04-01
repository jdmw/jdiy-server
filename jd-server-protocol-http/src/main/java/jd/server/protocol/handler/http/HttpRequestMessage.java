package jd.server.protocol.handler.http;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import jd.server.protocol.Protocols;
import jd.server.protocol.http.HttpHeaderNames;
import jd.server.protocol.RawRequestMessage;
import jd.server.util.ParamUtils;
import jd.util.StrUt;
import jd.util.lang.collection.ObjArrayMap;

public class HttpRequestMessage extends RawRequestMessage {

	protected String getProtocolWithoutVersion() {
		return super.getProtocol().split("/")[0].toLowerCase();
	}
	
	public StringBuffer getRequestURL() {
		String protocol = getProtocolWithoutVersion() ;
		return new StringBuffer(protocol + "://" + getHeader("Host") + super.getRequestURI());
	}
	
	public String getScheme() {
		return getProtocolWithoutVersion();
	}
	
	
	public boolean isSecure() {
		return Protocols.isSecure(getScheme());
	}
	
	/***********************************************************
	 *  Parameters
	 *********************************************************/
	private ObjArrayMap<String,String> ParameterMap = null ;
	
	
	public Map<String,String[]> getParameterMap() {
		if(ParameterMap == null) {
			ParameterMap = new ObjArrayMap<String,String>();
			if(queryString != null) {
				String[] sts = queryString.split("&");
				for(String st : sts) {
					if(st != null && !(st=st.trim()).equals("") ) {
						String[] sp = st.split("=");
						String name = sp[0];
						String value = sp.length>0?sp[1]:"";
						if(StrUt.isNotBlank(name)) {
							ParameterMap.add(name, value);
						}
					}
				}
			}
		}
		return ParameterMap;
	}
	
	public String getParameter(String name) {
		return ((ObjArrayMap<String,String>)getParameterMap()).getParameter(name);
	}

	public Enumeration<String> getParameterNames() {
		return ((ObjArrayMap<String,String>)getParameterMap()).getParameterNames();
	}

	public String[] getParameterValues(String name) {
		return ((ObjArrayMap<String,String>)getParameterMap()).get(name);
	}

	
	/***********************************************************
	 *  Headers
	 *********************************************************/
	
	private boolean cookieParsed = false ;
	private Map<String,String> cookieMap ;
	private Cookie[] cookieArray ;
	private void parseCookie() {
		if(!cookieParsed) {
			String cstr = getHeader(HttpHeaderNames.COOKIE);
			if(StrUt.isNotBlank(cstr)) {
				cookieMap = ParamUtils.splitParameterPairs(cstr, ";", ":");
				List<Cookie> cookies = new ArrayList<Cookie>(cookieMap.size());
				cookieMap.forEach((k,v)->{
					cookies.add( new Cookie(k,v));
				});
				cookieArray = cookies.toArray(new Cookie[cookies.size()]);
			}
		}
	}
	
	protected Map<String,String> getCookieMap() {
		if(!cookieParsed) {
			parseCookie();
		}
		return cookieMap;
	}
	
	public Cookie[] getCookies() {
		if(!cookieParsed) {
			parseCookie();
		}
		return cookieArray;
	}
	
	public long getContentLengthLong() {
		long len = -1 ;
		String cl = super.getHeader(HttpHeaderNames.CONTENT_LENGTH);
		if(cl != null) {
			len = Long.valueOf(cl);
		}
		return len;
	}
	
	
	public int getContentLength() {
		long l = getContentLengthLong();
		if(l > Integer.MAX_VALUE) {
			return -1 ;
		}
		return (int)l;
	}

	
	public String getContentType() {
		return super.getHeader(HttpHeaderNames.CONTENT_TYPE);
	}

	
	
	public String getRemoteHost() {
		String hostName = getHeader(HttpHeaderNames.HOST);
		if(StrUt.isNotBlank(hostName)) {
			hostName = hostName.split(":")[0].trim();
		}else {
			hostName = super.getRemoteHost() ;
		}
		return hostName ;
	}
	
/*	
	public String getAuthType() {
		return super.getHeader(HttpHeaderNames.AUTH_TYPE);
	}

	
	public String getPathInfo() {
		return super.getHeader(HttpHeaderNames.PATH_INFO);
	}

	
	public String getPathTranslated() {
		return super.getHeader(HttpHeaderNames.PATH_TRANSLATED);
	}
	*/

	
}
