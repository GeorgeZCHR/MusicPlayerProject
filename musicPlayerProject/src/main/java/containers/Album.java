package containers;

public class Album {
    private String name;
    private long playCount;
    private String URL;
    private String artistmbid;
    private String artistName;
    private String artistURL;
    private ImageHolder imageHolder;

    public Album(String name, long playCount, String URL, String artistmbid,
                 String artistName, String artistURL, ImageHolder imageHolder) {
        this.name = name;
        this.playCount = playCount;
        this.URL = URL;
        this.artistmbid = artistmbid;
        this.artistName = artistName;
        this.artistURL = artistURL;
        this.imageHolder = imageHolder;
    }

    public void print() {
        System.out.println("Name              : " + name);
        System.out.println("Play Count        : " + playCount);
        System.out.println("URL               : " + URL);
        System.out.println("Artist MBID       : " + artistmbid);
        System.out.println("Artist Name       : " + artistName);
        System.out.println("Artist URL        : " + artistURL);
        System.out.println("Small Image       : " + imageHolder.getSmallImage());
        System.out.println("Medium Image      : " + imageHolder.getMediumImage());
        System.out.println("Large Image       : " + imageHolder.getLargeImage());
        System.out.println("Extra Large Image : " + imageHolder.getExtraLargeImage());
        //System.out.println("Mega Image        : " + imageHolder.getMegaImage());
    }

    public String getName() { return name; }

    public long getPlayCount() { return playCount; }

    public String getURL() { return URL; }

    public String getArtistmbid() { return artistmbid; }

    public String getArtistName() { return artistName; }

    public String getArtistURL() { return artistURL; }

    public ImageHolder getImageHolder() { return imageHolder; }

    public void setName(String name) { this.name = name; }

    public void setPlayCount(long playCount) { this.playCount = playCount; }

    public void setURL(String URL) { this.URL = URL; }

    public void setArtistmbid(String artistmbid) { this.artistmbid = artistmbid; }

    public void setArtistName(String artistName) { this.artistName = artistName; }

    public void setArtistURL(String artistURL) { this.artistURL = artistURL; }

    public void setImageHolder(ImageHolder imageHolder) { this.imageHolder = imageHolder; }
}