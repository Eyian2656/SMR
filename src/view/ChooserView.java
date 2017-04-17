package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import controller.ChooserController;

public class ChooserView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3170388574322213481L;

	private JButton cancel, execute, selectFile;
	private JLabel lblFilePath;
	private JFileChooser outputFileChooser;
	private Border loweredetched;
	private int result;
	private File outputFile;

	public ChooserView(int tableNameSize) {
		this.setMinimumSize(new Dimension(700, 300));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

		cancel = new JButton("Abbrechen");
		cancel.addActionListener(new onCancel());

		execute = new JButton("Ausführen");
		execute.addActionListener(new onExecute());
		execute.setEnabled(false);

		selectFile = new JButton("Datei auswählen");
		selectFile.addActionListener(new onSelectFile());

		lblFilePath = new JLabel("");

		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.add(cancel, BorderLayout.WEST);
		buttonPanel.add(execute, BorderLayout.EAST);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 70, 15, 70));

		JPanel inputNorthPanel = new JPanel(new BorderLayout());
		inputNorthPanel.add(selectFile, BorderLayout.WEST);
		inputNorthPanel.add(lblFilePath, BorderLayout.CENTER);
		inputNorthPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 0, 0));

		JPanel inputCenterPanel = new JPanel(new BorderLayout());
		inputCenterPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 0, 0));

		JPanel inputPanel = new JPanel(new BorderLayout());
		inputPanel.add(inputNorthPanel, BorderLayout.NORTH);
		inputPanel.add(inputCenterPanel, BorderLayout.CENTER);
		// inputPanel.add(inputSouthPanel, BorderLayout.SOUTH);
		inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));

		JPanel pnlHead = new JPanel(new BorderLayout());
		pnlHead.add(inputPanel, BorderLayout.NORTH);
		pnlHead.add(buttonPanel, BorderLayout.SOUTH);

		TitledBorder titleFileChooser;
		titleFileChooser = BorderFactory.createTitledBorder(loweredetched, "File Chooser");
		titleFileChooser.setTitleJustification(TitledBorder.CENTER);
		inputNorthPanel.setBorder(titleFileChooser);

		pack();
		this.setLocationRelativeTo(null);
		this.add(pnlHead);
	}

	// Innere Klasse für den Action Listener\\
	private class onCancel implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			ChooserController.getInstance().navigateToMainController();
		}
	}

	private class onExecute implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			ChooserController.getInstance().execute(outputFile);
		}
	}

	private class onSelectFile implements ActionListener {

		public void actionPerformed(ActionEvent ae) {
			selectFile();
			File newMapFile = outputFileChooser.getSelectedFile();

			if (result == JFileChooser.APPROVE_OPTION) {
				outputFile = newMapFile;
				System.out.println(outputFile);
				lblFilePath.setText(newMapFile.getAbsolutePath());
				execute.setEnabled(true);
			} else if (result == JFileChooser.CANCEL_OPTION) {
				JOptionPane.showMessageDialog(null, "Es wurde keine Datei ausgewählt.", "Keine Datei ausgewählt.",
						JOptionPane.PLAIN_MESSAGE);
			}
		}
	}

	/*
	 * - Method to open FileChooser
	 */
	private void selectFile() {
		outputFileChooser = new JFileChooser();
		outputFileChooser.addChoosableFileFilter(new FileNameExtensionFilter(".sql", "sql"));
		outputFileChooser.setAcceptAllFileFilterUsed(false);
		result = outputFileChooser.showDialog(null, "Datei Auswählen");
	}

}
