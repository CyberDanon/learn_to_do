package ua.nure.mikisha.ltd.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;
import ua.nure.mikisha.ltd.web.command.Command;
import ua.nure.mikisha.ltd.web.command.CommandContainer;
import ua.nure.mikisha.ltd.Path;

/**
 * Main servlet controller.
 * 
 * @author Mikisha Danylo
 * 
 */
public class Controller extends HttpServlet {

	private static final long serialVersionUID = 2423353715955164816L;

	private static final Logger LOG = Logger.getLogger(Controller.class);
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String forward = process(request, response);
		request.getRequestDispatcher(forward).forward(request, response);
		
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String forward = process(request, response);
		response.sendRedirect(forward);
		//request.getRequestDispatcher(forward).forward(request, response);
	}

	/**
	 * Main method of this controller.
	 * 
	 * @throws ServerException
	 */
	private String process(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		LOG.debug("Controller starts");

		// extract command name from the request
		String commandName = request.getParameter("command");
		LOG.trace("Request parameter: command --> " + commandName);

		// obtain command object by its name
		Command command = CommandContainer.get(commandName);
		LOG.trace("Obtained command --> " + command);

		// execute command and get forward address
		String forward = Path.PAGE_ERROR_PAGE;
		try {
			forward = command.execute(request, response);
		} catch (Exception ex) {
			ex.printStackTrace();
			request.setAttribute("errorMessage", ex.getMessage());
			return "controller?command=error&method=GET&errorMessage="+ex.getMessage();
		}
		LOG.trace("Forward address --> " + forward);

		LOG.debug("Controller finished, now go to forward address --> " + forward);

		// go to forward
		return forward;
	}

}