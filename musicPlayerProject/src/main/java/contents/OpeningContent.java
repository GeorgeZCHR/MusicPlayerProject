package contents;
import javax.swing.*;
import java.awt.*;
import general.Util;

public class OpeningContent extends JPanel implements Content{
    public OpeningContent(LayoutManager layout) { super(layout); }

    @Override
    public void init() {
        //---Opening Label---
        JLabel openingLabel = new JLabel("Welcome to the Music Player Project!");
        openingLabel.setBounds((int)(getWidth() * 0.02),
                (int)(getHeight() * 0.02),(int)(getWidth() * 0.96),50);
        openingLabel.setFont(Util.myFont);
        openingLabel.setForeground(Util.blue_dark_color);

        add(openingLabel);
    }
}