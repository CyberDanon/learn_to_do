package ua.nure.mikisha.ltd.web.command;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.mikisha.ltd.Path;
import ua.nure.mikisha.ltd.db.MessagesManager;
import ua.nure.mikisha.ltd.db.entity.Message;
import ua.nure.mikisha.ltd.db.entity.User;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;

public class GetMessagesCommand extends Command{
	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger.getLogger(GetCourseInfoCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException, AppException, ServerException {
		LOG.debug("Command starts");
		HttpSession session = request.getSession();
		LOG.trace("Getting of current user info to update starts");
		User user = (User) session.getAttribute("user");
		MessagesManager ms = MessagesManager.getInstance();
		List<Message> messages = ms.getMessagesForUser(user);
		request.setAttribute("messages", messages);
		return Path.PAGE_MESSAGES;
	}
}
