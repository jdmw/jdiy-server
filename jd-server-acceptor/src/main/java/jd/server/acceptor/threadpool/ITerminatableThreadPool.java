package jd.server.acceptor.threadpool;

public interface ITerminatableThreadPool {
    public void submit(Runnable r);
    public void shutdown();
}
