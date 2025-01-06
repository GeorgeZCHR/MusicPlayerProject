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
    private List<Integer> selectedIndexes = new ArrayList<>();
    private int cornerRadius;
    private final Color panelColor;
    private final Color selectionColor;
    private int counter = 0;

    public SongSelector(Color panelColor, Color selectionColor, int cornerRadius,
                        List<String> names) {
        this.panelColor = panelColor;
        this.selectionColor = selectionColor;
        this.cornerRadius = cornerRadius;
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        addSongs(names);
    }

    public void addSongs(List<String> names) {
        for (int i = counter; i < names.size(); i++) {
            int finalI = i;
            JPanel songPanel = new JPanel();
            songPanel.setLayout(new BorderLayout());
            songPanel.setOpaque(false);

            RoundButton song = new RoundButton(
                    names.get(i),panelColor,20,20);
            song.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
            song.setFocusable(false);
            song.addActionListener(e -> {
                RoundButton songNameButton = (RoundButton)e.getSource();
                if (isSelected.get(finalI)) {
                    removeSongFromName(songNameButton.getText());
                    for(int j = 0; j < selectedIndexes.size(); j++) {
                        if (selectedIndexes.get(j) == finalI) {
                            selectedIndexes.remove(j);
                            break;
                        }
                    }
                    songNameButton.setColor(panelColor);
                    songNameButton.setForeground(panelColor.darker());
                } else {
                    selectedSongs.add(songNameButton.getText());
                    selectedIndexes.add(finalI);
                    songNameButton.setColor(selectionColor);
                }
                isSelected.set(finalI,!isSelected.get(finalI));
                System.out.println(selectedSongs);
            });
            isSelected.add(false);
            songPanel.add(song, BorderLayout.CENTER);

            songs.add(songPanel);
            add(songs.get(finalI));
            counter++;
        }
        repaint();
        revalidate();
    }

    public void clearAll() {
        removeAll();
        songs.clear();
        selectedSongs.clear();
        selectedIndexes.clear();
        counter = 0;
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
            RoundButton songNameButton = (RoundButton)(songPanel.getComponent(0));
            removeSongFromName(songNameButton.getText());
            songNameButton.setColor(panelColor);
            songNameButton.setForeground(panelColor.darker());
        }
        selectedIndexes.clear();
        Collections.fill(isSelected, false);
        System.out.println(selectedSongs);
        repaint();
    }

    public List<String> getSelectedSongs() {
        return selectedSongs;
    }
    public List<Integer> getSelectedIndexes() { return selectedIndexes; }

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