package com.oasis.exam;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public final class Ui {
    private Ui() {}
    public static Font font(int size, int style) { return new Font("SansSerif", style, size); }
    public static JPanel page() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Main.CANVAS);
        return p;
    }
    public static JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setBorder(new CompoundBorder(new LineBorder(Main.BORDER, 1, true),
                new EmptyBorder(28, 32, 28, 32)));
        return p;
    }
    public static JLabel heading(String s, int size) {
        JLabel l = new JLabel(s);
        l.setFont(font(size, Font.BOLD));
        l.setForeground(Main.TEXT);
        return l;
    }
    public static JLabel muted(String s) {
        JLabel l = new JLabel(s);
        l.setFont(font(13, Font.PLAIN));
        l.setForeground(Main.MUTED);
        return l;
    }
    public static JButton button(String text, boolean primary) {
        JButton b = new JButton(text);
        b.setFont(font(14, Font.BOLD));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(12, 22, 12, 22));
        b.setOpaque(true);
        b.setBackground(primary ? Main.BRAND : Color.WHITE);
        b.setForeground(primary ? Color.WHITE : Main.SECONDARY);
        if (!primary) b.setBorder(new CompoundBorder(new LineBorder(Main.BORDER, 1, true),
                new EmptyBorder(11, 21, 11, 21)));
        return b;
    }
    public static JTextField field(int columns) {
        JTextField f = new JTextField(columns);
        f.setFont(font(14, Font.PLAIN));
        f.setForeground(Main.TEXT);
        f.setBorder(new CompoundBorder(new LineBorder(Main.BORDER, 1, true),
                new EmptyBorder(10, 12, 10, 12)));
        return f;
    }
    public static JPasswordField password(int columns) {
        JPasswordField f = new JPasswordField(columns);
        f.setFont(font(14, Font.PLAIN));
        f.setBorder(new CompoundBorder(new LineBorder(Main.BORDER, 1, true),
                new EmptyBorder(10, 12, 10, 12)));
        return f;
    }
    public static void addField(JPanel p, String label, JComponent field, GridBagConstraints c, int row) {
        c.gridx = 0; c.gridy = row; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(row == 0 ? 0 : 16, 0, 6, 0);
        JLabel l = new JLabel(label); l.setFont(font(13, Font.BOLD)); l.setForeground(Main.SECONDARY);
        p.add(l, c);
        c.gridy = row + 1; c.insets = new Insets(0, 0, 0, 0);
        p.add(field, c);
    }
}
