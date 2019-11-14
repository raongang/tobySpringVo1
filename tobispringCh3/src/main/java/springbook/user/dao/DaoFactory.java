package springbook.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

//object factory

@Configuration //application context or bean factory가 사용할 설정정보라는 표시
public class DaoFactory {
	@Bean
	public UserDao userDao() {
		//1. UserDao 생성
		//2. 사용할 ConnectonMaker 타입의 오브젝트 제공 , 결국 두 오브젝트 사이의 의존관계 설정 효과
		
		// 생성자 DI호출시
		//return new UserDao(connectionMaker());
		
		//수정자 DI( setter호출시 ) 
		UserDao userDao = new UserDao();
		userDao.setConnectionMaker(connectionMaker());
		return userDao;
	}
	
	//팩토리의 메소드는 UserDao 타입의 오브젝트를 어떻게 만들고, 어떻게 준비시킬지를 결정한다.
	//UserDao가 사용할 ConnectionMaker 구현 클래스를 결정하고 오브젝트를 생성.

	@Bean
	public ConnectionMaker connectionMaker() {
		return new DConnectionMaker(); 
	}
	
	
	
	

}
