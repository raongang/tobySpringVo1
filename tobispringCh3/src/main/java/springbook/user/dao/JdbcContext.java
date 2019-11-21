package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.lang3.ArrayUtils;

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
	
	/* 2019 11-21
	 *   - UserDao의 deleteAll의 익명클래스 공통 부분을 별도로 추출.
	 *   
	 *  -  executeSql : client
	 *  -  workWithStatementStrategy : template
	 *  -  makePreparedStatement : callback
	 */
	public void executeSql(final String query, String ...strs) throws SQLException{
		
		workWithStatementStrategy(
				new StatementStrategy() {
					@Override
					public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
						// TODO Auto-generated method stub
						return c.prepareStatement(query);
					}
				},strs);
	}

	
	public void workWithStatementStrategy(StatementStrategy stmt, String ...strs) throws SQLException{
		
		Connection c = null;
		PreparedStatement ps = null;
		
		try {
			c = this.dataSource.getConnection();
			ps = stmt.makePreparedStatement(c);
			
			if(!ArrayUtils.isEmpty(strs)) { //파라미터가 있으면 insert로..
				for(int i=0; i<strs.length;i++) {
					System.out.println("strs["+i+"] >> " + strs[i]);
					ps.setString(i+1,strs[i]);
				}
			}
			
			ps.executeUpdate();
		}catch(SQLException e) {
			throw e;
		}finally {
			if(ps!=null)   {  try { ps.close(); }catch(SQLException e) {}  }
			if(c!=null) {  try { c.close();}catch(SQLException e){}  }
		}
	}
	
}
