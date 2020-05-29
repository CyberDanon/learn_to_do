package ua.nure.mikisha.ltd.web.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;


import ua.nure.mikisha.ltd.Path;
import ua.nure.mikisha.ltd.db.UserManager;
import ua.nure.mikisha.ltd.db.entity.Student;
import ua.nure.mikisha.ltd.db.entity.User;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;
/**
 * Command to create User
 * @author Mikisha Danylo
 *
 */
public class CreateUserCommand extends Command {
	private static final long serialVersionUID = 7132286014029478505L;

	private static final Logger LOG = Logger.getLogger(CreateUserCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServerException, AppException {
		HttpSession session = request.getSession();
		LOG.trace("Getting of edition info to create starts");
		User user = new Student();
		user.setLogin((String)request.getParameter("login"));
		user.setPassword((String)request.getParameter("password"));
		user.setMail((String)request.getParameter("mail"));
		UserManager us = UserManager.getInstance();
		us.insertUser(user);
		LOG.trace("User "+user.getLogin()+" was successfully created");
		return Path.FORWARD_TO_LOGIN;
	}
}