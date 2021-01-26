package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import beans.AlbumBean;
import beans.UserBean;
import dao.AlbumDAO;
import utils.ConnectionAndEngineHandler;

@WebServlet("/HomePage")
public class HomePage extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;
       
    public HomePage() {
        super();
    }
    
    public void init() throws ServletException{
		ServletContext context = getServletContext();
		connection = ConnectionAndEngineHandler.getConnection(context);
		templateEngine = ConnectionAndEngineHandler.getTemplateEngine(context);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		AlbumDAO albumDAO = new AlbumDAO(connection);
		List<AlbumBean> album = null;
		
		try {
			album = albumDAO.getAlbum();
		} catch(SQLException e) {
			response.sendError(500, "Errore extracting album from database");
			return;
		}
		
		String path = "/WEB-INF/homePage.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("album", album);
		ctx.setVariable("userName", user.getUserName());
		templateEngine.process(path, ctx, response.getWriter());	
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendError(404, "Make GET request");
		return;
	}
	
	public void destroy() {
		try {
			ConnectionAndEngineHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
