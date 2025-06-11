package gui;

import javax.swing.*;
import java.awt.*;
import util.JsonUtil;
import java.util.List;
import model.Course;
import model.Timetable;
import model.UserSession;

public class MainPanel extends JPanel {
	private TimetablePanel timetablePanel;
	
    public MainPanel(StudentAppGUI app) {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        JButton selectSemesterBtn = new JButton("학기 선택");
        topPanel.add(selectSemesterBtn);

        //좌측 상단에 학기 표기
        JLabel semesterLabel = new JLabel("선택된 학기: 2025년 1학기");
        topPanel.setLayout(new BorderLayout());
        topPanel.add(semesterLabel, BorderLayout.WEST);
        topPanel.add(selectSemesterBtn, BorderLayout.EAST);

        selectSemesterBtn.addActionListener(e -> {
            String selected = showSemesterSelectionDialog();
            if (selected != null && !selected.isEmpty()) {
                semesterLabel.setText("선택된 학기: " + selected);
            }
            app.showPanel("semester");
        });

        //JPanel coursePanel = new JPanel(new GridLayout(2, 2, 10, 10));
        //coursePanel.add(createCourseButton("과목1"));
        //coursePanel.add(createCourseButton("과목2"));
        //coursePanel.add(createCourseButton("과목3"));

        JPanel bottomPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        JButton creditBtn = new JButton("학점계산기");
        JButton gradCheckBtn = new JButton("졸업요건 확인");
        JButton backBtn = new JButton("←");
        
        backBtn.addActionListener(e -> app.showPanel("user"));
        selectSemesterBtn.addActionListener(e -> app.showPanel("semester"));
        creditBtn.addActionListener(e -> app.showPanel("credit"));
        gradCheckBtn.addActionListener(e -> app.showPanel("grad"));
        
        bottomPanel.add(creditBtn);
        bottomPanel.add(gradCheckBtn);
        bottomPanel.add(backBtn);

        add(topPanel, BorderLayout.NORTH);
        //add(coursePanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        timetablePanel = new TimetablePanel();
        add(timetablePanel, BorderLayout.CENTER);
        
        //저장 버튼
        JButton saveBtn = new JButton("시간표 저장");
        saveBtn.addActionListener(e -> {
            Timetable t = timetablePanel.getTimetable();
                JsonUtil.saveUserTimetable(
                UserSession.getStudentId(),
                UserSession.getName(),
                UserSession.getSelectedSemester(),
                t.getCourses()
            );
        });

        //불러오기 버튼
        JButton loadBtn = new JButton("시간표 불러오기");
        loadBtn.addActionListener(e -> {
            List<Course> courses = JsonUtil.loadUserTimetable(
                UserSession.getStudentId(),
                UserSession.getSelectedSemester()
            );

            if (!courses.isEmpty()) {
                Timetable t = new Timetable();
                t.setCourses(courses);
                timetablePanel.setTimetable(t);
                JOptionPane.showMessageDialog(this, "불러오기 성공!");
            } else {
                JOptionPane.showMessageDialog(this, "불러올 데이터가 없습니다.");
            }
        });



        bottomPanel.add(saveBtn);
        bottomPanel.add(loadBtn);
    }
    
    private JButton createCourseButton(String courseName) {
        JButton btn = new JButton(courseName);
        btn.addActionListener(e -> showCourseDetailDialog(courseName));
        return btn;
    }

    private String showSemesterSelectionDialog() {
        String[] options = { "2024년 1학기", "2024년 2학기", "2025년 1학기" };
        return (String) JOptionPane.showInputDialog(
                this,
                "학기를 선택하세요:",
                "학기 선택",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);
    }

    
    private void showCourseDetailDialog(String courseName) {
    	
        JTextArea detailArea = new JTextArea("과목명: " + courseName +
                "\n- 교수: 김지훈" +
                "\n- 과목코드: KNU123" +
                "\n- 시간: 월 1A, 1B" +
                "\n- 장소: IT5호관 B101");
        detailArea.setEditable(false);

        JButton deleteButton = new JButton("삭제");
        deleteButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, courseName + " 과목이 삭제되었습니다.");
            //TODO : 일단 예시로 하드코딩해놨는데 나중에 데이터 받아오는 로직 구현하면 될 것 같습니다.
            //TODO : 시간표/수강 목록에서 실제 삭제 로직 구현
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(detailArea), BorderLayout.CENTER);
        panel.add(deleteButton, BorderLayout.SOUTH);

        JDialog dialog = new JDialog();
        dialog.setTitle(courseName + " 정보");
        dialog.setSize(300, 250);
        dialog.setLocationRelativeTo(this);
        dialog.add(panel);
        dialog.setVisible(true);
    }
}