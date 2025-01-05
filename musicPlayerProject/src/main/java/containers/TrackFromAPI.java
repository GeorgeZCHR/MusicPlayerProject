package containers;

public class TrackFromAPI {
    private String title, artist, id, originalTitle;
    private int duration;
    public TrackFromAPI(String title, String artist, String id,
                        String originalTitle, int duration) {
        this.title = title;
        this.artist = artist;
        this.id = id;
        this.originalTitle = originalTitle;
        this.duration = duration;
    }

    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getId() { return id; }
    public String getOriginalTitle() { return originalTitle; }
    public int getDuration() { return duration; }
}
