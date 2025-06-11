// JsonUtil.java
package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Timetable;
import model.Course;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

public class JsonUtil {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_PATH = "src/data/timetable.json";

    public static void saveTimetable(Timetable timetable) {
        try {
            // 기존 JSON 파일 읽기
            JSONObject root = new JSONObject();
            File file = new File(FILE_PATH);
            if (file.exists()) {
                FileReader reader = new FileReader(file);
                StringBuilder content = new StringBuilder();
                int i;
                while ((i = reader.read()) != -1) {
                    content.append((char) i);
                }
                root = new JSONObject(content.toString());
            }

            // 과목 배열 업데이트
            JSONArray courses = root.getJSONArray("courses");
            for (Course course : timetable.getCourses()) {
                JSONObject courseObj = new JSONObject();
                courseObj.put("name", course.getName());
                courseObj.put("professor", course.getProfessor());
                // 다른 속성들도 추가
                courses.put(courseObj);
            }

            // JSON 파일에 저장
            try (FileWriter writer = new FileWriter(file)) {
                gson.toJson(root.toString(2), writer);
                System.out.println("✅ 시간표 저장 완료: " + FILE_PATH);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Timetable loadTimetable() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            return gson.fromJson(reader, Timetable.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
