package jd.server.acceptor;

import jd.server.acceptor.bio.SimpleBIOServer;
import jd.server.acceptor.entity.ServerInfo;

import java.lang.reflect.Constructor ;
public enum  ServerConnectorType {
    BIO(SimpleBIOServer.class); // more: NIO,AIO

    Class<? extends AbstractServer> serverClass;
    private ServerConnectorType(Class<? extends AbstractServer> serverClass){
        this.serverClass = serverClass;
    }
    public <T extends AbstractServer> Constructor<T >  getServerConstructor(){
        try{
            return (Constructor<T >)this.serverClass.getConstructor(ServerInfo.class, ServerHandler.class);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
