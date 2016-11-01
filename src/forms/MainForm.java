package forms;

import components.GuiHelper;
import components.ImageButton;
import components.ImagePanel;
import components.PhotoPanel;
import org.javagram.dao.Contact;
import org.javagram.dao.Person;
import resources.Fonts;
import resources.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * Created by Anton on 22.05.2016.
 */
public class MainForm extends JPanel {
    private JPanel rootPanel;
    private JPanel topMenu;
    private JPanel leftPanel;
    private JPanel searchPanel;

    private JButton btGear;
    private JPanel rightPanel;
    private JPanel messagesPanel;
    private JPanel messagePanel;
    private JPanel logoMicro;

    private JPanel mePanel;
    private JLabel meText;
    private JPanel buddyPanel;
    private JButton buddyEditButton;
    private JButton sendMessageButton;
    private JScrollPane messageTextScrollPane;
    private JTextArea messageTextArea;
    private JPanel contactsPanel;
    private JPanel titlePanel;

    private BufferedImage buddyPhoto;
    private BufferedImage myPhoto;
    private String buddyName;
    private String myName;

    public MainForm() {
        contactsPanel.add(new JPanel());
        messagesPanel.add(new JPanel());

    }

    public Component getContactsPanel() {
        return contactsPanel.getComponent(0);
    }

    public void setContactsPanel(Component contactsPanel) {
        this.contactsPanel.removeAll();
        this.contactsPanel.add(contactsPanel);
    }

    public JButton getBtGear() {
        return btGear;
    }

    public JPanel getMessagesPanel() {
        return messagesPanel;
    }

      public JLabel getMeText() {
        return meText;
    }


    public void addActionListenerForGearButton(ActionListener actionListener) {
        btGear.addActionListener(actionListener);
    }

    public void setMyContact(Person me) {
        myName = me.getFirstName() + " " + me.getLastName();
    }

    public void setMyPhoto(BufferedImage myPhoto) {
        this.myPhoto = myPhoto;
    }

    public void setBuddyContact(Person person) {
        if(person != null) {
            btGear.setEnabled(true);
            buddyName = person.getFirstName() + " " + person.getLastName();
        } else {
            btGear.setEnabled(false);
        }

    }

    public void setBuddyPhoto(BufferedImage image) {
        this.buddyPhoto = image;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = this;
        btGear = new ImageButton(Images.getGearIcon());
        buddyEditButton = new ImageButton(Images.getIconEdit());
        logoMicro = new ImagePanel(Images.getLogoMicro(), false, true, 0);

        topMenu = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                int leftMostPoint = btGear.getX();
                int rightMostPoint = 12;

                if (myName != null) {
                    int inset = 10;
                    Font font = Fonts.getOpenSansRegular().deriveFont(Font.TRUETYPE_FONT, 14);
                    Color color = Color.decode("#94dbf7");
                    String text = myName;
                    leftMostPoint = GuiHelper.drawText(g, text, color, font, rightMostPoint, 0, leftMostPoint - rightMostPoint, this.getHeight() , inset, true);
                }
                if (myPhoto != null) {
                    int inset = 5;
                    BufferedImage image = myPhoto;
                    GuiHelper.drawImage(g, image, rightMostPoint, 0 , leftMostPoint - rightMostPoint, this.getHeight(), inset, true);
                }
            }
        };

        buddyPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);

                int leftMostPoint = buddyEditButton.getX();
                int rightMostPoint = 2;

                if (buddyPhoto != null) {
                    int inset = 10;
                    BufferedImage image = buddyPhoto;
                    rightMostPoint = GuiHelper.drawImage(graphics, image, rightMostPoint, 0, leftMostPoint - rightMostPoint, this.getHeight(), inset, false);
                }

                if (buddyName != null) {
                    int inset = 5;
                    Font font = Fonts.getOpenSansRegular().deriveFont(Font.TRUETYPE_FONT, 14);
                    Color color = Color.decode("#949494");
                    String text = buddyName;
                    GuiHelper.drawText(graphics, text, color, font, rightMostPoint, 0, leftMostPoint - rightMostPoint, this.getHeight(), inset, false);
                }

            }
        };

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
