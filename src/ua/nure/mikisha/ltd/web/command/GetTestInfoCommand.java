package ua.nure.mikisha.ltd.web.command;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.nure.mikisha.ltd.Path;
import ua.nure.mikisha.ltd.db.TestsManager;
import ua.nure.mikisha.ltd.db.entity.Test;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;

public class GetTestInfoCommand extends Command{
	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger.getLogger(GetCourseInfoCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException, AppException, ServerException {
		LOG.debug("Command starts");
		int course_id = Integer.parseInt(request.getParameter("test_id"));
		Test test = new Test();
		test.setId(course_id);
		TestsManager tm = TestsManager.getInstance();
		test = tm.getTest(test);
		request.setAttribute("test", test);
		return Path.PAGE_TEST_EDIT;
	}
}
