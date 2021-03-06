package overlays;

import components.*;
import resources.Images;
import utils.PhoneFormat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;

/**
 * Created by user on 30.10.16.
 */
public class AddContactForm extends OverlayBackground {
    private JPanel rootPanel;
    private JLabel addPersonLabel;
    private JPanel rowPanel1;
    private JPanel rowPanel3;
    private JPanel rowPanel2;
    private JButton backButton;
    private JPasswordField passwordField1;
    private JButton saveButton;
    private JPanel telIcon;
    private JFormattedTextField phoneField;
    private JTextField firstNameField;
    private JTextField lastNameField;

    {
        rowPanel1.setBorder(BorderFactory.createMatteBorder(0,0,2,0, Color.white));
        rowPanel2.setBorder(BorderFactory.createMatteBorder(0,0,2,0, Color.white));
        rowPanel3.setBorder(BorderFactory.createMatteBorder(0,0,2,0, Color.white));
        firstNameField.setBorder(null);
        lastNameField.setBorder(null);
        phoneField.setBorder(null);
        try {
            phoneField.setFormatterFactory(PhoneFormat.getRussianPhoneFormat());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = this;
        rowPanel1 = new JPanel();
        rowPanel2 = new JPanel();
        rowPanel3 = new JPanel();
        telIcon = new ImagePanel(Images.getIconPhone(),false,true,0);
        backButton = new ImageButton(Images.getBackIcon());
        saveButton = new AntiAliasedImageButton(Images.getButtonBackground());
        phoneField = new JFormattedTextField();
        firstNameField = new JTextField();
        lastNameField = new JTextField();

    }

    public void clearFields() {
        phoneField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
    }

    public void addActionListenerForBackButton(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    public void addActionListenerForSaveButton(ActionListener listener) {
        saveButton.addActionListener(listener);
    }

    public ContactInfo getContactInfo() {

        return new ContactInfo(phoneField.getText().trim(),
                firstNameField.getText(),
                lastNameField.getText());
    }
}
