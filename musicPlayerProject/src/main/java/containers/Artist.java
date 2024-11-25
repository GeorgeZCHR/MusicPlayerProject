package containers;

public class Artist {
    private String name;
    private long playCount;
    private long listeners;
    private String mbid;
    private String url;
    private long streamable;
    private ImageHolder imageHolder;

    public Artist(String name, long playCount, long listeners, String mbid, String url, long streamable,
                  ImageHolder imageHolder) {
        this.name = name;
        this.playCount = playCount;
        this.listeners = listeners;
        this.mbid = mbid;
        this.url = url;
        this.streamable = streamable;
        this.imageHolder = imageHolder;
    }

    public void print() {
        System.out.println("Name              : " + name);
        System.out.println("Play Count        : " + playCount);
        System.out.println("Listeners         : " + listeners);
        System.out.println("MBID              : " + mbid);
        System.out.println("URL               : " + url);
        System.out.println("Streamable        : " + streamable);
        System.out.println("Small Image       : " + imageHolder.getSmallImage());
        System.out.println("Medium Image      : " + imageHolder.getMediumImage());
        System.out.println("Large Image       : " + imageHolder.getLargeImage());
        System.out.println("Extra Large Image : " + imageHolder.getExtraLargeImage());
        System.out.println("Mega Image        : " + imageHolder.getMegaImage());
    }

    public String getName() { return name; }

    public long getPlayCount() { return playCount; }

    public long getListeners() { return listeners; }

    public String getMbid() { return mbid; }

    public String getUrl() { return url; }

    public long getStreamable() { return streamable; }

    public ImageHolder getImageHolder() { return imageHolder; }

    public void setName(String name) { this.name = name; }

    public void setPlayCount(int playCount) { this.playCount = playCount; }

    public void setListeners(int listeners) { this.listeners = listeners; }

    public void setMbid(String mbid) { this.mbid = mbid; }

    public void setUrl(String url) { this.url = url; }

    public void setStreamable(int streamable) { this.streamable = streamable; }

    public void setImageHolder(ImageHolder imageHolder) { this.imageHolder = imageHolder; }
}