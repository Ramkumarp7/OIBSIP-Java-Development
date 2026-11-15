package com.oasis.exam;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DashboardFrame extends JFrame {
    private final AppUser user;
    private final JPanel root = new JPanel(new BorderLayout());
    private final JPanel content = new JPanel(new CardLayout());
    private final JPanel dashboard = new JPanel(new BorderLayout(18,18));
    private final JPanel examCards = new JPanel(new GridLayout(0,1,0,14));
    private final JTextField search = new JTextField(22);
    private String category = "All";
    private boolean dark = false;

    public DashboardFrame(AppUser user) {
        this.user = user;
        setTitle(Main.APP_TITLE);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1180, 780);
        setMinimumSize(new Dimension(980, 680));
        setLocationRelativeTo(null);
        root.setBackground(Main.CANVAS);
        root.add(sidebar(), BorderLayout.WEST);
        root.add(mainArea(), BorderLayout.CENTER);
        setContentPane(root);
        showView("dashboard");
    }

    private JPanel sidebar() {
        JPanel side = new JPanel(new BorderLayout());
        side.setPreferredSize(new Dimension(220, 0));
        side.setBackground(new Color(255,255,255));
        side.setBorder(new MatteBorder(0,0,0,1,Main.BORDER));
        JPanel top = new JPanel(); top.setOpaque(false);
        top.setBorder(new EmptyBorder(25,18,20,14));
        top.setLayout(new BoxLayout(top,BoxLayout.Y_AXIS));
        JLabel logo = new JLabel("OASIS INFOBYTE");
        logo.setFont(Ui.font(13,Font.BOLD)); logo.setForeground(Main.BRAND);
        top.add(logo); top.add(Box.createVerticalStrut(7));
        JLabel product = Ui.heading("Online Examination",16); top.add(product);
        top.add(Box.createVerticalStrut(3)); top.add(Ui.muted("STUDENT WORKSPACE"));
        side.add(top,BorderLayout.NORTH);

        JPanel nav = new JPanel(); nav.setOpaque(false);
        nav.setBorder(new EmptyBorder(8,12,8,12));
        nav.setLayout(new BoxLayout(nav,BoxLayout.Y_AXIS));
        addNav(nav,"▦   Dashboard","dashboard");
        addNav(nav,"▤   Available Exams","exams");
        addNav(nav,"▥   My Results & Analytics","results");
        addNav(nav,"♙   Profile & Security","profile");
        side.add(nav,BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(4,8)); bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(12,14,20,14));
        JLabel signed = Ui.muted("SIGNED IN AS");
        JLabel name = new JLabel(user.getDisplayName()); name.setFont(Ui.font(13,Font.BOLD)); name.setForeground(Main.TEXT);
        JButton logout = Ui.button("↪  Logout",false);
        logout.setHorizontalAlignment(SwingConstants.LEFT);
        logout.addActionListener(e->{dispose();new LoginFrame().setVisible(true);});
        bottom.add(signed,BorderLayout.NORTH); bottom.add(name,BorderLayout.CENTER); bottom.add(logout,BorderLayout.SOUTH);
        side.add(bottom,BorderLayout.SOUTH);
        return side;
    }

    private void addNav(JPanel nav,String label,String key) {
        JButton b = new JButton(label);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setFont(Ui.font(13,Font.BOLD)); b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(13,12,13,8)); b.setBackground(Color.WHITE); b.setForeground(Main.SECONDARY);
        b.setMaximumSize(new Dimension(190,44)); b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.addActionListener(e->showView(key));
        nav.add(b); nav.add(Box.createVerticalStrut(5));
    }

    private JPanel mainArea() {
        JPanel area = new JPanel(new BorderLayout(18,18));
        area.setBackground(Main.CANVAS);
        area.setBorder(new EmptyBorder(24,28,24,28));
        JPanel header = new JPanel(new BorderLayout()); header.setOpaque(false);
        JPanel title = new JPanel(); title.setOpaque(false); title.setLayout(new BoxLayout(title,BoxLayout.Y_AXIS));
        title.add(Ui.heading("Student Dashboard",23));
        title.add(Box.createVerticalStrut(4)); title.add(Ui.muted("Your learning and assessment overview"));
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0)); actions.setOpaque(false);
        JButton theme = Ui.button("☾  Dark mode",false);
        theme.addActionListener(e->{dark=!dark; Main.setTheme(dark); SwingUtilities.updateComponentTreeUI(this); theme.setText(dark?"☀  Light mode":"☾  Dark mode"); applyTheme();});
        actions.add(theme);
        header.add(title,BorderLayout.WEST); header.add(actions,BorderLayout.EAST);
        area.add(header,BorderLayout.NORTH);

        dashboard.setOpaque(false);
        dashboard.add(greetingBanner(),BorderLayout.NORTH);
        JPanel middle = new JPanel(); middle.setOpaque(false); middle.setLayout(new BoxLayout(middle,BoxLayout.Y_AXIS));
        middle.add(profileCard()); middle.add(Box.createVerticalStrut(16));
        JPanel examPanel = cardPanel(); examPanel.setLayout(new BorderLayout(12,14));
        JPanel examHead = new JPanel(new BorderLayout()); examHead.setOpaque(false);
        examHead.add(Ui.heading("Available assessments",18),BorderLayout.WEST);
        search.setFont(Ui.font(13,Font.PLAIN)); search.setBorder(new CompoundBorder(new LineBorder(Main.BORDER,1,true),new EmptyBorder(8,10,8,10)));
        search.setToolTipText("Search exams");
        search.putClientProperty("JTextField.placeholderText", "Search assessments..."); search.addActionListener(e->filterCards());
        examHead.add(search,BorderLayout.EAST);
        examPanel.add(examHead,BorderLayout.NORTH);
        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT,7,0)); filters.setOpaque(false);
        for(String f:new String[]{"All","Java","OOPs","Completed"}) {
            JButton pill = Ui.button(f, f.equals("All"));
            pill.setBorder(new EmptyBorder(7,14,7,14));
            pill.addActionListener(e->{category=f; for(Component c:filters.getComponents()) if(c instanceof JButton) ((JButton)c).setBackground(((JButton)c).getText().equals(f)?Main.BRAND:Color.WHITE); filterCards();});
            filters.add(pill);
        }
        JPanel north = new JPanel(); north.setOpaque(false); north.setLayout(new BoxLayout(north,BoxLayout.Y_AXIS));
        north.add(examHead); north.add(Box.createVerticalStrut(12)); north.add(filters);
        examPanel.add(north,BorderLayout.NORTH);
        examCards.setOpaque(false); examCards.add(assessmentCard());
        examPanel.add(examCards,BorderLayout.CENTER);
        middle.add(examPanel);
        dashboard.add(middle,BorderLayout.CENTER);

        content.add(dashboard,"dashboard");
        content.add(examsView(),"exams");
        content.add(resultsView(),"results");
        content.add(profileView(),"profile");
        area.add(content,BorderLayout.CENTER);
        return area;
    }

    private JPanel greetingBanner() {
        JPanel p = cardPanel(); p.setLayout(new BorderLayout());
        JPanel left = new JPanel(); left.setOpaque(false); left.setLayout(new BoxLayout(left,BoxLayout.Y_AXIS));
        left.add(Ui.heading("Welcome back, "+user.getDisplayName(),24));
        left.add(Box.createVerticalStrut(7));
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy",Locale.ENGLISH));
        left.add(Ui.muted(date+"   •   Good to see you again"));
        p.add(left,BorderLayout.CENTER);
        JLabel status = new JLabel("●  System Ready");
        status.setFont(Ui.font(12,Font.BOLD)); status.setForeground(new Color(22,163,74));
        status.setBorder(new EmptyBorder(8,12,8,12)); status.setOpaque(true); status.setBackground(new Color(240,253,244));
        p.add(status,BorderLayout.EAST);
        return p;
    }

    private JPanel profileCard() {
        JPanel p = cardPanel(); p.setLayout(new BorderLayout(18,8));
        JPanel avatarWrap = new JPanel(new GridBagLayout());
        avatarWrap.setOpaque(false);
        avatarWrap.setPreferredSize(new Dimension(76,76));
        JLabel avatar = new JLabel(initials(user.getDisplayName()),SwingConstants.CENTER);
        avatar.setOpaque(true); avatar.setBackground(new Color(219,234,254)); avatar.setForeground(Main.BRAND);
        avatar.setFont(Ui.font(22,Font.BOLD)); avatar.setPreferredSize(new Dimension(64,64));
        avatar.setMinimumSize(new Dimension(64,64)); avatar.setMaximumSize(new Dimension(64,64));
        avatar.setBorder(new LineBorder(new Color(191,219,254),1,true));
        avatarWrap.add(avatar);
        p.add(avatarWrap,BorderLayout.WEST);
        JPanel details = new JPanel(new GridLayout(2,2,20,12)); details.setOpaque(false);
        details.add(info("STUDENT ID","OIB-JD-4390"));
        details.add(info("REGISTERED EMAIL","student@oasisinfobyte.com"));
        details.add(info("ROLE","Java Development Intern"));
        details.add(info("ACCOUNT STATUS","Active"));
        p.add(details,BorderLayout.CENTER);
        JButton edit = Ui.button("✎  Update profile",false);
        edit.setPreferredSize(new Dimension(170,42));
        edit.setMinimumSize(new Dimension(170,42));
        edit.setMaximumSize(new Dimension(170,42));
        edit.addActionListener(e->profileDialog());
        JPanel editWrap = new JPanel(new GridBagLayout());
        editWrap.setOpaque(false);
        editWrap.setPreferredSize(new Dimension(184,76));
        editWrap.add(edit);
        p.add(editWrap,BorderLayout.EAST);
        return p;
    }

    private JPanel info(String label,String value) {
        JPanel p=new JPanel();p.setOpaque(false);p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        JLabel l=Ui.muted(label); l.setFont(Ui.font(10,Font.BOLD));
        JLabel v=new JLabel(value);v.setFont(Ui.font(13,Font.BOLD));v.setForeground(Main.TEXT);
        p.add(l);p.add(Box.createVerticalStrut(4));p.add(v);return p;
    }

    private JPanel assessmentCard() {
        JPanel p=cardPanel();p.setLayout(new BorderLayout(16,8));
        JPanel text=new JPanel();text.setOpaque(false);text.setLayout(new BoxLayout(text,BoxLayout.Y_AXIS));
        JLabel assessmentTitle = Ui.heading("Java Development Assessment",17);
        assessmentTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel assessmentSubtitle = Ui.muted("Core Java • Fundamentals • Multiple choice");
        assessmentSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        text.add(assessmentTitle);
        text.add(Box.createVerticalStrut(6));text.add(assessmentSubtitle);
        text.add(Box.createVerticalStrut(11));
        JPanel chips=new JPanel(new FlowLayout(FlowLayout.LEFT,7,0));chips.setOpaque(false);
        chips.add(chip("☷  10 MCQs"));chips.add(chip("◷  30 Mins"));chips.add(chip("✓  Pass: 60%"));
        chips.add(chip("Beginner / Intermediate"));
        chips.setAlignmentX(Component.LEFT_ALIGNMENT);
        text.add(chips);
        p.add(text,BorderLayout.CENTER);
        JButton start=Ui.button("Start examination",true);
        start.setPreferredSize(new Dimension(170,42));
        start.setMinimumSize(new Dimension(170,42));
        start.setMaximumSize(new Dimension(170,42));
        start.setBackground(Main.BRAND); start.setForeground(Color.WHITE);
        start.addActionListener(e->preExamChecklist());
        JPanel startWrap = new JPanel(new GridBagLayout());
        startWrap.setOpaque(false);
        startWrap.setPreferredSize(new Dimension(190,90));
        startWrap.add(start);
        p.add(startWrap,BorderLayout.EAST);
        return p;
    }

    private JLabel chip(String s) {
        JLabel l=new JLabel(s);l.setFont(Ui.font(11,Font.BOLD));l.setForeground(Main.SECONDARY);
        l.setOpaque(true);l.setBackground(new Color(241,245,249));l.setBorder(new EmptyBorder(7,9,7,9));return l;
    }

    private JPanel cardPanel() {
        JPanel p=new JPanel();p.setBackground(Color.WHITE);
        p.setBorder(new CompoundBorder(new LineBorder(Main.BORDER,1,true),new EmptyBorder(18,20,18,20)));
        return p;
    }

    private JPanel examsView() {
        JPanel p=cardPanel();p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        p.add(Ui.heading("Available Exams",22));p.add(Box.createVerticalStrut(12));
        p.add(Ui.muted("Browse assessments assigned to your internship track."));
        p.add(Box.createVerticalStrut(20));p.add(assessmentCard());return wrapPage(p);
    }
    private JPanel resultsView() {
        JPanel p=cardPanel();p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        p.add(Ui.heading("My Results & Analytics",22));p.add(Box.createVerticalStrut(12));
        p.add(Ui.muted("Your completed assessment results will appear here after submission."));
        p.add(Box.createVerticalStrut(18));p.add(info("ASSESSMENTS COMPLETED","0"));p.add(Box.createVerticalStrut(8));p.add(info("AVERAGE SCORE","—"));
        return wrapPage(p);
    }
    private JPanel profileView() {
        JPanel p=cardPanel();p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        p.add(Ui.heading("Profile & Security",22));p.add(Box.createVerticalStrut(10));
        p.add(Ui.muted("Manage your display name and sign-in password."));
        p.add(Box.createVerticalStrut(20));
        JButton edit=Ui.button("✎  Edit profile",true);edit.addActionListener(e->profileDialog());p.add(edit);
        return wrapPage(p);
    }
    private JPanel wrapPage(JPanel p) {
        JPanel w=new JPanel(new BorderLayout());w.setOpaque(false);w.add(p,BorderLayout.NORTH);return w;
    }
    private void showView(String key) {
        CardLayout cl=(CardLayout)content.getLayout();cl.show(content,key);
    }

    private void filterCards() {
        String q=search.getText().trim().toLowerCase(Locale.ROOT);
        boolean visible=(category.equals("All")||category.equals("Java")||category.equals("OOPs")) &&
                ("java development assessment core java fundamentals".contains(q));
        for(Component c:examCards.getComponents()) c.setVisible(visible);
        examCards.revalidate();examCards.repaint();
    }

    private void profileDialog() {
        JTextField name=new JTextField(user.getDisplayName(),20);
        JPasswordField pass=new JPasswordField(user.getPassword(),20);
        JPanel tabs=new JPanel(new BorderLayout(0,10));
        JTabbedPane pane=new JTabbedPane();
        JPanel namePanel=new JPanel(new GridLayout(2,1,6,6));namePanel.setBorder(new EmptyBorder(18,18,18,18));
        namePanel.add(new JLabel("Display name"));namePanel.add(name);
        JPanel passPanel=new JPanel(new GridLayout(2,1,6,6));passPanel.setBorder(new EmptyBorder(18,18,18,18));
        passPanel.add(new JLabel("New password"));passPanel.add(pass);
        JPanel colorPanel=new JPanel(new FlowLayout(FlowLayout.LEFT,12,18));
        colorPanel.add(new JLabel("Choose avatar color"));
        String[] colors={"Blue","Violet","Teal","Rose"};
        JComboBox<String> color=new JComboBox<>(colors);colorPanel.add(color);
        pane.addTab("Edit Name",namePanel);pane.addTab("Change Password",passPanel);pane.addTab("Avatar Color",colorPanel);
        tabs.add(pane);
        int result=JOptionPane.showConfirmDialog(this,tabs,"Profile quick edit",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
        if(result==JOptionPane.OK_OPTION) {
            String n=name.getText().trim(),pw=new String(pass.getPassword()).trim();
            if(n.isEmpty()||pw.length()<4){toast("Enter a name and password of at least 4 characters");return;}
            user.setDisplayName(n);user.setPassword(pw);
            try {UserStore.save(user);toast("Profile updated successfully");}
            catch(Exception ex){toast("Could not save profile: "+ex.getMessage());}
        }
    }

    private void preExamChecklist() {
        JCheckBox timer=new JCheckBox("I understand the timer auto-submits the examination");
        JCheckBox full=new JCheckBox("I have enabled fullscreen mode (recommended)");
        JPanel p=new JPanel();p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));p.setBorder(new EmptyBorder(12,10,8,10));
        p.add(Ui.heading("Before you begin",18));p.add(Box.createVerticalStrut(12));
        p.add(Ui.muted("Please confirm the following before starting your 30-minute test."));
        p.add(Box.createVerticalStrut(12));p.add(timer);p.add(Box.createVerticalStrut(8));p.add(full);
        int r=JOptionPane.showConfirmDialog(this,p,"Examination checklist",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
        if(r==JOptionPane.OK_OPTION) {
            if(!timer.isSelected()||!full.isSelected()){toast("Please confirm both checklist items");return;}
            dispose();new ExamFrame(user).setVisible(true);
        }
    }

    private void toast(String message) {
        JWindow w=new JWindow(this);
        JPanel p=new JPanel(new BorderLayout());p.setBackground(new Color(30,41,59));
        p.setBorder(new CompoundBorder(new LineBorder(new Color(71,85,105),1,true),new EmptyBorder(12,18,12,18)));
        JLabel l=new JLabel("✓  "+message);l.setForeground(Color.WHITE);l.setFont(Ui.font(13,Font.BOLD));p.add(l);
        w.setContentPane(p);w.pack();
        Point loc=getLocationOnScreen();w.setLocation(loc.x+getWidth()-w.getWidth()-28,loc.y+getHeight()-w.getHeight()-35);
        w.setVisible(true);
        new javax.swing.Timer(2600,e->{w.dispose();((javax.swing.Timer)e.getSource()).stop();}).start();
    }
    private String initials(String n) {
        String[] a=n.trim().split("\\s+");if(a.length==1)return a[0].substring(0,Math.min(2,a[0].length())).toUpperCase();
        return (a[0].substring(0,1)+a[a.length-1].substring(0,1)).toUpperCase();
    }
    private void applyTheme() {
        Color bg=dark?new Color(15,23,42):Main.CANVAS;
        root.setBackground(bg);repaint();
    }
}
