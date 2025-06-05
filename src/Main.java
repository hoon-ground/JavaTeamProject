import javax.swing.SwingUtilities;
import gui.GraduationUI;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GraduationUI());
    }
}
