package springbook.user.dao.junit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.dao.UserDao;
import springbook.user.domain.User;
import springbook.user.domain.User.UserLevel; 

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
public class UserDaoChapter5 {
	
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
		this.user1 = new User("abc","tom","married", UserLevel.BASIC,1,0,"sayllclubs@naver.com"); //픽스처
		this.user2 = new User("bcd","tom2","married2",UserLevel.SILVER,55,10,"sayllclubs@naver.com"); 
		this.user3 = new User("cde","tom3","married3",UserLevel.GOLD,100,40,"sayllclubs@naver.com");
		
		
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
		dao.add(user3);
		assertThat(dao.getCount(),is(3));
		
		User userget1 = dao.get(user1.getId());
		checkSumUser(userget1,user1);
		
		User userget2 = dao.get(user2.getId());
		checkSumUser(userget2,user2);
	}
	
	@SuppressWarnings("unused")
	private void checkSumUser(User user1, User user2) {
		
		assertThat(user1.getId(),is(user2.getId()));
		assertThat(user1.getName(),is(user2.getName()));
		assertThat(user1.getPassword(),is(user2.getPassword()));
		
		assertThat(user1.getLogin(),is(user2.getLogin()));
		assertThat(user1.getRecommend(),is(user2.getRecommend()));
	}
	
	/* 간단한 수정 기능 테스트 */
	@Test
	public void update() {
		dao.deleteAll();
		
		dao.add(user1);
		dao.add(user2);
		
		user1.setName("TEST");
		user1.setPassword("spring3");
		user1.setUserLevel(UserLevel.GOLD);
		user1.setLogin(1000);
		user1.setRecommend(999);
		
		dao.update(user1);
		
		User user1update = dao.get(user1.getId());
		checkSumUser(user1, user1update);
		
		User user2same = dao.get(user2.getId());
		checkSumUser(user2, user2same);
		
	}//end update

}