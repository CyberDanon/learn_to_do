package ua.nure.mikisha.ltd.web.command;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.mikisha.ltd.Path;
import ua.nure.mikisha.ltd.db.CourseManager;
import ua.nure.mikisha.ltd.db.TheoryBlocksManager;
import ua.nure.mikisha.ltd.db.entity.Course;
import ua.nure.mikisha.ltd.db.entity.Theorical;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;


public class CreateTheoryCommand extends Command{
	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger.getLogger(LoginCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException, ServerException {
		LOG.debug("Command starts");
		HttpSession session = request.getSession();
		int course_id = Integer.parseInt(request.getParameter("course_id"));
		Theorical th = new Theorical();
		th.setCourse_id(course_id);
		th.setName("TheoryBlock");
		th.setText("Text");
		TheoryBlocksManager tm = TheoryBlocksManager.getInstance();
		tm.CreateTheory(th);
		CourseManager cm = CourseManager.getInstance();
		Course course = new Course();
		course.setId(course_id);
		course = cm.getCourse(course);
		request.setAttribute("course", course);
		return Path.FORWARD_COURSE_EDIT+"&method=GET";
	}
}
