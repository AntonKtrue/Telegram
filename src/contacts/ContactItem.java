package contacts;

import components.GuiHelper;
import components.PhotoPanel;
import forms.Helper;
import org.javagram.dao.*;
import org.javagram.dao.Dialog;
import org.javagram.dao.proxy.TelegramProxy;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Anton on 05.06.2016.
 */
public class ContactItem extends JPanel implements ListCellRenderer<Person> {
    private JPanel rootPanel;
    private JLabel contactName;
    private JPanel avaPanel;
    private JTextPane lastMessage;
    private JPanel timePanel;
    private JPanel infoPanel;
    private JLabel timeLabel;

    private boolean hasFocus;
    private final int focusMarkerWidth = 4;

    private TelegramProxy telegramProxy;

    public ContactItem(TelegramProxy telegramProxy) {
        this.telegramProxy = telegramProxy;
    }

    public void setBaseColor() {
        rootPanel.setBackground(new Color(230, 230, 230));
    }

    public void setActiveColor() {
        rootPanel.setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.lightGray);
        g.drawRect(0,0,this.getWidth()-1, this.getHeight()-1);
        if(hasFocus) {
            g.setColor(Color.blue);
            g.fillRect(getWidth()-focusMarkerWidth, 1, focusMarkerWidth, getHeight()-2);
        }
    }

    private String messageTime(Date date) {
        String result = "";
        SimpleDateFormat oldEventFormat = new SimpleDateFormat("dd.MM.yy");
        SimpleDateFormat todayEventFormat = new SimpleDateFormat("HH:MM");
        Date nowDate = new Date();
        Long nowSeconds = nowDate.getTime()/1000;
        Long messageSeconds = date.getTime()/1000;
        Long timeDiff = nowSeconds - messageSeconds;

        //Время приводится к часовому поясу читающего?
        if( timeDiff < 0) return "в будущем";
        if( timeDiff < 60 ) return "только что";

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        int nowDay = calendar.get(Calendar.DAY_OF_MONTH);
        int nowMonth = calendar.get(Calendar.MONTH);
        int nowYear = calendar.get(Calendar.YEAR);
        calendar.setTime(date);
        int messageDay = calendar.get(Calendar.DAY_OF_MONTH);
        int messageMonth = calendar.get(Calendar.MONTH);
        int messageYear = calendar.get(Calendar.YEAR);

        if(messageDay == nowDay &&
                messageMonth == nowMonth &&
                messageYear == nowYear) {
            return todayEventFormat.format(date);
        } else {
            return oldEventFormat.format(date);
        }

    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Person> list, Person person, int index, boolean selected, boolean cellHasFocus) {
        Dialog dialog = telegramProxy.getDialog(person);
        this.contactName.setText(person.getFirstName() + " " + person.getLastName());

        if(dialog != null){
            this.lastMessage.setText(dialog.getLastMessage().getText());
            this.timeLabel.setText(messageTime(dialog.getLastMessage().getDate()));
        } else {
            this.lastMessage.setText("");
        }

        if (selected) {
            setActiveColor();
        } else {
            setBaseColor();
        }
        this.hasFocus = cellHasFocus;
        ((PhotoPanel)avaPanel).setImage(Helper.getPhoto(telegramProxy, person, true, true));
        ((PhotoPanel)avaPanel).setOnline(telegramProxy.isOnline(person));


        return rootPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = this;
        avaPanel = new PhotoPanel(null, true, false, 0, false);
    }

}
