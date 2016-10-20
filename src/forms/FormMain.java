package forms;

import components.ImageButton;
import org.javagram.dao.*;
import resources.Images;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by Anton on 22.05.2016.
 */
public class FormMain {
    private JPanel rootPanel;
    private JPanel topMenu;
    private JPanel leftPanel;
    private JPanel searchPanel;

    private JScrollPane contactsPane;
    private JList<Person> contactsList;
    private JButton btGear;
    private JPanel rightPanel;
    private JPanel messagesPanel;
    private JPanel messagePanel;

    public FormMain() {
        messagesPanel.add(new JPanel());

    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public JPanel getMessagesPanel() {
        return messagesPanel;
    }

    public JList getContactsList() {
        return contactsList;
    }

    public void addActionListenerForGearButton(ActionListener actionListener) {
        btGear.addActionListener(actionListener);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        btGear = new ImageButton(Images.getGearIcon());
    }

    public Person getSelectedValue() {
        return contactsList.getSelectedValue();
    }

    public void addListSelectionListener(ListSelectionListener listSelectionListener) {
        contactsList.addListSelectionListener(listSelectionListener);
    }

    public void setMessagesPanel(Component messagesPanel) {
        this.messagesPanel.removeAll();
        this.messagesPanel.add(messagesPanel);
    }
}
