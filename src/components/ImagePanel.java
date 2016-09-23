package components;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by HerrSergio on 27.06.2016.
 */
public class ImagePanel extends JPanel {

    private BufferedImage image;
    private boolean keepRatio;
    private int insetX, insetY;

    public ImagePanel(BufferedImage image, boolean opaque, boolean keepRatio, int insetX, int insetY) {
        this.image = image;
        setOpaque(opaque);
        this.keepRatio = keepRatio;
        this.insetX = insetX;
        this.insetY = insetY;
    }

    public ImagePanel(BufferedImage image, boolean opaque, boolean keepRatio, int inset) {
        this(image, opaque, keepRatio, inset, inset);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = this.getWidth() - insetX * 2;
        int height = this.getHeight() - insetY * 2;

        if (image == null || width <= 0 || height <= 0)
            return;

        if(keepRatio)
            GuiHelper.drawImage(g, image, insetX, insetY, width, height);
        else
            g.drawImage(image, insetX, insetY, width, height, null);
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        if(image != this.image) {
            this.image = image;
            repaint();
        }
    }

    public boolean isKeepRatio() {
        return keepRatio;
    }

    public void setKeepRatio(boolean keepRatio) {
        if(keepRatio !=this.keepRatio) {
            this.keepRatio = keepRatio;
            repaint();
        }
    }

    public int getInsetX() {
        return insetX;
    }

    public void setInsetX(int insetX) {
        if(insetX != this.insetX) {
            this.insetX = insetX;
            repaint();
        }
    }

    public int getInsetY() {
        return insetY;
    }

    public void setInsetY(int insetY) {
        if(insetY != this.insetY) {
            this.insetY = insetY;
            repaint();
        }
    }
}
