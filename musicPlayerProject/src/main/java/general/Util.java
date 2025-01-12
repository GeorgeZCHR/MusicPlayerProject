package general;
import gui.CustomScrollBarUI;
import org.json.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public class Util {
    public static final int OPENING_CONTENT = -1;
    public static final int MUSIC_CONTENT = 0;
    public static final int CREATE_PLAYLIST_CONTENT = 1;
    public static final int BIO_CONTENT = 2;
    public static final int TOP_ARTISTS_CONTENT = 3;
    public static final int TOP_TRACKS_CONTENT = 4;
    public static final int TOP_ALBUMS_CONTENT = 5;
    public static final int SEARCH_RESULTS_CONTENT = 6;
    public static final int ABOUT_ME_CONTENT = 7;
    public static final int LOADING_CONTENT = 8;
    public static final Color blue_dark_color = new Color(0x264653);
    public static final Color blue_color = new Color(0x2a9d8f);
    public static final Color orange_light_color = new Color(0xe9c46a);
    public static final Color orange_dark_color = new Color(0xF08041);
    public static final Color orange_color = new Color(0xf4a261);
    public static final Color red_light_color = new Color(0xe76f51);
    public static final Color DEFAULT_TEXT_COLOR = (new JLabel()).getForeground();
    public static final Font songNameFont = new Font(Font.SANS_SERIF, Font.BOLD,30);
    public static final Font myFont = new Font(Font.SANS_SERIF, Font.BOLD,20);
    public static final Font headerFont = new Font(Font.SANS_SERIF, Font.BOLD,40);

    public static JScrollPane createScrollPane(JPanel panel, Rectangle rectangle, Color thColor, Color trColor) {
        JScrollPane sp = new JScrollPane(panel);
        sp.setBounds(rectangle);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getHorizontalScrollBar().setUI(new CustomScrollBarUI(thColor,trColor));
        sp.getVerticalScrollBar().setUI(new CustomScrollBarUI(thColor,trColor));
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        return sp;
    }

    public static JScrollPane createScrollPane(JTextArea jTextArea, Rectangle rectangle, Color thColor, Color trColor) {
        jTextArea.setForeground(blue_dark_color);
        jTextArea.setDisabledTextColor(blue_dark_color);
        jTextArea.setOpaque(false);
        jTextArea.setDisabledTextColor(Util.DEFAULT_TEXT_COLOR);
        jTextArea.setLineWrap(true);
        jTextArea.setWrapStyleWord(true);
        jTextArea.setFont(Util.myFont);
        JScrollPane sp = new JScrollPane(jTextArea);
        sp.setBounds(rectangle);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getHorizontalScrollBar().setUI(new CustomScrollBarUI(thColor,trColor));
        sp.getVerticalScrollBar().setUI(new CustomScrollBarUI(thColor,trColor));
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        return sp;
    }

    public static boolean isUnderHour(long duration) {
        return duration < 3600; // is shorter than 1 hour
    }

    public static String getDurationInHumanTime(long duration) {
        return (duration / 60) + ":" + (duration % 60);
    }

    public static void writeExampleUserCredentials() {
        JSONObject userCredentials = new JSONObject();

        userCredentials.put("email", "blablabla");
        userCredentials.put("password", "blablabla");

        String filePath = "userCredentials.json";

        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(userCredentials.toString(4));
            System.out.println("JSON file created: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing JSON file: " + e.getMessage());
        }
    }
}