package springbook.user.service;

import java.util.List;
import java.util.Properties;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import springbook.user.dao.UserDao;
import springbook.user.domain.User;
import springbook.user.domain.User.UserLevel;

 // UserSerivce-->UserDao 에 의존. UserDao의 내용이 바뀌면 UserSerivce에 영향  , 따라서 UserDao를 Interface로 둬야 한다.
public class UserServiceImpl implements UserService{

	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;
	
	UserDao userDao;

	//DI
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public void upgradeLevels() {
		// TODO Auto-generated method stub
		List<User> users = userDao.getAll();
		for(User user : users) {
			if(canUpgradeLevel(user)) {
				upgradeLevel(user);
			}
		}
	}

	
	//업그레이드 가능 확인 메소드
	private boolean canUpgradeLevel(User user) {
		UserLevel currentLevel = user.getUserLevel();
		
		switch(currentLevel) {
			case BASIC: return ( user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
			case SILVER : return ( user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
			case GOLD : return false;
			default : throw new IllegalArgumentException("Unknown Level : " + currentLevel);
		}
	}
	
	//레벨 업그레이드 작업 메소드
	//upgradeLevels 에 생성한 트랜잭션 정보는 트랜잭션 동기화 방법을 토애 userDao에서 알아서 활용한다.
	protected void upgradeLevel(User user) {
		
		System.out.println("UserServiceImpl upgradeLevel enter");
		user.upgradeLevel();
		userDao.update(user);
	}
	
	@Override
	public void add(User user) {
		// TODO Auto-generated method stub
	}
	
	
	
}//end UserService
