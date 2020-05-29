package ua.nure.mikisha.ltd.exception;
/**
 * Also thrown when it's unnable to directly close connection
 * @author Mikisha Danylo
 */
public class ServerException extends Exception {

	private static final long serialVersionUID = 818877902647218916L;

	public ServerException() {
		super();
	}

	public ServerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServerException(String message) {
		super(message);
	}

}
