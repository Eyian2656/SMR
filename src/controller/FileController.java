package controller;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.Column;
import model.TableName;
import view.ChooserView;

/**
 * Diese Klasse steuert die Informationen aus der FileChooser view
 * @author Ian
 *
 */
public class FileController {
	private JFrame chooserView;
	private static FileController chooserController;
	private Connection oldSchema;
	private Connection newSchema;

	public FileController() {
		this.chooserView = new ChooserView(TableName.list.size());
	}

	public static FileController getInstance() {
		if (chooserController == null) {
			chooserController = new FileController();
		}
		return chooserController;
	}

	/**
	 * Startet das Abbilden der View
	 * 
	 * @param newSchema
	 * @param oldSchema
	 */
	public void showChooserView() {
		this.oldSchema = AccessV1DB.getInstance().getConnection();
		this.newSchema = AccessV2DB.getInstance().getConnection();
		chooserView.setVisible(true);
	}

	public void hideChooserView() {
		chooserView.setVisible(false);
	}

	/**
	 * Funktion die die View schließt durch den Cancel Button
	 */
	public void navigateToMainController() {
		this.hideChooserView();
		this.closeConnection();
		MainController.getInstance().showMainView();
	}

	/**
	 * Erstellt TableCrawler und TableComparer. Hier wird die Funktion aufgerufen
	 * um die Tabellen zu kontrollieren. Die Tabellennamen werden aus dem Klasse TableName 
	 * gezogen.
	 * @param file
	 */
	public void execute(File file) {
		TableCrawler tableCrawler = new TableCrawler();
		TableComparer tableComparer = new TableComparer(file);
		List<String> toBeCheckedTable = TableName.list;

		// Hier werden alle Tabellen durchiteriert um diese als string an
		// die einzelnen Methoden zu übergeben
		try {
			for (String string : toBeCheckedTable) {
				System.out.println("Now checking table " + string);

				// mittels TableCrawler werden die Metadaten aus einer Tabelle
				// gezogen in in ein Objekt gespeicher welches
				// dann vergleichen werden kann um die unterschiede festzustellen
				List<Column> oldColumn = tableCrawler.crawlColumns(oldSchema, string);
				List<Column> newColumn = tableCrawler.crawlColumns(newSchema, string);
				tableComparer.differColumn(oldColumn, newColumn, string, oldSchema, newSchema);

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
	 * Schließen der Datenbankverbindung
	 */
	public void closeConnection() {
		try {
			if (AccessV1DB.getInstance().getConnection() != null) {
				AccessV1DB.getInstance().getConnection().close();
			}
			if (AccessV2DB.getInstance().getConnection() != null) {
				AccessV2DB.getInstance().getConnection().close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
