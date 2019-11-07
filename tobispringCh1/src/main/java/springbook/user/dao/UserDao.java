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
	
	private SimpleConnectionMaker simpleConnectionMaker;
	
	public UserDao() {
		simpleConnectionMaker = new SimpleConnectionMaker();
	}
	
	 
	 public void add(User user) throws  SQLException, ClassNotFoundException{
		
		Connection conn =  simpleConnectionMaker.makeNewConnection(); 
		
		PreparedStatement ps = conn.prepareStatement("insert into users(id,name,password) values(?,?,?)");
		
		ps.setString(1, user.getId());
		ps.setString(2, user.getName());
		ps.setString(3, user.getPassword());
		
		ps.executeUpdate();
		
		ps.close();
		conn.close();
	 }//end add
	 
	public User get(String id) throws ClassNotFoundException, SQLException{
		
		Connection conn =  simpleConnectionMaker.makeNewConnection();  
		
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
	 
	
	/* db test
	public static void main(String args[]) throws ClassNotFoundException, SQLException {
		
		UserDao dao = new UserDao();
		User user = new User();
		user.setId("whiteship");
		user.setName("tom");
		user.setPassword("married");
		dao.add(user);
		System.out.println(user.getId() + "등록 성공");
		
		User user2 = dao.get(user.getId());
		System.out.println(user2.getName());
		System.out.println(user2.getPassword());
		
		System.out.println(user.getId() + " 조회 성공");
	}*/	
}



