package jd.server.protocol.handle;

import jd.server.protocol.RawResponseMessage;

import java.io.OutputStream;

public interface ProtocolResponser {

	void redirect(String topath, RawResponseMessage resp, OutputStream os);
}
