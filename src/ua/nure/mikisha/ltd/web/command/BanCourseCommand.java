package ua.nure.mikisha.ltd.web.command;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.mikisha.ltd.db.CourseManager;
import ua.nure.mikisha.ltd.db.MessagesManager;
import ua.nure.mikisha.ltd.db.SubManager;
import ua.nure.mikisha.ltd.db.UserManager;
import ua.nure.mikisha.ltd.db.entity.Course;
import ua.nure.mikisha.ltd.db.entity.Student;
import ua.nure.mikisha.ltd.db.entity.User;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;
import ua.nure.mikisha.ltd.Path;

public class BanCourseCommand extends Command{
	private static final long serialVersionUID = 7732286014029478505L;

	private static final Logger LOG = Logger.getLogger(UnbanCommand.class);
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws ServerException, AppException {
		HttpSession session = request.getSession();
		LOG.trace("Getting of users info to unban starts");
		String user =  request.getParameter("user_login");
		LOG.info("user for unban with id "+ user);
		CourseManager us = CourseManager.getInstance();
		int course_id = Integer.parseInt(request.getParameter("course_id"));
		Course course = new Course();
		course.setId(course_id);
		course = us.getCourse(course);
		us.ban_course(course);
		User owner = new Student();
		owner.setLogin(course.getOwner());
		MessagesManager mm = MessagesManager.getInstance();
		String text = request.getParameter("message");
		text = text.equals("null")?"Курс только был создан и требует верификации":text;
		text = new StringBuilder("Курс "+course.getName()+" был забанен по причине:").append(text).toString();
		mm.CreateMessage(owner, course, text, "teach");
		text = "Курс "+course.getName()+" был забанен на доработку";
		SubManager sm = SubManager.getInstance();
		List<User> followers = sm.getSubscribers(course);
		text = "Курс"+course.getName()+", на который Вы подписаны, не прошел верификацию";
		for (User u:followers) {
			mm.CreateMessage(u, course, text, "subs_banned");
		}
		mm.delete_unbanned_notif(course);
		return Path.FORWARD_GET_ALL_UNBANNED_COURCES;
	}
}