import components.*;
import containers.*;
import gui.*;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

public class MusicPlayerFrame extends JFrame {
    private final Color blue_dark_color = new Color(0x264653);
    private final Color blue_color = new Color(0x2a9d8f);
    private final Color orange_light_color = new Color(0xe9c46a);
    private final Color orange_color = new Color(0xf4a261);
    private final Color red_light_color = new Color(0xe76f51);
    private final Color DEFAULT_TEXT_COLOR = (new JLabel()).getForeground();
    private final int OPENING_CONTENT = -1;
    private final int MUSIC_CONTENT = 0;
    private final int CREATE_PLAYLIST_CONTENT = 1;
    private final int BIO_CONTENT = 2;
    private final int TOP_ARTISTS_CONTENT = 3;
    private final int TOP_TRACKS_CONTENT = 4;
    private final int TOP_ALBUMS_CONTENT = 5;
    private int currentContent = OPENING_CONTENT;
    private int width, realW, height, realH;
    private JPanel header = new JPanel(null), menu = new JPanel(null);
    private JPanel footer = new JPanel(null);
    private List<Song> allSongs = new ArrayList();
    private List<String> allSongsNames = new ArrayList<>();
    private Timer timer, sliderUpdateTimer;
    private Clip clip;
    private Image image = (new ImageIcon("img/GIAM_Icon_AcademyOfMusic_RGB.png")).getImage();
    private int menuOptionCounter = 0;
    private int totalSeconds = 0;
    private boolean isSongStarted = false;
    private int songSliderLength = 10000000;
    //private SongPlayer songPlayer;
    //---Header---
    private CustomButton menuShower = new CustomButton("≡",orange_color,20,20);
    private boolean showing = true;
    //----------------
    //---Menu---
    private CustomButton createPlaylist = new CustomButton("Create Playlist",orange_color,0);
    private CustomButton musicContentButton = new CustomButton("Music",orange_color,0);
    private CustomButton searchArtistBio = new CustomButton("Search Artist Bio",orange_color,0);
    private CustomButton discoverTopArtists = new CustomButton("Discover Top Artists",orange_color,0);
    private CustomButton discoverTopTracks = new CustomButton("Discover Top Tracks",orange_color,0);
    private CustomButton discoverTopAlbums = new CustomButton("Discover Top Albums",orange_color,0);
    //----------------
    //---Content---
    private JPanel openingContent = new JPanel(null);
    private JPanel musicContent = new JPanel(null), createPLContent = new JPanel(null);
    private JPanel bioContent = new JPanel(null), topArtistsContent = new JPanel(null);
    private JPanel topTracksContent = new JPanel(null), topAlbumsContent = new JPanel(null);
    //----------------
    //---Opening Content---
    private JLabel openingLabel = new JLabel("Welcome to the Music Player Project!");
    //----------------
    //---Music Content---
    private boolean played = false;
    private boolean started = false;
    private int framePosition = 0;
    private Song currentSong;
    private int currentSongNum = 0;
    private boolean songFinished = false;
    private JLabel songNameLabel = new JLabel();
    private CustomButton playPauseButton = new CustomButton("",orange_color,1);
    private CustomButton nextButton = new CustomButton("",orange_color,1);
    private CustomButton previousButton = new CustomButton("",orange_color,1);
    //private JSlider songSlider = new JSlider();
    private JList<String> mainPlaylist;
    private JScrollPane mainPlaylistSP;
    //----------------
    //---Create Playlist Content---
    private String playlistName;
    private JLabel playlistNameLabel = new JLabel("Playlist Name : ");
    private JTextField playlistNameText = new JTextField();
    private CustomButton next = new CustomButton("Next",orange_color,40,40);
    private CustomButton back = new CustomButton("Back",orange_color,40,40);
    private JList<String> songSelectorForPlaylist;
    private JScrollPane sp;
    private List<String> newPlaylist;
    private JLabel jLabel = new JLabel();
    private JButton create = new JButton("Create");
    private JPanel pagePL1 = new JPanel();
    private JPanel pagePL2 = new JPanel();
    //----------------
    //---Bio Content---
    private String wikiURL;
    private JTextArea bioTextArea = new JTextArea();
    private JScrollPane bioTextAreaSP;
    private CustomButton viewMoreButton = new CustomButton(
            "View More Bio",orange_color,40,40);
    private JLabel artistLabel = new JLabel("Enter the artist's name:");
    private JTextField artistInput = new JTextField();
    private CustomButton searchBio = new CustomButton(
            "Search",orange_color,40,40);
    private boolean isArtistBioEmpty = true;
    private boolean isArtistBioValid = false;
    //----------------
    //---Discover Top Artists Content---
    private List<Artist> artistList;
    private int topArtistNum = 0;
    private CustomButton topArtistsNext = new CustomButton(
            "Next",orange_color, 20,20);
    private CustomButton topArtistsBack = new CustomButton(
            "Back",orange_color, 20,20);
    private JLabel topArtistImage, topArtistNumLabel;
    private JLabel topArtistName, topArtistNameLabel;
    private JLabel topArtistPlayCount, topArtistPlayCountLabel;
    private JLabel topArtistListeners, topArtistListenersLabel;
    private JLabel topArtistURL, topArtistURLLabel;
    //----------------
    //---Discover Top Tracks Content---
    private CustomButton getTopTracks = new CustomButton(
            "Get Top Tracks",orange_color, 20,20);
    //----------------
    //---Discover Top Albums Content---
    private CustomButton getTopAlbums = new CustomButton(
            "Get Top Albums",orange_color, 20,20);
    //----------------
    private final Font songNameFont = new Font(Font.SANS_SERIF, Font.BOLD,30);
    private final Font myFont = new Font(Font.SANS_SERIF, Font.BOLD,20);
    private final Font headerFont = new Font(Font.SANS_SERIF, Font.BOLD,40);
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
        header.setBackground(blue_dark_color);

        footer.setBounds(0,realH - header.getHeight(),realW,50);
        footer.setBackground(blue_dark_color);

        menu.setBounds(0,header.getHeight(),250,footer.getY());
        menu.setBackground(blue_color);

        createContent(openingContent,blue_color.brighter(),true);
        createContent(musicContent,blue_color.brighter(),false);
        createContent(bioContent,blue_color.brighter(),false);
        createContent(createPLContent,blue_color.brighter(),false);
        createContent(topArtistsContent,blue_color.brighter(),false);
        createContent(topTracksContent,blue_color.brighter(),false);
        createContent(topAlbumsContent,blue_color.brighter(),false);

        loadSongs("music/");
        currentSong = allSongs.get(currentSongNum);
        fillAllSongsNames();

        //---Header Options---
        menuShower.setBounds((int)(header.getWidth() * 0.02),(int)(header.getHeight() * 0.1),
            (int)(header.getWidth() * 0.06),(int)(header.getHeight() * 0.8));
        menuShower.setFocusable(false);
        menuShower.setFont(headerFont);
        menuShower.addActionListener(e -> changeMenu());

        //---Menu Options---
        createMenuOption(musicContentButton,MUSIC_CONTENT);
        createMenuOption(createPlaylist,CREATE_PLAYLIST_CONTENT);
        createMenuOption(searchArtistBio,BIO_CONTENT);
        createMenuOption(discoverTopArtists,TOP_ARTISTS_CONTENT);
        createMenuOption(discoverTopTracks,TOP_TRACKS_CONTENT);
        createMenuOption(discoverTopAlbums,TOP_ALBUMS_CONTENT);

        //---Contents---
        initOpeningContent();
        initMusicContent();
        initCreatePLContent();
        initBioContent();
        initTopArtistsContent();
        initTopTracksContent();
        initTopAlbumsContent();

        //---Footer---
        // todo

        // Create a timer that updates every 1000 milliseconds (1 second)
        timer = new Timer(1000, e -> {
        songNameLabel.setText(currentSong.getName());
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

        header.add(menuShower);

        menu.add(musicContentButton);
        menu.add(createPlaylist);
        menu.add(searchArtistBio);
        menu.add(discoverTopArtists);
        menu.add(discoverTopTracks);
        menu.add(discoverTopAlbums);

        openingContent.add(openingLabel);

        //musicContent.add(songSlider);
        musicContent.add(playPauseButton);
        musicContent.add(nextButton);
        musicContent.add(previousButton);
        musicContent.add(songNameLabel);
        musicContent.add(mainPlaylistSP);

        pagePL1.add(playlistNameLabel);
        pagePL1.add(playlistNameText);
        pagePL1.add(next);
        pagePL2.add(back);
        pagePL2.add(jLabel);
        pagePL2.add(create);
        pagePL2.add(sp);
        createPLContent.add(pagePL1);
        createPLContent.add(pagePL2);

        bioContent.add(artistLabel);
        bioContent.add(artistInput);
        bioContent.add(searchBio);
        bioContent.add(bioTextAreaSP);
        bioContent.add(viewMoreButton);

        topArtistsContent.add(topArtistImage);
        topArtistsContent.add(topArtistNumLabel);
        topArtistsContent.add(topArtistNameLabel);
        topArtistsContent.add(topArtistName);
        topArtistsContent.add(topArtistPlayCountLabel);
        topArtistsContent.add(topArtistPlayCount);
        topArtistsContent.add(topArtistListenersLabel);
        topArtistsContent.add(topArtistListeners);
        topArtistsContent.add(topArtistURLLabel);
        topArtistsContent.add(topArtistURL);
        topArtistsContent.add(topArtistsNext);
        topArtistsContent.add(topArtistsBack);

        topTracksContent.add(getTopTracks);

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

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void initOpeningContent() {
        //---Opening Label---
        openingLabel.setBounds((int)(openingContent.getWidth() * 0.02),
                (int)(openingContent.getHeight() * 0.05),(int)(openingContent.getWidth() * 0.96),50);
        openingLabel.setFont(myFont);
        openingLabel.setForeground(blue_dark_color);
    }

    private void initMusicContent() {
        //---Playlist---
        mainPlaylist = new JList<>(allSongsNames.toArray(new String[0]));
        mainPlaylist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mainPlaylist.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                goTo(mainPlaylist.getLeadSelectionIndex());
            }
        });
        mainPlaylist.setCellRenderer(new CenterListCellRenderer());
        mainPlaylist.setBackground(orange_color);
        // the 3 vertical dots "⋮" todo

        mainPlaylistSP = new JScrollPane(mainPlaylist);
        mainPlaylistSP.setBounds((int)(musicContent.getWidth() * 0.5) - 250,
                (int)(musicContent.getHeight() * 0.01),
                500,300);
        mainPlaylistSP.setBorder(null);
        mainPlaylistSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainPlaylistSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        //---Play/Pause Button---
        playPauseButton.setBounds(
                (musicContent.getWidth()/2) - 30,
                (int)(0.85 * musicContent.getHeight()), 60,60);
        playPauseButton.setFocusable(false);
        playPauseButton.setText("▶");
        playPauseButton.setFont(myFont);
        playPauseButton.addActionListener(e -> playPauseMusic());

        //---Title Label---
        songNameLabel.setBounds(
                (musicContent.getWidth()/2) - 250,
                (int)(0.55 * musicContent.getHeight()), 500,50);
        songNameLabel.setFont(songNameFont);

        //---Next containers.Song Button---
        nextButton.setBounds(
                (int)(musicContent.getWidth() * 0.75) - 30,
                (int)(0.85 * musicContent.getHeight()), 60,60);
        nextButton.setVisible(false);
        nextButton.setFocusable(false);
        nextButton.setText("⏩");
        nextButton.setFont(myFont);
        nextButton.addActionListener(e -> nextMusic());

        //---Previous containers.Song Button---
        previousButton.setBounds(
                (int)(musicContent.getWidth() * 0.25) - 30,
                (int)(0.85 * musicContent.getHeight()), 60,60);
        previousButton.setVisible(false);
        previousButton.setFocusable(false);
        previousButton.setText("⏪");
        previousButton.setFont(myFont);
        previousButton.addActionListener(e -> previousMusic());

        //---containers.Song Bar Slider---
        /*songSlider.setBounds((int)(musicContent.getWidth() * 0.02),
                (int)(musicContent.getHeight() * 0.97),
                (int)(musicContent.getWidth() * 0.96), 20);
        songSlider.setVisible(false);
        songSlider.setMinimum(0);
        songSlider.setValue(0);
        songSlider.addChangeListener(e -> changeFramesOfSong());*/
    }

    private void initCreatePLContent() {
        Color color = createPLContent.getBackground();
        //---First Page---
        pagePL1.setBounds(0,0,createPLContent.getWidth(),createPLContent.getHeight());
        pagePL1.setBackground(color);
        pagePL1.setLayout(null);

        //---Second Page---
        pagePL2.setBounds(0,0,createPLContent.getWidth(),createPLContent.getHeight());
        pagePL2.setBackground(color);
        pagePL2.setLayout(null);
        pagePL2.setVisible(false);

        //---Label---
        playlistNameLabel.setBounds((int)(0.02 * createPLContent.getWidth()),
                (int)(0.1 * createPLContent.getHeight()), 100, 50);
        playlistNameText.setBounds((int)(0.02 * createPLContent.getWidth()) + 100,
                (int)(0.1 * createPLContent.getHeight()), 200, 50);

        next.setFocusable(false);
        next.setBounds((int)(0.8 * createPLContent.getWidth()),
                createPLContent.getHeight() - 100, 80, 50);
        next.addActionListener(e -> {
            if (playlistNameText.getText().equals("")) {
                playlistNameLabel.setForeground(red_light_color);
            } else {
                playlistNameLabel.setForeground(DEFAULT_TEXT_COLOR);
                playlistName = playlistNameText.getText();
                jLabel.setText(
                        "Select which songs do you want into the " + playlistName + " playlist");
                pagePL1.setVisible(false);
                pagePL2.setVisible(true);
            }
        });

        back.setFocusable(false);
        back.setBounds(0, createPLContent.getHeight() - 100, 80, 50);
        back.addActionListener(e -> {
            pagePL1.setVisible(true);
            pagePL2.setVisible(false);
        });

        create.setBounds((int)(0.8 * createPLContent.getWidth()),
                createPLContent.getHeight() - 100, 80, 50);
        create.addActionListener(e -> {
            // TODO: 15/11/2024
        });
        create.setFocusable(false);

        jLabel.setBounds((int)(0.02 * createPLContent.getWidth()),
                (int)(0.1 * createPLContent.getHeight()),
                (int)(0.96 * createPLContent.getWidth()), 50);
        songSelectorForPlaylist = new JList<>(allSongsNames.toArray(new String[0]));
        songSelectorForPlaylist.setCellRenderer(new CenterListCellRenderer());

        songSelectorForPlaylist.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        songSelectorForPlaylist.addListSelectionListener(e -> {
            newPlaylist = new ArrayList<>(songSelectorForPlaylist.getSelectedValuesList());
        });

        sp = new JScrollPane(songSelectorForPlaylist);
        sp.setBounds((int)(0.02 * createPLContent.getWidth()),
                (int)(0.2 * createPLContent.getHeight()),
                (int)(0.93 * createPLContent.getWidth()), 200);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
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
        bioTextArea.setDisabledTextColor(DEFAULT_TEXT_COLOR);
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

    private void initTopArtistsContent() {
        //---Show JSon Object to console with the button getTopArtists---
        artistList = new ArrayList<>();
        GetTopArtists topArtists = new GetTopArtists();
        JSONObject jsonObject = topArtists.getTopArtists();

        JSONObject artists = jsonObject.getJSONObject("artists");

        //JSONObject attr = artists.getJSONObject("@attr");

        JSONArray artistArray = artists.getJSONArray("artist");
        for (int i = 0; i < artistArray.length(); i++) {
            JSONObject artist = artistArray.getJSONObject(i);

            JSONArray images = artist.getJSONArray("image");
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

            artistList.add(new Artist(
                    artist.getString("name"),
                    Long.parseLong(artist.getString("playcount")),
                    Long.parseLong(artist.getString("listeners")),
                    artist.getString("mbid"),
                    artist.getString("url"),
                    Long.parseLong(artist.getString("streamable")),
                    imageHolder
            ));
            //artistList.get(i).print();
        }

        //---Top Artist Image---
        URL url;
        BufferedImage image;
        try {
            url = new URL(artistList.get(topArtistNum).getImageHolder().getMegaImage());
            image = ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ImageIcon icon = new ImageIcon(image);
        topArtistImage = new JLabel(icon);
        // Optional: Center align the image in the JLabel
        topArtistImage.setHorizontalAlignment(JLabel.CENTER);
        topArtistImage.setVerticalAlignment(JLabel.CENTER);
        topArtistImage.setBounds((int)(0.02 * topArtistsContent.getWidth()),
                (int)(0.05 * topArtistsContent.getHeight()),icon.getIconWidth(),
                icon.getIconHeight());

        //---Top Artist Number---
        topArtistNumLabel = new JLabel(String.valueOf(topArtistNum + 1));
        topArtistNumLabel.setFont(headerFont);
        topArtistNumLabel.setBounds((int)(0.5 * topArtistsContent.getWidth()) - 25,
                (int)(0.9 * topArtistsContent.getHeight()),50,50);

        //---Next Button---
        topArtistsNext.setFocusable(false);
        topArtistsNext.setBounds(
                (int)(0.98 * topArtistsContent.getWidth()) - 100,
                (int)(0.9 * topArtistsContent.getHeight()),100,50);
        topArtistsNext.addActionListener(e -> {
            if (!(topArtistNum + 1 < artistList.size())) {
                topArtistNum = 0;
            } else {
                topArtistNum++;
            }
            setArtist();
        });

        //---Back Button---
        topArtistsBack.setFocusable(false);
        topArtistsBack.setBounds((int)(0.02 * topArtistsContent.getWidth()),
                (int)(0.9 * topArtistsContent.getHeight()),100,50);
        topArtistsBack.addActionListener(e -> {
            if (!(topArtistNum - 1 > -1)) {
                topArtistNum = artistList.size() - 1;
            } else {
                topArtistNum--;
            }
            setArtist();
        });

        //---Top Artist Name---
        topArtistNameLabel = new JLabel("Name          :");
        topArtistNameLabel.setBounds(icon.getIconWidth() + 40,
                (int)(0.05 * topArtistsContent.getHeight()),100,50);
        topArtistName = new JLabel(artistList.get(topArtistNum).getName());
        topArtistName.setBounds(icon.getIconWidth() + 120,
                (int)(0.05 * topArtistsContent.getHeight()),400,50);

        //---Top Artist Play Count---
        topArtistPlayCountLabel = new JLabel("Play Count :");
        topArtistPlayCountLabel.setBounds(icon.getIconWidth() + 40,
                (int)(0.05 * topArtistsContent.getHeight())+50,100,50);
        topArtistPlayCount = new JLabel(String.valueOf(artistList.get(topArtistNum).getPlayCount()));
        topArtistPlayCount.setBounds(icon.getIconWidth() + 120,
                (int)(0.05 * topArtistsContent.getHeight())+50,400,50);

        //---Top Artist Listeners---
        topArtistListenersLabel = new JLabel("Listeners   :");
        topArtistListenersLabel.setBounds(icon.getIconWidth() + 40,
                (int)(0.05 * topArtistsContent.getHeight())+100,100,50);
        topArtistListeners = new JLabel(String.valueOf(artistList.get(topArtistNum).getListeners()));
        topArtistListeners.setBounds(icon.getIconWidth() + 120,
                (int)(0.05 * topArtistsContent.getHeight())+100,400,50);

        //---Top Artist URL---
        topArtistURLLabel = new JLabel("URL             :");
        topArtistURLLabel.setBounds(icon.getIconWidth() + 40,
                (int)(0.05 * topArtistsContent.getHeight())+150,100,50);
        topArtistURL = new JLabel(artistList.get(topArtistNum).getUrl());
        topArtistURL.setBounds(icon.getIconWidth() + 120,
                (int)(0.05 * topArtistsContent.getHeight())+150,400,50);
    }

    private void setArtist() {
        //---Top Artist Image---
        URL url;
        BufferedImage image;
        try {
            url = new URL(artistList.get(topArtistNum).getImageHolder().getMegaImage());
            image = ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ImageIcon icon = new ImageIcon(image);
        topArtistImage = new JLabel(icon);
        topArtistImage.setHorizontalAlignment(JLabel.CENTER);
        topArtistImage.setVerticalAlignment(JLabel.CENTER);
        topArtistImage.setBounds((int)(0.02 * topArtistsContent.getWidth()),
                (int)(0.05 * topArtistsContent.getHeight()),icon.getIconWidth(),
                icon.getIconHeight());

        //---Top Artist Number---
        topArtistNumLabel.setText(String.valueOf(topArtistNum + 1));

        //---Top Artist Name---
        topArtistName.setText(artistList.get(topArtistNum).getName());

        //---Top Artist Play Count---
        topArtistPlayCount.setText(String.valueOf(artistList.get(topArtistNum).getPlayCount()));

        //---Top Artist Listeners---
        topArtistListeners.setText(String.valueOf(artistList.get(topArtistNum).getListeners()));

        //---Top Artist URL---
        topArtistURL.setText(artistList.get(topArtistNum).getUrl());
    }

    private void initTopTracksContent() {
        //--- show JSon Object to console with the button getTopTracks ---
        getTopTracks.setBounds((int)(topTracksContent.getWidth() * 0.1),
                (int)(topTracksContent.getHeight() * 0.1),
                (int)(topTracksContent.getWidth() * 0.2), 50);
        getTopTracks.setFocusable(false);
        getTopTracks.addActionListener(e -> {
            GetTopTracks tracks = new GetTopTracks();
            //JsonObject jsonObject = tracks.getTopTracks();
            //System.out.println(jsonObject);
        });
    }

    private void initTopAlbumsContent() {
        //--- show JSon Object to console with the button getTopAlbums ---
        getTopAlbums.setBounds((int)(topAlbumsContent.getWidth() * 0.1),(int)(topAlbumsContent.getHeight() * 0.1),
                (int)(topAlbumsContent.getWidth() * 0.2), 50);
        getTopAlbums.setFocusable(false);
        getTopAlbums.addActionListener(e -> {
            GetTopAlbums albums = new GetTopAlbums();
            //JsonObject jsonObject = albums.getTopAlbums();
            //System.out.println(jsonObject);
        });
    }

    public void playPauseMusic() {
        nextButton.setVisible(true);
        previousButton.setVisible(true);
        //songSlider.setVisible(true);
        if (played) {
            playPauseButton.setText("▶");
            //framePosition = clip.getFramePosition();
            /*long songLengthMin = (clip.getMicrosecondLength()/60000000);
            long songLengthSec = (clip.getMicrosecondLength()%60000000)/1000000;
            System.out.println("containers.Song length : " + songLengthMin + ":" + songLengthSec);*/
            clip.stop();
        } else {
            playPauseButton.setText("⏸");
            if (!started) {
                System.out.println("containers.Song : " + currentSong.getName());
                started = true;
            }
            playAudio();
            if (songFinished) {
                songFinished = false;
                nextSong();
            }
        }
        played = !played;
    }

    public void nextMusic() {
        clip.close();
        nextSong();
    }

    public void previousMusic() {
        clip.close();
        previousSong();
    }

    private void goTo(int leadSelectionIndex) {
        if (clip != null) {
            clip.close();
        }
        framePosition = 0;
        played = false;
        started = false;
        setCurSong(leadSelectionIndex);
        loadAudio();
        playPauseMusic();
    }

    /*public void changeFramesOfSong() {
        if (clip != null) {
            int newPosition = songSlider.getValue();
            clip.setFramePosition(newPosition); // Seek to new frame position
            framePosition = newPosition; // Update framePosition to new slider position
        }
        //System.out.println("SongSlider Value : " + songSlider.getValue());
    }*/

    public void loadAudio() {
        if (currentSong.getExcention().equals(".wav") || currentSong.getExcention().equals(".au")) {
            File file = new File(currentSong.getPath());
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(ais);
            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                e.printStackTrace();
            }
            //songSliderLength = clip.getFrameLength();
        }
        if (currentSong.getExcention().equals(".mp3")) {}
    }

    public void playAudio() {
        loadAudio();
        clip.addLineListener(e -> {
            if (e.getType() == LineEvent.Type.START) {
                playPauseButton.setText("▶");
                System.out.println("containers.Song : Start");
            } else if (e.getType() == LineEvent.Type.STOP) {
                playPauseButton.setText("⏸");
                framePosition = clip.getFramePosition();
                if (clip.getFramePosition() < clip.getFrameLength()) {
                    System.out.println("containers.Song : Stop");
                } else {
                    played = false;
                    songFinished = true;
                    clip.close();
                }
            } else if (e.getType() == LineEvent.Type.CLOSE) {
                System.out.println("containers.Song : Close");
                framePosition = 0;
                started = false;
                isSongStarted = false;
                if (songFinished) {
                    songFinished = false;
                    nextSong();
                }
            }
        });
        clip.setFramePosition(framePosition);
        clip.start();
    }

    public void nextSong() {
        framePosition = 0;
        played = false;
        started = false;
        if (getCurSongNum() + 1 < allSongs.size()) {
            System.out.println("nextMusic true");
            setCurSong(getCurSongNum() + 1);
        } else {
            System.out.println("nextMusic false");
            setCurSong(0);
            System.out.println("Loop :");
        }
        loadAudio();
        playPauseMusic();
    }

    public void previousSong() {
        framePosition = 0;
        played = false;
        started = false;
        if (getCurSongNum() - 1 > -1) {
            System.out.println("previousMusic true");
            setCurSong(getCurSongNum() - 1);
        } else {
            System.out.println("previousMusic false");
            setCurSong(allSongs.size() - 1);
            System.out.println("Loop :");
        }
        loadAudio();
        playPauseMusic();
    }

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

    private void changeMenu() {
        JPanel content = getContent();
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

    private JPanel getContent() {
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

    private void fillAllSongsNames() {
        for (int i = 0; i < allSongs.size(); i++) {
            allSongsNames.add(allSongs.get(i).getName());
        }
    }

    private void createMenuOption(JButton button, int content) {
        button.setBounds(0,menuOptionCounter * 50,250,50);
        button.setFocusable(false);
        button.setFont(myFont);
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

    private int getCurSongNum() {
        return currentSongNum;
    }
    private void setCurSong(int num) {
        System.out.println("Num : " + num);
        currentSongNum = num;
        currentSong = allSongs.get(num);
        //songPlayer.changeSong(currentSong);
    }
}

