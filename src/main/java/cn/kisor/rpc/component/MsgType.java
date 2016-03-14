package cn.kisor.rpc.component;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author 孙浩
 * @email sunhao5@jd.com
 * @description 消息类型
 */
public enum MsgType {
	REQUEST((byte) 0), RESPONSE((byte) 1);
	public final byte type;

	private MsgType(byte type) {
		this.type = type;
	}

	private static final Map<Byte, MsgType> data = new HashMap<Byte, MsgType>();
	static {
		MsgType[] values = values();
		for (MsgType msgType : values) {
			data.put(msgType.type, msgType);
		}
	}

	public static MsgType getMsgType(byte type) {
		return data.get(type);
	}
}
