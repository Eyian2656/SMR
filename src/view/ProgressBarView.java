package view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProgressBarView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8384759546854865564L;

	JProgressBar current = new JProgressBar(0, 100);
	int num = 0;

	public ProgressBarView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel pane = new JPanel();
		current.setValue(0);
		current.setStringPainted(true);
		setContentPane(pane);
		current.setIndeterminate(true);
		this.setLocationRelativeTo(null);
		pane.add(current);
		pack();
	}

	public void setValue(int value) {
		current.setValue(value);
	}
}
