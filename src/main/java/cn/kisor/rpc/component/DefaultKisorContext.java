package cn.kisor.rpc.component;

import cn.kisor.rpc.component.header.impl.KisorMessageHeader;
import cn.kisor.rpc.component.request.MessageRequest;
import cn.kisor.rpc.component.request.impl.KisorMessageRequest;
import cn.kisor.rpc.component.request.impl.KisorMessageRequestBody;
import cn.kisor.rpc.constant.KisorContext;

/**
 * @author sunhao
 * @description : 任务上下文
 */
public class DefaultKisorContext implements KisorContext {
	private static final ThreadLocal<MessageRequest> threadLocal = new ThreadLocal<MessageRequest>() {
		@Override
		protected KisorMessageRequest initialValue() {
			return new KisorMessageRequest(new KisorMessageHeader(), new KisorMessageRequestBody());
		};
	};
	private static final KisorContext kisorContext = new DefaultKisorContext();

	private DefaultKisorContext() {
		super();
	}

	@Override
	public MessageRequest getMessageRequest() {
		return threadLocal.get();
	}

	/**
	 *
	 * @author 孙浩
	 * @email sunhao5@jd.com
	 * @description
	 */
	public static class DefaultKisorContextBuilder {
		public KisorContext getKisorContext() {
			return kisorContext;
		}
	}

	/**
	 *
	 * @author 孙浩
	 * @email sunhao5@jd.com
	 * @description
	 */
	public static class RequestRegister {
		public static void setMessageRequest(MessageRequest messageRequest) {
			threadLocal.set(messageRequest);
		}

		public static void remove() {
			threadLocal.remove();
		}
	}
}
