package gui;

import javax.swing.*;
import java.awt.*;

public class CreditCalculatorPanel extends JPanel {
    public CreditCalculatorPanel(StudentAppGUI app) {
        setLayout(new BorderLayout());

        //TODO : 하드코딩. 데이터 불러와서 처리.
        JLabel gpaLabel = new JLabel("전체 평점: x.xx / 4.3");
        JLabel creditLabel = new JLabel("취득 학점: xx / 150");
        JTextArea courseArea = new JTextArea("수강 과목 목록\n(추후 연동)");
        courseArea.setEditable(false);

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.add(gpaLabel);
        infoPanel.add(creditLabel);

        JButton backButton = new JButton("←");
        backButton.addActionListener(e -> app.showPanel("main"));

        add(infoPanel, BorderLayout.NORTH);
        add(new JScrollPane(courseArea), BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);
    }
}
