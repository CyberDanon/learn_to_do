package ua.nure.mikisha.ltd.web.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.mikisha.ltd.db.CourseManager;
import ua.nure.mikisha.ltd.db.entity.Course;
import ua.nure.mikisha.ltd.db.entity.User;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;
import ua.nure.mikisha.ltd.Path;

public class CreateCourseCommand extends Command {
	long serialVersionUID = 7732286014029478505L;

	private static final Logger LOG = Logger.getLogger(UnbanCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServerException, AppException {
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		Course course = new Course();
		course.setName(name);
		course.setDescription(description);
		course.setPrice(0);
		course.setOwner(user.getLogin());
		CourseManager cm = CourseManager.getInstance();
		cm.CreateCourse(course);
		course = cm.getCourse(course);
		return Path.FORWARD_GET_COURSES;
	}
}
