package jd.server.protocol.handler.http;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import jd.server.protocol.RawRequestMessage;
import org.apache.commons.io.IOUtils;



public class HttpMessageWriter {

	private static final String LINE_SEP = "\r\n" ;
	public static void writeRequestMessage(OutputStream os, RawRequestMessage reqMsg) throws IOException {
		PrintWriter pw = new PrintWriter(os);
		pw.write(reqMsg.getMethod() + " " + reqMsg.getRequestURI() + reqMsg.getProtocol());
		pw.write(LINE_SEP);
		reqMsg.getHeaders().forEach((key,value)->{
			pw.write(key+": "+value + LINE_SEP);
		});
		pw.write(LINE_SEP);
		if("POST".equals(reqMsg.getMethod())) {
			IOUtils.copy(reqMsg.getIs(),os);
		}
		
		pw.flush();
		pw.close();
	}
	
	/*public static void writeResponseMessage(OutputStream os, RawResponseMessage reqMsg) throws IOException {

	}*/
}
