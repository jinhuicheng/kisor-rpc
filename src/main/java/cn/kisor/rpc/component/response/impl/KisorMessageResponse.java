package cn.kisor.rpc.component.response.impl;


import java.util.Map;

import cn.kisor.rpc.component.header.MessageHeader;
import cn.kisor.rpc.component.response.MessageResponse;
import cn.kisor.rpc.component.response.MessageResponseBody;


/**
 *
 * @author 孙浩
 * @email sunhao5@jd.com
 * @date 2016年1月17日 下午5:17:05
 * @description
 */
public class KisorMessageResponse implements MessageResponse {
	private transient MessageHeader header; // 响应头
	private MessageResponseBody body; // 响应体

	public KisorMessageResponse(MessageHeader header, MessageResponseBody body) {
		super();
		this.header = header;
		this.body = body;
	}

	@Override
	public Object getResult() {
		return body.getResult();
	}

	/*
	 * public void setResult(Object result) { body.setResult(result); }
	 */

	@Override
	public Throwable getError() {
		return body.getError();
	}

	@Override
	public Boolean isError() {
		return body.isError();
	}

	/*
	 * public void setError(Throwable error) { body.setError(error); }
	 */

	@Override
	public long getRequestId() {
		return this.getHeader().getRequestId();
	}

	@Override
	public byte getProtocolType() {
		return this.getHeader().getProtocolType();
	}

	@Override
	public byte getSerializeType() {
		return this.getHeader().getSerializeType();
	}

	@Override
	public byte getMsgType() {
		return this.getHeader().getMsgType();
	}

	@Override
	public byte getCompressType() {
		return this.getHeader().getCompressType();
	}

	@Override
	public Map<String, String> getAttachment() {
		return this.getHeader().getAttachment();
	}

	@Override
	public Map<String, String> addAttachmentAll(Map<String, String> attachment) {
		return this.getHeader().addAttachmentAll(attachment);
	}

	@Override
	public Map<String, String> addAttachment(String key, String value) {
		return this.getHeader().addAttachment(key, value);
	}

	@Override
	public MessageHeader getHeader() {
		return header;
	}

	/*
	 * @Override public int getLength() { return this.getHeader().getLength(); }
	 */

	@Override
	public MessageResponseBody getBody() {
		return body;
	}

}

