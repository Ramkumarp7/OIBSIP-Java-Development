package com.oasis.atm;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import java.awt.Font;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}
            UIManager.put("defaultFont", new FontUIResource("SansSerif", Font.PLAIN, 14));
            new LoginFrame(Bank.createDemoBank()).setVisible(true);
        });
    }
}
