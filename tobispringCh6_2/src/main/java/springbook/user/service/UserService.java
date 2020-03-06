package springbook.user.service;

import java.util.List;

import springbook.user.domain.User;

/**
 *  userSerive안의 transaction 서비스 추상화 기법이 적용된 transaction 경계 부분을 분리하기 위함. 
*/
public interface UserService {
	
	/* 신규 추가메소드 - service가 아닌 곳에서 다이렉트로 dao로 접근을 막기 위함. 
	 * DAO메소드와 1:1 대응되는 CRUD 메소드이지만, add()처럼 단순 위임 이상의 로직을 가질 수 있다. 
	 */
	void add(User user);
	User get(String id);
	List<User> getAll();
	void deleteAll();
	void update(User user);
	
	
	void upgradeLevels();
	
	
}
