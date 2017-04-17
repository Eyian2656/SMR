package controller;

import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.config.DbConfig;
import view.MainView;

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

	public void hideMainView() {
		mainView.setVisible(false);
	}

	public void start(DbConfig oldDbConfig, DbConfig newDbConfig) {
		// TODO Validation if the schema have the same crednetial

		try {
			AccessV1DB.getInstance().connect(oldDbConfig);
			AccessV2DB.getInstance().connect(newDbConfig);
		} catch (Exception e) {
			e.printStackTrace();
			closeConnection();
			JOptionPane.showMessageDialog(null,
					"Verbindung mit der DB wurde abgelehnt. Details : " + e.getMessage());
		}

		AccessV1DB.getInstance().getConnection();
		AccessV2DB.getInstance().getConnection();

		// Validate the connection
		if (AccessV1DB.getInstance().isConnected() && AccessV2DB.getInstance().isConnected()) {
			hideMainView();
			ChooserController.getInstance().showChooserView();
		}
	}

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
