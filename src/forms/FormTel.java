package forms;

import components.ExtendedImageButton;
import components.ImagePanel;
import resources.Images;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Anton on 13.05.2016.
 */
public class FormTel {
    private JButton btSubmit;
    private JPanel rootPanel;
    private JFormattedTextField ftfTelNumber;
    private JPanel logoPanel;
    private JPanel telPanel;
    private JPanel telIcon;

    public FormTel() throws IOException {

        try {
            MaskFormatter telMask = new MaskFormatter("+7 ### ### ## ##");
            DefaultFormatterFactory factory = new DefaultFormatterFactory(telMask);
            telMask.setPlaceholderCharacter(' ');
            ftfTelNumber.setFormatterFactory(factory);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        ftfTelNumber.setBorder(null);
        telPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.white));

    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public void addActionListenerForSubmit(ActionListener actionListener) {
        btSubmit.addActionListener(actionListener);
    }

    public String getTelNumber() throws ParseException {
        ftfTelNumber.commitEdit();
        return (String) ftfTelNumber.getValue();
    }

    public void clearTelNumber() {
        ftfTelNumber.setText("");
        ftfTelNumber.setValue("");
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = new ImagePanel(Images.getBackground(), false, true, 0);
        logoPanel = new ImagePanel(Images.getLogo(), false, true, 0);
        ftfTelNumber = new JFormattedTextField();
        telPanel = new JPanel();
        telIcon = new ImagePanel(Images.getIconPhone(),false,true,0);
        btSubmit = new ExtendedImageButton(Images.getButtonBackground());
    }

}
