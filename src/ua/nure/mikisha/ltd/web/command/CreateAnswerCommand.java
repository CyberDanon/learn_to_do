package ua.nure.mikisha.ltd.web.command;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.mikisha.ltd.db.AnswersManager;
import ua.nure.mikisha.ltd.db.QuestionsManager;
import ua.nure.mikisha.ltd.db.TestsManager;
import ua.nure.mikisha.ltd.db.UserManager;
import ua.nure.mikisha.ltd.db.entity.Answer;
import ua.nure.mikisha.ltd.db.entity.Question;
import ua.nure.mikisha.ltd.db.entity.Test;
import ua.nure.mikisha.ltd.db.entity.User;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;
import ua.nure.mikisha.ltd.Path;

public class CreateAnswerCommand extends Command{
	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger.getLogger(LoginCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException, AppException, ServerException {
		LOG.debug("Command starts");

		HttpSession session = request.getSession();

		// obtain login and password from a request
		int question_id = Integer.parseInt(request.getParameter("question_id"));
		Answer answ = new Answer();
		answ.setQuestion(question_id);
		answ.setText("");
		answ.setRight(0);
		AnswersManager am = AnswersManager.getInstance();
		am.CreateAnswer(answ);
		TestsManager t = TestsManager.getInstance();
		Test test = new Test();
		test.setId(Integer.parseInt(request.getParameter("test_id")));
		test = t.getTest(test);
		request.setAttribute("test_id", test.getId());
		return Path.FORWARD_TEST_EDIT+"&test_id="+test.getId()+"&course_id="+(int)session.getAttribute("course_id");
	}
}
