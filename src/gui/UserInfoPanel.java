// UserInfoPanel.java
package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import model.Course;
import model.Student;
import model.Timetable;
import model.UserSession;
import util.JsonUtil;

public class UserInfoPanel extends JPanel {
    private JTextField nameField;
    private JTextField studentIdField;
    private JButton saveButton;
    private StudentAppGUI parent;
    
    public UserInfoPanel(StudentAppGUI parent) {
        this.parent = parent;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel titleLabel = new JLabel("정보 입력");
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(titleLabel, gbc);

        JLabel nameLabel = new JLabel("이름:");
        nameField = new JTextField();  // 이름 입력 받기
        nameLabel.setFont(new Font("Poppins", Font.BOLD, 14));
        nameField.setFont(new Font("Poppins", Font.PLAIN, 14));
        nameField.setPreferredSize(new Dimension(200, 30));
        nameField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(nameLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(nameField, gbc);

        JLabel studentIdLabel = new JLabel("학번:");
        studentIdField = new JTextField();  // 학번 입력 받기
        studentIdLabel.setFont(new Font("Poppins", Font.BOLD, 14));
        studentIdField.setFont(new Font("Poppins", Font.PLAIN, 14));
        studentIdField.setPreferredSize(new Dimension(200, 30));
        studentIdField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(studentIdLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(studentIdField, gbc);
        
        saveButton = new JButton("로그인");
        saveButton.setFont(new Font("Poppins", Font.BOLD, 14));
        saveButton.setBackground(Color.WHITE);
        saveButton.setForeground(Color.BLACK);
        saveButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        saveButton.setPreferredSize(new Dimension(120, 40));
        saveButton.setFocusPainted(false);
        gbc.gridwidth = 2; // 버튼이 두 칸을 차지하도록 설정
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(saveButton, gbc);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveOrLoadUserInfo();
            }
        });
        
        nameField.setFont(new Font("Poppins", Font.BOLD, 14));
        studentIdField.setFont(new Font("Poppins", Font.BOLD, 14));
        saveButton.setFont(new Font("Poppins", Font.BOLD, 14));
        saveButton.setBackground(Color.WHITE);
        saveButton.setForeground(Color.BLACK);
        saveButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        saveButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        setBackground(Color.WHITE); 
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); 
        
    }
    private void saveOrLoadUserInfo() {
        String name = nameField.getText().trim();
        String studentId = studentIdField.getText().trim();

        File file = new File("src/data/timetable.json");
        JSONObject root;
        JSONArray users;

        // 파일 읽기 또는 새로 생성
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder jsonBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }

                root = new JSONObject(jsonBuilder.toString());
                users = root.optJSONArray("users");
                if (users == null) users = new JSONArray();

            } catch (Exception e) {
                e.printStackTrace();
                root = new JSONObject();
                users = new JSONArray();
            }
        } else {
            root = new JSONObject();
            users = new JSONArray();
        }

        // 동일 사용자 찾기
        JSONObject currentUser = null;
        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if (studentId.equals(user.optString("studentId")) &&
                name.equals(user.optString("name"))) {
                currentUser = user;
                break;
            }
        }

        // 없으면 새로 생성
        if (currentUser == null) {
            currentUser = new JSONObject();
            currentUser.put("studentId", studentId);
            currentUser.put("name", name);
            currentUser.put("selectedSemester", "2025년 1학기"); // 기본 학기
            currentUser.put("timetables", new JSONObject());
            users.put(currentUser);
        }

        // ✅ 세션에 현재 사용자 저장
        UserSession.setStudentId(studentId);
        UserSession.setName(name);
        UserSession.setSelectedSemester(currentUser.optString("selectedSemester", "2025년 1학기"));

        root.put("users", users);

        try (FileWriter fw = new FileWriter(file)) {
            fw.write(root.toString(2));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 👇 시간표 객체 연동
        parent.setCurrentUserJson(currentUser);
        Timetable timetable = new Timetable();
        timetable.setOwner(new Student(name, List.of(), 0));
        parent.setCurrentTimetable(timetable);

        parent.showPanel("main");
    }

}
