package springbook.user.service;

import java.lang.reflect.Proxy;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

/**

 * FatoryBean
   - dynamic proxy, TransactionHandler 를 스프링 di 할 방법이 없음.
   - 스프링은 클래스 정보를 가지고 디폴트 생성자를 통해 오브젝트를 만드는 방법 외에도 빈을 만들수 있는 방법이 있음.
   - 스프링을 대신해서 오브젝트의 생성로직을 담당하도록 만들어진 특별한 빈.
   - FactoryBean 인터페이스를 구현하고 이를 스프링 di로 등록하면 Factory Bean으로 동작.
*/ 

public class TxProxyFactoryBean implements FactoryBean<Object>{
	
	//TransactionHandler 생성시 필요.
	private Object target; //부가기능을 제공할 타켓 오브젝트 
	private PlatformTransactionManager transactionManager; //트랜잭션 기능을 제공하는데 필요한 트랜잭션 매니저
	private String pattern; //트랜잭션을 적용할 메소드 이름 패턴
	
	//다이나믹 프록시를 생성할때마다 필요함.
	Class<?> serviceInterface; 
	
	//setter , getter 
	public void setTarget(Object target) {
		this.target = target;
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	//factoryBean 인터페이스 구현 메소드.
	@Override
	public Object getObject() throws Exception {
		// TODO Auto-generated method stub
		//di 받은 정보를 이용해서 TransactionHandler를 사용하는 다이나믹 프록시를 생성.
		TransactionHandler txHandler = new TransactionHandler();
		txHandler.setTarget(target);
		txHandler.setTransactionManager(transactionManager);
		txHandler.setPattern("upgradeLevels");
		
		return Proxy.newProxyInstance(
				getClass().getClassLoader(),
				new Class[] { serviceInterface } ,
				txHandler);
	}

	@Override
	public Class<?> getObjectType() {
		// TODO Auto-generated method stub
		return serviceInterface;
	}

	@Override
	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
}
