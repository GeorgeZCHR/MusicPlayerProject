package contents;
import general.MusicPlayerFrame;
import general.Util;
import gui.CustomButton;
import gui.CustomTextField;
import gui.Playlist;
import gui.SongSelector;
import javax.swing.*;
import java.awt.*;

public class CreatePlaylistContent extends JPanel implements Content {
    private MusicPlayerFrame mpf;

    public CreatePlaylistContent(LayoutManager layout, MusicPlayerFrame mpf) {
        super(layout);
        this.mpf = mpf;
    }
    @Override
    public void init() {
        //---Label---
        JLabel playlistNameLabel = new JLabel("Playlist Name : ");
        playlistNameLabel.setBounds((int)(0.02 * getWidth()),
                (int)(0.02 * getHeight()), 150, 50);
        playlistNameLabel.setFont(Util.myFont);
        playlistNameLabel.setForeground(Util.blue_dark_color);

        //---TextField---
        CustomTextField playlistNameText = new CustomTextField(Util.orange_color,20,20);
        playlistNameText.setBounds((int)(0.5 * getWidth()) - 245,
                (int)(0.02 * getHeight()), 490, 50);
        playlistNameText.setFont(Util.myFont);

        JLabel jLabel = new JLabel();
        jLabel.setBounds((int)(0.02 * getWidth()),
                (int)(0.1 * getHeight()),
                (int)(0.96 * getWidth()), 50);

        //---Song Selector for Playlist---
        SongSelector songSelectorForPlaylist = new SongSelector(Util.orange_color,
                Util.orange_dark_color,20,mpf.getAllSongNames());

        JScrollPane sp = Util.createScrollPane(songSelectorForPlaylist,new Rectangle((int)(0.02 * getWidth()),
                        70,(int)(0.93 * getWidth()),(int)(0.76 * getHeight())),
                Util.orange_color,getBackground());

        //---Create Button---
        CustomButton create = new CustomButton("Create",Util.orange_color,40,40);
        create.setBounds((int)(0.98 * getWidth() - 120),
                (int)(0.98 * getHeight() - 50), 120, 50);
        create.setFont(Util.myFont);
        create.addActionListener(e -> {
            if (!playlistNameText.getText().equals("")) {
                if (!songSelectorForPlaylist.getSelectedSongs().isEmpty()) {
                    Playlist playlist = new Playlist(playlistNameText.getText(),
                            Util.orange_color,20,
                            songSelectorForPlaylist.getSelectedSongs(),mpf);
                    playlist.setRecordBackgroundColor(Util.orange_dark_color,
                            mpf.getCurSongNum());
                    playlist.checkHearts();
                    playlist.repaint();


                    JScrollPane jScrollPane = Util.createScrollPane(playlist,new Rectangle(
                            (int)(mpf.getContent(mpf.MUSIC_CONTENT).getWidth() * 0.5) - 250,
                            (int)(mpf.getContent(mpf.MUSIC_CONTENT).getHeight() * 0.01),
                            500,300),
                            Util.orange_color,mpf.getContent(mpf.MUSIC_CONTENT).getBackground());
                    jScrollPane.setVisible(false);

                    mpf.getContent(mpf.MUSIC_CONTENT).add(jScrollPane);
                    mpf.getAllPlaylists().add(jScrollPane);
                    mpf.getPlaylistSelector().addItem(playlist.getTitle());

                    playlistNameText.setText("");
                    songSelectorForPlaylist.clearSelectedSongs();
                    JOptionPane.showMessageDialog(this,
                            "New playlist " + playlistNameText.getText() + " was created!",
                            "Playlist created", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Please choose at least one song!",
                            "No song chosen", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Please enter a playlist name!",
                        "No playlist name Entered", JOptionPane.WARNING_MESSAGE);
            }
        });
        create.setFocusable(false);

        //---Clear Button---
        CustomButton clear = new CustomButton("Clear",Util.orange_color,40,40);
        clear.setBounds((int)(0.02 * getWidth()),
                (int)(0.98 * getHeight() - 50), 120, 50);
        clear.setFont(Util.myFont);
        clear.setFocusable(false);
        clear.addActionListener(e -> songSelectorForPlaylist.clearSelectedSongs());

        add(playlistNameLabel);
        add(playlistNameText);
        add(jLabel);
        add(create);
        add(clear);
        add(sp);
    }
}