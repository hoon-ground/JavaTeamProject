import javax.swing.*;
import gui.TimetableGUIWithExcel;
import javax.swing.SwingUtilities;
import gui.GraduationUI;

public class Main {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 에브리타임 스타일 엑셀 기반 시간표 GUI 실행
		SwingUtilities.invokeLater(TimetableGUIWithExcel::new);
	}
}