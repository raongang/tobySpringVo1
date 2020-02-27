package springbook.dynamicProxy;

import java.lang.reflect.Proxy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.dynmicProxy.Hello;
import springbook.user.dynmicProxy.HelloTarget;
import springbook.user.dynmicProxy.UppsercaseHandler;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class DynmaicProxyTest {

	@Test
	public void dynamicProxy() {
		Hello dynimicProxiedHello = (Hello)Proxy.newProxyInstance( //생성된 다이나믹프록시 오브젝트는 Hello Interface를 구현하고 있으므로 Hello 타입으로 캐스팅 가능
				getClass().getClassLoader(), //동적으로 사용.
				new Class[] { Hello.class },
				new UppsercaseHandler(new HelloTarget()));
	}
	
	
}
