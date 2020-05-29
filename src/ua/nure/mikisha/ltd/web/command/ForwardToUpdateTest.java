package ua.nure.mikisha.ltd.web.command;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import ua.nure.mikisha.ltd.Path;
import ua.nure.mikisha.ltd.db.TestsManager;
import ua.nure.mikisha.ltd.db.entity.Test;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;

public class ForwardToUpdateTest extends Command{
	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger.getLogger(LoginCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException, ServerException {
		LOG.debug("Command starts");

		HttpSession session = request.getSession();
		TestsManager t = TestsManager.getInstance();
		Test test = new Test();
		test.setId(Integer.parseInt(request.getParameter("test_id")));
		test = t.getTest(test);
		request.setAttribute("test", test);
		session.setAttribute("course_id", Integer.parseInt(request.getParameter("course_id")));
		return Path.PAGE_TEST_EDIT;
	}
}
