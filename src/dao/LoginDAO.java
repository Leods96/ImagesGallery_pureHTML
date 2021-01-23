package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import beans.UtenteBean;

public class LoginDAO {
	private Connection connection;

	public LoginDAO(Connection connection) {
		this.connection = connection;
	}

	public UtenteBean checkCredentials(String username, String password) throws SQLException {
		ResultSet result = null;
		PreparedStatement preparedstatement = null;
		UtenteBean user = null;
		
		String query = "SELECT idUser, userName, role, email FROM user WHERE userName = ? AND password = ?";
		preparedstatement = connection.prepareStatement(query);
		preparedstatement.setString(1, username);
		preparedstatement.setString(2, password);
		result = preparedstatement.executeQuery();
		
		if (result.isBeforeFirst()) { //Credenziali presenti nel db
			result.next();
			user = new UtenteBean();
			user.setIdUtente(result.getInt("idUtente"));
			user.setUserName(result.getString("userName"));
			user.setEmail(result.getString("email"));
		}
		try {
			result.close();
			preparedstatement.close();
		}
		catch (Exception e) {
			throw new SQLException(e);
		}
		return user;
	}
	
}


