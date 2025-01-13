package gui;
import contents.MusicContent;
import general.MusicPlayerFrame;
import general.Util;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Playlist extends JPanel {
    private String title;
    private int cornerRadius;
    private Color panelColor;
    private List<String> allSongNames = new ArrayList<>();
    private MusicPlayerFrame frame;
    private List<String> currentNames = new ArrayList<>();
    private boolean main;
    private List<Boolean> settingsOpened = new ArrayList<>();
    public static int playlistCounter = 0;

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


        if(!this.main) {
            JPanel playlistUpperComponents = new JPanel();
            playlistUpperComponents.setLayout(new BorderLayout());
            playlistUpperComponents.setOpaque(false);

            JLabel playlistNameLabel = new JLabel(this.title, JLabel.CENTER);
            playlistNameLabel.setFont(Util.headerFont);
            playlistNameLabel.setForeground(Util.orange_color.darker());
            playlistUpperComponents.add(playlistNameLabel, BorderLayout.WEST);


            RoundButton addButton = new RoundButton(
                    "+",panelColor,20,20);
            addButton.setFont(Util.headerFont);
            addButton.setFocusable(false);
            addButton.addActionListener(e -> addNewSongsIntoPlaylist());
            playlistUpperComponents.add(addButton, BorderLayout.CENTER);

            RoundButton deleteButton = new RoundButton(
                    "❌",panelColor,20,20);
            deleteButton.setFont(Util.myFont);
            deleteButton.setFocusable(false);
            deleteButton.addActionListener(e -> deletePlaylist());
            playlistUpperComponents.add(deleteButton, BorderLayout.EAST);

            add(playlistUpperComponents);
        }

        addNewRecords(currentNames);
        playlistCounter++;
    }


    private void addNewSongsIntoPlaylist() {
        if (getRemainingSongNames().size() == 0) {
            new WarningFrame("Playlist Full","There aren't any songs to add!");
        } else {
            MusicContent mc = (MusicContent)this.frame.getContent(Util.MUSIC_CONTENT);
            mc.clearMusicContent();

            JDialog songSelectorFrame = new JDialog(
                    this.frame,"Song Selector for " + this.title,true);
            songSelectorFrame.setSize(426, 400);
            songSelectorFrame.setLayout(null);
            songSelectorFrame.setLocationRelativeTo(null);

            JPanel songSelPanel = new JPanel();
            songSelPanel.setBounds(0,0,songSelectorFrame.getWidth(),songSelectorFrame.getHeight());
            songSelPanel.setLayout(null);
            songSelPanel.setOpaque(true);
            songSelPanel.setBackground(Util.blue_color.brighter());

            SongSelector songSelector = new SongSelector(Util.orange_color,
                    Util.orange_dark_color,20,getRemainingSongNames());
            JScrollPane sp = Util.createScrollPane(
                    songSelector,new Rectangle(5,5,400,300),
                    Util.blue_color,getBackground());

            RoundButton confirmButton = new RoundButton(
                    "Confirm",panelColor,20,20);
            confirmButton.setBounds(255,325,150,25);
            confirmButton.setFont(Util.myFont);
            confirmButton.setFocusable(false);
            confirmButton.addActionListener(ev -> {
                List<String> newSongNames = new ArrayList<>();
                newSongNames.addAll(songSelector.getSelectedSongs());
                if (!newSongNames.isEmpty()) {
                    currentNames.addAll(newSongNames);
                    addNewRecords(newSongNames);
                    frame.fr.addPlaylistSongs(frame.user.getEmail(),this.title,newSongNames);
                    update();
                    songSelectorFrame.dispose();
                } else {
                    new WarningFrame("No song chosen",
                            "Please choose at least one song!");
                }
            });

            RoundButton cancelButton = new RoundButton(
                    "Cancel",panelColor,20,20);
            cancelButton.setBounds(5,325,150,25);
            cancelButton.setFont(Util.myFont);
            cancelButton.setFocusable(false);
            cancelButton.addActionListener(ev -> songSelectorFrame.dispose());

            songSelPanel.add(sp);
            songSelPanel.add(confirmButton);
            songSelPanel.add(cancelButton);
            songSelectorFrame.add(songSelPanel);

            songSelectorFrame.setVisible(true);
        }
    }

    private void deletePlaylist() {
        MusicContent mc = (MusicContent)frame.getContent(Util.MUSIC_CONTENT);
        for (int i = 0; i < frame.getAllPlaylists().size(); i++) {
            frame.getAllPlaylists().get(i).setVisible(false);
        }
        mc.clearMusicContent();
        Playlist pl = frame.getPlaylistFromPlaylistTitle("Main");
        pl.setVisible(true);
        frame.setCurrentPlaylistNames(pl.getCurrentNames());
        frame.setCurSong(0);
        frame.setCurPlaylist(pl);
        frame.getCurPlaylist().checkHearts();
        frame.getCurPlaylist().setRecordBackgroundColor(Util.orange_dark_color,frame.getCurSongNum());

        for (int i = 0; i < frame.getAllPlaylists().size(); i++) {
            if (((Playlist)frame.getAllPlaylists().get(i).getViewport().getView())
                    .getTitle().equals(title)) {
                for (int j = 0; j < mc.getComponents().length; j++) {
                    if (mc.getComponents()[j].getClass().getName().equals("JScrollPane")) {
                        JScrollPane scrollPane = (JScrollPane)mc.getComponents()[j];
                        Playlist playlist = (Playlist)scrollPane.getViewport().getView();
                        if (playlist.getTitle().equals(title)) {
                            mc.remove(j);
                            break;
                        }
                    }
                }
                frame.getAllPlaylists().remove(i);
                frame.getPlaylistSelector().removeItem(title);
                frame.fr.deletePlaylist(frame.user.getEmail(),title);
                break;
            }
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
        RoundButton nameButton;
        int begin = 0;
        if (main) {
            nameButton = (RoundButton)(((JPanel)getComponents()[num]).getComponent(0));
        } else {
            begin = 1;
            nameButton = (RoundButton)(((JPanel)getComponents()[num+1]).getComponent(0));
        }
        for (int i = begin; i < getComponents().length; i++) {
            RoundButton button = (RoundButton)(((JPanel)getComponents()[i]).getComponent(0));
            button.setColor(panelColor);
        }
        nameButton.setColor(color);
        validate();
        repaint();
    }

    public void checkHearts() {
        int index = 0;
        if (!main) {
            index = 1;
        }
        for (int i = index; i < currentNames.size(); i++) {
            RoundButton nameButton = (RoundButton)(((JPanel)getComponents()[i]).getComponent(0));
            RoundButton likeButton = (RoundButton)(((JPanel)((JPanel)getComponents()[i]).getComponent(1)).getComponent(0));
            for (int j = 0; j < frame.getAllSongNames().size(); j++) {
                if (nameButton.getText().equals(frame.getAllSongNames().get(j))) {
                    if (!frame.getAllSongs().get(j).isHearted()) {
                        likeButton.setText("♡");
                    } else {
                        likeButton.setText("♥");
                    }
                }
            }
        }
        validate();
        repaint();
    }

    public void update() {
        frame.setCurrentPlaylistNames(currentNames);
        frame.setCurSong(0);
        if (frame.getCurPlaylist() != null) {
            frame.getCurPlaylist().checkHearts();
            frame.getCurPlaylist().setRecordBackgroundColor(Util.orange_dark_color,frame.getCurSongNum());
        }
        validate();
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

    public List<String> getRemainingSongNames() {
        List<String> remainingSongNames = new ArrayList<>();
        for (int i = 0; i < allSongNames.size(); i++) {
            if (!currentNames.contains(allSongNames.get(i))) {
                remainingSongNames.add(allSongNames.get(i));
            }
        }
        return remainingSongNames;
    }

    public void addNewRecords(List<String> newSongNames) {
        for (int i = 0; i < newSongNames.size(); i++) {
            JPanel record = new JPanel();
            record.setLayout(new BorderLayout());
            record.setOpaque(false);

            RoundButton name = new RoundButton(
                    newSongNames.get(i), panelColor, 20, 20);
            name.setFont(Util.myFont);
            name.setFocusable(false);
            name.addActionListener(e -> {
                RoundButton nameButton = (RoundButton) e.getSource();
                this.frame.goTo(nameButton.getText());
            });
            name.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }
            });
            record.add(name, BorderLayout.WEST);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BorderLayout());
            buttonPanel.setOpaque(false);

            String likeName = "♡";
            for (int j = 0; j < frame.getAllSongs().size(); j++) {
                if (newSongNames.get(i).equals(frame.getAllSongs().get(j).getName())) {
                    if (frame.getAllSongs().get(j).isHearted()) {
                        likeName = "♥";
                    }
                }
            }

            RoundButton like = new RoundButton(
                    likeName, panelColor, 20, 20);

            like.setFont(Util.myFont);
            like.setFocusable(false);
            like.addActionListener(e -> addRemoveHeart(e));
            buttonPanel.add(like, BorderLayout.WEST);

            if (this.main) {
                RoundButton settingsButton = new RoundButton(
                        "⋮", panelColor, 20, 20);
                settingsButton.setFont(Util.songNameFont);
                settingsButton.setFocusable(false);
                settingsButton.addActionListener(e -> addRemoveSettings(e));
                settingsOpened.add(false);
                buttonPanel.add(settingsButton, BorderLayout.EAST);
            } else {
                RoundButton removeButton = new RoundButton(
                        "-", panelColor, 20, 20);
                removeButton.setFont(Util.songNameFont);
                removeButton.setFocusable(false);
                removeButton.addActionListener(e -> removeSongFromPlaylist(e));
                buttonPanel.add(removeButton, BorderLayout.EAST);
            }

            record.add(buttonPanel, BorderLayout.EAST);
            add(record);
            /*if (playlistCounter == 0) {
                validate();
                repaint();
            } else {
                update();
            }*/
        }
    }

    private void addRemoveHeart(ActionEvent e) {
        RoundButton likeButton = (RoundButton)e.getSource();
        JPanel bPanel = (JPanel)likeButton.getParent();
        JPanel oldRecord = (JPanel)bPanel.getParent();
        RoundButton nameButton = (RoundButton)oldRecord.getComponent(0);
        for (int j = 0; j < this.frame.getAllSongs().size(); j++) {
            //System.out.println(likeButton.getText()+" = "+frame.getAllSongs().get(j).getName());
            if (nameButton.getText().equals(this.frame.getAllSongs().get(j).getName())) {
                if (!this.frame.getAllSongs().get(j).isHearted()) {
                    likeButton.setText("♥");
                    this.frame.getAllSongs().get(j).setHearted(true);
                    frame.fr.updateLoved(frame.user.getEmail(),nameButton.getText(),true);
                } else {
                    likeButton.setText("♡");
                    this.frame.getAllSongs().get(j).setHearted(false);
                    frame.fr.updateLoved(frame.user.getEmail(),nameButton.getText(),false);
                }
            }
        }
    }

    private void addRemoveSettings(ActionEvent e) {
        RoundButton sButton = (RoundButton)e.getSource();
        JPanel bPanel = (JPanel)sButton.getParent();
        JPanel oldRecord = (JPanel)bPanel.getParent();
        RoundButton nameButton = (RoundButton)oldRecord.getComponent(0);
        boolean somethingIsOpen = false;
        for (int j = 0; j < settingsOpened.size(); j++) {
            if (settingsOpened.get(j)) {
                somethingIsOpen = true;
            }
        }
        int pos = getPositionFromName(nameButton.getText());
        SettingsDropDown settingsDropDown = new SettingsDropDown(
                Util.blue_color,this.cornerRadius,nameButton.getText(),this.frame);
        Rectangle rec = this.frame.getCurPlaylist().getBounds();
        settingsDropDown.setBounds(rec.width + 200,rec.y + 50,100,200);
        MusicContent mc = (MusicContent)this.frame.getContent(Util.MUSIC_CONTENT);
        if (!settingsOpened.get(pos)) {
            if (!somethingIsOpen) {
                mc.add(settingsDropDown);
                mc.repaint();
                settingsOpened.set(pos,!settingsOpened.get(pos));
            }
        } else {
            if (somethingIsOpen) {
                closeSettingsDropDown();
                settingsOpened.set(pos,!settingsOpened.get(pos));
            }
        }
    }

    public void closeSettingsDropDown() {
        MusicContent mc = (MusicContent)this.frame.getContent(Util.MUSIC_CONTENT);
        for (int k = 0; k < mc.getComponents().length; k++) {
            if (mc.getComponents()[k].getClass().getName().equals("gui.SettingsDropDown")) {
                mc.remove(k);
                break;
            }
        }
        mc.repaint();
    }

    private void removeSongFromPlaylist(ActionEvent e) {
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
            MusicContent mc = (MusicContent)this.frame.getContent(Util.MUSIC_CONTENT);
            mc.clearMusicContent();
            int pos = getPositionFromName(nameButton.getText());
            this.currentNames.remove(pos);
            remove(pos+1);
            update();
            List<String> songsForDeletion = new ArrayList<>();
            songsForDeletion.add(nameButton.getText());
            frame.fr.removePlaylistSongs(frame.user.getEmail(),this.title,songsForDeletion);
        }
    }

    public String getTitle() { return title; }

    public List<String> getCurrentNames() { return currentNames; }

    public void addNewNames(List<String> newSongs) {
        currentNames.addAll(newSongs);
    }
}