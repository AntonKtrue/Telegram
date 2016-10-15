import org.javagram.dao.ApiBridgeTelegramDAO;
import org.javagram.dao.DebugTelegramDAO;
import org.javagram.dao.TelegramDAO;
import resources.Config;

import javax.swing.*;

/**
 * Created by Anton on 13.05.2016.
 */
public class Loader {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            JFrame window = null;
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                //new ApiBridgeTelegramDAO(Config.SERVER, Config.APP_ID, Config.APP_HASH);
                TelegramDAO telegramDAO = new DebugTelegramDAO();
                window = new Window(telegramDAO);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
            window.setLocationRelativeTo(null);
            window.setVisible(true);
        });
    }
}
