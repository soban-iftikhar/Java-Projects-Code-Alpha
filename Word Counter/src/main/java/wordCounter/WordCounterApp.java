package wordCounter;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class WordCounterApp extends JFrame {
    private final JTextArea textArea;
    private final JLabel wordCountLabel;
    private final JLabel charCountLabel;
    private final JLabel charNoSpacesCountLabel;
    private final JLabel sentenceCountLabel;
    private final JLabel readabilityScoreLabel;
    private final Color primaryColor = new Color(25, 118, 210); // Material blue
    private final Color darkGray = new Color(33, 33, 33);
    private final Color lightGray = new Color(245, 245, 245);
    private final BarGraphPanel barGraphPanel; // Make barGraphPanel a class field

    public WordCounterApp() {
        // Set up the frame
        setTitle("Word Counter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        try {
            // Load the image using ClassLoader
            Image icon = Toolkit.getDefaultToolkit().getImage(
                    getClass().getClassLoader().getResource("logo.jpeg")
            );
            setIconImage(icon);
        } catch (Exception e) {
            System.err.println("Error loading icon: " + e.getMessage());
        }
        // Set the look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Word Counter");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Create content panel with split pane
        // Make splitPane a class field
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(550);
        splitPane.setDividerSize(8);
        splitPane.setBorder(null);
        splitPane.setContinuousLayout(true);

        // Create left panel (input area)
        JPanel leftPanel = new JPanel(new BorderLayout(0, 15));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(new EmptyBorder(20, 20, 20, 10));

        // Create input section title
        JLabel inputTitleLabel = new JLabel("Enter Your Text");
        inputTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        inputTitleLabel.setForeground(darkGray);

        // Create text area with custom styling
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        textArea.setBackground(lightGray);

        // Create a custom border for the scroll pane
        Border lineBorder = BorderFactory.createLineBorder(new Color(200, 200, 200));
        Border emptyBorder = new EmptyBorder(5, 5, 5, 5);
        CompoundBorder compoundBorder = new CompoundBorder(lineBorder, emptyBorder);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(compoundBorder);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Add document listener to update counts as user types
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateCounts();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateCounts();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateCounts();
            }
        });

        // Add components to left panel
        leftPanel.add(inputTitleLabel, BorderLayout.NORTH);
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        // Create right panel (output area)
        JPanel rightPanel = new JPanel(new BorderLayout(0, 15));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(new EmptyBorder(20, 10, 20, 20));

        // Create stats title
        JLabel statsTitleLabel = new JLabel("Text Statistics");
        statsTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        statsTitleLabel.setForeground(darkGray);

        // Create a panel to hold all statistics with proper padding
        JPanel statsOuterPanel = new JPanel(new BorderLayout());
        statsOuterPanel.setBackground(Color.WHITE);
        statsOuterPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        // Create stats panel with proper layout - using BoxLayout for better control
        JPanel statsCardsPanel = new JPanel();
        statsCardsPanel.setLayout(new BoxLayout(statsCardsPanel, BoxLayout.Y_AXIS));
        statsCardsPanel.setBackground(Color.WHITE);

        // Create styled stat cards
        wordCountLabel = createStatsValueLabel();
        charCountLabel = createStatsValueLabel();
        charNoSpacesCountLabel = createStatsValueLabel();
        sentenceCountLabel = createStatsValueLabel();
        readabilityScoreLabel = createStatsValueLabel();

        // Add cards with improved styling and spacing
        statsCardsPanel.add(createStatCard("Word Count", wordCountLabel));
        statsCardsPanel.add(Box.createVerticalStrut(10)); // Add spacing
        statsCardsPanel.add(createStatCard("Character Count", charCountLabel));
        statsCardsPanel.add(Box.createVerticalStrut(10)); // Add spacing
        statsCardsPanel.add(createStatCard("Characters (no spaces)", charNoSpacesCountLabel));
        statsCardsPanel.add(Box.createVerticalStrut(10)); // Add spacing
        statsCardsPanel.add(createStatCard("Sentence Count", sentenceCountLabel));
        statsCardsPanel.add(Box.createVerticalStrut(10)); // Add spacing
        statsCardsPanel.add(createStatCard("Readability Score", readabilityScoreLabel));

        // Add stats panel to a scroll pane for better handling of different window sizes
        JScrollPane statsScrollPane = new JScrollPane(statsCardsPanel);
        statsScrollPane.setBorder(null);
        statsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        statsScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        statsScrollPane.setBackground(Color.WHITE);

        statsOuterPanel.add(statsScrollPane, BorderLayout.CENTER);

        // Add components to right panel
        rightPanel.add(statsTitleLabel, BorderLayout.NORTH);
        rightPanel.add(statsOuterPanel, BorderLayout.CENTER);

        // Add a visual element at the bottom of the right panel
        JPanel visualPanel = new JPanel(new BorderLayout());
        visualPanel.setBackground(Color.WHITE);
        visualPanel.setPreferredSize(new Dimension(0, 150));
        visualPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        // Add a title for the visualization
        JLabel visualTitleLabel = new JLabel("Text Visualization");
        visualTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        visualTitleLabel.setForeground(darkGray);
        visualTitleLabel.setBorder(new EmptyBorder(0, 5, 5, 0));

        JPanel visualContentPanel = new JPanel(new BorderLayout());
        visualContentPanel.setBackground(lightGray);
        visualContentPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        // Create a simple visualization panel
        barGraphPanel = new BarGraphPanel();
        visualContentPanel.add(barGraphPanel, BorderLayout.CENTER);

        visualPanel.add(visualTitleLabel, BorderLayout.NORTH);
        visualPanel.add(visualContentPanel, BorderLayout.CENTER);

        rightPanel.add(visualPanel, BorderLayout.SOUTH);

        // Add panels to split pane
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);

        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Add footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(lightGray);
        footerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel footerLabel = new JLabel("Type or paste text to analyze • Statistics update in real-time");
        footerLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        footerLabel.setForeground(new Color(100, 100, 100));

        footerPanel.add(footerLabel);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);

        // Initialize counts
        updateCounts();
    }

    // Create a styled card for each statistic
    private JPanel createStatCard(String title, JLabel valueLabel) {
        // Use a panel with FlowLayout for the entire card to ensure it takes full width
        JPanel cardOuterPanel = new JPanel();
        cardOuterPanel.setLayout(new BoxLayout(cardOuterPanel, BoxLayout.X_AXIS));
        cardOuterPanel.setBackground(Color.WHITE);
        cardOuterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Create the actual card with proper styling
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBackground(lightGray);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(10, 15, 10, 15)
        ));

        // Create a small colored indicator
        JPanel indicatorPanel = new JPanel();
        indicatorPanel.setPreferredSize(new Dimension(5, 0));
        indicatorPanel.setBackground(primaryColor);

        // Create title and value with proper alignment
        JPanel contentPanel = new JPanel(new BorderLayout(5, 5));
        contentPanel.setBackground(lightGray);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(darkGray);

        contentPanel.add(titleLabel, BorderLayout.WEST);
        contentPanel.add(valueLabel, BorderLayout.EAST);

        // Assemble the card
        cardPanel.add(indicatorPanel, BorderLayout.WEST);
        cardPanel.add(contentPanel, BorderLayout.CENTER);

        // Add the card to the outer panel with full width
        cardOuterPanel.add(cardPanel);

        return cardOuterPanel;
    }

    private JLabel createStatsValueLabel() {
        JLabel label = new JLabel("0");
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(primaryColor);
        return label;
    }

    // Simple bar graph visualization panel
    private class BarGraphPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            // Get current values
            int wordCount = Integer.parseInt(wordCountLabel.getText());
            int charCount = Integer.parseInt(charCountLabel.getText());
            int sentenceCount = Integer.parseInt(sentenceCountLabel.getText());

            // Calculate max value for scaling
            int maxValue = Math.max(wordCount, Math.max(charCount, sentenceCount));
            if (maxValue == 0) maxValue = 1; // Avoid division by zero

            // Draw bars
            int barWidth = 60;
            int spacing = 40;
            int startX = width / 2 - (3 * barWidth + 2 * spacing) / 2;
            int barBottom = height - 30;
            int maxBarHeight = height - 50;

            // Word count bar
            drawBar(g2d, startX, barBottom, barWidth,
                    (int)((double)wordCount / maxValue * maxBarHeight),
                    "Words", wordCount, new Color(33, 150, 243));

            // Character count bar
            drawBar(g2d, startX + barWidth + spacing, barBottom, barWidth,
                    (int)((double)charCount / maxValue * maxBarHeight),
                    "Chars", charCount, new Color(76, 175, 80));

            // Sentence count bar
            drawBar(g2d, startX + 2 * (barWidth + spacing), barBottom, barWidth,
                    (int)((double)sentenceCount / maxValue * maxBarHeight),
                    "Sentences", sentenceCount, new Color(255, 152, 0));
        }

        private void drawBar(Graphics2D g2d, int x, int y, int width, int height,
                             String label, int value, Color color) {
            // Ensure minimum height for visibility
            height = Math.max(height, 2);

            // Draw bar
            g2d.setColor(color);
            g2d.fillRoundRect(x, y - height, width, height, 10, 10);

            // Draw value
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
            String valueStr = String.valueOf(value);
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(valueStr);

            // Only draw text if bar is tall enough
            if (height > 20) {
                g2d.drawString(valueStr, x + (width - textWidth) / 2, y - height + 20);
            } else {
                // Draw above the bar if it's too short
                g2d.setColor(color);
                g2d.drawString(valueStr, x + (width - textWidth) / 2, y - height - 5);
            }

            // Draw label
            g2d.setColor(darkGray);
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            textWidth = fm.stringWidth(label);
            g2d.drawString(label, x + (width - textWidth) / 2, y + 15);
        }
    }

    private void updateCounts() {
        String text = textArea.getText();

        // Update word count
        int wordCount = countWords(text);
        wordCountLabel.setText(String.valueOf(wordCount));

        // Update character count (with spaces)
        int charCount = text.length();
        charCountLabel.setText(String.valueOf(charCount));

        // Update character count (without spaces)
        int charNoSpacesCount = text.replaceAll("\\s", "").length();
        charNoSpacesCountLabel.setText(String.valueOf(charNoSpacesCount));

        // Update sentence count
        int sentenceCount = countSentences(text);
        sentenceCountLabel.setText(String.valueOf(sentenceCount));

        // Update readability score
        double readabilityScore = calculateReadabilityScore(text, wordCount, sentenceCount);
        readabilityScoreLabel.setText(String.format("%.2f", readabilityScore));

        // Trigger repaint of the visualization
        if (barGraphPanel != null) {
            barGraphPanel.repaint();
        }
    }

    private int countWords(String text) {
        if (text.trim().isEmpty()) {
            return 0;
        }
        return text.trim().split("\\s+").length;
    }

    private int countSentences(String text) {
        if (text.trim().isEmpty()) {
            return 0;
        }

        // Count sentences by splitting on period, exclamation mark, or question mark
        // followed by a space or end of text
        String[] sentences = text.split("[.!?]+\\s*");

        // If the last split resulted in an empty string, don't count it
        if (sentences.length > 0 && sentences[sentences.length - 1].trim().isEmpty()) {
            return sentences.length - 1;
        }

        return sentences.length;
    }

    private int countSyllables(String word) {
        word = word.toLowerCase();

        // Special case for empty strings
        if (word.isEmpty()) {
            return 0;
        }

        // Count vowel groups
        int count = 0;
        boolean lastWasVowel = false;
        char[] vowels = {'a', 'e', 'i', 'o', 'u', 'y'};

        // For each character in the word
        for (int i = 0; i < word.length(); i++) {
            boolean isVowel = false;

            // Check if the character is a vowel
            for (char vowel : vowels) {
                if (word.charAt(i) == vowel) {
                    isVowel = true;
                    break;
                }
            }

            // If we have a new vowel group
            if (isVowel && !lastWasVowel) {
                count++;
            }

            lastWasVowel = isVowel;
        }

        // Handle silent 'e' at the end of the word
        if (word.length() > 2 && word.endsWith("e") && !word.endsWith("le")) {
            count--;
        }

        // Every word has at least one syllable
        return Math.max(1, count);
    }

    private double calculateReadabilityScore(String text, int wordCount, int sentenceCount) {
        if (wordCount == 0 || sentenceCount == 0) {
            return 0.0;
        }

        // Count total syllables
        int totalSyllables = 0;
        String[] words = text.trim().split("\\s+");

        for (String word : words) {
            // Remove punctuation
            word = word.replaceAll("[^a-zA-Z]", "");
            if (!word.isEmpty()) {
                totalSyllables += countSyllables(word);
            }
        }

        // Calculate Flesch-Kincaid readability score
        // Formula: 0.39 * (words/sentences) + 11.8 * (syllables/words) - 15.59
        double avgWordsPerSentence = (double) wordCount / sentenceCount;
        double avgSyllablesPerWord = (double) totalSyllables / wordCount;

        return 0.39 * avgWordsPerSentence + 11.8 * avgSyllablesPerWord - 15.59;
    }
}