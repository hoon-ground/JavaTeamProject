// 패키지 및 임포트
package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.table.DefaultTableModel;

import model.*;

public class TimetableGUIWithExcel {
    private static final String[] DAYS = {"", "월", "화", "수", "목", "금"};
    private static final String[] TIMES = {
        "", "오전 9시", "오전 10시", "오전 11시", "오후 12시", "오후 1시",
        "오후 2시", "오후 3시", "오후 4시", "오후 5시", "오후 6시"
    };

    private static final int ROWS = TIMES.length;
    private static final int COLS = DAYS.length;

    private static JComboBox<String> semesterComboBox;
    private static String currentSemester = "2025년 1학기";
    private static Map<String, Timetable> semesterTimetables = new HashMap<>();
    private static Timetable timetable;
    private static JPanel gridPanel;

    private static JLabel totalCreditLabel;
    private static JLabel majorCreditLabel;
    private static JLabel generalCreditLabel;
    private static GraduationUI gradUI;

    public static void main(String[] args) {
        timetable = new Timetable(new ArrayList<>());
        Student student = new Student("컴퓨터공학과", timetable.getCourses(), 0);
        timetable.setOwner(student);

        semesterTimetables.put(currentSemester, timetable);
        SwingUtilities.invokeLater(() -> createMainFrame());
    }

    private static void createMainFrame() {
        JFrame frame = new JFrame("에브리타임 스타일 시간표");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 700);

        gridPanel = new JPanel(new GridLayout(ROWS, COLS));
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(gridPanel, BorderLayout.CENTER);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(180, 700));

        JLabel label = new JLabel("학기 선택:");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] semesters = {"2025년 1학기", "2025년 2학기", "2026년 1학기"};
        semesterComboBox = new JComboBox<>(semesters);
        semesterComboBox.setMaximumSize(new Dimension(160, 25));
        semesterComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        semesterComboBox.addActionListener(e -> {
            currentSemester = (String) semesterComboBox.getSelectedItem();
            timetable = semesterTimetables.computeIfAbsent(currentSemester, k -> new Timetable(new ArrayList<>()));
            drawGrid();
        });

        totalCreditLabel = new JLabel("총 학점: 0학점");
        majorCreditLabel = new JLabel("전공 학점: 0학점");
        generalCreditLabel = new JLabel("교양 학점: 0학점");

        totalCreditLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        majorCreditLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        generalCreditLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(label);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(semesterComboBox);
        leftPanel.add(Box.createVerticalStrut(30));
        leftPanel.add(totalCreditLabel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(majorCreditLabel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(generalCreditLabel);
        leftPanel.add(Box.createVerticalGlue());

        mainPanel.add(leftPanel, BorderLayout.WEST);

        JButton addButton = new JButton("수업 목록에서 검색 및 추가");
        addButton.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        addButton.setBackground(Color.RED);
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> showCourseSelectionDialog());

        JButton gradCheckButton = new JButton("졸업 요건 확인");
        gradCheckButton.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        gradCheckButton.setBackground(Color.DARK_GRAY);
        gradCheckButton.setForeground(Color.WHITE);
        gradCheckButton.setFocusPainted(false);

        gradCheckButton.addActionListener(e -> {
            Student s = timetable.getOwner();
            GraduationRequirement req = new GraduationRequirement(140, 53, 24, s.getEnglishScore());
            if (gradUI == null || !gradUI.isDisplayable()) {
                gradUI = new GraduationUI(s, req);
            } else {
                gradUI.refresh();
            }
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(addButton);
        bottom.add(gradCheckButton);
        mainPanel.add(bottom, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);

        //drawGrid는 UI 구성 끝나고 나서 호출해야 함
        drawGrid();

        frame.setVisible(true);
    }


    private static void drawGrid() {
        gridPanel.removeAll();

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                JPanel cell = new JPanel(new BorderLayout());
                cell.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                cell.setBackground(Color.WHITE);

                if (r == 0 && c > 0) {
                    JLabel label = new JLabel(DAYS[c], SwingConstants.CENTER);
                    label.setFont(new Font("맑은 고딕", Font.BOLD, 13));
                    cell.add(label, BorderLayout.CENTER);
                } else if (c == 0 && r > 0) {
                    JLabel label = new JLabel(TIMES[r], SwingConstants.CENTER);
                    label.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
                    cell.add(label, BorderLayout.CENTER);
                } else if (r > 0 && c > 0) {
                    String day = DAYS[c];
                    int hour = 9 + (r - 1);

                    for (Course course : timetable.getCourses()) {
                        for (TimeSlot slot : course.getTimeSlots()) {
                            if (slot.getDay().equals(day) && slot.getStartHour() == hour) {
                                Color bgColor = new Color(240, 248, 255);

                                JPanel classPanel = new JPanel(new BorderLayout());
                                classPanel.setBackground(bgColor);
                                classPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

                                JTextPane textPane = new JTextPane();
                                textPane.setContentType("text/html");
                                textPane.setText(
                                    "<html><body style='margin:2px; font-family:맑은 고딕; font-size:11px;'>" +
                                    "<b>" + course.getName() + "</b><br>" +
                                    course.getProfessor() +
                                    "</body></html>"
                                );
                                textPane.setEditable(false);
                                textPane.setOpaque(false);
                                textPane.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));

                                JPanel textWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                                textWrapper.setOpaque(false);
                                textWrapper.add(textPane);

                                JButton deleteBtn = new JButton("X");
                                deleteBtn.setMargin(new Insets(0, 0, 0, 0));
                                deleteBtn.setFont(new Font("맑은 고딕", Font.BOLD, 10));
                                deleteBtn.setForeground(Color.RED);
                                deleteBtn.setBorder(BorderFactory.createEmptyBorder());
                                deleteBtn.setContentAreaFilled(false);
                                deleteBtn.setToolTipText("삭제");
                                deleteBtn.addActionListener(e -> {
                                    timetable.removeCourse(course);  // 수업 제거 + 학점 자동 갱신
                                    drawGrid();
                                });


                                JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
                                topPanel.setOpaque(false);
                                topPanel.add(deleteBtn);

                                classPanel.add(topPanel, BorderLayout.NORTH);
                                classPanel.add(textWrapper, BorderLayout.CENTER);

                                cell.add(classPanel, BorderLayout.CENTER);
                            }
                        }
                    }
                }

                gridPanel.add(cell);
            }
        }

        gridPanel.revalidate();
        gridPanel.repaint();

        Student owner = timetable.getOwner();
        if (owner != null) {
            owner.recalculate();  // 학점 재계산
        }

        updateCreditLabels();
        if (gradUI != null && gradUI.isVisible()) {
            gradUI.refresh();
        }
    }

    private static void updateCreditLabels() {
        Student s = timetable.getOwner();
        if (s == null) return;

        // ↓ 이 부분을 기존 for‐loop 대신 이렇게 바꿔 주세요 ↓
        totalCreditLabel.setText("총 학점: " + s.getTotalCredits() + "학점");
        majorCreditLabel.setText("전공 학점: " + s.getMajorCredits() + "학점");
        generalCreditLabel.setText("교양 학점: " + s.getGeneralCredits() + "학점");
    }


    private static void showCourseSelectionDialog() {
        List<Course> allCourses = new ArrayList<>();
        allCourses.addAll(ExcelCourseLoader.loadCourses("src/resources/교양.xlsx"));
        allCourses.addAll(ExcelCourseLoader.loadCourses("src/resources/전공.xlsx"));

        JFrame dialog = new JFrame("강의 목록 (검색 및 추가)");
        dialog.setSize(1000, 500);
        dialog.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        JTextField searchField = new JTextField();
        searchField.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        panel.add(searchField, BorderLayout.NORTH);

        String[] columnNames = {"학년", "구분", "교과목번호", "교과목명", "담당교수", "강의시간", "강의실", "학점"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        table.setRowHeight(24);
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        for (Course c : allCourses) {
            String timeStr = c.getTimeSlots().stream().map(TimeSlot::toString).collect(Collectors.joining(", "));
            model.addRow(new Object[]{
                c.getYear() + "학년",
                c.getDivision(),
                c.getSubjectCode(),
                c.getName(),
                c.getProfessor(),
                timeStr,
                c.getLocation(),
                c.getCredit()
            });
        }

        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String query = searchField.getText().trim();
                model.setRowCount(0);

                for (Course c : allCourses) {
                    if (c.getName().contains(query)) {
                        String timeStr = c.getTimeSlots().stream().map(TimeSlot::toString).collect(Collectors.joining(", "));
                        model.addRow(new Object[]{
                            c.getYear() + "학년",
                            c.getDivision(),
                            c.getSubjectCode(),
                            c.getName(),
                            c.getProfessor(),
                            timeStr,
                            c.getLocation(),
                            c.getCredit()
                        });
                    }
                }
            }
        });

        JButton addBtn = new JButton("선택한 수업 추가");
        addBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String name = (String) table.getValueAt(selectedRow, 3);
                String professor = (String) table.getValueAt(selectedRow, 4);
                for (Course c : allCourses) {
                    if (c.getName().equals(name) && c.getProfessor().equals(professor)) {
                        if (timetable.addCourse(c)) {
                            drawGrid();
                            dialog.dispose();
                        } else {
                            JOptionPane.showMessageDialog(dialog, "시간이 겹쳐 추가할 수 없습니다.", "중복 오류", JOptionPane.WARNING_MESSAGE);
                        }
                        break;
                    }
                }
            }
        });

        panel.add(addBtn, BorderLayout.SOUTH);
        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    public TimetableGUIWithExcel() {
        timetable = new Timetable(new ArrayList<>());

        Student student = new Student("컴퓨터공학과", timetable.getCourses(), 0);
        timetable.setOwner(student);

        semesterTimetables.put(currentSemester, timetable);
        createMainFrame();
    }

}
