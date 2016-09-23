package forms;

import components.ImageButton;
import org.javagram.dao.TelegramDAO;
import org.javagram.dao.proxy.TelegramProxy;
import resources.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by Anton on 22.05.2016.
 */
public class FormMain {
    private JPanel rootPanel;
    private JPanel topMenu;
    private JPanel leftPanel;
    private JPanel searchPanel;

    private JScrollPane contactsPane;
    private JList contactsList;
    private JButton btGear;

    public FormMain(TelegramDAO telegramDAO, TelegramProxy telegramProxy) throws IOException {
        contactsList.setListData(telegramProxy.getPersons().toArray());
        contactsList.setCellRenderer(new ContactItem(telegramProxy));
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public void addActionListenerForGearButton(ActionListener actionListener) {
        btGear.addActionListener(actionListener);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        btGear = new ImageButton(Images.getGearIcon());
    }
}
