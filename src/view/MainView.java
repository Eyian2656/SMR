package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import controller.MainController;
import model.config.DbConfig;

// Filechooser und Actionlistener hinzufügen https://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html
public class MainView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTextField txtUserOld, txtUserNew, txtURLOld, txtURLNew;
	private JPasswordField pwSchemaOld, pwSchemaNew;
	private JButton ok, cancel;
	private JLabel lblUser, lblPw, lblUserB, lblPwB, lblURLA, lblURLB;
	private Border loweredetched;

	public MainView() {
		this.setMinimumSize(new Dimension(700, 300));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

		lblUser = new JLabel("User:");
		lblPw = new JLabel("Passwort:");
		lblUserB = new JLabel("User:");
		lblPwB = new JLabel("Passwort:");
		lblURLA = new JLabel("URL:");
		lblURLB = new JLabel("URL: ");

		txtUserOld = new JTextField(20);
		txtUserNew = new JTextField(20);
		txtURLOld = new JTextField(20);
		txtURLNew = new JTextField(20);

		pwSchemaOld = new JPasswordField(20);
		pwSchemaNew = new JPasswordField(20);

		ok = new JButton("Starten");
		ok.addActionListener(new onLogin());

		cancel = new JButton("Cancel");
		cancel.addActionListener(new onCancel());

		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.add(ok, BorderLayout.EAST);
		buttonPanel.add(cancel, BorderLayout.WEST);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 70, 15, 70));

		JPanel inputNorthPanel = new JPanel(new GridLayout(3, 2));
		inputNorthPanel.add(lblUserB);
		inputNorthPanel.add(txtUserOld);
		inputNorthPanel.add(lblPwB);
		inputNorthPanel.add(pwSchemaOld);
		inputNorthPanel.add(lblURLA);
		inputNorthPanel.add(txtURLOld);

		JPanel inputSouthPanel = new JPanel(new GridLayout(3, 2));
		inputSouthPanel.add(lblUser);
		inputSouthPanel.add(txtUserNew);
		inputSouthPanel.add(lblPw);
		inputSouthPanel.add(pwSchemaNew);
		inputSouthPanel.add(lblURLB);
		inputSouthPanel.add(txtURLNew);

		JPanel inputCenterPanel = new JPanel(new BorderLayout());
		inputCenterPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 0, 0));

		JPanel inputPanel = new JPanel(new BorderLayout());
		inputPanel.add(inputNorthPanel, BorderLayout.NORTH);
		inputPanel.add(inputCenterPanel, BorderLayout.CENTER);
		inputPanel.add(inputSouthPanel, BorderLayout.SOUTH);
		inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));

		JPanel pnlHead = new JPanel(new BorderLayout());
		pnlHead.add(inputPanel, BorderLayout.NORTH);
		pnlHead.add(buttonPanel, BorderLayout.SOUTH);

		TitledBorder titleOld;
		titleOld = BorderFactory.createTitledBorder(loweredetched, "Schema Old");
		titleOld.setTitleJustification(TitledBorder.CENTER);
		inputNorthPanel.setBorder(titleOld);

		TitledBorder titleNew;
		titleNew = BorderFactory.createTitledBorder(loweredetched, "Schema New");
		titleNew.setTitleJustification(TitledBorder.CENTER);
		inputSouthPanel.setBorder(titleNew);

		pack();
		this.add(pnlHead);
	}

	// Inner Classes für Action Listener\\
	private class onLogin implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			DbConfig oldConfig = new DbConfig();
			oldConfig.setUrl(txtURLOld.getText());
			oldConfig.setUsername(txtUserOld.getText());
			oldConfig.setPassword(new String(pwSchemaOld.getPassword()));

			DbConfig newConfig = new DbConfig();
			newConfig.setUrl(txtURLNew.getText());
			newConfig.setUsername(txtUserNew.getText());
			newConfig.setPassword(new String(pwSchemaNew.getPassword()));

			boolean isSuccess = MainController.getInstance().start(oldConfig, newConfig);
			if (isSuccess) {
				JOptionPane.showMessageDialog(null, "Erfolgreich durchgeführt", "", JOptionPane.OK_OPTION);
			} else{
				JOptionPane.showMessageDialog(null, "Fehler.", "", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	// Inner Classes für Action Listener\\
	private class onCancel implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			MainController.getInstance().closeConnection();
			System.exit(0);
		}
	}

}