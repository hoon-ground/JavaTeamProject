import javax.swing.*;
import javax.swing.SwingUtilities;
import gui.StudentAppGUI;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(StudentAppGUI::new);
    }
}
