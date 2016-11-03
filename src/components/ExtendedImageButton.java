package components;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;


/**
 * Created by HerrSergio on 19.07.2016.
 */
public class ExtendedImageButton extends ImageButton {

    private int inset = 5;


    public ExtendedImageButton(BufferedImage image) {
        this(image, GuiHelper.makeGray(image));
    }

    public ExtendedImageButton(BufferedImage image, BufferedImage disabledImage) {
        super(image, false, disabledImage, false);
    }
    public ExtendedImageButton(Image image, Image disabledImage) {
        this(image, false, disabledImage, false);
    }
    public ExtendedImageButton(Image image, boolean keepRatio, Image disabledImage, boolean keepDisabledRatio) {
        super(image, keepRatio, assureImages(disabledImage, image), keepDisabledRatio);
    }

    private static Image assureImages(Image disabledImage, Image image) {
        if(image == null)
            return null;
        else if(disabledImage == null)
            return GuiHelper.makeGray(image);
        else
            return disabledImage;
    }



    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        int width = getWidth() - 2 * inset;

        String text = getText();

        FontMetrics fontMetrics = graphics.getFontMetrics();

        while (fontMetrics.stringWidth(text) > width) {
            int len = text.length() - 4;
            if (len >= 0) {
                text = text.substring(0, len) + "...";
            } else if (text.isEmpty()) {
                break;
            } else {
                len = text.length() - 1;
                text = text.substring(0, len);
            }
        }

        int textWidth = fontMetrics.stringWidth(text);

        int x = inset + (width - textWidth) / 2;
        int y = getBaseline(getWidth(), getHeight());

        graphics.setColor(getForeground());
        graphics.setFont(getFont());




        if (!text.isEmpty()) {
            graphics.drawString(text, x, y);
        }

        int y0 = y - fontMetrics.getAscent() / 2;
        int x1 = x - inset;
        int x2 = x + textWidth + inset;

        if(!isEnabled()) {
            graphics.drawLine(x1, y0, x2, y0);
        }

        y0 = y + fontMetrics.getDescent() / 2;

        if (isFocusOwner() && isFocusPainted()) {
            graphics.drawLine(x1, y0, x2, y0);
        }

    }
}
