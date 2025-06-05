package gui;

import javax.swing.*;
import java.awt.*;

public class StartPanel extends JPanel {
    public StartPanel(StudentAppGUI app) {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("KNU 학생관리앱", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        JButton startButton = new JButton("시작");
        startButton.addActionListener(e -> app.showPanel("user"));

        add(titleLabel, BorderLayout.CENTER);
        add(startButton, BorderLayout.SOUTH);
    }
}
