package ua.nure.mikisha.ltd.web.command;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.mikisha.ltd.Path;
import ua.nure.mikisha.ltd.db.TestsManager;
import ua.nure.mikisha.ltd.db.entity.Answer;
import ua.nure.mikisha.ltd.db.entity.Question;
import ua.nure.mikisha.ltd.db.entity.Test;
import ua.nure.mikisha.ltd.db.entity.User;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;

public class PassTestCommand extends Command{
	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger.getLogger(LoginCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException, AppException, ServerException {
		LOG.debug("Command starts");

		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		int test_id = Integer.parseInt(request.getParameter("test_id"));
		Test test = new Test();
		test.setId(test_id);
		TestsManager tm = TestsManager.getInstance();
		test = tm.getTest(test);
		int grade = 0;
		for(Question quest:test.getQuestions()) {
			boolean fl = true;
			for(Answer answ:quest.getAnswers()) {
				int right = request.getParameter("question"+answ.getId()+"_right")==null?0:1;
				if (answ.getRight()!=right) {
					fl=false;
					break;
				}
			}
			if (fl) {
				grade +=quest.getRac();
			}
		}
		tm.pass(user, test, grade);
		String sourse = request.getParameter("source");
		int sourse_id = Integer.parseInt(request.getParameter("source_id"));
		request.setAttribute("test_id", test.getId());
		if (sourse.equals("course")) {
			return Path.FORWARD_COURSE_DISPLAY+sourse_id;
		} else {
			return Path.FORWARD_THEORY_DISPLAY+sourse_id;
		}
	}
}
