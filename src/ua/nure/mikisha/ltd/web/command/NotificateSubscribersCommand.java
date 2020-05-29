package ua.nure.mikisha.ltd.web.command;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.nure.mikisha.ltd.Path;
import ua.nure.mikisha.ltd.db.CourseManager;
import ua.nure.mikisha.ltd.db.MessagesManager;
import ua.nure.mikisha.ltd.db.SubManager;
import ua.nure.mikisha.ltd.db.entity.Course;
import ua.nure.mikisha.ltd.db.entity.User;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;

public class NotificateSubscribersCommand extends Command {
	long serialVersionUID = 7732286014029478505L;

	private static final Logger LOG = Logger.getLogger(UnbanCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServerException, AppException {
		int course_id = Integer.parseInt(request.getParameter("course_id"));
		Course course = new Course();
		CourseManager cm = CourseManager.getInstance();
		course.setId(course_id);
		course = cm.getCourse(course);
		String text = request.getParameter("message");
		text = "Оповещение от курса "+course.getName()+"<br>"+text;
		String goal = "subs";
		MessagesManager mm= MessagesManager.getInstance();
		SubManager sm = SubManager.getInstance();
		List<User> followers = sm.getSubscribers(course);
		for (User u:followers) {
			mm.CreateMessage(u, course, text, goal);
		}
		request.setAttribute("course", course);
		return Path.PAGE_COURSE_DISPLAY;
	}
}

