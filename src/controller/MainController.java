package controller;

import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.config.DbConfig;
import view.MainView;

/**
 * Steuert die MainView und die Informationen
 * @author Ian
 *
 */
public class MainController {
	private JFrame mainView;
	private static MainController mainController;

	public MainController() {
		this.mainView = new MainView();
	}

	public static MainController getInstance() {
		if (mainController == null) {
			mainController = new MainController();
		}
		return mainController;
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
	 * Verbindet die Datenbank und startet der die n‰chste View
	 * @param oldDbConfig
	 * @param newDbConfig
	 */
	
	public void start(DbConfig oldDbConfig, DbConfig newDbConfig) {
		try {
			AccessV1DB.getInstance().connect(oldDbConfig);
			AccessV2DB.getInstance().connect(newDbConfig);
		} catch (Exception e) {
			e.printStackTrace();
			closeConnection();
			JOptionPane.showMessageDialog(null,
					"Verbindung mit der DB wurde abgelehnt. Log : " + e.getMessage());
		}

		AccessV1DB.getInstance().getConnection();
		AccessV2DB.getInstance().getConnection();

		// Validate the connection
		if (AccessV1DB.getInstance().isConnected() && AccessV2DB.getInstance().isConnected()) {
			hideMainView();
			FileController.getInstance().showChooserView();
		}
	}

	/**
	 * Schlieﬂt die DB verbindung
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
