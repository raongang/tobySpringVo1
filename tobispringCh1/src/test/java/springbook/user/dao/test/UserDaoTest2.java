package springbook.user.dao.test;

import springbook.user.dao.DaoFactory;
import springbook.user.dao.UserDao;

public class UserDaoTest2 {

	//동일하지 않음.
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DaoFactory factory = new DaoFactory();
		UserDao dao1 = factory.userDao();
		UserDao dao2 = factory.userDao();
		
		System.out.println("dao1 >> " + dao1);
		System.out.println("dao2 >> " + dao2);
		
	}

}

