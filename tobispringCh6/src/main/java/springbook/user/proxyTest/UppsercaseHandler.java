package springbook.user.proxyTest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/** dynamic handler */
public class UppsercaseHandler implements InvocationHandler{
	Hello target;
	
	public UppsercaseHandler(Hello target) {
		super();
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		String ret = (String)method.invoke(target, args);
		return ret.toUpperCase();
	}
	
	

}
