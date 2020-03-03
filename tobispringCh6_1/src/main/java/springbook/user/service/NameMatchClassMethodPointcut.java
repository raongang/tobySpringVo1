package springbook.user.service;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.util.PatternMatchUtils;

//classFilter를 적용한 Pointcut ( 관련 Test예제 - DynamicProxyTest )
public class NameMatchClassMethodPointcut extends NameMatchMethodPointcut{
	
	public void setMappedClassName(String mappedClassName) {
		
		//mappedClassName - 모든 class를 다 허용하던 default class filter를 프로퍼티로 받은 클래스 이름을 
		//이용해서 필터를 만들어 덮어씌운다.
		this.setClassFilter(new SimpleClassFilter(mappedClassName));
	}
	static class SimpleClassFilter implements ClassFilter{
		String mappedName;
		
		private SimpleClassFilter(String mappedName) {
			this.mappedName =  mappedName;
		}
		
		
		public boolean matches(Class<?> clazz) {
			// TODO Auto-generated method stub
			//와일드카드(*)가 들어간 문자열 비교를 지원하는 스프링의 유틸리티 메소드.
			// *name, name*, *name* 모두 지원.
			return PatternMatchUtils.simpleMatch(mappedName,clazz.getSimpleName());
		}
	}//end simpleClassFilter
}
