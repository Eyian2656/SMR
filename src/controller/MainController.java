package controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLRecoverableException;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;

import model.config.DbConfig;
import view.MainView;
import view.ProgressBarView;

/**
 * Steuert die MainView und stellt die Verbindung zur Ziel- und Quellschema her
 * und ruft dann einen Thread. Der Thread ist ein Objekt der Klasse MainTask und
 * startet die Funktionen zur überprüfung von der Tabellenstruktur und den
 * Tabellendaten
 * 
 * @author Ian Noack
 *
 */
public class MainController implements PropertyChangeListener {
	private AccessDB accessSourceDb;
	private AccessDB accessTargetDb;
	private MainView mainView;
	private ProgressBarView progressView;
	private static DbConfig targetDB;

	public MainController(String[] args) {
		this.accessSourceDb = new AccessDB();
		this.accessTargetDb = new AccessDB();
		this.mainView = new MainView(this, args);
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
	 * @param targetDbConfig
	 *            Model Klasse enthält Ziel DB Daten.
	 * @param sourceDbConfig
	 *            Model Klasse enthält Quelle DB Daten.
	 * @throws SQLException
	 */
	public boolean connect(DbConfig targetDbConfig, DbConfig sourceDbConfig, String tnsPath) {
		targetDB=targetDbConfig;
		
		// Wird benötigt wenn kein localhost verwendet wird sondern TNS.
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
	 * Ein Thread aufgerufen um die Überprüfung zu starten.
	 * Der Thread wird für die Progressbar benötigt. 
	 * Infos aus:
	 * https://docs.oracle.com/javase/tutorial/uiswing/components/progress.html
	 * @param file
	 *            Enthält das im FileChooser erstellte File
	 */
	public void execute(File file) {
		Connection targetConnection = accessTargetDb.getConnection();
		Connection sourceConnection = accessSourceDb.getConnection();

		progressView = new ProgressBarView();
		progressView.setVisible(true);

		mainView.setVisible(false);
		
		// Der Thread  ermöglicht die Progressbar
		MainTask task = new MainTask(file, targetConnection, sourceConnection, progressView, mainView, targetDB);
		task.addPropertyChangeListener(this);
		task.execute();
	}

	/**
	 * Methode welche die Progressbar verwaltet.
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			progressView.setValue(progress);
		}
	}

}
