package gui;

import model.Student;
import model.GraduationRequirement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GraduationUI extends JFrame {
    private final Student student;
    private final GraduationRequirement req;
    private JTextArea statusArea;
    private JLabel englishScoreLabel;
    private int englishScore;

    public void refresh() {
        updateStatus();
    }

    public GraduationUI(Student student, GraduationRequirement req) {
        this.student = student;
        this.req = req;
        this.englishScore = student.getEnglishScore();
        initUI();
    }

    private void initUI() {
        setTitle("졸업 요건 확인");
        setSize(500, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 상단 상태 표시 영역
        statusArea = new JTextArea(6, 40);
        statusArea.setEditable(false);
        add(new JScrollPane(statusArea), BorderLayout.CENTER);

        // 영어 점수 입력 영역
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        englishScoreLabel = new JLabel("공인 영어 점수: " + englishScore + "점");
        JButton inputButton = new JButton("영어 점수 입력");
        inputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog(
                        GraduationUI.this,
                        "공인 영어 점수를 입력하세요 (예: 750)",
                        englishScore
                );
                if (input != null && !input.isEmpty()) {
                    try {
                        englishScore = Integer.parseInt(input.trim());
                        englishScoreLabel.setText("공인 영어 점수: " + englishScore + "점");
                        updateStatus();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(
                                GraduationUI.this,
                                "유효한 숫자를 입력해주세요.",
                                "입력 오류",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        });

        inputPanel.add(englishScoreLabel);
        inputPanel.add(inputButton);
        add(inputPanel, BorderLayout.SOUTH);

        // 초기 상태 업데이트
        updateStatus();

        setVisible(true);
    }

    private void updateStatus() {
        boolean totalOk = student.getTotalCredits() >= req.getRequiredTotalCredits();
        boolean majorOk = student.getMajorCredits() >= req.getRequiredMajorCredits();
        boolean generalOk = student.getGeneralCredits() >= req.getRequiredGeneralCredits();
        boolean englishOk = englishScore >= req.getRequiredEnglishScore();

        StringBuilder sb = new StringBuilder();
        sb.append("졸업 가능 여부: ")
                .append((totalOk && majorOk && generalOk && englishOk) ? "✅ 가능" : "❌ 불가능")
                .append("\n\n");

        sb.append(totalOk ? "✅ " : "❌ ")
                .append("총 이수학점: ")
                .append(student.getTotalCredits())
                .append(" / ")
                .append(req.getRequiredTotalCredits())
                .append("\n");

        sb.append(majorOk ? "✅ " : "❌ ")
                .append("전공 이수학점: ")
                .append(student.getMajorCredits())
                .append(" / ")
                .append(req.getRequiredMajorCredits())
                .append("\n");

        sb.append(generalOk ? "✅ " : "❌ ")
                .append("교양 이수학점: ")
                .append(student.getGeneralCredits())
                .append(" / ")
                .append(req.getRequiredGeneralCredits())
                .append("\n");

        sb.append(englishOk ? "✅ " : "❌ ")
                .append("공인 영어 점수: ")
                .append(englishScore)
                .append(" / ")
                .append(req.getRequiredEnglishScore())
                .append("\n");

        statusArea.setText(sb.toString());
    }
}
