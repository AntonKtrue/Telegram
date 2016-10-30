package resources;

import javax.imageio.ImageIO;
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


//    private static BufferedImage background;
//    private static BufferedImage logo;
//    private static BufferedImage sendMessageImage;

//    private static BufferedImage gearIcon;
      private static BufferedImage plusImage;
//    private static BufferedImage magnifyingGlassIcon;
//    private static BufferedImage pencilIcon;
//    private static BufferedImage penIcon;
//    private static BufferedImage penLogo;
//    private static BufferedImage addContact;
//    private static BufferedImage updateContact;
//    private static BufferedImage removeContact;
//    private static BufferedImage closeOverlay;
//    private static BufferedImage logoutIcon;
//    private static BufferedImage blueButton;

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

    public static BufferedImage getIconLock() {
        if (iconLock == null)
            iconLock = loadImage("icon-lock.png");
        return iconLock;
    }


//    public synchronized static BufferedImage getBackground() {
//        if (background == null)
//            background = loadImage("background.png");
//        return background;
//    }
//
//    public synchronized static BufferedImage getLogo() {
//        if (logo == null)
//            logo = loadImage("logo.png");
//        return logo;
//    }
//
//    public synchronized static BufferedImage getSendMessageImage() {
//        if (sendMessageImage == null)
//            sendMessageImage = loadImage("button-send.png");
//        return sendMessageImage;
//    }
//
    public static BufferedImage getGearIcon() {
        if(gearIcon == null)
            gearIcon = loadImage("icon-settings.png");
        return gearIcon;
    }

    public static BufferedImage getBackIcon() {
        if(backIcon == null)
            backIcon = loadImage("icon-back.png");
        return backIcon;
    }

    //
    public static BufferedImage getPlus() {
        if(plusImage == null)
            plusImage = loadImage("icon-plus.png");
        return plusImage;
    }
//
//    public static BufferedImage getMagnifyingGlassIcon() {
//        if(magnifyingGlassIcon == null)
//            magnifyingGlassIcon = loadImage("icon-search.png");
//        return magnifyingGlassIcon;
//    }
//
//    public static BufferedImage getPencilIcon() {
//        if(pencilIcon == null)
//            pencilIcon = loadImage("blue-pencil.jpg");
//        return pencilIcon;
//    }
//
//    public static BufferedImage getPenIcon() {
//        if(penIcon == null)
//            penIcon = loadImage("writing-146913_960_720.png");
//        return penIcon;
//    }
//
//    public static BufferedImage getPenLogo() {
//        if(penLogo == null)
//            penLogo = loadImage("handposition3png.png");
//        return penLogo;
//    }
//
//    public static BufferedImage getAddContact() {
//        if(addContact == null)
//            addContact = loadImage("Add-Male-User.png");
//        return addContact;
//    }
//
//    public static BufferedImage getUpdateContact() {
//        if(updateContact == null)
//            updateContact = loadImage("43781db5c40ecc39fd718685594f0956.png");
//        return updateContact;
//    }
//
//    public static BufferedImage getRemoveContact() {
//        if(removeContact == null)
//            removeContact = loadImage("Remove-Male-User.png");
//        return removeContact;
//    }
//
//    public static BufferedImage getCloseOverlay() {
//        if(closeOverlay == null)
//            closeOverlay = loadImage("Close.png");
//        return closeOverlay;
//    }
//
//    public static BufferedImage getLogoutIcon() {
//        if(logoutIcon == null)
//            logoutIcon = loadImage("logout-icon.png");
//        return logoutIcon;
//    }
//
//    public static BufferedImage getBlueButton() {
//        if(blueButton == null)
//            blueButton = loadImage("expences-button-png-hi.png");
//        return blueButton;
//    }
//
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
}
