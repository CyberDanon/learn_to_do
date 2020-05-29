package ua.nure.mikisha.ltd.web.command;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.mikisha.ltd.Path;
import ua.nure.mikisha.ltd.db.CourseManager;
import ua.nure.mikisha.ltd.db.SubManager;
import ua.nure.mikisha.ltd.db.entity.Course;
import ua.nure.mikisha.ltd.db.entity.User;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;

public class RateCourseCommand extends Command{
	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger.getLogger(LoginCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException, ServerException {
		LOG.debug("Command starts");

		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		int course_id = Integer.parseInt(request.getParameter("course_id"));
		Course course = new Course();
		course.setId(course_id);
		int rate = Integer.parseInt(request.getParameter("grade"));
		SubManager sm = SubManager.getInstance();
		sm.rate(user, course, rate);
		course = CourseManager.getInstance().getCourse(course);
		request.setAttribute("course", course);
		return Path.FORWARD_COURSE_DISPLAY+course.getId();
	}
}
