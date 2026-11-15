package com.oasis.exam;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ResultFrame extends JFrame {
    public ResultFrame(AppUser user, List<Question> questions, int[] answers,
                       int correct, int elapsedSeconds, boolean automatic) {
        setTitle(Main.APP_TITLE);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1050, 760);
        setMinimumSize(new Dimension(820, 620));
        setLocationRelativeTo(null);

        JPanel page = new JPanel(new BorderLayout(20, 20));
        page.setBackground(Main.CANVAS);
        page.setBorder(BorderFactory.createEmptyBorder(24, 30, 26, 30));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JPanel brand = new JPanel(); brand.setOpaque(false);
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));
        JLabel logo = new JLabel("OASIS INFOBYTE");
        logo.setFont(Ui.font(12, Font.BOLD)); logo.setForeground(Main.BRAND);
        brand.add(logo); brand.add(Box.createVerticalStrut(5));
        brand.add(Ui.heading("Online Examination System", 22));
        header.add(brand, BorderLayout.WEST);
        JButton logout = Ui.button("Logout", true);
        logout.addActionListener(e -> { dispose(); new LoginFrame().setVisible(true); });
        header.add(logout, BorderLayout.EAST);
        page.add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(); content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        JPanel summary = Ui.card();
        summary.setLayout(new BoxLayout(summary, BoxLayout.Y_AXIS));
        JLabel status = Ui.heading(automatic ? "Time is up — examination submitted" : "Examination submitted successfully", 21);
        status.setAlignmentX(Component.LEFT_ALIGNMENT);
        summary.add(status); summary.add(Box.createVerticalStrut(8));
        JLabel name = Ui.muted("Candidate: " + user.getDisplayName() + "  •  Time taken: " + formatTime(elapsedSeconds));
        name.setAlignmentX(Component.LEFT_ALIGNMENT); summary.add(name);
        summary.add(Box.createVerticalStrut(20));
        JPanel scoreRow = new JPanel(new GridLayout(1, 3, 12, 0)); scoreRow.setOpaque(false);
        scoreRow.add(metric("SCORE", correct + " / " + questions.size()));
        scoreRow.add(metric("CORRECT", String.valueOf(correct)));
        scoreRow.add(metric("INCORRECT / UNANSWERED", String.valueOf(questions.size() - correct)));
        summary.add(scoreRow);
        content.add(summary); content.add(Box.createVerticalStrut(18));

        JPanel details = Ui.card();
        details.setLayout(new BorderLayout(10, 12));
        details.add(Ui.heading("Answer review", 18), BorderLayout.NORTH);
        JPanel rows = new JPanel(); rows.setOpaque(false);
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            boolean isCorrect = answers[i] == q.getCorrectIndex();
            JPanel row = new JPanel(new BorderLayout(10, 6));
            row.setOpaque(false);
            row.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            JPanel text = new JPanel(); text.setOpaque(false);
            text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
            JLabel prompt = new JLabel("<html><b>" + (i + 1) + ". " + html(q.getPrompt()) + "</b></html>");
            prompt.setFont(Ui.font(13, Font.PLAIN)); prompt.setForeground(Main.TEXT);
            text.add(prompt); text.add(Box.createVerticalStrut(5));
            String[] options = q.getOptions();
            String your = answers[i] < 0 ? "Not answered" : options[answers[i]];
            String correctAnswer = options[q.getCorrectIndex()];
            JLabel response = Ui.muted("Your answer: " + your + "  |  Correct answer: " + correctAnswer);
            text.add(response);
            text.add(Ui.muted("Why: " + q.getExplanation()));
            row.add(text, BorderLayout.CENTER);
            JLabel result = new JLabel(isCorrect ? "CORRECT" : "INCORRECT");
            result.setFont(Ui.font(11, Font.BOLD));
            result.setForeground(isCorrect ? new Color(28, 135, 91) : new Color(190, 55, 65));
            row.add(result, BorderLayout.EAST);
            rows.add(row);
            if (i < questions.size() - 1) {
                JSeparator sep = new JSeparator(); sep.setForeground(Main.BORDER); rows.add(sep);
            }
        }
        details.add(new JScrollPane(rows), BorderLayout.CENTER);
        details.setPreferredSize(new Dimension(900, 420));
        content.add(details);
        page.add(content, BorderLayout.CENTER);
        setContentPane(page);
    }

    private JPanel metric(String label, String value) {
        JPanel p = new JPanel(); p.setOpaque(true); p.setBackground(Main.LILAC);
        p.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JLabel l = new JLabel(label); l.setFont(Ui.font(10, Font.BOLD)); l.setForeground(Main.MUTED);
        JLabel v = new JLabel(value); v.setFont(Ui.font(24, Font.BOLD)); v.setForeground(Main.TEXT);
        p.add(l); p.add(Box.createVerticalStrut(5)); p.add(v);
        return p;
    }
    private String formatTime(int seconds) { return String.format("%02d min %02d sec", seconds / 60, seconds % 60); }
    private String html(String s) { return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;"); }
}
