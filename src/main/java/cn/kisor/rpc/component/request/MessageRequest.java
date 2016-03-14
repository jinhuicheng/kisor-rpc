package cn.kisor.rpc.component.request;

import cn.kisor.rpc.component.header.MessageHeader;

/**
 *
 * @author 孙浩
 * @email sunhao5@jd.com
 * @date 2016年3月2日 下午6:28:25
 * @description
 */
public interface MessageRequest extends MessageHeader {
	MessageHeader getHeader();

	MessageRequestBody getBody();

	Object[] getParameters();

	MessageRequest addAttributes(String key, String value);

	String getInterfaceName();

	Object getServiceBean();

	String getMethodName();

	Class<?>[] getParameterTypes();

}
