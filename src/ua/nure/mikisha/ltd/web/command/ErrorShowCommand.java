package ua.nure.mikisha.ltd.web.command;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;


import ua.nure.mikisha.ltd.Path;
import ua.nure.mikisha.ltd.exception.AppException;
import ua.nure.mikisha.ltd.exception.ServerException;
/**
 * Command to operate with attributes of filtration and sending it to it's direction
 * @author Mikisha Danylo
 *
 */
public class ErrorShowCommand extends Command{
	private static final long serialVersionUID = 7732286011029478505L;

	private static final Logger LOG = Logger.getLogger(ErrorShowCommand.class);
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws ServerException, AppException {
		LOG.trace("Forward to error page");
		request.setAttribute("errorMessage", request.getParameter("errorMessage"));
		return Path.PAGE_ERROR_PAGE;
	}
}