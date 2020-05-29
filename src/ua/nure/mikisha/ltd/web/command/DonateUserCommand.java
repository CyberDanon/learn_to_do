package ua.nure.mikisha.ltd.web.command;

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
 * Command of computing user's charging
 * @author Mikisha Danylo
 *
 */
public class DonateUserCommand extends Command{
	private static final long serialVersionUID = 7732286014029478501L;

	private static final Logger LOG = Logger.getLogger(DonateUserCommand.class);
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws ServerException, AppException {
		HttpSession session = request.getSession();
		LOG.trace("Getting of current user info to update starts");
		User user = (User) session.getAttribute("user");
		LOG.info("User with id "+user.getLogin());
		int money = Integer.parseInt(request.getParameter("sum"));
		UserManager us = UserManager.getInstance();
		user.setBalance(user.getBalance()+money);
		us.pay(user);
		LOG.trace("User's balance is updated");
		session.setAttribute("user", user);
		return Path.FORWARD_PERSONAL;
	}
}