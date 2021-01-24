package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import beans.CommentBean;

public class CommentDAO {
	
	private Connection connection;
	
	public CommentDAO(Connection connection) {
		this.connection = connection;
	}
	
	public void insertComment(String text, int image, int user) throws SQLException {
		String query = "INSERT INTO comments (text, image, user) VALUES (?, ?, ?)";
		PreparedStatement preparedstatement = null;	
		try {
			preparedstatement = connection.prepareStatement(query);
			preparedstatement.setString(1, text);
			preparedstatement.setInt(2, image);
			preparedstatement.setInt(3, user);
			preparedstatement.executeUpdate();
			preparedstatement.close();
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}
	
	public List<CommentBean> getComments(int image) throws SQLException{
		List<CommentBean> comments = new ArrayList<CommentBean>();
		String query = "SELECT C.text, U.userName "
				+ "FROM comments C JOIN user U ON C.user = U.idUser "
				+ "WHERE C.image = ? ";
		ResultSet result = null;
		PreparedStatement preparedstatement = null;	
		try {
			preparedstatement = connection.prepareStatement(query);
			preparedstatement.setInt(1, image);
			result = preparedstatement.executeQuery();
			while (result.next()) {
				CommentBean comment = new CommentBean();
				comment.setText(result.getString("text"));
				comment.setUserName(result.getString("userName"));
				comments.add(comment);
			}
			result.close();
			preparedstatement.close();
		} catch (Exception e) {
			throw new SQLException(e);
		}
		return comments;
	}
}
