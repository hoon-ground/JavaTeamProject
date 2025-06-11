package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Course;
import model.TimeSlot;
import model.Timetable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

public class JsonUtil {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_PATH = "src/data/timetable.json";

    public static List<Course> jsonToCourses(JSONArray jsonArray) {
        List<Course> courses = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String subjectCode = obj.optString("subjectCode", "UNKNOWN");
            String name = obj.getString("name");
            String professor = obj.optString("professor", "미정");
            String location = obj.optString("location", "미정");
            String division = obj.optString("division", "전공");
            int year = obj.optInt("year", 1);
            int credit = obj.optInt("credit", 3);

            JSONArray times = obj.getJSONArray("timeSlots");
            List<TimeSlot> timeSlots = new ArrayList<>();
            for (int j = 0; j < times.length(); j++) {
                JSONObject t = times.getJSONObject(j);
                timeSlots.add(new TimeSlot(
                    t.getString("day"),
                    t.getInt("startHour"),
                    t.getInt("startMinute"),
                    t.getInt("endHour"),
                    t.getInt("endMinute")
                ));
            }

            Course c = new Course(subjectCode, name, professor, location, timeSlots, division, year, credit);
            courses.add(c);
        }

        return courses;
    }

    public static void saveUserTimetable(String studentId, String name, String semester, List<Course> courses) {
        try {
            File file = new File("src/data/timetable.json");
            file.getParentFile().mkdirs();

            JSONObject root;
            if (file.exists()) {
                root = new JSONObject(new String(Files.readAllBytes(file.toPath())));
            } else {
                root = new JSONObject();
                root.put("users", new JSONArray());
            }

            JSONArray users = root.getJSONArray("users");
            JSONObject targetUser = null;

            // 기존 유저 탐색
            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                if (studentId.equals(user.optString("studentId"))) {
                    targetUser = user;
                    break;
                }
            }

            // 유저 없으면 새로 추가
            if (targetUser == null) {
                targetUser = new JSONObject();
                targetUser.put("studentId", studentId);
                targetUser.put("name", name);
                targetUser.put("courses", new JSONArray());
                targetUser.put("timetables", new JSONObject());
                users.put(targetUser);
            }

            // 학기 반영
            targetUser.put("selectedSemester", semester);
            JSONArray courseArray = new JSONArray(gson.toJson(courses));
            targetUser.getJSONObject("timetables").put(semester, courseArray);

            // 저장
            Files.write(file.toPath(), root.toString(4).getBytes());
            System.out.println("✅ 시간표 저장 완료");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static List<Course> loadUserTimetable(String studentId, String semester) {
        List<Course> courses = new ArrayList<>();
        try {
            File file = new File("src/data/timetable.json");
            if (!file.exists()) return courses;

            String content = new String(Files.readAllBytes(file.toPath()));
            JSONObject root = new JSONObject(content);
            JSONArray users = root.optJSONArray("users");
            if (users == null) return courses;

            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                if (!studentId.equals(user.optString("studentId"))) continue;

                JSONObject timetables = user.optJSONObject("timetables");
                if (timetables == null || !timetables.has(semester)) return courses;

                JSONArray courseArray = timetables.getJSONArray(semester);
                return jsonToCourses(courseArray);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courses;
    }



    public static String getSelectedSemester(String studentId) {
        try {
            File file = new File("src/data/timetable.json");
            if (!file.exists()) return "2025년 1학기";

            String content = new String(Files.readAllBytes(file.toPath()));
            JSONObject root = new JSONObject(content);
            JSONArray users = root.optJSONArray("users");
            if (users == null) return "2025년 1학기";

            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                if (studentId.equals(user.optString("studentId"))) {
                    return user.optString("selectedSemester", "2025년 1학기");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "2025년 1학기";
    }

}
