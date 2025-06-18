package model;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.*;

public class ExcelCourseLoader {
    public static List<Course> loadCourses(String filePath) {
        List<Course> courses = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(new FileInputStream(filePath))) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 2; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String subjectCode = getCellString(row, 1);
                String name = getCellString(row, 5);
                String professor = getCellString(row, 13);
                String location = getCellString(row, 9);
                String timeString = getCellString(row, 8);
                String division = getCellString(row, 7);
                String yearStr = getCellString(row, 6);
                int year = parseYear(yearStr);
                int credit = (int) getCellNumber(row, 2);

                if (subjectCode.isEmpty() || name.isEmpty()) continue;

                List<TimeSlot> timeSlots = TimeSlot.parseTimeString(timeString);
                Course course = new Course(subjectCode, name, professor, location, timeSlots, division, year, credit);
                courses.add(course);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return courses;
    }

    private static String getCellString(Row row, int index) {
        Cell cell = row.getCell(index);
        return cell != null ? cell.toString().trim() : "";
    }

    private static double getCellNumber(Row row, int index) {
        try {
            Cell cell = row.getCell(index);
            if (cell == null) return 0;
            switch (cell.getCellType()) {
                case NUMERIC:
                    return cell.getNumericCellValue();
                case STRING:
                    return Double.parseDouble(cell.getStringCellValue().replaceAll("[^\\d.]", ""));
                default:
                    return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    private static int parseYear(String yearStr) {
        try {
            int year = Integer.parseInt(yearStr.replaceAll("[^0-9]", ""));
            if (year >= 10) year = year / 10;
            return year;
        } catch (Exception e) {
            return 0;
        }
    }
}
