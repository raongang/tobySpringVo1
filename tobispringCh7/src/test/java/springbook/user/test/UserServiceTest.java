package springbook.user.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import springbook.user.dao.UserDao;
import springbook.user.domain.User;
import springbook.user.domain.User.UserLevel;
import springbook.user.service.UserService;
import springbook.user.service.UserServiceImpl;

/**
 *   데이터 update중에 에러가 발생했을때, 이전 데이터는 roll-back이 되는지, 그대로 commit이 되는지 
 *   확인하기 위한 테스트 
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class UserServiceTest {

	//컨테이너가 관리하는 스프링 빈 선언
	//타입으로 검색, 같은 타입의 빈이 두개라면 필드 이름을 이용해서 찾음. 
	@Autowired
	private UserDao userDao;

	/* 같은 타입의 빈이 2개이므로, 필드이름을 기준으로 주입될 빈이 결정된다. 자동 프록시 생성기에 의해 트랜잭션 부가기능이 testUserService 빈에 적용되었는지를 확인하기 위함.*/
	@Autowired
	UserService testUserService;
	
	//테스트를 위해 명시적 트랜잭션 선언
	@Autowired
	PlatformTransactionManager transactionManager;
	@Autowired 
	UserService userService;
	
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;
	
	//픽스쳐 선언 
	List<User> users;
	
	@Before
	public void setUp() {
		//배열을 리스트로 만들어주는 메소드 배열.
		users = Arrays.asList(
			new User ("bumjin", "박범진", "p1", UserLevel.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0, "sayllclubs.naver.com"),
			new User ("joytouch", "강명성", "p2", UserLevel.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0,"sayllclubs.naver.com"),
			new User ("erwins", "신승한", "p1", UserLevel.SILVER, 60, MIN_RECOMMEND_FOR_GOLD-1,"sayllclubs.naver.com"),
			new User ("madnite1", "박범진", "p1", UserLevel.SILVER, 60, MIN_RECOMMEND_FOR_GOLD,"sayllclubs.naver.com"),
			new User ("green", "박범진", "p1", UserLevel.GOLD, 100, Integer.MAX_VALUE,"sayllclubs.naver.com")
		);
	}//end setup
	
	@Test
	public void upgradeAllOrNothing() throws Exception {
		userDao.deleteAll();
		
		for(User user : users)  userDao.add(user); 
		
		try {
			//TestUserSerivce는 업그레이드 작업중에 예외가 발생해야 함. 정상 종료라면 문제 있으니 실패.
			this.testUserService.upgradeLevels();
			fail("TestUserSerivceException expected"); //JUnit 테스트 결과를 무조건 fail로 함 ( ex. java.lang.AssertionError:message )
		}catch(TestUserServiceException e) {
			//TestUserSerivce가 던져주는 예외를 잡아서 계속 진행되게 한다. 그외는 실패처리 
		}
		//예외가 발생하기전에 레벨 변경이 있었던 사용자의 레벨이 처음 상태로 바뀌었나 체크함.
		checkLevelUpgrade(users.get(1),false);
		
		
	}//end test upgradeAllOrNothing
	
	static class TestUserServiceException extends RuntimeException{ }
	
	/**The read-only behaviour is strictly driver specific. Oracle driver ignores this flag entirely */
	//@Test(expected=TransientDataAccessResourceException.class)
	public void readOnlyTransactionAttribute() {
		testUserService.getAll();
	}
	
	/**  테스트를 위한 UserService 대역 생성 
	 *         ※  보통 오버라이딩을 하기 위해 class파일을 별도로 만들어서 상속하는데 테스트용이라면 다음과 같이 inner class중의 한
	 *     종류인 nested class를 이용하면 편함.         
	 */
	
	//포인트컷의 클래스필터에 선정되도록 이름 변경
	static class TestUserService extends UserServiceImpl{
		private String id = "madnite1"; //테스트 픽스처의 user(3)의 id값 고정.
	
		//UserService의 메소드 오버라이딩
		@Override
		protected void upgradeLevel(User user) { 
			//지정된 id의 User 오브젝트가 발견되면 예외를 던져서 강제로 작업을 중지시킨다.
			if(user.getId().equals(this.id)) throw new TestUserServiceException();
			super.upgradeLevel(user); 
		}
		
		//읽기전용 트랜잭션의 대상인 get으로 시작하는 메소드를 오버라이드.
		@Override
		public List<User> getAll() {
			// TODO Auto-generated method stub
			for(User user : super.getAll()) {
				super.update(user); //강제로 쓰기시도. 여기서 읽기전용속성으로 인한 예외가 발생해야 한다.
			}
			return null;//메소드가 끝나기전에 예외가 발생해야 하니 리턴값은 의미없음.
		}
	}//end TestUserService
	
	//upgraded - 어떤 레벨로 바뀔 것인가가 아니라, 다음 레벨로 업그레이드 될것인가 아닌가를 지정.
	private void checkLevelUpgrade(User user, boolean upgraded) {
		User userUpdate = userDao.get(user.getId());

		if(upgraded) { //업데이트 일어났는지 확인
			assertThat(userUpdate.getUserLevel(),is(user.getUserLevel().nextLevel()));
		}else { //업데이트가 일어나지 않았는지 확인
			assertThat(userUpdate.getUserLevel(),is(user.getUserLevel()));
		}
	}//end checkLevelUpgrade
	
	//자동생성된 프록시 확인
	@Test
	public void advisorAutoProxyCreator() {
		assertThat(testUserService,is(java.lang.reflect.Proxy.class)); //프록시로 변경된 오브젝트인지 확인.
	}
	
	/** 명시적 트랜잭션 테스트
	 *   UserSevice Interface에 @Transactional 에 있으니 모든 메소드에 적용됨
	 *   delete실행시 트랜잭션 실행
	 *   add 실행시 기존에 실행되어 있는 트랜잭션이 없으니 default속성에 의해 새로 생성
	 *   
	 *   총 3번의 transaction이 생성됨.
	 * 
	 */
	@Test
	public void trnasactionSync() {
		userService.deleteAll();
		
		System.out.println("users.get(0) : " + users.get(0));
		System.out.println("users.get(1) : " + users.get(1));
		userService.add(users.get(0));
		userService.add(users.get(1)); //upgrade 로직을 먼저 타야 하는데 그렇지 않아서 add에서 BASIC->SILVER로 변경되어야 함에도 불구하고 그대로임.
	}
	
	/**
	 *   trnasactionSync
	 *    - 3개의 트랜잭션을 하나의 트랜잭션으로 하기 위해서는 트랜잭션을 먼저 실행하고
	 *    service의 각각의 메소드를 실행하면 됨.
	 *    - Ch5 의 UserService.java를 참고.
	*/   
	@Test
	public void transactionSyncOne() {
		
		DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition(); //기본트랜잭션 정의사용.
		txDefinition.setReadOnly(true);
		
		//트랜잭션 매니저에게 트랜잭션을 요청한다. 기존에 시작된 트랜잭션이 없으니 새로운 트랜잭션을 시작시키고,
		//트랜잭션 정보를 돌려준다. 동시에 만들어진 트랜잭션을 다른곳에서도 사용할 수 있게 동기화한다.
		TransactionStatus txStatus = transactionManager.getTransaction(txDefinition); 
		
		//앞에서 만들어진 트랜잭션에 모두 참여.
		//userService.deleteAll();
		userDao.deleteAll();
		
		userService.add(users.get(0));
		userService.add(users.get(1));
		
		transactionManager.commit(txStatus);
		assertThat(userDao.getCount(),is(2));
	}
	
	@Test
	public void transactionSyncRollback() {
		userDao.deleteAll();
		assertThat(userDao.getCount(),is(0));
		DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition(); //기본트랜잭션 정의사용.
		TransactionStatus txStatus = transactionManager.getTransaction(txDefinition); 
		
		userService.add(users.get(0));
		userService.add(users.get(1));
		/* 선언적 트랜잭션은 service에만 국한되는게 아니라서, JdbcTemplate와 같이 스프링이 제공하는 데이터 엑세스 추상화를 적용한 DAO도 동일하게 영향미침.
		   <bean id="userDao" class="springbook.user.dao.UserDaoImpl">
		    <property name="jdbcTemplate" ref="dataSource" />
	       </bean>
	       위의 선언이 있으므로, JdbcTemplate는 트랜잭션이 시작한게 있다면, 해당 트랜잭션에 참여하고 없으면 자동커밋모드로 JDBC작업 수행
		*/
		assertThat(userDao.getCount(),is(2));
		
		transactionManager.rollback(txStatus);
		assertThat(userDao.getCount(),is(0));
	}
	
	
	
}//end UserServiceTest





