package model;

import java.util.List;
import java.util.ArrayList;

public class Student {
    private String major;                    //소속 학과
    private int totalCredits;               //총 이수학점
    private int majorCredits;               //전공 이수학점
    private int generalCredits;             //교양 이수학점
    private int englishScore;               //공인 영어성적
    private List<Course> completedCourses;  //수강 완료한 과목목록

    public Student(String major, List<Course> completedCourses, int englishScore) {
        this.major = major;
        this.completedCourses = completedCourses;
        this.englishScore = englishScore;
        calculateCredits();
    }

    private void calculateCredits() {
        totalCredits = 0;
        majorCredits = 0;
        generalCredits = 0;

        for (Course course : completedCourses) {
            totalCredits += course.getCredit();

            if (isMajorCourse(course)) {
                majorCredits += course.getCredit();
            } else {
                generalCredits += course.getCredit();
            }
        }
    }

    public void recalculate() {
        calculateCredits();
    }

    private boolean isMajorCourse(Course course) {
        return course.getCourseId().startsWith("CS") || course.getCourseId().startsWith("MAJ");
    }

    public boolean isGraduationPossible(GraduationRequirement req) {
        boolean totalOk = totalCredits >= req.getRequiredTotalCredits();
        boolean majorOk = majorCredits >= req.getRequiredMajorCredits();
        boolean generalOk = generalCredits >= req.getRequiredGeneralCredits();
        boolean englishOk = englishScore >= req.getRequiredEnglishScore();

        return totalOk && majorOk && generalOk && englishOk;
    }

	public List<String> getGraduationStatusDetails(GraduationRequirement req) {
    	List<String> result = new ArrayList<>();

    	if (totalCredits >= req.getRequiredTotalCredits()) {
        	result.add("✅ 총 이수 학점 충족 (" + totalCredits + "/" + req.getRequiredTotalCredits() + ")");
    	} else {
        	result.add("❌ 총 이수 학점 부족 (" + totalCredits + "/" + req.getRequiredTotalCredits() + ")");
    	}

    	if (majorCredits >= req.getRequiredMajorCredits()) {
        	result.add("✅ 전공 학점 충족 (" + majorCredits + "/" + req.getRequiredMajorCredits() + ")");
    	} else {
        	result.add("❌ 전공 학점 부족 (" + majorCredits + "/" + req.getRequiredMajorCredits() + ")");
    	}

    	if (generalCredits >= req.getRequiredGeneralCredits()) {
        	result.add("✅ 교양 학점 충족 (" + generalCredits + "/" + req.getRequiredGeneralCredits() + ")");
    	} else {
        	result.add("❌ 교양 학점 부족 (" + generalCredits + "/" + req.getRequiredGeneralCredits() + ")");
    	}

    	if (englishScore >= req.getRequiredEnglishScore()) {
        	result.add("✅ 공인 영어 점수 충족 (" + englishScore + "/" + req.getRequiredEnglishScore() + ")");
    	} else {
        	result.add("❌ 공인 영어 점수 부족 (" + englishScore + "/" + req.getRequiredEnglishScore() + ")");
    	}

    	return result;
	}

    public int getTotalCredits() { return totalCredits; }
    public int getMajorCredits() { return majorCredits; }
    public int getGeneralCredits() { return generalCredits; }
    public int getEnglishScore() { return englishScore; }
    public List<Course> getCompletedCourses() { return completedCourses; }

}
