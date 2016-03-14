package cn.kisor.rpc.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.Map;

import cn.kisor.rpc.component.MsgType;
import cn.kisor.rpc.component.header.impl.KisorMessageHeader;
import cn.kisor.rpc.component.request.MessageRequestBody;
import cn.kisor.rpc.component.request.impl.KisorMessageRequest;
import cn.kisor.rpc.component.request.impl.KisorMessageRequestBody;
import cn.kisor.rpc.component.response.MessageResponseBody;
import cn.kisor.rpc.component.response.impl.KisorMessageResponse;
import cn.kisor.rpc.component.response.impl.KisorMessageResponseBody;
import cn.kisor.rpc.utils.SerializationUtil;

public class RpcDecoder extends LengthFieldBasedFrameDecoder {

	public RpcDecoder(int maxFrameLength) {

		// int maxFrameLength : 信息最大长度
		// int lengthFieldOffset : 长度属性的起始（偏移）位
		// int lengthFieldLength : “长度属性”的长度
		// int lengthAdjustment : 长度调节值，在总长被定义为包含包头长度时，修正信息长度，
		// int initialBytesToStrip : 跳过的字节数，根据需要我们跳过前0个字节，以便接收端直接接受到不含“长度属性”的内容
		super(maxFrameLength, 0, 4, 0, 4);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		ByteBuf frame = (ByteBuf) super.decode(ctx, in);
		if (frame == null) {
			return null;
		}
		KisorMessageHeader messageHeader = new KisorMessageHeader();
		messageHeader.setRequestId(frame.readLong());
		messageHeader.setProtocolType(frame.readByte());
		messageHeader.setSerializeType(frame.readByte());
		messageHeader.setMsgType(MsgType.getMsgType(frame.readByte()));
		messageHeader.setCompressType(frame.readByte());
		int attachmentSize = frame.readInt();
		Map<String, String> attachment = messageHeader.getAttachment();
		for (int i = 0; i < attachmentSize; i++) {
			byte keyLength = frame.readByte();
			byte valueLength = frame.readByte();
			byte[] keyArray = new byte[keyLength];
			frame.readBytes(keyArray);
			byte[] valueArray = new byte[valueLength];
			frame.readBytes(valueArray);
			String key = new String(keyArray, "UTF-8");
			attachment.put(key, new String(valueArray, "UTF-8"));
		}
		byte[] dataArray = new byte[frame.readableBytes()];
		frame.readBytes(dataArray);
		if (MsgType.REQUEST == MsgType.getMsgType(messageHeader.getMsgType())) {
			MessageRequestBody messageRequestBody = SerializationUtil.deserialize(dataArray, KisorMessageRequestBody.class);
			return new KisorMessageRequest(messageHeader, messageRequestBody);
		}
		if (MsgType.RESPONSE == MsgType.getMsgType(messageHeader.getMsgType())) {
			MessageResponseBody messageResponseBody = SerializationUtil.deserialize(dataArray, KisorMessageResponseBody.class);
			return new KisorMessageResponse(messageHeader, messageResponseBody);
		}
		return null;
	}
}