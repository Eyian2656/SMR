package controller;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import model.Column;
import model.TableName;
import view.MainView;
import view.ProgressBarView;

public class MainTask extends SwingWorker<Void, Void> {

	private File file;
	private Connection targetConnection;
	private Connection sourceConnection;
	private ProgressBarView progressView;
	private MainView mainView;

	public MainTask(File file, Connection targetConnection, Connection sourceConnection, ProgressBarView progressView,
			MainView mainView) {
		this.file = file;
		this.targetConnection = targetConnection;
		this.sourceConnection = sourceConnection;
		this.progressView = progressView;
		this.mainView = mainView;
	}

	@Override
	protected Void doInBackground() throws Exception {
		try {
			TableCrawler tableCrawler = new TableCrawler();
			TableComparer tableComparer = new TableComparer(file);
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

				progress = (i / (double) toBeCheckedTable.size()) * 100;
				setProgress((int) progress);
			}

			SQLStatements sqlStatements = new SQLStatements(file);
			if (file.exists()) {
				sqlStatements.transaction();
				JOptionPane.showMessageDialog(null, "Erfolgreich ausgeführt. Das Updateskript wurde in '"
						+ file.getAbsolutePath() + "' gespeichert");

			} else {
				// file.delete();
				JOptionPane.showMessageDialog(null, "Schemas sind kongruent. Es wurde kein Updateskript erstellt.");
			}
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

	@Override
	public void done() {
		this.progressView.dispose();
		this.mainView.setVisible(true);
		System.exit(0);
	}
}
