package springbook.user.factoryBeanTest;

import org.springframework.beans.factory.FactoryBean;

//Message 클래스의 오브젝트를 생성해주는 팩토리 빈 클래스.
public class MessageFactoryBean implements FactoryBean<Message>{

	String text;

	/* 오브젝트를 생성할때 필요한 정보를 팩토리 빈의 프로퍼티로 설정해서 대신 DI받을 수 있게 해준다.
	 * 주입된 정보는 오브젝트 생성중에 사용된다. */
	public void setText(String text) {
		this.text = text;
	}

	/* 실제 빈으로 사용될 오브젝트를 직접 생성. */
	@Override
	public Message getObject() throws Exception {
		return Message.newMessage(this.text);
	}

	@Override
	public Class<?> getObjectType() {
		// TODO Auto-generated method stub
		return Message.class;
	}

	/* getObject()메소드가 돌려주는 오브젝트가 싱글톤인지 알려준다.
	 *  이 팩토리빈은 매번 요청할때마다 새로운 오브젝트를 만들기 때문에 false로 설정.
	 *  이것은 팩토리 빈의 동작방식에 대한 설정이고 만들어진 빈 오브젝트는 싱글톤으로 스프링이 관리해 줄 수 있다. 
	 * */
	@Override
	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
	
}
