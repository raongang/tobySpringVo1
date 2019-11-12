package springbook.user.dao.test;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import springbook.user.dao.UserDao;
import springbook.user.domain.User;

/**
 * 
 *  테스트 방법
 *    1. UserDaoConnectionCountingTest.java 삭제
 *    2. CountingConnectionMaker.java 삭제
 *    3. CountingDaoFactory.java 삭제
 *   
 * @author admin
 *
 */
public class UserDaoConnectionTestXML {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {

		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		
		UserDao dao = context.getBean("userDao",UserDao.class);
		
		User user = new User();
		user.setId("whiteship3");
		user.setName("tom3");
		user.setPassword("married3");
		dao.add(user);
		System.out.println(user.getId() + "등록 성공");
		
	}
}


