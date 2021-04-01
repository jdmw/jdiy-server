package jd.server.acceptor.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

import jd.server.acceptor.AbstractServer;
import jd.server.acceptor.entity.BasicConnectionInfo;
import jd.server.acceptor.ServerHandler;
import jd.server.acceptor.entity.ServerInfo;
import jd.server.acceptor.threadpool.ITerminatableThreadPool;

public class SimpleBIOServer extends AbstractServer {

	ITerminatableThreadPool pool ;
	public SimpleBIOServer(int port, ServerHandler handler,ITerminatableThreadPool pool) {
		this(new ServerInfo(port),handler,pool);
	}

	public SimpleBIOServer(ServerInfo info, ServerHandler handler,ITerminatableThreadPool pool) {
		super(info,handler);
		this.pool = pool;
	}

	public void startup() {
		Socket client = null ;
		try(ServerSocket ser = new ServerSocket(super.serverInfo.getServerPort())) {
			System.out.println("server start up at port: " + super.serverInfo.getServerPort());
			while(!super.shutdownflag && (client=ser.accept())!= null) {
				try {
					pool.submit(handler.handler(client));
				}catch(Throwable t) {
					t.printStackTrace();
				}
			}
			pool.shutdown();
		} catch (IOException e) {
			e.printStackTrace();
			pool.shutdown();
		}
	}

}
