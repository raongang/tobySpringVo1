package springbook.user.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.dao.UserDao;
import springbook.user.domain.User;
import springbook.user.domain.User.UserLevel;
import springbook.user.service.UserServiceImpl;

/**
 *  고립된 단위테스트 (Mokito Framework 이용)
 *    1. 장점 
 *      - Mock클래스를 일일이 준비할 필요가 없다. 
 *      - 간단히 메소드 호출만으로 특정인터페이스를 구현한 테스트용 목오브젝트 생성가능
 *             
*/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class UserServiceMokitoTest {

	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;
	
	//픽스쳐 선언 
	List<User> users;

	@Before
	public void setUp() {
		//배열을 리스트로 만들어주는 메소드 배열.
		users = Arrays.asList(
			new User ("bumjin", "박범진", "p1", UserLevel.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0,"sayllclubs.naver.com"),
			new User ("joytouch", "강명성", "p2", UserLevel.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0,"sayllclubs.naver.com"),
			new User ("erwins", "신승한", "p1", UserLevel.SILVER, 60, MIN_RECOMMEND_FOR_GOLD-1,"sayllclubs.naver.com"),
			new User ("madnite1", "박범진", "p1", UserLevel.SILVER, 60, MIN_RECOMMEND_FOR_GOLD,"sayllclubs.naver.com"),
			new User ("green", "박범진", "p1", UserLevel.GOLD, 100, Integer.MAX_VALUE, "sayllclubs.naver.com")
		);
	}
	
	@Test
	public void mockitoUpgradeLevels() throws Exception{
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		
		//UserDao 인터페이스를 구현한 테스트용 목오브젝트 생성. import org.mockito.Matchers
		UserDao mockUserDao = mock(UserDao.class);
		when(mockUserDao.getAll()).thenReturn(this.users);
		userServiceImpl.setUserDao(mockUserDao); //테스트 대상에 DI
		
		userServiceImpl.upgradeLevels();
		
		//update() 메소드가 2번호출되었는지 체크하라. any() 는 파라미터 내용무시하고 호출횟수만 체크.
		verify(mockUserDao,times(2)).update(any(User.class));
		verify(mockUserDao,times(2)).update(any(User.class));
		
		//users.get(1)파라미터로 updage()가 호출된적이 있는지 확인.
		verify(mockUserDao).update(users.get(1));
		assertThat(users.get(1).getUserLevel(),is(UserLevel.SILVER));
		
		verify(mockUserDao).update(users.get(3));
		assertThat(users.get(3).getUserLevel(),is(UserLevel.GOLD));
		
	}
	
	
	
}
