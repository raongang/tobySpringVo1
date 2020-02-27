package springbook.user.proxyTest;

/**
 *   Hello Interface를 구현한 proxy
 *   부가기능 추가를 위한 데코레이터 패턴을 적용.
 *   
 *   단점
 *    - 인터페이스의 모든 메소드 구현
 *    - 부가 기능이 모든 메소드에 중복. 
 *    
 *   해결방안
 *    - reflect를 이용한 dynamicProxy를 구성( 해당 패키지 참고 ) 
 * */
public class HelloUppercase implements Hello{

	//위임할 타켓 오브젝트. 여기서는 타켓클래스의 오브젝트 인것은 알지만 다른 프록시를 추가할수 있으므로 인터페이스로 접근.
	Hello hello;
	
	public HelloUppercase(Hello hello) {
		super();
		this.hello = hello;
	}

	@Override
	public String sayHello(String name) {
		// TODO Auto-generated method stub
		return hello.sayHello(name).toUpperCase(); //위임과 부가기능
	}

	@Override
	public String sayHi(String name) {
		// TODO Auto-generated method stub
		return hello.sayHello(name).toUpperCase();
	}

	@Override
	public String sayThankYou(String name) {
		// TODO Auto-generated method stub
		return hello.sayThankYou(name).toUpperCase();
	}
	
	

}
