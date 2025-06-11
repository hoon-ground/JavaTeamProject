package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreditCalculatorPanel extends JPanel {
    private static final String[] GRADES = {"A+", "A0", "A-", "B+", "B0", "B-", "C+", "C0", "C-", "D+", "D0", "D-", "F"};
    private static final double[] GRADE_POINTS = {4.3, 4.0, 3.7, 3.3, 3.0, 2.7, 2.3, 2.0, 1.7, 1.3, 1.0, 0.7, 0.0};

    public CreditCalculatorPanel(StudentAppGUI app) {
        setLayout(new BorderLayout());

        String[] columns = {"과목명", "학점", "성적", "전공(O/X)"};  // 테이블 컬럼
        DefaultTableModel model = new DefaultTableModel(columns, 50);  // 테이블 모델 생성
        JTable table = new JTable(model);
        table.setFont(new Font("Poppins", Font.PLAIN, 14));
        table.setRowHeight(30);

        // 테이블 데이터 초기화
        for (int i = 0; i < 50; i++) {
            model.setValueAt("과목" + (i + 1), i, 0);
            model.setValueAt(0, i, 1); // 학점은 숫자로 초기화
            model.setValueAt("A+", i, 2); // 성적은 기본값 A+
            model.setValueAt("X", i, 3); // 전공 여부는 기본값 X
        }

        // 학점 입력을 위한 텍스트 필드로 셀 설정 (학점은 Integer)
        table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(new JTextField()));

        // 성적 드롭다운(JComboBox) 추가
        JComboBox<String> gradeComboBox = new JComboBox<>(GRADES);
        gradeComboBox.setFont(new Font("Poppins", Font.BOLD, 14));
        gradeComboBox.setBackground(Color.WHITE);
        gradeComboBox.setForeground(Color.BLACK);
        gradeComboBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        // 전공 O/X 드롭다운 추가
        JComboBox<String> majorComboBox = new JComboBox<>(new String[]{"O", "X"});
        majorComboBox.setFont(new Font("Poppins", Font.BOLD, 14));
        majorComboBox.setBackground(Color.WHITE);
        majorComboBox.setForeground(Color.BLACK);
        majorComboBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        // 셀 편집기 설정
        table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(gradeComboBox)); // 성적 콤보박스 편집기
        table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(majorComboBox)); // 전공 O/X 편집기

        // 테이블을 스크롤 가능하게 추가
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // GPA, 학점 합계 표시
        JPanel infoPanel = new JPanel(new GridLayout(4, 1));
        JLabel gpaSumLabel = new JLabel("평점 합계: 0.00");
        JLabel gpaAvgLabel = new JLabel("평점 평균: 0.00");
        JLabel majorGpaLabel = new JLabel("전공 평점: 0.00");
        JLabel genGpaLabel = new JLabel("교양 평점: 0.00");
        infoPanel.add(gpaSumLabel);
        infoPanel.add(gpaAvgLabel);
        infoPanel.add(majorGpaLabel);
        infoPanel.add(genGpaLabel);
        add(infoPanel, BorderLayout.NORTH);

        // 제출 버튼
        JButton submitBtn = new JButton("제출");
        submitBtn.setFont(new Font("Poppins", Font.BOLD, 14));
        submitBtn.setBackground(Color.WHITE);
        submitBtn.setForeground(Color.BLACK);
        submitBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double totalGradePoints = 0;
                int totalCredits = 0;
                double totalMajorGradePoints = 0;
                int totalMajorCredits = 0;
                double totalGenGradePoints = 0;
                int totalGenCredits = 0;

                // 모든 과목에 대해 계산
                for (int i = 0; i < 50; i++) {
                    Object gradeObj = model.getValueAt(i, 2);
                    Object creditObj = model.getValueAt(i, 1);
                    Object majorObj = model.getValueAt(i, 3);

                    String grade = gradeObj != null ? gradeObj.toString().trim() : "";
                    String creditStr = creditObj != null ? creditObj.toString().trim() : "0";
                    String major = majorObj != null ? majorObj.toString().trim() : "X";

                    int credits = 0;
                    try {
                        credits = Integer.parseInt(creditStr);
                    } catch (NumberFormatException ex) {
                        credits = 0;
                    }

                    double gradePoint = getGradePoint(grade);
                    totalGradePoints += credits * gradePoint;
                    totalCredits += credits;

                    if (major.equalsIgnoreCase("O")) {
                        totalMajorGradePoints += credits * gradePoint;
                        totalMajorCredits += credits;
                    } else {
                        totalGenGradePoints += credits * gradePoint;
                        totalGenCredits += credits;
                    }
                }

                double gpa = totalCredits == 0 ? 0 : totalGradePoints / totalCredits;
                double majorGpa = totalMajorCredits == 0 ? 0 : totalMajorGradePoints / totalMajorCredits;
                double genGpa = totalGenCredits == 0 ? 0 : totalGenGradePoints / totalGenCredits;

                gpaSumLabel.setText("평점 합계: " + String.format("%.2f", totalGradePoints));
                gpaAvgLabel.setText("평점 평균: " + String.format("%.2f", gpa));
                majorGpaLabel.setText("전공 평점: " + String.format("%.2f", majorGpa));
                genGpaLabel.setText("교양 평점: " + String.format("%.2f", genGpa));
            }
        });

        // 뒤로가기 버튼 추가
        JButton backBtn = new JButton("<-");
        backBtn.setFont(new Font("Poppins", Font.BOLD, 14));
        backBtn.setBackground(Color.WHITE);
        backBtn.setForeground(Color.BLACK);
        backBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        backBtn.addActionListener(e -> app.showPanel("main"));
        // 하단에 제출 버튼과 뒤로가기 버튼 추가
        JPanel submitPanel = new JPanel();
        submitPanel.add(submitBtn);
        submitPanel.add(backBtn);  // 뒤로가기 버튼 추가
        add(submitPanel, BorderLayout.SOUTH);
    }

    private double getGradePoint(String grade) {
        switch (grade) {
            case "A+": return 4.3;
            case "A0": return 4.0;
            case "A-": return 3.7;
            case "B+": return 3.3;
            case "B0": return 3.0;
            case "B-": return 2.7;
            case "C+": return 2.3;
            case "C0": return 2.0;
            case "C-": return 1.7;
            case "D+": return 1.3;
            case "D0": return 1.0;
            case "D-": return 0.7;
            case "F": return 0.0;
            default: return 0.0;
        }
    }
}
