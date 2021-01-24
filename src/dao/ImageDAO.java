package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.ImageBean;

public class ImageDAO {
	
	private Connection connection;
	
	public ImageDAO(Connection connection) {
		this.connection = connection;
	}
	
	public List<ImageBean> getImages(int idAlbum) throws SQLException {
		List<ImageBean> imagesCollection = new ArrayList<ImageBean>();
		String query = "SELECT I.idImage, I.title, I.date, I.description, I.path "
				+ "FROM album A JOIN image_album IA ON A.idAlbum = IA.album "
				+ "JOIN image I ON I.idImage = IA.image "
				+ "WHERE A.idAlbum = ? "
				+ "ORDER BY I.date DESC";
		ResultSet result = null;
		PreparedStatement preparedstatement = null;		
		preparedstatement = connection.prepareStatement(query);
		preparedstatement.setInt(1, idAlbum);
		result = preparedstatement.executeQuery();
		while (result.next()) {
			ImageBean image = new ImageBean();
			image.setIdImage(result.getInt("idImage"));
			image.setTitle(result.getString("title"));
			image.setDate(result.getDate("date"));
			image.setDescription(result.getString("description"));
			image.setPath(result.getString("path"));
			imagesCollection.add(image);
		}
		try {
			result.close();
			preparedstatement.close();
		} catch (Exception e) {
			throw new SQLException(e);
		}
		return imagesCollection;
	}
}
