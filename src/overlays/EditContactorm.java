package overlays;

import components.ExtendedImageButton;
import components.GuiHelper;
import components.ImageButton;
import components.OverlayBackground;
import org.javagram.dao.Contact;
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
public class EditContactorm extends OverlayBackground {
    private JPanel rootPanel;
    private JPanel contactPanel;
    private JPanel namePanel;
    private JTextField contactNameField;
    private JLabel telLabel;
    private JButton deleteContact;
    private JButton backButton;
    private JButton saveButton;
    private BufferedImage buddyPhoto;

    {
        namePanel.setOpaque(false);
        contactNameField.setBorder(null);
        deleteContact.setBorder(BorderFactory.createMatteBorder(2,2,2,2, Color.decode("#f74d4e")));
    }

    public void setPhoto(BufferedImage photo) {
        this.buddyPhoto = photo;
    }

    public void setTel(String telNumber) {
        telLabel.setText(telNumber);
    }

    public void setContactNameFieldText(String userName) {
        contactNameField.setText(userName);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = this;
        contactPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);

                if (buddyPhoto != null) {
                    BufferedImage image = buddyPhoto;
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
