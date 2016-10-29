package overlays;

import components.ExtendedImageButton;
import components.GuiHelper;
import components.ImageButton;
import components.OverlayBackground;
import org.javagram.dao.Person;
import org.javagram.dao.proxy.TelegramProxy;
import resources.Images;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * Created by Anton on 27.10.2016.
 */
public class EditContactForm extends OverlayBackground {
    private JPanel rootPanel;
    private JPanel contactPanel;
    private JPanel namePanel;
    private JTextField contactNameField;
    private JLabel telLabel;
    private JButton deleteContact;
    private JButton backButton;
    private JButton saveButton;
    private BufferedImage buddyPhoto;

    private Person person;
    private TelegramProxy telegramProxy;

    {
        //namePanel.setBorder( BorderFactory.createMatteBorder(0, 0, 2, 0, Color.white));
        namePanel.setOpaque(false);
        contactNameField.setBorder(null);
        deleteContact.setBorder(BorderFactory.createMatteBorder(2,2,2,2, Color.decode("#f74d4e")));
    }

    public void setPhoto(BufferedImage photo) {
        this.buddyPhoto = photo;
    }

    public String getTel() {
        return telLabel.getText();
    }

    public void setTelLabel(String tel) {
        this.telLabel.setText(tel);
    }

    public JTextField getContactNameField() {
        return contactNameField;
    }

    public void setContactNameField(JTextField contactNameField) {
        this.contactNameField = contactNameField;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = this;
        contactPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);

                int leftMostPoint = contactPanel.getX();
                int rightMostPoint = 2;

                if (buddyPhoto != null) {
                    int inset = 2;
                    BufferedImage image = buddyPhoto;
                   // rightMostPoint = GuiHelper.drawImage(graphics, image, rightMostPoint, 0, leftMostPoint - rightMostPoint, this.getHeight(), inset, false);
                    GuiHelper.drawLine(graphics, Color.white, 0, 40, 350, 1);
                    GuiHelper.drawImage(graphics, image, 20, 0, 90, 90);
                }



            }
        };
        namePanel = new JPanel();
        deleteContact = new JButton();
        contactNameField = new JTextField();
        backButton = new ImageButton(Images.getBackIcon());
        saveButton = new ExtendedImageButton(Images.getButtonBackground());

    }

    public void addActionListenerForBackButton(ActionListener listener) {
        backButton.addActionListener(listener);
    }
}
