package cn.kisor.rpc.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextHolder implements ApplicationContextAware {
	private static final Logger LOGGER = LoggerFactory.getLogger(SpringContextHolder.class);

	/** 容器上下文 */
	private static ApplicationContext applicationContext = null;

	private void initApplicationContext(ApplicationContext _applicationContext) {
		LOGGER.debug("注入ApplicationContext到SpringContextHolder:" + applicationContext);

		if (applicationContext != null) {
			LOGGER.warn("SpringContextHolder中的ApplicationContext被覆盖, 原有ApplicationContext为:" + applicationContext);
		}

		SpringContextHolder.applicationContext = _applicationContext; // NOSONAR
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		initApplicationContext(applicationContext);
	}

	/**
	 * 实现DisposableBean接口,在Context关闭时清理静态变量.
	 */
	public void destroy() throws Exception {
		SpringContextHolder.clear();
	}

	/**
	 * 取得存储在静态变量中的ApplicationContext.
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 * 
	 * @param name
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		assertContext();
		return (T) getApplicationContext().getBean(name);
	}

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	public static <T> T getBean(String name, Class<T> requiredType) {
		assertContext();
		return (T) getApplicationContext().getBean(name, requiredType);
	}

	/**
	 * 清除SpringContextHolder中的ApplicationContext为Null.
	 */
	public static void clear() {
		LOGGER.debug("清除SpringContextHolder中的ApplicationContext:" + applicationContext);
		applicationContext = null;
	}

	/**
	 * 检查ApplicationContext不为空.
	 */
	private static void assertContext() {
		if (getApplicationContext() == null) {
			throw new IllegalStateException("applicaitonContext为空.");
		}
	}
}
