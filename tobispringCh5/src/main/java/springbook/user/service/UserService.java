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
public class UserService {
	
	UserDao userDao;

	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;
	
	/*
	//transaction 동기화 적용
	private DataSource dataSource;
	
	//Connection을 생성할 때 사용할 DateSource DI
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}*/
	
	//DI
	private PlatformTransactionManager transactionManager;

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	//DI
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	/***
	 *  Transaction 동기화 기법
	    - 트랜잭션 이용을 위해 Transaction Synchronizations 기법을 이용
	    - 트랜잭션 동기화 저장소는 작업 스레드마다 독립적으로 Connection 오브젝트를 저장하고 관리하기 때문에,
	      다중 사용자를 처리하는 서버의 멀티스레드 환경에서도 충돌나지 않음.
	    - 트랜잭션 동기화 기법을 이용시 파라미터를 통해 일일이 Connection 오브젝트를 전달 할 필요가 없다.
	 * @throws Exception
	
	public void upgradeLevels() throws Exception{
		
		// 트랜잭션 동기화 작업관리자를 이용해 동기화 작업을 초기화한다.
		TransactionSynchronizationManager.initSynchronization();
		//DB 커넥션을 생성하고 트랜잭션을 시작한다. 이후의 DAO 작업은 모두 여기서 시작한 트랜잭션 안에서 진행된다.
		//DB 커넥션 생성과 동기화를 함께 해주는 유틸리티 메소드.
		//DataSourceUtils.getConnection -> Connection 오브젝트를 생성할 뿐만 아니라, 트랜잭션 동기화에 사용하도록 저장소에 바인딩해줌.
		
		Connection c = DataSourceUtils.getConnection(dataSource);
		c.setAutoCommit(false);
		
		try {
			//동기화가 되어있으므로, JdbcTemplate에서 동기화된 DB커넥션을 이용.
			List<User> users = userDao.getAll();
			for(User user : users) {
				if(canUpgradeLevel(user)) {
					upgradeLevel(user);
				}
			}
			c.commit();
			
		}catch(Exception e) {
			c.rollback();
			throw e;
		}finally {
			//스프링 유틸리티 메소드를 이용해서 DB 커넥션을 안전하게 닫는다.
			DataSourceUtils.releaseConnection(c, dataSource);
			//동기화 작업조욜 및 정리
			TransactionSynchronizationManager.unbindResource(this.dataSource);
			TransactionSynchronizationManager.clearSynchronization();
		}
	}
	*/
	
	/** 트랜잭션 서비스 추상화 **/
	public void upgradeLevels() throws Exception{
		
		//dataSourc - JDBC트랜잭션 추상 오브젝트 생성.  PlatformTransactionManager 는 추상 interface
		//DI방식으로 변경되어야 한다.
		//PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
		
		//트랜잭션 시작
		//new DefaultTransactionDefinition() - transaction 속성 
		
		TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());

		try {
			List<User> users = userDao.getAll();
			for(User user : users) {
				if(canUpgradeLevel(user)) {
					upgradeLevel(user);
				}
			}
			this.transactionManager.commit(status);
		}catch(Exception e) {
			this.transactionManager.rollback(status);
			throw e;
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
	protected void upgradeLevel(User user) {
		user.upgradeLevel();
		userDao.update(user);
	}

	
	
}//end UserService
