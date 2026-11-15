package com.oasis.exam;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;

public class Main {
    public static final String APP_TITLE = "OASIS INFOBYTE - Online Examination System";
    public static final Color BRAND = new Color(37, 99, 235);
    public static final Color BRAND_HOVER = new Color(29, 78, 216);
    public static final Color TEXT = new Color(30, 41, 59);
    public static final Color SECONDARY = new Color(51, 65, 85);
    public static final Color MUTED = new Color(100, 116, 139);
    public static final Color CANVAS = new Color(246, 248, 252);
    public static final Color LILAC = new Color(239, 244, 255);
    public static final Color BORDER = new Color(203, 213, 225);
    public static boolean darkMode = false;

    public static void setTheme(boolean dark) {
        darkMode = dark;
        try { if (dark) FlatDarkLaf.setup(); else FlatLightLaf.setup(); }
        catch (Exception ignored) {}
        UIManager.put("Component.arc", 12);
        UIManager.put("Button.arc", 20);
        UIManager.put("TextComponent.arc", 8);
        UIManager.put("defaultFont", new FontUIResource("Inter", Font.PLAIN, 13));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            setTheme(false);
            new LoginFrame().setVisible(true);
        });
    }
}
