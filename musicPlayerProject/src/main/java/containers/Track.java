package containers;

public class Track {
    private String name;
    private long playCount;
    private long listeners;
    private String mbid;
    private String url;
    private ImageHolder imageHolder;
    private long duration;
    private long streamableText;
    private long streamableFulltrack;
    private String artistMBID;
    private String artistName;
    private String artistURL;

    public Track(String name, long playCount, long listeners, String mbid, String url,
                  ImageHolder imageHolder, long duration, long streamableText,
                 long streamableFulltrack, String artistMBID, String artistName,
                 String artistURL) {
        this.name = name;
        this.playCount = playCount;
        this.listeners = listeners;
        this.mbid = mbid;
        this.url = url;
        this.imageHolder = imageHolder;
        this.duration = duration;
        this.streamableText = streamableText;
        this.streamableFulltrack = streamableFulltrack;
        this.artistMBID = artistMBID;
        this.artistName =artistName;
        this.artistURL = artistURL;
    }

    public void print() {
        System.out.println("Name                  : " + name);
        System.out.println("Play Count            : " + playCount);
        System.out.println("Listeners             : " + listeners);
        System.out.println("MBID                  : " + mbid);
        System.out.println("URL                   : " + url);
        System.out.println("Small Image           : " + imageHolder.getSmallImage());
        System.out.println("Medium Image          : " + imageHolder.getMediumImage());
        System.out.println("Large Image           : " + imageHolder.getLargeImage());
        System.out.println("Extra Large Image     : " + imageHolder.getExtraLargeImage());
        System.out.println("Mega Image            : " + imageHolder.getMegaImage());
        System.out.println("Duration              : " + duration);
        System.out.println("Streamable Text       : " + streamableText);
        System.out.println("Streamable Full Track : " + streamableFulltrack);
        System.out.println("Artist MBID           : " + artistMBID);
        System.out.println("Artist Name           : " + artistName);
        System.out.println("Artist URL            : " + artistURL);
    }

    public String getName() { return name; }

    public long getPlayCount() { return playCount; }

    public long getListeners() { return listeners; }

    public String getMbid() { return mbid; }

    public String getUrl() { return url; }

    public ImageHolder getImageHolder() { return imageHolder; }

    public long getDuration() { return duration; }

    public long getStreamableText() { return streamableText; }

    public long getStreamableFulltrack() { return streamableFulltrack; }

    public String getArtistMBID() { return artistMBID; }

    public String getArtistName() { return artistName; }

    public String getArtistURL() { return artistURL; }

    public void setName(String name) { this.name = name; }

    public void setPlayCount(int playCount) { this.playCount = playCount; }

    public void setListeners(int listeners) { this.listeners = listeners; }

    public void setMbid(String mbid) { this.mbid = mbid; }

    public void setUrl(String url) { this.url = url; }

    public void setImageHolder(ImageHolder imageHolder) { this.imageHolder = imageHolder; }

    public void setDuration(long duration) { this.duration = duration; }

    public void setStreamableText(long streamableText) { this.streamableText = streamableText; }

    public void setStreamableFulltrack(long streamableFulltrack) { this.streamableFulltrack = streamableFulltrack; }

    public void setArtistMBID(String artistMBID) { this.artistMBID = artistMBID; }

    public void setArtistName(String artistName) { this.artistName = artistName; }

    public void setArtistURL(String artistURL) { this.artistURL = artistURL; }
}