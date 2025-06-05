package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import javax.swing.table.DefaultTableModel;
import model.Course;
import model.TimeSlot;
import model.ExcelCourseLoader;
import model.Timetable;

public class TimetableGUIWithExcel {
    private static final String[] DAYS = {"월", "화", "수", "목", "금"};
    private static final int START_HOUR = 9;
    private static final int END_HOUR = 18;
    private static final Color[] COLOR_POOL = {
        new Color(210, 240, 255), new Color(255, 230, 230), new Color(230, 255, 230),
        new Color(255, 255, 210), new Color(235, 210, 255), new Color(210, 255, 250)
    };

    private static Timetable timetable;
    private static JPanel timetablePanel;

    public static void main(String[] args) {
        timetable = new Timetable(new ArrayList<>());
        SwingUtilities.invokeLater(() -> createMainFrame());
    }

    private static void createMainFrame() {
        JFrame frame = new JFrame("에브리타임 시간표 보기");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);

        JPanel mainPanel = new JPanel(new BorderLayout());
        timetablePanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(timetablePanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JButton searchButton = new JButton("수업 목록에서 검색 및 추가");
        searchButton.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        searchButton.setBackground(Color.RED);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.addActionListener(e -> showCourseTableWithSearch());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(searchButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        drawTimetable();
    }

    private static void showCourseTableWithSearch() {
        List<Course> allCourses = new ArrayList<>();
        allCourses.addAll(ExcelCourseLoader.loadCourses("src/resources/교양.xlsx"));
        allCourses.addAll(ExcelCourseLoader.loadCourses("src/resources/전공.xlsx"));

        JFrame dialog = new JFrame("강의 목록 (검색 및 추가)");
        dialog.setSize(1000, 500);
        dialog.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField();
        panel.add(searchField, BorderLayout.NORTH);

        String[] columnNames = {"교과목명", "담당교수", "강의실", "시간정보"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // 초기 전체 course 목록 테이블에 채우기
        for (Course c : allCourses) {
            String timeStr = c.getTimeSlots().stream().map(TimeSlot::toString).collect(Collectors.joining(", "));
            model.addRow(new String[]{c.getName(), c.getProfessor(), c.getLocation(), timeStr});
        }

        JButton addButton = new JButton("선택한 수업 추가");
        addButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String selectedName = (String) table.getValueAt(row, 0);
                String selectedProf = (String) table.getValueAt(row, 1);
                for (Course c : allCourses) {
                    if (c.getName().equals(selectedName) && c.getProfessor().equals(selectedProf)) {
                        if (timetable.addCourse(c)) {
                            drawTimetable();
                            JOptionPane.showMessageDialog(dialog, "수업이 추가되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(dialog, "시간이 겹쳐 추가할 수 없습니다.", "추가 실패", JOptionPane.WARNING_MESSAGE);
                        }
                        break;
                    }
                }
            }
        });
        panel.add(addButton, BorderLayout.SOUTH);

        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String query = searchField.getText();
                model.setRowCount(0);
                for (Course c : allCourses) {
                    if (c.getName().contains(query)) {
                        String timeStr = c.getTimeSlots().stream().map(TimeSlot::toString).collect(Collectors.joining(", "));
                        model.addRow(new String[]{c.getName(), c.getProfessor(), c.getLocation(), timeStr});
                    }
                }
                if (query.isEmpty()) {
                    for (Course c : allCourses) {
                        String timeStr = c.getTimeSlots().stream().map(TimeSlot::toString).collect(Collectors.joining(", "));
                        model.addRow(new String[]{c.getName(), c.getProfessor(), c.getLocation(), timeStr});
                    }
                }
            }
        });

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    // drawTimetable 및 getDayIndex 등은 기존과 동일하게 유지


    private static void drawTimetable() {
        timetablePanel.removeAll();
        timetablePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        for (int i = 0; i < DAYS.length; i++) {
            JLabel label = new JLabel(DAYS[i], SwingConstants.CENTER);
            label.setOpaque(true);
            label.setBackground(Color.WHITE);
            label.setFont(new Font("맑은 고딕", Font.BOLD, 14));
            label.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
            gbc.gridx = i + 1;
            gbc.gridy = 0;
            timetablePanel.add(label, gbc);
        }

        for (int i = START_HOUR; i <= END_HOUR; i++) {
            JLabel label = new JLabel("오전 " + i + "시", SwingConstants.CENTER);
            if (i >= 12) label.setText("오후 " + (i > 12 ? i - 12 : 12) + "시");
            label.setOpaque(true);
            label.setBackground(Color.WHITE);
            label.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
            label.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
            gbc.gridx = 0;
            gbc.gridy = i - START_HOUR + 1;
            timetablePanel.add(label, gbc);
        }

        Map<String, Color> courseColorMap = new HashMap<>();
        int colorIndex = 0;

        for (Course course : new ArrayList<>(timetable.getCourses())) {
            if (!courseColorMap.containsKey(course.getName())) {
                courseColorMap.put(course.getName(), COLOR_POOL[colorIndex % COLOR_POOL.length]);
                colorIndex++;
            }
            Color bgColor = courseColorMap.get(course.getName());

            JPanel classPanel = new JPanel(new BorderLayout());
            classPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            classPanel.setBackground(bgColor);

            JTextArea textArea = new JTextArea(course.getName() + "\n" + course.getProfessor() + "\n" + course.getLocation());
            textArea.setEditable(false);
            textArea.setOpaque(false);
            textArea.setFont(new Font("맑은 고딕", Font.PLAIN, 12));

            JButton deleteBtn = new JButton("X");
            deleteBtn.setMargin(new Insets(0, 0, 0, 0));
            deleteBtn.setFont(new Font("맑은 고딕", Font.BOLD, 10));
            deleteBtn.setForeground(Color.RED);
            deleteBtn.setBorder(BorderFactory.createEmptyBorder());
            deleteBtn.setContentAreaFilled(false);
            deleteBtn.setToolTipText("삭제");
            deleteBtn.addActionListener(e -> {
                timetable.getCourses().remove(course);
                drawTimetable();
            });

            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setOpaque(false);
            topPanel.add(deleteBtn, BorderLayout.EAST);

            classPanel.add(topPanel, BorderLayout.NORTH);
            classPanel.add(textArea, BorderLayout.CENTER);

            for (TimeSlot slot : course.getTimeSlots()) {
                int dayIndex = getDayIndex(slot.getDay());
                if (dayIndex == -1) continue;

                int start = slot.getStartHour();
                int end = slot.getEndHour();

                gbc.gridx = dayIndex + 1;
                gbc.gridy = start - START_HOUR + 1;
                gbc.gridheight = end - start;
                timetablePanel.add(classPanel, gbc);
                gbc.gridheight = 1;
            }
        }
        timetablePanel.revalidate();
        timetablePanel.repaint();
    }

    private static int getDayIndex(String day) {
        for (int i = 0; i < DAYS.length; i++) {
            if (DAYS[i].equals(day)) return i;
        }
        return -1;
    }
}