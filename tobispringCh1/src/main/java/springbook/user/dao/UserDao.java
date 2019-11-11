package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import springbook.user.domain.User;

/**
DI(Dependency Injection)
A ---- > B 
A는 B에 의존한다.
즉, B의 내용이 바뀌면 A에도 영향을 미친다.
*/

public class UserDao {

	// 인터페이스 도입으로 인해 UserDao는 자신이 사용할 클래스가 어떤 건지 몰라도 됨. 인터페이스를 통해 원하는 기능을 사용하기만 하면 된다.
	
	private ConnectionMaker connectionMaker;
	
	public UserDao(ConnectionMaker connectionMaker) {
		this.connectionMaker = connectionMaker;
		
	}
	
	 public void add(User user) throws  SQLException, ClassNotFoundException{
		
		Connection conn =  connectionMaker.makeConnect(); 
		
		PreparedStatement ps = conn.prepareStatement("insert into users(id,name,password) values(?,?,?)");
		
		ps.setString(1, user.getId());
		ps.setString(2, user.getName()); 
		ps.setString(3, user.getPassword());
		
		ps.executeUpdate();
		
		ps.close();
		conn.close();
	 }//end add
	 
	public User get(String id) throws ClassNotFoundException, SQLException{
		Connection conn =  connectionMaker.makeConnect();  
		
		PreparedStatement ps = conn.prepareStatement("select * from users where id=?");
		ps.setString(1, id);
		
		ResultSet rs = ps.executeQuery();
		rs.next();
		
		User user = new User();
		user.setId(rs.getString("id"));
		user.setName(rs.getString("name"));
		user.setPassword(rs.getString("password"));
		
		rs.close();
		ps.close();
		conn.close();
		return user;
		
	 }//end get
}
