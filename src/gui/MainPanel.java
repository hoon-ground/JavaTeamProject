package gui;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    public MainPanel(StudentAppGUI app) {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        JButton addCourseBtn = new JButton("과목 추가");
        JButton selectSemesterBtn = new JButton("학기 선택");
        topPanel.add(addCourseBtn);
        topPanel.add(selectSemesterBtn);

        JPanel coursePanel = new JPanel(new GridLayout(2, 2, 10, 10));
        coursePanel.add(new JButton("과목1"));
        coursePanel.add(new JButton("과목2"));
        coursePanel.add(new JButton("과목3"));

        JPanel bottomPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        JButton creditBtn = new JButton("학점계산기");
        JButton gradCheckBtn = new JButton("졸업요건 확인");
        JButton backBtn = new JButton("←");
        backBtn.addActionListener(e -> app.showPanel("user"));

        bottomPanel.add(creditBtn);
        bottomPanel.add(gradCheckBtn);
        bottomPanel.add(backBtn);

        add(topPanel, BorderLayout.NORTH);
        add(coursePanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}