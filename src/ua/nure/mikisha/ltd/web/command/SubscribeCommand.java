package ua.nure.mikisha.ltd.web.command;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;


import ua.nure.mikisha.ltd.Path;
import ua.nure.mikisha.ltd.db.CourseManager;
import ua.nure.mikisha.ltd.db.SubManager;
import ua.nure.mikisha.ltd.db.UserManager;
import ua.nure.mikisha.ltd.db.entity.Course;
import ua.nure.mikisha.ltd.db.entity.User;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.Messages;
import ua.nure.mikisha.ltd.exception.ServerException;
/**
 * Subscribe command with charging of it's owner and withdrawing of current user
 * @author Mikisha Danylo
 *
 */
public class SubscribeCommand extends Command {
	private static final long serialVersionUID = 7732286014009478505L;

	private static final Logger LOG = Logger.getLogger(SubscribeCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServerException, AppException {
		HttpSession session = request.getSession();
		LOG.trace("Getting of edition and user info for subscribe starts");
		User user = (User) session.getAttribute("user");
		LOG.info("User with id " + user.getLogin() + " and balance " + user.getBalance());
		CourseManager ed = CourseManager.getInstance();
		Course c = new Course();
		c = ed.getCourse(c);
		LOG.info("Edition with id " + c.getId() + " and price " + c.getPrice());
		if (user.getBalance() >= c.getPrice()) {
			session.setAttribute("user", user);
			SubManager subs = SubManager.getInstance();
			SubManager s = SubManager.getInstance();
			boolean fl = subs.SUB(user,c);
			if (fl) {
				user.setBalance(user.getBalance()-c.getPrice());
				UserManager us = UserManager.getInstance();
				us.pay(user);
				User owner = us.getUser(user.getLogin());
				int money = owner.getBalance()+c.getPrice();
				owner.setBalance(money);
				us.pay(owner);
				LOG.trace("User " + user.getLogin() + " sucessfully subscribed for course " + c.getId());
			}
			return Path.FORWARD_GET_RECOMS;
		} else {
			throw new AppException(Messages.APP_ERR_LESS_BALANCE);
		}
	}
}