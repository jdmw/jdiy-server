package jd.server.acceptor.entity;

import java.net.InetAddress;
import java.util.Locale;

public interface BasicConnectionInfo {

	
	String getServerName() ;

	int getServerPort();

	/**
	 * Returns the Internet Protocol (IP) source port of the client or last proxy that sent the request.
	 * @return
	 */
	int getRemotePort();

	/**
	 * Returns the Internet Protocol (IP) port number of the interface on which the request was received.
	 * @return
	 */
	int getLocalPort() ;

	InetAddress getInetAddress();
	
	InetAddress getLocalAddress();
	
	
	Locale getDefaultLocale();
}
