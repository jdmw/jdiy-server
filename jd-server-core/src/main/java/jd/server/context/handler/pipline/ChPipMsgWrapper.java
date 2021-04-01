package jd.server.context.handler.pipline;

import java.io.InputStream;
import java.io.OutputStream;

import jd.server.protocol.RawRequestMessage;
import jd.server.context.Context;

public class ChPipMsgWrapper {

	private RawRequestMessage reqMsg;
	private InputStream is;
	private OutputStream os;
	private Context targetContext ;
	private boolean init ;
	private boolean done ;
	
	public ChPipMsgWrapper(RawRequestMessage reqMsg, InputStream is, OutputStream os) {
		this.reqMsg = reqMsg;
		this.is = is;
		this.os = os;
	}
	public RawRequestMessage getReqMsg() {
		return reqMsg;
	}
	public void setReqMsg(RawRequestMessage reqMsg) {
		this.reqMsg = reqMsg;
	}
	public InputStream getIs() {
		return is;
	}
	public void setIs(InputStream is) {
		this.is = is;
	}
	public OutputStream getOs() {
		return os;
	}
	public void setOs(OutputStream os) {
		this.os = os;
	}
	public Context getTargetContext() {
		return targetContext;
	}
	public void setTargetContext(Context targetContext) {
		this.targetContext = targetContext;
	}
	public boolean isInit() {
		return init;
	}
	public void setInit(boolean init) {
		this.init = init;
	}
	public boolean isDone() {
		return done;
	}
	public void setDone(boolean done) {
		this.done = done;
	}
	
}
