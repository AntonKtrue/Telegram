package forms;

import components.ExtendedImageButton;
import components.ImagePanel;
import resources.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by Anton on 21.06.2016.
 */
public class RegistrationForm extends ImagePanel {
    private JPanel rootPanel;
    private JPanel firstNamePanel;
    private JPanel lastNamePanel;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JPanel logoPanel;
    private JButton submitButton;
    private JPanel submitPanel;

    public RegistrationForm() {
        super(Images.getBackground(), false, true, 0);
        firstNamePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.white));
        lastNamePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.white));
        firstNameField.setBorder(null);
        lastNameField.setBorder(null);
    }

    public void addActionListenerForSubmitButton(ActionListener actionListener) {
        submitButton.addActionListener(actionListener);
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public JTextField getFirstNameField() {
        return firstNameField;
    }

    public JTextField getLastNameField() {
        return lastNameField;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = this;
        logoPanel = new ImagePanel(Images.getLogoMini(), false, true, 0);
        firstNamePanel = new JPanel();
        lastNamePanel = new JPanel();
        firstNameField = new JTextField();
        lastNameField = new JTextField();
        submitButton = new ExtendedImageButton(Images.getButtonBackground());
    }

}
