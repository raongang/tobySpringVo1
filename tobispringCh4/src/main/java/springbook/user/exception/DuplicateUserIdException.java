package springbook.user.exception;



/** ID Duplicate exception class
 * 
 * 	object
 *    └ Throwable class
 *       └ Exception
 * */
public class DuplicateUserIdException extends RuntimeException{
	//중첩예제
	public DuplicateUserIdException(Throwable cause) {
		super(cause);
	}
}
