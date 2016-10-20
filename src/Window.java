
import forms.*;

import messages.MessagesForm;
import org.javagram.dao.Person;
import org.javagram.dao.TelegramDAO;
import org.javagram.dao.proxy.TelegramProxy;

import org.telegram.api.engine.RpcException;
import overlays.MyBufferedOverlayDialog;
import overlays.ProfileForm;
import resources.Images;
import utils.ComponentResizer;
import utils.ComponentResizerExtended;
import utils.FrameDragger;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.ParseException;


/**
 * Created by Anton on 13.05.2016.
 */
public class Window extends JFrame {
    private FormHead formHead;
    private FormTel formTel = new FormTel();
    private FormReg formReg = new FormReg();
    private FormMain formMain;
    private FormCode formCode = new FormCode();
    private FrameDragger frameDragger;
    private ComponentResizer cr;

    private TelegramDAO telegramDAO;
    private TelegramProxy telegramProxy;

    private ProfileForm profileForm = new ProfileForm();
    private MyBufferedOverlayDialog windowManager;

    private static final int MAIN_WINDOW = -1, PROFILE_FORM = 0;
    public static final String ERR_TEL = "Введите корректный номер телефона и нажмите \"Продолжить\"";
    public static final String ERR_CODE = "Вы ввели некоректный код";

    {
        formHead = new FormHead();
        setUndecorated(true);
        setSize(900, 630);
        setMinimumSize(new Dimension(900, 630));
        setTitle("kaa-work@telegram");
        initDragAndResize();
        initFormTel();
        initFromReg();
        initFormCode();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeWindow();
            }
        });
        formHead.getHeaderPanel().addMouseListener(frameDragger);
        formHead.getHeaderPanel().addMouseMotionListener(frameDragger);
        formHead.setContentPanel(formTel.getRootPanel());
        //formHead.setContentPanel(formReg.getRootPanel());
        //formHead.setContentPanel(formCode.getRootPanel());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        formHead.addActionListenerForClose((ActionEvent e) -> closeWindow());
        formHead.addActionListenerForMinimize((ActionEvent e) -> setState(Frame.ICONIFIED));
        setContentPane(formHead.getRootPanel());
        setLocationRelativeTo(null);

        setVisible(true);
    }

    public Window(TelegramDAO telegramDAO) throws Exception {
        this.telegramDAO = telegramDAO;
    }

    private void initDragAndResize() {
        frameDragger = new FrameDragger(this);
        cr = new ComponentResizerExtended(ComponentResizerExtended.KEEP_RATIO, this);
        cr.setSnapSize(new Dimension(10,10));
        cr.setMinimumSize(new Dimension(900,630));
    }

    private void initFormTel() throws IOException {
        formTel.addActionListenerForSubmit((ActionEvent e) -> {
            String telNumber;
            try {
                telNumber = formTel.getTelNumber();
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(Window.this, ex.getMessage());
                return;
            }

            if (telNumber != null) {
                try {
                    telegramDAO.acceptNumber(telNumber);
                } catch (RpcException ex) {
                    JOptionPane.showMessageDialog(Window.this, ERR_TEL);
                    return;
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return;
                }

                if(telegramDAO.canSignIn()) {
                    try {
                        telegramDAO.sendCode();
                        showFormCode();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(Window.this,"Потеряно соединение с сервером");
                        formHead.setContentPanel(formTel.getRootPanel());
                        return;
                    }
                } else {
                    formHead.setContentPanel(formReg.getRootPanel());
                }
            } else {
                JOptionPane.showMessageDialog(Window.this, ERR_TEL);
            }
        });
    }

    private void showFormCode() throws IOException {
        formCode.setLTelNumberText(telegramDAO.getPhoneNumber());
        formHead.setContentPanel(formCode.getRootPanel());
    }

    private void initFromReg() throws IOException {
        formReg.addActionListenerForSwitchAction((ActionEvent e) -> {
            try {
                telegramDAO.sendCode();
                showFormCode();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void initFormCode() throws IOException {
        formCode.addActionListenerForSwitchAction((ActionEvent e) -> {
            String code = String.valueOf(formCode.getCodeField().getPassword());
            try {
                if (telegramDAO.canSignIn()) {
                    telegramDAO.signIn(code);
                } else {
                    telegramDAO.signUp(code, formReg.getTfName().getText(), formReg.getTfSurname().getText());
                }
                telegramProxy = new TelegramProxy(telegramDAO);

                switchToFormMain();
            } catch (RpcException ex) {
                JOptionPane.showMessageDialog(Window.this, ERR_CODE);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void switchToFormMain() {
        initFormMain();
        formMain.getContactsList().setListData(telegramProxy.getPersons().toArray());
        formMain.getContactsList().setCellRenderer(new ContactItem(telegramProxy));
        windowManager = new MyBufferedOverlayDialog(formMain.getRootPanel(), profileForm);
        formHead.setContentPanel(windowManager);
    }

    private void displayDialog(Person person) {
        try {
            MessagesForm messagesForm = getMessagesForm();
            messagesForm.display(person);
            formMain.getMessagesPanel().revalidate();
            formMain.getMessagesPanel().repaint();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Проблема соединения с сервером");
        }
    }

    private MessagesForm getMessagesForm() {
        if(formMain.getMessagesPanel() instanceof MessagesForm) {
            return (MessagesForm) formMain.getMessagesPanel();
        } else {
            return createMessagesForm();
        }
    }

    private MessagesForm createMessagesForm() {
        MessagesForm messagesForm = new MessagesForm(telegramProxy);
        formMain.setMessagesPanel(messagesForm);
        formMain.getRootPanel().revalidate();
        formMain.getRootPanel().repaint();
        return messagesForm;
    }

    private void initFormMain() {
        formMain = new FormMain();
        formMain.addActionListenerForGearButton((ActionEvent e) -> {
            profileForm.setTelegramProxy(telegramProxy);
            windowManager.setIndex(PROFILE_FORM);
        });
        formMain.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(telegramProxy == null) {
                    displayDialog(null);
                } else {
                    displayDialog(formMain.getSelectedValue());
                }
            }
        });
        profileForm.addActionListenerToBackButton((ActionEvent e) -> {
            windowManager.setIndex(MAIN_WINDOW);
        });
        profileForm.addActionListenerToSubmitButton((ActionEvent e) -> {
            //Какой метод реализует account.updateUsername или его нужно самому реализовать ?
        });
        profileForm.addActionListenerToExitButton((ActionEvent e)-> {
            telegramDAO.logOut();
            formTel.clearTelNumber();
            formCode.clearCodeField();
            formHead.setContentPanel(formTel.getRootPanel());
        });
    }

    private void closeWindow() {
        if (telegramDAO.isLoggedIn()) {
            int result = JOptionPane.showOptionDialog(Window.this,
                    "Вы действительно хотите выйти?",
                    "Выход",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new Object[]{"Да","Нет"},
                    "Да");
            if(result == JOptionPane.YES_OPTION) {
                try {
                    telegramDAO.logOut();
                    dispose();
                    System.exit(0);
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        }
    }




}
