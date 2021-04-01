package jd.server.acceptor.entity;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;

public class ServerInfo {

	private final String serverName;
	private final int port ;
	private Locale defaultLocale = Locale.getDefault() ;
	
	public ServerInfo(int port) {
		this(null,port);
	}
	
	public ServerInfo(String serverName ,int port) {
		this.serverName = serverName ;
		this.port = port ;
	}
	
	/**
	 * Returns the host name of the server to which the request was sent. It is the resolved server name, or the server IP address.
	 * @return
	 */
	public String getServerName() {
		if(serverName != null) {
			return serverName ;
		}else {
			try {
				return InetAddress.getLocalHost().getHostAddress() ;
			} catch (UnknownHostException e) {
				e.printStackTrace();
				return "" ;
			}
		}
	}

	public int getServerPort() {
		return this.port;
	}

	public Locale getDefaultLocale() {
		return defaultLocale;
	}

	public void setDefaultLocale(Locale defaultLocale) {
		this.defaultLocale = defaultLocale;
	}
	
	
	
}
