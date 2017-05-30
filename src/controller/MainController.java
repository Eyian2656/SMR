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
 * Steuert die MainView und führt das Programm aus in dem es die Funktionen der
 * anderen Klassen aufruft.
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
	 * @param targetDbConfig
	 *            Model Klasse enthält Ziel DB Daten.
	 * @param sourceDbConfig
	 *            Model Klasse enthält Quelle DB Daten.
	 * @throws SQLException
	 */
	public boolean connect(DbConfig targetDbConfig, DbConfig sourceDbConfig, String tnsPath) {
		targetDB=sourceDbConfig;
		
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
	 * Erstellt die Verbindung zum Ziel- und Quelleschema. 
	 * Anschließend wird ein Thread aufgerufen um die Überprüfung zu starten.
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
		
		// Ein  Thread wird erzeugt um die Progressbar zu ermöglichen
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
