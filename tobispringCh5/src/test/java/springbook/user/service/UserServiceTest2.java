package springbook.user.service;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.is;
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
 *  LEVEL이 정상적으로 upgrade되는지 확인하기 위한 테스트 
*/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class UserServiceTest2 {

	//컨테이너가 관리하는 스프링 빈 선언
	@Autowired 
	UserService userSerivce;
	
	@Autowired 
	private ApplicationContext context;
	
	@Autowired
	private UserDao userDao;
	
	
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;
	
	//픽스쳐 선언 
	List<User> users;

	@Before
	public void setUp() {
		//배열을 리스트로 만들어주는 메소드 배열.
		users = Arrays.asList(
			new User ("bumjin", "박범진", "p1", UserLevel.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0),
			new User ("joytouch", "강명성", "p2", UserLevel.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
			new User ("erwins", "신승한", "p1", UserLevel.SILVER, 60, MIN_RECOMMEND_FOR_GOLD-1),
			new User ("madnite1", "박범진", "p1", UserLevel.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
			new User ("green", "박범진", "p1", UserLevel.GOLD, 100, Integer.MAX_VALUE)
		);
	}
	
	@Test
	public void upgradeLevels() {
		
		userDao.deleteAll();
		
		for(User user:users) { userDao.add(user); };
		userSerivce.upgradeLevels();
		
		//업그레이드후의 예상 레벨을 검증한다.
		checkLevelUpgrade(users.get(0), false);
		checkLevelUpgrade(users.get(1), true);
		checkLevelUpgrade(users.get(2), false);
		checkLevelUpgrade(users.get(3), true);
		checkLevelUpgrade(users.get(4), false);
		
	}

	//upgraded - 어떤 레벨로 바뀔 것인가가 아니라, 다음 레벨로 업그레이드 될것인가 아닌가를 지정.
	private void checkLevelUpgrade(User user, boolean upgraded) {
		User userUpdate = userDao.get(user.getId());
		
		if(upgraded) { //업데이트 일어났는지 확인
			assertThat(userUpdate.getUserLevel(),is(user.getUserLevel().nextLevel()));
		}else { //업데이트가 일어나지 않았는지 확인
			assertThat(userUpdate.getUserLevel(),is(user.getUserLevel()));
		}
	}
	

	
	
}
