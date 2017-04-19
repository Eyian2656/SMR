package controller;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import model.Column;
import model.TableName;
import model.config.DbConfig;
import view.MainView;

/**
 * Steuert die MainView und die Informationen
 * 
 * @author Ian
 *
 */
public class MainController {
	private AccessDB accessNewDb;
	private AccessDB accessOldDb;
	private MainView mainView;

	public MainController() {
		this.accessNewDb = new AccessDB();
		this.accessOldDb = new AccessDB();
		this.mainView = new MainView(this);
	}

	/**
	 * Startet das Abbilden der View
	 */
	public void showMainView() {
		mainView.setVisible(true);
	}

	/**
	 * Versteckt die View
	 */
	public void hideMainView() {
		mainView.setVisible(false);
	}

	/**
	 * Verbindet die Datenbank und startet der die nächste View
	 * 
	 * @param oldDbConfig
	 * @param newDbConfig
	 * @throws SQLException
	 */

	public void start(DbConfig oldDbConfig, DbConfig newDbConfig, File file) {
		Connection oldConnection = null;
		Connection newConnection = null;
		try {
			accessOldDb.connect(oldDbConfig);
			accessNewDb.connect(newDbConfig);

			oldConnection = accessOldDb.getConnection();
			newConnection = accessNewDb.getConnection();

			// Validierung von der Verbindung
			if (accessOldDb.isConnected() && accessNewDb.isConnected()) {
				this.execute(file);
				hideMainView();
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Verbindung mit der DB wurde abgelehnt. Log : " + e.getMessage());
		} finally {
			if (oldConnection != null) {
				try {
					oldConnection.close();
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Error bei alte DB mit der Nachricht: " + e.getMessage());
				}
			}
			if (newConnection != null) {
				try {
					newConnection.close();
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Error bei neue DB mit der Nachricht : " + e.getMessage());
				}
			}
		}
	}

	/**
	 * Erstellt TableCrawler und TableComparer. Hier wird die Funktion
	 * aufgerufen um die Tabellen zu kontrollieren. Die Tabellennamen werden aus
	 * dem Klasse TableName gezogen.
	 * 
	 * @param file
	 */
	private void execute(File file) {
		TableCrawler tableCrawler = new TableCrawler();
		TableComparer tableComparer = new TableComparer(file);
		List<String> toBeCheckedTable = TableName.list;

		Connection oldConnection = accessOldDb.getConnection();
		Connection newConnection = accessNewDb.getConnection();

		// Hier werden alle Tabellen durchiteriert um diese als string an
		// die einzelnen Methoden zu übergeben
		try {
			for (String string : toBeCheckedTable) {
				System.out.println("Now checking table " + string);

				// mittels TableCrawler werden die Metadaten aus einer Tabelle
				// gezogen in in ein Objekt gespeicher welches
				// dann vergleichen werden kann um die unterschiede
				// festzustellen
				List<Column> oldColumn = tableCrawler.crawlColumns(oldConnection, string);
				List<Column> newColumn = tableCrawler.crawlColumns(newConnection, string);
				tableComparer.differColumn(oldColumn, newColumn, string, oldConnection, newConnection);

				System.out.println("=============================");
			}

			if (file.exists()) {
				JOptionPane.showMessageDialog(null, "Erfolgreich ausgeführt. Das Updateskript wurde in '"
						+ file.getAbsolutePath() + "' gespeichert");

			} else {
				JOptionPane.showMessageDialog(null, "Schemas sind kongruent. Es wurde kein Updateskript erstellt.");
			}

			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error Message : " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Schließt die DB verbindung
	 */
	public void closeConnection() {
		try {
			if (accessOldDb.getConnection() != null) {
				accessOldDb.close();
			}
			if (accessNewDb.getConnection() != null) {
				accessNewDb.getConnection().close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
