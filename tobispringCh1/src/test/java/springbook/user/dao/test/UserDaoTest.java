package springbook.user.dao.test;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import springbook.user.dao.DaoFactory;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

/**
 * @author admin
 *  - Spring IoC 예제.
 */
public class UserDaoTest {
	public static void main(String args[]) throws ClassNotFoundException, SQLException {
		
		/* 의존검색 관계를 이용하는 UserDao 생성자
		   1.7.3 의존관계 검색과 주입
				 1) 의존관계 검색
				   - 런타임시 의존관계를 맺을 오브젝트를 결정하는 것과 오브젝트의 생성 작업은 외부 컨테이너에게 IoC로 맡기지만, 
				     이를 가져올때는 메소드나 생성자를 통합 주입 대신 스스로 컨테이너에게 요청하는 방법을 사용한다.
 
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		this.connectionMaker = context.getBean("connectionMaker",ConnectionMaker.class);
		 */
		
		//application context를 적용한 예.
		//@Configuration read
		ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		
		//동일성,동등성 비교- 동일하게 나옴.
		UserDao dao3 = context.getBean("userDao",UserDao.class);
		UserDao dao4 = context.getBean("userDao",UserDao.class);
		
		System.out.println("dao3 >> " + dao3);
		System.out.println("dao4 >> " + dao4);
		System.out.println("dao3==dao4 >> " + (dao3==dao4));
		
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
		/** 1.7.3 의존관계 검색과 주입
		 *    1) 의존관계 검색
		 *      - 런타임시 의존관계를 맺을 오브젝트를 결정하는 것과 오브젝트의 생성 작업은 외부 컨테이너에게 IoC로 맡기지만, 
		 *        이를 가져올때는 메소드나 생성자를 통합 주입 대신 스스로 컨테이너에게 요청하는 방법을 사용한다.
		 *   
		 */System.out.println(user2.getPassword());
		
		System.out.println(user.getId() + " 조회 성공");
		
	}
}
