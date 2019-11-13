package springbook.user.dao.junit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import springbook.user.dao.UserDao;
import springbook.user.domain.User;

/**
 *    @Test 조건 
 *      1) 접근제어자는 public 
 *      2) return 값 void
 *      3) parameter가 없는.
 *       
 * */
public class UserDaoConnectionTestXMLJunit {
	
	@Test
	public void addAndGet() throws SQLException, ClassNotFoundException{ //Jnit 테스트위해 접근제어자를 public으로..)
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		UserDao dao = context.getBean("userDao",UserDao.class);
		
		User user = new User("whiteship","tom","married");
		User user2 = new User("whiteship2","tom2","married2");
		User user3 = new User("whiteship3","tom3","married3");
		
		dao.deleteAll();
		assertThat(dao.getCount(),is(0));
		
		dao.add(user);
		assertThat(dao.getCount(),is(1));
		
		dao.add(user2);
		assertThat(dao.getCount(),is(2));		
		
		dao.add(user3);
		assertThat(dao.getCount(),is(3));	
	}
	
	@Test 
	public void addAndGetExpand() throws ClassNotFoundException, SQLException {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		UserDao dao = context.getBean("userDao",UserDao.class);
		
		User user1 = new User("whiteship","tom","married");
		User user2 = new User("whiteship2","tom2","married2");
		
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
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		
		UserDao dao = context.getBean("userDao", UserDao.class);
		dao.deleteAll();
		assertThat(dao.getCount(),is(0));
		
		dao.get("unknown_id"); //강제 예외 발생
		
		
	}
}


