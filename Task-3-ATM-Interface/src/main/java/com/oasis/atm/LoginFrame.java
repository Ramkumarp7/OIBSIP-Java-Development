package com.oasis.atm;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final Bank bank;
    private final JTextField userField = Ui.field(22);
    private final JPasswordField pinField = Ui.password(22);
    private int attempts = 0;

    public LoginFrame(Bank bank) {
        this.bank = bank;
        setTitle("OASIS INFOBYTE — ATM Interface");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000,680); setMinimumSize(new Dimension(850,600)); setLocationRelativeTo(null);

        JPanel page = new JPanel(new GridLayout(1,2));
        page.setBackground(Ui.CANVAS);
        JPanel brandSide = new JPanel();
        brandSide.setBackground(Ui.TEAL);
        brandSide.setBorder(BorderFactory.createEmptyBorder(50,48,50,42));
        brandSide.setLayout(new BoxLayout(brandSide,BoxLayout.Y_AXIS));
        JLabel brand = new JLabel("OASIS INFOBYTE");
        brand.setFont(Ui.font(15,Font.BOLD)); brand.setForeground(new Color(218,245,239));
        brandSide.add(brand); brandSide.add(Box.createVerticalGlue());
        JLabel headline = new JLabel("<html><div style='width:330px'>Banking, made<br>simple.</div></html>");
        headline.setFont(Ui.font(38,Font.BOLD)); headline.setForeground(Color.WHITE);
        brandSide.add(headline); brandSide.add(Box.createVerticalStrut(16));
        JLabel copy = new JLabel("<html><div style='width:320px'>A clean and secure ATM experience for your everyday banking tasks.</div></html>");
        copy.setFont(Ui.font(15,Font.PLAIN)); copy.setForeground(new Color(220,239,235));
        brandSide.add(copy); brandSide.add(Box.createVerticalGlue());
        brandSide.add(new JLabel("JAVA DEVELOPMENT  •  TASK 03"));

        JPanel right = new JPanel(new GridBagLayout()); right.setBackground(Ui.CANVAS);
        JPanel card = Ui.card(); card.setLayout(new GridBagLayout()); card.setPreferredSize(new Dimension(390,440));
        GridBagConstraints c = new GridBagConstraints(); c.gridx=0;c.weightx=1;c.fill=GridBagConstraints.HORIZONTAL;
        c.insets=new Insets(0,0,10,0);c.gridy=0;
        JLabel eyebrow = new JLabel("WELCOME BACK"); eyebrow.setFont(Ui.font(11,Font.BOLD));eyebrow.setForeground(Ui.TEAL);card.add(eyebrow,c);
        c.gridy++;c.insets=new Insets(0,0,8,0);card.add(Ui.title("Sign in to your account",24),c);
        c.gridy++;c.insets=new Insets(0,0,26,0);card.add(Ui.muted("Enter your demo banking credentials to continue."),c);
        c.gridy++;c.insets=new Insets(0,0,7,0);card.add(label("User ID"),c);
        c.gridy++;c.insets=new Insets(0,0,18,0);card.add(userField,c);
        c.gridy++;c.insets=new Insets(0,0,7,0);card.add(label("4-digit PIN"),c);
        c.gridy++;c.insets=new Insets(0,0,20,0);card.add(pinField,c);
        c.gridy++;c.insets=new Insets(0,0,12,0);
        JButton signIn=Ui.button("Continue",true);signIn.setPreferredSize(new Dimension(330,44));
        signIn.addActionListener(e->login());card.add(signIn,c);
        c.gridy++;c.insets=new Insets(5,0,0,0);card.add(Ui.muted("Demo: ram / 1234   •   sita / 4321   •   arjun / 2468"),c);
        right.add(card);
        page.add(brandSide);page.add(right);
        setContentPane(page);getRootPane().setDefaultButton(signIn);
    }
    private JLabel label(String text){JLabel l=new JLabel(text);l.setFont(Ui.font(12,Font.BOLD));l.setForeground(Ui.INK);return l;}
    private void login(){
        Account account=bank.authenticate(userField.getText().trim(),new String(pinField.getPassword()));
        if(account!=null){dispose();new DashboardFrame(bank,account).setVisible(true);return;}
        attempts++;pinField.setText("");
        if(attempts>=3){JOptionPane.showMessageDialog(this,"Three unsuccessful attempts. Access is locked.","Sign-in locked",JOptionPane.ERROR_MESSAGE);userField.setEnabled(false);pinField.setEnabled(false);}
        else JOptionPane.showMessageDialog(this,"Incorrect User ID or PIN. Attempts remaining: "+(3-attempts),"Sign-in failed",JOptionPane.WARNING_MESSAGE);
    }
}
