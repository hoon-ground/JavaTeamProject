package gui;

import javax.swing.*;
import java.awt.*;

public class StartPanel extends JPanel {
    public StartPanel(StudentAppGUI app) {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("KNU 시간표관리", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));

        try {
            String imagePath = "src/resources/images.jpeg";
            ImageIcon logoIcon = new ImageIcon(imagePath);
            JLabel logoLabel = new JLabel(logoIcon);
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            add(logoLabel, BorderLayout.CENTER);

        } catch (Exception e) {
            e.printStackTrace();
        }

        JButton startButton = new JButton("시작");
        startButton.setFont(new Font("Poppins", Font.BOLD, 14));
        startButton.setBackground(Color.WHITE);
        startButton.setForeground(Color.BLACK);
        startButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        startButton.setPreferredSize(new Dimension(30, 40));
        startButton.setFocusPainted(false);

        startButton.addActionListener(e -> app.showPanel("user"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(startButton, BorderLayout.CENTER);

        add(titleLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
}
