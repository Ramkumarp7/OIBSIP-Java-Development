package com.oasis.exam;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class ExamFrame extends JFrame {
    private final AppUser user;
    private final List<Question> questions = QuestionBank.getQuestions();
    private final int[] answers = new int[questions.size()];
    private int current = 0;
    private int secondsLeft = 30 * 60;
    private final JLabel timerLabel = new JLabel();
    private final JLabel progressLabel = new JLabel();
    private final JLabel questionLabel = new JLabel();
    private final JRadioButton[] choices = new JRadioButton[4];
    private final ButtonGroup group = new ButtonGroup();
    private final JPanel optionsPanel = new JPanel();
    private final javax.swing.Timer timer;

    public ExamFrame(AppUser user) {
        this.user = user;
        Arrays.fill(answers, -1);
        setTitle(Main.APP_TITLE);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setSize(1050, 720);
        setMinimumSize(new Dimension(820, 620));
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(ExamFrame.this,
                        "Your examination is in progress. Close and submit it now?",
                        "Confirm exit", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (choice == JOptionPane.YES_OPTION) submitExam(true);
            }
        });

        JPanel page = new JPanel(new BorderLayout(20, 20));
        page.setBackground(Main.CANVAS);
        page.setBorder(BorderFactory.createEmptyBorder(24, 30, 26, 30));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JPanel heading = new JPanel(); heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        JLabel brand = new JLabel("OASIS INFOBYTE");
        brand.setFont(Ui.font(12, Font.BOLD)); brand.setForeground(Main.BRAND);
        heading.add(brand); heading.add(Box.createVerticalStrut(5));
        heading.add(Ui.heading("Online Examination System", 22));
        header.add(heading, BorderLayout.WEST);
        timerLabel.setFont(Ui.font(18, Font.BOLD)); timerLabel.setForeground(Main.BRAND);
        timerLabel.setOpaque(true); timerLabel.setBackground(Main.LILAC);
        timerLabel.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));
        header.add(timerLabel, BorderLayout.EAST);
        page.add(header, BorderLayout.NORTH);

        JPanel card = Ui.card();
        card.setLayout(new BorderLayout(18, 22));
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        progressLabel.setFont(Ui.font(13, Font.BOLD)); progressLabel.setForeground(Main.MUTED);
        top.add(progressLabel, BorderLayout.WEST);
        top.add(Ui.muted("Java Development Assessment"), BorderLayout.EAST);
        card.add(top, BorderLayout.NORTH);

        JPanel center = new JPanel(); center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        questionLabel.setFont(Ui.font(21, Font.BOLD)); questionLabel.setForeground(Main.TEXT);
        questionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        center.add(questionLabel); center.add(Box.createVerticalStrut(24));
        optionsPanel.setOpaque(false);
        optionsPanel.setLayout(new GridLayout(4, 1, 0, 12));
        for (int i = 0; i < 4; i++) {
            choices[i] = new JRadioButton();
            choices[i].setFont(Ui.font(15, Font.PLAIN));
            choices[i].setForeground(Main.SECONDARY);
            choices[i].setOpaque(true); choices[i].setBackground(Color.WHITE);
            choices[i].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Main.BORDER, 1, true),
                    BorderFactory.createEmptyBorder(13, 14, 13, 14)));
            final int index = i;
            choices[i].addActionListener(e -> answers[current] = index);
            group.add(choices[i]); optionsPanel.add(choices[i]);
        }
        center.add(optionsPanel);
        card.add(center, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        JButton previous = Ui.button("Previous", false);
        previous.addActionListener(e -> { saveSelection(); if (current > 0) { current--; renderQuestion(); } });
        JButton next = Ui.button("Next", true);
        next.addActionListener(e -> { saveSelection(); if (current < questions.size() - 1) { current++; renderQuestion(); } });
        JButton submit = Ui.button("Submit examination", false);
        submit.setForeground(new Color(190, 55, 65));
        submit.addActionListener(e -> {
            saveSelection();
            int choice = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to submit your examination?",
                    "Confirm submission", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (choice == JOptionPane.YES_OPTION) submitExam(false);
        });
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); nav.setOpaque(false);
        nav.add(previous); nav.add(next);
        footer.add(nav, BorderLayout.WEST); footer.add(submit, BorderLayout.EAST);
        card.add(footer, BorderLayout.SOUTH);
        page.add(card, BorderLayout.CENTER);
        setContentPane(page);

        renderQuestion();
        updateTimer();
        timer = new javax.swing.Timer(1000, e -> {
            secondsLeft--;
            updateTimer();
            if (secondsLeft <= 0) submitExam(true);
        });
        timer.start();
    }

    private void saveSelection() {
        for (int i = 0; i < choices.length; i++) if (choices[i].isSelected()) answers[current] = i;
    }
    private void renderQuestion() {
        Question q = questions.get(current);
        progressLabel.setText("QUESTION " + (current + 1) + " OF " + questions.size());
        questionLabel.setText("<html><div style='width:760px'>" + (current + 1) + ". " + escape(q.getPrompt()) + "</div></html>");
        String[] opts = q.getOptions();
        for (int i = 0; i < 4; i++) {
            choices[i].setText("<html><b>" + (char)('A' + i) + ".</b> &nbsp;" + escape(opts[i]) + "</html>");
            choices[i].setSelected(answers[current] == i);
        }
        group.clearSelection();
        if (answers[current] >= 0) choices[answers[current]].setSelected(true);
        revalidate(); repaint();
    }
    private String escape(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
    private void updateTimer() {
        int m = Math.max(0, secondsLeft) / 60, s = Math.max(0, secondsLeft) % 60;
        timerLabel.setText(String.format("TIME LEFT  %02d:%02d", m, s));
        timerLabel.setForeground(secondsLeft <= 300 ? new Color(190, 55, 65) : Main.BRAND);
    }
    private void submitExam(boolean automatic) {
        if (timer.isRunning()) timer.stop();
        int correct = 0;
        for (int i = 0; i < questions.size(); i++)
            if (answers[i] == questions.get(i).getCorrectIndex()) correct++;
        int elapsed = 30 * 60 - Math.max(0, secondsLeft);
        dispose();
        new ResultFrame(user, questions, answers, correct, elapsed, automatic).setVisible(true);
    }
}
