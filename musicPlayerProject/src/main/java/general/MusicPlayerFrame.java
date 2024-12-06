package general;
import components.*;
import containers.*;
import contents.*;
import gui.*;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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
    private JPanel bioContent = new JPanel(null);
    private TopArtistsContent topArtistsContent = new TopArtistsContent(null);
    private JPanel topTracksContent = new JPanel(null), topAlbumsContent = new JPanel(null);
    //----------------
    //---Bio Content---
    private String wikiURL;
    private JTextArea bioTextArea = new JTextArea();
    private JScrollPane bioTextAreaSP;
    private CustomButton viewMoreButton = new CustomButton(
            "View More Bio",Util.orange_color,40,40);
    private JLabel artistLabel = new JLabel("Enter the artist's name:");
    private JTextField artistInput = new JTextField();
    private CustomButton searchBio = new CustomButton(
            "Search",Util.orange_color,40,40);
    private boolean isArtistBioEmpty = true;
    private boolean isArtistBioValid = false;
    //----------------
    //---Discover Top Tracks Content---
    private List<Track> trackList;
    private int topTrackNum = 0;
    private CustomButton topTracksNext = new CustomButton(
            "Next",Util.orange_color, 20,20);
    private CustomButton topTracksBack = new CustomButton(
            "Back",Util.orange_color, 20,20);
    private JLabel topTrackImage, topTrackNumLabel;
    private JLabel topTrackName, topTrackNameLabel;
    private JLabel topTrackPlayCount, topTrackPlayCountLabel;
    private JLabel topTrackListeners, topTrackListenersLabel;
    private JLabel topTrackURL, topTrackURLLabel;
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

    private void initBioContent() {
        //---containers.Artist Label---
        artistLabel.setBounds((int)(0.02 * bioContent.getWidth()),
                (int)(0.1 * bioContent.getHeight()), 200, 50);

        //---containers.Artist Input Text Field---
        artistInput.setBounds((int)(0.02 * bioContent.getWidth()) + 150,
                (int)(0.1 * bioContent.getHeight()), 200, 50);

        //---Search Bio Button---
        searchBio.setFocusable(false);
        searchBio.setBounds((int)(0.02 * bioContent.getWidth()) + 400,
                (int)(0.1 * bioContent.getHeight()), 100, 50);
        searchBio.addActionListener(e -> searchArtistBio());

        //---Bio Text Area---
        bioTextArea.setEnabled(false);
        bioTextArea.setDisabledTextColor(Util.DEFAULT_TEXT_COLOR);
        // Ενεργοποίηση αναδίπλωσης γραμμής
        bioTextArea.setLineWrap(true);
        // Αναδίπλωση ανά λέξη για καλύτερη εμφάνιση
        bioTextArea.setWrapStyleWord(true);
        // Ρυθμιση της γραμματοσειράς για να είναι αναγνώσιμη
        bioTextArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        bioTextAreaSP = new JScrollPane(bioTextArea);
        bioTextAreaSP.setVisible(false);
        bioTextAreaSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        bioTextAreaSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        bioTextAreaSP.setBounds((int)(0.02 * bioContent.getWidth()),
                (int)(0.2 * bioContent.getHeight()), (int)(0.96 * bioContent.getWidth()),
                (int)(0.8 * bioContent.getHeight()));

        //---View More Button---
        viewMoreButton.setVisible(false);
        viewMoreButton.setFocusable(false);
        viewMoreButton.setBounds((int)(0.7 * bioContent.getWidth()),
                (int)(0.1 * bioContent.getHeight()),
                (int)(0.2 * bioContent.getWidth()), 50);
        viewMoreButton.addActionListener(e -> openWikiPage());
    }

    private void initTopTracksContent() {
        //---show JSon Object to console with the button getTopTracks ---
        trackList = new ArrayList<>();
        GetTopTracks topTracks = new GetTopTracks();
        JSONObject jsonObject = topTracks.getTopTracks();

        JSONObject tracks = jsonObject.getJSONObject("tracks");

        //JSONObject attr = tracks.getJSONObject("@attr");

        JSONArray tracksArray = tracks.getJSONArray("track");
        for (int i = 0; i < tracksArray.length(); i++) {
            JSONObject track = tracksArray.getJSONObject(i);

            JSONArray images = track.getJSONArray("image");
            ImageHolder imageHolder = new ImageHolder();
            for (int j = 0; j < images.length(); j++) {
                if (images.getJSONObject(j).getString("size").equals("small")) {
                    imageHolder.setSmallImage(images.getJSONObject(j).getString("#text"));
                } else if (images.getJSONObject(j).getString("size").equals("medium")) {
                    imageHolder.setMediumImage(images.getJSONObject(j).getString("#text"));
                } else if (images.getJSONObject(j).getString("size").equals("large")) {
                    imageHolder.setLargeImage(images.getJSONObject(j).getString("#text"));
                } else if (images.getJSONObject(j).getString("size").equals("extralarge")) {
                    imageHolder.setExtraLargeImage(images.getJSONObject(j).getString("#text"));
                } else if (images.getJSONObject(j).getString("size").equals("mega")) {
                    imageHolder.setMegaImage(images.getJSONObject(j).getString("#text"));
                }
            }

            String streamableText = track.getJSONObject("streamable").getString("#text");
            String streamableFulltrack = track.getJSONObject("streamable").getString("fulltrack");
            String artistMBID = track.getJSONObject("artist").getString("mbid");
            String artistName = track.getJSONObject("artist").getString("name");
            String artistURL = track.getJSONObject("artist").getString("url");

            trackList.add(new Track(
                    track.getString("name"),
                    Long.parseLong(track.getString("playcount")),
                    Long.parseLong(track.getString("listeners")),
                    track.getString("mbid"),
                    track.getString("url"),
                    imageHolder,
                    Long.parseLong(track.getString("duration")),
                    Long.parseLong(streamableText),
                    Long.parseLong(streamableFulltrack),
                    artistMBID,
                    artistName,
                    artistURL
            ));
            //trackList.get(i).print();
        }

        //---Top Track Image---
        URL url;
        BufferedImage image;
        try {
            //System.out.println(trackList.get(topTrackNum).getImageHolder().getMegaImage());
            url = new URL(trackList.get(topTrackNum).getImageHolder().getExtraLargeImage());
            image = ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ImageIcon icon = new ImageIcon(image);
        topTrackImage = new JLabel(icon);
        // Optional: Center align the image in the JLabel
        topTrackImage.setHorizontalAlignment(JLabel.CENTER);
        topTrackImage.setVerticalAlignment(JLabel.CENTER);
        topTrackImage.setBounds((int)(0.02 * topTracksContent.getWidth()),
                (int)(0.05 * topTracksContent.getHeight()),icon.getIconWidth(),
                icon.getIconHeight());

        //---Top Track Number---
        topTrackNumLabel = new JLabel(String.valueOf(topTrackNum + 1));
        topTrackNumLabel.setFont(Util.headerFont);
        topTrackNumLabel.setBounds((int)(0.5 * topTracksContent.getWidth()) - 25,
                (int)(0.9 * topTracksContent.getHeight()),50,50);

        //---Next Button---
        topTracksNext.setFocusable(false);
        topTracksNext.setBounds(
                (int)(0.98 * topTracksContent.getWidth()) - 100,
                (int)(0.9 * topTracksContent.getHeight()),100,50);
        topTracksNext.addActionListener(e -> {
            if (!(topTrackNum + 1 < trackList.size())) {
                topTrackNum = 0;
            } else {
                topTrackNum++;
            }
            setTrack();
        });

        //---Back Button---
        topTracksBack.setFocusable(false);
        topTracksBack.setBounds((int)(0.02 * topTracksContent.getWidth()),
                (int)(0.9 * topTracksContent.getHeight()),100,50);
        topTracksBack.addActionListener(e -> {
            if (!(topTrackNum - 1 > -1)) {
                topTrackNum = trackList.size() - 1;
            } else {
                topTrackNum--;
            }
            setTrack();
        });

        //---Top Track Name---
        topTrackNameLabel = new JLabel("Name          :");
        topTrackNameLabel.setBounds(icon.getIconWidth() + 40,
                (int)(0.05 * topTracksContent.getHeight()),150,50);
        topTrackNameLabel.setFont(Util.myFont);
        topTrackNameLabel.setForeground(Util.blue_dark_color);
        topTrackName = new JLabel(trackList.get(topTrackNum).getName());
        topTrackName.setBounds(icon.getIconWidth() + 190,
                (int)(0.05 * topTracksContent.getHeight()),400,50);
        topTrackName.setFont(Util.myFont);
        topTrackName.setForeground(Util.blue_dark_color);

        //---Top Track Play Count---
        topTrackPlayCountLabel = new JLabel("Play Count :");
        topTrackPlayCountLabel.setBounds(icon.getIconWidth() + 40,
                (int)(0.05 * topTracksContent.getHeight())+50,150,50);
        topTrackPlayCountLabel.setFont(Util.myFont);
        topTrackPlayCountLabel.setForeground(Util.blue_dark_color);
        topTrackPlayCount = new JLabel(String.valueOf(trackList.get(topTrackNum).getPlayCount()));
        topTrackPlayCount.setBounds(icon.getIconWidth() + 190,
                (int)(0.05 * topTracksContent.getHeight())+50,400,50);
        topTrackPlayCount.setFont(Util.myFont);
        topTrackPlayCount.setForeground(Util.blue_dark_color);

        //---Top Track Listeners---
        topTrackListenersLabel = new JLabel("Listeners   :");
        topTrackListenersLabel.setBounds(icon.getIconWidth() + 40,
                (int)(0.05 * topTracksContent.getHeight())+100,150,50);
        topTrackListenersLabel.setFont(Util.myFont);
        topTrackListenersLabel.setForeground(Util.blue_dark_color);
        topTrackListeners = new JLabel(String.valueOf(trackList.get(topTrackNum).getListeners()));
        topTrackListeners.setBounds(icon.getIconWidth() + 190,
                (int)(0.05 * topTracksContent.getHeight())+100,400,50);
        topTrackListeners.setFont(Util.myFont);
        topTrackListeners.setForeground(Util.blue_dark_color);

        //---Top Track URL---
        topTrackURLLabel = new JLabel("URL :");
        topTrackURLLabel.setBounds((int)(0.02 * topTracksContent.getWidth()),
                icon.getIconHeight()+50,100,50);
        topTrackURLLabel.setFont(Util.myFont);
        topTrackURLLabel.setForeground(Util.blue_dark_color);
        topTrackURL = new JLabel(trackList.get(topTrackNum).getUrl());
        topTrackURL.setBounds((int)(0.02 * topArtistsContent.getWidth()) + 100,
                icon.getIconHeight()+50,1200,50);
        topTrackURL.setFont(Util.myFont);
        topTrackURL.setForeground(Util.blue_dark_color);
    }

    private void setTrack() {
        //---Top Track Image---
        URL url;
        BufferedImage image;
        try {
            url = new URL(trackList.get(topTrackNum).getImageHolder().getExtraLargeImage());
            image = ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ImageIcon icon = new ImageIcon(image);
        topTrackImage = new JLabel(icon);
        topTrackImage.setHorizontalAlignment(JLabel.CENTER);
        topTrackImage.setVerticalAlignment(JLabel.CENTER);
        topTrackImage.setBounds((int)(0.02 * topTracksContent.getWidth()),
                (int)(0.05 * topTracksContent.getHeight()),icon.getIconWidth(),
                icon.getIconHeight());

        //---Top Track Number---
        topTrackNumLabel.setText(String.valueOf(topTrackNum + 1));

        //---Top Track Name---
        topTrackName.setText(trackList.get(topTrackNum).getName());

        //---Top Track Play Count---
        topTrackPlayCount.setText(String.valueOf(trackList.get(topTrackNum).getPlayCount()));

        //---Top Track Listeners---
        topTrackListeners.setText(String.valueOf(trackList.get(topTrackNum).getListeners()));

        //---Top Track URL---
        topTrackURL.setText(trackList.get(topTrackNum).getUrl());
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

    public void searchArtistBio() {
        ArtistBioSearcher abs;
        /*String artistName = JOptionPane.showInputDialog(this,
                "Enter the artist's name:",
                "containers.Artist Bio Search", JOptionPane.QUESTION_MESSAGE);*/
        String artistName = artistInput.getText();
        if (!artistName.trim().isEmpty()) {
            isArtistBioEmpty = false;
            wikiURL = "https://en.wikipedia.org/wiki/" + artistName.replace(" ", "_");
            abs = new ArtistBioSearcher(artistName);
            String bio = abs.produceBio();
            if (bio != null) {
                isArtistBioValid = true;
                bioTextArea.setText(bio);
                bioTextAreaSP.setVisible(true);
                viewMoreButton.setVisible(true);
            } else {
                isArtistBioValid = false;
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid artist's name.",
                        "Not Valid containers.Artist Entered", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            isArtistBioEmpty = true;
            JOptionPane.showMessageDialog(this,
                    "Please enter an artist's name.",
                    "No containers.Artist Entered", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void openWikiPage() {
        searchArtistBio();
        //String artist = artistInput.getText();
        /*wikiURL = "https://en.wikipedia.org/wiki/" + artist.replace(" ", "_");*/
        if (isArtistBioEmpty) {
            /*JOptionPane.showMessageDialog(this,
                    "Please enter an artist's name.",
                    "No containers.Artist Entered", JOptionPane.WARNING_MESSAGE);*/
            return;
        }
        if (Desktop.isDesktopSupported()) {
            try {
                if (isArtistBioValid) {
                    Desktop.getDesktop().browse(new URI(wikiURL));
                }
            } catch (IOException | URISyntaxException e) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid artist's name.",
                        "Not Valid containers.Artist Entered", JOptionPane.WARNING_MESSAGE);
                /*JOptionPane.showMessageDialog(this,
                        "Error opening browser: " + e.getMessage(),
                        "Browser Error", JOptionPane.ERROR_MESSAGE);*/
            }
        } else {
            JOptionPane.showMessageDialog(this, "Desktop is not supported on this system.", "Unsupported Operation", JOptionPane.ERROR_MESSAGE);
        }
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
        initBioContent();
        topArtistsContent.init();
        initTopTracksContent();
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

        bioContent.add(artistLabel);
        bioContent.add(artistInput);
        bioContent.add(searchBio);
        bioContent.add(bioTextAreaSP);
        bioContent.add(viewMoreButton);

        topTracksContent.add(topTrackImage);
        topTracksContent.add(topTrackNumLabel);
        topTracksContent.add(topTrackNameLabel);
        topTracksContent.add(topTrackName);
        topTracksContent.add(topTrackPlayCountLabel);
        topTracksContent.add(topTrackPlayCount);
        topTracksContent.add(topTrackListenersLabel);
        topTracksContent.add(topTrackListeners);
        topTracksContent.add(topTrackURLLabel);
        topTracksContent.add(topTrackURL);
        topTracksContent.add(topTracksNext);
        topTracksContent.add(topTracksBack);

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