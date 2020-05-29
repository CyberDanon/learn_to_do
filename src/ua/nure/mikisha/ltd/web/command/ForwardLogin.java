package ua.nure.mikisha.ltd.web.command;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.mikisha.ltd.Path;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;
/**
 * Command to forward user to login page
 * @author Mikisha Danylo
 *
 */
public class ForwardLogin extends Command{
	private static final long serialVersionUID = 7672286014029478505L;

	private static final Logger LOG = Logger.getLogger(ForwardLogin.class);
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws ServerException, AppException {
		HttpSession session = request.getSession();
		LOG.trace("Forwarded to Login");
		return Path.PAGE_LOGIN;
	}
}