package model;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.util.*;
import model.Course;
import model.TimeSlot;

public class ExcelCourseLoader {

  public static List<Course> loadCourses(String filePath) {
    List<Course> courses = new ArrayList<>();

    try (FileInputStream fis = new FileInputStream(filePath);
         Workbook workbook = new XSSFWorkbook(fis)) {

      Sheet sheet = workbook.getSheetAt(0);
      for (int i = 1; i <= sheet.getLastRowNum(); i++) {
        Row row = sheet.getRow(i);
        if (row == null) continue;

        String name = getCellAsString(row.getCell(5));      // 교과목명
        String timeInfo = getCellAsString(row.getCell(8));  // 강의시간
        String location = getCellAsString(row.getCell(9));  // 강의실
        String professor = getCellAsString(row.getCell(13));// 담당교수


        List<TimeSlot> timeSlots = parseTimeSlotString(timeInfo);

        Course course = new Course(name, professor, location, timeSlots);
        courses.add(course);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return courses;
  }

  // 시간표 문자열 파싱 함수
  private static List<TimeSlot> parseTimeSlotString(String timeInfo) {
    List<TimeSlot> slots = new ArrayList<>();
    if (timeInfo == null) return slots;

    String[] lines = timeInfo.split("\n"); // 요일별 분리
    for (String line : lines) {
      String[] parts = line.trim().split(" ");
      if (parts.length < 2) continue;

      String day = parts[0];
      String[] codes = parts[1].split(",");
      for (String code : codes) {
        int[] times = lectureTimeMap.get(code.trim());
        if (times != null) {
          slots.add(new TimeSlot(day, times[0], times[1], times[2], times[3]));
        }
      }
    }
    return slots;
  }

  // 교시 코드 → 시간대 매핑
  private static final Map<String, int[]> lectureTimeMap = Map.ofEntries(
      Map.entry("1A", new int[]{9, 0, 9, 30}),
      Map.entry("1B", new int[]{9, 30, 10, 0}),
      Map.entry("2A", new int[]{10, 0, 10, 30}),
      Map.entry("2B", new int[]{10, 30, 11, 0}),
      Map.entry("3A", new int[]{11, 0, 11, 30}),
      Map.entry("3B", new int[]{11, 30, 12, 0}),
      Map.entry("4A", new int[]{12, 0, 12, 30}),
      Map.entry("4B", new int[]{12, 30, 13, 0}),
      Map.entry("5A", new int[]{13, 0, 13, 30}),
      Map.entry("5B", new int[]{13, 30, 14, 0}),
      Map.entry("6A", new int[]{14, 0, 14, 30}),
      Map.entry("6B", new int[]{14, 30, 15, 0}),
      Map.entry("7A", new int[]{15, 0, 15, 30}),
      Map.entry("7B", new int[]{15, 30, 16, 0}),
      Map.entry("8A", new int[]{16, 0, 16, 30}),
      Map.entry("8B", new int[]{16, 30, 17, 0}),
      Map.entry("9A", new int[]{17, 0, 17, 30}),
      Map.entry("9B", new int[]{17, 30, 18, 0})
  );

  private static String getCellAsString(Cell cell) {
    if (cell == null) return "";
    return switch (cell.getCellType()) {
      case STRING -> cell.getStringCellValue();
      case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
      default -> "";
    };
  }
}