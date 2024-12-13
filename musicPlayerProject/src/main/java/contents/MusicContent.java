package contents;
import general.MusicPlayerFrame;
import general.Util;
import gui.OvalButton;
import gui.Playlist;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MusicContent extends JPanel implements Content {
    private MusicPlayerFrame mpf;
    private Clip clip;
    public boolean played = false;
    private boolean started = false;
    private boolean isSongStarted = false;
    private int framePosition = 0;
    //private JButton heartButton = new JButton("\uFE0F");
    private boolean isHearted = false;
    private boolean songFinished = false;
    private JLabel songNameLabel = new JLabel();
    public OvalButton playPauseButton = new OvalButton("", Util.orange_color);
    public OvalButton nextButton = new OvalButton("",Util.orange_color);
    public OvalButton previousButton = new OvalButton("",Util.orange_color);
    //private JSlider songSlider = new JSlider();
    public Playlist mainPlaylist;
    private JScrollPane mainPlaylistSP;
    private JComboBox playlistSelector;
    public MusicContent(LayoutManager layout, MusicPlayerFrame mpf) {
        super(layout);
        this.mpf = mpf;
    }

    @Override
    public void init() {
        //---Playlist---
        mainPlaylist = new Playlist("Main",Util.orange_color,20,
                mpf.getAllSongNames(),mpf, true);
        mainPlaylist.setRecordBackgroundColor(Util.orange_dark_color,mpf.getCurSongNum());

        mainPlaylistSP = Util.createScrollPane(mainPlaylist,new Rectangle(
                        (int)(getWidth() * 0.5) - 250,
                        (int)(getHeight() * 0.01),500,300),
                Util.blue_color,getBackground());
        mpf.getAllPlaylists().add(mainPlaylistSP);

        mpf.setCurPlaylist(mainPlaylist);

        //---Play/Pause Button---
        playPauseButton.setBounds((getWidth()/2) - 30,
                (int)(0.85 * getHeight()), 60,60);
        playPauseButton.setFocusable(false);
        playPauseButton.setText("▶");
        playPauseButton.setFont(Util.myFont);
        playPauseButton.addActionListener(e -> playPauseMusic());

        //---Title Label---
        songNameLabel.setBounds((getWidth()/2) - 250,
                (int)(0.55 * getHeight()), 500,50);
        songNameLabel.setFont(Util.songNameFont);

        //---Next Song Button---
        nextButton.setBounds((int)(getWidth() * 0.75) - 30,
                (int)(0.85 * getHeight()), 60,60);
        nextButton.setVisible(false);
        nextButton.setFocusable(false);
        nextButton.setText("⏩");
        nextButton.setFont(Util.myFont);
        nextButton.addActionListener(e -> nextSong());

        //---Previous Song Button---
        previousButton.setBounds((int)(getWidth() * 0.25) - 30,
                (int)(0.85 * getHeight()), 60,60);
        previousButton.setVisible(false);
        previousButton.setFocusable(false);
        previousButton.setText("⏪");
        previousButton.setFont(Util.myFont);
        previousButton.addActionListener(e -> previousSong());

        //heartButton.setBounds((int)(getWidth() * 0.85),
          //      (int)(getHeight() * 0.45), 60,60);

        previousButton.setVisible(false);
        previousButton.setFocusable(false);
        previousButton.setText("⏪");
        //heartButton.setFont(Util.myFont);

        //heartButton.addActionListener(e -> toggleHeart());

        //---Song Bar Slider---
        /*songSlider.setBounds((int)(musicContent.getWidth() * 0.02),
                (int)(musicContent.getHeight() * 0.97),
                (int)(musicContent.getWidth() * 0.96), 20);
        songSlider.setVisible(false);
        songSlider.setMinimum(0);
        songSlider.setValue(0);
        songSlider.addChangeListener(e -> changeFramesOfSong());*/

        //musicContent.add(songSlider);
        add(playPauseButton);
        add(nextButton);
        add(previousButton);
        add(songNameLabel);
        add(mainPlaylistSP);
    }

    public void clearMusicContent() {
        if (clip != null) {
            clip.close();
            played = false;
            playPauseButton.setText("▶");
            previousButton.setVisible(false);
            nextButton.setVisible(false);
            mpf.setCurSong(0);
            mpf.getCurPlaylist().setRecordBackgroundColor(Util.orange_dark_color,mpf.getCurSongNum());
        }
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
                System.out.println("Song : " + mpf.getCurrentSong().getName());
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

    public void goTo(String song) {
        if (clip != null) {
            clip.close();
        }
        framePosition = 0;
        played = false;
        started = false;
        int selectedSongNum = mpf.getSongNameNum(song);
        if (selectedSongNum != -1) {
            mpf.setCurSong(selectedSongNum);
            mpf.getCurPlaylist().setRecordBackgroundColor(Util.orange_dark_color,mpf.getCurSongNum());
            loadAudio();
            playPauseMusic();
        }
    }

    public void loadAudio() {
        if (mpf.getCurrentSong().getExcention().equals(".wav") ||
                mpf.getCurrentSong().getExcention().equals(".au")) {
            File file = new File(mpf.getCurrentSong().getPath());
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(ais);
            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                e.printStackTrace();
            }
            //songSliderLength = clip.getFrameLength();
        }
        if (mpf.getCurrentSong().getExcention().equals(".mp3")) {}
    }

    /*public void toggleHeart() {
        if (mpf.getCurrentSong().isHearted()) {
            heartButton.setText("\u2661");
            mpf.getCurrentSong().setHearted(false);
        } else {
            heartButton.setText("\uFE0F");
            mpf.getCurrentSong().setHearted(true);
        }
        mpf.getCurrentSong().addRemoveHeartFromName();
    }*/

    public void nextSong() {
        if (clip != null) {
            clip.close();
        }
        framePosition = 0;
        played = false;
        started = false;
        if (mpf.getCurSongNum() + 1 < mpf.getCurrentPLSongs().size()) {
            System.out.println("Next Song");
            mpf.setCurSong(mpf.getCurSongNum() + 1);
        } else {
            System.out.println("Loop");
            mpf.setCurSong(0);
        }
        mpf.getCurPlaylist().setRecordBackgroundColor(Util.orange_dark_color,mpf.getCurSongNum());
        loadAudio();
        playPauseMusic();
    }

    public void previousSong() {
        if (clip != null) {
            clip.close();
        }
        framePosition = 0;
        played = false;
        started = false;
        if (mpf.getCurSongNum() - 1 > -1) {
            System.out.println("Previous Song");
            mpf.setCurSong(mpf.getCurSongNum() - 1);
        } else {
            System.out.println("Loop");
            mpf.setCurSong(mpf.getCurrentPLSongs().size() - 1);
        }
        mpf.getCurPlaylist().setRecordBackgroundColor(Util.orange_dark_color,mpf.getCurSongNum());
        loadAudio();
        playPauseMusic();
    }

    public void playAudio() {
        loadAudio();
        clip.addLineListener(e -> {
            if (e.getType() == LineEvent.Type.START) {
                playPauseButton.setText("⏸");
                System.out.println("Song : Start");
            } else if (e.getType() == LineEvent.Type.STOP) {
                playPauseButton.setText("▶");
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
        clip.setFramePosition(framePosition);
        clip.start();
    }

    public JLabel getSongNameLabel() {
        return songNameLabel;
    }
    public Clip getClip() {
        return clip;
    }
    public JComboBox getPlaylistSelector() {
        return playlistSelector;
    }
}