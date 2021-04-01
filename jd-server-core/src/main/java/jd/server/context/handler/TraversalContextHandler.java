package jd.server.context.handler;

import java.io.InputStream;
import java.io.OutputStream;

import jd.server.protocol.RawRequestMessage;
import jd.server.context.Context;

public class TraversalContextHandler extends AbstractContextHandler {

	
	private Context defaultProcessor ;
	private TraversalContextHandler() {}
	private TraversalContextHandler(Context defaultProcessor) {
		this.defaultProcessor = defaultProcessor ;
	}
	public static TraversalContextHandler of(Context defaultProcessor) {
		TraversalContextHandler h = new TraversalContextHandler();
		h.defaultProcessor = defaultProcessor ;
		return h;
	}
	
	@Override
	protected boolean contextHandle(RawRequestMessage reqMsg, InputStream is, OutputStream os) {
		if(!inited) init();
		for(Context ctx: contexts) {
			if(ctx.process(reqMsg,null/*, is, os*/)) {
				return true ;
			}
		}
		return false ;
	}

	private boolean inited = false ;
	private void init(){
		contexts.forEach(c -> c.init());
		if(defaultProcessor != null) defaultProcessor.init();
	}
	@Override
	protected Context getDefaultProcessor() {
		return defaultProcessor;
	}
}
