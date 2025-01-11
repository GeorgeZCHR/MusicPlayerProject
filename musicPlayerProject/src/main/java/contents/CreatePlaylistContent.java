package contents;
import general.MusicPlayerFrame;
import general.Util;
import gui.*;
import javax.swing.*;
import java.awt.*;

public class CreatePlaylistContent extends JPanel implements Content {
    private MusicPlayerFrame mpf;
    public JLabel playlistNameLabel;
    public CustomTextField playlistNameText;
    public JLabel jLabel;
    public JScrollPane sp;
    public RoundButton create;
    public RoundButton clear;

    public CreatePlaylistContent(LayoutManager layout, MusicPlayerFrame mpf) {
        super(layout);
        this.mpf = mpf;
    }
    @Override
    public void init() {
        //---Label---
        playlistNameLabel = new JLabel("Playlist Name : ");
        playlistNameLabel.setBounds((int)(0.02 * getWidth()),
                (int)(0.02 * getHeight()), 150, 50);
        playlistNameLabel.setFont(Util.myFont);
        playlistNameLabel.setForeground(Util.blue_dark_color);

        //---TextField---
        playlistNameText = new CustomTextField(Util.orange_color,20,20);
        playlistNameText.setBounds((int)(0.5 * getWidth()) - 245,
                (int)(0.02 * getHeight()), 490, 50);
        playlistNameText.setFont(Util.myFont);

        jLabel = new JLabel();
        jLabel.setBounds((int)(0.02 * getWidth()),
                (int)(0.1 * getHeight()),
                (int)(0.96 * getWidth()), 50);

        //---Song Selector for Playlist---
        SongSelector songSelectorForPlaylist = new SongSelector(Util.orange_color,
                Util.orange_dark_color,20,mpf.getAllSongNames());

        sp = Util.createScrollPane(songSelectorForPlaylist,new Rectangle((int)(0.02 * getWidth()),
                        70,(int)(0.93 * getWidth()),(int)(0.76 * getHeight())),
                Util.blue_color,getBackground());

        //---Create Button---
        create = new RoundButton("Create",Util.orange_color,
                40,40);
        create.setBounds((int)(0.98 * getWidth() - 120),
                (int)(0.98 * getHeight() - 50), 120, 50);
        create.setFont(Util.myFont);
        create.addActionListener(e -> {
            if (!playlistNameText.getText().equals("")) {
                if (!songSelectorForPlaylist.getSelectedSongs().isEmpty()) {
                    Playlist playlist = new Playlist(playlistNameText.getText(),
                            Util.orange_color,20,
                            songSelectorForPlaylist.getSelectedSongs(),mpf, false);
                    playlist.setRecordBackgroundColor(Util.orange_dark_color,
                            mpf.getCurSongNum());
                    playlist.checkHearts();


                    JScrollPane jScrollPane = Util.createScrollPane(playlist,new Rectangle(
                                    (int)(mpf.getContent(Util.MUSIC_CONTENT).getWidth() * 0.5) - 250,
                                    (int)(mpf.getContent(Util.MUSIC_CONTENT).getHeight() * 0.01),
                                    500,300),
                            Util.blue_color,mpf.getContent(Util.MUSIC_CONTENT).getBackground());
                    jScrollPane.setVisible(false);

                    mpf.getContent(Util.MUSIC_CONTENT).add(jScrollPane);
                    mpf.getAllPlaylists().add(jScrollPane);
                    mpf.getPlaylistSelector().addItem(playlist.getTitle());

                    playlistNameText.setText("");
                    songSelectorForPlaylist.clearSelectedSongs();
                    WarningFrame wf = new WarningFrame("Playlist created",
                            "New playlist " + playlistNameText.getText() + " was created!");
                } else {
                    WarningFrame wf = new WarningFrame("No song chosen",
                            "Please choose at least one song!");
                }
            } else {
                WarningFrame wf = new WarningFrame("No playlist name Entered",
                        "Please enter a playlist name!");
            }
        });
        create.setFocusable(false);

        //---Clear Button---
        clear = new RoundButton("Clear",Util.orange_color,
                40,40);
        clear.setBounds((int)(0.02 * getWidth()),
                (int)(0.98 * getHeight() - 50), 120, 50);
        clear.setFont(Util.myFont);
        clear.setFocusable(false);
        clear.addActionListener(e -> {
            playlistNameText.setText("");
            songSelectorForPlaylist.clearSelectedSongs();
        });

        add(playlistNameLabel);
        add(playlistNameText);
        add(jLabel);
        add(create);
        add(clear);
        add(sp);
    }
}