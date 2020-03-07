package springbook.user.proxyTest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/** dynamic handler */
//advice
public class UppsercaseHandler implements InvocationHandler{
	Object target;
	
	public UppsercaseHandler(Object target) {
		super();
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		Object ret = method.invoke(target, args);
		
		//메소드를 선별해서 부가기능 실시.
		if(ret instanceof String && method.getName().startsWith("say")) {
			return ((String)ret).toUpperCase();
		}
		else {
			return ret;
		}
	}
}
