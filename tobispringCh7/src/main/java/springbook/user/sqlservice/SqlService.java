package springbook.user.sqlservice;

public interface SqlService {

	//SQL Key를 가져오는 경우 복구 불가능이므로 Runtime Exception 으로 throw하게 만듬.
	String getSql(String key) throws SqlRetrievalFailureException;
	
	
}
