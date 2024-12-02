package gui;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

public class SongSelector extends JPanel{
    private List<JPanel> songs = new ArrayList<>();
    private List<Boolean> isSeleced = new ArrayList<>();
    private List<String> selectedSongs = new ArrayList<>();
    private int cornerRadius;
    private Color panelColor;

    public SongSelector(Color panelColor, int cornerRadius,
                        List<String> names) {
        this.panelColor = panelColor;
        this.cornerRadius = cornerRadius;
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        for (int i = 0; i < names.size(); i++) {
            int finalI = i;
            JPanel songPanel = new JPanel();
            songPanel.setLayout(new BorderLayout());
            songPanel.setOpaque(false);

            CustomButton song = new CustomButton(
                    names.get(i),panelColor,20,20);
            song.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
            song.setFocusable(false);
            song.addActionListener(e -> {
                CustomButton songNameButton = (CustomButton)(songs.get(finalI).getComponent(0));
                if (isSeleced.get(finalI)) {
                    removeSongFromName(songNameButton.getText());
                    songNameButton.setColor(panelColor);
                } else {
                    selectedSongs.add(songNameButton.getText());
                    songNameButton.setColor(new Color(0xF08041));
                }
                isSeleced.set(finalI,!isSeleced.get(finalI));
                System.out.println(selectedSongs);
            });
            isSeleced.add(false);
            songPanel.add(song, BorderLayout.CENTER);

            songs.add(songPanel);
            add(songs.get(finalI));
        }
    }

    public void removeSongFromName(String name) {
        for (int i = 0; i < selectedSongs.size(); i++) {
            if (selectedSongs.get(i).equals(name)) {
                selectedSongs.remove(i);
                return;
            }
        }
    }

    public List<String> getSelectedSongs() {
        return selectedSongs;
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
}
