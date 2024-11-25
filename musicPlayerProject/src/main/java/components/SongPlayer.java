/*
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SongPlayer {
    private File file;
    private AudioInputStream ais;
    private Clip clip;
    private containers.Song song;
    private boolean played = false;
    private boolean started = false;
    private boolean songFinished = false;
    private int framePosition = 0, songNum = 0, a = 0;
    public SongPlayer(containers.Song song) {
        this.song = song;
    }

    public void changeSong(containers.Song song) {
        this.song = song;
    }

    public void loadAudio() {
        if (song.getExcention().equals(".wav") || song.getExcention().equals(".au")) {
            file = new File(song.getPath());
            try {
                ais = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
            } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }
        if (song.getExcention().equals(".mp3")) {}
    }

    public boolean playAudio() {
        try {
            loadAudio();
            clip.open(ais);
            clip.addLineListener(e -> {
                if (e.getType() == LineEvent.Type.START) {
                    System.out.println("containers.Song : Start");
                } else if (e.getType() == LineEvent.Type.STOP) {
                    if (clip.getFramePosition() < clip.getFrameLength()) {
                        System.out.println("containers.Song : Stop");
                    } else {
                        a = 1;
                        played = false;
                        songFinished = true;
                        clip.close();
                    }
                } else if (e.getType() == LineEvent.Type.CLOSE) {
                    System.out.println("containers.Song : Close");
                    framePosition = 0;
                    started = false;
                }
            });
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
        clip.setFramePosition(framePosition);
        clip.start();
        System.out.println("a = " + a);
        if (a == 1) {
            //songFinished = false;
            a = 0;
            return true;
        } else {
            return false;
        }
    }

    public void clipClose() {
        clip.close();
    }

    public void clipStop() {
        framePosition = clip.getFramePosition();
        clip.stop();
    }

    public int getFramePosition() {
        return clip.getFramePosition();
    }

    public void setFramePosition(int frames) {
        clip.setFramePosition(frames);
    }

    // Getters

    public containers.Song getSong() { return song; }
    public boolean isPlayed() { return played; }
    public boolean isStarted() { return started; }

    // Setters
    public void setPlayed(boolean played) { this.played = played;}

    public void setStarted(boolean started) { this.started = started; }
}*/
