package ui.core;

import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import ui.core.components.FAKeyMap;

/**
 *
 * @author sambryant
 * @param <T>
 */
public abstract class Controller <T extends Display> extends Observable implements KeyListener, FAKeyMap, FocusListener {

  private final Map<Integer, ActionListener> _keyMap;

  /**
   *
   */
  protected T display = null;

  /**
   *
   */
  public Controller() {
    super();
    this._keyMap = new HashMap<>();
  }

  final void setDisplay(T d) {
    this.display = d;
  }

  /**
   *
   */
  public void update() {
    this.notifyObservers();
    if (this.display != null)
      this.display.repaint();
  }

  /**
   *
   * @param keyCode
   * @param al
   */
  @Override
  public final void addKeyAction(int keyCode, ActionListener al) {
    this._keyMap.put(keyCode, al);
  }

  @Override
  public final void keyTyped(KeyEvent e) {}

  @Override
  public final void keyPressed(KeyEvent e) {
    ActionListener al = this._keyMap.get(e.getKeyCode());
    if (al != null) {
      al.actionPerformed(null);
    } else {
      System.out.printf("Unknown key code: %d\n", e.getKeyCode());
    }
  }

  @Override
  public final void keyReleased(KeyEvent e) {}

  @Override
  public void focusGained(FocusEvent e) {
  }

  @Override
  public void focusLost(FocusEvent e) {
//    Component c = e.getComponent();
//    c.requestFocus();
  };

  /**
   *
   */
  public final void requestFocus() {
    this.display.requestFocus();
  }  

}
