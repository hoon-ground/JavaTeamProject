package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GraduationRequirementPanel extends JPanel {
    private boolean isFirstVisit = true;
    private boolean passedEnglish = false;

    public GraduationRequirementPanel(StudentAppGUI app) {
        setLayout(new BorderLayout());
        renderFirstVisit(app);
    }

    private void renderFirstVisit(StudentAppGUI app) {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));

        JLabel label = new JLabel("TOEIC 점수 입력");
        JTextField scoreField = new JTextField();
        JButton submitBtn = new JButton("제출");
        JButton noExamBtn = new JButton("응시하지 않음");

        submitBtn.addActionListener(e -> {
            try {
                int score = Integer.parseInt(scoreField.getText());
                passedEnglish = score >= 700;
            } catch (NumberFormatException ignored) {
                passedEnglish = false;
            }
            isFirstVisit = false;
            removeAll();
            renderResult(app);
            revalidate();
            repaint();
        });

        noExamBtn.addActionListener(e -> {
            passedEnglish = false;
            isFirstVisit = false;
            removeAll();
            renderResult(app);
            revalidate();
            repaint();
        });

        panel.add(label);
        panel.add(scoreField);
        panel.add(submitBtn);
        panel.add(noExamBtn);

        JButton backButton = new JButton("←");
        backButton.addActionListener(e -> app.showPanel("main"));

        add(panel, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);
    }

    private void renderResult(StudentAppGUI app) {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        
        //TODO : 하드코딩. 데이터 불러와서 처리.
        panel.add(new JLabel("총 이수학점:"));
        panel.add(new JLabel("130 / 140"));

        panel.add(new JLabel("전공 이수학점:"));
        panel.add(new JLabel("51 / 53"));

        panel.add(new JLabel("교양 이수학점:"));
        panel.add(new JLabel("24 / 24"));

        panel.add(new JLabel("공인영어 성적:"));
        panel.add(new JLabel(passedEnglish ? "✅" : "❌"));

        JButton backButton = new JButton("←");
        backButton.addActionListener(e -> app.showPanel("main"));

        add(panel, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);
    }
}
