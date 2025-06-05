package gui;

import model.Course;
import model.ExcelCourseLoader;
import model.Timetable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TimetableGUIWithExcel extends JFrame {
  private final Timetable timetable = new Timetable();
  private final List<Course> allCourses;

  private final JComboBox<Course> courseComboBox;
  private final JTextArea outputArea = new JTextArea(15, 45);

  public TimetableGUIWithExcel() {
    setTitle("📚 시간표 관리 - 엑셀 기반");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    // 강의 데이터 불러오기
    allCourses = new ArrayList<>();
    allCourses.addAll(ExcelCourseLoader.loadCourses("C:/Users/HSK/OneDrive/문서/EveryTime_TeamP/전공.xlsx"));
    allCourses.addAll(ExcelCourseLoader.loadCourses("C:/Users/HSK/OneDrive/문서/EveryTime_TeamP/교양.xlsx"));


    // 상단 입력 UI
    JPanel topPanel = new JPanel(new FlowLayout());
    courseComboBox = new JComboBox<>(allCourses.toArray(new Course[0]));
    JButton addButton = new JButton("➕ 수업 추가");

    topPanel.add(new JLabel("과목 선택:"));
    topPanel.add(courseComboBox);
    topPanel.add(addButton);

    // 중앙 출력 영역
    outputArea.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(outputArea);

    add(topPanel, BorderLayout.NORTH);
    add(scrollPane, BorderLayout.CENTER);

    // 버튼 기능
    addButton.addActionListener(e -> {
      Course selected = (Course) courseComboBox.getSelectedItem();
      if (selected == null) return;

      if (timetable.addCourse(selected)) {
        outputArea.setText("✅ 수업 추가 완료!\n\n");
      } else {
        outputArea.setText("❌ 시간이 겹치는 과목입니다!\n\n");
      }
      outputArea.append(timetable.toString());
    });

    pack();
    setLocationRelativeTo(null); // 화면 중앙
    setVisible(true);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(TimetableGUIWithExcel::new);
  }
}