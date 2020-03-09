package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import springbook.user.domain.User;
import springbook.user.domain.User.UserLevel;

/**
 * DI(Dependency Injection) A ----> B A는 B에 의존한다. 즉, B의 내용이 바뀌면 A에도 영향을 미친다.
 */

public class UserDaoImpl implements UserDao{

	// 인터페이스 도입으로 인해 UserDao는 자신이 사용할 클래스가 어떤 건지 몰라도 됨. 인터페이스를 통해 원하는 기능을 사용하기만 하면
	// 된다.
	/**
	 * 1.6.2 싱글톤과 오브젝트 상태 
	 * 
	 * 0) 스프링 빈으로 선언시 싱글톤이 된다.
	 * 
	 * 1) 멀티 스레드 환경에서 기본적으로 인스턴스 필드 값을 변경하고 유지하는
	 * 상태유지(stateful)방식으로 만들지 않는다. 
	 * 
	 * 2) 아래와 같이 선언하면 멀티스레드 환경에서 매번 새로운 값으로 바뀌기 때문에 심각한
	 * 문제가 발생 - 파라미터와 로컬변수, 리턴값을 이용하면 새로운 값을 저장할 독립적인 공간이 만들어지기 때문에 싱글톤이라고 해도 여러
	 * 스레드가 변수를 덮어쓰지 않음.
	 * 
	 * 싱글톤은 static으로 생성한다.
	 * 
	 * private Connection c; private User user;
	 */

	// RowMapper callback 오브젝트에는 상태정보가 없기 때문에, 멀티 쓰레드에서 문제가 되지 않는다.
	private RowMapper<User> userMapper = new RowMapper<User>() {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			user.setUserLevel(UserLevel.valueOf(rs.getInt("userLevel")));
			user.setLogin(rs.getInt("login"));
			user.setRecommend(rs.getInt("recommend"));
			user.setEmailAddress(rs.getString("emailAddress"));
			return user;
		}
	};

	
	private String sqlAdd;
	
	public void setSqlAdd(String sqlAdd) {
		this.sqlAdd = sqlAdd;
	}

	// spring이 제공하는 template
	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(DataSource dataSource) {
		//수동 DI
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	/**
	 * 템플릿,콜백 패턴 - add 메소드 : Client - StatementStrategy : callback - jdbcContext.workWithStatementStrategy : template
	 *   
	 *  SQLException 예외처리 
	 *   - Spring은 DB별 ERROR CODE를 분류해서 Spring이 정의한 예외클래스와 매핑 해 놓은 에러 코드 매핑정보 테이블을 만들어두고 이를 이용.
	 
	public void add(final User user) throws DuplicateUserIdException  {
		try {
			this.jdbcTemplate.update("insert into users(id,name,password) values(?,?,?)", user.getId(), user.getName(), user.getPassword());
		}catch(DuplicateKeyException e) {
			throw new DuplicateUserIdException(e); //예외를 전환시에는 원인이 되는 예외를 중첩하는 것이 좋다.
		}
		
		//try {
		//	//JDBC를 이용해서 USER정보를 db에 추가하는 코드 또는 그런 기능이 있는 다른 SQLException을 던지는 메소드를 호출하는 코드
		//}catch(SQLException e) {
		//	if(e.getErrorCode()==MysqlErrorNumbers.ER_DUP_ENTRY) throw new DuplicateUserIdException(e); //예외전환
		//	throw new RuntimeException(); //예외 포장.
		//}
	}// end add
	*/

	/**
	 * 템플릿,콜백 패턴 - add 메소드 : Client - StatementStrategys:  callback - jdbcContext.workWithStatementStrategy : template
	 *   
	 *  SQLException 예외처리 
	 *   - Spring은 DB별 ERROR CODE를 분류해서 Spring이 정의한 예외클래스와 매핑 해 놓은 에러 코드 매핑정보 테이블을 만들어두고 이를 이용.
	 */
	public void add(final User user) {
		//this.jdbcTemplate.update("insert into users(id,name,password,userLevel,login,recommend, emailAddress) values(?,?,?,?,?,?,?)", user.getId(), user.getName(), user.getPassword(),user.getUserLevel().initValue(),user.getLogin(),user.getRecommend(), user.getEmailAddress());
		this.jdbcTemplate.update(this.sqlAdd
				, user.getId(), user.getName(), user.getPassword(),user.getUserLevel().initValue(),user.getLogin(),
				user.getRecommend(), user.getEmailAddress());
	}
	
	// queryForObject 의 조회결과가 없을 경우 예외처리가 자동으로 됨 / EmptyResultDataAccessException
	public User get(String id) { // row 1번 조회
		// sql에 바인딩할 파라미터 값, 가변인자 대신 배열이용
		return this.jdbcTemplate.queryForObject("select * from users where id=?", new Object[] { id }, this.userMapper);
	}// end get
	
	/* jdbcTemplate 모든 SQLException을 Runtime Exception인 DataAccessException으로 포장하기 떄때문에 필요시 이를 catch해서 처리하면 되고, 그 외는 무시. */
	public void deleteAll() {
		// 내장콜백호출.
		this.jdbcTemplate.update("delete from users");
	}
	
	/** 
	 * 현재 등록된 모든 사용자를 가져오기.
	 * query return type >> List<T>
	 *   - 결과값이 없을때 ?  크기가 0인 List<T> 가 반환된다. 
	 * @return
	 */
	
	public List<User> getAll(){
		return this.jdbcTemplate.query("SELECT * FROM USERS ORDER BY ID", this.userMapper);
	}
	
	public int getCount(){
		return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
	}// end getCount method
	
	/** update */
	@Override
	public void update(User user) {
		System.out.println("user data : " + user.toString());
		// TODO Auto-generated method stub
		this.jdbcTemplate.update("update users set name=?,password=?,userLevel=?,login=?, recommend=? , emailAddress=? where id=?", 
				user.getName(),user.getPassword(),user.getUserLevel().initValue(),user.getLogin(),user.getRecommend(),user.getEmailAddress(),user.getId());
	}

	
}// end class
