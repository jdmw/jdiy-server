package jd.server.protocol.handler.http;

import java.io.OutputStream;
import java.util.Date;

import jd.server.protocol.handle.ProtocolResponser;
import jd.server.protocol.http.HttpHeaderNames;
import jd.server.protocol.http.HttpStatusPorts;
import jd.server.protocol.RawResponseMessage;
import jd.util.lang.Console;

public class HttpResponser implements ProtocolResponser {

	
	@SuppressWarnings("deprecation")
	@Override
	public void redirect(String topath, RawResponseMessage resp,OutputStream os) {
		Console.ln("HTTP/1.1 302\r\n" +  // status code
				"Location: " + topath+"\r\n" + 
				"Transfer-Encoding: chunked\r\n" + 
				"Date: " + new Date().toGMTString());
		
		resp.setStatusCode(HttpStatusPorts.REDIRECT);
		resp.setHeader(HttpHeaderNames.LOCATION, "topath");
	}

}
	