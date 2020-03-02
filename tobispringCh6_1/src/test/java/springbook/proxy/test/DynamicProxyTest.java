package springbook.proxy.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.PatternMatchUtils;

import springbook.user.proxyTest.Hello;
import springbook.user.proxyTest.HelloTarget;
import springbook.user.proxyTest.UppsercaseHandler;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class DynamicProxyTest {

	@Test
	public void simpleProxy() {
		/**
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
		
		/** 데코레이터 패턴의 단점을 수정한 dynamic proxy */
		//생성된 다이나믹 프록시 오브젝트는 Hello 인터페이스를 구현하고 있으므로, Hello 타입 캐스팅 무관.
		Hello dynamicHello = (Hello)Proxy.newProxyInstance(
				getClass().getClassLoader(),  //동적으로 생성되는 다이나믹 프록시를 클래스의 로딩에 사용할 클래스 로더
				new Class[] {Hello.class}, //구현할 클래스
				new UppsercaseHandler(new HelloTarget())); //구현할 인터페이스
		
		assertThat(dynamicHello.sayHello("Toby"), is("HELLO TOBY"));
		assertThat(dynamicHello.sayHi("Toby"), is("HI TOBY"));
		assertThat(dynamicHello.sayThankYou("Toby"), is("THANK YOU TOBY"));
	}//end simpleProxy test
	
	@Test
	public void classNamePointcutAdvisor() {
		
		//포인트컷 준비
		//익명내부클래스 방식으로 클래스를 정의한다.
		NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut() {
			private static final long serialVersionUID = 1L;
			public ClassFilter getClassFilter() {
				return new ClassFilter() {
					@Override
					public boolean matches(Class<?> clazz) {
						// TODO Auto-generated method stub
						return clazz.getSimpleName().startsWith("HelloT"); //class이름이 HelloT로 시작하는것만
					}
				};
			}
		};
		
		classMethodPointcut.setMappedName("sayH*"); // sayH로 시작하는 메소드이름을 가진 메소드만 선정한다.
		
		//테스트
		//HelloTarget() ->적용 class
		checkAdviced(new HelloTarget(), classMethodPointcut, true);
		
		class HelloWorld extends HelloTarget{}; //advice 미적용.
		checkAdviced(new HelloWorld(),classMethodPointcut,false);
		
		class HelloToby extends HelloTarget{};
		checkAdviced(new HelloToby(),classMethodPointcut, true);
	}//end test
	
	
	//advanced - 적용대상여부
	private void checkAdviced(Object target, Pointcut pointcut, boolean advanced) {
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(target);
		pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
		Hello proxiedHello = (Hello)pfBean.getObject();
		
		if(advanced) { //메소드 선정방식을 통해 어디바이스 적용.
			assertThat(proxiedHello.sayHello("Toby") , is("HELLO TOBY"));
			assertThat(proxiedHello.sayHi("Toby") , is("HI TOBY"));
		}else { //어드바이스 적용 대상 후보에서 탈락.
			assertThat(proxiedHello.sayHello("Toby"),is("Hello Toby"));
			assertThat(proxiedHello.sayHi("Toby"),is("Hi Toby"));
			assertThat(proxiedHello.sayThankYou("Toby"),is("Thank You Toby"));
			
		}
	}
	
	/*
	 *     ※ Advice - MethodInterceptor처럼 target object에 종속되지 않는 순수한 부가기능을 담은 오브젝트(UppercaseAdvice)를 스프링에서 
	 *              어드바이스라고 함.
	 *      - MethodInterceptor 에 method정보와 타켓오브젝트가 담긴 MethodInvocation Object가 함께 전달되므로..
	*/
	static class UppercaseAdvice implements MethodInterceptor{
		
		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			
			/* 리플렉션의 Method와 달리 메소드 실행시 타켓 오브젝트를 전달할 필요가 없다.
			 * JDK의 dynamic proxy의 부가기능을 제공하는 InvocationHandler을 구현한 UppsercaseHandler에도 타켓이 존재하지만,
			   MethodInvocation은 메소드 정보와 함께 타켓 오브젝트를 알고 있기 때문에 타켓정보가 없다.
			 */
			String ret = (String)invocation.proceed(); //target method 호출.
			return ret.toUpperCase();
		}
	}//end UppercaseAdvice
	
	
}//end class
