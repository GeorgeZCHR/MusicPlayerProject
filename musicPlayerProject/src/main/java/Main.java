
import javax.swing.*;

public class Main {
    public static void main(String[] args) {



        SwingUtilities.invokeLater(() -> {
            MusicPlayerFrame mpf = new MusicPlayerFrame(1080,720);
        });
    }

}