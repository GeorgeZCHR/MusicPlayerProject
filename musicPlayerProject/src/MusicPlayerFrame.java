import javax.swing.*;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class MusicPlayerFrame extends JFrame {
    private int width;
    private int height;
    private boolean played = false;
    private int framePosition = -1;
    private String currentSongPath;
    private JButton playPauseButton = new JButton();
    private ImageIcon playIcon = new ImageIcon("img/play_button_200x200.png");
    private ImageIcon stopIcon = new ImageIcon("img/stop_button_200x200.png");
    private File file;
    private AudioInputStream ais;
    private Clip clip;
    private List<Song> allSongs = new ArrayList();
    public MusicPlayerFrame(int width, int height) {
        loadSongs("music/");
        /*// For debugging
        for (Song song : allSongs) {
            song.printData();
            System.out.println("-----------");
        }*/
        currentSongPath = allSongs.get(0).getPath();
        loadAudio(currentSongPath);
        this.width = width;
        this.height = height;

        int playStopW = playIcon.getIconWidth();
        int playStopY = playIcon.getIconHeight();

        this.setTitle("MusicPlayer");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(this.width,this.height);
        this.setLayout(null);

        playPauseButton.setBounds((this.width/2)-playStopW/2,(this.height/2)-playStopY/2,playStopW,playStopY);
        playPauseButton.setIcon(playIcon);
        playPauseButton.addActionListener(e -> playPauseMusic());

        clip.addLineListener(e -> {
            System.out.println("in");
            if (e.getType() == LineEvent.Type.STOP) {
                if (clip.getFramePosition() == clip.getFrameLength()) {
                    clip.close();
                    played = false;
                    System.out.println("Song : End");
                }
            }
        });

        this.add(playPauseButton);

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    void playPauseMusic() {
        if (played) {
            playPauseButton.setIcon(playIcon);
            framePosition = clip.getFramePosition();
            clip.close();
            System.out.println("Song : Pause");
        } else {
            playPauseButton.setIcon(stopIcon);
            playAudio(ais);
            System.out.println("Song : Start");
        }
        played = !played;
    }

    void loadAudio(String songPath) {
        file = new File(songPath);
        try {
            ais = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    void playAudio(AudioInputStream ais) {
        try {
            loadAudio(currentSongPath);
            clip.open(ais);
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
        clip.setFramePosition(framePosition);
        clip.start();
    }

    void loadSongs(String folderPath) {
        List<String> pathsOfSongs = new ArrayList(SongLoader.loadFromFolder(folderPath));
        for (String path : pathsOfSongs) {
            allSongs.add(new Song(path,folderPath));
        }
    }
}