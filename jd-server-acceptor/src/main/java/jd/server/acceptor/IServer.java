package jd.server.acceptor;

public interface IServer {

	public void startup();
	public void shutdown();

	public default void startupInNewThread(){
		new Thread(()->startup()).start();
	}

}
