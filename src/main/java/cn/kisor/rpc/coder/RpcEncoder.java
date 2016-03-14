package cn.kisor.rpc.coder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.Charset;
import java.util.Map.Entry;

import cn.kisor.rpc.component.header.MessageHeader;
import cn.kisor.rpc.component.request.MessageRequest;
import cn.kisor.rpc.component.response.MessageResponse;
import cn.kisor.rpc.utils.SerializationUtil;

public class RpcEncoder extends MessageToByteEncoder<Object> {

	/*
	 * private Class<?> genericClass; public RpcEncoder(Class<?> genericClass) { this.genericClass = genericClass; }
	 */

	public RpcEncoder() {
		super();
	}

	@Override
	public void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
		if (MessageRequest.class.isInstance(in)) {// 请求
			MessageRequest messageRequest = (MessageRequest) in;
			MessageHeader header = messageRequest.getHeader();
			byte[] bodyData = SerializationUtil.serialize(messageRequest.getBody());
			// ((KisorMessageHeader) header).setLength(bodyData.length);
			ByteBuf headerBuf = this.encodeHeader(header);
			out.writeInt(bodyData.length + headerBuf.readableBytes());
			out.writeBytes(headerBuf, 0, headerBuf.readableBytes());
			out.writeBytes(bodyData);
		} else if (MessageResponse.class.isInstance(in)) {// 响应
			MessageResponse messageResponse = (MessageResponse) in;
			MessageHeader header = messageResponse.getHeader();
			byte[] bodyData = SerializationUtil.serialize(messageResponse.getBody());
			// ((KisorMessageHeader) header).setLength(bodyData.length);
			ByteBuf headerBuf = this.encodeHeader(header);
			// out.writeInt(data.length);
			out.writeInt(bodyData.length + headerBuf.readableBytes());
			out.writeBytes(headerBuf, 0, headerBuf.readableBytes());
			out.writeBytes(bodyData);
		}
	}

	private ByteBuf encodeHeader(MessageHeader header) {
		// private int length;// 只包含请求体的长度
		// private long requestId;
		// private byte protocolType;// 协议类型
		// private byte serializeType;// 序列化类型
		// private byte msgType;// 消息类型
		// private byte compressType;// 压缩类型
		// private Map<String, Object> attachment = new HashMap<String, Object>();// 附件
		ByteBuf headerBuf = ByteBufAllocator.DEFAULT.heapBuffer();
		// headerBuf.writeInt(header.getLength());
		headerBuf.writeLong(header.getRequestId());
		headerBuf.writeByte(header.getProtocolType());
		headerBuf.writeByte(header.getSerializeType());
		headerBuf.writeByte(header.getMsgType());
		headerBuf.writeByte(header.getCompressType());
		headerBuf.writeInt(header.getAttachment().size());
		for (Entry<String, String> param : header.getAttachment().entrySet()) {
			String key = param.getKey() == null ? "" : param.getKey();
			String value = param.getValue() == null ? "" : param.getValue();
			headerBuf.writeInt(key.length());
			headerBuf.writeInt(value.length());
			headerBuf.writeBytes(key.getBytes(Charset.forName("UTF-8")));
			headerBuf.writeBytes(value.getBytes(Charset.forName("UTF-8")));
		}
		return headerBuf;
	}
}