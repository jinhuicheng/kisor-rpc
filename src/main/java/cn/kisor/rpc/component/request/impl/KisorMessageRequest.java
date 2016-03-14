package cn.kisor.rpc.component.request.impl;

import java.util.Map;

import cn.kisor.rpc.component.header.MessageHeader;
import cn.kisor.rpc.component.request.MessageRequest;
import cn.kisor.rpc.component.request.MessageRequestBody;

/**
 *
 * @author 孙浩
 * @email sunhao5@jd.com
 * @date 2016年1月15日 上午3:58:58
 * @description
 */
public class KisorMessageRequest implements MessageRequest {
	private MessageHeader header;// 请求体
	private MessageRequestBody body;// 请求体

	public KisorMessageRequest(MessageHeader header, MessageRequestBody body) {
		this.header = header;
		this.body = body;
	}

	@Override
	public KisorMessageRequest addAttributes(String key, String value) {
		body.getAttributes().put(key, value);
		return this;
	}

	@Override
	public String getInterfaceName() {
		return body.getInterfaceName();
	}

	@Override
	public Object getServiceBean() {
		return body.getServiceBean();
	}

	@Override
	public String getMethodName() {
		return body.getMethodName();
	}

	@Override
	public Class<?>[] getParameterTypes() {
		return body.getParameterTypes();
	}

	@Override
	public Object[] getParameters() {
		return body.getParameters();
	}

	@Override
	public MessageHeader getHeader() {
		return header;
	}

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
	public MessageRequestBody getBody() {
		return body;
	}
}