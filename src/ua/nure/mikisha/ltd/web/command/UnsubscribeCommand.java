package ua.nure.mikisha.ltd.web.command;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import ua.nure.mikisha.ltd.Path;
import ua.nure.mikisha.ltd.db.SubManager;
import ua.nure.mikisha.ltd.db.entity.Course;
import ua.nure.mikisha.ltd.db.entity.User;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;
/**
 * Unsubscribe from subscribed edition
 * @author User
 *
 */
public class UnsubscribeCommand extends Command{
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
		boolean fl = subs.UNSUB(user,course);
		if (fl) {
			LOG.trace("User "+user.getLogin()+" sucessfully unsubscribed for edition "+course.getId());
		}
		return Path.FORWARD_GET_SUBS;
	}
}