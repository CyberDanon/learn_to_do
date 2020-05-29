package ua.nure.mikisha.ltd.web.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import ua.nure.mikisha.ltd.Path;
import ua.nure.mikisha.ltd.db.UserManager;
import ua.nure.mikisha.ltd.db.entity.User;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.Messages;
import ua.nure.mikisha.ltd.exception.ServerException;
/**
 * Update user
 * @author Mikisha Danylo
 *
 */
public class UpdateUserCommand extends Command{
	private static final long serialVersionUID = 7732286014020478505L;

	private static final Logger LOG = Logger.getLogger(UpdateUserCommand.class);
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws ServerException, AppException {
		HttpSession session = request.getSession();
		LOG.trace("Getting of current user info to update starts");
		User curr_user = (User) session.getAttribute("user");
		LOG.info("User with id "+curr_user.getLogin());
		UserManager us = UserManager.getInstance();
		if (request.getParameter("oldpass").equals(curr_user.getPassword())) {
		curr_user.setPassword((String)request.getParameter("newpass"));
		us.updateUser(curr_user);
		} else {
			throw new AppException(Messages.WRONG_PASS);
		}
		LOG.trace("Results are putten into request");
		return Path.FORWARD_PERSONAL;
	}
}