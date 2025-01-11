package contents;
import components.SongPlayer_Agg;
import containers.Song;
import containers.TrackFromAPI;
import general.MusicPlayerFrame;
import general.Util;
import gui.RoundButton;
import gui.SongSelector;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class SearchResultsContent extends JPanel implements Content{
    private MusicPlayerFrame mpf;
    public JScrollPane sp;
    public RoundButton clearButton, addButton;
    private List<TrackFromAPI> tracksFromAPI = new ArrayList<>();
    public SearchResultsContent(LayoutManager layout, MusicPlayerFrame mpf) {
        super(layout);
        this.mpf = mpf;
    }

    @Override
    public void init() {
        //---Song Selector For Download---
        SongSelector songSelector = new SongSelector(Util.orange_color,
                Util.orange_dark_color,20,new ArrayList<>());

        sp = Util.createScrollPane(songSelector,new Rectangle(
                (int)(0.02 * getWidth()),(int)(0.02 * getHeight()),
                (int)(0.96 * getWidth()),(int)(0.86 * this.getHeight())
        ),Util.blue_color,getBackground());

        //---Clear Button---
        clearButton = new RoundButton("Clear", Util.orange_color, 20, 20);
        clearButton.setBounds((int)(0.02 * getWidth()),(int)(0.9 * getHeight()),
                (int)(0.47 * getWidth()),(int)(0.08 * this.getHeight()));
        clearButton.setFocusable(false);
        clearButton.setFont(Util.myFont);
        clearButton.addActionListener(e -> songSelector.clearSelectedSongs());

        //---Add Button---
        addButton = new RoundButton("Add", Util.orange_color, 20, 20);
        addButton.setBounds((int)(0.51 * getWidth()),(int)(0.9 * getHeight()),
                (int)(0.47 * getWidth()),(int)(0.08 * this.getHeight()));
        addButton.setFocusable(false);
        addButton.setFont(Util.myFont);
        addButton.addActionListener(e -> {
            // Show the loading content immediately
            mpf.showContent(Util.LOADING_CONTENT);

            // Create a SwingWorker to perform the time-consuming task
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    SongPlayer_Agg playerAgg = new SongPlayer_Agg();
                    List<String> newSongs = new ArrayList<>();

                    for (int i = 0; i < songSelector.getSelectedIndexes().size(); i++) {
                        String id = tracksFromAPI.get(songSelector.getSelectedIndexes().get(i)).getId();
                        String originalTitle = tracksFromAPI.get(songSelector.getSelectedIndexes().get(i)).getOriginalTitle();

                        long startTime = System.nanoTime();
                        String songWithFolder = playerAgg.downloadSongToFolder("music/", id, originalTitle);
                        long endTime = System.nanoTime();
                        System.out.println("Conversion time : " + Util.getDurationInHumanTime((endTime - startTime) / 1000000000));

                        if (songWithFolder != null) {
                            Song newSong = new Song(songWithFolder, "music/");
                            mpf.getAllSongs().add(newSong);
                            newSongs.add(newSong.getName());
                        }
                    }

                    mpf.fillAllSongsNames();
                    mpf.setCurrentPlaylistNames(mpf.getAllSongNames());
                    mpf.setCurSong(0);

                    mpf.getCurPlaylist().addNewNames(newSongs);
                    mpf.getCurPlaylist().addNewRecords(newSongs);
                    mpf.getCurPlaylist().update();
                    songSelector.clearSelectedSongs();

                    CreatePlaylistContent cpc = (CreatePlaylistContent) mpf.getContent(Util.CREATE_PLAYLIST_CONTENT);
                    for (int i = 0; i < cpc.getComponents().length; i++) {
                        if (cpc.getComponents()[i] instanceof JScrollPane) {
                            JScrollPane sp = (JScrollPane) cpc.getComponents()[i];
                            SongSelector ss = ((SongSelector) sp.getViewport().getView());
                            ss.clearAll();
                            ss.addSongs(mpf.getAllSongNames());
                            break;
                        }
                    }
                    return null;
                }

                @Override
                protected void done() {
                    mpf.showContent(Util.MUSIC_CONTENT);
                }
            };
            worker.execute();
        });

        add(sp);
        add(clearButton);
        add(addButton);
    }

    public List<TrackFromAPI> getTracksFromAPI() { return tracksFromAPI; }

    public void setTracksFromAPI(List<TrackFromAPI> tracksFromAPI) {
        this.tracksFromAPI.clear();
        this.tracksFromAPI.addAll(tracksFromAPI);
    }
}