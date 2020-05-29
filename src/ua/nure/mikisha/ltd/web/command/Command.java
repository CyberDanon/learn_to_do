package ua.nure.mikisha.ltd.web.command;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;

/**
 * Main interface for the Command pattern implementation.
 * 
 * @author Mikisha Danylo
 * 
 */
public abstract class Command implements Serializable {
	private static final long serialVersionUID = 8879403039606311780L;

	/**
	 * Execution method for command.
	 * 
	 */
	public abstract String execute(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			AppException, ServerException;

	@Override
	public final String toString() {
		return getClass().getSimpleName();
	}
}