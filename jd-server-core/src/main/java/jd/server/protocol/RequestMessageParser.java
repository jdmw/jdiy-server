package jd.server.protocol;

import java.io.InputStream;
import java.io.OutputStream;

public interface RequestMessageParser {

	RawRequestMessage parse(InputStream is,OutputStream os);
	
}
