package cn.kisor.rpc.server.excutor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author 孙浩
 * @email sunhao5@jd.com
 * @description
 */
public class Invoker {
	private final static Map<MethodDescription, Method> methods = new ConcurrentHashMap<MethodDescription, Method>();

	public static Method getMethod(Object bean, String methodName, Class<?>[] parameterTypes) throws NoSuchMethodException, SecurityException {
		MethodDescription key = new MethodDescription(bean, methodName, parameterTypes);
		Method method = methods.get(key);
		if (method != null) {
			return method;
		}
		method = bean.getClass().getDeclaredMethod(methodName, parameterTypes);
		methods.put(key, method);
		return method;
	}

	public static class MethodDescription {
		private final Object bean;
		private final String MethodName;
		private final Class<?>[] parameterTypes;

		public MethodDescription(Object bean, String methodName, Class<?>[] parameterTypes) {
			super();
			this.bean = bean;
			MethodName = methodName;
			this.parameterTypes = parameterTypes;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((MethodName == null) ? 0 : MethodName.hashCode());
			result = prime * result + ((bean == null) ? 0 : bean.hashCode());
			result = prime * result + Arrays.hashCode(parameterTypes);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			MethodDescription other = (MethodDescription) obj;
			if (MethodName == null) {
				if (other.MethodName != null)
					return false;
			} else if (!MethodName.equals(other.MethodName))
				return false;
			if (bean == null) {
				if (other.bean != null)
					return false;
			} else if (!bean.equals(other.bean))
				return false;
			if (!Arrays.equals(parameterTypes, other.parameterTypes))
				return false;
			return true;
		}

	}
}
