package gui;

import javax.swing.*;
import java.awt.*;
//TODO : 일단 기본 UI로 구성을 해놨는데 로직처리 다 해놓고 나중에 디자인처리하면 될 것 같습니다.
public class StudentAppGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public StudentAppGUI() {
        setTitle("KNU 학생관리앱");
        setSize(450, 400);
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

    public void showPanel(String name) {
        cardLayout.show(mainPanel, name);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentAppGUI::new);
    }
}
