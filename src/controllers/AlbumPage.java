package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import beans.ImageBean;
import beans.AlbumBean;
import dao.ImageDAO;
import utils.ChunkExtractor;
import utils.ImagesScrollManager;

@WebServlet("/AlbumPage")
public class AlbumPage extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;
       
    public AlbumPage() {
        super();
    }
    
    public void init() throws ServletException{
		ServletContext context = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(context);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
    	try {
    		String driver = context.getInitParameter("dbDriver");
    		String url = context.getInitParameter("dbUrl");
    		String user = context.getInitParameter("dbUser");
    		String password = context.getInitParameter("dbPassword");
    		Class.forName(driver);
    		
    		connection = DriverManager.getConnection(url, user, password);
    	} catch (ClassNotFoundException e) {
    		throw new UnavailableException("Cannot load db driver");
		} catch (SQLException e) {
    		throw new UnavailableException("Cannot connect to db");
		}
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idAlbumString = request.getParameter("idAlbum");
		String albumTitle = request.getParameter("albumTitle");
		if(idAlbumString == null || idAlbumString.isEmpty()) {
			response.sendError(400, "Missing idAlbum parameter");
			return;
		}
		int idAlbum;
		try {
			idAlbum = Integer.parseInt(idAlbumString);
		} catch (NumberFormatException e){
			response.sendError(400, "Wrong idAlbum type - must be numeric");
			return;
		}
		
		ImageDAO imageDAO = new ImageDAO(connection);
		List<ImageBean> images = null;
		
		try {
			images = ChunkExtractor.extractChunk(imageDAO.getImages(idAlbum), 0, ImagesScrollManager.imagesChunkSize);
		} catch (SQLException e) {
			response.sendError(500, "Errore extracting images from database");
			return;
		}
		
		ImagesScrollManager scrollManager = new ImagesScrollManager();
		scrollManager.setBegin(true);
		scrollManager.setEnd(images.size() <= ImagesScrollManager.imagesChunkSize - 1);
		scrollManager.setChunkNumber(1);
		
		
		request.getSession().setAttribute("idAlbum", idAlbum);
		request.getSession().setAttribute("albumTitle", albumTitle);
		
		String path = "/WEB-INF/albumPage.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("images", images);
		ctx.setVariable("scrollManager", scrollManager);
		templateEngine.process(path, ctx, response.getWriter());	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendError(404, "Make GET request");
		return;
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
