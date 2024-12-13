package gui;
import containers.Song;
import contents.MusicContent;
import general.MusicPlayerFrame;
import general.Util;

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
    private boolean main;

    public Playlist(String title, Color panelColor, int cornerRadius,
                    List<String> currentNames, MusicPlayerFrame frame, boolean main) {
        this.main = main;
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

            RoundButton name = new RoundButton(
                    currentNames.get(i),panelColor,20,20);
            name.setFont(Util.myFont);
            name.setFocusable(false);
            name.addActionListener(e -> {
                RoundButton nameButton = (RoundButton)e.getSource();
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

            RoundButton like = new RoundButton(
                    "♡",panelColor,20,20);
            like.setFont(Util.myFont);
            like.setFocusable(false);
            like.addActionListener(e -> {
                RoundButton likeButton = (RoundButton)e.getSource();
                JPanel bPanel = (JPanel)likeButton.getParent();
                JPanel oldRecord = (JPanel)bPanel.getParent();
                RoundButton nameButton = (RoundButton)oldRecord.getComponent(0);
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

            if (this.main) {
                RoundButton settingsButton = new RoundButton(
                        "⋮",panelColor,20,20);
                settingsButton.setFont(Util.songNameFont);
                settingsButton.setFocusable(false);
                settingsButton.addActionListener(e -> {
                    RoundButton sButton = (RoundButton)e.getSource();
                    JPanel bPanel = (JPanel)sButton.getParent();
                    JPanel oldRecord = (JPanel)bPanel.getParent();
                    RoundButton nameButton = (RoundButton)oldRecord.getComponent(0);
                    System.out.println("Settings for: " + nameButton.getText());
                });
                buttonPanel.add(settingsButton, BorderLayout.EAST);
            } else {
                RoundButton removeButton = new RoundButton(
                        "-",panelColor,20,20);
                removeButton.setFont(Util.songNameFont);
                removeButton.setFocusable(false);
                removeButton.addActionListener(e -> {
                    if (this.currentNames.size() <= 1) {
                        new WarningFrame("Song cannot be removed",
                                "The playlist must have at least one song inside!");
                    } else {
                        RoundButton rButton = (RoundButton)e.getSource();
                        JPanel bPanel = (JPanel)rButton.getParent();
                        JPanel oldRecord = (JPanel)bPanel.getParent();
                        RoundButton nameButton = (RoundButton)oldRecord.getComponent(0);
                        System.out.println(nameButton.getText() + " was removed from " + getTitle());
                        //todo a JPanel that will say are you sure?
                        MusicContent mc = (MusicContent)frame.getContent(Util.MUSIC_CONTENT);
                        if (mc.getClip() != null) {
                            mc.getClip().close();
                        }
                        int pos = getPositionFromName(nameButton.getText());
                        records.remove(pos);
                        this.currentNames.remove(pos);
                        remove(pos);
                        update();
                    }
                });
                buttonPanel.add(removeButton, BorderLayout.EAST);
            }

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
            RoundButton button = (RoundButton)(record.getComponent(0));
            button.setColor(panelColor);
        }
        RoundButton nameButton = (RoundButton)(records.get(num).getComponent(0));
        nameButton.setColor(color);
        repaint();
    }

    public void checkHearts() {
        for (int i = 0; i < currentNames.size(); i++) {
            RoundButton nameButton = (RoundButton)(records.get(i).getComponent(0));
            RoundButton likeButton = (RoundButton)(((JPanel)(records.get(i).getComponent(1))).getComponent(0));
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
        repaint();
    }

    public void update() {
        frame.setCurrentPlaylistNames(currentNames);
        frame.setCurSong(0);
        //frame.setCurPlaylist(pl);
        frame.getCurPlaylist().checkHearts();
        frame.getCurPlaylist().setRecordBackgroundColor(Util.orange_dark_color,frame.getCurSongNum());
        repaint();
    }

    public int getPositionFromName(String name) {
        for (int i = 0; i < currentNames.size(); i++) {
            if (currentNames.get(i).equals(name)) {
                return i;
            }
        }
        return -1;
    }

    public String getTitle() { return title; }

    public List<String> getAllSongNames() { return allSongNames; }
}