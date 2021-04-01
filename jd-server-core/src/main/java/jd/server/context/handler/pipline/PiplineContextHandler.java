package jd.server.context.handler.pipline;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import jd.server.protocol.RawRequestMessage;
import jd.server.context.Context;
import jd.server.context.handler.AbstractContextHandler;

public class PiplineContextHandler extends AbstractContextHandler {

	private Deque<ChPipMsgWrapper> in = null ;
	private CountDownLatch shutdownSignal = new CountDownLatch(1);
	private CountDownLatch doneSignal ;
	
	private Context defaultProcessor ;
	private PiplineContextHandler() {}
	public static PiplineContextHandler of(Context defaultProcessor) {
		PiplineContextHandler h = new PiplineContextHandler();
		h.defaultProcessor = defaultProcessor;
		return h ;
	}
	
	@Override
	protected Context getDefaultProcessor() {
		return defaultProcessor;
	}
	
	/**
	 * make the pipelines and start each thread
	 */
	public void initContexts() {
		List<ChPip> pips = new ArrayList<>();
		ChPip cp=null,lastcp = null ;
		// join each other
		if(defaultProcessor!= null) {
			contexts.add(defaultProcessor);
		}
		
		for(Context ctx :  contexts) {
			cp = new ChPip(ctx);
			pips.add(cp);
			if(lastcp == null) {
				in = cp.inQueue;
				cp.setLastdoneSignal(shutdownSignal);
			}else {
				lastcp.setNextPip(cp.inQueue);
				cp.setLastdoneSignal(lastcp.doneSignal);
			}
			lastcp = cp ;
		}
		doneSignal = lastcp.doneSignal;
		// start threads
		for(ChPip pip :  pips) {
			pip.startWorking();
		}
	}
	
	@Override
	public boolean contextHandle(RawRequestMessage reqMsg, InputStream is, OutputStream os) {
		in.addFirst(new ChPipMsgWrapper(reqMsg,is,os));
		return true;
	}
	
	public CountDownLatch getDoneSignal() {
		return doneSignal;
	}
	
	public void shutdown() {
		shutdownSignal.countDown();
	}
	
	/*
	 * shutdown the handle and wait until all working threads stopped
	 */
	public void shutdownSync() {
		shutdownSignal.countDown();
		await();
	}
	public void await() {
		try {
			doneSignal.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
