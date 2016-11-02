package overlays;

import components.AntiAliasedImageButton;
import resources.Images;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Created by user on 30.10.16.
 */
public class PlusOverlay extends JPanel {
    private JButton plusButton;
    private JPanel rootPanel;



    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = this;
        plusButton = new AntiAliasedImageButton(Images.getPlus());
    }

    public void addActionListenerToPlusButton(ActionListener actionListener) {
        plusButton.addActionListener(actionListener);
    }

}
