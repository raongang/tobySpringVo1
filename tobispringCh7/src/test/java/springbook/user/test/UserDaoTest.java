package springbook.user.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.dao.UserDao;
import springbook.user.domain.User;
import springbook.user.domain.User.UserLevel;
import springbook.user.service.UserService;


@RunWith(SpringJUnit4ClassRunner.class)
//application context 생성과 동시에 XML파일을 읽어오고 초기화까지 수행한다. -> 이때 만들어지는 application Context가 GenericApplicationContext
@ContextConfiguration(locations="/applicationContext.xml")
public class UserDaoTest {

	//컨테이너가 관리하는 스프링 빈 선언
	//타입으로 검색, 같은 타입의 빈이 두개라면 필드 이름을 이용해서 찾음. 
	@Autowired
	private UserDao userDao;
	
	@Autowired 
	UserService userService;
	
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;
	
	//픽스쳐 선언 
	List<User> users;
	
	@Before
	public void setUp() {
		//배열을 리스트로 만들어주는 메소드 배열.
		users = Arrays.asList(
			new User ("bumjin", "박범진1", "p1", UserLevel.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0, "sayllclubs.naver.com"),
			new User ("joytouch", "강명성", "p2", UserLevel.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0,"sayllclubs.naver.com"),
			new User ("erwins", "신승한", "p1", UserLevel.SILVER, 60, MIN_RECOMMEND_FOR_GOLD-1,"sayllclubs.naver.com"),
			new User ("madnite1", "박범진2", "p1", UserLevel.SILVER, 60, MIN_RECOMMEND_FOR_GOLD,"sayllclubs.naver.com"),
			new User ("green", "박범진3", "p1", UserLevel.GOLD, 100, Integer.MAX_VALUE,"sayllclubs.naver.com")
		);
	}//end setup


	
	@Test
	public void addAndGet() throws SQLException, ClassNotFoundException{ //Jnit 테스트위해 접근제어자를 public으로..)
		userDao.deleteAll();
		assertThat(userDao.getCount(),is(0));
		
		userDao.add(users.get(0));
		assertThat(userDao.getCount(),is(1));
	
		userDao.add(users.get(1));
		assertThat(userDao.getCount(),is(2));		
		
		userDao.add(users.get(2));
		assertThat(userDao.getCount(),is(3));
	}
	
	
	@Test 
	public void addAndGetExpand() throws ClassNotFoundException, SQLException {
		
		userDao.deleteAll();
		assertThat(userDao.getCount(),is(0));
		
		userDao.add(users.get(0));
		userDao.add(users.get(1));
		assertThat(userDao.getCount(),is(2));
		
		User userget1 = userDao.get(users.get(0).getId());
		assertThat(userget1.getName(),is(users.get(0).getName()));
		assertThat(userget1.getPassword(),is(users.get(0).getPassword()));
		
		User userget2 = userDao.get(users.get(1).getId());
		assertThat(userget2.getName(),is(users.get(1).getName()));
		assertThat(userget2.getPassword(),is(users.get(1).getPassword()));		
	}	
	
	//getId()가 없을때 예외처리 - 스프링에 정의된 Data Access Exception Class를 이용.
	@Test(expected=EmptyResultDataAccessException.class)   //테스트중에 발생할 것으로 기대하는 예외 클래스를 지정
	public void getUserFailure() throws SQLException, ClassNotFoundException{
		userDao.deleteAll();
		System.out.println("userDao.getCount() >> " + userDao.getCount());
		assertThat(userDao.getCount(),is(0));
		userDao.get("unknown_id"); //강제 예외 발생
	}
	
	
}//end UserDaoTest





