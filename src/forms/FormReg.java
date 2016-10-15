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
public class FormReg {
    private JPanel rootPanel;
    private JPanel namePanel;
    private JPanel surnamePanel;
    private JTextField tfName;
    private JTextField tfSurname;
    private JPanel logoPanel;
    private JButton btDone;
    private JPanel buttonPanel;

    public FormReg() {
        namePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.white));
        surnamePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.white));
        tfName.setBorder(null);
        tfSurname.setBorder(null);
    }

    public void addActionListenerForSwitchAction(ActionListener actionListener) {
        btDone.addActionListener(actionListener);
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public JTextField getTfName() {
        return tfName;
    }

    public JTextField getTfSurname() {
        return tfSurname;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = new ImagePanel(Images.getBackground(), false, true, 0);
        logoPanel = new ImagePanel(Images.getLogoMini(), false, true, 0);
        namePanel = new JPanel();
        surnamePanel = new JPanel();
        tfName = new JTextField();
        tfSurname = new JTextField();
        btDone = new ExtendedImageButton(Images.getButtonBackground());
    }

}
