package cn.kisor.rpc.component.header.impl;

import java.util.HashMap;
import java.util.Map;

import cn.kisor.rpc.component.MsgType;
import cn.kisor.rpc.component.header.MessageHeader;

/**
 *
 * @author 孙浩
 * @email sunhao5@jd.com
 * @description
 */
public class KisorMessageHeader implements MessageHeader {
	/** header begin */
	private long requestId;
	private byte protocolType;// 协议类型
	private byte serializeType;// 序列化类型
	private byte msgType;// 消息类型
	private byte compressType;// 压缩类型
	private Map<String, String> attachment = new HashMap<String, String>(0);// 附件

	/** header end */

	@Override
	public long getRequestId() {
		return requestId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	@Override
	public byte getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(byte protocolType) {
		this.protocolType = protocolType;
	}

	@Override
	public byte getSerializeType() {
		return serializeType;
	}

	public void setSerializeType(byte serializeType) {
		this.serializeType = serializeType;
	}

	@Override
	public byte getMsgType() {
		return msgType;
	}

	public void setMsgType(MsgType msgType) {
		this.msgType = msgType.type;
	}

	@Override
	public byte getCompressType() {
		return compressType;
	}

	public void setCompressType(byte compressType) {
		this.compressType = compressType;
	}

	@Override
	public Map<String, String> getAttachment() {
		return attachment;
	}

	@Override
	public Map<String, String> addAttachmentAll(Map<String, String> attachment) {
		this.attachment.putAll(attachment);
		return attachment;
	}

	@Override
	public Map<String, String> addAttachment(String key, String value) {
		this.attachment.put(key, value);
		return attachment;
	}

}
