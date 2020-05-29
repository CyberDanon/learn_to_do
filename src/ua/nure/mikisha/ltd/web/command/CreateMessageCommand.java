package ua.nure.mikisha.ltd.web.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.mikisha.ltd.db.MessagesManager;
import ua.nure.mikisha.ltd.db.entity.Course;
import ua.nure.mikisha.ltd.db.entity.Message;
import ua.nure.mikisha.ltd.db.entity.Student;
import ua.nure.mikisha.ltd.db.entity.User;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;
import ua.nure.mikisha.ltd.Path;

public class CreateMessageCommand extends Command {
	private static final long serialVersionUID = 7732286014029478501L;

	private static final Logger LOG = Logger.getLogger(DonateUserCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServerException, AppException {
		HttpSession session = request.getSession();
		LOG.trace("Getting of current user info to update starts");
		MessagesManager mm = MessagesManager.getInstance();
		Course course = new Course();
		course.setId(Integer.parseInt(request.getParameter("course_id")));
		User user = new Student();
		String topic = request.getParameter("topic");
		String text = request.getParameter("text");
		user.setLogin(request.getParameter("user_login"));
		return Path.FORWARD_COURSE_DISPLAY+course.getId();
	}
}

