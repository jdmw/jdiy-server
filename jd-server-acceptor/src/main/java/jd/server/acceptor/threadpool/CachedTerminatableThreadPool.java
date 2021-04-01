package jd.server.acceptor.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CachedTerminatableThreadPool implements ITerminatableThreadPool{
    //private final int maxLength ;
    private final int shutdownWaitingSeconds ;
    private final ExceptionHandler exceptionHandler;
    private final ExecutorService pool;
    //private Semaphore semaphore ;

    public CachedTerminatableThreadPool(int shutdownWaitingSeconds, ExceptionHandler exceptionHandler) {
        this.shutdownWaitingSeconds = shutdownWaitingSeconds;
        //this.semaphore= new Semaphore(maxLength);
        this.pool =  Executors.newCachedThreadPool();
        this.exceptionHandler = exceptionHandler;
    }

    public void submit(Runnable r){
        pool.submit(()->{
            try{
                //semaphore.acquire();
                r.run();
                //semaphore.release();
            }catch (Exception e){
                if(exceptionHandler !=null) {
                    exceptionHandler.handle(e);
                }else{
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void shutdown() {
        pool.shutdown();
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(shutdownWaitingSeconds, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(shutdownWaitingSeconds, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args){
        CachedTerminatableThreadPool pool = new CachedTerminatableThreadPool(1,(e)->e.printStackTrace());
        //ExecutorService pool = Executors.newFixedThreadPool(3);
        for(int i=0;i<1000;i++){
            final int t = i ;
            pool.submit(()->{
                System.out.format("[%s] i=%d\n",Thread.currentThread().getName(),t);
            });
        }
    }

}
