package model;

// 추상 클래스 

public abstract class AbstractCourse {
    protected String subjectCode;
    protected String name;
    protected String professor;
    protected String location;
    protected String division;
    protected int year;
    protected int credit;

    public abstract String getCourseType(); // 예: "전공", "교양"

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
