package ua.nure.mikisha.ltd.web.command;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.mikisha.ltd.db.AnswersManager;
import ua.nure.mikisha.ltd.db.CourseManager;
import ua.nure.mikisha.ltd.db.TestsManager;
import ua.nure.mikisha.ltd.db.TheoryBlocksManager;
import ua.nure.mikisha.ltd.db.entity.Answer;
import ua.nure.mikisha.ltd.db.entity.Course;
import ua.nure.mikisha.ltd.db.entity.Test;
import ua.nure.mikisha.ltd.db.entity.Theorical;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;
import ua.nure.mikisha.ltd.Path;

public class CreateTestCommand extends Command{
	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger.getLogger(LoginCommand.class);
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException, AppException, ServerException {
		LOG.debug("Command starts");
		int sourse_id = Integer.parseInt(request.getParameter("sourse_id"));
		Test test = new Test();
		test.setDescription("Course description");
		test.setName("New course");
		TestsManager tm = TestsManager.getInstance();
		tm.CreateTest(test);
		String sourse = request.getParameter("sourse");
		if (sourse.equals("course")) {
			CourseManager cm = CourseManager.getInstance();
			Course course = new Course();
			course.setId(sourse_id);
			course = cm.getCourse(course);
			tm.CreateCourseTest(test, course);
			request.setAttribute("course", course);
			return Path.FORWARD_COURSE_EDIT+course.getId()+"&method=GET";
		} else {
			TheoryBlocksManager cm = TheoryBlocksManager.getInstance();
			Theorical th = new Theorical();
			th.setId(sourse_id);
			th = cm.getTheory(th);
			tm.CreateTheoryTest(test, th);
			request.setAttribute("theory", th);
			CourseManager cmt = CourseManager.getInstance();
			Course course = new Course();
			course.setId(th.getCourse_id());
			course = cmt.getCourse(course);
			tm.CreateCourseTest(test, course);
			request.setAttribute("course", course);
			return Path.FORWARD_COURSE_EDIT+course.getId()+"&method=GET";
		}
		
	}
}
