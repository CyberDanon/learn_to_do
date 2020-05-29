package ua.nure.mikisha.ltd.exception;
/**
 * 
 * also thrown where unable to get connection
 * @author Mikisha Danylo
 *
 */
public class AppException extends Exception {

	private static final long serialVersionUID = 818877902647218916L;

	public AppException() {
		super();
	}

	public AppException(String message, Throwable cause) {
		super(message, cause);
	}

	public AppException(String message) {
		super(message);
	}

}
