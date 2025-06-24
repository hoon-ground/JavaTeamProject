# JavateamProject
<code>#Demo Video</code>

https://www.youtube.com/watch?v=AOxGKzkTcmE

<code>#DESCRIPTION</code>

대학생활에 필요한 시간표 편성, 학점 계산, 졸업 요건 확인 기능을 하나로 통합한 Java 기반 학사관리 프로그램.


사용자는 이름과 학번을 입력하면 개인 시간표를 만들 수 있으며, 전공/교양 구분과 학점을 바탕으로 GPA 계산, 졸업 충족 여부 확인까지 가능.

<code>#HOW TO COMPILE & RUN</code>

Java JDK 17 이상 설치
resources/ 폴더 내 과목 엑셀파일: 교양.xlsx, 전공.xlsx
data/ 폴더 (자동 생성됨): 사용자 시간표 JSON 저장소

컴파일 및 실행 방법
Java IDE에서 프로젝트 src/ 디렉토리를 열기
gui.StudentAppGUI.java 파일 실행
main() 함수에서 프로그램 시작
GUI 화면이 뜨면 아래 순서로 사용 가능

<code>#PROGRAM FLOW</code>

시작 화면 (StartPanel)
→ "시작" 버튼 클릭 시 사용자 입력 패널로 이동

사용자 정보 입력 (UserInfoPanel)
→ 이름과 학번 입력 후 JSON 저장 및 세션 생성
→ 이후 메인 화면으로 이동

메인 기능 화면 (MainPanel)
⏱️ 수업 추가: 엑셀에서 로딩된 수업 리스트 검색 및 선택

💾 시간표 저장/불러오기 (학번 및 학기 기준)

📚 학점 계산기: 성적 입력 후 GPA 계산

🎓 졸업요건 확인: 현재 상태와 기준 비교 후 충족 여부 출력

<code>#DATA FORMAT</code>

Input:

resources/교양.xlsx, 전공.xlsx: 시간표 데이터

사용자 입력: 이름, 학번, 성적, 전공 여부 등

Output:

src/data/timetable.json: 학번/학기별 시간표 JSON 저장


<code>#IMPLEMENTATION ENVIRONMENT</code>

OS: Windows 10 / Ubuntu 20.04 (JDK 17 기준)

Language: Java (Swing GUI 기반)

IDE: Eclipse(또는 IntelliJ IDEA)

