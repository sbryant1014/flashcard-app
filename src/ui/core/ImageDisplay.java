/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import ui.core.components.FABuildable;
import ui.core.components.FAPanel;

/**
 *
 * @author sambryant
 */
public abstract class ImageDisplay extends FAPanel implements FABuildable {

  /**
   *
   */
  public final boolean resizeImage;

  /**
   *
   * @param resizeImage
   */
  public ImageDisplay(boolean resizeImage) {
    super();
    this.resizeImage = resizeImage;
  }

  /**
   *
   * @return
   */
  public abstract BufferedImage generateDisplayImage();

  @Override
  public void paintComponent(Graphics gr) {
    int w = this.getWidth();
    int h = this.getHeight();

    gr.setColor(Color.WHITE);
    gr.fillRect(0, 0, w, h);

    BufferedImage img = generateDisplayImage();
    int width = img.getWidth();
    int height = img.getHeight();

    if (this.resizeImage) {
      if (width < w && height < h) {
        gr.drawImage(img, 0, 0, null);
      } else {
        double heightRatio = ((double) height) / h;
        double widthRatio = ((double) width) / w;
        double maxRatio = Math.max(widthRatio, heightRatio);

        width = (int) (width / maxRatio);
        height = (int) (height / maxRatio);
        gr.drawImage(img, 0, 0, width, height, null);
      }
    } else {
      this.setSize(width, height);
      gr.drawImage(img, 0, 0, null);
    }
  }

  /**
   *
   */
  @Override
  public void buildComponents() {}

  /**
   *
   * @param totalSize
   */
  @Override
  public void layoutComponents(Dimension totalSize) {
    this.sizeComponent(this, totalSize);
  }

}
