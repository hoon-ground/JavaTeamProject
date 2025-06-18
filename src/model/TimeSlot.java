package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeSlot {
  private final String day;
  private final int startHour;
  private final int startMinute;
  private final int endHour;
  private final int endMinute;

  public TimeSlot(String day, int startHour, int startMinute, int endHour, int endMinute) {
    this.day = day;
    this.startHour = startHour;
    this.startMinute = startMinute;
    this.endHour = endHour;
    this.endMinute = endMinute;
  }

  public String getDay() {
    return day;
  }

  public int getStartHour() {
    return startHour;
  }

  public int getStartMinute() {
    return startMinute;
  }

  public int getEndHour() {
    return endHour;
  }

  public int getEndMinute() {
    return endMinute;
  }

  public boolean overlaps(TimeSlot other) {
    if (!this.day.equals(other.day)) return false;
    int thisStart = startHour * 60 + startMinute;
    int thisEnd = endHour * 60 + endMinute;
    int otherStart = other.startHour * 60 + other.startMinute;
    int otherEnd = other.endHour * 60 + other.endMinute;

    return !(thisEnd <= otherStart || otherEnd <= thisStart);
  }

  @Override
  public String toString() {
    return String.format("%s %02d:%02d~%02d:%02d", day, startHour, startMinute, endHour, endMinute);
  }

  public static List<TimeSlot> parseTimeString(String timeString) {
      List<TimeSlot> slots = new ArrayList<>();

      if (timeString == null || timeString.trim().isEmpty()) return slots;

      Map<String, Integer> startTimes = new HashMap<>();
      startTimes.put("1A", 9 * 60);
      startTimes.put("1B", 9 * 60 + 30);
      startTimes.put("2A", 10 * 60);
      startTimes.put("2B", 10 * 60 + 30);
      startTimes.put("3A", 11 * 60);
      startTimes.put("3B", 11 * 60 + 30);
      startTimes.put("4A", 12 * 60);
      startTimes.put("4B", 12 * 60 + 30);
      startTimes.put("5A", 13 * 60);
      startTimes.put("5B", 13 * 60 + 30);
      startTimes.put("6A", 14 * 60);
      startTimes.put("6B", 14 * 60 + 30);
      startTimes.put("7A", 15 * 60);
      startTimes.put("7B", 15 * 60 + 30);
      startTimes.put("8A", 16 * 60);
      startTimes.put("8B", 16 * 60 + 30);
      startTimes.put("9A", 17 * 60);
      startTimes.put("9B", 17 * 60 + 30);

      String[] lines = timeString.split("\n");
      for (String line : lines) {
          line = line.trim();
          if (line.isEmpty()) continue;

          String[] parts = line.split(" ");
          if (parts.length != 2) continue;

          String day = parts[0];
          String[] codes = parts[1].split(",");

          for (String code : codes) {
              code = code.trim();
              if (!startTimes.containsKey(code)) continue;

              int start = startTimes.get(code);
              int end = start + 30;

              int startHour = start / 60, startMin = start % 60;
              int endHour = end / 60, endMin = end % 60;

              slots.add(new TimeSlot(day, startHour, startMin, endHour, endMin));
          }
      }

      return slots;
  }

}
