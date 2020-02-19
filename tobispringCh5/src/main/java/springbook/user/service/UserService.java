package springbook.user.service;

import java.util.List;

import springbook.user.dao.UserDao;
import springbook.user.domain.User;
import springbook.user.domain.User.UserLevel;

 // UserSerivce-->UserDao 에 의존. UserDao의 내용이 바뀌면 UserSerivce에 영향  , 따라서 UserDao를 Interface로 둬야 한다.
public class UserService {
	
	UserDao userDao;

	//DI
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public void upgradeLevels() {
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
			case BASIC: return ( user.getLogin() >= 50);
			case SILVER : return ( user.getRecommend() >= 30);
			case GOLD : return false;
			default : throw new IllegalArgumentException("Unknown Level : " + currentLevel);
		}
	}
	//레벨 업그레이드 작업 메소드
	public void upgradeLevel(User user) {
		user.upgradeLevel();
		userDao.update(user);
	}
	
	
}//end UserService
