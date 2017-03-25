

import java.sql.*;

/**
 * Steuert die Datenbank zugriffe. Baut verbindung auf usw.
 * 
 * @author inoack
 *
 */
public class AccessV2DB {
	private static final String URL = "jdbc:oracle:thin:@//localhost:1521/xe";
	private static final String USER = "IAN_NEW_EPOSDB";
	private static final String PASSWORT = "EPOSDev";
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

	public boolean connect() {
		connected = false;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection(URL, USER, PASSWORT);
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

	public ResultSet executeQuery(String query) throws SQLException {
		if (connected) {
			Statement stmt = connection.createStatement();
			return stmt.executeQuery(query);
		} else {
			throw new SQLException("Database not connected");
		}
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
