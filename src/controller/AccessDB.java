package controller;

import java.sql.Connection;
import java.sql.DriverManager;

import model.config.DbConfig;

/**
 * Diese Klasse steuert den Datenbankzugriff. Stellt eine Connection zu einem
 * Schema mittels OJDBC Treiber.
 * 
 * @author Ian Noack
 *
 */
public class AccessDB {
	private Connection connection;
	private boolean connected;

	/**
	 * Die Funktion connect() stellt letztendlich die Verbindung zur Datenbank
	 * mittels OJDBC Treiber. Dazu wird ein Connection Objekt erstellt welches
	 * die Daten zur Verbindung mit ei-nem Schema erhält. Die Connection
	 * benötigt immer ein Link welcher bei einer Oracle Datenbank wie folgt
	 * aussieht: jdbc:oracle:thin:@ + URL + User + Passwort.
	 * 
	 * Wird eine andere Datenbank genutzt muss sowohl der JDBC Treiber als auch der Connection Link geändert werden
	 * @param config
	 *            Model Klasse mit den Attributen: URL, Passwort und User
	 * @return eine aktive Verbindung mit der Datenbank
	 * @throws Exception
	 */
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
