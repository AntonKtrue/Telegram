package messages;

import org.javagram.dao.Me;
import org.javagram.dao.Message;
import org.javagram.dao.Person;
import org.javagram.dao.proxy.TelegramProxy;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Anton on 19.10.2016.
 */
public class MessagesForm extends JPanel {
    private JPanel rootPanel;
    private JScrollPane scrollPane;
    private JPanel scrollPanel;

    private final DateFormat dateFormat = new SimpleDateFormat("HH:mm dd MMM yyyy");
    private TelegramProxy telegramProxy;
    private Person person;

    private int messgesCount = 100;
    private final int width = 150;
    public MessagesForm(TelegramProxy telegramProxy) {
        this(telegramProxy, null);
    }

    public MessagesForm(TelegramProxy telegramProxy, Person person) {
        this.telegramProxy = telegramProxy;
        display(person);
    }

    public void display(Person person) {
        scrollPanel.removeAll();
        this.person = person;

        scrollPanel.setLayout(new BoxLayout(scrollPanel, BoxLayout.Y_AXIS));
        scrollPanel.add(Box.createGlue());

        if(person == null)
            return;

        List<Message> messages = telegramProxy.getMessages(person,messgesCount );
        for(int i = messages.size() - 1; i >= 0; i--) {
            JPanel panel = new JPanel() {
                @Override
                public Dimension getMaximumSize() {
                    Dimension maxSize = super.getMaximumSize();
                    Dimension prefSize = super.getMaximumSize();
                    return new Dimension(maxSize.width, prefSize.height);
                }
            };
            Message message = messages.get(i);
            int alignment;
            Color color;
            String fontColor;
            if(message.getReceiver() instanceof Me) {
                alignment = FlowLayout.LEFT;
                color = Color.decode("#01a7d9");
                fontColor = "white";
            } else if(message.getSender() instanceof Me) {
                alignment = FlowLayout.RIGHT;
                color = Color.decode("#4a44a8");
                fontColor = "black";
            } else {
                alignment = FlowLayout.CENTER;
                color = Color.red;
                fontColor = "green";
            }
            panel.setLayout(new FlowLayout(alignment));
            panel.add(new MessageForm(message.getText(), dateFormat.format(message.getDate()), width, color, fontColor, alignment));
            scrollPanel.add(panel);
        }

        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());

    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = this;
    }
}
