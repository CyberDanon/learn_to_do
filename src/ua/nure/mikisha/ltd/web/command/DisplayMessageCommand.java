package ua.nure.mikisha.ltd.web.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.mikisha.ltd.Path;
import ua.nure.mikisha.ltd.db.MessagesManager;
import ua.nure.mikisha.ltd.db.entity.Message;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;

public class DisplayMessageCommand extends Command {
	private static final long serialVersionUID = 7732286014029478501L;

	private static final Logger LOG = Logger.getLogger(DonateUserCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServerException, AppException {
		HttpSession session = request.getSession();
		LOG.trace("Getting of current user info to update starts");
		int mes_id = Integer.parseInt(request.getParameter("message_id"));
		Message mes = new Message();
		MessagesManager mm = MessagesManager.getInstance();
		mes.setId(mes_id);
		mes = mm.getMessage(mes);
		request.setAttribute("message", mes);
		return Path.PAGE_DISPLAY_MESSAGE;
	}
}
