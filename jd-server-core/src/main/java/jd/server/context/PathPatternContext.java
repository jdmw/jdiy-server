package jd.server.context;

import jd.server.protocol.handle.ProtocolResponser;
import jd.server.protocol.RawRequestMessage;
import jd.server.protocol.RawResponseMessage;
import jd.server.util.PathUtils;
import jd.util.StrUt;

public abstract class PathPatternContext implements Context {

	private final static String SEPARATOR = "/" ;
	protected final String ctxpath ; // such as /a
	private final String ctxurl ; // such as /a/
	private final int ctxpathlength ;
	private final ProtocolResponser resp ;
	public PathPatternContext(String ctxpath,ProtocolResponser resp) {
		this.ctxpath = PathUtils.normalize(ctxpath);
		this.ctxurl = this.ctxpath + SEPARATOR ;
		this.ctxpathlength = this.ctxpath.length() ;
		this.resp = resp ;
	}

	public abstract boolean innerProcess(String path,RawRequestMessage reqMsg,RawResponseMessage resp/*, InputStream is, OutputStream os*/);

	public String getContextPath() {
		return ctxpath ;
	}

	// @Override
	public boolean process(RawRequestMessage reqMsg,RawResponseMessage res/*, InputStream is, OutputStream os*/) {
		String path = reqMsg.getRequestURI();
		if(path.startsWith(ctxurl)) {
			return innerProcess(path.substring(ctxpathlength),reqMsg, res/*,is,os*/);
		}else if(path.equals(ctxpath)) {
			String url = StrUt.concatIfLastNotBlank(ctxurl,"?",reqMsg.getQueryString());
			this.resp.redirect(url,res, null);
			return true ;
		}
		return false;
	}

}
