package springbook.user.exception;



/** ID Duplicate exception class
 * 
 * 	object
 *    └ Throwable class
 *       └ Exception
 *       
 *    
 *    어디서든 DuplicateUserIdException 를 잡아서 처리할 수 있으면 런타임 예외로 만드는게 나음.
 *    대신 명시적으로 throw DuplicateUserIdException 를 이용해야함 ( cf. add() )
 * */
public class DuplicateUserIdException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	//중첩예제
	public DuplicateUserIdException(Throwable cause) {
		super(cause);
	}
}
