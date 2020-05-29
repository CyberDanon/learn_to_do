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
import ua.nure.mikisha.ltd.db.TestsManager;
import ua.nure.mikisha.ltd.db.entity.Course;
import ua.nure.mikisha.ltd.db.entity.Test;
import ua.nure.mikisha.ltd.db.entity.User;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;

public class DisplayCourseCommand extends Command{
	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger.getLogger(LoginCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException, ServerException {
		LOG.debug("Command starts");

		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		CourseManager c = CourseManager.getInstance();
		Course course = new Course();
		course.setId(Integer.parseInt(request.getParameter("course_id")));
		course = c.getCourse(course);
		TestsManager tm = TestsManager.getInstance();
		int rate = SubManager.getInstance().getRate(user, course);
		for (Test test:course.getTests()) {
			int grade = tm.getGrade(user, test);
			request.setAttribute("test"+test.getId()+"_grade", grade);
		}
		request.setAttribute("rate", rate);
		request.setAttribute("course", course);
		return Path.PAGE_COURSE_DISPLAY;
	}
}
