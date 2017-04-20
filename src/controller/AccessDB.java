package controller;

import java.sql.Connection;
import java.sql.DriverManager;

import model.config.DbConfig;

/**
 * Diese Klasse steuert den Datenbankzugriff. Stellt eine Connection zu einem Schema.
 * 
 * @author Ian Noack
 *
 */
public class AccessDB {
	private Connection connection;
	private boolean connected;

	public boolean connect(DbConfig config) throws Exception {

		connected = false;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + config.getUrl(), config.getUsername(),
					config.getPassword());
			connected = true;
		} catch (Exception e) {
			connected = false;
			throw e;
		}
		return connected;
	}

	public Connection getConnection() {
		return this.connection;
	}

	public boolean isConnected() {
		return connected;
	}
}
