package controller;

import java.sql.Connection;
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
		AccessV1DB.getInstance().connect(oldDbConfig);
		AccessV2DB.getInstance().connect(newDbConfig);

		Connection oldSchema = AccessV1DB.getInstance().getConnection();
		Connection newSchema = AccessV2DB.getInstance().getConnection();

		// TODO Validation if the schema have the same crednetial
		// TODO Check if the connection both successfull, then continue
		// Validate the connection
		if (oldSchema != null && !AccessV1DB.getInstance().isConnected()) {
			JOptionPane.showMessageDialog(null, "Verbindung mit der alten DB wurde abgelehnt");
		} else if (newSchema != null && !AccessV2DB.getInstance().isConnected()) {
			JOptionPane.showMessageDialog(null, "Verbindung mit der neuen DB wurde abgelehnt");
		} else if (AccessV1DB.getInstance().isConnected() && AccessV2DB.getInstance().isConnected()) {
			hideMainView();
			ChooserController.getInstance().showChooserView(oldSchema, newSchema);
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
