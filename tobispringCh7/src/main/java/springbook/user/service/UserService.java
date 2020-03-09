package springbook.user.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import springbook.user.domain.User;

/**
 *  userSerive안의 transaction 서비스 추상화 기법이 적용된 transaction 경계 부분을 분리하기 위함. 
*/

//<tx:method name="*"/> 과 같은 설정효과를 가져온다. 메소드레벨 @Transactional이 없으므로 대체 정책에 따라 타입레벨에 부여된 디폴트 속성이 적용된다.
@Transactional
public interface UserService {
	
	/* 신규 추가메소드 - service가 아닌 곳에서 다이렉트로 dao로 접근을 막기 위함. 
	 * DAO메소드와 1:1 대응되는 CRUD 메소드이지만, add()처럼 단순 위임 이상의 로직을 가질 수 있다. 
	 */
	void add(User user);
	
	
	/* <tx:method name="get" read-only="true/>애노테이션 방식으로 변경.
	 *  메소드 단위로 부여된 트랜잭션의 속성이 타입 레벨에 부여된 것에 우선해서 적용된다.
	 *  같은 속성을 가졌어도 메소드 레벨에 부여될 때는 메소드마다 반복될수밖에 없음.
	 * */
	@Transactional(readOnly=true)
	User get(String id);
	@Transactional(readOnly=true)
	List<User> getAll();
	
	void deleteAll();
	void update(User user);
	
	void upgradeLevels();

	
}
