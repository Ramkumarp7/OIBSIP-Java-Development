package com.oasis.atm;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public final class Ui {
    public static final Color TEAL = new Color(8,129,119);
    public static final Color TEAL_DARK = new Color(5,103,96);
    public static final Color INK = new Color(27,27,27);
    public static final Color MUTED = new Color(112,116,120);
    public static final Color CANVAS = new Color(247,247,243);
    public static final Color CARD = Color.WHITE;
    public static final Color BORDER = new Color(229,231,226);
    public static final Color PALE_TEAL = new Color(232,245,242);

    private Ui() {}
    public static Font font(int size, int style) { return new Font("SansSerif", style, size); }

    public static JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(CARD);
        p.setBorder(new CompoundBorder(new LineBorder(BORDER,1,true), new EmptyBorder(20,22,20,22)));
        return p;
    }
    public static JLabel title(String text, int size) {
        JLabel l = new JLabel(text);
        l.setFont(font(size,Font.BOLD)); l.setForeground(INK);
        return l;
    }
    public static JLabel muted(String text) {
        JLabel l = new JLabel(text);
        l.setFont(font(12,Font.PLAIN)); l.setForeground(MUTED);
        return l;
    }
    public static JButton button(String text, boolean primary) {
        JButton b = new JButton(text);
        b.setFont(font(13,Font.BOLD));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(150,42));
        b.setBorder(new EmptyBorder(10,16,10,16));
        b.setOpaque(true);
        b.setBackground(primary ? TEAL : Color.WHITE);
        b.setForeground(primary ? Color.WHITE : INK);
        if (!primary) b.setBorder(new CompoundBorder(new LineBorder(BORDER,1,true),new EmptyBorder(9,15,9,15)));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { if(primary) b.setBackground(TEAL_DARK); }
            public void mouseExited(java.awt.event.MouseEvent e) { if(primary) b.setBackground(TEAL); }
        });
        return b;
    }
    public static JTextField field(int columns) {
        JTextField f = new JTextField(columns);
        f.setFont(font(14,Font.PLAIN));
        f.setForeground(INK);
        f.setBorder(new CompoundBorder(new LineBorder(BORDER,1,true),new EmptyBorder(10,12,10,12)));
        return f;
    }
    public static JPasswordField password(int columns) {
        JPasswordField f = new JPasswordField(columns);
        f.setFont(font(14,Font.PLAIN));
        f.setBorder(new CompoundBorder(new LineBorder(BORDER,1,true),new EmptyBorder(10,12,10,12)));
        return f;
    }
    public static JPanel page() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(CANVAS);
        return p;
    }
    public static String money(java.math.BigDecimal amount) {
        return "₹" + amount.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString();
    }
}
