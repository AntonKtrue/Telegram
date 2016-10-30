
import components.GuiHelper;
import components.PhotoPanel;
import contacts.ContactsList;
import forms.*;

import messages.MessagesForm;
import org.javagram.dao.*;
import org.javagram.dao.Dialog;
import org.javagram.dao.proxy.TelegramProxy;

import org.javagram.dao.proxy.changes.UpdateChanges;
import org.telegram.api.engine.RpcException;
import overlays.*;

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
import java.util.Objects;


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
    private EditContactorm editContactForm  = new EditContactorm();
    private AddContactForm addContactForm = new AddContactForm();

    private MyLayeredPane contactsLayeredPane = new MyLayeredPane();
    private PlusOverlay plusOverlay = new PlusOverlay();

    private MyBufferedOverlayDialog windowManager;
    private ContactsList contactsList;

    private Timer timer;

    private static final int MAIN_WINDOW = -1, PROFILE_FORM = 0, ADD_CONTACT_FORM = 1, EDIT_CONTACT_FORM = 2;
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
        initFormMain();
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
        timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                checkForUpdates();
            }
        });
        timer.start();


    }



    private void checkForUpdates() {
        if(telegramProxy != null) {
            UpdateChanges updateChanges = telegramProxy.update();

            int photosChangedCount = updateChanges.getLargePhotosChanged().size() +
                    updateChanges.getSmallPhotosChanged().size() +
                    updateChanges.getStatusesChanged().size();
            if(updateChanges.getListChanged()) {
                updateContacts();
            } else if (photosChangedCount != 0) {
                contactsList.repaint();
            }

            Person currentBuddy = getMessagesForm().getPerson();
            Person targetPerson = contactsList.getSelectedValue();

            Dialog currentDialog = currentBuddy != null ? telegramProxy.getDialog(currentBuddy) : null;

            if(!Objects.equals(targetPerson, currentBuddy) ||
                   updateChanges.getDialogsToReset().contains(currentDialog) ||
                    updateChanges.getDialogsChanged().getDeleted().contains(currentDialog)) {
                updateMessages();
            } else if(updateChanges.getPersonsChanged().getChanged().containsKey(currentBuddy)
                    || updateChanges.getSmallPhotosChanged().contains(currentBuddy)
                    || updateChanges.getLargePhotosChanged().contains(currentBuddy)) {
                displayBuddy(targetPerson);
            }

            if(updateChanges.getPersonsChanged().getChanged().containsKey(telegramProxy.getMe())
                    || updateChanges.getSmallPhotosChanged().contains(telegramProxy.getMe())
                    || updateChanges.getLargePhotosChanged().contains(telegramProxy.getMe())) {
               displayMe(telegramProxy.getMe());
            }

        }
    }

    private void displayMe(Me me) {

    }

    private void displayBuddy(Person person) {

    }

    private void updateContacts() {
        Person person = contactsList.getSelectedValue();

    }

    private void updateMessages() {
        displayDialog(contactsList.getSelectedValue());
        formMain.revalidate();
        formMain.repaint();
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
                telNumber = null;
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
                if(telegramDAO.isLoggedIn()) {
                    telegramProxy = new TelegramProxy(telegramDAO);
                    profileForm.getBtExit().setEnabled(true);
                    formMain.getBtGear().setEnabled(true);
                    Person me = telegramProxy.getMe();
                    formMain.setAvaImage(new PhotoPanel(null, true, false, 0, false));
                    ((PhotoPanel)formMain.getAvaImage()).setImage(GuiHelper.getPhoto(telegramProxy, me, true, true));
                    formMain.getUserName().setText(me.getFirstName()+ " " + me.getLastName());
                    formMain.getAvaImage().revalidate();
                    formMain.getAvaImage().repaint();
                    formMain.revalidate();
                    formMain.repaint();
                }



                switchToFormMain();
            } catch (RpcException ex) {
                JOptionPane.showMessageDialog(Window.this, ERR_CODE);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void switchToFormMain() {
        //contactsList.getContactsList().setListData(telegramProxy.getPersons().toArray());
        //formMain.getContactsList().setCellRenderer(new ContactItem(telegramProxy));
        contactsList.setTelegramProxy(telegramProxy);
        windowManager = new MyBufferedOverlayDialog(formMain, profileForm, addContactForm, editContactForm);
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
        formMain.revalidate();
        formMain.repaint();
        return messagesForm;
    }

    private void initFormMain() {

        formMain = new FormMain();
        contactsList = new ContactsList();
        //formMain.setContactsPanel(contactsList);
        formMain.setContactsPanel(contactsLayeredPane);
        contactsLayeredPane.add(contactsList, new Integer(0));
        contactsLayeredPane.add(plusOverlay, new Integer(1));
        plusOverlay.addActionListenerToPlusButton((ActionEvent e) -> {
            windowManager.setIndex(ADD_CONTACT_FORM);
        });

        formMain.addActionListenerForGearButton((ActionEvent e) -> {
            profileForm.setTelegramProxy(telegramProxy);
            windowManager.setIndex(PROFILE_FORM);
        });
        formMain.addActionListenerForEditContactButton((ActionEvent e)-> {
            windowManager.setIndex(EDIT_CONTACT_FORM);
            editContactForm.setPhoto(GuiHelper.getPhoto(telegramProxy, contactsList.getSelectedValue(), true, true));
            editContactForm.setTel(((Contact) contactsList.getSelectedValue()).getPhoneNumber());
            editContactForm.setContactNameFieldText(contactsList.getSelectedValue().getFirstName() + " " +
                contactsList.getSelectedValue().getLastName());

        });
        formMain.addSendMessageListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Person buddy =  contactsList.getSelectedValue();
                String text = formMain.getMessageText().trim();
                if(telegramProxy != null && buddy != null && !text.isEmpty()) {
                    try {
                        telegramProxy.sendMessage(buddy, text);
                        formMain.setMessageText("");
                        checkForUpdates();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(formMain, "Не могу отправить сообщение");
                    }
                }
            }
        });

        contactsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(telegramProxy == null) {
                    displayDialog(null);
                } else {
                    displayDialog(contactsList.getSelectedValue());
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
            logOut();
        });

        editContactForm.addActionListenerForBackButton((ActionEvent e) -> {
            windowManager.setIndex(MAIN_WINDOW);
        });

        addContactForm.addActionListenerForBackButton((ActionEvent e) -> {
            windowManager.setIndex(MAIN_WINDOW);
        });


    }

    private void logOut() {
        if(telegramDAO.logOut()) {
            destroyTelegramProxy();
            formTel.clearTelNumber();
            formCode.clearCodeField();
            formHead.setContentPanel(formTel.getRootPanel());
            profileForm.getBtExit().setEnabled(false);
            formMain.getBtGear().setEnabled(false);
            //telegramDAO.acceptNumber(); сбросить номер в DAO ?
        } else {
            JOptionPane.showMessageDialog(this,"Ошибка выхода!");
        }
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
                    exit();
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        } else {
            try {
                exit();
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
    }

    private void exit() {
        telegramDAO.close();
        System.exit(0);
    }

    private void destroyTelegramProxy() {
        telegramProxy = null;
        contactsList.setTelegramProxy(telegramProxy);
    }




}
