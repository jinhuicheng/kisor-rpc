package cn.kisor.rpc.server;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections4.MapUtils;

import cn.kisor.rpc.annotation.Provider;
import cn.kisor.rpc.config.spring.ProviderBean;
import cn.kisor.rpc.utils.SpringContextHolder;

/**
 *
 * @author 孙浩
 * @email sunhao5@jd.com
 * @date 2016年1月18日 上午12:07:40
 * @description
 */
public class HandlerMapping {
	private final static Map<String, Object> handlerMap = new ConcurrentHashMap<String, Object>(); // 存放接口名与服务对象之间的映射关系

	@SuppressWarnings("unchecked")
	public static <T> T getServiceBean(String key) {
		return (T) handlerMap.get(key);
	}

	@SuppressWarnings("rawtypes")
	public static void initHandlerMapping() {
		// 注解Provider
		Map<String, Object> serviceBeanMap = SpringContextHolder.getApplicationContext().getBeansWithAnnotation(Provider.class); // 获取所有带有 RpcService 注解的 Spring Bean
		if (MapUtils.isNotEmpty(serviceBeanMap)) {
			for (Object serviceBean : serviceBeanMap.values()) {
				String interfaceName = serviceBean.getClass().getAnnotation(Provider.class).value().getName();
				handlerMap.put(interfaceName, serviceBean);
			}
		}
		// xml Provider
		final Map<String, ProviderBean> providerBeanMap = SpringContextHolder.getApplicationContext().getBeansOfType(ProviderBean.class, false, false);
		if (MapUtils.isNotEmpty(providerBeanMap)) {
			// final Set<Entry<String, ProviderBean>> entrySet = providerBeanMap.entrySet();
			// for (Entry<String, ProviderBean> entry : entrySet) {
			// handlerMap.put(entry.getKey(), entry.getValue().getRef());
			// }
			Collection<ProviderBean> values = providerBeanMap.values();
			for (ProviderBean providerBean : values) {
				handlerMap.put(providerBean.getInterfaceId(), providerBean.getRef());
			}
		}
	}
}
