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

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import beans.ImageBean;
import beans.CommentBean;
import dao.ImageDAO;
import dao.CommentDAO;
import utils.ChunkExtractor;
import utils.ConnectionAndEngineHandler;
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
		connection = ConnectionAndEngineHandler.getConnection(context);
		templateEngine = ConnectionAndEngineHandler.getTemplateEngine(context);
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idAlbumString = request.getParameter("idAlbum");
		String imageIndexString = request.getParameter("imageIndex");
		String albumTitle = request.getParameter("albumTitle");
		String chunkIndexString = request.getParameter("chunkIndex");
		if(idAlbumString == null || idAlbumString.isEmpty()) {
			response.sendError(400, "Missing idAlbum parameter");
			return;
		}
		if(imageIndexString == null || imageIndexString.isEmpty()) {
			response.sendError(400, "Missing imageIndex parameter");
			return;
		}
		if(chunkIndexString == null || chunkIndexString.isEmpty()) {
			response.sendError(400, "Missing chunkIndex parameter");
			return;
		}
		int idAlbum;
		int imageIndex;
		int chunkIndex;
		try {
			chunkIndex = Integer.parseInt(chunkIndexString);
			idAlbum = Integer.parseInt(idAlbumString);
			imageIndex = Integer.parseInt(imageIndexString);
		} catch (NumberFormatException e){
			response.sendError(400, "Wrong idAlbum or imageIndex or chunkIndex type - only numeric type accepted");
			return;
		}
		
		ImageDAO imageDAO = new ImageDAO(connection);
		List<ImageBean> images = null;
		List<ImageBean> chunkOfImages = null;
		
		try {
			images = imageDAO.getImages(idAlbum);
		} catch (SQLException e) {
			response.sendError(500, "Errore extracting images from database");
			return;
		}
		ImagesScrollManager scrollManager = new ImagesScrollManager();
		if (images.size() > ImagesScrollManager.imagesChunkSize) {
			chunkOfImages = ChunkExtractor.extractChunk(images, chunkIndex, chunkIndex + ImagesScrollManager.imagesChunkSize);	
			scrollManager.setBegin(images.indexOf(chunkOfImages.get(0)) == 0);
			scrollManager.setEnd(images.indexOf(chunkOfImages.get(chunkOfImages.size() - 1)) == (images.size() - 1));
			scrollManager.setChunkIndex((chunkIndex < 0) ? 0 : ((chunkIndex > images.size() - ImagesScrollManager.imagesChunkSize) ? images.size() - ImagesScrollManager.imagesChunkSize : chunkIndex));
		} else {
			chunkOfImages = images;
			scrollManager.setBegin(true);
			scrollManager.setEnd(true);
			scrollManager.setChunkIndex(0);
		}
		
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
		if(imageIndex >= chunkOfImages.size() || imageIndex < 0) {
			ctx.setVariable("isShowingImage", false);
		} else {
			ctx.setVariable("isShowingImage", true);
			ctx.setVariable("imageIndex", imageIndex);
			CommentDAO commentDAO = new CommentDAO(connection);
			List<CommentBean> comments = null;
			try {
				comments = commentDAO.getComments(chunkOfImages.get(imageIndex).getIdImage());
			} catch (SQLException e) {
				response.sendError(500, "Errore extracting images from database");
				return;
			}
			ctx.setVariable("comments", comments);
		}
		ctx.setVariable("images", chunkOfImages);
		ctx.setVariable("scrollManager", scrollManager);
		ctx.setVariable("idAlbum", idAlbum);
		ctx.setVariable("albumTitle", albumTitle);
		
		String path = "/WEB-INF/albumPage.html";
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
