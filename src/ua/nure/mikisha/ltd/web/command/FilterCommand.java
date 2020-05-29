package ua.nure.mikisha.ltd.web.command;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.mikisha.ltd.Path;
import ua.nure.mikisha.ltd.db.CourseManager;
import ua.nure.mikisha.ltd.db.entity.Course;
import ua.nure.mikisha.ltd.db.entity.User;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;
/**
 * Command to operate with attributes of filtration and sending it to it's direction
 * @author Mikisha Danylo
 *
 */
public class FilterCommand extends Command{
	private static final long serialVersionUID = 7732286011029478505L;

	private static final Logger LOG = Logger.getLogger(FilterCommand.class);
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws ServerException, AppException {
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		String name = request.getParameter("name");
		String type = request.getParameter("type");
		List<Course> c = new LinkedList();
		CourseManager cm = CourseManager.getInstance();
		switch (type) {
		case "admin_unbanned":{c = cm.admin_filter_unbanned(user, name);break;}
		case "admin_banned":{c = cm.admin_filter_banned(user, name);break;}
		case "recoms":{c = cm.recom_filter(user, name); break;}
		case "subs":{c = cm.student_filter(user, name);break;}
		case "teach":{c = cm.teacher_filter(user, name); break;}
		case "admin_all":{c = cm.admin_filter_all(user, name); break;}
		}
		request.setAttribute("type",type);
		request.setAttribute("cources", c);
		return Path.PAGE_COURCES;
	}
}