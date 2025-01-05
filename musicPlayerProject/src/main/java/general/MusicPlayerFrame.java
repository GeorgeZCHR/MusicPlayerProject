package general;
import components.FirestoreManager;
import components.SongPlayer_Agg;
import components.User;
import containers.*;
import contents.*;
import gui.*;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.ArrayList;

public class MusicPlayerFrame extends JFrame {
    private int currentContent = Util.OPENING_CONTENT;
    private int width, realW, height, realH;
    public User user;
    public FirestoreManager fr;
    private JPanel header = new JPanel(null), menu = new JPanel(null);
    private JPanel footer = new JPanel(null);
    private List<Song> allSongs = new ArrayList<>();
    private List<String> allSongsNames = new ArrayList<>();
    private Timer timer, sliderUpdateTimer;
    private Image image = (new ImageIcon("img/GIAM_Icon_AcademyOfMusic_RGB.png")).getImage();
    private int menuOptionCounter = 0;
    private int totalSeconds = 0;
    private int songSliderLength = 10000000;
    private List<JScrollPane> allPlaylists = new ArrayList<>();
    private List<String> currentPlaylistNames = new ArrayList<>();
    private List<Song> currentPLSongs = new ArrayList<>();
    private Playlist curPlaylist;
    private Song currentSong;
    private int currentSongNum = 0;
    //---Header---
    private RoundButton menuShower = new RoundButton("≡",Util.orange_color,20,20);
    private boolean showing = true;
    private JComboBox playlistSelector;
    private CustomTextField searchBar = new CustomTextField(Util.orange_color,20,20);
    //----------------
    //---Menu---
    private RectButton createPlaylist = new RectButton("Create Playlist",Util.orange_color);
    private RectButton musicContentButton = new RectButton("Music",Util.orange_color);
    private RectButton searchArtistBio = new RectButton("Search Artist Bio",Util.orange_color);
    private RectButton discoverTopArtists = new RectButton("Discover Top Artists",Util.orange_color);
    private RectButton discoverTopTracks = new RectButton("Discover Top Tracks",Util.orange_color);
    private RectButton discoverTopAlbums = new RectButton("Discover Top Albums",Util.orange_color);
    //----------------
    //---Contents---
    private OpeningContent openingContent = new OpeningContent(null,this);
    private MusicContent musicContent = new MusicContent(null,this);
    private CreatePlaylistContent createPLContent = new CreatePlaylistContent(null,this);
    private BioContent bioContent = new BioContent(null);
    private TopArtistsContent topArtistsContent = new TopArtistsContent(null);
    private TopTracksContent topTracksContent = new TopTracksContent(null);
    private TopAlbumsContent topAlbumsContent = new TopAlbumsContent(null);
    private SearchResultsContent searchResultsContent = new SearchResultsContent(null,this);
    //----------------
        public MusicPlayerFrame(User user, FirestoreManager fr, int width, int height) {
            this.user = user;
            this.fr = fr;
        this.width = width;
        this.height = height;
        realW = width - 16;  // For Windows 10
        realH = height - 39; // For Windows 10

        this.setTitle("MusicPlayer");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(this.width,this.height);
        this.setLayout(null);
        this.setIconImage(image);

        //---Header, Footer, Menu, Contents---
        header.setBounds(0,0,realW,50);
        header.setBackground(Util.blue_dark_color);

        footer.setBounds(0,realH - header.getHeight(),realW,50);
        footer.setBackground(Util.blue_dark_color);

        menu.setBounds(0,header.getHeight(),250,footer.getY());
        menu.setBackground(Util.blue_color);

        createContent(openingContent,Util.blue_color.brighter(),true);
        createContent(musicContent,Util.blue_color.brighter(),false);
        createContent(bioContent,Util.blue_color.brighter(),false);
        createContent(createPLContent,Util.blue_color.brighter(),false);
        createContent(topArtistsContent,Util.blue_color.brighter(),false);
        createContent(topTracksContent,Util.blue_color.brighter(),false);
        createContent(topAlbumsContent,Util.blue_color.brighter(),false);
        createContent(searchResultsContent,Util.blue_color.brighter(),false);

        loadSongs("music/");
        fillAllSongsNames();
        setCurrentPlaylistNames(allSongsNames);
        currentSong = currentPLSongs.get(currentSongNum);

        //---Menu Options---
        createMenuOption(musicContentButton,Util.MUSIC_CONTENT);
        createMenuOption(createPlaylist,Util.CREATE_PLAYLIST_CONTENT);
        createMenuOption(searchArtistBio,Util.BIO_CONTENT);
        createMenuOption(discoverTopArtists,Util.TOP_ARTISTS_CONTENT);
        createMenuOption(discoverTopTracks,Util.TOP_TRACKS_CONTENT);
        createMenuOption(discoverTopAlbums,Util.TOP_ALBUMS_CONTENT);

        //---Contents---
        initContents();

        //---Header Options---
        menuShower.setBounds((int)(header.getWidth() * 0.02),(int)(header.getHeight() * 0.1),
                (int)(header.getWidth() * 0.06),(int)(header.getHeight() * 0.8));
        menuShower.setFocusable(false);
        menuShower.setFont(Util.headerFont);
        menuShower.addActionListener(e -> changeMenu());

        //---Search Bar---
        searchBar.setBounds((int)(header.getWidth() * 0.1),(int)(header.getHeight() * 0.1),
                (int)(header.getWidth() * 0.18),(int)(header.getHeight() * 0.8));
        searchBar.setFont(Util.myFont);
        searchBar.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    System.out.println("Search Bar Text : " + searchBar.getText());
                    SongPlayer_Agg player = new SongPlayer_Agg();
                    JSONObject results = player.searchArtist(searchBar.getText());
                    //System.out.println(results);
                    List<TrackFromAPI> tracksFromAPI = new ArrayList<>();
                    if (results != null) {
                        JSONArray tracks = results.getJSONArray("data");
                        for (int i = 0; i < tracks.length(); i++) {
                            JSONObject track = tracks.getJSONObject(i);
                            if (Util.isUnderHour(track.getInt("duration"))) {
                                tracksFromAPI.add(new TrackFromAPI(
                                        track.getString("title"),
                                        track.getJSONObject("user").getString("name"),
                                        track.getString("id"),
                                        track.getString("orig_filename"),
                                        track.getInt("duration")
                                        )
                                );
                            }
                        }

                        showContent(Util.SEARCH_RESULTS_CONTENT);

                        List<String> songsWithArtists = new ArrayList<>();
                        for (TrackFromAPI trackFromAPI : tracksFromAPI) {
                            songsWithArtists.add(trackFromAPI.getTitle() + " by " +
                                    trackFromAPI.getArtist() + " [" +
                                    Util.getDurationInHumanTime(trackFromAPI.getDuration()) + "]");
                        }

                        for (int i = 0; i < searchResultsContent.getComponents().length; i++) {
                            if (searchResultsContent.getComponents()[i] instanceof JScrollPane) {
                                JScrollPane sp = (JScrollPane)searchResultsContent.getComponents()[i];
                                SongSelector songSelector = ((SongSelector)sp.getViewport().getView());
                                songSelector.clearAll();
                                songSelector.addSongs(songsWithArtists);
                                searchResultsContent.setTracksFromAPI(tracksFromAPI);
                                break;
                            }
                        }
                    }
                    searchBar.setText("");
                }
            }
        });

        //---Playlist Selector ComboBox---
        playlistSelector = new JComboBox();
        playlistSelector.setBounds((int)(header.getWidth() * 0.74),(int)(header.getHeight() * 0.1),
                (int)(header.getWidth() * 0.24),(int)(header.getHeight() * 0.8));
        playlistSelector.addActionListener(e -> {
            for (int i = 0; i < allPlaylists.size(); i++) {
                allPlaylists.get(i).setVisible(false);
            }
           musicContent.clearMusicContent();
            for (int i = 0; i < allPlaylists.size(); i++) {
                Playlist pl = (Playlist)(allPlaylists.get(i).getViewport().getView());
                if (pl.getTitle().equals(playlistSelector.getSelectedItem())) {
                    allPlaylists.get(i).setVisible(true);
                    setCurrentPlaylistNames(pl.getCurrentNames());
                    setCurSong(0);
                    setCurPlaylist(pl);
                    getCurPlaylist().checkHearts();
                    getCurPlaylist().setRecordBackgroundColor(Util.orange_dark_color,getCurSongNum());
                }
            }
        });
        playlistSelector.addItem(musicContent.mainPlaylist.getTitle());

        //---Footer---
        // todo

        // Create a timer that updates every 1000 milliseconds (1 second)
        timer = new Timer(1000, e -> {
            musicContent.getSongNameLabel().setText(currentSong.getName());
        });
        timer.start();

        //---Add Components---
        addComponents();

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void goTo(String song) {
        musicContent.goTo(song);
    }

    private void changeMenu() {
        JPanel content = getCurrentContent();
        if (content == null) {
            return;
        }
        if (showing) {
            menuShower.setText("⦀");
            menu.setVisible(false);
            content.setBounds(
                    0,header.getHeight(),realW,
                    realH - (header.getHeight() + footer.getHeight()));
        } else {
            menuShower.setText("≡");
            menu.setVisible(true);
            content.setBounds(
                    menu.getWidth(),header.getHeight(),
                    realW - menu.getWidth(),
                    realH - (header.getHeight() + footer.getHeight()));
        }
        showing = !showing;
    }

    private JPanel getCurrentContent() {
        switch (currentContent) {
            case Util.OPENING_CONTENT:
                return openingContent;
            case Util.MUSIC_CONTENT:
                return musicContent;
            case Util.CREATE_PLAYLIST_CONTENT:
                return createPLContent;
            case Util.BIO_CONTENT:
                return bioContent;
            case Util.TOP_ARTISTS_CONTENT:
                return topArtistsContent;
            case Util.TOP_TRACKS_CONTENT:
                return topTracksContent;
            case Util.TOP_ALBUMS_CONTENT:
                return topAlbumsContent;
        }
        return null;
    }

    public JPanel getContent(int contentNum) {
        switch (contentNum) {
            case Util.OPENING_CONTENT:
                return openingContent;
            case Util.MUSIC_CONTENT:
                return musicContent;
            case Util.CREATE_PLAYLIST_CONTENT:
                return createPLContent;
            case Util.BIO_CONTENT:
                return bioContent;
            case Util.TOP_ARTISTS_CONTENT:
                return topArtistsContent;
            case Util.TOP_TRACKS_CONTENT:
                return topTracksContent;
            case Util.TOP_ALBUMS_CONTENT:
                return topAlbumsContent;
            case Util.SEARCH_RESULTS_CONTENT:
                return searchResultsContent;
        }
        return null;
    }

    public void fillAllSongsNames() {
        allSongsNames.clear();
        for (int i = 0; i < allSongs.size(); i++) {
            allSongsNames.add(allSongs.get(i).getName());
        }
    }
    public void setCurrentPlaylistNames(List<String> songNames) {
        currentPlaylistNames = new ArrayList<>(songNames);
        currentPLSongs.clear();
        for (int i = 0; i < currentPlaylistNames.size(); i++) {
            boolean found = false;
            for (int j = 0; j < allSongs.size() && !found; j++) {
                if (currentPlaylistNames.get(i).equals(allSongs.get(j).getName())) {
                    currentPLSongs.add(allSongs.get(j));
                    found = true;
                }
            }
        }
    }

    public int getSongNameNum(String songName) {
        for (int i = 0; i < currentPlaylistNames.size(); i++) {
            if (currentPlaylistNames.get(i).contains(songName)) {
                return i;
            }
        }
        return -1;
    }

    private void createMenuOption(JButton button, int content) {
        button.setBounds(0,menuOptionCounter * 50,250,50);
        button.setFocusable(false);
        button.setFont(Util.myFont);
        button.addActionListener(e -> showContent(content));
        menuOptionCounter++;
    }

    private void createContent(JPanel panel,Color color, boolean visible) {
        panel.setBounds(
                menu.getWidth(),header.getHeight(),
                realW - menu.getWidth(),
                realH - (header.getHeight() + footer.getHeight()));
        panel.setBackground(color);
        panel.setVisible(visible);
    }

    public void showContent(int content) {
        openingContent.setVisible(false);
        musicContent.setVisible(false);
        createPLContent.setVisible(false);
        bioContent.setVisible(false);
        topArtistsContent.setVisible(false);
        topTracksContent.setVisible(false);
        topAlbumsContent.setVisible(false);
        searchResultsContent.setVisible(false);
        musicContent.clearMusicContent();
        switch (content) {
            case Util.MUSIC_CONTENT:
                musicContent.setVisible(true);
                currentContent = Util.MUSIC_CONTENT;
                break;
            case Util.CREATE_PLAYLIST_CONTENT:
                createPLContent.setVisible(true);
                currentContent = Util.CREATE_PLAYLIST_CONTENT;
                break;
            case Util.BIO_CONTENT:
                bioContent.setVisible(true);
                currentContent = Util.BIO_CONTENT;
                break;
            case Util.TOP_ARTISTS_CONTENT:
                topArtistsContent.setVisible(true);
                currentContent = Util.TOP_ARTISTS_CONTENT;
                break;
            case Util.TOP_TRACKS_CONTENT:
                topTracksContent.setVisible(true);
                currentContent = Util.TOP_TRACKS_CONTENT;
                break;
            case Util.TOP_ALBUMS_CONTENT:
                topAlbumsContent.setVisible(true);
                currentContent = Util.TOP_ALBUMS_CONTENT;
                break;
            case Util.SEARCH_RESULTS_CONTENT:
                searchResultsContent.setVisible(true);
                currentContent = Util.SEARCH_RESULTS_CONTENT;
                break;
        }
    }

    public void initContents() {
        openingContent.init();
        musicContent.init();
        createPLContent.init();
        bioContent.init();
        topArtistsContent.init();
        topTracksContent.init();
        topAlbumsContent.init();
        searchResultsContent.init();
    }

    public void addComponents() {
        header.add(menuShower);
        header.add(playlistSelector);
        header.add(searchBar);

        menu.add(musicContentButton);
        menu.add(createPlaylist);
        menu.add(searchArtistBio);
        menu.add(discoverTopArtists);
        menu.add(discoverTopTracks);
        menu.add(discoverTopAlbums);

        this.add(header);
        this.add(footer);
        this.add(menu);
        this.add(openingContent);
        this.add(musicContent);
        this.add(createPLContent);
        this.add(bioContent);
        this.add(topArtistsContent);
        this.add(topTracksContent);
        this.add(topAlbumsContent);
        this.add(searchResultsContent);
    }

    public void loadSongs(String folderPath) {
        List<String> pathsOfSongs = new ArrayList(SongLoader.loadFromFolder(folderPath));
        for (String path : pathsOfSongs) {
            if (    path.substring(path.lastIndexOf(".")).equals(".wav") ||
                    path.substring(path.lastIndexOf(".")).equals(".au")){
                allSongs.add(new Song(path,folderPath));
            } else {
                System.out.println("Unknown Path: " + path);
            }
        }
    }

    public int getCurSongNum() {
        return currentSongNum;
    }
    public void setCurSong(int num) {
        System.out.println("Num : " + num);
        currentSongNum = num;
        currentSong = currentPLSongs.get(num);
    }

    public List<String> getAllSongNames() {
        return allSongsNames;
    }
    public List<Song> getAllSongs() { return allSongs; }
    public List<JScrollPane> getAllPlaylists() {
        return allPlaylists;
    }
    public JComboBox getPlaylistSelector() { return playlistSelector; }
    public Playlist getCurPlaylist() {
        return curPlaylist;
    }
    public void setCurPlaylist(Playlist pl) {
        curPlaylist = pl;
    }
    public Song getCurrentSong() {
        return currentSong;
    }
    public List<Song> getCurrentPLSongs() {
        return currentPLSongs;
    }
    public User getUser() { return user; }
    public Playlist getPlaylistFromPlaylistTitle(String playlistName) {
        for (int i = 0; i < allPlaylists.size(); i++) {
            Playlist pl = (Playlist)(allPlaylists.get(i).getViewport().getView());
            if (pl.getTitle().equals(playlistName)) {
                return pl;
            }
        }
        return null;
    }
}