package general;
import components.*;
import containers.*;
import contents.*;
import gui.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.ArrayList;

public class MusicPlayerFrame extends JFrame {
    public final int OPENING_CONTENT = -1;
    public final int MUSIC_CONTENT = 0;
    public final int CREATE_PLAYLIST_CONTENT = 1;
    public final int BIO_CONTENT = 2;
    public final int TOP_ARTISTS_CONTENT = 3;
    public final int TOP_TRACKS_CONTENT = 4;
    public final int TOP_ALBUMS_CONTENT = 5;
    private int currentContent = OPENING_CONTENT;
    private int width, realW, height, realH;
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
    //private SongPlayer songPlayer;
    //---Header---
    private CustomButton menuShower = new CustomButton("≡",Util.orange_color,20,20);
    private boolean showing = true;
    //----------------
    //---Menu---
    private CustomButton createPlaylist = new CustomButton("Create Playlist",Util.orange_color,0);
    private CustomButton musicContentButton = new CustomButton("Music",Util.orange_color,0);
    private CustomButton searchArtistBio = new CustomButton("Search Artist Bio",Util.orange_color,0);
    private CustomButton discoverTopArtists = new CustomButton("Discover Top Artists",Util.orange_color,0);
    private CustomButton discoverTopTracks = new CustomButton("Discover Top Tracks",Util.orange_color,0);
    private CustomButton discoverTopAlbums = new CustomButton("Discover Top Albums",Util.orange_color,0);
    //----------------
    //---Content---
    private OpeningContent openingContent = new OpeningContent(null);
    private MusicContent musicContent = new MusicContent(null,this);
    private CreatePlaylistContent createPLContent = new CreatePlaylistContent(null,this);
    private BioContent bioContent = new BioContent(null);
    private TopArtistsContent topArtistsContent = new TopArtistsContent(null);
    private TopTracksContent topTracksContent = new TopTracksContent(null);
    private JPanel topAlbumsContent = new JPanel(null);
    //----------------
    //---Discover Top Albums Content---
    private CustomButton getTopAlbums = new CustomButton(
            "Get Top Albums",Util.orange_color, 20,20);
    //----------------
    public MusicPlayerFrame(int width, int height) {
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

        loadSongs("music/");
        fillAllSongsNames();
        setCurrentPlaylistNames(allSongsNames);
        currentSong = currentPLSongs.get(currentSongNum);

        //---Header Options---
        menuShower.setBounds((int)(header.getWidth() * 0.02),(int)(header.getHeight() * 0.1),
                (int)(header.getWidth() * 0.06),(int)(header.getHeight() * 0.8));
        menuShower.setFocusable(false);
        menuShower.setFont(Util.headerFont);
        menuShower.addActionListener(e -> changeMenu());

        //---Menu Options---
        createMenuOption(musicContentButton,MUSIC_CONTENT);
        createMenuOption(createPlaylist,CREATE_PLAYLIST_CONTENT);
        createMenuOption(searchArtistBio,BIO_CONTENT);
        createMenuOption(discoverTopArtists,TOP_ARTISTS_CONTENT);
        createMenuOption(discoverTopTracks,TOP_TRACKS_CONTENT);
        createMenuOption(discoverTopAlbums,TOP_ALBUMS_CONTENT);

        //---Contents---
        initContents();

        //---Footer---
        // todo

        // Create a timer that updates every 1000 milliseconds (1 second)
        timer = new Timer(1000, e -> {

            musicContent.getSongNameLabel().setText(currentSong.getName());
            /*if (currentSong.isHearted()) {
                heartButton.setIcon(heartRedIcon);

            } else {
                heartButton.setIcon(heartWhiteIcon);
            }*/
            //songSlider.setMaximum(songSliderLength);
            //System.out.println(e.getWhen());
            /*System.out.println("Seconds : " + totalSeconds);
            totalSeconds++;*/
            /*if (clip != null) {
                 if (!isSongStarted) {
                     songSliderLength = clip.getFrameLength();
                     isSongStarted = true;
                 }
                 int frames = clip.getFramePosition();
                 songSliderLength = clip.getFrameLength();
                 songSlider.setValue(frames);
                 System.out.println("Frames of containers.Song : " + frames);
            }*/
        });
        timer.start();

        //---Add Components---
        addComponents();
        //this.add(heartButton);

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void initTopAlbumsContent() {
        //--- show JSon Object to console with the button getTopAlbums ---
        getTopAlbums.setBounds((int)(topAlbumsContent.getWidth() * 0.1),
                (int)(topAlbumsContent.getHeight() * 0.1),
                (int)(topAlbumsContent.getWidth() * 0.2), 50);
        getTopAlbums.setFocusable(false);
        getTopAlbums.addActionListener(e -> {
            GetTopAlbums albums = new GetTopAlbums();
            //JsonObject jsonObject = albums.getTopAlbums();
            //System.out.println(jsonObject);
        });
    }

    /*public void changeFramesOfSong() {
        if (clip != null) {
            int newPosition = songSlider.getValue();
            clip.setFramePosition(newPosition); // Seek to new frame position
            framePosition = newPosition; // Update framePosition to new slider position
        }
        //System.out.println("SongSlider Value : " + songSlider.getValue());
    }*/

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
            case OPENING_CONTENT:
                return openingContent;
            case MUSIC_CONTENT:
                return musicContent;
            case CREATE_PLAYLIST_CONTENT:
                return createPLContent;
            case BIO_CONTENT:
                return bioContent;
            case TOP_ARTISTS_CONTENT:
                return topArtistsContent;
            case TOP_TRACKS_CONTENT:
                return topTracksContent;
            case TOP_ALBUMS_CONTENT:
                return topAlbumsContent;
        }
        return null;
    }

    public JPanel getContent(int contentNum) {
        switch (contentNum) {
            case OPENING_CONTENT:
                return openingContent;
            case MUSIC_CONTENT:
                return musicContent;
            case CREATE_PLAYLIST_CONTENT:
                return createPLContent;
            case BIO_CONTENT:
                return bioContent;
            case TOP_ARTISTS_CONTENT:
                return topArtistsContent;
            case TOP_TRACKS_CONTENT:
                return topTracksContent;
            case TOP_ALBUMS_CONTENT:
                return topAlbumsContent;
        }
        return null;
    }

    private void fillAllSongsNames() {
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

    private void showContent(int content) {
        openingContent.setVisible(false);
        musicContent.setVisible(false);
        createPLContent.setVisible(false);
        bioContent.setVisible(false);
        topArtistsContent.setVisible(false);
        topTracksContent.setVisible(false);
        topAlbumsContent.setVisible(false);
        musicContent.clearMusicContent();
        switch (content) {
            case MUSIC_CONTENT:
                musicContent.setVisible(true);
                currentContent = MUSIC_CONTENT;
                break;
            case CREATE_PLAYLIST_CONTENT:
                createPLContent.setVisible(true);
                currentContent = CREATE_PLAYLIST_CONTENT;
                break;
            case BIO_CONTENT:
                bioContent.setVisible(true);
                currentContent = BIO_CONTENT;
                break;
            case TOP_ARTISTS_CONTENT:
                topArtistsContent.setVisible(true);
                currentContent = TOP_ARTISTS_CONTENT;
                break;
            case TOP_TRACKS_CONTENT:
                topTracksContent.setVisible(true);
                currentContent = TOP_TRACKS_CONTENT;
                break;
            case TOP_ALBUMS_CONTENT:
                topAlbumsContent.setVisible(true);
                currentContent = TOP_ALBUMS_CONTENT;
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
        initTopAlbumsContent();
    }

    public void addComponents() {
        header.add(menuShower);

        menu.add(musicContentButton);
        menu.add(createPlaylist);
        menu.add(searchArtistBio);
        menu.add(discoverTopArtists);
        menu.add(discoverTopTracks);
        menu.add(discoverTopAlbums);

        topAlbumsContent.add(getTopAlbums);

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
    }

    public void loadSongs(String folderPath) {
        List<String> pathsOfSongs = new ArrayList(SongLoader.loadFromFolder(folderPath));
        for (String path : pathsOfSongs) {
            if (path.substring(path.lastIndexOf(".")).equals(".mp3") ||
                    path.substring(path.lastIndexOf(".")).equals(".wav") ||
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
        //songPlayer.changeSong(currentSong);
    }

    public List<String> getAllSongNames() {
        return allSongsNames;
    }
    public List<JScrollPane> getAllPlaylists() {
        return allPlaylists;
    }
    public JComboBox getPlaylistSelector() {
        return musicContent.getPlaylistSelector();
    }
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
}