package jd.server.plugin.proxy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.Map;

import jd.server.protocol.handle.Protocolhandler;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import jd.server.protocol.Protocols;
import jd.server.protocol.handle.Protocolhandler;
import jd.server.protocol.RawRequestMessage;
import jd.server.protocol.RawResponseMessage;
import jd.server.protocol.handler.http.HttpMessageWriter;
import jd.util.ArrUt;
import jd.util.StrUt;
import jd.util.io.IOUt;
import jodd.util.StringUtil;

/**
 * example：
 * 	SimpleProxyConfig cfg=new SimpleProxyConfig("world.wallstreetenglish.com.cn","https");
 *	SimpleProxyConfig cfg=new SimpleProxyConfig("127.0.0.1",8080);
 *        //cfg.setDiskCacheDir(new File("F:\\doc\\net\\baidu"));
 *	Protocolhandler responser = new SimpleProxy(cfg);
 *	new SimpleBIOServer(Protocols.HTTP,null,8010).setProtocolhandler(responser).startup();
 */
public class SimpleProxy implements Protocolhandler {

	private static Logger logger = Logger.getLogger(SimpleProxy.class);
	
	private final String[] STATIC_EXTS = new String[] {"js","css",
			"svg","jpg","jpeg","jfif","gif","tif","tiff","png","bmp","ico", // image
			"mp4","mp3", // media
			"woff","woff2",//font
			} ;
	private String[] APPENDS = new String[] {";jsessionid"};
	
	private SimpleProxyConfig cfg ;
	
	public SimpleProxy(SimpleProxyConfig cfg) {
		this.cfg = cfg;
	}

	@SuppressWarnings("resource")
	public void handle(RawRequestMessage reqMsg,RawResponseMessage resp,InputStream is, OutputStream os) {
		InputStream remoteServerIn = null ;
		FileOutputStream fos = null ;
		String resourceUrl = dealWithPath(StrUt.substrBefore(reqMsg.getRequestURI(),"?"));
		String resourceUrlExtName = StrUt.substrAfter(resourceUrl,".",true);
		
		try {
			File file = null;
			URL url = null;
			if(cfg.isUseDiskCache() && (file = getCachedFilePosition(resourceUrl)) != null && file.exists() ) {
				// load local file
				try {
					//logger.debug("reading file:" + file.getRequestURI());
					remoteServerIn = new FileInputStream(file);
				} catch (FileNotFoundException e) {
					logger.error("can't find file:" + file,e);
					throw e ;
				}
			}else {
				if(!ArrUt.contains(STATIC_EXTS ,resourceUrlExtName)) {
					//if(cfg.getProtocol().equalsIgnoreCase(cfg.DEFAULT_PROTOCOL)) {
						//reqMsg.setProtocol("HTTP/1.1");
					//}
					Map<String, String[]>  headers = reqMsg.getHeaders(); 
					headers.put("Connection", new String[] {"keep-alive"});
					headers.put("Host", new String[] {cfg.getHost() + ((cfg.getPort() == Protocols.DEFAULT_HTTP_PORT
							|| cfg.getPort() == Protocols.DEFAULT_HTTPS_PORT ) ?"":":"+cfg.getPort())});
					Socket rmSocket = null;
					try {
						rmSocket = new Socket(cfg.getHost(),cfg.getPort());
						OutputStream so = rmSocket.getOutputStream();
						HttpMessageWriter.writeRequestMessage(so,reqMsg);
						remoteServerIn = rmSocket.getInputStream();
						//IOUtils.copy(remoteServerIn, os);
					} catch (IOException e) {
						logger.error("request " + reqMsg.getRequestURI() + " error",e);
						throw e ;
					} finally {
						//IOUtils.closeQuietly(rmSocket);
					}
				}else {
					try {
						url = new URL(cfg.getProtocol(),cfg.getHost(),reqMsg.getRequestURI());
						//logger.debug("requesting url:" + url);
						remoteServerIn = url.openStream();
						//logger.debug("opened url:" + url);
					} catch (IOException e) {
						logger.error("opened url error:" + url,e);
						throw e ;
					} // TODO host
				}
			}
			
			if(file != null && !file.exists()) {
				try {
					fos = new FileOutputStream(file) ;
					// logger.debug("downloading:" + file.getRequestURI());
					//System.out.println(IOUtils.toString(remoteServerIn));
					IOUt.copy(remoteServerIn, os, fos);
					//logger.debug("downloaded:" + file.getRequestURI());
				} catch (FileNotFoundException e) {
					logger.error("can't find file:" + file,e);
					throw e ;
				} catch (IOException e) {
					logger.error("request or donwload file error,write to file=" + file,e);
					throw e ;
				}
				  
			}else {
				try {
					IOUt.copy(remoteServerIn, os);
					//System.out.println(IOUtils.toString(remoteServerIn));
					//logger.debug("writen socket,file="+file+",url="+url);
				} catch (IOException e) {
					if(remoteServerIn instanceof FileInputStream) {
						logger.error("read file or write socket error,file="+file,e);
					}else {
						logger.error("request for url or write socket error,url="+reqMsg.getRequestURI(),e);
					}
					throw e ;
				}
			}
		}catch(Exception e) {
			logger.error(e);
		}finally {
			IOUtils.closeQuietly(remoteServerIn, fos);
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(os);
		}
	}

	private String dealWithPath(String path) {
		if(StringUtil.isNotBlank(path)) {
			for(String ap:APPENDS) {
				String t = StrUt.substrAfter(path, ap, true);
				if(t != null) {
					path = t ;
					break;
				}
			}
		}
		return path;
	}
	
	private File getCachedFilePosition(String path) throws FileNotFoundException {
		if(cfg.isUseDiskCache() && StringUtil.isNotBlank(path)) {
			if(path.endsWith("/")) {
				path += "index.html" ;
			}else {
				for(String ap:APPENDS) {
					String t = StrUt.substrAfter(path, ap, true);
					if(t != null) {
						path = t ;
						break;
					}
				}
			}
			File file = new File(cfg.getDiskCacheDir() + File.separator + path);
			if(!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			return file;
		}
		return null;
	}
	
	
	
}
