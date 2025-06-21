package gui;

import model.Exportable;  // 인터페이스

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.AbstractCourse;
import model.Course;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CreditCalculatorPanel extends JPanel {
    private static final String[] GRADES = {"A+", "A0", "A-", "B+", "B0", "B-", "C+", "C0", "C-", "D+", "D0", "D-", "F"};

    public CreditCalculatorPanel(StudentAppGUI app) {
    	
        setLayout(new BorderLayout());
        String[] columns = {"과목명", "학점", "성적", "전공(O/X)"};
        DefaultTableModel model = new DefaultTableModel(columns, 50);
        JTable table = new JTable(model);
        table.setFont(new Font("Poppins", Font.PLAIN, 14));
        table.setRowHeight(30);

        for (int i = 0; i < 50; i++) {
            model.setValueAt("과목" + (i + 1), i, 0);
            model.setValueAt(0, i, 1);
            model.setValueAt("A+", i, 2);
            model.setValueAt("X", i, 3);
        }

        table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(new JTextField()));

        JComboBox<String> gradeComboBox = new JComboBox<>(GRADES);
        gradeComboBox.setFont(new Font("Poppins", Font.BOLD, 14));
        gradeComboBox.setBackground(Color.WHITE);
        gradeComboBox.setForeground(Color.BLACK);
        gradeComboBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JComboBox<String> majorComboBox = new JComboBox<>(new String[]{"O", "X"});
        majorComboBox.setFont(new Font("Poppins", Font.BOLD, 14));
        majorComboBox.setBackground(Color.WHITE);
        majorComboBox.setForeground(Color.BLACK);
        majorComboBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(gradeComboBox)); // 성적 콤보박스 편집기
        table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(majorComboBox)); // 전공 O/X 편집기

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

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

        JButton submitBtn = new JButton("제출");
        submitBtn.setFont(new Font("Poppins", Font.BOLD, 14));
        submitBtn.setBackground(Color.WHITE);
        submitBtn.setForeground(Color.BLACK);
        submitBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        
        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<AbstractCourse> allCourses = new ArrayList<>();
                double totalGradePoints = 0;
                int totalCredits = 0;
                double totalMajorGradePoints = 0;
                int totalMajorCredits = 0;
                double totalGenGradePoints = 0;
                int totalGenCredits = 0;

                for (int i = 0; i < 50; i++) {
                    Object gradeObj = model.getValueAt(i, 2);
                    Object creditObj = model.getValueAt(i, 1);
                    Object majorObj = model.getValueAt(i, 3);
                    Object nameObj = model.getValueAt(i, 0);

                    String grade = gradeObj != null ? gradeObj.toString().trim() : "";
                    String creditStr = creditObj != null ? creditObj.toString().trim() : "0";
                    String major = majorObj != null ? majorObj.toString().trim() : "X";
                    String name = nameObj != null ? nameObj.toString().trim() : "과목";

                    int credits = 0;
                    try {
                        credits = Integer.parseInt(creditStr);
                    } catch (NumberFormatException ex) {
                        credits = 0;
                    }

                    // 전공이면 division = "전공", 아니면 "교양"
                    String division = major.equalsIgnoreCase("O") ? "전공" : "교양";

                    // AbstractCourse 생성
                    AbstractCourse course = new Course("TMP", name, "prof", "room",
                            new ArrayList<>(), division, 1, credits);
                    allCourses.add(course);

                    double gradePoint = getGradePoint(grade);
                    totalGradePoints += credits * gradePoint;
                    totalCredits += credits;

                    if (course.getCourseType().equals("전공")) {
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

        JButton backBtn = new JButton("<-");
        backBtn.setFont(new Font("Poppins", Font.BOLD, 14));
        backBtn.setBackground(Color.WHITE);
        backBtn.setForeground(Color.BLACK);
        backBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        backBtn.addActionListener(e -> app.showPanel("main"));

        JButton exportBtn = new JButton("요약 작게 보기");
        exportBtn.setFont(new Font("Poppins", Font.BOLD, 14));
        exportBtn.setBackground(Color.WHITE);
        exportBtn.setForeground(Color.BLACK);
        exportBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        exportBtn.addActionListener(e -> {
            List<Exportable> exportList = new ArrayList<>();

            for (int i = 0; i < 50; i++) {
                Object nameObj = model.getValueAt(i, 0);
                Object creditObj = model.getValueAt(i, 1);
                Object majorObj = model.getValueAt(i, 3);

                if (nameObj == null || creditObj == null || majorObj == null) continue;

                String name = nameObj.toString().trim();
                String creditStr = creditObj.toString().trim();
                String major = majorObj.toString().trim();
                int credits;

                try {
                    credits = Integer.parseInt(creditStr);
                } catch (NumberFormatException ex) {
                    credits = 0;
                }

                String division = major.equalsIgnoreCase("O") ? "전공" : "교양";

                Course course = new Course("TEMP", name, "PROF", "ROOM",
                        new ArrayList<>(), division, 1, credits);

                exportList.add(course);
            }

            StringBuilder sb = new StringBuilder();

            sb.append("<< 과목 정보 요약 >>\n\n");
            for (Exportable course : exportList) {
                String[] fields = course.toExportString().split(",");
                sb.append("과목명: ").append(fields[1])
                .append(" | 학점: ").append(fields[6])
                .append(" | 구분: ").append(fields[4])
                .append("\n");
            }

            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Poppins", Font.PLAIN, 14));
            JScrollPane csvscrollPane = new JScrollPane(textArea);
            csvscrollPane.setPreferredSize(new Dimension(450, 300));

            JOptionPane.showMessageDialog(null, csvscrollPane, "CSE 과목 정보 요약", JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel submitPanel = new JPanel();
        submitPanel.add(submitBtn);
        submitPanel.add(backBtn);
        submitPanel.add(exportBtn);
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
