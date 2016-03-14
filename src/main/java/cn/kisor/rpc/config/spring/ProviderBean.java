package cn.kisor.rpc.config.spring;

import java.io.Serializable;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 *
 * @author 孙浩
 * @email sunhao5@jd.com
 * @description
 * @param <T>
 */
public class ProviderBean<T> implements BeanNameAware, InitializingBean, DisposableBean, Serializable {
	private static final long serialVersionUID = -7360994495581622709L;
	protected String interfaceId;
	protected T ref;
	private transient String beanName;

	public String getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}

	public T getRef() {
		return ref;
	}

	public void setRef(T ref) {
		this.ref = ref;
	}


	@Override
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// 发布服务 TODO
	}

	@Override
	public void destroy() throws Exception {
		// 取消服务 TODO
	}

	@Override
	public String toString() {
		return "ProviderBean [interfaceId=" + interfaceId + ", ref=" + ref + ", beanName=" + beanName + "]";
	}

}
