import general.MusicPlayerFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MusicPlayerFrame(1080,720));
    }
}