package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.UserBean;
import dao.CommentDAO;
import utils.ConnectionAndEngineHandler;

@WebServlet("/Comment")
public class Comment extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
       
    public Comment() {
        super();
    }
    
    public void init() throws ServletException{
    	connection = ConnectionAndEngineHandler.getConnection(getServletContext());
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendError(HttpServletResponse.SC_NOT_FOUND, "Make POST request");
		return;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		
		String text = request.getParameter("text");
		String idImageString = request.getParameter("idImage");
		String idAlbumString = request.getParameter("idAlbum");
		String imageIndexString = request.getParameter("imageIndex");
		String chunkIndexString = request.getParameter("chunkIndex");
		if(text == null || text.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing text");
			return;
		}
		if(idImageString == null || idImageString.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing idImage");
			return;
		}
		if(idAlbumString == null || idAlbumString.isEmpty() || imageIndexString == null || imageIndexString.isEmpty()
				|| chunkIndexString == null || chunkIndexString.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad params request");
			return;
		}
		int idImage;
		int idAlbum;
		int imageIndex;
		int chunkIndex;
		try {
			idImage = Integer.parseInt(idImageString);
			idAlbum = Integer.parseInt(idAlbumString);
			imageIndex = Integer.parseInt(imageIndexString);
			chunkIndex = Integer.parseInt(chunkIndexString);
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Wrong params type");
			return;
		}
		
		CommentDAO commentDAO = new CommentDAO(connection);
		try {
			commentDAO.insertComment(text, idImage, user.getIdUser());
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error creating a comment");
			return;
		}
		
		
		String path = getServletContext().getContextPath();
		String target = "/AlbumPage"
				+ "?idAlbum=" + idAlbum
				+ "&imageIndex=" + imageIndex
				+ "&chunkIndex=" + chunkIndex;
		response.sendRedirect(path + target);	
	}
	
	public void destroy() {
		try {
			ConnectionAndEngineHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
}
