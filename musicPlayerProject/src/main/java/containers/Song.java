package containers;

public class Song {
    private String name;
    private String artist;
    private String album;
    private String path;
    private String extension;
    //private String lyrics; todo
    //private String ImgSongPath; todo

    private boolean isHearted;
    private String heart = "❤\uFE0F";

    public Song(String path, String folderPath) {
        String songName = path.substring(0,path.lastIndexOf("."));
        this.name = songName.replace(folderPath,"");
        this.artist = "Unknown";
        this.album = "Unknown";
        this.path = path;
        this.extension = path.substring(path.lastIndexOf("."));
        this.isHearted = false;
    }

    public void printData() {
        System.out.println("Name       : " + getName());
        System.out.println("Artist     : " + getArtist());
        System.out.println("Album      : " + getAlbum());
        System.out.println("Path       : " + getPath());
        System.out.println("Εxtension  : " + getΕxtension());
        System.out.println("Is Hearted : " + isHearted());
    }

    public void addRemoveHeartFromName() {
        if (isHearted) {
            name = name + heart;
            System.out.println("1 " + name);
        } else {
            if (name.contains(heart)) {
                name = name.replace(heart,"");
                System.out.println("2 " + name);
            }
        }
    }

    public String getName() { return name; }
    public String getArtist() { return artist; }
    public String getAlbum() { return album; }
    public String getPath() { return path; }
    public String getΕxtension() { return extension; }

    public boolean isHearted() { return isHearted; }

    public void setName(String name) { this.name = name; }
    public void setArtist(String artist) { this.artist = artist; }
    public void setAlbum(String album) { this.album = album; }
    public void setPath(String path) { this.path = path; }
    public void setΕxtension(String extension) { this.extension = extension; }

    public void setHearted(boolean isHearted) { this.isHearted = isHearted; }
}