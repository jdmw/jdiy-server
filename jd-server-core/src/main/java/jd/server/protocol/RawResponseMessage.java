package jd.server.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class RawResponseMessage {

	private String protocol ;
	private int statusCode ; // default: HttpStatusPorts.OK ;
	private Map<String,String> headers = new LinkedHashMap<>(); // do not reorder
	private ByteArrayOutputStream content = new ByteArrayOutputStream();
	
	public RawResponseMessage(String protocol,int statusCode) {
		this.protocol = protocol ;
		this.statusCode = statusCode ;
	}

	public RawResponseMessage(RawRequestMessage msg,int statusCode) {
		this.protocol = msg.getProtocol() ;
		this.statusCode = statusCode ;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public void setHeader(String name,String value) {
		this.headers.put(name, value);
	}
	
	public ByteArrayOutputStream getContent() {
		return content;
	}

	public void setContent(ByteArrayOutputStream content) {
		this.content = content;
	}


	@Override
	public void finalize() {
		if(content != null) {
			try {
				content.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
