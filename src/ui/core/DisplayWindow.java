package ui.core;

import java.awt.Dimension;
import javax.swing.JFrame;

public class DisplayWindow extends JFrame {

  private Controller ctrl = null;
  private Display display = null;
  private final int totalWidth, totalHeight;
  
  public DisplayWindow(int totalWidth, int totalHeight) {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.totalWidth = totalWidth;
    this.totalHeight = totalHeight;
  }

  public void showDisplay(Display d) {    
    // Remove old display.
    if (this.display != null) {
      this.getContentPane().remove(this.display);
    }

    // Set new controller / display.
    this.display = d;
    this.ctrl = d.getController();

    // Setup this component based on display.
    this.setJMenuBar(d.getDisplayMenuBar());
    
    this.display.buildComponents();
    this.display.layoutComponents(new Dimension(totalWidth, totalHeight));
    Dimension thisSize = new Dimension(totalWidth, totalHeight + 50);
    this.setSize(thisSize);

    // Redo UI stuff.
    this.getContentPane().add(d);
    this.repaint();

    // Display window.
    this.setVisible(true);
  }

}