package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import springbook.user.domain.User;

/**
DI(Dependency Injection)
A ----> B 
A는 B에 의존한다.
즉, B의 내용이 바뀌면 A에도 영향을 미친다.
*/

public class UserDao {
	
	// 인터페이스 도입으로 인해 UserDao는 자신이 사용할 클래스가 어떤 건지 몰라도 됨. 인터페이스를 통해 원하는 기능을 사용하기만 하면 된다.
	/* 1.6.2 싱글톤과 오브젝트 상태
	 *  1) 멀티 스레드 환경에서 기본적으로 인스턴스 필드 값을 변경하고 유지하는 상태유지(stateful)방식으로 만들지 않는다.
	    2) 아래와 같이 선언하면 멀티스레드 환경에서 매번 새로운 값으로 바뀌기 때문에 심각한 문제가 발생
	        - 파라미터와 로컬변수, 리턴값을 이용하면 새로운 값을 저장할 독립적인 공간이 만들어지기 때문에 싱글톤이라고 해도 여러 스레드가 변수를 덮어쓰지 않음.
	
	private Connection c;
	private User user;
	*/

	// RowMapper callback 오브젝트에는 상태정보가 없기 때문에, 멀티 쓰레드에서 문제가 되지 않는다.
	private RowMapper<User> userMapper = 
		new RowMapper<User>() {
			public User mapRow(ResultSet rs, int rowNum) throws SQLException{
				User user = new User();
				user.setId(rs.getString("id"));
				user.setName(rs.getString("name"));
				user.setPassword(rs.getString("password"));
				return user;
			}
	};
	
	//spring이 제공하는 template
	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(DataSource dataSource) {
		//수동 DI
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	
	
	/**  템플릿,콜백 패턴 
	 *    - add 메소드 : Client
	 *    - StatementStrategy : callback
	 *    - jdbcContext.workWithStatementStrategy : template
	 */
	public void add(final User user){
		this.jdbcTemplate.update("insert into users(id,name,password) values(?,?,?)", user.getId(),user.getName(),user.getPassword());
	 }//end add
	 
	
	// queryForObject 의 조회결과가 없을 경우 예외처리가 자동으로 됨.
	// EmptyResultDataAccessException
	public User get(String id){ //row 1번 조회
		
		/**
		 * ResultSetExtractor callback vs RowMapper callback
		 * 
		 * 1. 공통점 
		 *   - template로부터 ResultSet을 전달받고, 필요한 정보를 추출해서 리턴 하는 방식으로 동작
		 * 2. 차이점
		 *   - ResultSetExtractor 은 ResultSet을 한번 전달 받아서 알아서 추출작업을 모두 진행하고 최종 결과만 리턴
		 *   - RowMapper 는 ResultSet의 로우 하나를 매핑하기 위해 사용되므로 여러번 호출될 수 잇다.
		 */
		//sql에 바인딩할 파라미터 값, 가변인자 대신 배열이용
		return this.jdbcTemplate.queryForObject("select * from users where id=?", new Object[] {id},  this.userMapper);
	 }//end get
	

	/** 
	 * 현재 등록된 모든 사용자를 가져오기.
	 * query return type >> List<T>
	 *   - 결과값이 없을때 ?  크기가 0인 List<T> 가 반환된다.
	 * 
	 * @return
	 */
	public List<User> getAll(){
		return this.jdbcTemplate.query("SELECT * FROM USERS ORDER BY ID", this.userMapper);
	}
	
	public void deleteAll(){
			/*
			this.jdbcTemplate.update( new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					return con.prepareStatement("delete from users");
				}
			});*/
		
		//내장콜백호출.
		this.jdbcTemplate.update("delete from users");
	}
	
	public int getCount(){
		/*
		return this.jdbcTemplate.query(new PreparedStatementCreator() { //첫 번째 callback Statement create
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				// TODO Auto-generated method stub
				return con.prepareStatement("select count(*) from users");
			}
		}, new ResultSetExtractor<Integer>() {  //두번쨰 콜백 ResultSet으로부터 값 추출
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException{
				rs.next();
				return rs.getInt(1);
			}
		});*/
		
		return this.jdbcTemplate.queryForObject("select count(*) from users",Integer.class);
	}//end getCount method 
	
}//end class

