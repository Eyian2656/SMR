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

/**
 * Die View die sich öffnet wenn das Programm gestartet wird. Diese View öffnet
 * später den JFileChooser. Es werden URL, Passwörter, User und TNS Adressen an
 * den Controller MainController weitergegeben.
 * 
 * @author Ian Noack
 *
 */
public class MainView extends JFrame {
	private static final long serialVersionUID = 1L;
	private JFileChooser outputFileChooser;
	private int result;
	private JTextField txtUserOld, txtUserNew, txtURLOld, txtURLNew, txtTnsPath;
	private JPasswordField pwSchemaOld, pwSchemaNew;
	private JButton ok, cancel;
	private JLabel lblUser, lblPw, lblUserB, lblPwB, lblURLA, lblURLB, lblTnsPath;
	private Border loweredetched;
	private MainController mainController;

	public MainView(MainController mainController, String[] args) {
		this.mainController = mainController;
		this.setMinimumSize(new Dimension(700, 310));
		this.setTitle("EPOS Synch-Tool");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

		lblUser = new JLabel("User:");
		lblPw = new JLabel("Passwort:");
		lblUserB = new JLabel("User:");
		lblPwB = new JLabel("Passwort:");
		lblURLA = new JLabel("URL:");
		lblURLB = new JLabel("URL: ");
		lblTnsPath = new JLabel("Oracle TNS Pfad: ");

		txtUserOld = new JTextField(20);
		txtUserNew = new JTextField(20);
		txtURLOld = new JTextField(20);
		txtURLNew = new JTextField(20);
		txtTnsPath = new JTextField(50);

		pwSchemaOld = new JPasswordField(20);
		pwSchemaNew = new JPasswordField(20);

		txtURLOld.setText("//localhost:1521/xe");
		txtURLNew.setText("//localhost:1521/xe");

		if (args != null && args.length == 4) {
			txtUserOld.setText(args[0]);
			txtUserNew.setText("IAN_NEW_EPOSDB");
			pwSchemaNew.setText("EPOSDev");
			pwSchemaOld.setText("EPOSDev ");
		}

		ok = new JButton("Skript erzeugen");
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

		JPanel inputCenterPanel = new JPanel(new GridLayout(3, 2));
		inputCenterPanel.add(lblUser);
		inputCenterPanel.add(txtUserNew);
		inputCenterPanel.add(lblPw);
		inputCenterPanel.add(pwSchemaNew);
		inputCenterPanel.add(lblURLB);
		inputCenterPanel.add(txtURLNew);

		JPanel inputSouthPanel = new JPanel(new GridLayout(1, 2));
		inputSouthPanel.add(lblTnsPath);
		inputSouthPanel.add(txtTnsPath);
		inputSouthPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 0, 0));

		JPanel inputPanel = new JPanel(new BorderLayout());
		inputPanel.add(inputNorthPanel, BorderLayout.NORTH);
		inputPanel.add(inputCenterPanel, BorderLayout.CENTER);
		inputPanel.add(inputSouthPanel, BorderLayout.SOUTH);
		inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));

		JPanel pnlHead = new JPanel(new BorderLayout());
		pnlHead.add(inputPanel, BorderLayout.NORTH);
		pnlHead.add(buttonPanel, BorderLayout.SOUTH);

		TitledBorder titleOld;
		titleOld = BorderFactory.createTitledBorder(loweredetched, "Ziel Schema");
		titleOld.setTitleJustification(TitledBorder.CENTER);
		inputNorthPanel.setBorder(titleOld);

		TitledBorder titleNew;
		titleNew = BorderFactory.createTitledBorder(loweredetched, "Quelle Schema");
		titleNew.setTitleJustification(TitledBorder.CENTER);
		inputCenterPanel.setBorder(titleNew);

		pack();
		this.setLocationRelativeTo(null);
		this.add(pnlHead);
	}

	// Innere Klasse für den Action Listener\\
	private class onConnect implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			DbConfig oldConfig = new DbConfig();
			DbConfig newConfig = new DbConfig();

			oldConfig.setUrl(txtURLOld.getText());
			oldConfig.setUsername(txtUserOld.getText());

			newConfig.setUrl(txtURLNew.getText());
			newConfig.setUsername(txtUserNew.getText());

			// Prüft ob das Passwort befüllt ist
			if (StringUtils.isBlank(new String(pwSchemaOld.getPassword()))
					|| StringUtils.isBlank(new String(pwSchemaNew.getPassword()))) {
				JOptionPane.showMessageDialog(null, "Passwort kann nicht leer sein");
			} else {
				oldConfig.setPassword(new String(pwSchemaOld.getPassword()));
				newConfig.setPassword(new String(pwSchemaNew.getPassword()));

				if (mainController.connect(oldConfig, newConfig, txtTnsPath.getText())) {
					File file = selectFile();
					if (file != null) {
						mainController.execute(file);
					}
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

	// FileChooser Klasse
	public File selectFile() {
		File outputFile = null;
		outputFileChooser = new JFileChooser();
		outputFileChooser.addChoosableFileFilter(new FileNameExtensionFilter(".sql", "sql"));
		outputFileChooser.setAcceptAllFileFilterUsed(false);
		result = outputFileChooser.showDialog(null, "Starten");

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