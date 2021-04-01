package jd.server;

import jd.server.acceptor.AbstractServer;
import jd.server.acceptor.ServerConnectorType;
import jd.server.context.handler.ContextHandler;
import jd.server.context.handler.TraversalContextHandler;
import jd.server.context.sevlet.DefaultServletContext;
import jd.server.plugin.proxy.SimpleProxy;
import jd.server.plugin.proxy.SimpleProxyConfig;
import jd.server.protocol.Protocols;
import jd.server.protocol.RequestMessageParser;
import jd.server.protocol.handle.Protocolhandler;
import jd.server.protocol.handler.http.HttpRequestMessageParser;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class JdServerApplication {

    private final SimpleProxyConfig config ;
    private final int port;
    private int mgrPort = 2081 ;
    private String serverName = "jd-server" ;
    private String protocol = Protocols.HTTP;
    private ServerConnectorType type ;
    Protocolhandler protocolhandler ;
    ContextHandler contextHandler;

    public JdServerApplication(int port) {
        this(Protocols.HTTP,port);
    }
    public JdServerApplication(String protocol,int port) {
        this.config = new SimpleProxyConfig(null,protocol,port);
        this.protocol = protocol ;
        this.serverName = serverName;
        this.port = port ;
    }

    public JdServerApplication protocolhandler(Protocolhandler protocolhandler) {
        this.protocolhandler = protocolhandler;
        return this;
    }
    public JdServerApplication managePort(int port) {
        this.mgrPort = port;
        return this;
    }
    public JdServerApplication name(String name) {
        this.serverName = name;
        return this;
    }

    public JdServerApplication type(ServerConnectorType type) {
        this.type = type;
        return this;
    }

    private static RequestMessageParser getRequestMessageParser(String protocol){
        if (Protocols.HTTP.equals(protocol)){
            return new HttpRequestMessageParser();
        }
        throw new RuntimeException("Protocol "  + protocol + " is not supported");
    }
    /*private static DefaultServletContext getDefaultServletContext(String protocol){
        if (Protocols.HTTP.equals(protocol)){
            return new DefaultServletContext();
        }
        throw new RuntimeException("Protocol "  + protocol + " is not supported");
    }*/

    public JdServerApplication contextHandler(ContextHandler contextHandler){
        this.contextHandler = contextHandler;
        return this ;
    }

    private HttpConfig http(){
        return new HttpConfig();
    }

    class HttpConfig {
        class Config {
            File ctxRoot;
            String contextPath;
            boolean isServelt = true ;
        }
        private List<Config> list = new ArrayList<>();
        public HttpConfig setPath(File ctxRoot, String contextPath){
            Config cfg = new Config();
            cfg.ctxRoot = ctxRoot;
            cfg.contextPath = contextPath;
            list.add(cfg);
            return this ;
        }

        public JdServerApplication traversalHandle(){
            TraversalContextHandler h = TraversalContextHandler.of(null);
            list.forEach(c -> h.addContext(new DefaultServletContext(c.ctxRoot,c.contextPath)));
            // list.clear();
            return JdServerApplication.this.contextHandler(h) ;
        }
    }

    public void start() throws Exception{
        Constructor<AbstractServer> c = type.getServerConstructor();
        RequestMessageParser parser = getRequestMessageParser(protocol);
        Protocolhandler protocolhandler = new SimpleProxy(config);
        AbstractServer server = c.newInstance(protocol,serverName,port,parser,protocolhandler);
        server.startup();
    }

    public static void main(String[] args) throws Exception {
        new JdServerApplication(8900)
                .http().setPath(new File("root"),"/").traversalHandle().managePort(9801).start();
    }
}
