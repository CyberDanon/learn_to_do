package ua.nure.mikisha.ltd.web.command;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import ua.nure.mikisha.ltd.Path;
import ua.nure.mikisha.ltd.db.CourseManager;
import ua.nure.mikisha.ltd.db.SubManager;
import ua.nure.mikisha.ltd.db.entity.Course;
import ua.nure.mikisha.ltd.db.entity.Theorical;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;

public class GetCourseInfoCommand extends Command {
	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger.getLogger(GetCourseInfoCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException, ServerException {
		LOG.debug("Command starts");
		int course_id = Integer.parseInt(request.getParameter("course_id"));
		Course c = new Course();
		c.setId(course_id);
		CourseManager cm = CourseManager.getInstance();
		c = cm.getCourse(c);
		request.setAttribute("course", c);
		request.setAttribute("theory", c.getTh().size());
		request.setAttribute("users", SubManager.getInstance().getSubscribers(c).size());
		request.setAttribute("test", c.getTests().size());
		int tt = 0;
		for (Theorical t : c.getTh()) {
			tt += t.getTests().size();
		}
		request.setAttribute("theory_tests", tt);
		if (request.getParameter("type").equals("get")) {
			return Path.PAGE_COURSE_PREVIEW;
		} else {
			return Path.PAGE_COURSE_EDIT;
		}
	}
}
