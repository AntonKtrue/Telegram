package utils;

import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;

/**
 * Created by Anton on 31.10.2016.
 */
public class PhoneFormat  {
    private static final String RUSSIAN_FORMAT = "+7 ### ### ## ##";
    private static final char PLACEHOLDER = ' ';
    private static DefaultFormatterFactory russianPhoneFormat;

    public static DefaultFormatterFactory getRussianPhoneFormat() throws ParseException{
            if(russianPhoneFormat == null) {
                MaskFormatter formatter = new MaskFormatter(RUSSIAN_FORMAT);
                formatter.setPlaceholderCharacter(PLACEHOLDER);
                russianPhoneFormat = new DefaultFormatterFactory(formatter);
            }
            return russianPhoneFormat;
    }

}
