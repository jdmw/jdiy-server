package jd.server.protocol.handler.http;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import jd.server.protocol.RawRequestMessage;
import jd.server.protocol.RequestMessageParser;
import jd.util.lang.collection.ObjArrayMap;

public class HttpRequestMessageParser implements RequestMessageParser {
	
	private static Logger logger = Logger.getLogger(HttpRequestMessageParser.class);
	
	public RawRequestMessage parse(InputStream is, OutputStream os)  {
		RawRequestMessage msg = new RawRequestMessage();
		StringBuilder msgBody = new StringBuilder();
		BufferedInputStream bis = null;
		Scanner scan = null;
		String line ;
		String path=null;
		try {
			bis = IOUtils.buffer(is);
			scan = new Scanner(bis);
			scan.useDelimiter("\r\n");
			
			// read start line : method url protocol
			line = scan.next();
			String[] s = line.split(" ");
			msg.setMethod(s[0]);
			path = s[1];
			msg.setProtocol(s[2]);
			msgBody.append(line);
			if("GET".equals(msg.getMethod()) && path.contains("?")) {
				s = path.split("\\?");
				path = s[0];
				msg.setQueryString(s[1]);
			}
			
			// read header
			ObjArrayMap<String,String> headers = msg.getHeaders();
			int index ;
			String key,value ;
			while(scan.hasNext()) {
				line = scan.next();
				if(line != null && line.length() > 0) {
					index = line.indexOf(":");
					key = line.substring(0,index);
					value = line.substring(index);
					headers.add(key, value.trim());
					msgBody.append(line);
				}
			}
			
			// read content
			if(scan.hasNext()) {
				scan.next();
				msg.setIs(bis);
/*				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				IOUtils.copy(bis, baos);
				if(baos.size()>0) {
					byte[] bytes = baos.toByteArray();
					msg.setContent(bytes);
					
					String contentType = headers.get("Content-Type");
					String charset = contentType.contains("charset") ? Charset.defaultCharset().name() 
							: contentType.substring(contentType.indexOf("charset=") + "charset=".length());
					if("POST".equals(msg.getMethod()) && contentType.startsWith("text/") ) {
						String paramsString = new String(bytes,charset);
						msg.setParamsString(paramsString);
						url+="?"+paramsString;
					}
				}*/
			}
		}catch(Exception e) {
			logger.error(e);
		}finally {
			//IOUtils.closeQuietly(scan);
			//IOUtils.closeQuietly(bis);
		}
		msg.setUri(path);
		return msg ;
	}

}
