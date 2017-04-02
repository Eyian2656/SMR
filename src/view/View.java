package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class View extends JFrame{
	
	private JTextField txtUserOld, txtUserNew, txtURLOld, txtURLNew;
	private JPasswordField pwSchemaOld, pwSchemaNew;
	private JButton ok , cancel;
	private JLabel  lblUser, lblPw , lblUserB, lblPwB , lblURLA, lblURLB;
	private Border loweredetched;
	
	public View(){
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
		
		cancel = new JButton("Cancel");
		
		
		
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
		setVisible(true);
		this.add(pnlHead);
		
		

	}
}