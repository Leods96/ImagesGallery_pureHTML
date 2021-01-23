package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.UtenteBean;
import dao.LoginDAO;

@WebServlet("/Login")
public class Login extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
       
    public Login() {
        super();
    }
    
    public void init() throws ServletException{
    	try {
    		ServletContext context = getServletContext();
    		String driver = context.getInitParameter("dbDriver");
    		String url = context.getInitParameter("dbUrl");
    		String user = context.getInitParameter("dbUser");
    		String password = context.getInitParameter("dbPassword");
    		Class.forName(driver);
    		
    		//Connessione con il database
    		connection = DriverManager.getConnection(url, user, password);
    	}
    	catch (ClassNotFoundException e) {
    		throw new UnavailableException("Cannot load db driver");
		}
    	catch (SQLException e) {
    		throw new UnavailableException("Cannot connect to db");
		}
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendError(404, "Effetturare richiesta POST");
		return;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if(username == null || password == null || username.isEmpty() || password.isEmpty()) {
			response.sendError(400, "Parametri login mancanti");
			return;
		}

		LoginDAO loginDAO = new LoginDAO(connection);
		UtenteBean user = null;
		
		try {
			user = loginDAO.checkCredentials(username, password);
		} catch (SQLException e) {
			response.sendError(500, "Errore nell'estrazione delle credenziali dal database");
			return;
		}
		
		//Redirect alle servlet che gestiscono le home pages
		String path = getServletContext().getContextPath();
		String target = null;
		if (user == null) { //Login fallito
			target = "/index.html";
		} else { //Login ok
			request.getSession().setAttribute("user", user); //User salvato nella sessione del client.
			target = "/HomePage";
		}
		response.sendRedirect(path + target);	
	}
	
	public void destroy() {
		try {
			if (connection != null) {
				connection.close();
			}
		}
		catch (SQLException sqle) {
		}
	}

}
