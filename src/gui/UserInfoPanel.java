package gui;

import javax.swing.*;
import java.awt.*;

public class UserInfoPanel extends JPanel {
    public UserInfoPanel(StudentAppGUI app) {
        setLayout(new GridLayout(4, 2, 10, 10));

        JLabel studentIdLabel = new JLabel("학번");
        JTextField studentIdField = new JTextField();

        JLabel departmentLabel = new JLabel("소속 학과");
        JTextField departmentField = new JTextField();

        JButton backButton = new JButton("←");
        backButton.addActionListener(e -> app.showPanel("start"));

        JButton confirmButton = new JButton("확인");
        confirmButton.addActionListener(e -> app.showPanel("main"));

        add(studentIdLabel);
        add(studentIdField);
        add(departmentLabel);
        add(departmentField);
        add(backButton);
        add(confirmButton);
    }
}
