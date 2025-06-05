package gui;

import model.Student;
import model.GraduationRequirement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GraduationUI extends JFrame {
    private JLabel statusLabel;
    private JTextArea statusArea;
    private JLabel englishScoreLabel;
    private int englishScore = 0;
    private Student student;

    public GraduationUI() {
        setTitle("졸업 요건 확인 시스템");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 상단 상태 표시 영역
        statusLabel = new JLabel("졸업 요건");
        statusArea = new JTextArea(6, 40);
        statusArea.setEditable(false);

        // dummyCourses는 향후 실제 시간표 데이터와 연동되도록 수정 필요 (현재는 과목 없음)
        ArrayList<model.Course> dummyCourses = new ArrayList<>(); 

        // GraduationRequirement는 로그인 시 소속 학과에 따라 동적으로 불러오도록 수정 필요
        GraduationRequirement req = new GraduationRequirement(140, 53, 24, 700);

        // 로그인한 사용자 데이터 기반으로 생성되도록 수정 필요
        student = new Student("컴퓨터공학과", dummyCourses, englishScore);
        updateStatus(req);

        // 공인 영어 점수 입력 ui
        JPanel inputPanel = new JPanel();
        englishScoreLabel = new JLabel("공인 영어 점수: 0점");

        JButton inputButton = new JButton("입력");
        inputButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog("공인 영어 점수를 입력하세요 (예: 750)");
                if (input != null && !input.isEmpty()) {
                    try {
                        englishScore = Integer.parseInt(input);
                        englishScoreLabel.setText("공인 영어 점수: " + englishScore + "점");
                        student = new Student("컴퓨터공학과", dummyCourses, englishScore);
                        updateStatus(req);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "숫자를 입력해주세요.");
                    }
                }
            }
        });

        inputPanel.add(englishScoreLabel);
        inputPanel.add(inputButton);

        add(statusLabel, BorderLayout.NORTH);
        add(new JScrollPane(statusArea), BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void updateStatus(GraduationRequirement req) {
        StringBuilder sb = new StringBuilder();
        sb.append("졸업 가능 여부: ").append(student.isGraduationPossible(req) ? "✅ 가능" : "❌ 불가능").append("\n\n");

        for (String status : student.getGraduationStatusDetails(req)) {
            sb.append(status).append("\n");
        }

        statusArea.setText(sb.toString());
    }
}
