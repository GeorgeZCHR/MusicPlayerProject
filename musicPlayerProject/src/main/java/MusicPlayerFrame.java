package main.java;

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
    private JButton searchBioButton = new JButton("Search Bio of Artist");
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
    private Timer sliderUpdateTimer;
    //private SongPlayer songPlayer;
    private Font songNameFont = new Font(Font.SANS_SERIF, Font.BOLD,30);
    public MusicPlayerFrame(int width, int height) {
        loadSongs("music/");
        int i = 0;
        // For debugging
        for (Song song : allSongs) {
            System.out.println("#" + i);
            song.printData();
            i++;
            System.out.println("-----------");
        }
        currentSong = allSongs.get(currentSongNum);
        //songPlayer = new SongPlayer(currentSong);
        //loadAudio();
        this.width = width;
        this.height = height;

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
                (this.height/2) - 200,
                500,50);
        songNameLabel.setFont(songNameFont);


        SearchArtistBio.setBounds(
                0, 0, 250, 50);
        SearchArtistBio.addActionListener(e ->  new SearchArtistBio());

        playPauseButton.setBounds(
                (this.width/2)-playStopW/2,
                (this.height/2)-playStopH/2,
                playStopW,playStopH);
        playPauseButton.setIcon(playIcon);
        playPauseButton.addActionListener(e -> playPauseMusic());

        nextButton.setBounds(
                (this.width/2)+playStopW,
                (this.height/2)-playStopH/2,
                nextW,nextH);
        nextButton.setVisible(false);
        nextButton.setIcon(nextIcon);
        nextButton.addActionListener(e -> nextMusic());

        previousButton.setBounds(
                (this.width/2)-2*playStopW,
                (this.height/2)-playStopH/2,
                previousW,previousH);
        previousButton.setVisible(false);
        previousButton.setIcon(previousIcon);
        previousButton.addActionListener(e -> previousMusic());

        searchBioButton.setBounds(0,51,250,50);



        bioTextArea.setWrapStyleWord(true);
        bioTextArea.setLineWrap(true);
        bioTextArea.setEditable(false);
        scrollPane = new JScrollPane(bioTextArea);
        scrollPane.setBounds(780,0,300,200);



        viewMoreButton.setEnabled(false); // Initially disabled
        viewMoreButton.setBounds(780,201,300,200);

        // Add action listeners
        searchBioButton.addActionListener(e -> {
            searchArtistBio(viewMoreButton);
        });

        viewMoreButton.addActionListener(e -> {
            openWikiPage();
        });

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

        this.setLocationRelativeTo(null);
        this.setVisible(true);







        // Create and add the search button



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


    private void searchArtistBio(JButton viewMoreButton) {
        // Prompt the user to input an artist name
        String artistName = JOptionPane.showInputDialog(this, "Enter the artist's name:", "Artist Bio Search", JOptionPane.QUESTION_MESSAGE);

        if (artistName != null && !artistName.trim().isEmpty()) {
            // Construct the Wikipedia API URL
            String wikipediaApiUrl = "https://en.wikipedia.org/api/rest_v1/page/summary/" + artistName.replace(" ", "_");

            try {
                // Fetch and display the first paragraph of the bio
                String bio = fetchFirstParagraph(wikipediaApiUrl);
                bioTextArea.setText(bio);

                // Enable the "View More Bio" button and store the Wikipedia page URL
                viewMoreButton.setEnabled(true);
                viewMoreButton.putClientProperty("wikiURL", "https://en.wikipedia.org/wiki/" + artistName.replace(" ", "_"));

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error fetching bio: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter an artist's name.", "No Artist Entered", JOptionPane.WARNING_MESSAGE);
        }
    }

    private String fetchFirstParagraph(String apiUrl) throws IOException {
        // Connect to Wikipedia API and fetch the summary
        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // Parse the JSON response to get the first paragraph
            String json = response.toString();
            int startIdx = json.indexOf("\"extract\":\"") + 11;
            int endIdx = json.indexOf("\",\"extract_html\"");
            if (startIdx > 0 && endIdx > startIdx) {
                return json.substring(startIdx, endIdx).replace("\\n", "\n");
            }
            return "No bio available for this artist.";
        }
    }

    private void openWikiPage() {
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