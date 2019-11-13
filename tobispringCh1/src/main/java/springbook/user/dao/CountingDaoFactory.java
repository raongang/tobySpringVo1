package springbook.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CountingDaoFactory {

	/**/
	@Bean 
	public UserDao userDao() {
//		return new UserDao(connectionMaker());
		
		//수정자 DI( setter호출시 ) 
		UserDao userDao = new UserDao();
		userDao.setConnectionMaker(connectionMaker());
		return userDao;
		
	}

	@Bean
	public ConnectionMaker connectionMaker() {
		// TODO Auto-generated method stub
		return new CountingConnectionMaker(realConnectionMaker());
	}

	@Bean
	public ConnectionMaker realConnectionMaker() {
		// TODO Auto-generated method stub
		return new DConnectionMaker();
	}
	
	
}
