package ua.nure.mikisha.ltd.web.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.mikisha.ltd.db.MessagesManager;
import ua.nure.mikisha.ltd.db.entity.FAQmessage;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;
import ua.nure.mikisha.ltd.Path;

public class CreateFAQ extends Command{
	private static final long serialVersionUID = 6732286014029478505L;

	private static final Logger LOG = Logger.getLogger(ForwardChangePass.class);
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws ServerException, AppException {
		HttpSession session = request.getSession();
		String faq_text = request.getParameter("question");
		FAQmessage faq = new FAQmessage();
		MessagesManager mm = MessagesManager.getInstance(); 
		faq.setText(faq_text);
		mm.CreateFAQ(faq);
		return Path.FORWARD_FAQ;
	}
}
