package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.AlbumBean;

public class AlbumDAO {
	
	private Connection connection;
	
	public AlbumDAO(Connection connection) {
		this.connection = connection;
	}
	
	public List<AlbumBean> getAlbum() throws SQLException {
		List<AlbumBean> albumCollection = new ArrayList<AlbumBean>();
		String query = "SELECT idAlbum, title, date "
						+ "FROM album "
						+ "ORDER BY date DESC";
		ResultSet result = null;
		PreparedStatement preparedstatement = null;		
		preparedstatement = connection.prepareStatement(query);
		result = preparedstatement.executeQuery();
		while (result.next()) {
			AlbumBean album = new AlbumBean();
			album.setIdAlbum(result.getInt("idAlbum"));
			album.setTitle(result.getString("title"));
			album.setDate(result.getDate("date"));
			albumCollection.add(album);
		}
		try {
			result.close();
			preparedstatement.close();
		} catch (Exception e) {
			throw new SQLException(e);
		}
		return albumCollection;
	}
}
