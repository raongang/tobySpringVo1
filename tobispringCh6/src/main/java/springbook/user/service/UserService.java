package springbook.user.service;

import springbook.user.domain.User;

/**
 *  userSerive안의 transaction 서비스 추상화 기법이 적용된 transaction 경계 부분을 분리하기 위함. 
*/
public interface UserService {
	
	void add(User user);
	void upgradeLevels();

}
