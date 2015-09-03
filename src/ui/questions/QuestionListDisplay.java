package ui.questions;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import javafx.scene.input.KeyCode;
import javax.swing.BoxLayout;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import models.Database;
import models.DatabaseIO;
import ui.DisplayWindow;
import ui.Display;
import ui.ImageDisplay;
import ui.questions.action.ActionController;
import ui.questions.action.ActionPanel;

public class QuestionListDisplay extends Display<QuestionListController> {
  
  public static final int TOTAL_WIDTH = 700;
  public static final int TOTAL_HEIGHT = 700;
  public static final int ACTION_PANEL_HEIGHT = 50;

  protected ImageDisplay questionPanel;
  protected ActionPanel actionPanel;

  public QuestionListDisplay(final QuestionListController ctrl) {
    super(ctrl);
  }

  @Override
  protected void setupMenuBar() {
    JMenu file = new JMenu("File");
    JMenuItem menuItem = new JMenuItem("Save");
    menuItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ev) {
        QuestionListDisplay.this.ctrl.saveToDatabase();
      }
      });
    file.add(menuItem);
    this.menuBar.add(file);
  }

  @Override
  public void buildComponents() {
    ActionController actionController = new ActionController(this.ctrl);
    this.actionPanel = new ActionPanel(actionController);
    this.actionPanel.buildComponents();
    
    // Build image display in middle.
    this.questionPanel = new ImageDisplay(true) {
      @Override
      public BufferedImage generateDisplayImage() {
        if (QuestionListDisplay.this.ctrl.getState() == QuestionListController.State.NOT_STARTED) {
          BufferedImage img = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
          Graphics2D gr = img.createGraphics();
          gr.setColor(Color.BLACK);
          gr.drawString("Questions List Not Started", 40, 40);
          return img;
        } else {
          return ctrl.getCurrentQuestion().getImage();
        }
      }
    };
    this.questionPanel.buildComponents();
  }

  @Override
  public void layoutComponents() {
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    
    // Build action panel on top.
    this.actionPanel.layoutComponents();
    this.add(this.actionPanel);
    
    // Build image display in middle.
    this.questionPanel.layoutComponents();
    this.add(this.questionPanel);
  }

  @Override
  public void sizeComponents(Dimension totalSize) {
    Dimension actionPanelSize = new Dimension(
        totalSize.width, ACTION_PANEL_HEIGHT);
    Dimension questionPanelSize = new Dimension(
        totalSize.width, totalSize.height - ACTION_PANEL_HEIGHT);

    this.actionPanel.sizeComponents(actionPanelSize);
    this.questionPanel.sizeComponents(questionPanelSize);
    
    this.sizeComponent(this, totalSize);
  }
  
  public static void main(String[] args) {
    // Load/initialize models.
    Database db = DatabaseIO.loadDatabase();

    // Load/initialize controller/display.
    QuestionListController ctrl = new QuestionListController(db);
    QuestionListDisplay display = new QuestionListDisplay(ctrl);

    // Bring it all home.
    DisplayWindow window = new DisplayWindow(TOTAL_WIDTH, TOTAL_HEIGHT);
    window.showDisplay(display);
  }
}
