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
import ua.nure.mikisha.ltd.db.entity.Answer;
import ua.nure.mikisha.ltd.db.entity.Course;
import ua.nure.mikisha.ltd.db.entity.Question;
import ua.nure.mikisha.ltd.db.entity.Test;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;

public class UpdateTestCommand extends Command{
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
		test = tm.getTest(test);
		test.setDescription(request.getParameter("name"));
		test.setDescription(request.getParameter("description"));
		for(Question quest:test.getQuestions()) {
			quest.setText(request.getParameter("question"+quest.getId()+"_text"));
			quest.setRac(Integer.parseInt(request.getParameter("question"+quest.getId()+"_rac")));
			for(Answer answ:quest.getAnswers()) {
				answ.setText(request.getParameter("answer"+answ.getId()+"_text"));
				int right = request.getParameter("question"+answ.getId()+"_right")==null?0:1;
				answ.setRight(right);
			}
			quest.setRac(Integer.parseInt(request.getParameter("quest"+quest.getId()+"_rac")));
		}
		int sourse_id = Integer.parseInt(request.getParameter("course_id"));
		tm.UpdateTest(test);
		request.setAttribute("test_id", test.getId());
		CourseManager cm = CourseManager.getInstance();
		Course course = new Course();
		course.setId(Integer.parseInt((String)session.getAttribute("course_id")));
		//request.setAttribute("course_id", course.getId());
		return Path.FORWARD_COURSE_EDIT+course.getId();
	}
}
