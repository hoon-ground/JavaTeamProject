package model;

import java.util.ArrayList;
import java.util.List;

public class Timetable {
	private final List<Course> courses;

	public Timetable(List<Course> courseList) {
		this.courses = new ArrayList<>();
		for (Course course : courseList) {
			this.addCourse(course);
		}
	}


	public boolean addCourse(Course course) {
		for (Course existing : courses) {
			for (TimeSlot ts1 : existing.getTimeSlots()) {
				for (TimeSlot ts2 : course.getTimeSlots()) {
					if (ts1.overlaps(ts2)) {
						System.out.println("❌ 시간이 겹치는 수업입니다: " + course.getName());
						return false;
					}
				}
			}
		}
		courses.add(course);
		return true;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void printTimetable() {
		System.out.println(this);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Course course : courses) {
			sb.append("📘 수업: ").append(course.getName()).append("\n");
			for (TimeSlot ts : course.getTimeSlots()) {
				sb.append("  - ").append(ts).append("\n");
			}
		}
		return sb.toString();
	}
}
