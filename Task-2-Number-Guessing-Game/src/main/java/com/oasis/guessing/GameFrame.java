package com.oasis.guessing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Cards-Against-Humanity-inspired dark table UI for the OASIS number game. */
public class GameFrame extends JFrame {
    private static final Color BLACK = Color.BLACK;
    private static final Color WHITE = Color.WHITE;
    private static final Color RED = new Color(0xFE, 0x2F, 0x2F);
    private static final Color VIOLET = new Color(0x73, 0x33, 0xF1);
    private static final Color GOLD = new Color(0xD7, 0xB7, 0x3B);
    private static final Color MUTED = new Color(0x65, 0x65, 0x65);
    private static final Color PALE = new Color(0xED, 0xE5, 0xFF);
    private static final Color FIELD = new Color(0xF3, 0xF3, 0xF3);

    private final GameEngine engine = new GameEngine();
    private final List<RoundResult> results = new ArrayList<>();
    private final Set<Integer> guessesThisRound = new HashSet<>();
    private final CardLayout cards = new CardLayout();
    private final JPanel screens = new JPanel(cards);
    private JComboBox<Difficulty> difficultyBox;
    private JTextField guessField;
    private JLabel feedback, temperatureLabel, attemptsLabel, roundLabel, rangeLabel;
    private JLabel scoreLabel, historyCount, pointsLabel, streakLabel;
    private JLabel roundsStat, winsStat, rateStat, streakStat, totalPointsStat;
    private JButton guessButton, playAgainButton;
    private DefaultListModel<String> historyModel;
    private JPanel guessChips;
    private int roundNumber = 0, wins = 0, totalPoints = 0, currentStreak = 0, bestStreak = 0;
    private boolean roundRecorded = false;
    private int possibleMin = 1, possibleMax = 100;
    private final List<JLabel> attemptMarks = new ArrayList<>();

    public GameFrame() {
        setTitle("OASIS INFOBYTE | Number Guessing Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(780, 700));
        setSize(1040, 900);
        setLocationRelativeTo(null);
        screens.setBackground(BLACK);
        screens.add(wrapScrollable(buildHome()), "home");
        screens.add(wrapScrollable(buildGame()), "game");
        add(screens);
        show("home");
    }

    private JScrollPane wrapScrollable(JPanel panel) {
        JScrollPane scroll = new JScrollPane(panel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BLACK);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return scroll;
    }

    private JPanel buildHome() {
        TablePanel page = new TablePanel();
        page.setLayout(new BoxLayout(page, BoxLayout.Y_AXIS));
        page.setBorder(new EmptyBorder(26, 54, 34, 54));
        page.add(header());
        page.add(Box.createVerticalStrut(42));

        JLabel kicker = label("OASIS INFOBYTE PRESENTS  /  JAVA DEVELOPMENT", 12, GOLD, true);
        kicker.setAlignmentX(Component.LEFT_ALIGNMENT);
        page.add(kicker);
        page.add(Box.createVerticalStrut(14));
        JLabel title = label("GUESS THE\nNUMBER.", 62, WHITE, true);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        page.add(title);
        page.add(Box.createVerticalStrut(12));
        JLabel desc = label("A secret number. A handful of chances. One shot at glory.", 17, WHITE, true);
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);
        page.add(desc);
        page.add(Box.createVerticalStrut(30));

        JPanel main = new JPanel(new GridLayout(1, 2, 20, 0));
        main.setOpaque(false);
        main.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
        main.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel startCard = playingCard(RED);
        startCard.setLayout(new BoxLayout(startCard, BoxLayout.Y_AXIS));
        startCard.add(label("YOUR MOVE", 12, RED, true));
        startCard.add(Box.createVerticalStrut(12));
        startCard.add(label("Pick your\ndifficulty.", 30, BLACK, true));
        startCard.add(Box.createVerticalStrut(18));
        difficultyBox = new JComboBox<>(Difficulty.values());
        difficultyBox.setFont(font(14, Font.BOLD));
        difficultyBox.setForeground(BLACK);
        difficultyBox.setBackground(WHITE);
        difficultyBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        startCard.add(difficultyBox);
        startCard.add(Box.createVerticalStrut(16));
        JButton start = primaryButton("DEAL ME IN", RED, WHITE);
        start.setAlignmentX(Component.LEFT_ALIGNMENT);
        start.addActionListener(e -> startRound());
        startCard.add(start);

        JPanel rules = playingCard(VIOLET);
        rules.setBackground(PALE);
        rules.setLayout(new BoxLayout(rules, BoxLayout.Y_AXIS));
        rules.add(label("THE HOUSE RULES", 12, VIOLET, true));
        rules.add(Box.createVerticalStrut(12));
        rules.add(ruleLine("EASY", "1–50", "10 TRIES"));
        rules.add(Box.createVerticalStrut(10));
        rules.add(ruleLine("MEDIUM", "1–100", "7 TRIES"));
        rules.add(Box.createVerticalStrut(10));
        rules.add(ruleLine("HARD", "1–200", "5 TRIES"));
        rules.add(Box.createVerticalStrut(17));
        rules.add(label("Too high? Go lower. Too low? Go higher.", 13, BLACK, false));
        main.add(startCard);
        main.add(rules);
        page.add(main);
        page.add(Box.createVerticalStrut(22));

        JPanel stats = playingCard(GOLD);
        stats.setLayout(new BorderLayout(10, 8));
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 112));
        stats.add(label("YOUR TABLE SCORE", 12, new Color(0x8A, 0x6E, 0x10), true), BorderLayout.NORTH);
        JPanel statGrid = new JPanel(new GridLayout(1, 5, 8, 0));
        statGrid.setOpaque(false);
        roundsStat = statCell("ROUNDS", "0");
        winsStat = statCell("WINS", "0");
        rateStat = statCell("WIN RATE", "0%");
        streakStat = statCell("WIN STREAK", "0");
        totalPointsStat = statCell("POINTS", "0");
        statGrid.add(roundsStat); statGrid.add(winsStat); statGrid.add(rateStat);
        statGrid.add(streakStat); statGrid.add(totalPointsStat);
        stats.add(statGrid, BorderLayout.CENTER);
        page.add(stats);
        page.add(Box.createVerticalStrut(18));
        JLabel foot = label("NO SEARCHING. NO CHEATING. JUST GOOD GUESSES.", 11, new Color(190, 190, 190), true);
        foot.setAlignmentX(Component.CENTER_ALIGNMENT);
        page.add(foot);
        return page;
    }

    private JLabel statCell(String heading, String value) {
        JLabel cell = new JLabel(statHtml(heading, value), SwingConstants.CENTER);
        cell.setForeground(BLACK);
        return cell;
    }

    private String statHtml(String heading, String value) {
        return "<html><div style='text-align:center'><span style='font-size:10px;color:#777777'>" + heading
                + "</span><br><span style='font-size:20px;font-weight:800'>" + value + "</span></div></html>";
    }

    private JLabel ruleLine(String tier, String range, String tries) {
        return label(tier + ":  " + range + "  •  " + tries, 16, BLACK, true);
    }

    private JPanel buildGame() {
        TablePanel page = new TablePanel();
        page.setLayout(new BoxLayout(page, BoxLayout.Y_AXIS));
        page.setBorder(new EmptyBorder(26, 54, 34, 54));
        page.add(header());
        page.add(Box.createVerticalStrut(26));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        roundLabel = label("ROUND 01", 13, GOLD, true);
        attemptsLabel = label("7 CHANCES LEFT", 13, WHITE, true);
        top.add(roundLabel, BorderLayout.WEST);
        top.add(attemptsLabel, BorderLayout.EAST);
        top.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        page.add(top);
        page.add(Box.createVerticalStrut(14));

        JPanel gameCard = playingCard(VIOLET);
        gameCard.setLayout(new BoxLayout(gameCard, BoxLayout.Y_AXIS));
        gameCard.setBorder(new EmptyBorder(26, 34, 26, 34));
        JLabel eyebrow = label("THE NUMBER IS OUT THERE", 12, VIOLET, true);
        eyebrow.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameCard.add(eyebrow);
        gameCard.add(Box.createVerticalStrut(10));
        rangeLabel = label("POSSIBLE RANGE: 1 — 100", 22, BLACK, true);
        rangeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameCard.add(rangeLabel);
        gameCard.add(Box.createVerticalStrut(8));
        JLabel rangeHint = label("THE RANGE NARROWS AFTER EVERY GUESS", 10, MUTED, true);
        rangeHint.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameCard.add(rangeHint);
        gameCard.add(Box.createVerticalStrut(20));

        guessField = new PlaceholderField("00");
        guessField.setFont(new Font("Segoe UI", Font.BOLD, 28));
        guessField.setHorizontalAlignment(JTextField.CENTER);
        guessField.setForeground(BLACK);
        guessField.setBackground(FIELD);
        guessField.setCaretColor(VIOLET);
        guessField.setToolTipText("Enter a number (example: 00)");
        guessField.putClientProperty("JTextField.placeholderText", "00");
        guessField.setMaximumSize(new Dimension(330, 68));
        guessField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xB8, 0xB8, 0xB8), 2), new EmptyBorder(8, 16, 8, 16)));
        guessField.addActionListener(e -> submitGuess());
        guessField.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameCard.add(guessField);
        gameCard.add(Box.createVerticalStrut(14));

        guessButton = primaryButton("LOCK IN YOUR GUESS", VIOLET, WHITE);
        guessButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        guessButton.addActionListener(e -> submitGuess());
        gameCard.add(guessButton);
        gameCard.add(Box.createVerticalStrut(18));

        JPanel feedbackRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        feedbackRow.setOpaque(false);
        feedback = label("MAKE IT COUNT.", 18, BLACK, true);
        temperatureLabel = label("READY", 12, VIOLET, true);
        feedbackRow.add(feedback);
        feedbackRow.add(temperatureLabel);
        gameCard.add(feedbackRow);
        gameCard.add(Box.createVerticalStrut(16));

        JPanel chipsWrap = new JPanel();
        chipsWrap.setOpaque(false);
        chipsWrap.setLayout(new BoxLayout(chipsWrap, BoxLayout.Y_AXIS));
        JLabel guessesTitle = label("GUESSES THIS ROUND", 10, MUTED, true);
        guessesTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        chipsWrap.add(guessesTitle);
        chipsWrap.add(Box.createVerticalStrut(7));
        guessChips = new JPanel(new FlowLayout(FlowLayout.CENTER, 7, 4));
        guessChips.setOpaque(false);
        chipsWrap.add(guessChips);
        gameCard.add(chipsWrap);
        gameCard.add(Box.createVerticalStrut(12));

        JPanel progress = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        progress.setOpaque(false);
        for (int i = 0; i < 10; i++) {
            JLabel mark = new JLabel(" ");
            mark.setOpaque(true);
            mark.setBackground(new Color(220, 220, 220));
            mark.setPreferredSize(new Dimension(22, 9));
            attemptMarks.add(mark);
            progress.add(mark);
        }
        gameCard.add(progress);
        gameCard.add(Box.createVerticalStrut(18));

        pointsLabel = label("ROUND POINTS: 0", 13, BLACK, true);
        pointsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameCard.add(pointsLabel);
        gameCard.add(Box.createVerticalStrut(14));
        playAgainButton = primaryButton("PLAY ANOTHER ROUND", GOLD, BLACK);
        playAgainButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playAgainButton.setVisible(false);
        playAgainButton.addActionListener(e -> startRound());
        gameCard.add(playAgainButton);
        JButton home = outlineButton("BACK TO THE TABLE");
        home.setAlignmentX(Component.CENTER_ALIGNMENT);
        home.addActionListener(e -> { updateStats(); show("home"); });
        gameCard.add(Box.createVerticalStrut(12));
        gameCard.add(home);
        page.add(gameCard);
        page.add(Box.createVerticalStrut(18));

        JPanel scoreboard = playingCard(RED);
        scoreboard.setLayout(new GridLayout(1, 2, 12, 0));
        scoreboard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 76));
        streakLabel = label("WIN STREAK: 0", 14, BLACK, true);
        scoreboard.add(streakLabel);
        scoreboard.add(label("POINTS ARE BANKED ON A WIN", 12, BLACK, true));
        page.add(scoreboard);
        page.add(Box.createVerticalStrut(18));

        JPanel history = playingCard(GOLD);
        history.setLayout(new BorderLayout(8, 8));
        history.setMaximumSize(new Dimension(Integer.MAX_VALUE, 175));
        JPanel histHead = new JPanel(new BorderLayout());
        histHead.setOpaque(false);
        histHead.add(label("PAST ROUNDS", 12, new Color(0x8A, 0x6E, 0x10), true), BorderLayout.WEST);
        historyCount = label("0 PLAYED", 11, MUTED, true);
        histHead.add(historyCount, BorderLayout.EAST);
        history.add(histHead, BorderLayout.NORTH);
        historyModel = new DefaultListModel<>();
        JList<String> list = new JList<>(historyModel);
        list.setFont(font(13, Font.BOLD));
        list.setForeground(BLACK);
        list.setBackground(WHITE);
        list.setVisibleRowCount(3);
        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(BorderFactory.createLineBorder(BLACK, 1));
        history.add(scroll, BorderLayout.CENTER);
        page.add(history);
        return page;
    }

    private void startRound() {
        Difficulty difficulty = (Difficulty) difficultyBox.getSelectedItem();
        engine.startRound(difficulty);
        roundNumber++;
        roundRecorded = false;
        possibleMin = 1;
        possibleMax = difficulty.getMaximum();
        roundLabel.setText(String.format("ROUND %02d  /  %s", roundNumber, difficulty.name()));
        rangeLabel.setText("POSSIBLE RANGE: " + possibleMin + " — " + possibleMax);
        attemptsLabel.setText(difficulty.getAttempts() + " CHANCES LEFT");
        feedback("MAKE IT COUNT.", BLACK);
        temperatureLabel.setText("READY");
        temperatureLabel.setForeground(VIOLET);
        guessField.setText("");
        guessField.setEnabled(true);
        guessButton.setEnabled(true);
        guessButton.setVisible(true);
        playAgainButton.setVisible(false);
        pointsLabel.setText("ROUND POINTS: 0");
        guessChips.removeAll();
        guessesThisRound.clear();
        guessChips.revalidate();
        guessChips.repaint();
        resetMarks();
        show("game");
        guessField.requestFocusInWindow();
    }

    private void submitGuess() {
        String raw = guessField.getText().trim();
        if (raw.isEmpty()) { feedback("PUT A NUMBER ON THE CARD.", RED); return; }
        final int value;
        try { value = Integer.parseInt(raw); }
        catch (NumberFormatException ex) { feedback("WHOLE NUMBERS ONLY.", RED); return; }
        if (value < 1 || value > engine.getDifficulty().getMaximum()) {
            feedback("KEEP IT BETWEEN 1 AND " + engine.getDifficulty().getMaximum() + ".", RED);
            return;
        }
        if (guessesThisRound.contains(value)) {
            feedback("YOU ALREADY PLAYED " + value + ". TRY A DIFFERENT NUMBER.", RED);
            return;
        }
        guessesThisRound.add(value);
        GameEngine.GuessStatus status = engine.guess(value);
        if (status == GameEngine.GuessStatus.NO_ATTEMPTS) return;
        attemptsLabel.setText(engine.getAttemptsRemaining() + " CHANCES LEFT");
        updateMarks();
        guessField.setText("");
        if (status == GameEngine.GuessStatus.TOO_LOW || status == GameEngine.GuessStatus.TOO_HIGH) {
            int distance = Math.abs(engine.getTarget() - value);
            addGuessChip(value, status == GameEngine.GuessStatus.TOO_LOW);
            if (status == GameEngine.GuessStatus.TOO_LOW) {
                possibleMin = Math.max(possibleMin, value + 1);
                feedback("TOO LOW. AIM HIGHER.", VIOLET);
            } else {
                possibleMax = Math.min(possibleMax, value - 1);
                feedback("TOO HIGH. BRING IT DOWN.", RED);
            }
            rangeLabel.setText("POSSIBLE RANGE: " + possibleMin + " — " + possibleMax);
            setTemperature(distance);
        } else if (status == GameEngine.GuessStatus.CORRECT) {
            addGuessChip(value, null);
            feedback("BULLSEYE. YOU GOT IT IN " + engine.getAttemptsUsed() + "!", new Color(0x18, 0x80, 0x00));
            temperatureLabel.setText("PERFECT HIT");
            temperatureLabel.setForeground(GOLD.darker());
            finishRound(true);
        }
        if (!engine.isWon() && engine.isOver()) {
            feedback("NO CHANCES LEFT. IT WAS " + engine.getTarget() + ".", RED);
            finishRound(false);
        }
    }

    private void setTemperature(int distance) {
        String text;
        Color color;
        if (distance <= 3) { text = "SCORCHING HOT"; color = RED; }
        else if (distance <= 10) { text = "WARM"; color = new Color(0xC0, 0x75, 0x00); }
        else if (distance > 20) { text = "FREEZING"; color = VIOLET; }
        else { text = "COOL"; color = new Color(0x55, 0x55, 0x55); }
        temperatureLabel.setText(text);
        temperatureLabel.setForeground(color);
    }

    private void addGuessChip(int value, Boolean tooLow) {
        String suffix = tooLow == null ? " ✓" : (tooLow ? " ↑" : " ↓");
        Color accent = tooLow == null ? GOLD : (tooLow ? VIOLET : RED);
        JLabel chip = new JLabel(value + suffix);
        chip.setFont(font(12, Font.BOLD));
        chip.setForeground(BLACK);
        chip.setOpaque(true);
        chip.setBackground(tooLow == null ? new Color(0xFF, 0xFE, 0xD5) : WHITE);
        chip.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(accent, 2),
                new EmptyBorder(5, 10, 5, 10)));
        guessChips.add(chip);
        guessChips.revalidate();
        guessChips.repaint();
    }

    private void finishRound(boolean won) {
        if (roundRecorded) return;
        roundRecorded = true;
        int awarded = 0;
        if (won) {
            wins++;
            currentStreak++;
            bestStreak = Math.max(bestStreak, currentStreak);
            int difficultyMultiplier = switch (engine.getDifficulty()) {
                case EASY -> 1;
                case MEDIUM -> 2;
                case HARD -> 3;
            };
            int streakMultiplier = Math.min(5, currentStreak);
            awarded = (engine.getAttemptsRemaining() + 1) * 100 * difficultyMultiplier * streakMultiplier;
            totalPoints += awarded;
        } else {
            currentStreak = 0;
        }
        RoundResult result = new RoundResult(roundNumber, engine.getDifficulty(), engine.getTarget(),
                engine.getAttemptsUsed(), won, LocalDateTime.now());
        results.add(result);
        historyModel.add(0, result.summary() + (won ? "  •  +" + awarded + " pts" : "  •  +0 pts"));
        historyCount.setText(results.size() + " PLAYED");
        int streakMultiplier = won ? Math.min(5, currentStreak) : 0;
        pointsLabel.setText("ROUND POINTS: " + awarded + "   •   STREAK MULTIPLIER: "
                + (streakMultiplier == 0 ? "RESET" : streakMultiplier + "×") + "   •   TOTAL: " + totalPoints);
        if (streakLabel != null) streakLabel.setText("WIN STREAK: " + currentStreak + "   •   BEST: " + bestStreak);
        guessField.setEnabled(false);
        guessButton.setEnabled(false);
        guessButton.setVisible(false);
        playAgainButton.setVisible(true);
        updateStats();
    }

    private void updateStats() {
        if (roundsStat == null) return;
        int played = results.size();
        int rate = played == 0 ? 0 : (int) Math.round(wins * 100.0 / played);
        roundsStat.setText(statHtml("ROUNDS", String.valueOf(played)));
        winsStat.setText(statHtml("WINS", String.valueOf(wins)));
        rateStat.setText(statHtml("WIN RATE", rate + "%"));
        streakStat.setText(statHtml("WIN STREAK", String.valueOf(currentStreak)));
        totalPointsStat.setText(statHtml("POINTS", String.valueOf(totalPoints)));
        if (streakLabel != null) streakLabel.setText("WIN STREAK: " + currentStreak + "   •   BEST: " + bestStreak);
    }

    private void resetMarks() { for (JLabel mark : attemptMarks) mark.setBackground(new Color(220, 220, 220)); }
    private void updateMarks() {
        int used = engine.getAttemptsUsed();
        for (int i = 0; i < attemptMarks.size(); i++) {
            if (i < used) attemptMarks.get(i).setBackground(engine.isWon() ? GOLD : (i % 2 == 0 ? RED : VIOLET));
            else attemptMarks.get(i).setBackground(new Color(220, 220, 220));
        }
    }
    private void feedback(String text, Color color) { feedback.setText(text); feedback.setForeground(color); }
    private void show(String name) { cards.show(screens, name); }

    private JPanel header() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel brand = label("OASIS INFOBYTE", 17, WHITE, true);
        JLabel tag = label("JAVA DEVELOPMENT  /  TASK 02", 11, GOLD, true);
        p.add(brand, BorderLayout.WEST);
        p.add(tag, BorderLayout.EAST);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        return p;
    }

    private JPanel playingCard(Color accent) {
        JPanel p = new JPanel();
        p.setBackground(WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(accent, 3),
                new EmptyBorder(20, 22, 20, 22)));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        return p;
    }

    private JLabel label(String text, int size, Color color, boolean bold) {
        JLabel l = new JLabel("<html>" + text.replace("\n", "<br>") + "</html>");
        l.setFont(font(size, bold ? Font.BOLD : Font.PLAIN));
        l.setForeground(color);
        return l;
    }
    private Font font(int size, int style) { return new Font("Arial", style, size); }

    private JButton primaryButton(String text, Color fill, Color textColor) {
        JButton b = new JButton(text);
        b.setFont(font(14, Font.BOLD));
        b.setForeground(textColor);
        b.setBackground(fill);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(true);
        b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(12, 28, 12, 28));
        return b;
    }

    private JButton outlineButton(String text) {
        JButton b = new JButton(text);
        b.setFont(font(12, Font.BOLD));
        b.setForeground(BLACK);
        b.setBackground(WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BLACK, 2),
                new EmptyBorder(9, 22, 9, 22)));
        b.setOpaque(true);
        return b;
    }

    private static class PlaceholderField extends JTextField {
        private final String placeholder;
        PlaceholderField(String placeholder) { this.placeholder = placeholder; }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty() && !isFocusOwner()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0xA0, 0xA0, 0xA0));
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(placeholder)) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(placeholder, x, y);
                g2.dispose();
            }
        }
    }

    /** Black felt with small, fully-contained corner marks instead of clipped floating cards. */
    private static class TablePanel extends JPanel {
        TablePanel() { setOpaque(true); setBackground(BLACK); }
        @Override protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            drawCornerBadge(g, 12, 58, 30, 42, -8, PALE, VIOLET, "?" );
            drawCornerBadge(g, Math.max(12, getWidth() - 42), 58, 30, 42, 8, new Color(0xFF, 0xFE, 0xD5), RED, "!");
            g.dispose();
        }
        private static void drawCornerBadge(Graphics2D g, int x, int y, int w, int h, double angle,
                                            Color fill, Color border, String text) {
            AffineTransform old = g.getTransform();
            g.rotate(Math.toRadians(angle), x + w / 2.0, y + h / 2.0);
            g.setColor(fill);
            g.fillRoundRect(x, y, w, h, 8, 8);
            g.setColor(border);
            g.setStroke(new BasicStroke(2));
            g.drawRoundRect(x, y, w, h, 8, 8);
            g.setColor(BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 19));
            g.drawString(text, x + 10, y + 27);
            g.setTransform(old);
        }
    }
}
