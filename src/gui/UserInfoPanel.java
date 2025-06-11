// UserInfoPanel.java
package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class UserInfoPanel extends JPanel {
    private JTextField nameField;
    private JTextField studentIdField;
    private JButton saveButton;
    private StudentAppGUI parent;

    public UserInfoPanel(StudentAppGUI parent) {
        this.parent = parent;
        setLayout(new GridLayout(3, 2));

        JLabel nameLabel = new JLabel("이름:");
        nameField = new JTextField();  // 이름 입력 받기
        JLabel studentIdLabel = new JLabel("학번:");
        studentIdField = new JTextField();  // 학번 입력 받기
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
                handleUserInfo();
            }
        });
    }

    private void handleUserInfo() {
        String name = nameField.getText().trim();  // 이름 입력값
        String studentId = studentIdField.getText().trim();  // 학번 입력값

        // 입력값이 비어 있지 않은지 확인
        if (name.isEmpty() || studentId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "이름과 학번을 입력해주세요.");
            return;
        }

        // timetable.json에서 학번과 이름이 있는지 확인
        JSONObject root = loadUserInfoFromFile(studentId);  // studentId를 전달하여 해당 사용자 찾기
        if (root != null && root.has("studentId") && root.getString("studentId").equals(studentId)) {
            // 학번과 이름이 존재하면 해당 시간표 불러오기
            JOptionPane.showMessageDialog(this, "시간표를 불러옵니다.");
            parent.showPanel("main");
        } else {
            // 새로운 학번/이름이면 빈 시간표로 설정하고 저장
            JOptionPane.showMessageDialog(this, "새로운 사용자입니다. 시간표를 생성합니다.");
            saveUserInfo(name, studentId);
            parent.showPanel("main");
        }
    }

    // studentId를 매개변수로 받음
    private JSONObject loadUserInfoFromFile(String studentId) {
        try (FileReader reader = new FileReader("src/data/timetable.json")) {
            StringBuilder content = new StringBuilder();
            int i;
            while ((i = reader.read()) != -1) {
                content.append((char) i);
            }

            // 파일 내용이 비어 있으면 빈 JSONArray 반환
            String contentStr = content.toString().trim();
            if (contentStr.isEmpty()) {
                return new JSONObject();  // 비어 있을 경우 빈 JSON 반환
            }

            // JSONArray로 파싱
            JSONArray usersArray = new JSONArray(contentStr);
            for (int j = 0; j < usersArray.length(); j++) {
                JSONObject user = usersArray.getJSONObject(j);
                if (user.getString("studentId").equals(studentId)) {
                    return user;  // 해당 학번에 맞는 사용자 정보를 반환
                }
            }
            return null;  // 해당 학번이 없으면 null 반환
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONObject();  // 파일 읽기 실패 시 빈 객체 반환
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject();  // 파싱 실패 시 빈 객체 반환
        }
    }

    private void saveUserInfo(String name, String studentId) {
        // 기존 timetable.json 파일을 읽어 기존 데이터를 유지하고 새로운 정보를 추가
        JSONArray usersArray = loadUsersArrayFromFile();  // 기존 사용자 배열 읽기
        JSONObject newUser = new JSONObject();
        newUser.put("name", name);
        newUser.put("studentId", studentId);

        // 과목 리스트는 빈 배열로 시작
        JSONArray courses = new JSONArray();
        newUser.put("courses", courses);

        // 새로운 사용자 정보 배열에 추가
        usersArray.put(newUser);

        try (FileWriter file = new FileWriter("src/data/timetable.json")) {
            file.write(usersArray.toString(2));  // JSON 배열 포맷으로 저장
            file.flush();
            JOptionPane.showMessageDialog(this, "정보가 저장되었습니다.");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "파일 저장 오류: " + ex.getMessage());
        }
    }

    private JSONArray loadUsersArrayFromFile() {
        try (FileReader reader = new FileReader("src/data/timetable.json")) {
            StringBuilder content = new StringBuilder();
            int i;
            while ((i = reader.read()) != -1) {
                content.append((char) i);
            }

            // 파일 내용이 비어 있으면 빈 JSONArray 반환
            String contentStr = content.toString().trim();
            if (contentStr.isEmpty()) {
                return new JSONArray();  // 비어 있을 경우 빈 JSON 배열 반환
            }

            // JSONArray로 파싱
            return new JSONArray(contentStr);
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray();  // 파일 읽기 실패 시 빈 배열 반환
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();  // 파싱 실패 시 빈 배열 반환
        }
    }
}
