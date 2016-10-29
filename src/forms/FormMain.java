package forms;

import components.ImageButton;
import components.ImagePanel;
import components.PhotoPanel;
import org.javagram.dao.*;
import resources.Images;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by Anton on 22.05.2016.
 */
public class FormMain extends JPanel {
    private JPanel rootPanel;
    private JPanel topMenu;
    private JPanel leftPanel;
    private JPanel searchPanel;

    private JButton btGear;
    private JPanel rightPanel;
    private JPanel messagesPanel;
    private JPanel messagePanel;
    private JPanel logoMicro;
    private JPanel infoPanel;
    private JPanel mePanel;
    private JPanel avaImage;
    private JLabel userName;
    private JPanel buddyPanel;
    private JButton buddyEditButton;
    private JPanel buddyInfo;
    private JButton sendMessageButton;
    private JScrollPane messageTextScrollPane;
    private JTextArea messageTextArea;
    private JPanel contactsPanel;

    public FormMain() {
        contactsPanel.add(new JPanel());
        messagesPanel.add(new JPanel());

    }

    public Component getContactsPanel() {
        return contactsPanel.getComponent(0);
    }

    public void setContactsPanel(JPanel contactsPanel) {
        this.contactsPanel.removeAll();
        this.contactsPanel.add(contactsPanel);
    }

    public JButton getBtGear() {
        return btGear;
    }

    public JPanel getMessagesPanel() {
        return messagesPanel;
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
        rootPanel = this;
        btGear = new ImageButton(Images.getGearIcon());
        logoMicro = new ImagePanel(Images.getLogoMicro(), false, true, 0);
        avaImage = new PhotoPanel(null,true,true,0,false);


    }

    public void setMessagesPanel(Component messagesPanel) {
        this.messagesPanel.removeAll();
        this.messagesPanel.add(messagesPanel);
    }

    public void addSendMessageListener(ActionListener listener) {
        this.sendMessageButton.addActionListener(listener);
    }

    public void addActionListenerForEditContactButton(ActionListener listener) {
        this.buddyEditButton.addActionListener(listener);
    }

    public void removeSendMessageListener(ActionListener listener) {
        this.sendMessageButton.removeActionListener(listener);
    }

    public String getMessageText() {
        return this.messageTextArea.getText();
    }

    public void setMessageText(String text) {
        this.messageTextArea.setText(text);
    }
}
