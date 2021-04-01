package jd.server.acceptor;

import jd.server.acceptor.entity.ServerInfo;

// import jd.server.protocol.handle.ProtocolsUtil;

public abstract class AbstractServer implements IServer {

	protected final ServerInfo serverInfo ;
	protected final ServerHandler handler;
	protected boolean shutdownflag = false ;

	public AbstractServer(int port, ServerHandler handler) {
		this(new ServerInfo(port),handler);
	}

	public AbstractServer(ServerInfo serverInfo, ServerHandler handler) {
		this.serverInfo = serverInfo;
		this.handler = handler;
	}

	@Override
	public void shutdown() {
		shutdownflag = true ;
	}
	
/*
	public AbstractServer setRequestMessageParser(RequestMessageParser parser) {
		this.parser = parser;
		return this ;
	}
	
	public RequestMessageParser getRequestMessageParser() {
		*/
/*if(parser == null) {
			parser  = ProtocolsUtil.getDefaultRequestMessageParser(protocol);
		}*//*

		return parser ;
	}

	public Protocolhandler getProtocolhandler() {
		return protocolhandler;
	}

	public AbstractServer setProtocolhandler(Protocolhandler protocolhandler) {
		this.protocolhandler = protocolhandler;
		return this;
	}
*/

	
}
