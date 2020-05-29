package ua.nure.mikisha.ltd.web.command;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.mikisha.ltd.Path;
import ua.nure.mikisha.ltd.db.CourseManager;
import ua.nure.mikisha.ltd.db.entity.Course;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;

public class ForwardToUpdateCourse extends Command{
	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger.getLogger(LoginCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException, ServerException {
		LOG.debug("Command starts");

		HttpSession session = request.getSession();
		CourseManager c = CourseManager.getInstance();
		Course course = new Course();
		course.setId(Integer.parseInt(request.getParameter("course_id")));
		course = c.getCourse(course);
		request.setAttribute("course", course);
		return Path.PAGE_COURSE_EDIT;
	}
}
