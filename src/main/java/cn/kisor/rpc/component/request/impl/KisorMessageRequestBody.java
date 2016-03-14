package cn.kisor.rpc.component.request.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.kisor.rpc.component.request.MessageRequestBody;

public class KisorMessageRequestBody implements MessageRequestBody {
	/** body begin */
	private String interfaceName;
	private transient Object serviceBean;
	private String methodName;
	private Class<?>[] parameterTypes;
	private Object[] parameters;
	private final Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();

	/** body end */
	@Override
	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	@Override
	public Object getServiceBean() {
		return serviceBean;
	}

	public void setServiceBean(Object serviceBean) {
		this.serviceBean = serviceBean;
	}

	@Override
	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	@Override
	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	@Override
	public Object[] getParameters() {
		return parameters;
	}

	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setValue(String interfaceName, Object serviceBean, String methodName, Class<?>[] parameterTypes, Object[] parameters) {
		this.interfaceName = interfaceName;
		this.serviceBean = serviceBean;
		this.methodName = methodName;
		this.parameterTypes = parameterTypes;
		this.parameters = parameters;
	}

}
