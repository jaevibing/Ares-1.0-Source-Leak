package com.ares.utils;

import com.ares.utils.chat.ChatUtils;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.TrayIcon;
import java.awt.SystemTray;

public class TrayUtils
{
    private static SystemTray tray;
    private static TrayIcon trayIcon;
    
    public TrayUtils() {
        try {
            TrayUtils.tray = SystemTray.getSystemTray();
            (TrayUtils.trayIcon = new TrayIcon(new BufferedImage(20, 20, 1), "Tray Demo")).setImageAutoSize(true);
            TrayUtils.trayIcon.setToolTip("Ares");
            TrayUtils.tray.add(TrayUtils.trayIcon);
        }
        catch (Exception v1) {
            v1.printStackTrace();
            ChatUtils.printMessage("Could not send notification due to error: " + v1.toString());
        }
    }
    
    public static void sendMessage(final String a1) {
        sendMessage(/*EL:51*/"Ares", a1);
    }
    
    public static void sendMessage(final String a1, final String a2) {
        /*SL:56*/if (TrayUtils.trayIcon == null) {
            /*SL:58*/new TrayUtils();
        }
        TrayUtils.trayIcon.displayMessage(/*EL:60*/a1, a2, TrayIcon.MessageType.INFO);
    }
}
