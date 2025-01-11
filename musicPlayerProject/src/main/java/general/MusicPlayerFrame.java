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
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
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
    public ProfileDropDown profileDropDown = new ProfileDropDown(Util.orange_color,20,this);
    public boolean profileShowing = false;
    //---Header---
    private ImageIcon accountIcon;
    private RoundButton menuShower = new RoundButton("≡",Util.orange_color,20,20);
    private OvalButton profileButton = new OvalButton("",Util.orange_color);
    private JComboBox playlistSelector;
    private CustomTextField searchBar = new CustomTextField(Util.orange_color,20,20);
    //----------------
    //---Menu---
    private boolean isMenuShowing = true;
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
    private AboutMeContent aboutMeContent = new AboutMeContent(null,this);
    private LoadingContent loadingContent = new LoadingContent(null,this);
    //----------------
    public MusicPlayerFrame(User user, FirestoreManager fr, int width, int height) {
        this.user = user;
        this.fr = fr;
        this.width = width;
        this.height = height;

        this.pack();
        Insets insets = this.getInsets();

        this.setTitle("MusicPlayer");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(this.width,this.height);
        this.setLayout(null);
        this.setIconImage(image);

        realW = this.width - insets.left - insets.right;
        realH = this.height - insets.top - insets.bottom;

        //---Header, Footer, Menu, Contents---
        header.setBounds(0,0,realW,(int)(0.074 * realH));
        header.setBackground(Util.blue_dark_color);

        footer.setBounds(0,realH - header.getHeight(),realW,(int)(0.074 * realH));
        footer.setBackground(Util.blue_dark_color);

        menu.setBounds(0,header.getHeight(),(int)(0.3672 * realH),footer.getY() - (int)(0.3672 * realH));
        menu.setBackground(Util.blue_color);
        menu.setLayout(new BoxLayout(menu,BoxLayout.Y_AXIS));

        createContent(openingContent,Util.blue_color.brighter(),true);
        createContent(musicContent,Util.blue_color.brighter(),false);
        createContent(bioContent,Util.blue_color.brighter(),false);
        createContent(createPLContent,Util.blue_color.brighter(),false);
        createContent(topArtistsContent,Util.blue_color.brighter(),false);
        createContent(topTracksContent,Util.blue_color.brighter(),false);
        createContent(topAlbumsContent,Util.blue_color.brighter(),false);
        createContent(searchResultsContent,Util.blue_color.brighter(),false);
        createContent(aboutMeContent,Util.blue_color.brighter(),false);
        createContent(loadingContent,Util.blue_color.brighter(),false);

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
        playlistSelector.setBounds((int)(header.getWidth() * 0.6),(int)(header.getHeight() * 0.1),
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

        //---Login Logout Register Button---
        accountIcon = new ImageIcon("img/circle-user-orange.png");
        Image image = accountIcon.getImage(); // resize the icon
        Image scaledImage = image.getScaledInstance(35, 35, Image.SCALE_SMOOTH); // scale it
        accountIcon = new ImageIcon(scaledImage);

        profileButton.setIcon(accountIcon);
        profileButton.setBounds((int)(header.getWidth() * 0.9),(int)(header.getHeight() * 0.1),
                (int)(header.getWidth() * 0.04),(int)(header.getHeight() * 0.8));
        profileButton.setFocusable(false);
        profileButton.setFont(Util.songNameFont);
        profileButton.addActionListener(e -> {
            if (profileShowing) {
                profileDropDown.setVisible(false);
            } else {
                profileDropDown.setVisible(true);
            }
            profileShowing = !profileShowing;
        });

        profileDropDown.setBounds((int)(header.getWidth() * 0.85),header.getHeight() + 5,
                (int)(header.getWidth() * 0.13),204);
        profileDropDown.setVisible(false);

        //---Footer---
        // todo

        Timer resizeTimer = new Timer(25, e -> resizeComponents());
        resizeTimer.setRepeats(false);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeTimer.restart();
            }
        });

        //---Add Components---
        addComponents();

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void resizeComponents() {
        /*Content Pane Width  : 1064
        Content Pane Height : 681

        Header Width  : 1064
        Header Height : 50

        Footer Width  : 1064
        Footer Height : 50

        Content Width  : 816
        Content Height : 581*/
        int totalW = getContentPane().getSize().width;
        int totalH = getContentPane().getSize().height;

        header.setBounds(0,0,totalW,(int)(0.074 * totalH));
        footer.setBounds(0,totalH - header.getHeight(),totalW,(int)(0.074 * totalH));
        if (isMenuShowing) {
            menu.setBounds(0,header.getHeight(),(int)(0.282 * totalW),footer.getY() - (int)(0.074 * totalH));
            System.out.println("Menu getting orig size");
        } else {
            menu.setBounds(0,0,0,0);
            System.out.println("Menu getting 0 size");
        }
        menu.repaint();
        menu.revalidate();

        resizeHeaderComponents();
        //resizeFooterComponents(); if needed

        //---openingContent---
        openingContent.setBounds(
                menu.getWidth(),header.getHeight(),
                totalW - menu.getWidth(),
                totalH - (header.getHeight() + footer.getHeight()));
        int contentW = openingContent.getWidth();
        int contentH = openingContent.getHeight();
        openingContent.openingLabel.setBounds((int)(contentW * 0.02),(int)(contentH * 0.02),
                (int)(contentW * 0.96),(int)(0.0861 * contentH));

        //---musicContent---
        musicContent.setBounds(
                menu.getWidth(),header.getHeight(),
                totalW - menu.getWidth(),
                totalH - (header.getHeight() + footer.getHeight()));
        musicContent.progressBar.setBounds((int)(contentW * 0.02),(int)(contentH * 0.75),
                (int)(contentW * 0.96),(int)(contentH * 0.0276));
        musicContent.playPauseButton.setBounds((int)(contentW * 0.5) - (int)(contentW * 0.0368),
                (int)(0.85 * contentH), (int)(contentW * 0.0736),(int)(0.1033 * contentH));
        musicContent.nextButton.setBounds((int)(contentW * 0.75) - (int)(contentW * 0.0368),
                (int)(0.85 * contentH),(int)(contentW * 0.0736),(int)(0.1033 * contentH));
        musicContent.previousButton.setBounds((int)(contentW * 0.25) - (int)(contentW * 0.0368),
                (int)(0.85 * contentH),(int)(contentW * 0.0736),(int)(0.1033 * contentH));
        /*musicContent.songNameLabel.setBounds((int)(contentW * 0.5) - (int)(contentW * 0.3064),
                (int)(0.55 * contentH), (int)(contentW * 0.6128),(int)(0.0861 * contentH));*/
        musicContent.mainPlaylistSP.setBounds((int)(contentW * 0.02),(int)(contentH * 0.01),
                (int)(contentW * 0.96),(int)(contentH * 0.5165));

        //---createPLContent---
        createPLContent.setBounds(
                menu.getWidth(),header.getHeight(),
                totalW - menu.getWidth(),
                totalH - (header.getHeight() + footer.getHeight()));
        createPLContent.playlistNameLabel.setBounds((int)(0.02 * contentW),(int)(0.02 * contentH),
                (int)(0.1839 * contentW),(int)(0.0861 * contentH));
        createPLContent.playlistNameText.setBounds((int)(0.5 * contentW) - (int)(0.3003 * contentW),
                (int)(0.02 * contentH),(int)(0.6005 * contentW),(int)(0.0861 * contentH));
        createPLContent.jLabel.setBounds((int)(0.02 * contentW),(int)(0.1 * contentH),
                (int)(0.96 * contentW),(int)(0.0861 * contentH));
        createPLContent.create.setBounds((int)(0.98 * contentW - (int)(0.1471 * contentW)),
                (int)(0.98 * contentH - (int)(0.0861 * contentH)),(int)(0.1471 * contentW),(int)(0.0861 * contentH));
        createPLContent.clear.setBounds((int)(0.02 * contentW),(int)(0.98 * contentH - (int)(0.0861 * contentH)),
                (int)(0.1471 * contentW),(int)(0.0861 * contentH));
        createPLContent.sp.setBounds((int)(0.02 * contentW),(int)(0.1205 * contentH),
                (int)(0.93 * contentW),(int)(0.76 * contentH));

        //---bioContent---
        bioContent.setBounds(
                menu.getWidth(),header.getHeight(),
                totalW - menu.getWidth(),
                totalH - (header.getHeight() + footer.getHeight()));
        bioContent.artistLabel.setBounds((int)(0.02 * contentW),(int)(0.02 * contentH),
                (int)(0.3064 * contentW),(int)(0.0861 * contentH));
        bioContent.artistInput.setBounds((int)(0.5 * contentW) - (int)(0.1839 * contentW),
                (int)(0.02 * contentH),(int)(0.4902 * contentW),(int)(0.0861 * contentH));
        bioContent.searchBio.setBounds((int)(0.02 * contentW),(int)(0.11 * contentH),
                (int)(0.2451 * contentW),(int)(0.0861 * contentH));
        bioContent.bioTextAreaSP.setBounds((int)(0.02 * contentW),(int)(0.2 * contentH),
                (int)(0.96 * contentW),(int)(0.8 * contentH));
        bioContent.viewMoreButton.setBounds((int)(0.98 * contentW) - (int)(0.2451 * contentW),
                (int)(0.11 * contentH),(int)(0.2451 * contentW),(int)(0.0861 * contentH));

        //---topArtistsContent---
        topArtistsContent.setBounds(
                menu.getWidth(),header.getHeight(),
                totalW - menu.getWidth(),
                totalH - (header.getHeight() + footer.getHeight()));
        topArtistsContent.icon = new ImageIcon(topArtistsContent.image.getScaledInstance(
                (int)(0.3676 * contentW),(int)(0.5164 * contentH),Image.SCALE_SMOOTH));
        topArtistsContent.topArtistImage.setBounds((int)(0.02 * contentW),(int)(0.05 * contentH),
                topArtistsContent.icon.getIconWidth(), topArtistsContent.icon.getIconHeight());
        topArtistsContent.topArtistNumLabel.setBounds((int)(0.5 * contentW) - (int)(0.0307 * contentW),
                (int)(0.9 * contentH),(int)(0.0613 * contentW),(int)(0.0861 * contentH));
        topArtistsContent.topArtistNameLabel.setBounds(topArtistsContent.icon.getIconWidth() + (int)(0.0491 * contentW),
                (int)(0.05 * contentH),(int)(0.1839 * contentW),(int)(0.0861 * contentH));
        topArtistsContent.topArtistName.setBounds(topArtistsContent.icon.getIconWidth() + (int)(0.2329 * contentW),
                (int)(0.05 * contentH),(int)(0.4902 * contentW),(int)(0.0861 * contentH));
        topArtistsContent.topArtistPlayCountLabel.setBounds(topArtistsContent.icon.getIconWidth() + (int)(0.0491 * contentW),
                (int)(0.05 * contentH)+(int)(0.0861 * contentH),(int)(0.1839 * contentW),(int)(0.0861 * contentH));
        topArtistsContent.topArtistPlayCount.setBounds(topArtistsContent.icon.getIconWidth() + (int)(0.2329 * contentW),
                (int)(0.05 * contentH)+(int)(0.0861 * contentH),(int)(0.4902 * contentW),(int)(0.0861 * contentH));
        topArtistsContent.topArtistListenersLabel.setBounds(topArtistsContent.icon.getIconWidth() + (int)(0.0491 * contentW),
                (int)(0.05 * contentH)+(int)(0.1722 * contentH),(int)(0.1839 * contentW),(int)(0.0861 * contentH));
        topArtistsContent.topArtistListeners.setBounds(topArtistsContent.icon.getIconWidth() + (int)(0.2329 * contentW),
                (int)(0.05 * contentH)+(int)(0.1722 * contentH),(int)(0.4902 * contentW),(int)(0.0861 * contentH));
        topArtistsContent.topArtistURLLabel.setBounds((int)(0.02 * contentW),
                topArtistsContent.icon.getIconHeight()+(int)(0.0861 * contentH),(int)(0.1226 * contentW),(int)(0.0861 * contentH));
        topArtistsContent.topArtistURL.setBounds((int)(0.02 * contentW) + (int)(0.1226 * contentW),
                topArtistsContent.icon.getIconHeight()+(int)(0.0861 * contentH),contentW,(int)(0.0861 * contentH));
        topArtistsContent.topArtistsNext.setBounds((int)(0.98 * contentW) - (int)(0.1226 * contentW),
                (int)(0.9 * contentH),(int)(0.1226 * contentW),(int)(0.0861 * contentH));
        topArtistsContent.topArtistsBack.setBounds((int)(0.02 * contentW),
                (int)(0.9 * contentH),(int)(0.1226 * contentW),(int)(0.0861 * contentH));

        //---topTracksContent---
        topTracksContent.setBounds(
                menu.getWidth(),header.getHeight(),
                totalW - menu.getWidth(),
                totalH - (header.getHeight() + footer.getHeight()));
        topTracksContent.icon = new ImageIcon(topTracksContent.image.getScaledInstance(
                (int)(0.3676 * contentW),(int)(0.5164 * contentH),Image.SCALE_SMOOTH));
        topTracksContent.topTrackImage.setBounds((int)(0.02 * contentW),(int)(0.05 * contentH),
                topTracksContent.icon.getIconWidth(), topTracksContent.icon.getIconHeight());
        topTracksContent.topTrackNumLabel.setBounds((int)(0.5 * contentW) - (int)(0.0307 * contentW),
                (int)(0.9 * contentH),(int)(0.0613 * contentW),(int)(0.0861 * contentH));
        topTracksContent.topTrackNameLabel.setBounds(topTracksContent.icon.getIconWidth() + (int)(0.0491 * contentW),
                (int)(0.05 * contentH),(int)(0.1839 * contentW),(int)(0.0861 * contentH));
        topTracksContent.topTrackName.setBounds(topTracksContent.icon.getIconWidth() + (int)(0.2329 * contentW),
                (int)(0.05 * contentH),(int)(0.4902 * contentW),(int)(0.0861 * contentH));
        topTracksContent.topTrackPlayCountLabel.setBounds(topTracksContent.icon.getIconWidth() + (int)(0.0491 * contentW),
                (int)(0.05 * contentH)+(int)(0.0861 * contentH),(int)(0.1839 * contentW),(int)(0.0861 * contentH));
        topTracksContent.topTrackPlayCount.setBounds(topTracksContent.icon.getIconWidth() + (int)(0.2329 * contentW),
                (int)(0.05 * contentH)+(int)(0.0861 * contentH),(int)(0.4902 * contentW),(int)(0.0861 * contentH));
        topTracksContent.topTrackListenersLabel.setBounds(topTracksContent.icon.getIconWidth() + (int)(0.0491 * contentW),
                (int)(0.05 * contentH)+(int)(0.1722 * contentH),(int)(0.1839 * contentW),(int)(0.0861 * contentH));
        topTracksContent.topTrackListeners.setBounds(topTracksContent.icon.getIconWidth() + (int)(0.2329 * contentW),
                (int)(0.05 * contentH)+(int)(0.1722 * contentH),(int)(0.4902 * contentW),(int)(0.0861 * contentH));
        topTracksContent.topTrackURLLabel.setBounds((int)(0.02 * contentW),
                topTracksContent.icon.getIconHeight()+(int)(0.0861 * contentH),(int)(0.1226 * contentW),(int)(0.0861 * contentH));
        topTracksContent.topTrackURL.setBounds((int)(0.02 * contentW) + (int)(0.1226 * contentW),
                topTracksContent.icon.getIconHeight()+(int)(0.0861 * contentH),contentW,(int)(0.0861 * contentH));
        topTracksContent.topTracksNext.setBounds((int)(0.98 * contentW) - (int)(0.1226 * contentW),
                (int)(0.9 * contentH),(int)(0.1226 * contentW),(int)(0.0861 * contentH));
        topTracksContent.topTracksBack.setBounds((int)(0.02 * contentW),
                (int)(0.9 * contentH),(int)(0.1226 * contentW),(int)(0.0861 * contentH));

        //---topAlbumsContent---
        topAlbumsContent.setBounds(
                menu.getWidth(),header.getHeight(),
                totalW - menu.getWidth(),
                totalH - (header.getHeight() + footer.getHeight()));
        topAlbumsContent.content.setBounds((int) (0.02 * contentW),
                (int) (0.01 * contentH), (int) (0.4902 * contentW),(int) (0.08 * contentH));
        topAlbumsContent.search.setBounds((int) (0.02 * contentW) + (int) (0.5148 * contentW),(int) (0.01 * contentH),
                (int) (0.2451 * contentW),(int) (0.08 * contentH));
        topAlbumsContent.icon = new ImageIcon(topAlbumsContent.image.getScaledInstance(
                (int)(0.3676 * contentW),(int)(0.5164 * contentH),Image.SCALE_SMOOTH));
        topAlbumsContent.topAlbumsImage.setBounds((int) (0.02 * contentW),(int)(0.12 * contentH),
                topAlbumsContent.icon.getIconWidth(),topAlbumsContent.icon.getIconHeight());
        topAlbumsContent.topAlbumsNumLabel.setBounds((int) (0.5 * contentW) - (int) (0.0307 * contentW),
                (int) (0.9 * contentH), (int) (0.0613 * contentW), (int) (0.0861 * contentH));
        topAlbumsContent.topAlbumsNext.setBounds((int) (0.98 * contentW) - (int) (0.1226 * contentW),
                (int) (0.9 * contentH), (int) (0.1226 * contentW), (int) (0.0861 * contentH));
        topAlbumsContent.topAlbumsBack.setBounds((int) (0.02 * contentW),
                (int) (0.9 * contentH), (int) (0.1226 * contentW), (int) (0.0861 * contentH));
        topAlbumsContent.topAlbumsNameLabel.setBounds(topAlbumsContent.icon.getIconWidth() + (int) (0.0491 * contentW),
                (int) (0.12 * contentH), (int) (0.1838 * contentW), (int) (0.0861 * contentH));
        topAlbumsContent.topAlbumsName.setBounds(topAlbumsContent.icon.getIconWidth() + (int) (0.2329 * contentW),
                (int) (0.12 * contentH), (int) (0.4902 * contentW), (int) (0.0861 * contentH));
        topAlbumsContent.topAlbumsPlayCountLabel.setBounds(topAlbumsContent.icon.getIconWidth() + (int) (0.0491 * contentW),
                (int) (0.12 * contentH) + (int) (0.0861 * contentH), (int) (0.1838 * contentW), (int) (0.0861 * contentH));
        topAlbumsContent.topAlbumsPlayCount.setBounds(topAlbumsContent.icon.getIconWidth() + (int) (0.2329 * contentW),
                (int) (0.12 * contentH) + (int) (0.0861 * contentH), (int) (0.4902 * contentW), (int) (0.0861 * contentH));
        topAlbumsContent.topAlbumsArtistNameLabel.setBounds(topAlbumsContent.icon.getIconWidth() + (int) (0.0491 * contentW),
                (int) (0.12 * contentH) + (int) (0.1721 * contentH), (int) (0.1838 * contentW), (int) (0.0861 * contentH));
        topAlbumsContent.topAlbumsArtistName.setBounds(topAlbumsContent.icon.getIconWidth() + (int) (0.2329 * contentW),
                (int) (0.12 * contentH) + (int) (0.1721 * contentH), (int) (0.4902 * contentW), (int) (0.0861 * contentH));
        topAlbumsContent.topAlbumsURLLabel.setBounds((int) (0.02 * contentW),
                topAlbumsContent.icon.getIconHeight() + (int) (0.1721 * contentH), (int) (0.1838 * contentW), (int) (0.0861 * contentH));
        topAlbumsContent.topAlbumsURL.setBounds((int) (0.02 * contentW) + (int) (0.1838 * contentW),
                topAlbumsContent.icon.getIconHeight() + (int) (0.1721 * contentH), contentW, (int) (0.0861 * contentH));
        topAlbumsContent.topAlbumsArtistURLLabel.setBounds((int) (0.02 * contentW),
                topAlbumsContent.icon.getIconHeight() + (int) (0.2582 * contentH), (int) (0.1838 * contentW), (int) (0.0861 * contentH));
        topAlbumsContent.topAlbumsArtistURL.setBounds((int) (0.02 * contentW) + (int) (0.1838 * contentW),
                topAlbumsContent.icon.getIconHeight() + (int)(0.2582 * contentH), contentW, (int) (0.0861 * contentH));

        //---searchResultsContent---
        searchResultsContent.setBounds(
                menu.getWidth(),header.getHeight(),
                totalW - menu.getWidth(),
                totalH - (header.getHeight() + footer.getHeight()));
        searchResultsContent.sp.setBounds((int)(0.02 * contentW),(int)(0.02 * contentH),
                (int)(0.96 * contentW),(int)(0.86 * contentH));
        searchResultsContent.clearButton.setBounds((int)(0.02 * contentW),(int)(0.9 * contentH),
                (int)(0.47 * contentW),(int)(0.08 * contentH));
        searchResultsContent.addButton.setBounds((int)(0.51 * contentW),(int)(0.9 * contentH),
                (int)(0.47 * contentW),(int)(0.08 * contentH));

        //---aboutMeContent---
        aboutMeContent.setBounds(
                menu.getWidth(),header.getHeight(),
                totalW - menu.getWidth(),
                totalH - (header.getHeight() + footer.getHeight()));
        aboutMeContent.icon = new ImageIcon(aboutMeContent.image.getScaledInstance(
                (int)(0.3138 * contentW),(int)(0.4407 * contentH),Image.SCALE_SMOOTH));
        aboutMeContent.accountImage.setBounds((int)(0.02 * contentW),(int)(0.02 * contentH),
                aboutMeContent.icon.getIconWidth(),aboutMeContent.icon.getIconHeight());
        aboutMeContent.nameLabel.setBounds((int)(0.02 * contentW),(int)(0.482 * contentH),
                (int)(0.1839 * contentW),(int)(0.0861 * contentH));
        aboutMeContent.name.setBounds((int)(0.02 * contentW) + (int)(0.1961 * contentW),(int)(0.482 * contentH),
                (int)(0.3677 * contentW),(int)(0.0861 * contentH));
        aboutMeContent.passwordLabel.setBounds((int)(0.02 * contentW),(int)(0.568 * contentH),
                (int)(0.1839 * contentW),(int)(0.0861 * contentH));
        aboutMeContent.password.setBounds((int)(0.02 * contentW) + (int)(0.1961 * contentW),(int)(0.568 * contentH),
                (int)(0.3677 * contentW),(int)(0.0861 * contentH));
        aboutMeContent.emailLabel.setBounds((int)(0.02 * contentW),(int)(0.6541 * contentH),
                (int)(0.1839 * contentW),(int)(0.0861 * contentH));
        aboutMeContent.email.setBounds((int)(0.02 * contentW) + (int)(0.1961 * contentW),(int)(0.6541 * contentH),
                (int)(0.3677 * contentW),(int)(0.0861 * contentH));
        aboutMeContent.adminLabel.setBounds((int)(0.02 * contentW),(int)(0.7402 * contentH),
                (int)(0.1839 * contentW),(int)(0.0861 * contentH));
        aboutMeContent.admin.setBounds((int)(0.02 * contentW) + (int)(0.1961 * contentW),(int)(0.7402 * contentH),
                (int)(0.3677 * contentW),(int)(0.0861 * contentH));

        //---loadingContent---
        loadingContent.setBounds(
                menu.getWidth(),header.getHeight(),
                totalW - menu.getWidth(),
                totalH - (header.getHeight() + footer.getHeight()));
        loadingContent.waitingLabel.setBounds((int)(0.5 * contentW) - (int)(0.0613 * contentW),
                (int)(0.5 * contentH),(int)(0.1226 * contentW),(int)(0.0861 * contentH));

        System.out.println("---Resize Completed---\n");
        realW = totalW;
        realH = totalH;
    }

    public void resizeHeaderComponents() {
        int width = header.getWidth();
        int height = header.getHeight();

        menuShower.setBounds((int)(width * 0.02),(int)(height * 0.1),(int)(width * 0.06),(int)(height * 0.8));
        searchBar.setBounds((int)(width * 0.1),(int)(height * 0.1),(int)(width * 0.18),(int)(height * 0.8));
        playlistSelector.setBounds((int)(width * 0.6),(int)(height * 0.1),(int)(width * 0.24),(int)(height * 0.8));
        playlistSelector.repaint();
        playlistSelector.revalidate();
        accountIcon = new ImageIcon(image.getScaledInstance((int)(0.0329 * width), (int)(0.7 * height), Image.SCALE_SMOOTH));
        profileButton.setBounds((int)(width* 0.9),(int)(height * 0.1),(int)(width * 0.04),(int)(height * 0.8));
    }

    public void goTo(String song) {
        musicContent.goTo(song);
    }

    private void changeMenu() {
        if (isMenuShowing) {
            menuShower.setText("⦀");
            menu.setVisible(false);
        } else {
            menuShower.setText("≡");
            menu.setVisible(true);
        }
        isMenuShowing = !isMenuShowing;
        resizeComponents();
    }

    public JPanel getCurrentContent() {
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
            case Util.SEARCH_RESULTS_CONTENT:
                return searchResultsContent;
            case Util.ABOUT_ME_CONTENT:
                return aboutMeContent;
            case Util.LOADING_CONTENT:
                return loadingContent;
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
            case Util.ABOUT_ME_CONTENT:
                return aboutMeContent;
            case Util.LOADING_CONTENT:
                return loadingContent;
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
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE,(int)(0.074 * realH)));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusable(false);
        button.setFont(Util.myFont);
        button.addActionListener(e -> showContent(content));
        menu.add(button);
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
        aboutMeContent.setVisible(false);
        loadingContent.setVisible(false);
        loadingContent.stopTimer();
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
            case Util.ABOUT_ME_CONTENT:
                aboutMeContent.setVisible(true);
                currentContent = Util.ABOUT_ME_CONTENT;
                break;
            case Util.LOADING_CONTENT:
                loadingContent.setVisible(true);
                currentContent = Util.LOADING_CONTENT;
                loadingContent.startTimer();
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
        aboutMeContent.init();
        loadingContent.init();
    }

    public void addComponents() {
        header.add(menuShower);
        header.add(playlistSelector);
        header.add(searchBar);
        header.add(profileButton);

        menu.add(musicContentButton);
        menu.add(createPlaylist);
        menu.add(searchArtistBio);
        menu.add(discoverTopArtists);
        menu.add(discoverTopTracks);
        menu.add(discoverTopAlbums);

        this.add(header);
        this.add(footer);
        this.add(menu);
        this.add(profileDropDown);
        this.add(openingContent);
        this.add(musicContent);
        this.add(createPLContent);
        this.add(bioContent);
        this.add(topArtistsContent);
        this.add(topTracksContent);
        this.add(topAlbumsContent);
        this.add(searchResultsContent);
        this.add(aboutMeContent);
        this.add(loadingContent);
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