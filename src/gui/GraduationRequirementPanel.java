package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GraduationRequirementPanel extends JPanel {
    private boolean isFirstVisit = true;
    private boolean passedEnglish = false;

    public GraduationRequirementPanel(StudentAppGUI app) {
        setLayout(new BorderLayout());
        renderFirstVisit(app);
    }

    private void renderFirstVisit(StudentAppGUI app) {
        // Create a new JPanel with GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        // Create GridBagConstraints to control component placement
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Add padding between components
        gbc.anchor = GridBagConstraints.WEST;

        // Create title
        JLabel titleLabel = new JLabel("졸업요건 확인 입력");
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 18));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Create labels and text fields
        JLabel totalCreditsLabel = new JLabel("   총 이수학점:");
        JTextField totalCreditsField = new JTextField(20);
        JLabel majorCreditsLabel = new JLabel("전공 이수학점:");
        JTextField majorCreditsField = new JTextField(20);
        JLabel genCreditsLabel = new JLabel("교양 이수학점:");
        JTextField genCreditsField = new JTextField(20);

        // Create O/X buttons for public English score
        JPanel englishPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton oButton = new JButton("O");
        JButton xButton = new JButton("X");
        oButton.setFont(new Font("Poppins", Font.BOLD, 18));
        xButton.setFont(new Font("Poppins", Font.BOLD, 18));
        oButton.setPreferredSize(new Dimension(130, 30));  
        xButton.setPreferredSize(new Dimension(130, 30));
        oButton.setBackground(Color.WHITE);
        oButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        xButton.setBackground(Color.WHITE);
        xButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        englishPanel.add(oButton);
        englishPanel.add(xButton);

        // Submit button
        JButton submitBtn = new JButton("제출");
        submitBtn.setFont(new Font("Poppins", Font.BOLD, 14));
        submitBtn.setBackground(Color.WHITE);
        submitBtn.setForeground(Color.BLACK);
        submitBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        submitBtn.setPreferredSize(new Dimension(300, 60));

        // Style for labels and text fields
        totalCreditsField.setFont(new Font("Poppins", Font.BOLD, 14));
        totalCreditsField.setBackground(Color.WHITE);
        totalCreditsField.setForeground(Color.BLACK);
        totalCreditsField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        totalCreditsField.setPreferredSize(new Dimension(200, 30));
        majorCreditsField.setFont(new Font("Poppins", Font.BOLD, 14));
        majorCreditsField.setBackground(Color.WHITE);
        majorCreditsField.setForeground(Color.BLACK);
        majorCreditsField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        majorCreditsField.setPreferredSize(new Dimension(200, 30));
        genCreditsField.setFont(new Font("Poppins", Font.BOLD, 14));
        genCreditsField.setBackground(Color.WHITE);
        genCreditsField.setForeground(Color.BLACK);
        genCreditsField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        genCreditsField.setPreferredSize(new Dimension(200, 30));

        // Layout components
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(totalCreditsLabel, gbc);

        gbc.gridx = 1;
        panel.add(totalCreditsField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(majorCreditsLabel, gbc);

        gbc.gridx = 1;
        panel.add(majorCreditsField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(genCreditsLabel, gbc);

        gbc.gridx = 1;
        panel.add(genCreditsField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("공인영어 성적:"), gbc);

        gbc.gridx = 1;
        panel.add(englishPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(submitBtn, gbc);

        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int totalCredits = Integer.parseInt(totalCreditsField.getText().trim());
                int majorCredits = Integer.parseInt(majorCreditsField.getText().trim());
                int genCredits = Integer.parseInt(genCreditsField.getText().trim());

                // Update the title and submit logic
                isFirstVisit = false;
                removeAll();
                renderResult(app, totalCredits, majorCredits, genCredits);
                revalidate();
                repaint();
            }
        });

        // O/X button action listeners
        oButton.addActionListener(e -> {
            passedEnglish = true;
            oButton.setBackground(Color.GREEN);  // Highlight "O"
            xButton.setBackground(null);  // Reset "X"
        });
        xButton.addActionListener(e -> {
            passedEnglish = false;
            xButton.setBackground(Color.RED);  // Highlight "X"
            oButton.setBackground(null);  // Reset "O"
        });

        JButton backButton = new JButton("←");
        backButton.addActionListener(e -> app.showPanel("main"));

        add(panel, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);
    }

    private void renderResult(StudentAppGUI app, int totalCredits, int majorCredits, int genCredits) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Add padding between components

        // Create labels for the result
        JLabel totalCreditsResult = new JLabel("총 이수학점: " + totalCredits + " / 140");
        JLabel majorCreditsResult = new JLabel("전공 이수학점: " + majorCredits + " / 72");
        JLabel genCreditsResult = new JLabel("교양 이수학점: " + genCredits + " / 30");
        JLabel englishResult = new JLabel("공인영어 성적: " + (passedEnglish ? "✅" : "❌"));

        // Style the result labels
        totalCreditsResult.setFont(new Font("Poppins", Font.BOLD, 14));
        majorCreditsResult.setFont(new Font("Poppins", Font.BOLD, 14));
        genCreditsResult.setFont(new Font("Poppins", Font.BOLD, 14));
        englishResult.setFont(new Font("Poppins", Font.BOLD, 14));

        // Add the result labels to the panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(totalCreditsResult, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(majorCreditsResult, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(genCreditsResult, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(englishResult, gbc);

        JButton backButton = new JButton("←");
        backButton.addActionListener(e -> app.showPanel("main"));

        add(panel, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);
    }
}
