package springbook.user.dao.test;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import springbook.user.dao.DaoFactory;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

/**
 * 
 * @author admin
 *  - Spring IoC 예제.
 */
public class UserDaoTest {
	public static void main(String args[]) throws ClassNotFoundException, SQLException {
		//application context를 적용한 예.
		//@Configuration read
		ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		
		//동일성,동등성 비교- 동일하게 나옴.
		UserDao dao3 = context.getBean("userDao",UserDao.class);
		UserDao dao4 = context.getBean("userDao",UserDao.class);
		
		System.out.println("dao3 >> " + dao3);
		System.out.println("dao4 >> " + dao4);
		
		
		
		// getBean - application context가 관리하는 bean을 요청
		// "userDao" - application context에 등록된 bean의 이름
		// UserDao.class - return type.
		UserDao dao = context.getBean("userDao",UserDao.class);
		
		User user = new User();
		user.setId("whiteship");
		user.setName("tom");
		user.setPassword("married");
		dao.add(user);
		System.out.println(user.getId() + "등록 성공");
		
		User user2 = dao.get(user.getId());
		System.out.println(user2.getName());
		System.out.println(user2.getPassword());
		
		System.out.println(user.getId() + " 조회 성공");
		
	}
}
