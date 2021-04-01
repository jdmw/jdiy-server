package jd.server.acceptor;

public interface ServerHandler<T> {
    Runnable handler(T socket);
}
