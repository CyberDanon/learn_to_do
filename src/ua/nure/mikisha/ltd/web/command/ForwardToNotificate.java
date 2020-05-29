package ua.nure.mikisha.ltd.web.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.nure.mikisha.ltd.Path;
import ua.nure.mikisha.ltd.db.CourseManager;
import ua.nure.mikisha.ltd.db.entity.Course;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;

public class ForwardToNotificate extends Command {
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
		request.setAttribute("course", course);
		return Path.PAGE_NOTIFICATE;
	}
}
