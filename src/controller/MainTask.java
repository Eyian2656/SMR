package controller;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import model.Column;
import model.TableName;
import model.config.DbConfig;
import view.MainView;
import view.ProgressBarView;
/**
 * Notwendige Klasse um eine Progressbar mit Informationen zu versorgen.
 * Hier wird der TableCrawler und TableComparer gestartet und damit die
 * Struktur des Schematas überprüft.
 * @author Dev
 *
 */
public class MainTask extends SwingWorker<Void, Void> {

	private File file;
	private Connection targetConnection;
	private Connection sourceConnection;
	private ProgressBarView progressView;
	private MainView mainView;
	private DbConfig targetDB;

	public MainTask(File file, Connection targetConnection, Connection sourceConnection, ProgressBarView progressView,
			MainView mainView, DbConfig targetDB) {
		this.file = file;
		this.targetConnection = targetConnection;
		this.sourceConnection = sourceConnection;
		this.progressView = progressView;
		this.mainView = mainView;
		this.targetDB = targetDB;
	}

	/**
	 * Standart Methode von der Progressbar um Threads im Hintergrund arbeiten zu lassen.
	 * 
	 * In der Funktion wird der TableCrawler und TableComparer erstellt. Es werden die Funktion
	 * aufgerufen um die Tabellen zu kontrollieren. Die Tabellennamen werden aus
	 * der Klasse TableName gezogen. Es wird nur dann ein Skript erzeugt wenn die Schematas inkongruent sind. 
	 * Am Ende des wird die Methode transaction() ausgeführt dir ein Commit am Ende des Skripts schreibt.
	 */
	@Override
	protected Void doInBackground() throws Exception {
		try {
			TableCrawler tableCrawler = new TableCrawler();
			TableComparer tableComparer = new TableComparer(file, targetDB.getUsername());
			List<String> toBeCheckedTable = TableName.list;
			double progress = 0;
			setProgress(0);
			// Hier werden alle Tabellen durchiteriert um diese als string an
			// die einzelnen Methoden zu übergeben
			for (int i = 0; i < toBeCheckedTable.size(); i++) {

				String string = toBeCheckedTable.get(i);

				// mittels TableCrawler werden die Metadaten aus einer Tabelle
				// gezogen in in ein Objekt gespeicher welches
				// dann verglichen werden kann um die unterschiede
				// festzustellen
				List<Column> oldColumn = tableCrawler.crawlColumns(targetConnection, string);
				List<Column> newColumn = tableCrawler.crawlColumns(sourceConnection, string);
				tableComparer.differColumn(oldColumn, newColumn, string, targetConnection, sourceConnection);

				//Rechnung um die Prozentzahl in der Progressbar zu bestimmen
				progress = (i / (double) toBeCheckedTable.size()) * 100;
				setProgress((int) progress);
			} 
			
			if (file.exists()) {
				//String parameter muss null sein da ansonsten am Ende des Files nochmals die Informationen zum Erstellungzeitraum und Nutzung aufgeschrieben werden.
				SQLStatements sqlStatements = new SQLStatements(file, null);
				sqlStatements.transaction();
				
				JOptionPane.showMessageDialog(null, "Erfolgreich ausgeführt. Das Updateskript wurde in '"
						+ file.getAbsolutePath() + "' gespeichert");

			} else {
				JOptionPane.showMessageDialog(null, "Schemas sind kongruent. Es wurde kein Updateskript erstellt.");
			}
		// Error Handling
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
		return null;
	}

	/**
	 * Standard Methode von der Progressbar, sie schließt die Progresbar und setzt die Mainview wieder sichtbar sobald der Thread durchgelaufen ist.
	 */
	@Override
	public void done() {
		this.progressView.dispose();
		this.mainView.setVisible(true);
		System.exit(0);
	}
}
