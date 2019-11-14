package springbook.user.dao.junit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.dao.UserDao;
import springbook.user.domain.User; 

/**
 *    @Test 조건 
 *      1) 접근제어자는 public 
 *      2) return 값 void
 *      3) parameter가 없는.
 * */

//스프링 테스트 컨텍스트 프레임워크의 JUnit 확장기능 지정.
@RunWith(SpringJUnit4ClassRunner.class)
//테스트 컨텍스트가 자동으로 만들어줄 application context 위치 지정
@ContextConfiguration(locations="/applicationContext.xml")
public class UserDaoConnectionTestXMLJunit {
	
	//픽스처 - 테스트를 수행하는 필요한 정보나 오브젝트.
	private User user1;
	private User user2;
	private User user3;
	
	@Autowired 
	private ApplicationContext context;
	
	@Autowired
	private UserDao dao;
	
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
		this.user1 = new User("whiteship","tom","married"); //픽스처
		this.user2 = new User("whiteship2","tom2","married2"); 
		this.user3 = new User("whiteship3","tom3","married3");
	}
	
	@Test
	public void addAndGet() throws SQLException, ClassNotFoundException{ //Jnit 테스트위해 접근제어자를 public으로..)
		
		dao.deleteAll();
		assertThat(dao.getCount(),is(0));
		
		dao.add(user1);
		assertThat(dao.getCount(),is(1));
		
		dao.add(user2);
		assertThat(dao.getCount(),is(2));		
		
		dao.add(user3);
		assertThat(dao.getCount(),is(3));	
	}
	
	@Test 
	public void addAndGetExpand() throws ClassNotFoundException, SQLException {
		
		dao.deleteAll();
		assertThat(dao.getCount(),is(0));
		
		dao.add(user1);
		dao.add(user2);
		assertThat(dao.getCount(),is(2));
		
		User userget1 = dao.get(user1.getId());
		assertThat(userget1.getName(),is(user1.getName()));
		assertThat(userget1.getPassword(),is(user1.getPassword()));
		
		User userget2 = dao.get(user2.getId());
		assertThat(userget2.getName(),is(user2.getName()));
		assertThat(userget2.getPassword(),is(user2.getPassword()));		
	}
	
	//getId()가 없을때 예외처리 - 스프링에 정의된 Data Access Exception Class를 이용.
	@Test(expected=EmptyResultDataAccessException.class)   //테스트중에 발생할 것으로 기대하는 예외 클래스를 지정
	public void getUserFailure() throws SQLException, ClassNotFoundException{
		dao.deleteAll();
		assertThat(dao.getCount(),is(0));
		dao.get("unknown_id"); //강제 예외 발생
	}
	
	
	
}