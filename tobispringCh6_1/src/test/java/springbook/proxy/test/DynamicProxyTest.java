package springbook.proxy.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Proxy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.proxyTest.Hello;
import springbook.user.proxyTest.HelloTarget;
import springbook.user.proxyTest.UppsercaseHandler;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class DynamicProxyTest {

	@Test
	public void simpleProxy() {

		/*
		Hello hello = new HelloTarget(); //target은 interface를 통해 접근하는 습관.
		assertThat(hello.sayHello("Toby"), is("Hello Toby"));
		assertThat(hello.sayHi("Toby"), is("Hi Toby"));
		assertThat(hello.sayThankYou("Toby"), is("Thank You Toby"));
		
		// 데코레이터 패턴 
		// proxy를 통해 target object에 접근하도록한다.
		
		Hello proxiedHello = new HelloUppercase(new HelloTarget());
		assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
		assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY"));
		assertThat(proxiedHello.sayThankYou("Toby"), is("THANK YOU TOBY"));
		*/
		
		/* 데코레이터 패턴의 단점을 수정한 JDK의 dynamic proxy 
		//생성된 다이나믹 프록시 오브젝트는 Hello 인터페이스를 구현하고 있으므로, Hello 타입 캐스팅 무관.
		Hello dynamicHello = (Hello)Proxy.newProxyInstance(
				getClass().getClassLoader(),  //동적으로 생성되는 다이나믹 프록시를 클래스의 로딩에 사용할 클래스 로더
				new Class[] {Hello.class}, //구현할 클래스
				new UppsercaseHandler(new HelloTarget())); //구현할 인터페이스
		
		assertThat(dynamicHello.sayHello("Toby"), is("HELLO TOBY"));
		assertThat(dynamicHello.sayHi("Toby"), is("HI TOBY"));
		assertThat(dynamicHello.sayThankYou("Toby"), is("THANK YOU TOBY"));
		*/
		
		/* 스프링 ProxyFactoryBean을 이용한 dynamic proxy 테스트 */
		
		
	}//end simpleProxy
	
	
	
}
