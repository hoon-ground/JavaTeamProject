package model;

public class GraduationRequirement {
    private int requiredTotalCredits;           // 전체 졸업 이수 학점
    private int requiredMajorCredits;           // 전공 필수 이수 학점
    private int requiredGeneralCredits;         // 교양 필수 이수 학점
    private int requiredEnglishScore;           // 공인 영어 성적 기준 (700점 이상)

    public GraduationRequirement(int requiredTotalCredits,
                                 int requiredMajorCredits,
                                 int requiredGeneralCredits,
                                 int requiredEnglishScore) {
        this.requiredTotalCredits = requiredTotalCredits;
        this.requiredMajorCredits = requiredMajorCredits;
        this.requiredGeneralCredits = requiredGeneralCredits;
        this.requiredEnglishScore = requiredEnglishScore;
    }

    // Getter & Setter
    public int getRequiredTotalCredits() {
        return requiredTotalCredits;
    }

    public void setRequiredTotalCredits(int requiredTotalCredits) {
        this.requiredTotalCredits = requiredTotalCredits;
    }

    public int getRequiredMajorCredits() {
        return requiredMajorCredits;
    }

    public void setRequiredMajorCredits(int requiredMajorCredits) {
        this.requiredMajorCredits = requiredMajorCredits;
    }

    public int getRequiredGeneralCredits() {
        return requiredGeneralCredits;
    }

    public void setRequiredGeneralCredits(int requiredGeneralCredits) {
        this.requiredGeneralCredits = requiredGeneralCredits;
    }

    public int getRequiredEnglishScore() {
        return requiredEnglishScore;
    }

    public void setRequiredEnglishScore(int requiredEnglishScore) {
        this.requiredEnglishScore = requiredEnglishScore;
    }

    @Override
    public String toString() {
        return String.format(
            "졸업요건 - 총학점: %d, 전공학점: %d, 교양학점: %d, 영어기준점수: %d",
            requiredTotalCredits,
            requiredMajorCredits,
            requiredGeneralCredits,
            requiredEnglishScore
        );
    }
    /*
     * 추후 구현:
     * - 학과별 기본 졸업요건 정보를 JSON 또는 DB에서 로드하는 정적 메서드 추가
     * - 예: GraduationRequirement.fromMajor("컴퓨터공학과")
     */

}
