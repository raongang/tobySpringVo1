package springbook.user.dao.junit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.dao.UserDao;
import springbook.user.domain.User; 


//스프링 테스트 컨텍스트 프레임워크의 JUnit 확장기능 지정.
@RunWith(SpringJUnit4ClassRunner.class)
//테스트 컨텍스트가 자동으로 만들어줄 application context 위치 지정
@ContextConfiguration(locations="/applicationContext.xml")
public class UserDaoTest {
	
	//픽스처 - 테스트를 수행하는 필요한 정보나 오브젝트.
	private User user1;
	private User user2;
	private User user3;
	
	@Autowired 
	private ApplicationContext context;
	
	@Autowired
	private UserDao dao;
	
	@Autowired
	private DataSource dataSource;
	
	//Junit이 제공하는 어노테이션 ( @Test가 실행되기전에 먼저 실행되어야 할 메소드를 정의한다 ) 
	@Before
	public void setUp() {
		
		
		/**
		 *   this.context 
		 *     - 값 모두 동일 
		 *     - 하나의 application context가 생성되어 모든 test method에 사용됨.
		 *   this
		 *     - 값이 모두 다름 
		 *     - JUnit은 테스트 메소드를 실행할때마다 새로운 테스트 오브젝트를 생성하기 때문.
		 */
		System.out.println("======================== this.context ======================== " + this.context); 
		System.out.println("======================== this ======================== " + this);
		
		//this.dao = this.context.getBean("userDao",UserDao.class);
		this.user1 = new User("abc","tom","married"); //픽스처
		
		this.user2 = new User("bcd","tom2","married2"); 
		this.user3 = new User("cde","tom3","married3");
	}
	
	/* 예외테스트 */
	@Test(expected=DataAccessException.class)
	public void duplicateKey() {
		dao.deleteAll();
		
		dao.add(user1);
		dao.add(user1); //강제로 같은 사용자 두번 입력 여기서 exception발생해야함.
	}
	
	/* 예외전환 테스트 
	 *   SQLException을 DataAccessException으로 전환하는 효율적인 방법
	 *     - DB에러코드 이용
	 *     ( SQLExceptionTranslator interface 
	 *         ㄴ SQLErrorCodeSQLExceptionTranslator ) 
	 * */
	
	@SuppressWarnings("deprecation")
	@Test
	public void sqlExceptionTranslate() {
		dao.deleteAll();
		
		try {
			dao.add(user1);
			dao.add(user1);
		}catch(DuplicateKeyException ex) {
			
			/** DuplicateKeyException은 중첩된 예외로, JDBC API에서 처음 발생한 SQLException을 가지고 내부에 가지고 있다. */ 
			SQLException sqlEx = (SQLException)ex.getRootCause();
			//코드를 잉요한 SQL Exception의 전환.
			SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource); 
			//assertThat(set.translate(null, null, sqlEx),is(DuplicateKeyException.class));
		}
		
	}
	
	
	
}