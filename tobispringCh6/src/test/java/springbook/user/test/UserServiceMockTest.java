package springbook.user.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
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
 *  LEVEL이 정상적으로 upgrade되는지 확인하기 위한 테스트 
 *  고립된 단위테스트를 이용하는 예제. ( stub과 MockObject 이용 ) 
*/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class UserServiceMockTest {

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
	public void upgradeLevels() throws Exception {
		
		//고립된 테스트에서는 테스트 대상 오브젝트를 직접 생성하면 됨.
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		
		//목 오브젝트로 만든 UserDao를 직접 DI
		MockUserDao mockUserDao = new MockUserDao(this.users);
		userServiceImpl.setUserDao(mockUserDao);
		
		userServiceImpl.upgradeLevels();
		
		//MockUserDao로부터 업데이트 결과를 가져온다.
		List<User> updated = mockUserDao.getUpdated();
		
		//업데이트한 횟수와 정보를 확인한다.
		assertThat(updated.size(),is(2));
		checkLevelUpgrade(users.get(1), "joytouch",UserLevel.SILVER);
		checkLevelUpgrade(users.get(3), "madnite1",UserLevel.GOLD);
	}

	private void checkLevelUpgrade(User updated,String expectedId, UserLevel expectedLevel) {
		assertThat(updated.getId(),is(expectedId));
		assertThat(updated.getUserLevel(),is(expectedLevel));
	}
	
	
	//userDao 목오브젝트
	static class MockUserDao implements UserDao{
		private List<User> users; // 레벨 Upgrad 후보 User 오브젝트 목록
		private List<User>	updated = new ArrayList<User>(); //업그레이드 대상 오브젝트를 저장해 둘 목록
		
		private MockUserDao(List<User> users) {
			this.users = users;
		}
		
		public List<User> getUpdated(){
			return this.updated;
		}
		


		public List<User> getAll(){
			return this.users;
		}
		

		public void update(User user) {
			updated.add(user);
		}

		//사용하지 않는 메소드.
		@Override
		public void add(User user) { throw new UnsupportedOperationException(); }
		@Override
		public User get(String id) { throw new UnsupportedOperationException(); }
		@Override
		public void deleteAll() { throw new UnsupportedOperationException(); }
		@Override
		public int getCount() { throw new UnsupportedOperationException(); }
	}	
	
}
