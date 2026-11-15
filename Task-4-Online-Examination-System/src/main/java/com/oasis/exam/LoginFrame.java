package com.oasis.exam;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final AppUser user = UserStore.loadDemoUser();
    private final JTextField username = Ui.field(22);
    private final JPasswordField password = Ui.password(22);

    public LoginFrame() {
        setTitle(Main.APP_TITLE);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(940, 620);
        setMinimumSize(new Dimension(760, 540));
        setLocationRelativeTo(null);

        JPanel page = Ui.page();
        JPanel card = Ui.card();
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(470, 470));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 10, 0);
        JLabel brand = new JLabel("OASIS INFOBYTE");
        brand.setFont(Ui.font(13, Font.BOLD)); brand.setForeground(Main.BRAND);
        card.add(brand, c);
        c.gridy++;
        JLabel title = Ui.heading("Online Examination System", 25);
        card.add(title, c);
        c.gridy++;
        c.insets = new Insets(8, 0, 24, 0);
        card.add(Ui.muted("Sign in to continue to your examination workspace."), c);

        c.insets = new Insets(0, 0, 0, 0);
        Ui.addField(card, "Username", username, c, 3);
        Ui.addField(card, "Password", password, c, 5);
        c.gridy = 7; c.insets = new Insets(22, 0, 0, 0);
        JButton login = Ui.button("Sign in", true);
        login.addActionListener(e -> signIn());
        card.add(login, c);
        c.gridy++;
        c.insets = new Insets(14, 0, 0, 0);
        JLabel hint = Ui.muted("Demo login: student  |  Password: oasis123");
        card.add(hint, c);

        page.add(card);
        setContentPane(page);
        getRootPane().setDefaultButton(login);
    }

    private void signIn() {
        String u = username.getText().trim();
        String p = new String(password.getPassword());
        if (u.equals(user.getUsername()) && p.equals(user.getPassword())) {
            dispose();
            new DashboardFrame(user).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "The username or password is incorrect.",
                    "Sign-in failed", JOptionPane.ERROR_MESSAGE);
            password.setText("");
        }
    }
}
