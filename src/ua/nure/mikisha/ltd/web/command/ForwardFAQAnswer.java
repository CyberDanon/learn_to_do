package ua.nure.mikisha.ltd.web.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.mikisha.ltd.Path;
import ua.nure.mikisha.ltd.db.MessagesManager;
import ua.nure.mikisha.ltd.db.entity.FAQmessage;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;

public class ForwardFAQAnswer extends Command{
	private static final long serialVersionUID = 6732286014029478505L;

	private static final Logger LOG = Logger.getLogger(ForwardChangePass.class);
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws ServerException, AppException {
		HttpSession session = request.getSession();
		int faq_id = Integer.parseInt(request.getParameter("faq_id"));
		FAQmessage faq = new FAQmessage();
		MessagesManager mm = MessagesManager.getInstance(); 
		faq.setId(faq_id);
		faq = mm.getFAQ(faq);
		request.setAttribute("faq", faq);
		return Path.PAGE_FAQ_ANSWER;
	}
}
