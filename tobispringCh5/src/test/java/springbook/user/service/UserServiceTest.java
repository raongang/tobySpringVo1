package springbook.user.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

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
 *   데이터 update중에 에러가 발생했을때, 이전 데이터는 roll-back이 되는지, 그대로 commit이 되는지 
 *   확인하기 위한 테스트 
 * 
*/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class UserServiceTest {

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
	public void upgradeAllOrNothing() {
		
		//예외를 발생시킬 사용자의 id를 넣어서 테스트용 UserService 대역 오브젝트를 생성한다.
		UserService testUserSerivce = new TestUserService(users.get(3).getId());
		testUserSerivce.setUserDao(this.userDao);//useDao를 수동 DI
		
		userDao.deleteAll();
		
		for(User user : users) userDao.add(user);
		
		try {
			//TestUserSerivce는 업그레이드 작업중에 예외가 발생해야 함. 정상 종료라면 문제 있으니 실패.
			testUserSerivce.upgradeLevels();
			fail("TestUserSerivceException expected"); //JUnit 테스트 결과를 무조건 fail로 함 ( ex. java.lang.AssertionError:message )
		}catch(TestUserServiceException e) {
			System.out.println("예외발생");
			//TestUserSerivce가 던져주는 예외를 잡아서 계속 진행되게 한다. 그외는 실패처리 
		}
		//예외가 발생하기전에 레벨 변경이 있었던 사용자의 레벨이 처음 상태로 바뀌었나 체크함.
		checkLevelUpgrade(users.get(1),false);
	}
	
	
	//upgraded - 어떤 레벨로 바뀔 것인가가 아니라, 다음 레벨로 업그레이드 될것인가 아닌가를 지정.
	private void checkLevelUpgrade(User user, boolean upgraded) {
		System.out.println("checkLevelUpgrade");
		User userUpdate = userDao.get(user.getId());
		
		if(upgraded) { //업데이트 일어났는지 확인
			assertThat(userUpdate.getUserLevel(),is(user.getUserLevel().nextLevel()));
		}else { //업데이트가 일어나지 않았는지 확인
			assertThat(userUpdate.getUserLevel(),is(user.getUserLevel()));
		}
	}
	
	
	/**  테스트를 위한 UserService 대역 생성
	 *         ※  보통 오버라이딩을 하기 위해 class파일을 별도로 만들어서 상속하는데 테스트용이라면 다음과 같이 inner class중의 한
	 *     종류인 nested class를 이용하면 편함. 
	 *        
	 */
	static class TestUserService extends UserService{
		private String id;
		
		//예제를 발생시킬 User 오브젝트의 id를 지정할 수 있게 만든다.
		private TestUserService(String id) {
			this.id = id;
		}
		
		//UserService의 메소드 오버라이딩
		@Override
		protected void upgradeLevel(User user) { 
			//지정된 id의 User 오브젝트가 발견되면 예외를 던져서 강제로 작업을 중지시킨다.
			
			System.out.println("this.id >> " + this.id);
			System.out.println("user.getId() : " + user.getId());
			
			if(user.getId().equals(this.id)) throw new TestUserServiceException();
			
			super.upgradeLevel(user);
		}
		
		public static void printTest() {
			System.out.println("HERE IS CALL");
		}
	}
	
	static class TestUserServiceException extends RuntimeException{ }
	
	
}





