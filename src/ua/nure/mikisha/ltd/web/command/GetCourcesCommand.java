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
import ua.nure.mikisha.ltd.db.CourseManager;
import ua.nure.mikisha.ltd.db.SubManager;
import ua.nure.mikisha.ltd.db.entity.Course;
import ua.nure.mikisha.ltd.db.entity.Student;
import ua.nure.mikisha.ltd.db.entity.Teacher;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;

public class GetCourcesCommand extends Command {
	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger.getLogger(GetCourseInfoCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException, ServerException {
		LOG.debug("Command starts");
		HttpSession session = request.getSession();
		Student user = (Student) session.getAttribute("user");
		Teacher t = (Teacher) user;
		String type = request.getParameter("type");
		type = type.equals("null")?"default":type;
		CourseManager sub = CourseManager.getInstance();
		SubManager s = SubManager.getInstance();
		List<Course> c = new LinkedList();
		switch (type) {
		case "subs": {
			c = sub.getCoursesForStudent(user);
			break;
		}
		case "teach": {
			c = sub.getCoursesForTeacher(t);
			break;
		}
		case "recoms": {
			c = s.getRecoms(user);
			break;
		}
		case "admin_banned": {
			c = sub.admin_banned();
			break;
		}
		case "admin_unbanned": {
			c = sub.admin_unbanned();
			break;
		}
		case "default": {
			String role = (String)session.getAttribute("role");
			switch (role) {
			case "admin": {c = sub.get_all(); type="admin_all"; break;}
			case "student":{c = sub.getCoursesForStudent(user);  type="subs";break;}
			case "teacher":{c = c = sub.getCoursesForTeacher(t);type="teach";}
			}
			break;
		}
		}
		request.setAttribute("type", type);
		request.setAttribute("cources", c);
		return Path.PAGE_COURCES;
	}
}
