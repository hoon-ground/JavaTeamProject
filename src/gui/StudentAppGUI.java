package gui;

import javax.swing.*;
import java.awt.*;

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
