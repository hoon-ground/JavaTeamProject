package model;

import java.util.ArrayList;
import java.util.List;

public class Timetable {
	private List<Course> courses;
	private Student owner;

	public void setOwner(Student owner) {
		this.owner = owner;
	}

	public Timetable() {
		this.courses = new ArrayList<>();
	}

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
		if (owner != null) {
			owner.recalculate();
		}
		return true;
	}

	public boolean addCourse(String subjectCode, String name, String professor, String location,
                         List<TimeSlot> timeSlots, String division, int year, int credit) {
		Course course = new Course(subjectCode, name, professor, location, timeSlots, division, year, credit);
		return addCourse(course); // 기존 메서드 활용
	}
	
	public void setCourses(List<Course> courses) {
		this.courses = courses;
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

	public boolean removeCourse(Course course) {
		boolean removed = courses.remove(course);
		if (removed && owner != null) {
			owner.recalculate();
		}
		return removed;
	}

	public Student getOwner() {
		return owner;
	}
}
