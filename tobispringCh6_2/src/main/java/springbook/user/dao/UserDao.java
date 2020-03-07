package springbook.user.dao;

import java.util.List;

import springbook.user.domain.User;

public interface UserDao {
	
	void add(User user);
	
	User get(String id);
	
	void deleteAll();
	
	List<User> getAll();
	
	int getCount();
	
	public void update(User user1);
}
