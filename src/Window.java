
import forms.*;

import org.javagram.dao.TelegramDAO;
import org.javagram.dao.proxy.TelegramProxy;

import org.telegram.api.engine.RpcException;
import overlays.MyBufferedOverlayDialog;
import overlays.ProfileForm;
import utils.ComponentResizer;
import utils.ComponentResizerExtended;
import utils.FrameDragger;

import javax.swing.*;
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
        formHead.addActionListenerForClose((ActionEvent e) -> closeWindow());
        formHead.addActionListenerForMinimize((ActionEvent e) -> setState(Frame.ICONIFIED));
        setContentPane(formHead.getRootPanel());
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
                formMain = new FormMain(telegramDAO, telegramProxy);
                initFormMain();
                windowManager = new MyBufferedOverlayDialog(formMain.getRootPanel(), profileForm);
                formHead.setContentPanel(windowManager);
            } catch (RpcException ex) {
                JOptionPane.showMessageDialog(Window.this, ERR_CODE);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void initFormMain() throws IOException {
        formMain.addActionListenerForGearButton((ActionEvent e) -> {
            profileForm.setTelegramProxy(telegramProxy);
            windowManager.setIndex(PROFILE_FORM);
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
            try {
                telegramDAO.logOut();
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
        dispose();
        System.exit(0);
    }


}
