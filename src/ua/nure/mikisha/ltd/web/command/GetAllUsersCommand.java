package ua.nure.mikisha.ltd.web.command;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.mikisha.ltd.Path;
import ua.nure.mikisha.ltd.db.UserManager;
import ua.nure.mikisha.ltd.db.entity.User;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;

public class GetAllUsersCommand extends Command{
	private static final long serialVersionUID = 7732286014029478505L;

	private static final Logger LOG = Logger.getLogger(UnbanCommand.class);
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws ServerException, AppException {
		HttpSession session = request.getSession();
		LOG.trace("Getting of users info to unban starts");
		UserManager us = UserManager.getInstance();
		List<User> users= us.findAllUsers();
		request.setAttribute("user_list", users);
		LOG.trace("Results are putten into request");
		return Path.PAGE_GET_ALL_USERS;
	}
}
