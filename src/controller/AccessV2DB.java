package controller;

import java.sql.*;

import model.config.DbConfig;

/**
 * Diese Klasse steuert den Datenbankzugriff. Stellt eine Connection zum alten
 * Schema her.
 * 
 * @author inoack
 *
 */
public class AccessV2DB {
	private Connection connection;
	private boolean connected;

	private static AccessV2DB instance;

	public static AccessV2DB getInstance() {
		if (instance == null) {
			instance = new AccessV2DB();
		}
		return instance;
	}

	private AccessV2DB() {
		super();
	}

	public boolean connect(DbConfig config) {
		System.setProperty("oracle.net.tns_admin", "C:/oraclexe/app/oracle/product/11.2.0/server/network/ADMIN");

		connected = false;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + config.getUrl(), config.getUsername(),
					config.getPassword());
			connected = true;
		} catch (ClassNotFoundException e1) {
			connected = false;
			e1.printStackTrace();
		} catch (SQLException e) {
			connected = false;
			e.printStackTrace();
		}
		System.out.println("DB 2 ist verbunden = " + connected);
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
