package ua.nure.mikisha.ltd.web.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;


import ua.nure.mikisha.ltd.Path;
import ua.nure.mikisha.ltd.db.entity.User;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;

public class DisplayUser extends Command{
	private static final long serialVersionUID = 7732286014029478501L;

	private static final Logger LOG = Logger.getLogger(DonateUserCommand.class);
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws ServerException, AppException {
		HttpSession session = request.getSession();
		LOG.trace("Getting of current user info to update starts");
		User user = (User) session.getAttribute("user");
		session.setAttribute("curr_user", user);
		return Path.PAGE_PERSONAL;
	}
}
