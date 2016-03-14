package cn.kisor.rpc.filter;

import java.lang.reflect.Method;

import cn.kisor.rpc.component.request.MessageRequest;
import cn.kisor.rpc.component.response.MessageResponse;
import cn.kisor.rpc.component.response.impl.KisorMessageResponseBody;
import cn.kisor.rpc.server.HandlerMapping;
import cn.kisor.rpc.server.excutor.Invoker;

/**
 *
 * @author 孙浩
 * @email sunhao5@jd.com
 * @description
 */
public class RequestInvokeFilter implements Filter {

	@Override
	public void doFilter(MessageRequest messageRequest, MessageResponse messageResponse, FilterChain taskChain) throws Exception {
		// KisorRequest kisorRequest = KisorContext.getKisorRequest();
		// FastClass serviceFastClass = FastClass.create(serviceBean.getClass());
		// FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
		// return serviceFastMethod.invoke(serviceBean, parameters);
		// Method method = kisorRequest.getServiceBean().getClass().getDeclaredMethod(kisorRequest.getMethodName(), kisorRequest.getParameterTypes());
		// return (T) method.invoke(kisorRequest.getServiceBean(), kisorRequest.getParameters());
		Object serviceBean = HandlerMapping.getServiceBean(messageRequest.getInterfaceName());
		// Method method = serviceBean.getClass().getDeclaredMethod(kisorRequest.getMethodName(), kisorRequest.getParameterTypes());
		Method method = Invoker.getMethod(serviceBean, messageRequest.getMethodName(), messageRequest.getParameterTypes());
		Object o = method.invoke(serviceBean, messageRequest.getParameters());
		KisorMessageResponseBody kisorRequestBody = (KisorMessageResponseBody) messageResponse.getBody();
		kisorRequestBody.setResult(o);
	}

}
