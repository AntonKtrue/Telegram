package forms;

import components.ExtendedImageButton;
import components.ImagePanel;
import resources.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by Anton on 13.05.2016.
 */
public class CodeForm extends ImagePanel {
    private JButton submitButton;
    private JPasswordField codeField;
    private JPanel rootPanel;
    private JLabel phoneNumberLabel;
    private JPanel logoPanel;
    private JPanel codePanel;
    private JPanel lockImagePanel;
    private JPanel submitPanel;

    public CodeForm() {
        super(Images.getBackground(), false, true, 0);
        codePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.white));
        codeField.setBorder(null);
    }

    public void addActionListenerForSwitchAction(ActionListener actionListener) {
        submitButton.addActionListener(actionListener);
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public JPasswordField getCodeField() {
        return codeField;
    }

    public void clearCodeField() {
        codeField.setText("");
    }

    public void setPhoneNumberLabelText(String tel) {
        phoneNumberLabel.setText(tel);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = this;
        logoPanel = new ImagePanel(Images.getLogoMini(), false, true, 0);
        codePanel = new JPanel();
        lockImagePanel = new ImagePanel(Images.getIconLock(), false, true, 0);
        codeField = new JPasswordField();
        submitButton = new ExtendedImageButton(Images.getButtonBackground());
    }
}
