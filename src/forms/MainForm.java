package forms;

import components.*;
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
    private JButton searchButton;
    private JTextField searchField;
    private JPanel titlePanel;

    private BufferedImage buddyPhoto;
    private BufferedImage myPhoto;
    private String buddyName;
    private String myName;

    private GridBagConstraints messagesFormConstraints;
    {
        messagesFormConstraints = new GridBagConstraints();
        messagesFormConstraints.insets = new Insets(0,15,0,15);
        messagesFormConstraints.weightx = 1.0;
        messagesFormConstraints.weighty = 1.0;
        messagesFormConstraints.fill = GridBagConstraints.BOTH;
    }

    public MainForm() {
        contactsPanel.add(new JPanel());
        messagesPanel.add(new JPanel(),messagesFormConstraints);
        messagesPanel.getComponent(0).setBackground(Color.white);
        messageTextScrollPane.setBorder(null);
        messageTextScrollPane.setBackground(Color.white);
        messageTextScrollPane.setOpaque(true);
        messagesPanel.repaint();
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

    public Component getMessagesPanel() {
        return messagesPanel.getComponent(0);
    }

    public void addActionListenerForGearButton(ActionListener actionListener) {
        btGear.addActionListener(actionListener);
    }

    public void setMyContact(Person me) {
        myName = me != null ? me.getFirstName() + " " + me.getLastName() : "";

    }

    public void setMyPhoto(BufferedImage myPhoto) {
        this.myPhoto = myPhoto;
    }

    public void setBuddyContact(Person person) {
        buddyName = person != null ? person.getFirstName() + " " + person.getLastName() : "";
    }

    public void setBuddyPhoto(BufferedImage image) {
        this.buddyPhoto = image;
    }

    public void addSearchEventListener(ActionListener listener) {
        this.searchButton.addActionListener(listener);
    }

    public String getSearchText() {
        return this.searchField.getText();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = this;
        btGear = new ImageButton(Images.getGearIcon());
        searchButton = new ImageButton(Images.getIconSearch());
        searchField = new HintTextFieldUnderlined("", "Поиск...", false, false);
        buddyEditButton = new ImageButton(Images.getIconEdit());
        logoMicro = new ImagePanel(Images.getLogoMicro(), false, true, 0);
        topMenu = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D graphics2D = (Graphics2D) g;

                //Set  anti-alias!
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                // Set anti-alias for text
                graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                int leftMostPoint = btGear.getX();
                int rightMostPoint = 12;

                if (myName != null) {
                    int inset = 10;
                    Font font = Fonts.getOpenSansRegular().deriveFont(Font.TRUETYPE_FONT, 14);
                    Color color = Color.decode("#94dbf7");
                    String text = myName;
                    leftMostPoint = GuiHelper.drawText(graphics2D, text, color, font, rightMostPoint, 0, leftMostPoint - rightMostPoint, this.getHeight() , inset, true);
                }
                if (myPhoto != null) {
                    int inset = 5;
                    BufferedImage image = myPhoto;
                    GuiHelper.drawImage(graphics2D, image, rightMostPoint, 0 , leftMostPoint - rightMostPoint, this.getHeight(), inset, true);
                }
            }
        };
        buddyPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                Graphics2D graphics2D = (Graphics2D) graphics;

                //Set  anti-alias!
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                // Set anti-alias for text
                graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                int leftMostPoint = buddyEditButton.getX();
                int rightMostPoint = 2;

                if (buddyPhoto != null) {
                    int inset = 10;
                    BufferedImage image = buddyPhoto;
                    rightMostPoint = GuiHelper.drawImage(graphics2D, image, rightMostPoint, 0, leftMostPoint - rightMostPoint, this.getHeight(), inset, false);
                }

                if (buddyName != null) {
                    int inset = 5;
                    Font font = Fonts.getOpenSansRegular().deriveFont(Font.TRUETYPE_FONT, 14);
                    Color color = Color.decode("#949494");
                    String text = buddyName;
                    GuiHelper.drawText(graphics2D, text, color, font, rightMostPoint, 0, leftMostPoint - rightMostPoint, this.getHeight(), inset, false);
                }

            }
        };

        messagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int RADIUS = 5;
                Graphics2D graphics2D = (Graphics2D) g;
                //Set  anti-alias!
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                // Set anti-alias for text
                graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                graphics2D.setColor(Color.decode("#e0e0e0")); //e0e0e0
                graphics2D.fillRoundRect(messageTextScrollPane.getX()-5, messageTextScrollPane.getY()-5, messageTextScrollPane.getWidth()+sendMessageButton.getWidth(), sendMessageButton.getHeight(), RADIUS, RADIUS);

            }
        };
        //messageTextScrollPane = new JScrollPane();
        messageTextArea = new JTextArea();
        sendMessageButton = new AntiAliasedImageButton(Images.getSendMessageImage());

    }

    public void setMessagesPanel(Component messagesPanel) {
        this.messagesPanel.removeAll();
        this.messagesPanel.add(messagesPanel, messagesFormConstraints);
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
