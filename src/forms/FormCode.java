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
public class FormCode {
    private JButton btSubmit;
    private JPasswordField passwordField1;
    private JPanel rootPanel;
    private JLabel lTelNumber;
    private JPanel logoPanel;
    private JPanel codePanel;
    private JPanel lockIcon;
    private JPanel buttonPanel;

    public FormCode() {
    }

    public void addActionListenerForSwitchAction(ActionListener actionListener) {
        btSubmit.addActionListener(actionListener);
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public JPasswordField getPasswordField1() {
        return passwordField1;
    }

    public void setLTelNumberText(String tel) {
        lTelNumber.setText(tel);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

        rootPanel = new ImagePanel(Images.getBackground(), false, true, 0);

        logoPanel = new ImagePanel(Images.getLogoMini(), false, true, 0);

        codePanel = new JPanel();
        lockIcon = new ImagePanel(Images.getIconLock(), false, true, 0);

        codePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.white));
        passwordField1 = new JPasswordField();
        passwordField1.setBorder(null);
        btSubmit = new ExtendedImageButton(Images.getButtonBackground());

    }

}
