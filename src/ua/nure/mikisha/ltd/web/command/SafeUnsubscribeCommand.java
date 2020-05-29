package ua.nure.mikisha.ltd.web.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import ua.nure.mikisha.ltd.Path;
import ua.nure.mikisha.ltd.db.SubManager;
import ua.nure.mikisha.ltd.db.entity.Course;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;

public class SafeUnsubscribeCommand extends Command{
	private static final long serialVersionUID = 7732286014029478505L;

	private static final Logger LOG = Logger.getLogger(UnsubscribeCommand.class);
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws ServerException, AppException {
		HttpSession session = request.getSession();
		LOG.trace("Getting of users info to unban starts");
		User user = (User) session.getAttribute("user");
		LOG.info("User with id "+user.getLogin());
		SubManager subs = SubManager.getInstance();
		Course course = new Course();
		course.setId(Integer.parseInt((String)request.getAttribute("course_id")));
		boolean fl = subs.safety_unfollow(user, course);
		if (fl) {
			LOG.trace("User "+user.getLogin()+" sucessfully safety unsubscribed for edition "+course.getId());
		}
		MessagesManager.getInstance().delete_banned_notif(course,user);
		return Path.FORWARD_GET_MESSAGES;
	}
}
