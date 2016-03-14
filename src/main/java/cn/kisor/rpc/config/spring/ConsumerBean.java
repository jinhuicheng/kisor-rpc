package cn.kisor.rpc.config.spring;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import cn.kisor.rpc.client.RpcClient;
import cn.kisor.rpc.client.ServiceDiscovery;
import cn.kisor.rpc.component.MsgType;
import cn.kisor.rpc.component.header.impl.KisorMessageHeader;
import cn.kisor.rpc.component.request.impl.KisorMessageRequest;
import cn.kisor.rpc.component.request.impl.KisorMessageRequestBody;
import cn.kisor.rpc.component.response.impl.KisorMessageResponse;
import cn.kisor.rpc.constant.Constant;
import cn.kisor.rpc.exception.IllegalConfigureException;
import cn.kisor.rpc.exception.NoAliveProviderException;
import cn.kisor.rpc.utils.ProxyFactory;
import cn.kisor.rpc.utils.SpringContextHolder;
import cn.kisor.rpc.utils.thread.KisorFuture;

/**
 *
 * @author 孙浩
 * @email sunhao5@jd.com
 * @description
 */
@SuppressWarnings("rawtypes")
public class ConsumerBean implements BeanNameAware, InitializingBean, DisposableBean, FactoryBean, Serializable {
	private static final long serialVersionUID = 1506325302002996245L;
	protected String interfaceId;
	private transient String beanName;
	@Override
	public void destroy() throws Exception {
		// 消费者取消 TODO
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// 消费者注册 TODO
	}

	@Override
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}

	@Override
	public String toString() {
		return "ConsumerBean [interfaceId=" + interfaceId + ", beanName=" + beanName + "]";
	}

	@Override
	public Object getObject() throws Exception {
		return refer();
	}

	private synchronized Object refer() {
		return ProxyFactory.getProxy(getProxyClass(), new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				KisorMessageHeader requestHeader = new KisorMessageHeader();
				KisorMessageRequestBody requestBody = new KisorMessageRequestBody();
				requestBody.setValue(method.getDeclaringClass().getName(), "", method.getName(), method.getParameterTypes(), args);
				KisorMessageRequest request = new KisorMessageRequest(requestHeader, requestBody); // 创建并初始化 RPC 请求
				requestHeader.setRequestId(11111111111111111L); // TODO
				requestHeader.setMsgType(MsgType.REQUEST);
				String serverAddress = null;
				ServiceDiscovery serviceDiscovery = SpringContextHolder.getApplicationContext().getBean(ServiceDiscovery.class);
				if (serviceDiscovery != null) {
					serverAddress = serviceDiscovery.discover(); // 发现服务
				}
				if (StringUtils.isBlank(serverAddress)) {
					throw new NoAliveProviderException(method.getDeclaringClass().getName());
				}
				String[] array = serverAddress.split(":");
				String host = array[0];
				int port = Integer.parseInt(array[1]);
				RpcClient client = new RpcClient(host, port); // 初始化 RPC 客户端
				KisorFuture<KisorMessageResponse> kisorFuture = client.send(request); // 通过 RPC 客户端发送 RPC 请求并获取 RPC 响应
				KisorMessageResponse response = kisorFuture.get(Constant.RPC_TIMEOUT, TimeUnit.MILLISECONDS);// 同步阻塞
				if (response.isError()) {
					throw response.getError();
				} else {
					return response.getResult();
				}
			}
		});
	}

	/*
	 * @return
	 * @author 孙浩
	 * @date 2016年2月16日 下午5:46:58
	 * @description 获取class
	 */
	private Class<?> getProxyClass() {
		final Class<?> clazz;
		try {
			if (StringUtils.isNotBlank(interfaceId)) {
				clazz = Class.forName(interfaceId);
				if (!clazz.isInterface()) {
					throw new IllegalConfigureException("consumer.interfaceId" + interfaceId + "interfaceId must set interface class, not implement class");
				}
			} else {
				throw new IllegalConfigureException("consumer.interfaceId" + "null" + "interfaceId must be not null");
			}
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
		return clazz;
	}

	@Override
	public Class getObjectType() {
		return getProxyClass();
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
