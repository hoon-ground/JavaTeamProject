package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class UserInfoPanel extends JPanel {
    private JTextField nameField;
    private JTextField studentIdField;
    private JButton saveButton;
    private StudentAppGUI parent;

    public UserInfoPanel(StudentAppGUI parent) {
        setLayout(new GridLayout(3, 2));

        JLabel nameLabel = new JLabel("이름:");
        nameField = new JTextField();
        JLabel studentIdLabel = new JLabel("학번:");
        studentIdField = new JTextField();
        saveButton = new JButton("저장");

        add(nameLabel);
        add(nameField);
        add(studentIdLabel);
        add(studentIdField);
        add(new JLabel()); // 빈 칸
        add(saveButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveUserInfo();
            }
        });
    }

    private void saveUserInfo() {
        String name = nameField.getText();
        String studentId = studentIdField.getText();

        JSONObject root = new JSONObject();
        root.put("name", name);
        root.put("studentId", studentId);

        // 기존 시간표 배열 예시 (실제로는 다른 클래스에서 받아오게 수정 가능)
        JSONArray courses = new JSONArray();

        // 테스트용 더미 데이터 추가
        JSONObject sampleCourse = new JSONObject();
        sampleCourse.put("name", "자료구조");
        sampleCourse.put("professor", "이교수");
        sampleCourse.put("location", "IT관 101호");
        sampleCourse.put("day", "월");
        sampleCourse.put("startHour", 9);
        sampleCourse.put("endHour", 11);
        courses.put(sampleCourse);

        root.put("courses", courses);

        try (FileWriter file = new FileWriter("UserData.json")) {
            file.write(root.toString(2));
            file.flush();
            JOptionPane.showMessageDialog(this, "정보가 저장되었습니다.");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "파일 저장 오류: " + ex.getMessage());
        }
    }
}
