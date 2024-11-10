import javax.swing.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

public class MusicPlayerFrame extends JFrame {
    private int width;
    private int height;
    private Timer timer;
    private int totalSeconds = 0;
    private boolean isSongStarted = false;
    private boolean played = false;
    private boolean started = false;
    private int framePosition = 0;
    private int songSliderLength = 10000000;
    private Song currentSong;
    private int currentSongNum = 0;
    private boolean songFinished = false;
    private JLabel songNameLabel = new JLabel();
    private JButton playPauseButton = new JButton();
    private JButton nextButton = new JButton();
    private JButton previousButton = new JButton();
    private JButton SearchArtistBio = new JButton("Search Artist Bio");
    private String wikiURL;
    private JTextArea bioTextArea = new JTextArea();
    private JScrollPane scrollPane;
    private JButton viewMoreButton = new JButton("View More Bio");
    //private JSlider songSlider = new JSlider();
    private ImageIcon playIcon = new ImageIcon("img/play_button_200x200.png");
    private ImageIcon pauseIcon = new ImageIcon("img/pause_button_200x200.png");
    private ImageIcon nextIcon = new ImageIcon("img/next_button_200x200.png");
    private ImageIcon previousIcon = new ImageIcon("img/previous_button_200x200.png");
    private File file;
    private AudioInputStream ais;
    private Clip clip;
    private List<Song> allSongs = new ArrayList();
    private JList<String> mainPlaylist;
    private JScrollPane mainPlaylistSP;
    private Timer sliderUpdateTimer;
    //private SongPlayer songPlayer;
    private Font songNameFont = new Font(Font.SANS_SERIF, Font.BOLD,30);
    public MusicPlayerFrame(int width, int height) {
        this.width = width;
        this.height = height;

        loadSongs("music/");
        int counter = 0;
        // For debugging
        for (Song song : allSongs) {
            System.out.println("#" + counter);
            song.printData();
            counter++;
            System.out.println("-----------");
        }
        currentSong = allSongs.get(currentSongNum);
        List<String> allSongsNames = new ArrayList<>();
        for (int i = 0; i < allSongs.size(); i++) {
            allSongsNames.add(allSongs.get(i).getName());
        }
        mainPlaylist = new JList<>(allSongsNames.toArray(new String[0]));
        mainPlaylist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mainPlaylist.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                goTo(mainPlaylist.getLeadSelectionIndex());
            }
        });

        mainPlaylistSP = new JScrollPane(mainPlaylist);
        mainPlaylistSP.setBounds((int)(this.width/2)-250,0,500,300);
        mainPlaylistSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainPlaylistSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // playIcon and pauseIcon must have same size
        int playStopW = playIcon.getIconWidth();
        int playStopH = playIcon.getIconHeight();
        int nextW = nextIcon.getIconWidth();
        int nextH = nextIcon.getIconHeight();
        int previousW = previousIcon.getIconWidth();
        int previousH = previousIcon.getIconHeight();

        this.setTitle("MusicPlayer");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(this.width,this.height);
        this.setLayout(null);

        songNameLabel.setBounds(
                (this.width/2) - playStopW,
                (int)(0.55 * this.height),
                500,50);
        songNameLabel.setFont(songNameFont);

        playPauseButton.setBounds(
                (this.width/2)-playStopW/2,
                (int)(0.65 * this.height),
                playStopW,playStopH);
        playPauseButton.setIcon(playIcon);
        playPauseButton.addActionListener(e -> playPauseMusic());

        nextButton.setBounds(
                (this.width/2)+playStopW,
                (int)(0.65 * this.height),
                nextW,nextH);
        nextButton.setVisible(false);
        nextButton.setIcon(nextIcon);
        nextButton.addActionListener(e -> nextMusic());

        previousButton.setBounds(
                (this.width/2)-2*playStopW,
                (int)(0.65 * this.height),
                previousW,previousH);
        previousButton.setVisible(false);
        previousButton.setIcon(previousIcon);
        previousButton.addActionListener(e -> previousMusic());

        SearchArtistBio.setBounds(
                0, 0, 250, 50);
        SearchArtistBio.addActionListener(e -> searchArtistBio());

        scrollPane = new JScrollPane(bioTextArea);
        bioTextArea.setEnabled(false);
        bioTextArea.setDisabledTextColor(Color.BLACK);
        bioTextArea.setLineWrap(true);        // Ενεργοποίηση αναδίπλωσης γραμμής
        bioTextArea.setWrapStyleWord(true);   // Αναδίπλωση ανά λέξη για καλύτερη εμφάνιση
        bioTextArea.setFont(new Font("SansSerif", Font.PLAIN, 14)); // Ρυθμιση της γραμματοσειράς για να είναι αναγνώσιμη
        scrollPane.setVisible(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(0,100,400,200);

        viewMoreButton.setVisible(false);
        viewMoreButton.setBounds(0,50,250,50);
        viewMoreButton.addActionListener(e -> openWikiPage());

        /*songSlider.setBounds((int)(this.width * 0.1),(int)(this.height-(this.height * 0.1)),
                (int)(this.width * 0.8), 20);
        songSlider.setVisible(false);
        songSlider.setMinimum(0);
        songSlider.setValue(0);
        songSlider.addChangeListener(e -> changeFramesOfSong());*/

        // Create a timer that updates every 1000 milliseconds (1 second)
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                    System.out.println("Frames of Song : " + frames);
                }*/
            }
        });
        timer.start();

        this.add(playPauseButton);
        this.add(nextButton);
        this.add(previousButton);
        //this.add(songSlider);
        this.add(songNameLabel);
        this.add(SearchArtistBio);
        this.add(viewMoreButton);
        this.add(scrollPane);
        this.add(mainPlaylistSP);

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void playPauseMusic() {
        nextButton.setVisible(true);
        previousButton.setVisible(true);
        //songSlider.setVisible(true);
        if (played) {
            playPauseButton.setIcon(playIcon);
            //framePosition = clip.getFramePosition();
            /*long songLengthMin = (clip.getMicrosecondLength()/60000000);
            long songLengthSec = (clip.getMicrosecondLength()%60000000)/1000000;
            System.out.println("Song length : " + songLengthMin + ":" + songLengthSec);*/
            clip.stop();
        } else {
            playPauseButton.setIcon(pauseIcon);
            if (!started) {
                System.out.println("Song : " + currentSong.getName());
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
            file = new File(currentSong.getPath());
            try {
                ais = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                e.printStackTrace();
            }
            //songSliderLength = clip.getFrameLength();
        }
        if (currentSong.getExcention().equals(".mp3")) {}
    }

    public void playAudio() {
        try {
            loadAudio();
            clip.open(ais);
            clip.addLineListener(e -> {
                if (e.getType() == LineEvent.Type.START) {
                    playPauseButton.setIcon(pauseIcon);
                    System.out.println("Song : Start");
                } else if (e.getType() == LineEvent.Type.STOP) {
                    playPauseButton.setIcon(playIcon);
                    framePosition = clip.getFramePosition();
                    if (clip.getFramePosition() < clip.getFrameLength()) {
                        System.out.println("Song : Stop");
                    } else {
                        played = false;
                        songFinished = true;
                        clip.close();
                    }
                } else if (e.getType() == LineEvent.Type.CLOSE) {
                    System.out.println("Song : Close");
                    framePosition = 0;
                    started = false;
                    isSongStarted = false;
                    if (songFinished) {
                        songFinished = false;
                        nextSong();
                    }
                }
            });
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
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
       /* ArtistBioSearcher abs;
        String artistName = JOptionPane.showInputDialog(this,
                "Enter the artist's name:",
                "Artist Bio Search", JOptionPane.QUESTION_MESSAGE);
        if (artistName != null && !artistName.trim().isEmpty()) {
            wikiURL = "https://en.wikipedia.org/wiki/" + artistName.replace(" ", "_");
            abs = new ArtistBioSearcher(artistName);
            String bio = abs.produceBio();
            if (bio != null) {
                bioTextArea.setText(bio);
                scrollPane.setVisible(true);
                viewMoreButton.setVisible(true);

                bioTextArea.setSize(bioTextArea.getPreferredSize());
                scrollPane.setSize(bioTextArea.getPreferredSize());
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please enter an artist's name.",
                    "No Artist Entered", JOptionPane.WARNING_MESSAGE);
        }*/
    }

    public void openWikiPage() {
        String wikiURL = (String) ((JButton) getContentPane().getComponent(2)).getClientProperty("wikiURL");
        if (wikiURL != null && Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(wikiURL));
            } catch (IOException | URISyntaxException e) {
                JOptionPane.showMessageDialog(this, "Error opening browser: " + e.getMessage(), "Browser Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Desktop is not supported on this system.", "Unsupported Operation", JOptionPane.ERROR_MESSAGE);
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

