package cn.kisor.rpc.component.response;

import cn.kisor.rpc.component.header.MessageHeader;

/**
 *
 * @author 孙浩
 * @email sunhao5@jd.com
 * @date 2016年3月2日 下午6:17:17
 * @description
 */
public interface MessageResponse extends MessageHeader {

	Object getResult();

	Throwable getError();

	Boolean isError();

	MessageHeader getHeader();

	MessageResponseBody getBody();

}
