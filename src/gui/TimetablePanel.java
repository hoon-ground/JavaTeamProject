package gui;

import model.Course;
import model.ExcelCourseLoader;
import model.TimeSlot;
import model.Timetable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TimetablePanel extends JPanel {
    private static final String[] DAYS = {"", "월", "화", "수", "목", "금"};
    private static final String[] TIMES = {
        "", "오전 9시", "오전 10시", "오전 11시", "오후 12시", "오후 1시",
        "오후 2시", "오후 3시", "오후 4시", "오후 5시", "오후 6시"
    };

    private final Timetable timetable;
    private final JPanel gridPanel;

    public TimetablePanel() {
        this.setLayout(new BorderLayout());
        this.timetable = new Timetable();
        this.gridPanel = new JPanel(new GridLayout(TIMES.length, DAYS.length));

        JButton addCourseBtn = new JButton("수업 목록에서 검색 및 추가");
        addCourseBtn.addActionListener(e -> showCourseSelectionDialog());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(addCourseBtn);

        this.add(topPanel, BorderLayout.NORTH);
        this.add(gridPanel, BorderLayout.CENTER);
        drawGrid();
        
        addCourseBtn.setFont(new Font("Poppins", Font.BOLD, 14));
        addCourseBtn.setBackground(Color.WHITE);
        addCourseBtn.setForeground(Color.BLACK);
        addCourseBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        addCourseBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    }

    private void drawGrid() {
        gridPanel.removeAll();

        for (int r = 0; r < TIMES.length; r++) {
            for (int c = 0; c < DAYS.length; c++) {
                JPanel cell = new JPanel(new BorderLayout());
                cell.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                cell.setBackground(Color.WHITE);

                if (r == 0 && c > 0) {
                    cell.add(new JLabel(DAYS[c], SwingConstants.CENTER), BorderLayout.CENTER);
                } else if (c == 0 && r > 0) {
                    cell.add(new JLabel(TIMES[r], SwingConstants.CENTER), BorderLayout.CENTER);
                } else {
                    for (Course course : timetable.getCourses()) {
                        for (TimeSlot slot : course.getTimeSlots()) {
                        	if (slot.getDay().equals(DAYS[c]) && slot.getStartHour() == 9 + (r - 1)) {
                        	    JLabel label = new JLabel("<html><center>" +
                        	        course.getName() + "<br/>" + course.getProfessor() +
                        	        "</center></html>", SwingConstants.CENTER);
                        	    label.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
                        	    label.setOpaque(false);

                        	    JPanel overlay = new JPanel(new BorderLayout());
                        	    overlay.setOpaque(false);
                        	    overlay.add(label, BorderLayout.CENTER);

                        	    Course selectedCourse = course;
                        	    overlay.addMouseListener(new MouseAdapter() {
                        	        @Override
                        	        public void mouseClicked(MouseEvent e) {
                        	            showCourseDetailDialog(selectedCourse);
                        	        }

                        	        @Override
                        	        public void mouseEntered(MouseEvent e) {
                        	            overlay.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        	        }

                        	        @Override
                        	        public void mouseExited(MouseEvent e) {
                        	            overlay.setCursor(Cursor.getDefaultCursor());
                        	        }
                        	    });

                        	    cell.removeAll();
                        	    cell.setLayout(new BorderLayout());
                        	    cell.add(overlay, BorderLayout.CENTER);
                        	}

                        }
                    }
                }

                gridPanel.add(cell);
            }
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void showCourseSelectionDialog() {
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
                c.getYear() + "학년", c.getDivision(), c.getSubjectCode(), c.getName(),
                c.getProfessor(), timeStr, c.getLocation(), c.getCredit()
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
                            c.getYear() + "학년", c.getDivision(), c.getSubjectCode(), c.getName(),
                            c.getProfessor(), timeStr, c.getLocation(), c.getCredit()
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
    
    private void showCourseDetailDialog(Course course) {
        JTextArea detailArea = new JTextArea("과목명: " + course.getName() +
            "\n교수: " + course.getProfessor() +
            "\n시간: " + course.getTimeSlots().toString() +
            "\n장소: " + course.getLocation() +
            "\n과목코드: " + course.getSubjectCode() +
            "\n이수구분: " + course.getDivision() +
            "\n학점: " + course.getCredit());
        detailArea.setEditable(false);

        JButton deleteButton = new JButton("삭제");
        deleteButton.addActionListener(e -> {
            timetable.getCourses().remove(course);
            drawGrid();
            JOptionPane.showMessageDialog(this, "과목이 삭제되었습니다.");
            Window window = SwingUtilities.getWindowAncestor(deleteButton);
            if (window != null) window.dispose();
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(detailArea), BorderLayout.CENTER);
        panel.add(deleteButton, BorderLayout.SOUTH);

        JDialog dialog = new JDialog();
        dialog.setTitle("과목 상세정보");
        dialog.setSize(300, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setModal(true);
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    public Timetable getTimetable() {
        return this.timetable;
    }

    public void setTimetable(Timetable newTimetable) {
        this.timetable.getCourses().clear();
        this.timetable.getCourses().addAll(newTimetable.getCourses());
        drawGrid();
    }

}
