package cn.kisor.rpc.component.request;

import java.util.Map;

public interface MessageRequestBody {

	String getInterfaceName();

	Object getServiceBean();

	String getMethodName();

	Class<?>[] getParameterTypes();

	Object[] getParameters();

	Map<String, Object> getAttributes();

}
