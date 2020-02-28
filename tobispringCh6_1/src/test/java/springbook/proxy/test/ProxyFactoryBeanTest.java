package springbook.proxy.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.proxyTest.Hello;
import springbook.user.proxyTest.HelloTarget;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class ProxyFactoryBeanTest {
	
	@Test
	public void proxyFactoryBean() {
		
		ProxyFactoryBean pfBean = new ProxyFactoryBean(); //proxyFactoryBean
		pfBean.setTarget(new HelloTarget()); //타켓설정
		pfBean.addAdvice(new UppercaseAdvice()); //부가기능을 담은 advice추가
		
		//FactoryBean이므로, getObject()로 생성된 프록시를 가져온다.
		//Hello interface type의 proxy
		Hello proxiedHello = (Hello)pfBean.getObject();
		
		assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
		assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY"));
		assertThat(proxiedHello.sayThankYou("Toby"), is("THANK YOU TOBY"));
	}
	
	/*
	 *     ※ Advice - MethodInterceptor처럼 target object에 종속되지 않는 순수한 부가기능을 담은 오브젝트(UppercaseAdvice)를 스프링에서 어드바이스라고 함.
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
	
	/** pointCut을 적용한 ProxyFactoryBean */
	@Test
	public void pointcutAdvisor() {
		
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(new HelloTarget());
		
		//메소드이름을 비교해서 대상을 선정해서 알고리즘을 제공하는 포인트컷
		NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
		//이름비교조건 설정(sayH로 시작하는 모든 이름)
		pointcut.setMappedName("sayH*");
		
		/*  
		 * 포인트컷과 Advice를 Advisor로 묶어서 추가
		 *  - ProxyFactoryBean에 여러개의 어드바이스와 포인컷이 추가될수 있기 때문에 하나로 묶어서 나간다. 
		 *  Advisor : Advice와 pointcut을 묶은 오브젝트 */
		pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut,new UppercaseAdvice()));
		
		Hello proxiedHello = (Hello)pfBean.getObject();
		
		assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
		assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY"));
		assertThat(proxiedHello.sayThankYou("Toby"), is("Thank You Toby")); //pointcut 조건에 안맞음.
		
	}
	
}
