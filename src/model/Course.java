package model;

import java.util.List;

public class Course {
	private final String name;
	private final String professor;
	private final String location;
	private final List<TimeSlot> timeSlots;

	public Course(String name, String professor, String location, List<TimeSlot> timeSlots) {
		this.name = name;
		this.professor = professor;
		this.location = location;
		this.timeSlots = timeSlots;
	}

	public String getName() {
		return name;
	}

	public String getProfessor() {
		return professor;
	}

	public String getLocation() {
		return location;
	}

	public List<TimeSlot> getTimeSlots() {
		return timeSlots;
	}

	@Override
	public String toString() {
		return name + " - " + professor;
	}
}

