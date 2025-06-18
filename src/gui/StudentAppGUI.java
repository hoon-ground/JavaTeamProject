package gui;

import javax.swing.*;
import org.json.JSONObject;
import model.Timetable;
import java.awt.*;

public class StudentAppGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JSONObject currentUserJson;
    private Timetable currentTimetable;
    
    //초기화. 이후에 값 받아오면 변경.
    private String currentStudentId = "2020114744";
    private String currentStudentName = "김지훈";
    private String currentSemester = "2025년 1학기";
    
    public StudentAppGUI() {
        setTitle("KNU 시간표");
        setSize(1000, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new StartPanel(this), "start");
        mainPanel.add(new UserInfoPanel(this), "user");
        mainPanel.add(new MainPanel(this), "main");
        mainPanel.add(new CreditCalculatorPanel(this), "credit");
        mainPanel.add(new GraduationRequirementPanel(this), "grad");

        add(mainPanel);
        cardLayout.show(mainPanel, "start");
        setVisible(true);
    }

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

    public void showPanel(String name) {
        cardLayout.show(mainPanel, name);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentAppGUI::new);
    }
}
