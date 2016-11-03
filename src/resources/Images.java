package resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by HerrSergio on 25.05.2016.
 */
public class Images {
    private Images() {
    }
    private static BufferedImage background;
    private static BufferedImage logo;
    private static BufferedImage logoMini;
    private static BufferedImage logoMicro;
    private static BufferedImage iconPhone;
    private static BufferedImage buttonBackground;
    private static BufferedImage iconLock;
    private static BufferedImage smallUserImage;
    private static BufferedImage largeUserImage;
    private static BufferedImage gearIcon;
    private static BufferedImage backIcon;
    private static BufferedImage buttonSmallBackground;
    private static BufferedImage sendMessageImage;
    private static BufferedImage plusImage;
    private static BufferedImage iconEdit;
    private static Icon iconWaringn;
    private static Icon iconInfo;
    private static Icon iconQuestion;
    private static Icon iconError;
    private static BufferedImage iconSearch;

    public static BufferedImage getIconSearch() {
        if (iconSearch == null)
            iconSearch = loadImage("icon-search.png");
        return iconSearch;
    }

    public static Icon getIconWaringn() {
        if (iconWaringn == null)
            iconWaringn = scaleImageToIcon(loadImage("icon-warning.png"));
        return iconWaringn;
    }

    public static Icon getIconInfo() {
        if (iconInfo == null)
            iconInfo = scaleImageToIcon(loadImage("icon-info.png"));
        return iconInfo;
    }

    public static Icon getIconQuestion() {
        if (iconQuestion == null)
            iconQuestion = scaleImageToIcon(loadImage("icon-question.png"));
        return iconQuestion;
    }

    public static Icon getIconError() {
        if (iconError == null)
            iconError = scaleImageToIcon(loadImage("icon-error.png"));
        return iconError;
    }

    public static BufferedImage getIconEdit() {
        if (iconEdit == null)
            iconEdit = loadImage("icon-edit.png");
        return iconEdit;
    }

    public static BufferedImage getBackground() {
        if (background == null)
            background = loadImage("background.png");
        return background;
    }

    public static BufferedImage getLogo() {
        if (logo == null)
            logo = loadImage("logo.png");
        return logo;
    }

    public static BufferedImage getLogoMini() {
        if (logoMini == null)
            logoMini = loadImage("logo-mini.png");
        return logoMini;
    }

    public static BufferedImage getLogoMicro() {
        if (logoMicro == null)
            logoMicro = loadImage("logo-micro.png");
        return logoMicro;
    }

    public static BufferedImage getIconPhone() {
        if (iconPhone == null)
            iconPhone = loadImage("icon-phone.png");
        return iconPhone;
    }

    public static BufferedImage getButtonBackground() {
        if (buttonBackground == null)
            buttonBackground = loadImage("button-background.png");
        return buttonBackground;
    }

    public static BufferedImage getButtonSmallBackground() {
        if (buttonSmallBackground == null)
            buttonSmallBackground = loadImage("button-small-background.png");
        return buttonSmallBackground;
    }

    public static BufferedImage getIconLock() {
        if (iconLock == null)
            iconLock = loadImage("icon-lock.png");
        return iconLock;
    }


    public synchronized static BufferedImage getSendMessageImage() {
        if (sendMessageImage == null)
            sendMessageImage = loadImage("button-send.png");
        return sendMessageImage;
    }

    public static BufferedImage getGearIcon() {
        if (gearIcon == null)
            gearIcon = loadImage("icon-settings.png");
        return gearIcon;
    }

    public static BufferedImage getBackIcon() {
        if (backIcon == null)
            backIcon = loadImage("icon-back.png");
        return backIcon;
    }

    //
    public static BufferedImage getPlus() {
        if (plusImage == null)
            plusImage = loadImage("icon-plus.png");
        return plusImage;
    }

    public synchronized static BufferedImage getSmallUserImage() {
        if (smallUserImage == null)
            smallUserImage = loadImage("logo-mini.png"); //TODO
        return smallUserImage;
    }


    public synchronized static BufferedImage getLargeUserImage() {
        if (largeUserImage == null)
            largeUserImage = loadImage("logo.png"); //TODO
        return largeUserImage;
    }

    public static BufferedImage getUserImage(boolean small) {
        return small ? getSmallUserImage() : getLargeUserImage();
    }

    private static BufferedImage loadImage(String name) {
        try {
            return ImageIO.read(Images.class.getResource("/images/" + name));
        } catch (Exception e) {
            e.printStackTrace();
            return new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
        }
    }

    private static Icon scaleImageToIcon(BufferedImage image) {
        return new ImageIcon(scaleImage(image, 30, 30));
    }

    private static BufferedImage scaleImage(BufferedImage image, int width, int height) {
        BufferedImage result = new BufferedImage(width, height, image.getType());
        Graphics2D g2d = result.createGraphics();
        try {
            g2d.drawImage(image, 0, 0, width, height, null);
        } finally {
            g2d.dispose();
        }
        return result;
    }
}
