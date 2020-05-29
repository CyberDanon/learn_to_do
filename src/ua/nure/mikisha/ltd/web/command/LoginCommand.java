package ua.nure.mikisha.ltd.web.command;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import ua.nure.mikisha.ltd.Path;
import ua.nure.mikisha.ltd.db.UserManager;
import ua.nure.mikisha.ltd.db.entity.User;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;


/**
 * Login command.
 * 
 * @author Mikisha Danylo
 * 
 */
public class LoginCommand extends Command {

	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger.getLogger(LoginCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException, AppException, ServerException {
		LOG.debug("Command starts");

		HttpSession session = request.getSession();

		// obtain login and password from a request
		UserManager manager = UserManager.getInstance();
		String login = (String) request.getParameter("login");
		LOG.trace("Request parameter: login --> " + login);

		String password = (String) request.getParameter("password");
		if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
			throw new AppException("Login/password cannot be empty");
		}

		User user = manager.getUser(login);
		LOG.trace("Found in DB: user --> " + user);

		if (user == null || !password.equals(user.getPassword())) {
			throw new AppException("Cannot find user with such login/password");
		}
		if (user.getIs_banned()==1) {
			throw new AppException("¬ы забанены, невозможно залогинитьс€");
		}
		String role = (user.getIs_admin()==1)?"admin":((user.getIs_teacher()==1)?"teacher":"student");
		LOG.trace("Access level --> " + role);
		session.setAttribute("user", user);
		LOG.trace("Set the session attribute: user --> " + user);

		LOG.debug("Command finished");
		return "controller?command=personaldata&method=GET";
	}

}