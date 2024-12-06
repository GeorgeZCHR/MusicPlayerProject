package gui;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SongSelector extends JPanel{
    private List<JPanel> songs = new ArrayList<>();
    private List<Boolean> isSelected = new ArrayList<>();
    private List<String> selectedSongs = new ArrayList<>();
    private int cornerRadius;
    private final Color panelColor;
    private final Color selectionColor;

    public SongSelector(Color panelColor, Color selectionColor, int cornerRadius,
                        List<String> names) {
        this.panelColor = panelColor;
        this.selectionColor = selectionColor;
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
                if (isSelected.get(finalI)) {
                    removeSongFromName(songNameButton.getText());
                    songNameButton.setColor(panelColor);
                    songNameButton.setForeground(panelColor.darker());
                } else {
                    selectedSongs.add(songNameButton.getText());
                    songNameButton.setColor(selectionColor);
                }
                isSelected.set(finalI,!isSelected.get(finalI));
                System.out.println(selectedSongs);
            });
            isSelected.add(false);
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

    public void clearSelectedSongs() {
        for (JPanel songPanel : songs) {
            CustomButton songNameButton = (CustomButton)(songPanel.getComponent(0));
            removeSongFromName(songNameButton.getText());
            songNameButton.setColor(panelColor);
            songNameButton.setForeground(panelColor.darker());
        }
        Collections.fill(isSelected, false);
        System.out.println(selectedSongs);
        repaint();
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