package springbook.user.factoryBeanTest;


//FactoryBean Test
//생성자를 제공하지 않는 클래스.
public class Message {
	String text;
	
	//생성자 통한 외부 접근불가.  스프링 DI를 할때 기본생성자를 이용하는데 private라 직접사용금지.
	// <bean id="Message" class="springbook.user.factoryBeanTest.Message"> 
	private Message(String text) {
		this.text = text; 
	}
	
	public String getText() {
		return text;
	}
	
	//생성자 대신 사용할 수 있는 static factory 메소드를 제공. 
	public static Message newMessage(String text) {
		return new Message(text);
	}
}
