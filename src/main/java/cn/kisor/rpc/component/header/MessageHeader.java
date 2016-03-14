package cn.kisor.rpc.component.header;

import java.util.Map;

/**
 *
 * @author 孙浩
 * @email sunhao5@jd.com
 * @description
 */
public interface MessageHeader {

	public long getRequestId();

	public byte getProtocolType();

	public byte getSerializeType();

	public byte getMsgType();

	public byte getCompressType();

	public Map<String, String> getAttachment();

	public Map<String, String> addAttachmentAll(Map<String, String> attachment);

	public Map<String, String> addAttachment(String key, String value);

}
