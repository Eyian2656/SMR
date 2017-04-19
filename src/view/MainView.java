package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.lang3.StringUtils;

import controller.MainController;
import model.config.DbConfig;

public class MainView extends JFrame {
	private static final long serialVersionUID = 1L;
	private JFileChooser outputFileChooser;
	private int result;
	private JTextField txtUserOld, txtUserNew, txtURLOld, txtURLNew;
	private JPasswordField pwSchemaOld, pwSchemaNew;
	private JButton ok, cancel;
	private JLabel lblUser, lblPw, lblUserB, lblPwB, lblURLA, lblURLB;
	private Border loweredetched;
	private MainController mainController;

	public MainView(MainController mainController) {
		this.mainController = mainController;
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

		ok = new JButton("Verbinden");
		ok.addActionListener(new onConnect());

		cancel = new JButton("Abbrechen");
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
		this.setLocationRelativeTo(null);
		this.add(pnlHead);
	}

	// Innere Klasse für den Action Listener\\
	private class onConnect implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			File file = selectFile(); // outputfile wird ausgewählt

			if (file != null) {
				DbConfig oldConfig = new DbConfig();
				DbConfig newConfig = new DbConfig();

				oldConfig.setUrl(txtURLOld.getText());
				oldConfig.setUsername(txtUserOld.getText());

				newConfig.setUrl(txtURLNew.getText());
				newConfig.setUsername(txtUserNew.getText());

				if (StringUtils.isBlank(new String(pwSchemaOld.getPassword()))
						|| StringUtils.isBlank(new String(pwSchemaNew.getPassword()))) {
					JOptionPane.showMessageDialog(null, "Passwort kann nicht leer sein");
				} else {
					oldConfig.setPassword(new String(pwSchemaOld.getPassword()));
					newConfig.setPassword(new String(pwSchemaNew.getPassword()));
					mainController.start(oldConfig, newConfig, file);
				}
			}

		}
	}

	// Innere Klasse für den Action Listener\\
	private class onCancel implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}

	public File selectFile() {
		File outputFile = null;
		outputFileChooser = new JFileChooser();
		outputFileChooser.addChoosableFileFilter(new FileNameExtensionFilter(".sql", "sql"));
		outputFileChooser.setAcceptAllFileFilterUsed(false);
		result = outputFileChooser.showDialog(null, "Datei Auswählen");

		if (result == JFileChooser.APPROVE_OPTION) {
			File updateFile = outputFileChooser.getSelectedFile();

			// Loesche wenn file existiert
			if (updateFile.exists()) {
				int showConfirmDialog = JOptionPane.showConfirmDialog(null,
						"Die datei " + updateFile.getName()
								+ " ist bereits vorhanden. Möchten Sie diese Datei überschreiben?",
						"Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

				if (showConfirmDialog == JOptionPane.NO_OPTION) {
					selectFile();
				} else if (showConfirmDialog == JOptionPane.YES_OPTION) {
					updateFile.delete();
				}
			}

			outputFile = updateFile;
		}
		return outputFile;
	}
}