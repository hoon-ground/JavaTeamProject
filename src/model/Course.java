package model;

import java.util.List;

public class Course extends AbstractCourse implements Exportable {
    private final String subjectCode;   //교과목번호
    private final String name;          //교과목명
    private final String professor;		//교수명
    private final String location;		//수업장소
    private final String division;      //구분 (전공, 교양 등)
    private final int year;             //학년
    private final int credit;           //학점
    private final List<TimeSlot> timeSlots;

    public Course(String subjectCode, String name, String professor, String location,
                  List<TimeSlot> timeSlots, String division, int year, int credit) {
        this.subjectCode = subjectCode;
        this.name = name;
        this.professor = professor;
        this.location = location;
        this.timeSlots = timeSlots;
        this.division = division;
        this.year = year;
        this.credit = credit;
    }

    @Override
    public String getCourseType() {
        return division; // 예: "전공", "교양" 반환
    }

    @Override
    // 인터페이스 구현
    public String toExportString() {
        return subjectCode + "," + name + "," + professor + "," + location + "," +
               division + "," + year + "," + credit;
    }

    public String getSubjectCode() {
        return subjectCode;
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

    public String getDivision() {
        return division;
    }

    public int getYear() {
        return year;
    }

    public int getCredit() {
        return credit;
    }

    public String getCourseId() {
        return subjectCode;
    }

    @Override
    public String toString() {
        return name + " - " + professor;
    }
}
