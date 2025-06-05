package model;

public class TimeSlot {
  private final String day;       // "월", "화", ...
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
}
