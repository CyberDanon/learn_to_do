package ua.nure.mikisha.ltd.web.command;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.mikisha.ltd.Path;
import ua.nure.mikisha.ltd.db.TheoryBlocksManager;
import ua.nure.mikisha.ltd.db.entity.Theorical;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;

public class ForwardToUpdateTh  extends Command{
	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger.getLogger(LoginCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException, ServerException {
		LOG.debug("Command starts");

		HttpSession session = request.getSession();
		TheoryBlocksManager c = TheoryBlocksManager.getInstance();
		Theorical th = new Theorical();
		th.setId(Integer.parseInt(request.getParameter("theory_id")));
		th = c.getTheory(th);
		request.setAttribute("theory", th);
		return Path.PAGE_THEORY_EDIT;
	}
}
