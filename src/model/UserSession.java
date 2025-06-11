// src/model/UserSession.java
package model;

public class UserSession {
    private static String studentId;
    private static String name;
    private static String selectedSemester = "2025년 1학기"; // 기본값

    public static String getStudentId() {
        return studentId;
    }

    public static void setStudentId(String id) {
        studentId = id;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String n) {
        name = n;
    }

    public static String getSelectedSemester() {
        return selectedSemester;
    }

    public static void setSelectedSemester(String semester) {
        selectedSemester = semester;
    }
}