package jd.server.protocol.handle;

import jd.server.protocol.RawRequestMessage;
import jd.server.protocol.RawResponseMessage;

import java.io.InputStream;
import java.io.OutputStream;

public interface Protocolhandler {
	
	void handle(RawRequestMessage reqMsg, RawResponseMessage resp, InputStream is, OutputStream os) throws Exception ;
}
