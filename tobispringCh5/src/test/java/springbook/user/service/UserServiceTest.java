package springbook.user.service;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class UserServiceTest {

	//컨테이너가 관리하는 스프링 빈 선언
	@Autowired 
	UserService userSerivce;
	
	//빈 등록여부 테스트
	@Test
	public void bean() {
		assertThat(this.userSerivce,is(notNullValue()));
	}
	
	
	
	
}
