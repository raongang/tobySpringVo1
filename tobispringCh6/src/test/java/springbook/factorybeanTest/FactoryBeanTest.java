
package springbook.factorybeanTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.factoryBeanTest.Message;




@RunWith(SpringJUnit4ClassRunner.class)
//설정파일 이름을 지정하지 않으면 클래스이름+"-context.xml"이 default로 사용된다.  이거 안되네?
@ContextConfiguration(locations="/applicationContext.xml")

public class FactoryBeanTest {
 
	@Autowired
	ApplicationContext context;
	
	@Test
	public void getMessageFromFactoryBean() {
		Object message = context.getBean("message");
		assertThat(message,is(Message.class)); //type확인
		
		System.out.println("Text : " + ((Message)message).getText());
		assertThat(((Message)message).getText(),is("Factory Bean"));
	}
	
}





