package jd.server.context.handler;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.TreeSet;

import jd.server.context.Context;
import jd.server.protocol.RawRequestMessage;
import jd.server.util.PathUtils;

public abstract class AbstractContextHandler implements ContextHandler {

	
	protected abstract Context getDefaultProcessor();
	
	protected Set<Context> contexts = new TreeSet<>((c1,c2)-> {
		return PathUtils.longerAheadPathComparator.compare(c1.getContextPath(), c2.getContextPath()) ;
	});
	
	/**
	 * handle context by url
	 * @param reqMsg
	 * @param is
	 * @param os
	 * @return if the request is handled,return true.
	 */
	protected abstract boolean contextHandle(RawRequestMessage reqMsg, InputStream is, OutputStream os);
	
	@Override
	public void handle(RawRequestMessage reqMsg, InputStream is, OutputStream os) {
		boolean done = false ;
		try{
			done = contextHandle( reqMsg,  is,  os);
		}catch(Throwable t) {
			t.printStackTrace();
		}
		if(!done) {
			getDefaultProcessor().process( reqMsg,null/*, is, os*/);
		}
	}
	
	
	public void addContext(Context ctx) {
		// multiple-thread safe
		synchronized(contexts) {
			contexts.add(ctx);
		}
	}
	
}
