package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.AlbumBean;
import exceptions.AlbumNotFoundException;

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
	
	public String getAlbumTitle(int albumId) throws SQLException, AlbumNotFoundException {
		String albumTitle = null;
		String query = "SELECT title "
				+ "FROM album "
				+ "WHERE idAlbum = ?";
		ResultSet result = null;
		PreparedStatement preparedstatement = null;		
		preparedstatement = connection.prepareStatement(query);
		preparedstatement.setInt(1, albumId);
		result = preparedstatement.executeQuery();
		
		if (result.isBeforeFirst()) {
			result.next();
			albumTitle = result.getString("title");		
		}
		try {
			result.close();
			preparedstatement.close();
		} catch (Exception e) {
			throw new SQLException(e);
		}
		if(albumTitle == null) {
			throw new AlbumNotFoundException();
		}
		return albumTitle;
	}
}
