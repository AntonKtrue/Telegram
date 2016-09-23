package overlays;

import components.ExtendedImageButton;
import components.ImageButton;
import components.OverlayBackground;
import org.javagram.dao.proxy.TelegramProxy;
import resources.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by Anton on 17.08.2016.
 */
public class ProfileForm extends OverlayBackground {
    private JPanel rootPanel;
    private JTextField tfName;
    private JTextField tfSurname;
    private JButton btSubmit;
    private JButton btBack;
    private JButton btExit;
    private JLabel lTel;

    private TelegramProxy telegramProxy;

    public void setTelegramProxy(TelegramProxy telegramProxy) {
        this.telegramProxy = telegramProxy;
        tfName.setText(telegramProxy.getMe().getFirstName());
        tfSurname.setText(telegramProxy.getMe().getLastName());
        lTel.setText(telegramProxy.getMe().getPhoneNumber());
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = this;
        btSubmit = new ExtendedImageButton(Images.getButtonBackground());
        btBack = new ImageButton(Images.getBackIcon());
        tfName = new JTextField();
        tfSurname = new JTextField();
        tfName.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.white));
        tfSurname.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.white));
    }

    public void addActionListenerToBackButton(ActionListener actionListener) {
        btBack.addActionListener(actionListener);
    }
    public void addActionListenerToSubmitButton(ActionListener actionListener) {
        btSubmit.addActionListener(actionListener);
    }
    public void addActionListenerToExitButton(ActionListener actionListener) {
        btExit.addActionListener(actionListener);
    }
}
