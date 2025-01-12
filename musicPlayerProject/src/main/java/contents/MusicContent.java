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
    private boolean isHearted = false;
    private boolean songFinished = false;
    public JLabel songNameLabel = new JLabel();
    public OvalButton playPauseButton = new OvalButton("", Util.orange_color);
    public OvalButton nextButton = new OvalButton("",Util.orange_color);
    public OvalButton previousButton = new OvalButton("",Util.orange_color);
    public JProgressBar progressBar;
    public Playlist mainPlaylist;
    public JScrollPane mainPlaylistSP;
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
                (int)(getWidth() * 0.02),(int)(getHeight() * 0.01),
                (int)(getWidth() * 0.96),(int)(getHeight() * 0.5165)),
                Util.blue_color,getBackground());
        mpf.getAllPlaylists().add(mainPlaylistSP);

        mpf.setCurPlaylist(mainPlaylist);

        //---Play/Pause Button---
        playPauseButton.setBounds((int)(getWidth() * 0.5) - (int)(getWidth() * 0.0368),
                (int)(0.85 * getHeight()), (int)(getWidth() * 0.0736),(int)(0.1033 * getHeight()));
        playPauseButton.setFocusable(false);
        playPauseButton.setText("▶");
        playPauseButton.setFont(Util.myFont);
        playPauseButton.addActionListener(e -> playPauseMusic());

        /*//---Title Label---
        songNameLabel.setBounds((getWidth()/2) - 250,
                (int)(0.55 * getHeight()), 500,50);
        songNameLabel.setFont(Util.songNameFont);*/

        //---Next Song Button---
        nextButton.setBounds((int)(getWidth() * 0.75) - (int)(getWidth() * 0.0368),
                (int)(0.85 * getHeight()),(int)(getWidth() * 0.0736),(int)(0.1033 * getHeight()));
        nextButton.setVisible(false);
        nextButton.setFocusable(false);
        nextButton.setText("⏩");
        nextButton.setFont(Util.myFont);
        nextButton.addActionListener(e -> nextSong());

        //---Previous Song Button---
        previousButton.setBounds((int)(getWidth() * 0.25) - (int)(getWidth() * 0.0368),
                (int)(0.85 * getHeight()),(int)(getWidth() * 0.0736),(int)(0.1033 * getHeight()));
        previousButton.setVisible(false);
        previousButton.setFocusable(false);
        previousButton.setText("⏪");
        previousButton.setFont(Util.myFont);
        previousButton.addActionListener(e -> previousSong());

        previousButton.setVisible(false);
        previousButton.setFocusable(false);
        previousButton.setText("⏪");

        //---Song Bar Slider---
        progressBar = new JProgressBar(0, 100);
        progressBar.setBounds((int)(getWidth() * 0.02),(int)(getHeight() * 0.75),
                (int)(getWidth() * 0.96),(int)(getHeight() * 0.0276));
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);

        progressBar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (clip != null) {
                    // Calculate the clicked position as a percentage
                    int mouseX = e.getX();
                    int progressBarWidth = progressBar.getWidth();
                    float clickPercent = (float) mouseX / progressBarWidth;

                    // Set the audio clip's frame position based on the percentage
                    int newFramePosition = (int) (clip.getFrameLength() * clickPercent);
                    clip.setFramePosition(newFramePosition);

                    // Update the progress bar value
                    progressBar.setValue((int) (clickPercent * 100));
                }
            }
        });

        startProgressBarTimer();

        add(progressBar);
        add(playPauseButton);
        add(nextButton);
        add(previousButton);
       /* add(songNameLabel);*/
        add(mainPlaylistSP);
    }

    private void updateProgressBar() {
        if (clip != null) {
            int currentFrame = clip.getFramePosition();
            int totalFrames = clip.getFrameLength();
            int progress = (int) ((double) currentFrame / totalFrames * 100);
            progressBar.setValue(progress);

            // Calculate current time and total time in seconds
            int currentTimeInSeconds = (int) (currentFrame / clip.getFormat().getFrameRate());
            int totalTimeInSeconds = (int) (totalFrames / clip.getFormat().getFrameRate());

            // Format as MM:SS
            String currentTime = formatTime(currentTimeInSeconds);
            String totalTime = formatTime(totalTimeInSeconds);

            // Update progress bar string
            progressBar.setString(currentTime + " / " + totalTime);
        } else {
            progressBar.setString("0:00 / 0:00");
        }
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%d:%02d", minutes, remainingSeconds);
    }

    private void startProgressBarTimer() {
        Timer timer = new Timer(100, e -> updateProgressBar()); // Update every 100 ms
        timer.start();
    }

    public void clearMusicContent() {
        if (clip != null) {
            clip.close();
            played = false;
            started = false;
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
        progressBar.setVisible(true);
        if (played) {
            playPauseButton.setText("▶");
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
        System.out.println("selectedSongNum : "+selectedSongNum);
        if (selectedSongNum != -1) {
            mpf.setCurSong(selectedSongNum);
            mpf.getCurPlaylist().setRecordBackgroundColor(Util.orange_dark_color,mpf.getCurSongNum());
            loadAudio();
            playPauseMusic();
        }
    }

    public void loadAudio() {
        if (mpf.getCurrentSong().getΕxtension().equals(".wav") ||
                mpf.getCurrentSong().getΕxtension().equals(".au")) {
            File file = new File(mpf.getCurrentSong().getPath());
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(ais);
            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                e.printStackTrace();
            }
        }
    }

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

 /*   public JLabel getSongNameLabel() {
        return songNameLabel;
    }*/
}