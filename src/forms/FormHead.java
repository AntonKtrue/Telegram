package forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by Anton on 14.05.2016.
 */
public class FormHead {
    private JPanel rootPanel;
    private JPanel contentPanel;
    private JPanel headerPanel;
    private JButton btMinimize;
    private JButton btClose;

    public JPanel getHeaderPanel() {
        return headerPanel;
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public void addActionListenerForMinimize(ActionListener actionListener) {
        btMinimize.addActionListener(actionListener);
    }

    public void addActionListenerForClose(ActionListener actionListener) {
        btClose.addActionListener(actionListener);
    }

    public void setContentPanel(Component content) {
        contentPanel.removeAll();
        contentPanel.add(content);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

}
