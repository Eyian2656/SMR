package controller;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLRecoverableException;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;

import model.Column;
import model.TableName;
import model.config.DbConfig;
import view.MainView;

/**
 * Steuert die MainView und führt das Programm aus in dem es die Funktionen der anderen Klassen aufruft.
 * 
 * @author Ian Noack
 *
 */
public class MainController {
	private AccessDB accessSourceDb;
	private AccessDB accessTargetDb;
	private MainView mainView;

	public MainController() {
		this.accessSourceDb = new AccessDB();
		this.accessTargetDb = new AccessDB();
		this.mainView = new MainView(this);
	}

	/**
	 * Startet das Abbilden der View
	 */
	public void showMainView() {
		mainView.setVisible(true);
	}

	/**
	 * Erstellt mittels Objekt eine Verbindung zu Ziel und Quellen Schema
	 * 
	 * @param targetDbConfig Model Klasse enthält Ziel DB Daten.
	 * @param sourceDbConfig Model Klasse enthält Quelle DB Daten.
	 * @throws SQLException
	 */
	public boolean connect(DbConfig targetDbConfig, DbConfig sourceDbConfig, String tnsPath) {
		// Wird benötigt wenn kein localhost verwendet wird sondern nur localhost.
		if (!StringUtils.isBlank(tnsPath)) {
			System.setProperty("oracle.net.tns_admin", tnsPath);
		}

		try {
			accessTargetDb.connect(targetDbConfig);
			accessSourceDb.connect(sourceDbConfig);

			// Validierung von der Verbindung
			if (accessTargetDb.isConnected() && accessSourceDb.isConnected()) {
				return true;
			}
		} catch (SQLRecoverableException e) {
			JOptionPane.showMessageDialog(null, "Verbindung mit der Datenbank konnte nicht hergestellt werden. \n"
					+ e.getMessage() + "\nÜberprüfen Sie den Oracle TNS Pfad oder die URL.");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Verbindung mit der Datenbank konnte nicht hergestellt werden. \n" + e.getMessage());
		}
		return false;
	}

	/**
	 * Erstellt TableCrawler und TableComparer. Hier wird die Funktion
	 * aufgerufen um die Tabellen zu kontrollieren. Die Tabellennamen werden aus
	 * der Klasse TableName gezogen.
	 * 
	 * @param file Enthält das im FileChooser erstellte File
	 */
	public void execute(File file) {
		SQLStatements sqlStatements = new SQLStatements(file);
		TableCrawler tableCrawler = new TableCrawler();
		TableComparer tableComparer = new TableComparer(file);
		List<String> toBeCheckedTable = TableName.list;

		Connection targetConnection = accessTargetDb.getConnection();
		Connection sourceConnection = accessSourceDb.getConnection();

		// Hier werden alle Tabellen durchiteriert um diese als string an
		// die einzelnen Methoden zu übergeben
		try {
			for (String string : toBeCheckedTable) {

				// mittels TableCrawler werden die Metadaten aus einer Tabelle
				// gezogen in in ein Objekt gespeicher welches
				// dann verglichen werden kann um die unterschiede
				// festzustellen
				List<Column> oldColumn = tableCrawler.crawlColumns(targetConnection, string);
				List<Column> newColumn = tableCrawler.crawlColumns(sourceConnection, string);
				tableComparer.differColumn(oldColumn, newColumn, string, targetConnection, sourceConnection);
			}
			

			if (file.exists()) {
				sqlStatements.transaction();
				JOptionPane.showMessageDialog(null, "Erfolgreich ausgeführt. Das Updateskript wurde in '"
						+ file.getAbsolutePath() + "' gespeichert");

			} else {
				//file.delete();
				JOptionPane.showMessageDialog(null, "Schemas sind kongruent. Es wurde kein Updateskript erstellt.");
			}

			System.exit(0);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error \n" + e.getMessage());
			System.exit(1);
		} finally {
			if (targetConnection != null) {
				try {
					targetConnection.close();
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Ziel DB Error: \n" + e.getMessage());
				}
			}
			if (sourceConnection != null) {
				try {
					sourceConnection.close();
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Quelle DB Error: \n" + e.getMessage());
				}
			}
		}
	}
}
