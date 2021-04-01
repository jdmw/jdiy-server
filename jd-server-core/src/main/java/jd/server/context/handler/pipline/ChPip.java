package jd.server.context.handler.pipline;

import java.util.Deque;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import jd.server.context.Context;
import jd.util.lang.concurrent.ThreadSafeCounter;
//import jd.util.sys.Console;

public class ChPip {

	private final Context ctx ;
	
	protected final BlockingDeque<ChPipMsgWrapper> inQueue = new LinkedBlockingDeque<>();
	private Deque<ChPipMsgWrapper> nextQueue;
	private CountDownLatch lastdoneSignal;
	protected CountDownLatch doneSignal = new CountDownLatch(1);
	//private CountDownLatch nextDoneSignal ;
	//private final Deque<ChPipMsgWrapper> outQueue ;
	
	public ChPip(Context ctx) {
		this(ctx,1,1);
	}
	
	public ChPip(Context ctx,int handlerNum,int processorNum) {
		this.ctx = ctx;
		//this.outQueue = outQueue ;
		//this.nextQueue = outQueue ;
	}  
	
	public CountDownLatch getLastdoneSignal() {
		return lastdoneSignal;
	}

	public void setLastdoneSignal(CountDownLatch lastdoneSignal) {
		this.lastdoneSignal = lastdoneSignal;
	}

	public void setNextPip(Deque<ChPipMsgWrapper> nextQueue) {
		this.nextQueue = nextQueue ;
	}
	
	private final ExecutorService helperService = Executors.newWorkStealingPool();
	private final ThreadSafeCounter counter = new ThreadSafeCounter();
	
	private class Run implements Runnable{
		private final ChPipMsgWrapper w ;
		public Run(ChPipMsgWrapper w) {
			this.w = w ;
			counter.countUp();
		}
		@Override
		public void run() {
			//Console.tp("[%T][%s]pip thread start : " + w.getReqMsg().getUrl() + "\n",ctx);
			boolean finished = false;
			// process
			try {
				finished = ctx.process(w.getReqMsg(),null/*, w.getIs(), w.getOs()*/);
				//SysUt.threadPrint("[%t]%s\n", w.getReqMsg().getUrl());
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			w.setDone(finished);
			
			// next context
			if(!finished && nextQueue != null) {
				try {
					nextQueue.addFirst(w);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			//Console.tp("[%T][%s]pip thread stop : " + w.getReqMsg().getUrl() + "\n",ctx);
			counter.countDown();
		}
	}
	
	public void startWorking() {
		Executors.newWorkStealingPool().execute(() -> {
			while (lastdoneSignal.getCount() > 0) {
				final ChPipMsgWrapper wrapper = inQueue.pollLast(); // TODO ? is blocking IO capable here
				if (wrapper != null) {
					// execute in a new working thread
					helperService.execute(new Run(wrapper));
				}
			}
			if (lastdoneSignal.getCount() == 0) {
				//Console.tp("[%T][%s]last pip shutdown\n",ctx);
				while (true) {
					ChPipMsgWrapper wrapper = null;
					//try {
						wrapper = inQueue.pollLast();
					/*} catch (InterruptedException e) {
						e.printStackTrace();
					}*/
					//Console.tp("[%T][%s]take : " + (wrapper != null?wrapper.getReqMsg().getUrl():null) + "\n",ctx);
					if (wrapper != null) {
						// execute in a new working thread
						helperService.execute(new Run(wrapper));
					}
					if (inQueue.isEmpty()) {
						try {
							counter.await(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						// stop thread
						doneSignal.countDown();
						//Console.tp("[%T][%s]thread dead\n",ctx);
						break;
					}
				}
			}
		});
	}
	
}
