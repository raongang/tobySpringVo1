
package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// 전략패턴(Strategy pattern) 의 strategy 부분.
public interface StatementStrategy {
	PreparedStatement makePreparedStatement(Connection c) throws SQLException;
	
}
