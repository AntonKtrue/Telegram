package utils;

import components.ImageButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by HerrSergio on 24.05.2016.
 */
public class Undecorated extends JPanel {
    private JPanel rootPanel;
    private JPanel topPanel;
    private JPanel contentPanel;
    private JButton closeButton;
    private JButton minimizeButton;

    private ComponentMover componentMover;
    private ComponentResizerAbstract componentResizerExtended;
    private String title;

    public static final int CLOSE_BUTTON = 1, MINIMIZE_BUTTON = 4;
    public static final int BIND_CLOSE_BUTTON = 16, BIND_MINIMIZE_BUTTON = 64;

    public static final int MINIMIZE_CLOSE_BUTTONS = CLOSE_BUTTON | MINIMIZE_BUTTON;
    public static final int NO_BUTTONS = 0;
    public static final int BIND_MINIMIZE_CLOSE_BUTTONS = BIND_CLOSE_BUTTON | BIND_MINIMIZE_BUTTON;

    public static final int WINDOW_DEFAULT_RESIZE_POLICY = ComponentResizerAbstract.KEEP_RATIO_CENTER;
    public static final int FRAME_DEFAULT_RESIZE_POLICY = ComponentResizerAbstract.SIMPLE;
    public static final int DIALOG_DEFAULT_RESIZE_POLICY = -1;

    public static final int WINDOW_DEFAULT_BUTTONS = NO_BUTTONS;
    public static final int FRAME_DEFAULT_BUTTONS= MINIMIZE_CLOSE_BUTTONS | BIND_MINIMIZE_CLOSE_BUTTONS;
    public static final int DIALOG_DEFAULT_BUTTONS = CLOSE_BUTTON | BIND_CLOSE_BUTTON;



    public Undecorated(Window window, int resizePolicy, int buttons) {

        init(window, resizePolicy, buttons);
        initWindow();
        initContent(window);
        initValidation();
    }

    public Undecorated(Window window, int resizePolicy) {
        this(window, resizePolicy, WINDOW_DEFAULT_BUTTONS);
    }

    public Undecorated(Window window) {
        this(window, WINDOW_DEFAULT_RESIZE_POLICY);
    }


    public Undecorated(Frame window, int resizePolicy, int buttons) {
        init(window, resizePolicy, buttons);
        initFrame(window, buttons);
        initContent(window);
        initValidation();
    }

    public Undecorated(Frame window, int resizePolicy) {
        this(window, resizePolicy, FRAME_DEFAULT_BUTTONS);
    }

    public Undecorated(Frame window) {
        this(window, FRAME_DEFAULT_RESIZE_POLICY);
    }

    public Undecorated(Dialog window, int resizePolicy, int buttons) {
        init(window, resizePolicy, buttons);
        initDialog(window);
        initContent(window);
        initValidation();
    }

    public Undecorated(Dialog window, int resizePolicy) {
        this(window, resizePolicy, DIALOG_DEFAULT_BUTTONS);
    }

    public Undecorated(Dialog window) {
        this(window, DIALOG_DEFAULT_RESIZE_POLICY);
    }

    public Undecorated(JWindow window, int resizePolicy, int buttons){
        init(window, resizePolicy, buttons);
        initWindow();
        initContentPane(window);
        initValidation();
    }

    public Undecorated(JWindow window, int resizePolicy) {
        this(window, resizePolicy, WINDOW_DEFAULT_BUTTONS);
    }

    public Undecorated(JWindow window) {
        this(window, WINDOW_DEFAULT_RESIZE_POLICY);
    }

    public Undecorated(JFrame window, int resizePolicy, int buttons){
        init(window, resizePolicy, buttons);
        initFrame(window, buttons);
        initContentPane(window);
        initValidation();
    }

    public Undecorated(JFrame window, int resizePolicy) {
        this(window, resizePolicy, FRAME_DEFAULT_BUTTONS);
    }

    public Undecorated(JFrame window) {
        this(window, FRAME_DEFAULT_RESIZE_POLICY);
    }

    public Undecorated(JDialog window, int resizePolicy, int buttons){
        init(window, resizePolicy, buttons);
        initDialog(window);
        initContentPane(window);
        initValidation();

    }

    public Undecorated(JDialog window, int resizePolicy) {
        this(window, resizePolicy, DIALOG_DEFAULT_BUTTONS);
    }

    public Undecorated(JDialog window) {
        this(window, DIALOG_DEFAULT_RESIZE_POLICY);
    }

    private void init(Window window, int resizePolicy, int buttons) {

        setPreferredSize(null);
        setMinimumSize(null);
        setMaximumSize(null);

        if((buttons & CLOSE_BUTTON) == 0)
            topPanel.remove(closeButton);
        else if((buttons & BIND_CLOSE_BUTTON) != 0)
            addActionListenerForClose(e -> window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING)));

        if((buttons & MINIMIZE_BUTTON) == 0)
            topPanel.remove(minimizeButton);


        componentMover = new ComponentMover(window, topPanel);

        if(resizePolicy < 0)
            return;

        componentResizerExtended = new ComponentResizerAbstract(resizePolicy, window) {

            @Override
            protected int getExtraHeight() {
                return rootPanel.getHeight() - contentPanel.getHeight();
            }

            @Override
            protected int getExtraWidth() {
                return rootPanel.getWidth() - contentPanel.getWidth();
            }
        };

    }

    private void initWindow() {
        title = "";
    }

    private void initFrame(Frame window, int buttons) {
        title = window.getTitle();
        if((buttons & MINIMIZE_BUTTON) != 0 && (buttons &  BIND_MINIMIZE_BUTTON) != 0) {
            addActionListenerForMinimize(e -> window.setState(Frame.ICONIFIED));
        }
        window.setUndecorated(true);
    }

    private void initDialog(Dialog window) {
        title = window.getTitle();
        window.setUndecorated(true);
    }

    private void initContent(Window window) {
        window.removeAll();
        window.setLayout(new BorderLayout());
        window.add(this, BorderLayout.CENTER);
    }

    private void initContentPane(RootPaneContainer window) {
        window.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        this.setContentPanel(window.getContentPane());
        window.setContentPane(this);
    }

    private void initValidation() {
        revalidate();
        repaint();
    }

    public void setContentPanel(Component component) {
        contentPanel.removeAll();
        contentPanel.add(component);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = this;

  //      closeButton = new ImageButton(Images.get)
//                new JButton() {
//            @Override
//            protected void paintComponent(Graphics graphics) {
//                //super.paintComponent(graphics);
//                int width = this.getWidth();
//                int height = this.getHeight();
//                int x = width / 6;
//                int y = height / 6;
//
//                graphics.drawLine(x, y, width - x, height - y);
//                graphics.drawLine(x, height - y, width - x, y);
//
//            }
//        };

//        minimizeButton = new JButton() {
//            @Override
//            protected void paintComponent(Graphics graphics) {
//                //super.paintComponent(graphics);
//                int width = this.getWidth();
//                int height = this.getHeight();
//                int x = width / 6;
//                int y = (height * 2) / 3;
//
//                graphics.drawLine(x, y, width - x, y);
//
//            }
//        };

        topPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);

                Font font = getFont();
                graphics.setFont(font);

                FontMetrics fontMetrics = graphics.getFontMetrics();
                int height = fontMetrics.getAscent();// + fontMetrics.getDescent();
                int pos = (this.getHeight() + height) / 2;

                int start = 4;
                int end = this.getWidth();
                for(Component component : getComponents()) {
                    if(component.getX() < end)
                        end = component.getX();
                }
                end -= 4;
                String text = title;

                while(fontMetrics.stringWidth(text) > end - start) {
                    int len = text.length() - 4;
                    if(len < 0)
                        return;
                    else
                        text = text.substring(0, len) + "...";
                }

                graphics.setColor(Color.black);
                graphics.setFont(graphics.getFont());
                graphics.drawString(text, start, pos);
            }
        };
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        repaint();
    }

    public void addActionListenerForClose(ActionListener listener) {
        closeButton.addActionListener(listener);
    }

    public void addActionListenerForMinimize(ActionListener listener) {
        minimizeButton.addActionListener(listener);
    }

    public void removeActionListenerForClose(ActionListener listener) {
        closeButton.removeActionListener(listener);
    }

    public void removeActionListenerForMinimize(ActionListener listener) {
        minimizeButton.removeActionListener(listener);
    }

    //--------------------------------------------------------------------------//

    public static int showDialog(Frame frame, Object message, String title, int messageType, int optionType, Icon icon, Object[] options, Object initialValue) {
        return showDialog(new JDialog(frame, title, true), message, messageType, optionType, icon, options, initialValue);
    }

    public static int showDialog(Dialog dialog, Object message, String title, int messageType, int optionType, Icon icon, Object[] options, Object initialValue) {
        return showDialog(new JDialog(dialog, title, true), message, messageType, optionType, icon, options, initialValue);
    }

    public static int showDialog(Frame frame, Object message, String title, int messageType, int optionType, Icon icon) {
        return showDialog(new JDialog(frame, title, true), message, messageType, optionType, icon, null, null);
    }

    public static int showDialog(Dialog dialog, Object message, String title, int messageType, int optionType, Icon icon) {
        return showDialog(new JDialog(dialog, title, true), message, messageType, optionType, icon, null, null);
    }

    public static int showDialog(Frame frame, Object message, String title, int messageType, int optionType) {
        return showDialog(new JDialog(frame, title, true), message, messageType, optionType, null, null, null);
    }

    public static int showDialog(Dialog dialog, Object message, String title, int messageType, int optionType) {
        return showDialog(new JDialog(dialog, title, true), message, messageType, optionType, null, null, null);
    }

    private static int showDialog(JDialog dialog, Object message, int messageType, int optionType, Icon icon, Object[] options, Object initialValue) {
        UIManager UI = new UIManager();
        UI.put("OptionPane.background", Color.white);
        UI.put("Panel.background", Color.white);

        JOptionPane optionPane = new JOptionPane(message, messageType, optionType, icon, options, initialValue);

        dialog.setModal(true);
        dialog.setContentPane(optionPane);
        dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        new Undecorated(dialog);
        dialog.pack();
        dialog.setLocationRelativeTo(dialog.getParent());
        Map<ActionListener, AbstractButton> listeners = new HashMap<>();
        if(options != null) {
            for (Object option : options) {
                if(option instanceof AbstractButton) {
                    AbstractButton abstractButton = (AbstractButton)option;
                    ActionListener actionListener = actionEvent -> optionPane.setValue(option);
                    abstractButton.addActionListener(actionListener);
                    listeners.put(actionListener, abstractButton);
                }
            }
        }
        PropertyChangeListener propertyChangeListener = propertyChangeEvent -> dialog.setVisible(false);
        optionPane.addPropertyChangeListener("value", propertyChangeListener);
        dialog.setVisible(true);
        optionPane.removePropertyChangeListener("value", propertyChangeListener);
        for(Map.Entry<ActionListener, AbstractButton> entry : listeners.entrySet())
            entry.getValue().removeActionListener(entry.getKey());
        Object selectedValue = optionPane.getValue();
        if(selectedValue == null)
            return JOptionPane.CLOSED_OPTION;

        //If there is not an array of option buttons:
        if(options == null) {
            if(selectedValue instanceof Integer)
                return ((Integer)selectedValue);
            else
                return JOptionPane.CLOSED_OPTION;
        }
        //If there is an array of option buttons:
        for(int counter = 0, maxCounter = options.length; counter < maxCounter; counter++) {
            if(options[counter].equals(selectedValue))
                return counter;
        }
        return JOptionPane.CLOSED_OPTION;
    }
}
