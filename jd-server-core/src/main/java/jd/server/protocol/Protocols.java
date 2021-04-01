package jd.server.protocol;

public class Protocols {

	public static final int DEFAULT_HTTP_PORT = 80 ;
	public static final int DEFAULT_HTTPS_PORT = 443 ;
	
	public static final String HTTP = "http" ;
	public static final String HTTPS = "https" ;
	
	public static boolean isSecure(String protocol) {
		switch(protocol) {
			case HTTPS : 
				return true ;
			default : 
				return false ;
		}
	}
	
	public static int getDefaultPort(String protocol) {
		return HTTPS.equalsIgnoreCase(protocol)
				? DEFAULT_HTTPS_PORT
				: DEFAULT_HTTP_PORT ;
	}
	
	
}
