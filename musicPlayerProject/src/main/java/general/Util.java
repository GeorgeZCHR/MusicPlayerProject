package general;
import gui.CustomScrollBarUI;

import javax.swing.*;
import java.awt.*;

public class Util {
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
}