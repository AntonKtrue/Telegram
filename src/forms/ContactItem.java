package forms;

import components.GuiHelper;
import components.PhotoPanel;
import org.javagram.dao.Person;
import org.javagram.dao.proxy.TelegramProxy;
import javax.swing.*;
import java.awt.*;

/**
 * Created by Anton on 05.06.2016.
 */
public class ContactItem implements ListCellRenderer<Person> {
    private JPanel rootPanel;
    private JLabel contactName;
    private JPanel avaPanel;

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
    public Component getListCellRendererComponent(JList<? extends Person> list, Person person, int index, boolean isSelected, boolean cellHasFocus) {
        contactName.setText(person.getFirstName() + " " + person.getLastName());
        ((PhotoPanel)avaPanel).setImage(GuiHelper.getPhoto(telegramProxy, person, true, true));
        ((PhotoPanel)avaPanel).setOnline(telegramProxy.isOnline(person));

        if (isSelected) {
            setActiveColor();
        } else {
            setBaseColor();
        }
        return rootPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        avaPanel = new PhotoPanel(null, true, false, 0, false);
    }

}
