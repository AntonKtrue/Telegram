package forms;

import components.ImageButton;
import components.ImagePanel;
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
    private JPanel logoMicro;
    private JPanel infoPanel;
    private JPanel mePanel;
    private JPanel avaImage;
    private JLabel userName;

    public FormMain() {
        messagesPanel.add(new JPanel());
    }

    public JButton getBtGear() {
        return btGear;
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

    public JLabel getUserName() {
        return userName;
    }

    public JPanel getAvaImage() {
        return avaImage;
    }

    public void setAvaImage(JPanel avaImage) {
        this.avaImage = avaImage;
    }

    public void addActionListenerForGearButton(ActionListener actionListener) {
        btGear.addActionListener(actionListener);
    }

    public JPanel getMePanel() {
        return mePanel;
    }



    private void createUIComponents() {
        // TODO: place custom component creation code here
        btGear = new ImageButton(Images.getGearIcon());
        logoMicro = new ImagePanel(Images.getLogoMicro(), false, true, 0);


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
