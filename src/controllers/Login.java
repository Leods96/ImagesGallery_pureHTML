package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import utils.ConnectionAndEngineHandler;

import beans.UserBean;
import dao.LoginDAO;

@WebServlet("/Login")
public class Login extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;
       
    public Login() {
        super();
    }
    
    public void init() throws ServletException {
    	connection = ConnectionAndEngineHandler.getConnection(getServletContext());
		templateEngine = ConnectionAndEngineHandler.getTemplateEngine(getServletContext());
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendError(HttpServletResponse.SC_NOT_FOUND, "Make POST request");
		return;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if(username == null || password == null || username.isEmpty() || password.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing Login parameters");
			return;
		}

		LoginDAO loginDAO = new LoginDAO(connection);
		UserBean user = null;
		
		try {
			user = loginDAO.checkCredentials(username, password);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error extracting credentials from database");
			return;
		}
		
		String path = null;
		if (user == null) {
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("errorMsg", "Incorrect credentials");
			path = "/index.html";
			templateEngine.process(path, ctx, response.getWriter());
		} else {
			request.getSession().setAttribute("user", user);
			String target = "/HomePage";
			path = getServletContext().getContextPath();
			response.sendRedirect(path + target);	
		}
	}
	
	public void destroy() {
		try {
			ConnectionAndEngineHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

}
