package forms;

import components.ImageButton;
import org.javagram.dao.*;
import org.javagram.dao.Dialog;
import org.javagram.dao.proxy.TelegramProxy;
import resources.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Map;

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
    private JPanel rightPanel;
    private JPanel chatPanel;
    private JPanel messagePanel;
    private JScrollPane chatScrollPane;
    private JList chat;

    public FormMain(TelegramDAO telegramDAO, TelegramProxy telegramProxy) throws IOException {
        contactsList.setListData(telegramProxy.getPersons().toArray());
        contactsList.setCellRenderer(new ContactItem(telegramProxy));
        Map<Person, Dialog> chatList = telegramProxy.getDialogs(false);
        for(Map.Entry<Person,Dialog> entry : chatList.entrySet()) {
            System.out.println(entry.getKey().getFirstName() + " " +
                    entry.getKey().getLastName() + " " +
                entry.getValue());
        }
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
