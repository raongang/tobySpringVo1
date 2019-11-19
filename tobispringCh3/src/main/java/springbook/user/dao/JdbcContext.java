package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 *  jdbcContext를 UserDao와 DI구조로 만들어야 하는 이유
 *   ㄴ jdbcContext가 dataSource 프로퍼티를 통해 오브젝트를 주입받음
 *   ㄴ DI를 위해서는 주입되는 오브젝트와 주입받는 오브젝트 양쪽 모두 스프링 빈으로 등록되어야 함.
 *   ㄴ 스프링이 생성하고 관리하는 IoC 대상이어야 DI에 참여가능.
 * @author admin
 *
 */
public class JdbcContext {
	
	public DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void workWithStatementStrategy(StatementStrategy stmt) throws SQLException{
		Connection c = null;
		PreparedStatement ps = null;
		
		try {
			c = this.dataSource.getConnection();
			ps = stmt.makePreparedStatement(c);
			ps.executeUpdate();
		}catch(SQLException e) {
			throw e;
		}finally {
			if(ps!=null)   {  try { ps.close(); }catch(SQLException e) {}  }
			if(c!=null) {  try { c.close();}catch(SQLException e){}  }
		}
	}
	
}
