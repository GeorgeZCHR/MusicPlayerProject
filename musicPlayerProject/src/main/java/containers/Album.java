package containers;

public class Album {
    private String title;
    private long play_count;
    private String genre;
    private String artist_name;
    private String url;
    private long streamable;
    private ImageHolder imageHolder;
    private String release_date;
    private long track_count;

    public Album(String title, long play_count, String genre, String artist_name, String url,
                  ImageHolder imageHolder,long track_count,String release_date) {
        this.title = title;
        this.play_count = play_count;
        this.genre = genre;
        this.artist_name = artist_name;
        this.url = url;
        this.imageHolder = imageHolder;
        this.track_count = track_count;
        this.release_date = release_date;
    }

    public void print() {
        System.out.println("Title              : " + title);
        System.out.println("Play Count        : " + play_count);
        System.out.println("Genre         : " + genre);
        System.out.println("Artist Name              : " + artist_name);
        System.out.println("URL               : " + url);
        System.out.println("Streamable        : " + streamable);
        System.out.println("Small Image       : " + imageHolder.getSmallImage());
        System.out.println("Medium Image      : " + imageHolder.getMediumImage());
        System.out.println("Large Image       : " + imageHolder.getLargeImage());
        System.out.println("Extra Large Image : " + imageHolder.getExtraLargeImage());
        System.out.println("Mega Image        : " + imageHolder.getMegaImage());
        System.out.println("Track Count        : " + track_count);
        System.out.println("Release Date        : " + release_date);
    }

    public String getTitle() { return title; }

    public long getPlayCount() { return play_count; }

    public String getGenre() { return genre; }

    public String getArtistName() { return artist_name; }

    public String getUrl() { return url; }

    public long getStreamable() { return streamable; }

    public ImageHolder getImageHolder() { return imageHolder; }

    public void setTitle(String title) { this.title = title; }

    public void setPlayCount(int playCount) { this.play_count = play_count; }

    public void setGenre(String genre) { this.genre = genre; }

    public void setArtistName(String artist_name) { this.artist_name = artist_name; }

    public void setUrl(String url) { this.url = url; }

    public void setStreamable(int streamable) { this.streamable = streamable; }

    public void setImageHolder(ImageHolder imageHolder) { this.imageHolder = imageHolder; }
}
