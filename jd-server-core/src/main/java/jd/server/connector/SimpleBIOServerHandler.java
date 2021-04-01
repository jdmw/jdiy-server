package jd.server.connector;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import jd.server.acceptor.ServerHandler;
import jd.server.acceptor.entity.ServerInfo;
import jd.server.acceptor.entity.SocketConnectionInfo;
import jd.server.protocol.handle.Protocolhandler;
import org.apache.commons.io.IOUtils;

import jd.server.protocol.RawRequestMessage;
import jd.server.protocol.RequestMessageParser;


public class SimpleBIOServerHandler implements ServerHandler<Socket> {

	private final RequestMessageParser parser  ;
	private final Protocolhandler responser ;
	//private final Socket client ;
	private final ServerInfo serverInfo ;
	
	public SimpleBIOServerHandler(RequestMessageParser parser, Protocolhandler responser, ServerInfo serverInfo) {
		this.parser = parser;
		this.responser = responser;
		this.serverInfo = serverInfo ;
	}

	public Runnable handler(Socket client) {
		return ()->{
			RawRequestMessage reqMsg =null ;
			InputStream is = null;
			OutputStream os = null ;
			try {
				reqMsg = parser.parse(is = client.getInputStream(), os = client.getOutputStream());
				reqMsg.setConnectinoInfo(new SocketConnectionInfo(serverInfo,client));
				responser.handle(reqMsg, null,is, os);
			} catch (Throwable e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(client);
			}
		};
	}

}
