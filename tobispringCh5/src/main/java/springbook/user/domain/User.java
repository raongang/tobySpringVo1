package springbook.user.domain;

public class User {
	
	String id;
	String name;
	String password;
	
	UserLevel userLevel;
	int login;
	int recommend;
	
	public enum UserLevel{
		BASIC(1), SILVER(2), GOLD(3);
		
		private final int value;
		
		UserLevel(int value){
			this.value = value;
		}
		
		public int initValue(){ //값을 가져오느 메소드
			return value;
		}
		
		public static UserLevel valueOf(int value) { 
			switch(value) {
				case 1:   return BASIC;
				case 2:   return SILVER;
				case 3:   return GOLD;
				default : throw new AssertionError("Unkown value : " + value);
			}
		}
	}//end level
	


	public User(String id, String name, String password, UserLevel userLevel, int login, int recommend) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.userLevel = userLevel;
		this.login = login;
		this.recommend = recommend;
	}
	


	public UserLevel getUserLevel() {
		return userLevel;
	}


	public void setUserLevel(UserLevel userLevel) {
		this.userLevel = userLevel;
	}


	public int getLogin() {
		return login;
	}

	public void setLogin(int login) {
		this.login = login;
	}

	public int getRecommend() {
		return recommend;
	}

	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}



	public User() {
		
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "id >> " + id + " name >> " + name + " password >> " + password;
	}
}
