
import components.BlueButton;
import contacts.ContactsList;
import forms.*;

import messages.MessagesForm;
import org.javagram.dao.*;
import org.javagram.dao.Dialog;
import org.javagram.dao.proxy.TelegramProxy;

import org.javagram.dao.proxy.changes.UpdateChanges;
import org.telegram.api.engine.RpcException;
import overlays.*;

import resources.Images;
import utils.ComponentResizer;
import utils.ComponentResizerExtended;
import utils.FrameDragger;
import utils.Undecorated;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.List;
import java.awt.event.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;


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
            mainForm.setMyPhoto(Helper.getPhoto(telegramProxy, me, true, true));
        }

    }

    private void displayBuddy(Person person) {
        if(person == null) {
            mainForm.setBuddyContact(null);
            mainForm.setBuddyPhoto(null);
        } else {
            mainForm.setBuddyContact(person);
            mainForm.setBuddyPhoto(Helper.getPhoto(telegramProxy, person, true, true));
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
                showErrorMessage(ex.getMessage(), "Ошибка!");
                return;
            }

            if (telNumber != null) {
                try {
                    telegramDAO.acceptNumber(telNumber);
                } catch (RpcException ex) {
                    showErrorMessage(ERR_TEL, "Ошибка!");
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
                        showInformationMessage("Потеряно соединение с сервером","Статус соединения");
                        headForm.setContentPanel(phoneForm.getRootPanel());
                        return;
                    }
                } else {
                    headForm.setContentPanel(registrationForm.getRootPanel());
                }
                telNumber = null;
            } else {
                showErrorMessage(ERR_TEL, "Ошибка!");
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
                showWarningMessage(ERR_CODE,"Внимание!");
            } catch (IllegalArgumentException ex) {
                showWarningMessage(ERR_CODE,"Внимание!");
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
            showWarningMessage("Проблема соединения с сервером","Внимание!");
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
            editContactForm.setPhoto(Helper.getPhoto(telegramProxy, contactsList.getSelectedValue(), true, true));
            editContactForm.setContactInfo(new ContactInfo((Contact) contactsList.getSelectedValue()));

        });

        mainForm.addSearchEventListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                searchFor(mainForm.getSearchText());
            }
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
                        showWarningMessage("Не могу отправить сообщение", "Внимание!");
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
            addContactForm.clearFields();
        });

        addContactForm.addActionListenerForSaveButton((ActionEvent e)-> {
                tryAddContact(addContactForm.getContactInfo());
        });
        mainForm.revalidate();
        mainForm.repaint();
    }

    private boolean tryAddContact(ContactInfo info) {

        String phone = info.getClearedPhone() ;
        if(phone.isEmpty()) {
            showWarningMessage("Пожалуйста, введите номер телефона","Внимание!");
            return false;
        }
        if(info.getFirstName().isEmpty() && info.getLastName().isEmpty()) {
            showWarningMessage("Пожалуйста, введите имя и/или фамилию","Внимание!");
            return false;
        }
        for(Person person : telegramProxy.getPersons()) {
            if(person instanceof Contact) {
                if(((Contact) person).getPhoneNumber().replaceAll("\\D+", "").equals(phone)) {
                    showWarningMessage("Контакт с таким номером уже существует","Внимание!");
                    return false;
                }
            }
        }

        if(!telegramProxy.importContact(info.getPhone(), info.getFirstName(), info.getLastName())) {
            showErrorMessage("Ошибка на сервере при добавлении контакта","Ошибка!");
            return  false;
        }

        windowManager.setIndex(MAIN_WINDOW);
        addContactForm.clearFields();
        checkForUpdates(true);
        return true;
    }

    private boolean tryUpdateContact(ContactInfo info) {
        String phone = info.getClearedPhone() ;

        if(info.getFirstName().isEmpty() && info.getLastName().isEmpty()) {
            showWarningMessage("Пожалуйста, введите имя и/или фамилию","Внимание!");
            return false;
        }

        if(!telegramProxy.importContact(info.getPhone(), info.getFirstName(), info.getLastName())) {
            showErrorMessage("Ошибка на сервере при изменении контакта","Ошибка!");
            return  false;
        }

        windowManager.setIndex(MAIN_WINDOW);
        checkForUpdates(true);
        return true;
    }

    private boolean tryDeleteContact(ContactInfo info) {
        int id = info.getId();

        if(!telegramProxy.deleteContact(id)) {
            showErrorMessage("Ошибка на сервере при удалении контакта","Ошибка!");
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
            headForm.setContentPanel(phoneForm.getRootPanel());
            if (!telegramDAO.logOut()) {
                throw new RuntimeException("Отказ сервера разорвать соединение");
            }
        } catch (Exception e) {
            showErrorMessage( "Ошибка выхода!","Ошибка!");
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
            boolean result = showQuestionMessage(
                    "Вы действительно хотите выйти?",
                    "Выход");
            if(result) {
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
        updateTelegramProxy();

    }

    private JButton[] okButton = BlueButton.createDecoratedButtons(JOptionPane.DEFAULT_OPTION);
    private JButton[] yesNoButtons = BlueButton.createDecoratedButtons(JOptionPane.YES_NO_OPTION);

    private void showErrorMessage(String text, String title) {
        Undecorated.showDialog(this, text, title, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION, Images.getIconError(),
                okButton, okButton[0]);
    }

    private void showWarningMessage(String text, String title) {
        Undecorated.showDialog(this, text, title, JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION, Images.getIconWaringn(),
                okButton, okButton[0]);
    }

    private void showInformationMessage(String text, String title) {
        Undecorated.showDialog(this, text, title, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, Images.getIconInfo(),
                okButton, okButton[0]);
    }

    private boolean showQuestionMessage(String text, String title) {
        return Undecorated.showDialog(this, text, title, JOptionPane.QUESTION_MESSAGE, JOptionPane.DEFAULT_OPTION, Images.getIconQuestion(),
                yesNoButtons, yesNoButtons[0]) == 0;
    }

    private void searchFor(String text) {
        text = text.trim();
        if(text.isEmpty()) {
            return;
        }
        String[] words = text.toLowerCase().split("\\s+");
        java.util.List<Person> persons = telegramProxy.getPersons();
        Person person = contactsList.getSelectedValue();
        person = searchFor(text.toLowerCase(), words, persons, person);
        contactsList.setSelectedValue(person);
        if(person == null)
            showInformationMessage("Ничего не найдено", "Поиск");
    }

    private static Person searchFor(String text, String[] words, java.util.List<? extends Person> persons, Person current) {
        int currentIndex = persons.indexOf(current);

        for(int i = 1; i <= persons.size(); i++) {
            int index = (currentIndex + i) % persons.size();
            Person person = persons.get(index);
            if(contains(person.getFirstName().toLowerCase(), words)
                    || contains(person.getLastName().toLowerCase(), words)) {
                return person;
            }
        }
        return null;
    }

    private static boolean contains(String text, String... words) {
        for(String word : words) {
            if(text.contains(word))
                return true;
        }
        return false;
    }

}
