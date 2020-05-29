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
import ua.nure.mikisha.ltd.db.entity.User;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;

public class DisplayTestCommand extends Command{
		private static final long serialVersionUID = -3071536593627692473L;

		private static final Logger LOG = Logger.getLogger(LoginCommand.class);

		@Override
		public String execute(HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException, AppException, ServerException {
			LOG.debug("Command starts");

			HttpSession session = request.getSession();
			User user = (User)session.getAttribute("user");
			TestsManager t = TestsManager.getInstance();
			int course_id = Integer.parseInt(request.getParameter("course_id"));
			Test test = new Test();
			test.setId(Integer.parseInt(request.getParameter("test_id")));
			test = t.getTest(test);
			request.setAttribute("test_preview", test);
			Course course = new Course();
			course.setId(course_id);
			CourseManager c = CourseManager.getInstance();
			course = c.getCourse(course);
			request.setAttribute("course", course);
			TestsManager tm = TestsManager.getInstance();
			int grade = tm.getGrade(user, test);
			if (grade!=0) {
				request.setAttribute("grade", grade);
			}
			return Path.PAGE_COURSE_DISPLAY;
		}
}
