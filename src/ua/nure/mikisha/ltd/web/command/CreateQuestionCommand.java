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
import ua.nure.mikisha.ltd.db.entity.Answer;
import ua.nure.mikisha.ltd.db.entity.Question;
import ua.nure.mikisha.ltd.db.entity.Test;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;
import ua.nure.mikisha.ltd.Path;

public class CreateQuestionCommand extends Command{
	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger.getLogger(LoginCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException, AppException, ServerException {
		LOG.debug("Command starts");

		HttpSession session = request.getSession();

		// obtain login and password from a request
		int test_id = Integer.parseInt(request.getParameter("test_id"));
		Question question = new Question();
		question.setTest(test_id);
		question.setText("");
		question.setRac(0);
		QuestionsManager qm = QuestionsManager.getInstance();
		qm.CreateQuestion(question);
		TestsManager t = TestsManager.getInstance();
		Test test = new Test();
		test.setId(Integer.parseInt(request.getParameter("test_id")));
		test = t.getTest(test);
		request.setAttribute("test_id", test.getId());
		return Path.FORWARD_TEST_EDIT+"&test_id="+test.getId()+"&course_id="+(int)session.getAttribute("course_id");
	}
}