package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import springbook.user.domain.User;
import springbook.user.domain.User.UserLevel;

/**
 * DI(Dependency Injection) A ----> B A는 B에 의존한다. 즉, B의 내용이 바뀌면 A에도 영향을 미친다.
 */

public class UserDaoImpl implements UserDao{

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

	private Map<String,String> sqlMap;
	
	public void setSqlMap(Map<String, String> sqlMap) {
		this.sqlMap = sqlMap;
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
	 * 템플릿,콜백 패턴 - add 메소드 : Client - StatementStrategys:  callback - jdbcContext.workWithStatementStrategy : template
	 *   
	 *  SQLException 예외처리 
	 *   - Spring은 DB별 ERROR CODE를 분류해서 Spring이 정의한 예외클래스와 매핑 해 놓은 에러 코드 매핑정보 테이블을 만들어두고 이를 이용.
	 */
	public void add(final User user) {
		//this.jdbcTemplate.update("insert into users(id,name,password,userLevel,login,recommend, emailAddress) values(?,?,?,?,?,?,?)", user.getId(), user.getName(), user.getPassword(),user.getUserLevel().initValue(),user.getLogin(),user.getRecommend(), user.getEmailAddress());
		this.jdbcTemplate.update(this.sqlMap.get("add")
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
