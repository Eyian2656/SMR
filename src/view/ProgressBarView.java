package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProgressBarView extends JFrame {
  JProgressBar current = new JProgressBar(0, 100);
  int num = 0;
  public ProgressBarView() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel pane = new JPanel();
    current.setValue(0);
    current.setStringPainted(true);
    setContentPane(pane);
    current.setIndeterminate(true);
    pane.add(current);
    pack();
  }

  public void iterate(int percentage) {
{
      current.setValue(percentage);
    }
  }
}
