package ua.nure.mikisha.ltd.web.command;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.nure.mikisha.ltd.Path;
import ua.nure.mikisha.ltd.db.TheoryBlocksManager;
import ua.nure.mikisha.ltd.db.entity.Theorical;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;

public class GetTheoryInfoCommand extends Command{
	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger.getLogger(GetCourseInfoCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException, AppException, ServerException {
		LOG.debug("Command starts");
		int th_id = Integer.parseInt(request.getParameter("theory_id"));
		Theorical theory = new Theorical();
		theory.setId(th_id);
		TheoryBlocksManager tm = TheoryBlocksManager.getInstance();
		theory = tm.getTheory(theory);
		request.setAttribute("theory", theory);
		return Path.PAGE_THEORY_EDIT;
	}
}
