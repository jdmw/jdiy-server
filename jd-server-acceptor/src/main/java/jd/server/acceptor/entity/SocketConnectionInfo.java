package jd.server.acceptor.entity;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Locale;

public class SocketConnectionInfo implements BasicConnectionInfo {

	private final ServerInfo serverinfo ;
	private final Socket socket ;
	
	public SocketConnectionInfo(ServerInfo serverinfo, Socket socket) {
		this.serverinfo = serverinfo;
		this.socket = socket;
	}

	@Override
	public String getServerName() {
		return serverinfo.getServerName();
	}

	@Override
	public int getServerPort() {
		return serverinfo.getServerPort();
	}

	@Override
	public InetAddress getInetAddress() {
		return socket.getInetAddress();
	}

	@Override
	public InetAddress getLocalAddress() {
		return socket.getLocalAddress();
	}

	@Override
	public int getRemotePort() {
		return socket.getPort();
	}

	@Override
	public int getLocalPort() {
		return socket.getLocalPort();
	}
	
	
	public Locale getDefaultLocale(){
		return serverinfo.getDefaultLocale() ;
	}
}
