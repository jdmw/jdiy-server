package jd.server.protocol.handler;

import jd.server.protocol.Protocols;
import jd.server.protocol.RequestMessageParser;
import jd.server.protocol.handler.http.HttpRequestMessageParser;

public class ProtocolsUtil {

	public static RequestMessageParser getDefaultRequestMessageParser(String protocol) {
		RequestMessageParser p = null;
		if(Protocols.HTTP.equals(protocol)) {
			p =  new HttpRequestMessageParser();
		}
		return p ;
	}
}
