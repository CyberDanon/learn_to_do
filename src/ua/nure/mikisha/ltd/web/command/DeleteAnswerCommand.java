package ua.nure.mikisha.ltd.web.command;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;


import ua.nure.mikisha.ltd.Path;
import ua.nure.mikisha.ltd.db.AnswersManager;
import ua.nure.mikisha.ltd.db.entity.Answer;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;

public class DeleteAnswerCommand extends Command{
	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger.getLogger(GetCourseInfoCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException, AppException, ServerException {
		LOG.debug("Command starts");
		HttpSession session = request.getSession();
		int answer_id = Integer.parseInt(request.getParameter("answer_id"));
		Answer answ = new Answer();
		answ.setId(answer_id);
		AnswersManager am = AnswersManager.getInstance();
		am.DeleteAnswer(answ);
		int test_id = Integer.parseInt(request.getParameter("test_id"));
		request.setAttribute("test_id", test_id);
		return Path.FORWARD_TEST_EDIT+"&test_id="+test_id+"&course_id="+(int)session.getAttribute("course_id");
	}
}
