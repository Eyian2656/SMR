package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import model.config.DbConfig;

public class BasicDBAccess {
	private Connection connection;
	private boolean connected;
	
	public boolean connect(DbConfig config) throws Exception {
		System.setProperty("oracle.net.tns_admin", "C:/oraclexe/app/oracle/product/11.2.0/server/network/ADMIN");

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

	public boolean close() {
		if (connected) {
			try {
				connection.close();
				connected = false;
			} catch (SQLException e) {
				connected = false;
			}
		}
		return connected;
	}

	public boolean isConnected() {
		return connected;
	}
}