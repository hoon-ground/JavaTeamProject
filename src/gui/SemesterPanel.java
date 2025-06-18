package gui;

import javax.swing.*;
import java.awt.*;

public class SemesterPanel extends JPanel {
    public SemesterPanel(StudentAppGUI app) {
        setLayout(new BorderLayout());

        DefaultListModel<String> model = new DefaultListModel<>();
        model.addElement("2025년 1학기");
        model.addElement("2024년 겨울학기");
        model.addElement("2024년 2학기");

        JList<String> semesterList = new JList<>(model);
        semesterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        semesterList.addListSelectionListener(e -> {
            String selected = semesterList.getSelectedValue();
            JOptionPane.showMessageDialog(this, selected + " 시간표로 이동합니다.");
        });
        
        JButton backButton = new JButton("←");
        backButton.addActionListener(e -> app.showPanel("main"));
        add(backButton, BorderLayout.SOUTH);
        add(new JScrollPane(semesterList), BorderLayout.CENTER);
    }
}
