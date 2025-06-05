package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

class AddCoursePanel extends JPanel {
    private final List<String> addedCourses = new ArrayList<>();

    public AddCoursePanel(StudentAppGUI app) {
        setLayout(new BorderLayout());

        JTextField searchField = new JTextField();
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(new JLabel("검색: "), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        JPanel courseListPanel = new JPanel();
        courseListPanel.setLayout(new BoxLayout(courseListPanel, BoxLayout.Y_AXIS));

        String[] courses = {"과목1", "과목2", "과목3"};
        for (String course : courses) {
            JPanel row = new JPanel(new BorderLayout());
            JLabel name = new JLabel(course);
            JButton addBtn = new JButton("추가");
            addBtn.addActionListener(e -> {
                if (addedCourses.contains(course)) {
                    JOptionPane.showMessageDialog(this, "중복된 시간입니다.");
                } else {
                    addedCourses.add(course);
                    JOptionPane.showMessageDialog(this, course + " 추가 완료");
                }
            });
            row.add(name, BorderLayout.CENTER);
            row.add(addBtn, BorderLayout.EAST);
            courseListPanel.add(row);
        }

        JScrollPane scrollPane = new JScrollPane(courseListPanel);
        JButton backButton = new JButton("←");
        backButton.addActionListener(e -> app.showPanel("main"));

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);
    }
}