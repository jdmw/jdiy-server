package jd.server.protocol;

import jd.server.context.handler.ContextHandler;
import jd.server.context.handler.TraversalContextHandler;
import jd.server.context.sevlet.DefaultServletContext;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HttpConfig {
    static class Config {
        File ctxRoot;
        String contextPath;
        boolean isServelt = true ;
    }
    private List<Config> list = new ArrayList<>();
    public HttpConfig set(File ctxRoot, String contextPath){
        Config cfg = new Config();
        cfg.ctxRoot = ctxRoot;
        cfg.contextPath = contextPath;
        list.add(cfg);
        return this ;
    }

    public ContextHandler traversalHandle(){
        TraversalContextHandler handler = TraversalContextHandler.of(null);
        list.forEach(c -> handler.addContext(new DefaultServletContext(c.ctxRoot,c.contextPath)));
        // list.clear();
        return handler;
    }
}
