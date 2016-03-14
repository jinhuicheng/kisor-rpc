package cn.kisor.rpc.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 *
 * @author 孙浩
 * @email sunhao5@jd.com
 * @date 2016年1月15日 下午11:30:42
 * @description
 */
public class ProxyFactory {
	@SuppressWarnings("unchecked")
	public static <T> T getProxy(Class<?> clazz, InvocationHandler invocationHandler) {
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[] { clazz }, invocationHandler);
	}

}
