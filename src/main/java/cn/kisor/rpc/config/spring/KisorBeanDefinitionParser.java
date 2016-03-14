package cn.kisor.rpc.config.spring;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class KisorBeanDefinitionParser implements BeanDefinitionParser {
	private static final Logger LOGGER = LoggerFactory.getLogger(KisorBeanDefinitionParser.class);

	private final Class<?> beanClass;

	private final boolean required;

	public KisorBeanDefinitionParser(Class<?> beanClass, boolean required) {
		super();
		this.beanClass = beanClass;
		this.required = required;
	}

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		return parse(element, parserContext, beanClass, required);
	}

	private BeanDefinition parse(Element element, ParserContext parserContext, Class<?> beanClass, boolean required) {
		RootBeanDefinition beanDefinition = new RootBeanDefinition();
		beanDefinition.setBeanClass(beanClass);
		beanDefinition.setLazyInit(false);
		String id = element.getAttribute("id");
		if (StringUtils.isBlank(id) && required) {
			throw new IllegalStateException("this bean do not set spring bean id " + id);
		}
		// id 肯定是必须的所以此处去掉对id是否为空的判断
		if (required) {
			if (parserContext.getRegistry().containsBeanDefinition(id)) {
				throw new IllegalStateException("Duplicate spring bean id " + id);
			}
			parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);
		}
		// set各个属性值
		for (Method setter : beanClass.getMethods()) {
			LOGGER.debug("setter:{},beanClass :{}", setter, beanClass);
			if (!isProperty(setter, beanClass))
				continue; // 略过不是property的方法
			String name = setter.getName();
			String propertyName = name.substring(3, 4).toLowerCase() + name.substring(4);
			// 根据property名称来进行区别处理
			String value = element.getAttribute(propertyName);
			Object reference = value;
			// 默认非空字符串只是绑定值到属性 TODO
			if (StringUtils.isNotBlank(value)) {
				if ("ref".equals(propertyName)) {
					if (StringUtils.isNotBlank(value)) {
						BeanDefinition refBean = parserContext.getRegistry().getBeanDefinition(value);
						if (!refBean.isSingleton()) {
							throw new IllegalStateException("The exported service ref " + value + " must be singleton! Please set the " + value + " bean scope to singleton, eg: <bean id=\"" + value
									+ "\" scope=\"singleton\" ...>");
						}
						reference = new RuntimeBeanReference(value);
					} else {
						reference = null;// 保持住ref的null值
					}
					beanDefinition.getPropertyValues().addPropertyValue(propertyName, reference);
				} else {
					beanDefinition.getPropertyValues().addPropertyValue(propertyName, reference);
				}
			}
			if ("interfaceId".equals(propertyName)) {
				beanDefinition.getPropertyValues().addPropertyValue(propertyName, element.getAttribute("interface"));
			}

		}
		return beanDefinition;
	}

	/*
	 * 判断是否有相应get\set方法的property
	 */
	private boolean isProperty(Method method, Class<?> beanClass) {
		String methodName = method.getName();
		boolean flag = methodName.length() > 3 && methodName.startsWith("set") && Modifier.isPublic(method.getModifiers()) && method.getParameterTypes().length == 1;
		Method getter = null;
		if (flag) {
			Class<?> type = method.getParameterTypes()[0];
			try {
				getter = beanClass.getMethod("get" + methodName.substring(3), new Class<?>[0]);
			} catch (NoSuchMethodException e) {
				try {
					getter = beanClass.getMethod("is" + methodName.substring(3), new Class<?>[0]);
				} catch (NoSuchMethodException e2) {
				}
			}
			flag = getter != null && Modifier.isPublic(getter.getModifiers()) && type.equals(getter.getReturnType());
		}
		return flag;
	}
}
