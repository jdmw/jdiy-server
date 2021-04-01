package jd.server.context;

import jd.server.protocol.RawRequestMessage;
import jd.server.protocol.RawResponseMessage;

public interface Context {

	public String getContextPath();
	public void init();
	public boolean process(RawRequestMessage reqMsg,RawResponseMessage resp/*,InputStream is,OutputStream os*/);
}
