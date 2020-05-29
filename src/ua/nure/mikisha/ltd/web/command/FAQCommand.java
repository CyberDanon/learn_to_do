package ua.nure.mikisha.ltd.web.command;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.mikisha.ltd.Path;
import ua.nure.mikisha.ltd.db.MessagesManager;
import ua.nure.mikisha.ltd.db.entity.FAQmessage;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;

public class FAQCommand extends Command {
	long serialVersionUID = 7732286014029478505L;

	private static final Logger LOG = Logger.getLogger(UnbanCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServerException, AppException {
		HttpSession session = request.getSession();
		String user = (String) session.getAttribute("role");
		List <FAQmessage> mes = new LinkedList();
		MessagesManager mm = MessagesManager.getInstance();
		if (user.equals("admin")) {
			mes = mm.get_unaswered_faq();
		} else {
			mes = mm.get_aswered_faq();
		}
		request.setAttribute("faq", mes);
		return Path.PAGE_FAQ;
	}
}
