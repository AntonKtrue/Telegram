package forms;

import components.AntiAliasedImageButton;
import components.ImagePanel;
import resources.Images;
import utils.PhoneFormat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Anton on 13.05.2016.
 */
public class PhoneForm extends ImagePanel {
    private JButton submitButton;
    private JPanel rootPanel;
    private JFormattedTextField phoneNumberField;
    private JPanel logoPanel;
    private JPanel phonePanel;
    private JPanel phoneImagePanel;

   public PhoneForm() throws IOException {
       super(Images.getBackground(), false, true, 0);
        try {
            phoneNumberField.setFormatterFactory(PhoneFormat.getRussianPhoneFormat());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        phoneNumberField.setBorder(null);
        phonePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.white));
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public void addActionListenerForSubmit(ActionListener actionListener) {
        submitButton.addActionListener(actionListener);
    }

    public String getTelNumber() throws ParseException {
        phoneNumberField.commitEdit();
        return (String) phoneNumberField.getValue();
    }

    public void clearTelNumber() {
        phoneNumberField.setText("");
        phoneNumberField.setValue("");
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = this;
        logoPanel = new ImagePanel(Images.getLogo(), false, true, 0);
        phoneNumberField = new JFormattedTextField();
        phonePanel = new JPanel();
        phoneImagePanel = new ImagePanel(Images.getIconPhone(),false,true,0);
        submitButton = new AntiAliasedImageButton(Images.getButtonBackground());
    }

}
