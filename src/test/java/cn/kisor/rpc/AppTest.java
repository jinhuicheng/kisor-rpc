package cn.kisor.rpc;

import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.kisor.rpc.annotation.Provider;
import cn.kisor.rpc.config.spring.ProviderBean;

@RunWith(SpringJUnit4ClassRunner.class)
// 整合
@ContextConfiguration(locations = { "classpath:spring.xml" })
// 加载配置
public class AppTest {
	@Resource
	private ApplicationContext applicationContext;

	@SuppressWarnings("rawtypes")
	@Test
	public void testApp() throws InterruptedException {
		Map<String, ProviderBean> beansOfType = applicationContext.getBeansOfType(ProviderBean.class, false, false);
		System.out.println(beansOfType);
		Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(Provider.class);
		System.out.println(serviceBeanMap);

		Thread.sleep(Long.MAX_VALUE);
	}
}
