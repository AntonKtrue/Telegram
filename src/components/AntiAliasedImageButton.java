package components;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by user on 01.11.16.
 */
public class AntiAliasedImageButton extends ImageButton {

    private int inset = 5;
    //private int pressOffset = 0;
    public AntiAliasedImageButton(BufferedImage image) {
        super(image);
//        addMouseListener(new MouseAdapter() {
//            @Override
//            public void mousePressed(MouseEvent e) {
//                pressOffset = 4;
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                pressOffset = 0;
//            }
//        });
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D) graphics;
        //Set  anti-alias!
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        // Set anti-alias for text
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);


        int width = getWidth() - 2 * inset;

        String text = getText();

        FontMetrics fontMetrics = graphics2D.getFontMetrics();

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


        graphics2D.setColor(getForeground());
        graphics2D.setFont(getFont());

        if (!text.isEmpty()) {
            graphics2D.drawString(text, x, y - 4 ); //+ pressOffset);
        }

        int y0 = y - fontMetrics.getAscent() / 2;
        int x1 = x - inset;
        int x2 = x + textWidth + inset;

        if(!isEnabled()) {
            graphics2D.drawLine(x1, y0, x2, y0);
        }

        y0 = y + fontMetrics.getDescent() / 2;

        if (isFocusOwner() && isFocusPainted()) {
            graphics2D.drawLine(x1, y0, x2, y0);
        }
    }
}
