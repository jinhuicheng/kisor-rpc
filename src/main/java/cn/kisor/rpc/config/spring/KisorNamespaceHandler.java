package cn.kisor.rpc.config.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class KisorNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser("provider", new KisorBeanDefinitionParser(ProviderBean.class, true));
		registerBeanDefinitionParser("consumer", new KisorBeanDefinitionParser(ConsumerBean.class, true));

	}

}
