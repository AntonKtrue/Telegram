package forms;

import components.GuiHelper;
import components.PhotoPanel;
import org.javagram.dao.*;
import org.javagram.dao.Dialog;
import org.javagram.dao.proxy.TelegramProxy;
import javax.swing.*;
import java.awt.*;

/**
 * Created by Anton on 05.06.2016.
 */
public class ContactItem extends JPanel implements ListCellRenderer<Person> {
    private JPanel rootPanel;
    private JLabel contactName;
    private JPanel avaPanel;
    private JTextPane lastMessage;

    private boolean hasFocus;
    private final int focusMarkerWidth = 4;

    private TelegramProxy telegramProxy;

    public ContactItem(TelegramProxy telegramProxy) {
        this.telegramProxy = telegramProxy;
    }

    public void setBaseColor() {
        rootPanel.setBackground(new Color(230, 230, 230));
    }

    public void setActiveColor() {
        rootPanel.setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.lightGray);
        g.drawRect(0,0,this.getWidth()-1, this.getHeight()-1);
        if(hasFocus) {
            g.setColor(Color.blue);
            g.fillRect(getWidth()-focusMarkerWidth, 1, focusMarkerWidth, getHeight()-2);
        }
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Person> list, Person person, int index, boolean selected, boolean cellHasFocus) {
        Dialog dialog = telegramProxy.getDialog(person);
        this.contactName.setText(person.getFirstName() + " " + person.getLastName());

        if(dialog != null){
            this.lastMessage.setText(dialog.getLastMessage().getText());
        } else {
            this.lastMessage.setText("");
        }

        if (selected) {
            setActiveColor();
        } else {
            setBaseColor();
        }
        this.hasFocus = cellHasFocus;
        ((PhotoPanel)avaPanel).setImage(GuiHelper.getPhoto(telegramProxy, person, true, true));
        ((PhotoPanel)avaPanel).setOnline(telegramProxy.isOnline(person));

        return rootPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = this;
        avaPanel = new PhotoPanel(null, true, false, 0, false);
    }

}
