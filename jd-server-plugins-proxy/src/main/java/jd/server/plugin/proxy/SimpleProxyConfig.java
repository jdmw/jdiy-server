package jd.server.plugin.proxy;

import java.io.File;

import jd.server.protocol.Protocols;
import jd.util.StrUt;
import jodd.util.StringUtil;

public class SimpleProxyConfig {

	private final static String DEFAULT_PROTOCOL = Protocols.HTTP ;
	private static final String[] SUPPORTED_PROTOCOLS = new String[]{DEFAULT_PROTOCOL,"https"} ;

	private String host ;
	private String protocol ;
	private int port ;
	
	private boolean useDiskCache ;
	private File diskCacheDir ;
	
	public SimpleProxyConfig(String host, String protocol,int port) {
		protocol = StringUtil.isNotBlank(protocol)?protocol.trim():DEFAULT_PROTOCOL;
		this.protocol = protocol ;
		setPort(port);
		setHost(host);
		if(this.port <= 0) {
			this.port = Protocols.getDefaultPort(protocol);
		}
	}
	
	public SimpleProxyConfig(String host, int port) {
		this(host,DEFAULT_PROTOCOL,port);
	}
	
	public SimpleProxyConfig(String host, String protocol) {
		this(host,protocol,-1);
	}
	
	public SimpleProxyConfig(String host) {
		this(host,null);
	}
	
	public void setHost(String host) {
		if(host != null) {
			// contain http or https
			if(host != null && host.contains("://")) {
				for(String prot : SUPPORTED_PROTOCOLS) {
					if(host.startsWith(prot.toLowerCase()) || host.startsWith(prot.toUpperCase())){
						host = StrUt.substrAfter(host, "://");
						this.protocol = prot ;
						break;
					}
				}
				if(host.contains(":")) {
					String[] s = host.split("\\:");
					host = s[s.length - 2];
					this.port = Integer.valueOf(s[s.length - 1]);
				}
			}
		}
		this.host = host;
	}
	
	public boolean isUseDiskCache() {
		return useDiskCache;
	}
	public File getDiskCacheDir() {
		return diskCacheDir;
	}
	public void setDiskCacheDir(File diskCacheDir) {
		this.diskCacheDir = diskCacheDir;
		this.useDiskCache = diskCacheDir != null ;
	}
	
	public String getHost() {
		return host;
	}
	
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

/*	public static void main(String[] args) {
		//SimpleProxyConfig cfg = new SimpleProxyConfig("world.wallstreetenglish.com.cn","https");
		SimpleProxyConfig cfg = new SimpleProxyConfig("127.0.0.1",8080);
		//cfg.setDiskCacheDir(new File("F:\\doc\\net\\baidu"));
		Protocolhandler responser = new SimpleProxy(cfg);
		new SimpleBIOServer(Protocols.HTTP,null,8010).setProtocolhandler(responser).startup();
	}*/
}
