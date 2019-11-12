package springbook.user.dao;
import java.sql.Connection;
import java.sql.SQLException;

public class CountingConnectionMaker implements ConnectionMaker{

	int counter=0;
	private ConnectionMaker realConnectionMaker;
	
	public CountingConnectionMaker(ConnectionMaker realConnectionMaker) {
		this.realConnectionMaker = realConnectionMaker;
	}

	@Override
	public Connection makeConnect() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		System.out.println("makeConnect start!");
		this.counter++;
		return realConnectionMaker.makeConnect();
		
	}
	
	public int getCounter() {
		return this.counter;
	}
	
	
	
}
