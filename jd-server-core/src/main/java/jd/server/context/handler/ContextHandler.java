package jd.server.context.handler;

import java.io.InputStream;
import java.io.OutputStream;

import jd.server.protocol.RawRequestMessage;

public interface ContextHandler {
	
	//public void addContext(Context ctx);
	
	public void handle(RawRequestMessage reqMsg,InputStream is,OutputStream os);

	
}
