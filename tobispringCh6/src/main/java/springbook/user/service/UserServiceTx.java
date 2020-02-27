package springbook.user.service;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import springbook.user.domain.User;

/**
 *  userSerive안의 transaction 서비스 추상화 기법이 적용된 transaction 경계 부분을 분리하기 위함.
 *  UserServiceTx는 transaction 경계부분 설정을 통해 UserService Interface를 구현하는 구현체에 실제적인 로직처리 작업을 위임한다.
*/

public class UserServiceTx implements UserService{

	//target object
	UserService userService;
	
	//DI
	private PlatformTransactionManager transactionManager;

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	//userService를 구현한 또다른 오브젝트를 DI. ( 여기서는 UserServiceImpl ) 
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	/* DI받은 UserService 오브젝트에 모든 기능을 위임한다. */
	@Override
	public void add(User user) {
		// TODO Auto-generated method stub
		userService.add(user);
	}// add
	 
	//메소드 구현.
	@Override
	public void upgradeLevels() { 
		// TODO Auto-generated method stub
		//부가기능 수행
		TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			userService.upgradeLevels(); //위임
			this.transactionManager.commit(status);
		}catch(RuntimeException e) {
			this.transactionManager.rollback(status);
			throw e;
		}
	}//upgradeLevels
	
	
}
