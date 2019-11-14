package springbook.user.dao.test;

import java.sql.SQLException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import springbook.user.dao.CountingConnectionMaker;
import springbook.user.dao.CountingDaoFactory;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

/**
 *  의존관계 주입의 응용 
 *    - db counting 부가기능.
 * @author admin
 *
 */

public class UserDaoConnectionCountingTest {
	public static void main(String[] args) throws ClassNotFoundException,SQLException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);
		
		UserDao dao = context.getBean("userDao",UserDao.class);
		
		User user = new User();
		user.setId("whiteship2");
		user.setName("tom2");
		user.setPassword("married2");
		dao.add(user);
		System.out.println(user.getId() + "등록 성공");

		/** DL(의존관계검색)을 통해 어떤 빈이든 가져올 수 있다. */
		CountingConnectionMaker ccm = context.getBean("connectionMaker",CountingConnectionMaker.class);
		System.out.println("Connection counter >> " + ccm.getCounter());
		
		
	}
}
