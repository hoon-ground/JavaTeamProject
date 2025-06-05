package gui;

import model.Course;
import model.ExcelCourseLoader;
import model.Timetable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class TimetableGUI extends JFrame {
	private Timetable timetable = new Timetable();
	private List<Course> allCourses;

	private JComboBox<Course> courseComboBox;
	private JTextArea outputArea = new JTextArea(15, 45);

	public TimetableGUI() {
		setTitle("📚 에브리타임 스타일 시간표");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		// 과목 불러오기 (전공 + 교양)
		allCourses = new ArrayList<>();
		allCourses.addAll(ExcelCourseLoader.loadCourses("C:/Users/HSK/Documents/전공.xlsx"));
		allCourses.addAll(ExcelCourseLoader.loadCourses("C:/Users/HSK/Documents/교양.xlsx"));

		// 상단: 과목 선택
		JPanel topPanel = new JPanel(new FlowLayout());
		courseComboBox = new JComboBox<>(allCourses.toArray(new Course[0]));
		JButton addButton = new JButton("➕ 수업 추가");

		topPanel.add(new JLabel("과목 선택:"));
		topPanel.add(courseComboBox);
		topPanel.add(addButton);

		// 중앙: 시간표 출력
		outputArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(outputArea);

		add(topPanel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);

		// 버튼 동작
		addButton.addActionListener(e -> {
			Course selected = (Course) courseComboBox.getSelectedItem();
			if (selected == null) return;

			if (timetable.addCourse(selected)) {
				outputArea.setText("✅ 수업 추가 완료!\n\n");
			} else {
				outputArea.setText("❌ 시간이 겹치는 수업입니다!\n\n");
			}

			outputArea.append(timetable.toString());
		});

		pack();
		setLocationRelativeTo(null); // 화면 중앙 정렬
		setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(TimetableGUI::new);
	}
}