package messages;



import javax.swing.*;
import java.awt.*;

/**
 * Created by Anton on 19.10.2016.
 */
public class MessageForm extends JPanel {
    private JEditorPane textPane = new JEditorPane();
    private JLabel dateLabel = new JLabel();
    private BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);

    private Color color;
    private int alignment;

    private final static int MARGIN = 15;
    private final static int RADIUS = 25;

    public MessageForm(String text, String date, int width, Color color, String fontColor, int alignment) {
        this.alignment = alignment;
        setLayout(boxLayout);

//        this.setOpaque(true);
//        this.setBackground(Color.yellow);

        textPane.setAlignmentX(0.05f);
        add(textPane);
        dateLabel.setAlignmentX(0.05f);
        add(dateLabel);
        setBackground(Color.white);

        textPane.setContentType("text/html");
        textPane.setSize(width, Short.MAX_VALUE);
        textPane.setText("<HTML><BODY><TABLE color=\"" + fontColor +"\" style=\'table-layout: fixed;\' width=\'" +
                width + "px\' max-width=\'" + width + "px><TR><TD style=\"word-wrap: break-word;\" width=\'" +
                width + "px\' max-width=\'" + width + "px\'>" + text.replaceAll("\n", "<br/>") + "</TD></TR></TABLE></BODY></HTML>");

        textPane.setOpaque(false);
        textPane.setEditable(false);
        textPane.setMargin(new Insets(MARGIN, MARGIN, MARGIN , MARGIN ));
        dateLabel.setText(date);

        this.color = color;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        Graphics2D graphics2D = (Graphics2D) g;
        //Set  anti-alias!
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        // Set anti-alias for text
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.fillRoundRect(textPane.getX()+MARGIN, textPane.getY(), textPane.getWidth()-MARGIN*2, textPane.getHeight(), RADIUS, RADIUS);
        Polygon polygon = new Polygon();
        //g.setColor(Color.black);
        switch (alignment) {
            case FlowLayout.LEFT:
                polygon.addPoint(textPane.getX()+MARGIN,textPane.getY() + textPane.getHeight()/2 + 5);
                polygon.addPoint(textPane.getX()+MARGIN/2,textPane.getY() + textPane.getHeight()/2);
                polygon.addPoint(textPane.getX()+MARGIN,textPane.getY() + textPane.getHeight()/2 - 5);
                graphics2D.fillPolygon(polygon);
                break;
            case FlowLayout.RIGHT:
                polygon.addPoint(textPane.getWidth()-MARGIN,textPane.getY() + textPane.getHeight()/2 + 5);
                polygon.addPoint(textPane.getWidth()-MARGIN/2,textPane.getY() + textPane.getHeight()/2);
                polygon.addPoint(textPane.getWidth()-MARGIN,textPane.getY() + textPane.getHeight()/2 - 5);
                graphics2D.fillPolygon(polygon);
                break;
        }

    }
}
