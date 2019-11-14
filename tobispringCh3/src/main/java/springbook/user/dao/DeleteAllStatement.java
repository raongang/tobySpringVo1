package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteAllStatement implements StatementStrategy{

	@Override
	public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
		// 
		System.out.println("DeleteAllStatement makePreparedStatement enter");
		PreparedStatement ps = c.prepareStatement("delete from users");
		return ps;
	}
	

}
