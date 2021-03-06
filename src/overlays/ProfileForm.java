package overlays;

import components.AntiAliasedImageButton;
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

    private JLabel lTel;

    private JButton btExit;

    private TelegramProxy telegramProxy;

    public ProfileForm() {
        btExit.setEnabled(false);
        tfName.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.white));
        tfSurname.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.white));
        btExit.setBorder(BorderFactory.createMatteBorder(0,0,1,0, btExit.getForeground()));
    }

    public void setTelegramProxy(TelegramProxy telegramProxy) {
        this.telegramProxy = telegramProxy;

        tfName.setText(telegramProxy.getMe().getFirstName());
        tfSurname.setText(telegramProxy.getMe().getLastName());
        lTel.setText(telegramProxy.getMe().getPhoneNumber());
    }

    public JButton getBtExit() {
        return btExit;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = this;
        btSubmit = new AntiAliasedImageButton(Images.getButtonBackground());
        btBack = new ImageButton(Images.getBackIcon());
        tfName = new JTextField();
        tfSurname = new JTextField();

        btExit = new JButton();


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
