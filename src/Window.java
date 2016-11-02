
import components.GuiHelper;
import contacts.ContactsList;
import forms.*;

import jdk.nashorn.internal.scripts.JO;
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
    private HeadForm headForm;
    private PhoneForm phoneForm = new PhoneForm();
    private RegistrationForm registrationForm = new RegistrationForm();
    private MainForm mainForm;
    private CodeForm codeForm = new CodeForm();

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
    private int messagesFrozen;

    {
        headForm = new HeadForm();
        setUndecorated(true);
        setSize(900, 630);
        setMinimumSize(new Dimension(900, 630));
        setTitle("kaa-work@telegram");
        initDragAndResize();
        initPhoneForm();
        initRegistrationForm();
        initCodeForm();
        initMainForm();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeWindow();
            }
        });
        headForm.getHeaderPanel().addMouseListener(frameDragger);
        headForm.getHeaderPanel().addMouseMotionListener(frameDragger);
        headForm.setContentPanel(phoneForm.getRootPanel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        headForm.addActionListenerForClose((ActionEvent e) -> closeWindow());
        headForm.addActionListenerForMinimize((ActionEvent e) -> setState(Frame.ICONIFIED));
        setContentPane(headForm.getRootPanel());
        setLocationRelativeTo(null);
        setVisible(true);

        timer = new Timer(2000, ((ActionEvent e)->{
                checkForUpdates(false);
        }));
    }



    private void checkForUpdates(boolean force) {
        if(telegramProxy != null) {
            UpdateChanges updateChanges = telegramProxy.update(force ? TelegramProxy.FORCE_SYNC_UPDATE : TelegramProxy.USE_SYNC_UPDATE);
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
        if(me == null) {
            mainForm.setMyContact(null);
            mainForm.setMyPhoto(null);
        } else {
            mainForm.setMyContact(me);
            mainForm.setMyPhoto(GuiHelper.getPhoto(telegramProxy, me, true, true));
        }

    }

    private void displayBuddy(Person person) {
        if(person == null) {
            mainForm.setBuddyContact(null);
            mainForm.setBuddyPhoto(null);
        } else {
            mainForm.setBuddyContact(person);
            mainForm.setBuddyPhoto(GuiHelper.getPhoto(telegramProxy, person, true, true));
        }
        mainForm.repaint();
    }

    private void updateContacts() {
        Person person = contactsList.getSelectedValue();
        contactsList.setTelegramProxy(telegramProxy);
        contactsList.setSelectedValue(person);
    }

    private void updateMessages() {
        messagesFrozen++;
        try{
            displayDialog(contactsList.getSelectedValue());
            mainForm.revalidate();
            mainForm.repaint();
        } finally {
            messagesFrozen--;
        }
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

    private void initPhoneForm() throws IOException {
        phoneForm.addActionListenerForSubmit((ActionEvent e) -> {
            String telNumber;
            try {
                telNumber = phoneForm.getTelNumber();
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
                        headForm.setContentPanel(phoneForm.getRootPanel());
                        return;
                    }
                } else {
                    headForm.setContentPanel(registrationForm.getRootPanel());
                }
                telNumber = null;
            } else {
                JOptionPane.showMessageDialog(Window.this, ERR_TEL);
            }
        });
    }

    private void showFormCode() throws IOException {
        codeForm.setPhoneNumberLabelText(telegramDAO.getPhoneNumber());
        headForm.setContentPanel(codeForm.getRootPanel());
    }

    private void initRegistrationForm() throws IOException {
        registrationForm.addActionListenerForSubmitButton((ActionEvent e) -> {
            try {
                telegramDAO.sendCode();
                showFormCode();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void initCodeForm() throws IOException {
        codeForm.addActionListenerForSwitchAction((ActionEvent e) -> {
            String code = String.valueOf(codeForm.getCodeField().getPassword());
            try {
                if (telegramDAO.canSignIn()) {
                    telegramDAO.signIn(code);
                } else {
                    telegramDAO.signUp(code, registrationForm.getFirstNameField().getText(), registrationForm.getLastNameField().getText());
                }
                if(telegramDAO.isLoggedIn()) {
                    createTelegramProxy();

                    switchToFormMain();
                    timer.start();
                }
            } catch (RpcException ex) {
                JOptionPane.showMessageDialog(Window.this, ERR_CODE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(Window.this, ERR_CODE);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void updateTelegramProxy() {
        messagesFrozen++;
        try {
            contactsList.setTelegramProxy(telegramProxy);
            contactsList.setSelectedValue(null);
            createMessagesForm();
            displayDialog(null);
            displayMe(telegramProxy!= null ? telegramProxy.getMe() : null);
        } finally {
            messagesFrozen--;
        }

        mainForm.revalidate();
        mainForm.repaint();
    }

    private void createTelegramProxy() {
        telegramProxy = new TelegramProxy(telegramDAO);
        updateTelegramProxy();
    }

    private void switchToFormMain() {
        contactsList.setTelegramProxy(telegramProxy);
        windowManager = new MyBufferedOverlayDialog(mainForm, profileForm, addContactForm, editContactForm);
        headForm.setContentPanel(windowManager);
    }

    private void displayDialog(Person person) {
        try {
            MessagesForm messagesForm = getMessagesForm();
            messagesForm.display(person);
            displayBuddy(person);
            mainForm.getMessagesPanel().revalidate();
            mainForm.getMessagesPanel().repaint();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Проблема соединения с сервером");
        }
    }

    private MessagesForm getMessagesForm() {
        if(mainForm.getMessagesPanel() instanceof MessagesForm) {
            return (MessagesForm) mainForm.getMessagesPanel();
        } else {
            return createMessagesForm();
        }
    }

    private MessagesForm createMessagesForm() {
        MessagesForm messagesForm = new MessagesForm(telegramProxy);
        messagesForm.setBorder(null);
        mainForm.setMessagesPanel(messagesForm);
        return messagesForm;
    }

    private void initMainForm() {
        mainForm = new MainForm();
        contactsList = new ContactsList();
        profileForm.getBtExit().setEnabled(true);
        mainForm.getBtGear().setEnabled(true);
        mainForm.setContactsPanel(contactsLayeredPane);
//        Person me = telegramProxy.getMe();
//        mainForm.setMyContact(me);
//        mainForm.setMyPhoto(GuiHelper.getPhoto(telegramProxy, me, true, true));
        contactsLayeredPane.add(contactsList, new Integer(0));
        contactsLayeredPane.add(plusOverlay, new Integer(1));
        plusOverlay.addActionListenerToPlusButton((ActionEvent e) -> {
            windowManager.setIndex(ADD_CONTACT_FORM);
        });

        mainForm.addActionListenerForGearButton((ActionEvent e) -> {
            profileForm.setTelegramProxy(telegramProxy);
            windowManager.setIndex(PROFILE_FORM);
        });
        mainForm.addActionListenerForEditContactButton((ActionEvent e)-> {
            windowManager.setIndex(EDIT_CONTACT_FORM);
            editContactForm.setPhoto(GuiHelper.getPhoto(telegramProxy, contactsList.getSelectedValue(), true, true));
            editContactForm.setContactInfo(new ContactInfo((Contact) contactsList.getSelectedValue()));

        });
        mainForm.addSendMessageListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Person buddy =  contactsList.getSelectedValue();
                String text = mainForm.getMessageText().trim();

                if(telegramProxy != null && buddy != null && !text.isEmpty()) {
                    try {
                        telegramProxy.sendMessage(buddy, text);
                        mainForm.setMessageText("");
                        checkForUpdates(true);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(mainForm, "Не могу отправить сообщение");
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
            System.out.println("!!!!!!!!!!!!!!!!!!!!!НАЧАЛО ОПЕРАЦИИ ВЫХОДА!!!!!!!!!!!!!!!!!!!!!!!");
            logOut();
        });

        editContactForm.addActionListenerForBackButton((ActionEvent e) -> {
            windowManager.setIndex(MAIN_WINDOW);
        });

        editContactForm.addActionListenerForSaveButton((ActionEvent e) ->{
            tryUpdateContact(editContactForm.getContactInfo());
        });

        editContactForm.addActionListenerForRemove(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                tryDeleteContact(editContactForm.getContactInfo());
            }
        });

        addContactForm.addActionListenerForBackButton((ActionEvent e) -> {
            windowManager.setIndex(MAIN_WINDOW);
        });

        addContactForm.addActionListenerForSaveButton((ActionEvent e)-> {
                tryAddContact(addContactForm.getContactInfo());
        });




    }

    private boolean tryAddContact(ContactInfo info) {

        String phone = info.getClearedPhone() ;
        if(phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Пожалуйста, введите номер телефона");
            return false;
        }
        if(info.getFirstName().isEmpty() && info.getLastName().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Пожалуйста, введите имя и/или фамилию");
            return false;
        }
        for(Person person : telegramProxy.getPersons()) {
            if(person instanceof Contact) {
                if(((Contact) person).getPhoneNumber().replaceAll("\\D+", "").equals(phone)) {
                    JOptionPane.showMessageDialog(this, "Контакт с таким номером уже существует");
                    return false;
                }
            }
        }

        if(!telegramProxy.importContact(info.getPhone(), info.getFirstName(), info.getLastName())) {
            JOptionPane.showMessageDialog(this, "Ошибка на сервере при добавлении контакта");
            return  false;
        }

        windowManager.setIndex(MAIN_WINDOW);
        checkForUpdates(true);
        return true;
    }

    private boolean tryUpdateContact(ContactInfo info) {
        String phone = info.getClearedPhone() ;

        if(info.getFirstName().isEmpty() && info.getLastName().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Пожалуйста, введите имя и/или фамилию");
            return false;
        }

        if(!telegramProxy.importContact(info.getPhone(), info.getFirstName(), info.getLastName())) {
            JOptionPane.showMessageDialog(this, "Ошибка на сервере при изменении контакта");
            return  false;
        }

        windowManager.setIndex(MAIN_WINDOW);
        checkForUpdates(true);
        return true;
    }

    private boolean tryDeleteContact(ContactInfo info) {
        int id = info.getId();

        if(!telegramProxy.deleteContact(id)) {
            JOptionPane.showMessageDialog(this, "Ошибка на сервере при удалении контакта");
            return  false;
        }

        windowManager.setIndex(MAIN_WINDOW);
        checkForUpdates(true);
        return true;
    }

    private void logOut() {
        try {
            destroyTelegramProxy();
            codeForm.clearCodeField();
            phoneForm.clearTelNumber();
            windowManager.setIndex(MAIN_WINDOW);
            setContentPane(phoneForm);
            if (!telegramDAO.logOut()) {
                throw new RuntimeException("Отказ сервера разорвать соединение");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ошибка выхода!");
            abort(e);
        }

    }
    private void abort(Throwable e) {
        if(e != null)
            e.printStackTrace();
        else
            System.err.println("Unknown Error");
        telegramDAO.close();
        System.exit(-1);
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
        //telegramDAO.close();
        System.exit(0);
    }

    private void destroyTelegramProxy() {
        telegramProxy = null;
        updateTelegramProxy();

    }




}
