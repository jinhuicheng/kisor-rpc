package cn.kisor.rpc.config;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import cn.kisor.rpc.utils.NetUtils;

/**
 *
 * @author 孙浩
 * @email sunhao5@jd.com
 * @description 服务端rpc配置
 */
public class ServerConfig {
	private int port = 2266;
	private String host = "localhost";
	private int maxConnection = 1000; // 最大连接数 default set to 1000
	private transient EventLoopGroup bossGroup = new NioEventLoopGroup();
	private transient EventLoopGroup workerGroup = new NioEventLoopGroup();

	// private transient final Map<String, Object> handlerMap = new ConcurrentHashMap<String, Object>(); // 存放接口名与服务对象之间的映射关系

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public EventLoopGroup getBossGroup() {
		return bossGroup;
	}

	public void setBossGroup(EventLoopGroup bossGroup) {
		this.bossGroup = bossGroup;
	}

	public EventLoopGroup getWorkerGroup() {
		return workerGroup;
	}

	public void setWorkerGroup(EventLoopGroup workerGroup) {
		this.workerGroup = workerGroup;
	}

	public void init() {
		initHost();
		// initHandlerMap();
	}

	// @SuppressWarnings("rawtypes")
	// private void initHandlerMap() {
	// // 注解Provider
	// Map<String, Object> serviceBeanMap = SpringContextHolder.getApplicationContext().getBeansWithAnnotation(Provider.class); // 获取所有带有 RpcService 注解的 Spring Bean
	// if (MapUtils.isNotEmpty(serviceBeanMap)) {
	// for (Object serviceBean : serviceBeanMap.values()) {
	// String interfaceName = serviceBean.getClass().getAnnotation(Provider.class).value().getName();
	// handlerMap.put(interfaceName, serviceBean);
	// }
	// }
	// // xml Provider
	// final Map<String, ProviderBean> providerBeanMap = SpringContextHolder.getApplicationContext().getBeansOfType(ProviderBean.class, false, false);
	// if (MapUtils.isNotEmpty(providerBeanMap)) {
	// // final Set<Entry<String, ProviderBean>> entrySet = providerBeanMap.entrySet();
	// // for (Entry<String, ProviderBean> entry : entrySet) {
	// // handlerMap.put(entry.getKey(), entry.getValue().getRef());
	// // }
	// Collection<ProviderBean> values = providerBeanMap.values();
	// for (ProviderBean providerBean : values) {
	// handlerMap.put(providerBean.getInterfaceId(), providerBean.getRef());
	// }
	// }
	// }

	private void initHost() {
		this.setHost(NetUtils.getLocalHost());
	}

	// public Map<String, Object> getHandlerMap() {
	// return handlerMap;
	// }

	public int getMaxConnection() {
		return maxConnection;
	}

	public void setMaxConnection(int maxConnection) {
		this.maxConnection = maxConnection;
	}
}
