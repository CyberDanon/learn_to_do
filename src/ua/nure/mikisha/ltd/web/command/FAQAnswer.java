package ua.nure.mikisha.ltd.web.command;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.mikisha.ltd.Path;
import ua.nure.mikisha.ltd.db.MessagesManager;
import ua.nure.mikisha.ltd.db.entity.FAQmessage;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;

public class FAQAnswer extends Command{
	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger.getLogger(LoginCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException, AppException, ServerException {
		LOG.debug("Command starts");

		HttpSession session = request.getSession();

		// obtain login and password from a request
		
		String faq_text = request.getParameter("answer");
		int faq_id = Integer.parseInt("faq_id");
		FAQmessage faq = new FAQmessage();
		MessagesManager mm = MessagesManager.getInstance(); 
		faq.setText(faq_text);
		faq.setId(faq_id);
		mm.UpdateFAQ(faq);
		return Path.FORWARD_FAQ;
	}
}
