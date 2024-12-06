package gui;
import containers.Song;
import general.MusicPlayerFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Playlist extends JPanel {
    private String title;
    private List<JPanel> records = new ArrayList<>();
    private int cornerRadius;
    private Color panelColor;
    private List<String> allSongNames = new ArrayList<>();
    private MusicPlayerFrame frame;
    private List<String> currentNames = new ArrayList<>();

    public Playlist(String title, Color panelColor, int cornerRadius,
                    List<String> currentNames, MusicPlayerFrame frame) {
        this.title = title;
        this.frame = frame;
        this.panelColor = panelColor;
        this.cornerRadius = cornerRadius;
        this.currentNames.addAll(currentNames);
        for (int i = 0; i < frame.getAllSongs().size(); i++) {
            allSongNames.add(frame.getAllSongs().get(i).getName());
        }
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        for (int i = 0; i < this.currentNames.size(); i++) {
            int finalI = i;
            JPanel record = new JPanel();
            record.setLayout(new BorderLayout());
            record.setOpaque(false);

            CustomButton name = new CustomButton(
                    currentNames.get(i),panelColor,20,20);
            name.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
            name.setFocusable(false);
            name.addActionListener(e -> {
                CustomButton nameButton = (CustomButton)(records.get(finalI).getComponent(0));
                frame.goTo(nameButton.getText());
            });
            name.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {}

                @Override
                public void mouseExited(MouseEvent e) {}
            });
            record.add(name, BorderLayout.WEST);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BorderLayout());
            buttonPanel.setOpaque(false);

            CustomButton like = new CustomButton(
                    "♡",panelColor,20,20);
            like.setFont(new Font(Font.SANS_SERIF,Font.BOLD,40));
            like.setFocusable(false);
            like.addActionListener(e -> {
                CustomButton nameButton = (CustomButton)(records.get(finalI).getComponent(0));
                CustomButton likeButton = (CustomButton)
                        (((JPanel)(records.get(finalI).getComponent(1))).getComponent(0));
                for (int j = 0; j < frame.getAllSongs().size(); j++) {
                    //System.out.println(likeButton.getText()+" = "+frame.getAllSongs().get(j).getName());
                    if (nameButton.getText().equals(frame.getAllSongs().get(j).getName())) {
                        if (!frame.getAllSongs().get(j).isHearted()) {
                            likeButton.setText("♥");
                            frame.getAllSongs().get(j).setHearted(true);
                        } else {
                            likeButton.setText("♡");
                            frame.getAllSongs().get(j).setHearted(false);
                        }
                    }
                }
            });
            buttonPanel.add(like, BorderLayout.WEST);

            CustomButton settingsButton = new CustomButton(
                    "⋮",panelColor,20,20);
            settingsButton.setFont(new Font(Font.SANS_SERIF,Font.BOLD,30));
            settingsButton.setFocusable(false);
            settingsButton.addActionListener(e -> {
                CustomButton nameButton = (CustomButton)(records.get(finalI).getComponent(0));
                System.out.println("Settings for: " + nameButton.getText());
            });
            buttonPanel.add(settingsButton, BorderLayout.EAST);

            record.add(buttonPanel, BorderLayout.EAST);
            records.add(record);
            add(records.get(finalI));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(panelColor);
        g2.fill(new RoundRectangle2D.Float(
                0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));

        g2.dispose();
    }

    public void setRecordBackgroundColor(Color color, int num) {
        for (JPanel record : records) {
            CustomButton button = (CustomButton)(record.getComponent(0));
            button.setColor(panelColor);
        }
        CustomButton nameButton = (CustomButton)(records.get(num).getComponent(0));
        nameButton.setColor(color);
    }

    public void checkHearts() {
        for (int i = 0; i < currentNames.size(); i++) {
            CustomButton nameButton = (CustomButton)(records.get(i).getComponent(0));
            CustomButton likeButton = (CustomButton)(((JPanel)(records.get(i).getComponent(1))).getComponent(0));
            for (int j = 0; j < allSongNames.size(); j++) {
                if (nameButton.getText().equals(frame.getAllSongs().get(j).getName())) {
                    if (!frame.getAllSongs().get(j).isHearted()) {
                        likeButton.setText("♡");
                    } else {
                        likeButton.setText("♥");
                    }
                }
            }
        }
    }

    public String getTitle() { return title; }

    public List<String> getAllSongNames() { return allSongNames; }
}