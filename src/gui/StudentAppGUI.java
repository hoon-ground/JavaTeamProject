package gui;

import javax.swing.*;

import org.json.JSONObject;

import model.Timetable;

import java.awt.*;
//TODO : 일단 기본 UI로 구성을 해놨는데 로직처리 다 해놓고 나중에 디자인처리하면 될 것 같습니다.

public class StudentAppGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JSONObject currentUserJson;
    private Timetable currentTimetable;
    private String currentStudentId = "20241234";       // 로그인 후 실제 값으로 설정
    private String currentStudentName = "황상균";
    private String currentSemester = "2025년 1학기";
    
    public StudentAppGUI() {
        setTitle("KNU 학생관리앱");
        setSize(1000, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new StartPanel(this), "start");
        mainPanel.add(new UserInfoPanel(this), "user");
        mainPanel.add(new MainPanel(this), "main");
        mainPanel.add(new AddCoursePanel(this), "add");
        mainPanel.add(new SemesterPanel(this), "semester");
        mainPanel.add(new CreditCalculatorPanel(this), "credit");
        mainPanel.add(new GraduationRequirementPanel(this), "grad");
        
        
        
        add(mainPanel);
        cardLayout.show(mainPanel, "start");
        setVisible(true);
    }

    // < -- UI랑 DB 연동 -->    
    public void setCurrentUserJson(JSONObject userJson) {
        this.currentUserJson = userJson;
    }

    public JSONObject getCurrentUserJson() {
        return currentUserJson;
    }

    public void setCurrentTimetable(Timetable t) {
        this.currentTimetable = t;
    }

    public Timetable getCurrentTimetable() {
        return currentTimetable;
    }
    // < ------ >

    public void showPanel(String name) {
        cardLayout.show(mainPanel, name);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentAppGUI::new);
    }
}
