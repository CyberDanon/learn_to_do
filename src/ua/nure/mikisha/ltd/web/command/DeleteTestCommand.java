package ua.nure.mikisha.ltd.web.command;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;


import ua.nure.mikisha.ltd.Path;
import ua.nure.mikisha.ltd.db.CourseManager;
import ua.nure.mikisha.ltd.db.TestsManager;
import ua.nure.mikisha.ltd.db.entity.Course;
import ua.nure.mikisha.ltd.db.entity.Test;

public class DeleteTestCommand extends Command{
	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger.getLogger(LoginCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException, AppException, ServerException {
		LOG.debug("Command starts");

		HttpSession session = request.getSession();
		int test_id = Integer.parseInt(request.getParameter("test_id"));
		Test test = new Test();
		test.setId(test_id);
		TestsManager tm = TestsManager.getInstance();
		String sourse = request.getParameter("source");
		int sourse_id = Integer.parseInt(request.getParameter("source_id"));
		test = tm.getTest(test);
		tm.DeleteTest(test);
		request.setAttribute("test_id", test.getId());
			CourseManager cm = CourseManager.getInstance();
			Course course = new Course();
			course.setId(sourse_id);
			course = cm.getCourse(course);
			tm.CreateCourseTest(test, course);
			request.setAttribute("course_id", course.getId());
			return Path.FORWARD_COURSE_EDIT+course.getId();
		
	}
}
