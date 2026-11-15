package com.oasis.atm;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class DashboardFrame extends JFrame {
    private static final BigDecimal DAILY_LIMIT = new BigDecimal("20000.00");
    private final Bank bank;
    private final Account account;
    private final ATM atm;
    private final JPanel content = new JPanel(new CardLayout());
    private JLabel balanceValue;
    private JLabel limitText;
    private JProgressBar limitBar;
    private boolean balanceVisible = true;
    private String activeView = "dashboard";
    private DefaultTableModel historyModel;

    public DashboardFrame(Bank bank, Account account) {
        this.bank=bank; this.account=account; this.atm=new ATM(bank);
        setTitle("OASIS INFOBYTE — ATM Interface");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1220,830); setMinimumSize(new Dimension(1040,720)); setLocationRelativeTo(null);
        JPanel root=new JPanel(new BorderLayout()); root.setBackground(Ui.CANVAS);
        root.add(sidebar(),BorderLayout.WEST); root.add(mainPanel(),BorderLayout.CENTER);
        setContentPane(root); showView("dashboard");
    }

    private JPanel sidebar() {
        JPanel side=new JPanel(new BorderLayout()); side.setPreferredSize(new Dimension(230,0));
        side.setBackground(Color.WHITE); side.setBorder(new MatteBorder(0,0,0,1,Ui.BORDER));
        JPanel top=new JPanel(); top.setOpaque(false); top.setBorder(new EmptyBorder(28,22,22,18));
        top.setLayout(new BoxLayout(top,BoxLayout.Y_AXIS));
        JLabel logo=new JLabel("OASIS INFOBYTE"); logo.setFont(Ui.font(14,Font.BOLD)); logo.setForeground(Ui.TEAL);
        top.add(logo); top.add(Box.createVerticalStrut(7)); top.add(Ui.title("ATM Interface",17));
        top.add(Box.createVerticalStrut(3)); top.add(Ui.muted("PERSONAL BANKING")); side.add(top,BorderLayout.NORTH);
        JPanel nav=new JPanel(); nav.setOpaque(false); nav.setBorder(new EmptyBorder(12,12,12,12));
        nav.setLayout(new BoxLayout(nav,BoxLayout.Y_AXIS));
        navButton(nav,"▦   Overview","dashboard"); navButton(nav,"↗   Transfer money","transfer");
        navButton(nav,"↧   Withdraw cash","withdraw"); navButton(nav,"↥   Deposit funds","deposit");
        navButton(nav,"▤   Transactions","history"); side.add(nav,BorderLayout.CENTER);
        JPanel bottom=new JPanel(); bottom.setOpaque(false); bottom.setBorder(new EmptyBorder(14,16,20,16));
        bottom.setLayout(new BoxLayout(bottom,BoxLayout.Y_AXIS));
        bottom.add(Ui.muted("SIGNED IN AS")); bottom.add(Box.createVerticalStrut(5));
        JLabel n=new JLabel(account.getHolderName()); n.setFont(Ui.font(13,Font.BOLD)); n.setForeground(Ui.INK);
        bottom.add(n); bottom.add(Box.createVerticalStrut(14));
        JButton logout=Ui.button("↪  Sign out",false); logout.setAlignmentX(Component.LEFT_ALIGNMENT);
        logout.addActionListener(e->{dispose();new LoginFrame(bank).setVisible(true);});
        bottom.add(logout); side.add(bottom,BorderLayout.SOUTH); return side;
    }

    private void navButton(JPanel nav,String label,String key) {
        JButton b=new JButton(label); b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setFont(Ui.font(13,Font.BOLD)); b.setForeground(key.equals(activeView)?Ui.TEAL:Ui.INK);
        b.setBackground(key.equals(activeView)?new Color(230,244,241):Color.WHITE);
        b.setOpaque(true); b.setFocusPainted(false); b.setBorder(new EmptyBorder(13,12,13,8));
        b.setMaximumSize(new Dimension(205,45)); b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.addActionListener(e->showView(key)); nav.add(b); nav.add(Box.createVerticalStrut(5));
        b.putClientProperty("navKey",key);
    }

    private JPanel mainPanel() {
        JPanel area=new JPanel(new BorderLayout(20,20)); area.setBackground(Ui.CANVAS);
        area.setBorder(new EmptyBorder(26,30,28,30));
        JPanel header=new JPanel(new BorderLayout()); header.setOpaque(false);
        JPanel titles=new JPanel(); titles.setOpaque(false); titles.setLayout(new BoxLayout(titles,BoxLayout.Y_AXIS));
        titles.add(Ui.title("Your money, at a glance",24)); titles.add(Box.createVerticalStrut(5));
        titles.add(Ui.muted(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy"))));
        header.add(titles,BorderLayout.WEST);
        JLabel status=new JLabel("●  ACCOUNT ACTIVE"); status.setFont(Ui.font(11,Font.BOLD));
        status.setForeground(Ui.TEAL); status.setOpaque(true); status.setBackground(Ui.PALE_TEAL);
        status.setBorder(new EmptyBorder(9,13,9,13)); header.add(status,BorderLayout.EAST);
        area.add(header,BorderLayout.NORTH);
        content.add(dashboardView(),"dashboard"); content.add(operationView("transfer"),"transfer");
        content.add(operationView("withdraw"),"withdraw"); content.add(operationView("deposit"),"deposit");
        content.add(historyView(),"history"); area.add(content,BorderLayout.CENTER); return area;
    }

    private JPanel dashboardView() {
        JPanel page=new JPanel(); page.setOpaque(false); page.setLayout(new BoxLayout(page,BoxLayout.Y_AXIS));
        JPanel intro=new JPanel(new BorderLayout()); intro.setOpaque(false);
        intro.add(Ui.title("Good day, "+account.getHolderName(),22),BorderLayout.WEST);
        intro.add(Ui.muted("Here is your account overview."),BorderLayout.SOUTH);
        page.add(intro); page.add(Box.createVerticalStrut(18));
        JPanel hero=new JPanel(new BorderLayout(20,10)); hero.setBackground(Ui.TEAL);
        hero.setBorder(new EmptyBorder(22,26,20,26));
        JPanel left=new JPanel(); left.setOpaque(false); left.setLayout(new BoxLayout(left,BoxLayout.Y_AXIS));
        JLabel label=new JLabel("TOTAL AVAILABLE BALANCE"); label.setFont(Ui.font(11,Font.BOLD)); label.setForeground(new Color(204,235,229));
        JPanel amountRow=new JPanel(new FlowLayout(FlowLayout.LEFT,10,0)); amountRow.setOpaque(false);
        balanceValue=new JLabel(); balanceValue.setFont(Ui.font(34,Font.BOLD)); balanceValue.setForeground(Color.WHITE);
        JButton eye=new JButton("◉"); eye.setToolTipText("Show / hide balance"); eye.setFont(Ui.font(16,Font.BOLD));
        eye.setForeground(Color.WHITE); eye.setContentAreaFilled(false); eye.setBorder(new EmptyBorder(4,8,4,8));
        eye.addActionListener(e->{balanceVisible=!balanceVisible;updateBalance();});
        amountRow.add(balanceValue); amountRow.add(eye);
        JLabel accountNo=new JLabel("Account  •  "+account.getAccountId()); accountNo.setFont(Ui.font(12,Font.PLAIN)); accountNo.setForeground(new Color(218,240,235));
        left.add(label); left.add(Box.createVerticalStrut(8)); left.add(amountRow); left.add(Box.createVerticalStrut(6)); left.add(accountNo);
        hero.add(left,BorderLayout.CENTER);
        JLabel mark=new JLabel("OASIS",SwingConstants.CENTER); mark.setFont(Ui.font(20,Font.BOLD)); mark.setForeground(new Color(208,237,231));
        mark.setBorder(new LineBorder(new Color(82,169,159),1,true)); mark.setPreferredSize(new Dimension(100,64)); hero.add(mark,BorderLayout.EAST);
        JPanel bannerStack=new JPanel(); bannerStack.setOpaque(false); bannerStack.setLayout(new BoxLayout(bannerStack,BoxLayout.Y_AXIS));
        bannerStack.add(hero); bannerStack.add(dailyLimitPanel());
        page.add(bannerStack); page.add(Box.createVerticalStrut(17));
        JPanel actions=new JPanel(new GridLayout(1,3,12,0)); actions.setOpaque(false);
        actions.add(actionCard("↓","Withdraw","Move money out of your account",()->showView("withdraw")));
        actions.add(actionCard("↑","Deposit","Add money to your account",()->showView("deposit")));
        actions.add(actionCard("↗","Transfer","Send money to another account",()->showView("transfer")));
        page.add(actions); page.add(Box.createVerticalStrut(17));
        JPanel recent=Ui.card(); recent.setLayout(new BorderLayout(10,12));
        JPanel recentHead=new JPanel(new BorderLayout()); recentHead.setOpaque(false);
        recentHead.add(Ui.title("Recent activity",17),BorderLayout.WEST);
        JButton all=Ui.button("View all",false); all.setPreferredSize(new Dimension(100,36));
        all.addActionListener(e->showView("history")); recentHead.add(all,BorderLayout.EAST);
        recent.add(recentHead,BorderLayout.NORTH); recent.add(recentTable(5),BorderLayout.CENTER);
        page.add(recent); updateBalance(); updateDailyLimit(); return page;
    }

    private JPanel dailyLimitPanel() {
        JPanel p=new JPanel(new BorderLayout(12,5)); p.setBackground(new Color(5,112,104));
        p.setBorder(new EmptyBorder(10,26,12,26));
        limitText=new JLabel(); limitText.setFont(Ui.font(11,Font.BOLD)); limitText.setForeground(Color.WHITE);
        limitBar=new JProgressBar(0,100); limitBar.setPreferredSize(new Dimension(160,8));
        limitBar.setBorderPainted(false); limitBar.setForeground(new Color(190,235,224)); limitBar.setBackground(new Color(68,153,143));
        p.add(limitText,BorderLayout.WEST); p.add(limitBar,BorderLayout.EAST); return p;
    }

    private JPanel actionCard(String icon,String title,String desc,Runnable action) {
        JPanel p=Ui.card(); p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        JLabel badge=new JLabel(icon,SwingConstants.CENTER); badge.setFont(Ui.font(19,Font.BOLD));
        badge.setForeground(new Color(13,138,114)); badge.setOpaque(true); badge.setBackground(new Color(230,244,241));
        badge.setPreferredSize(new Dimension(42,42)); badge.setMinimumSize(new Dimension(42,42)); badge.setMaximumSize(new Dimension(42,42));
        badge.setBorder(new EmptyBorder(8,8,8,8)); badge.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(badge); p.add(Box.createVerticalStrut(10)); JLabel t=Ui.title(title,16);t.setAlignmentX(Component.LEFT_ALIGNMENT);p.add(t);
        p.add(Box.createVerticalStrut(5)); JLabel d=Ui.muted(desc);d.setAlignmentX(Component.LEFT_ALIGNMENT);p.add(d);
        p.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        p.addMouseListener(new MouseAdapter(){
            public void mouseEntered(MouseEvent e){p.setBorder(new CompoundBorder(new LineBorder(new Color(13,138,114),1,true),new EmptyBorder(20,22,20,22)));}
            public void mouseExited(MouseEvent e){p.setBorder(new CompoundBorder(new LineBorder(Ui.BORDER,1,true),new EmptyBorder(20,22,20,22)));}
            public void mouseClicked(MouseEvent e){action.run();}
        });
        return p;
    }

    private JPanel operationView(String type) {
        JPanel wrapper=new JPanel(new GridBagLayout()); wrapper.setOpaque(false);
        JPanel card=Ui.card(); card.setLayout(new BoxLayout(card,BoxLayout.Y_AXIS)); card.setPreferredSize(new Dimension(590,500));
        String title=type.equals("withdraw")?"Withdraw cash":type.equals("deposit")?"Deposit funds":"Transfer money";
        card.add(Ui.title(title,23)); card.add(Box.createVerticalStrut(8));
        card.add(Ui.muted(type.equals("transfer")?"Send money securely to another demo account.":"Choose a quick amount or enter a custom amount."));
        card.add(Box.createVerticalStrut(25));
        JTextField recipient=null;
        if(type.equals("transfer")){
            card.add(fieldLabel("Recipient account ID")); recipient=Ui.field(25); card.add(recipient); card.add(Box.createVerticalStrut(18));
        }
        card.add(fieldLabel("Amount (INR)")); JTextField amount=Ui.field(25); card.add(amount);
        if(!type.equals("transfer")) {
            card.add(Box.createVerticalStrut(14)); JLabel quick=fieldLabel("QUICK AMOUNTS");card.add(quick);
            JPanel chips=new JPanel(new FlowLayout(FlowLayout.LEFT,7,0));chips.setOpaque(false);
            for(String v:new String[]{"500","1000","2000","5000","10000"}){
                JButton chip=new JButton("₹"+NumberFormatUtil.group(v));chip.setFont(Ui.font(11,Font.BOLD));chip.setForeground(Ui.TEAL);
                chip.setBackground(new Color(230,244,241));chip.setBorder(new CompoundBorder(new LineBorder(new Color(210,233,227),1,true),new EmptyBorder(7,10,7,10)));
                chip.setFocusPainted(false);chip.addActionListener(e->amount.setText(v));chips.add(chip);
            }
            card.add(chips);
        }
        card.add(Box.createVerticalStrut(22));
        final JTextField recipientField=recipient;
        JButton submit=Ui.button(type.equals("withdraw")?"Continue to withdrawal":type.equals("deposit")?"Confirm deposit":"Continue to transfer",true);
        submit.setAlignmentX(Component.LEFT_ALIGNMENT);
        submit.addActionListener(e->{
            BigDecimal value=parseAmount(amount.getText()); if(value==null){error("Enter a valid positive amount with up to 2 decimal places.");return;}
            if(type.equals("withdraw")||type.equals("transfer")){
                if(!verifyPin())return;
            }
            if(type.equals("transfer")){
                Account target=bank.findByAccountId(recipientField.getText().trim());
                if(target==null){error("Recipient account was not found.");return;}
                if(target==account){error("You cannot transfer to your own account.");return;}
                int ok=JOptionPane.showConfirmDialog(this,"Transfer "+money(value)+" to "+target.getHolderName()+"?","Confirm transfer",JOptionPane.YES_NO_OPTION);
                if(ok==JOptionPane.YES_OPTION){
                    if(atm.transfer(account,recipientField.getText().trim(),value)){recordReceipt("TRANSFER",value,"Sent to "+target.getHolderName()+" ("+target.getAccountId()+")");success("Transfer completed successfully.");showView("dashboard");}
                    else error("Transfer failed. Check available balance.");
                }
            } else {
                int ok=JOptionPane.showConfirmDialog(this,(type.equals("withdraw")?"Withdraw ":"Deposit ")+money(value)+" "+(type.equals("withdraw")?"from":"to")+" your account?","Confirm transaction",JOptionPane.YES_NO_OPTION);
                if(ok==JOptionPane.YES_OPTION){
                    boolean done=type.equals("withdraw")?atm.withdraw(account,value):atm.deposit(account,value);
                    if(done){recordReceipt(type.toUpperCase(),value,type.equals("withdraw")?"Cash withdrawal":"Cash deposit");success(type.equals("withdraw")?"Withdrawal successful.":"Deposit successful.");showView("dashboard");}
                    else error(type.equals("withdraw")?"Insufficient funds or daily withdrawal limit exceeded.":"Deposit failed. Check the amount.");
                }
            }
        });
        card.add(submit); card.add(Box.createVerticalStrut(18));
        JButton back=Ui.button("Back to overview",false);back.setAlignmentX(Component.LEFT_ALIGNMENT);back.addActionListener(e->showView("dashboard"));card.add(back);
        wrapper.add(card);return wrapper;
    }

    private JPanel historyView() {
        JPanel p=Ui.card();p.setLayout(new BorderLayout(12,14));
        JPanel head=new JPanel(new BorderLayout());head.setOpaque(false);head.add(Ui.title("Transaction history",22),BorderLayout.WEST);
        JPanel actions=new JPanel(new FlowLayout(FlowLayout.RIGHT,7,0));actions.setOpaque(false);
        JButton receipt=Ui.button("Download Receipt (.txt / .pdf)",false);receipt.setPreferredSize(new Dimension(225,36));receipt.addActionListener(e->downloadLatestReceipt());
        JButton refresh=Ui.button("Refresh",false);refresh.setPreferredSize(new Dimension(90,36));refresh.addActionListener(e->showView("history"));
        actions.add(receipt);actions.add(refresh);head.add(actions,BorderLayout.EAST);
        p.add(head,BorderLayout.NORTH);p.add(recentTable(0),BorderLayout.CENTER);return p;
    }

    private JScrollPane recentTable(int limit) {
        String[] cols={"TYPE","DETAILS","DATE","AMOUNT"};
        DefaultTableModel model=new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
        List<Transaction> list=account.getTransactions(); int start=limit>0?Math.max(0,list.size()-limit):0;
        for(int i=list.size()-1;i>=start;i--){
            Transaction t=list.get(i); boolean credit=t.getType().equals("DEPOSIT")||t.getType().equals("RECEIVED");
            String sign=credit?"+":"−"; model.addRow(new Object[]{t.getType(),t.getDescription(),t.getFormattedDate(),sign+money(t.getAmount())});
        }
        JTable table=new JTable(model); table.setFillsViewportHeight(true); table.setBackground(Color.WHITE);
        table.setRowHeight(39);table.setFont(Ui.font(12,Font.PLAIN));table.setForeground(Ui.INK);table.setGridColor(Ui.BORDER);table.setShowVerticalLines(false);
        JTableHeader th=table.getTableHeader();th.setFont(Ui.font(11,Font.BOLD));th.setForeground(Ui.MUTED);th.setBackground(new Color(249,250,248));th.setPreferredSize(new Dimension(0,38));
        DefaultTableCellRenderer renderer=new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(JTable t,Object v,boolean selected,boolean focus,int row,int col){
                Component c=super.getTableCellRendererComponent(t,v,selected,focus,row,col);
                if(!selected){setBackground(Color.WHITE);setForeground(col==3?((String)t.getValueAt(row,0)).equals("DEPOSIT")||((String)t.getValueAt(row,0)).equals("RECEIVED")?new Color(13,138,84):new Color(190,65,72):Ui.INK);}
                if(col==3)setHorizontalAlignment(SwingConstants.RIGHT);else setHorizontalAlignment(SwingConstants.LEFT);
                setBorder(new EmptyBorder(0,10,0,10));return c;
            }
        };
        for(int i=0;i<table.getColumnCount();i++)table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);table.getColumnModel().getColumn(1).setPreferredWidth(220);
        table.getColumnModel().getColumn(2).setPreferredWidth(160);table.getColumnModel().getColumn(3).setPreferredWidth(140);
        JPanel holder=new JPanel(new BorderLayout());holder.setBackground(Color.WHITE);
        if(model.getRowCount()==0){
            JLabel empty=new JLabel("<html><div style='text-align:center'>No recent transactions yet.<br>Make a deposit or transfer to see activity.</div></html>",SwingConstants.CENTER);
            empty.setFont(Ui.font(13,Font.PLAIN));empty.setForeground(Ui.MUTED);empty.setOpaque(true);empty.setBackground(Color.WHITE);
            holder.add(empty,BorderLayout.CENTER);
        } else {
            holder.add(new JScrollPane(table),BorderLayout.CENTER);
        }
        JScrollPane scroll=new JScrollPane(holder);scroll.setBorder(new LineBorder(Ui.BORDER,1,true));scroll.getViewport().setBackground(Color.WHITE);return scroll;
    }

    private boolean verifyPin() {
        JPasswordField pin=new JPasswordField(8);pin.setFont(Ui.font(18,Font.PLAIN));
        JPanel panel=new JPanel();panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));panel.setBorder(new EmptyBorder(12,12,8,12));
        panel.add(Ui.title("Confirm your identity",17));panel.add(Box.createVerticalStrut(6));
        panel.add(Ui.muted("Enter your 4-digit PIN to authorize this transaction."));panel.add(Box.createVerticalStrut(12));panel.add(pin);
        int result=JOptionPane.showConfirmDialog(this,panel,"Security verification",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
        if(result!=JOptionPane.OK_OPTION)return false;
        String entered=new String(pin.getPassword());
        if(!entered.matches("\\d{4}")||!account.verifyPin(entered)){error("Incorrect PIN. The transaction was cancelled.");return false;}
        return true;
    }

    private void updateBalance() {
        if(balanceValue!=null)balanceValue.setText(balanceVisible?money(account.getBalance()):"₹XX,XXX.XX");
    }
    private void updateDailyLimit() {
        if(limitText==null||limitBar==null)return;
        BigDecimal used=account.getDailyWithdrawn();
        BigDecimal remaining=DAILY_LIMIT.subtract(used).max(BigDecimal.ZERO);
        limitText.setText("Daily withdrawal limit: "+money(remaining)+" remaining of "+money(DAILY_LIMIT));
        int pct=DAILY_LIMIT.signum()==0?0:used.multiply(new BigDecimal("100")).divide(DAILY_LIMIT,0,java.math.RoundingMode.HALF_UP).intValue();
        limitBar.setValue(Math.min(100,pct));
    }
    private void refresh(){updateBalance();updateDailyLimit();}
    private void showView(String key){
        activeView=key;((CardLayout)content.getLayout()).show(content,key);refresh();
        // Update sidebar selected styling.
        Container root=getContentPane(); if(root instanceof JPanel){
            Component side=((JPanel)root).getComponent(0);
            if(side instanceof JPanel)updateNav((JPanel)side,key);
        }
    }
    private void updateNav(JPanel side,String key){
        for(Component c:side.getComponents())if(c instanceof JPanel){
            JPanel panel=(JPanel)c;
            for(Component item:panel.getComponents())if(item instanceof JButton){
                JButton b=(JButton)item;Object k=b.getClientProperty("navKey");
                if(k!=null){boolean active=k.equals(key);b.setBackground(active?new Color(230,244,241):Color.WHITE);b.setForeground(active?Ui.TEAL:Ui.INK);}
            }
        }
    }
    private BigDecimal parseAmount(String text){
        try{BigDecimal v=new BigDecimal(text.trim());if(v.signum()<=0||v.scale()>2)return null;return v;}catch(Exception e){return null;}
    }
    private String money(BigDecimal value){return NumberFormatUtil.format(value);}
    private JLabel fieldLabel(String text){JLabel l=new JLabel(text);l.setFont(Ui.font(12,Font.BOLD));l.setForeground(Ui.INK);l.setAlignmentX(Component.LEFT_ALIGNMENT);return l;}
    private void success(String msg){JOptionPane.showMessageDialog(this,msg,"OASIS INFOBYTE",JOptionPane.INFORMATION_MESSAGE);}
    private void error(String msg){JOptionPane.showMessageDialog(this,msg,"Please check",JOptionPane.WARNING_MESSAGE);}

    private void recordReceipt(String type,BigDecimal amount,String details){
        try{
            String receipt="OASIS INFOBYTE - ATM RECEIPT\n"+
                    "----------------------------------------\n"+
                    "Account holder: "+account.getHolderName()+"\n"+
                    "Account number: "+account.getAccountId()+"\n"+
                    "Transaction: "+type+"\nAmount: "+money(amount)+"\nDetails: "+details+
                    "\nDate: "+java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"))+
                    "\nAvailable balance: "+money(account.getBalance())+"\n----------------------------------------\n";
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get("receipts"));
            java.nio.file.Files.write(java.nio.file.Paths.get("receipts","latest-receipt.txt"),receipt.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        }catch(Exception ignored){}
    }

    private void downloadLatestReceipt(){
        java.io.File file=new java.io.File("receipts/latest-receipt.txt");
        if(!file.exists()){error("No completed transaction receipt is available yet.");return;}
        Object[] choices={"Text file (.txt)","PDF file (.pdf)","Cancel"};
        int format=JOptionPane.showOptionDialog(this,"Choose receipt format","Download receipt",
                JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,choices,choices[0]);
        if(format<0||format==2)return;
        boolean pdf=format==1;
        JFileChooser chooser=new JFileChooser();
        chooser.setSelectedFile(new java.io.File(pdf?"Oasis-ATM-Receipt.pdf":"Oasis-ATM-Receipt.txt"));
        if(chooser.showSaveDialog(this)==JFileChooser.APPROVE_OPTION){
            try{
                String receipt=java.nio.file.Files.readString(file.toPath(),java.nio.charset.StandardCharsets.UTF_8);
                java.io.File target=chooser.getSelectedFile();
                if(pdf){
                    if(!target.getName().toLowerCase().endsWith(".pdf"))target=new java.io.File(target.getParentFile(),target.getName()+".pdf");
                    writeSimplePdf(target,receipt);
                }else{
                    if(!target.getName().toLowerCase().endsWith(".txt"))target=new java.io.File(target.getParentFile(),target.getName()+".txt");
                    java.nio.file.Files.writeString(target.toPath(),receipt,java.nio.charset.StandardCharsets.UTF_8);
                }
                success("Receipt saved successfully.");
            }catch(Exception ex){error("Could not save receipt: "+ex.getMessage());}
        }
    }

    private void writeSimplePdf(java.io.File target,String text) throws java.io.IOException {
        StringBuilder content=new StringBuilder("BT\n/F1 12 Tf\n50 790 Td\n16 TL\n");
        for(String line:text.split("\\R")){
            String safe=line.replace("₹","INR ").replace("—","-").replace("•","-")
                    .replace("\\","\\\\").replace("(","\\(").replace(")","\\)");
            content.append("(").append(safe).append(") Tj\nT*\n");
        }
        content.append("ET");
        String stream=content.toString();
        java.util.List<String> objects=new java.util.ArrayList<>();
        objects.add("<< /Type /Catalog /Pages 2 0 R >>");
        objects.add("<< /Type /Pages /Kids [3 0 R] /Count 1 >>");
        objects.add("<< /Type /Page /Parent 2 0 R /MediaBox [0 0 595 842] /Resources << /Font << /F1 4 0 R >> >> /Contents 5 0 R >>");
        objects.add("<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>");
        objects.add("<< /Length "+stream.getBytes(java.nio.charset.StandardCharsets.US_ASCII).length+" >>\nstream\n"+stream+"\nendstream");
        java.io.ByteArrayOutputStream out=new java.io.ByteArrayOutputStream();
        out.write("%PDF-1.4\n".getBytes(java.nio.charset.StandardCharsets.US_ASCII));
        java.util.List<Integer> offsets=new java.util.ArrayList<>(); offsets.add(0);
        for(int i=0;i<objects.size();i++){
            offsets.add(out.size());
            out.write((i+1+" 0 obj\n"+objects.get(i)+"\nendobj\n").getBytes(java.nio.charset.StandardCharsets.US_ASCII));
        }
        int xref=out.size();
        out.write(("xref\n0 "+(objects.size()+1)+"\n0000000000 65535 f \n").getBytes(java.nio.charset.StandardCharsets.US_ASCII));
        for(int i=1;i<offsets.size();i++)out.write(String.format("%010d 00000 n \n",offsets.get(i)).getBytes(java.nio.charset.StandardCharsets.US_ASCII));
        out.write(("trailer\n<< /Size "+(objects.size()+1)+" /Root 1 0 R >>\nstartxref\n"+xref+"\n%%EOF").getBytes(java.nio.charset.StandardCharsets.US_ASCII));
        java.nio.file.Files.write(target.toPath(),out.toByteArray());
    }

}
