package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Timetable;

import java.io.*;

public class JsonUtil {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_PATH = "src/data/timetable.json";

    public static void saveTimetable(Timetable timetable) {
        try {
            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs(); // 폴더 없으면 생성
            try (FileWriter writer = new FileWriter(file)) {
                gson.toJson(timetable, writer);
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
