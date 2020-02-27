package springbook.user.dynmicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

//데코레이터 패턴이 적용된 porxy를 dynamic으로 바꾸기 위한 클래스
public class UppsercaseHandler implements InvocationHandler{

	Hello target;
	
	//프록시팩토리를 통해 생성된 다이나믹팩토리로부터 전달받은 요청을 다시 타켓오브젝트에 위임해야 하기 때문에
	//타켓 오브젝트를 주입받는다.
	public UppsercaseHandler(Hello target) {
		this.target = target;
	}
	
	//InvocationHandler는 invoke 메소드 하나만 가진다.
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{
		
		String ret = (String)method.invoke(target, args);//타켓으로 위임,인터페이스의 메소드 호출에 모두 적용됨.
		return ret.toUpperCase(); //부가기능제공
	}
	
}
