package jd.server.protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Vector;

import jd.server.acceptor.entity.BasicConnectionInfo;
import jd.server.acceptor.entity.SocketConnectionInfo;
import jd.util.CUt;
import jd.util.StrUt;
import jd.util.lang.collection.ObjArrayMap;

public class RawRequestMessage  {

	private String protocol ;
	private String method ;
	private String uri ;
	protected String queryString ;
	protected ObjArrayMap<String,String> headers = new ObjArrayMap<String,String>();
	private InputStream is ;
	private boolean inputStreamGetted = false ;

	private SocketConnectionInfo connectinoInfo ;
	
	public RawRequestMessage() {}
	
	public RawRequestMessage(SocketConnectionInfo connectinoInfo) {
		this.connectinoInfo = connectinoInfo;
	}
	
	public void merge(RawRequestMessage m) {
		this.protocol = m.protocol;
		this.method = m.method;
		this.uri = m.uri ;
		this.queryString = m.queryString;
		this.headers = m.headers;
		this.is = m.is ;
		this.inputStreamGetted = m.inputStreamGetted;
		this.connectinoInfo = m.connectinoInfo;
	}
	
	public void setConnectinoInfo(SocketConnectionInfo connectinoInfo) {
		this.connectinoInfo = connectinoInfo;
	}

	public String getRequestURI() {
		return uri;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public void setIs(InputStream is) {
		this.is = is;
	}

	/**
	 * Returns the name and version of the protocol the request uses in the form
	 * protocol/majorVersion.minorVersion, for example, HTTP/1.1. For HTTP servlets,
	 * the value returned is the same as the value of the CGI variable 
	 * SERVER_PROTOCOL.
	 * 
	 * @return a String containing the protocol name and version number
	 */
	public String getProtocol() { // TODO  SERVER_PROTOCOL
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}

	public ObjArrayMap<String,String> getHeaders() {
		return headers;
	}

	public InputStream getIs() {
		if(!inputStreamGetted) {
			throw new IllegalStateException("InputStream has already been getted for this request");
		}
		inputStreamGetted = false ;
		return is;
	}
	
	public BufferedReader getReader() throws IOException{
		return new BufferedReader(new InputStreamReader(getIs()));
	}

	public boolean isInputStreamGetted() {
		return inputStreamGetted;
	}
	
	


	/**
	 * 
	 * Returns the value of the specified request header as a long value that
	 * represents a Date object. Use this method with headers that contain dates,
	 * such as If-Modified-Since. The date is returned as the number of milliseconds
	 * since January 1, 1970 GMT. The header name is case insensitive.
	 * 
	 * If the request did not have a header of the specified name, this method
	 * returns -1. If the header can't be converted to a date, the method throws an
	 * IllegalArgumentException.
	 * 
	 * @param name - a String specifying the name of the header
	 * @return a long value representing the date specified in the header expressed
	 *         as the number of milliseconds since January 1, 1970 GMT, or -1 if the
	 *         named header was not included with the request
	 * @Throws: IllegalArgumentException - If the header value can't be converted to a date
	 */
	public long getDateHeader(String name) {
		String v =  getHeader(name);
		long date = -1 ;
		if(v != null) {
			try{
				date = Long.valueOf(v);
			}catch(NumberFormatException e) {
				throw new IllegalArgumentException("the header " + name +"'s value '" + v + "' can't be converted to a date",e);
			}
		}
		return date ;
	}
	
	public int getIntHeader(String name) {
		String v =  getHeader(name);
		int i = -1 ;
		if(v != null) {
			try{
				i = Integer.valueOf(v);
			}catch(NumberFormatException e) {
				//throw new NumberFormatException("the header " + name +"'s value '" + v + "' can't be converted to a date",e);
				throw e ;
			}
		}
		return i ;
	}


	public String getHeader(String name) {
		return headers.getFirst(name);
	}


	public Enumeration<String> getHeaders(String name) {
		Vector<String> list = new Vector<>();
		String[] values = headers.get(name);
		if(values != null && values.length > 0) {
			CUt.addAll(list,values);
		}
		return list.elements();
	}


	/**
	 * Returns an enumeration of all the header names this request contains. If the request has no headers, this method returns an empty enumeration.
	 * TODO : Some servlet containers do not allow servlets to access headers using this method, in which case this method returns null
	 * @return
	 */
	public Enumeration<String> getHeaderNames() {
		return headers.getParameterNames();
	}

	public BasicConnectionInfo getConnectinoInfo() {
		return connectinoInfo;
	}

	
	public String getServerName() {
		return connectinoInfo.getServerName();
	}

	public int getServerPort() {
		return connectinoInfo.getServerPort();
	}

	/**
	 * Returns the Internet Protocol (IP) source port of the client or last proxy that sent the request.
	 * @return
	 */
	public int getRemotePort() {
		return connectinoInfo.getRemotePort();
	}

	/**
	 * Returns the Internet Protocol (IP) port number of the interface on which the request was received.
	 * @return
	 */
	public int getLocalPort() {
		return connectinoInfo.getLocalPort();
	}
	
	public String getLocalAddr() {
		return connectinoInfo.getLocalAddress().getHostAddress() ;
	}
	
	public String getLocalName() {
		String name =  connectinoInfo.getLocalAddress().getHostName() ;
		if(StrUt.isBlank(name)) {
			name = getLocalAddr();
		}
		return name ;
	}
	
	public String getRemoteAddr() {
		return connectinoInfo.getInetAddress().getHostAddress() ;
	}
	
	public String getRemoteHost() {
		String name =  connectinoInfo.getInetAddress().getHostName() ;
		if(StrUt.isBlank(name)) {
			name = getRemoteAddr();
		}
		return name ;
	}

	public Locale getDefaultLocale(){
		return connectinoInfo.getDefaultLocale();
	}
	
	
}
