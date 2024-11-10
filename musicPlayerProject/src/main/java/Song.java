

public class Song {
    private String name;
    private String artist;
    private String album;
    private String path;
    private String excention;
    //private String lyrics; todo
    //private String ImgSongPath; todo

    public Song(String path, String folderPath) {
        String songName = path.substring(0,path.lastIndexOf(".") - 1);
        this.name = songName.replace(folderPath,"");
        this.artist = "Unknown";
        this.album = "Unknown";
        this.path = path;
        this.excention = path.substring(path.lastIndexOf("."));
    }

    public void printData() {
        System.out.println(getName());
        System.out.println(getArtist());
        System.out.println(getAlbum());
        System.out.println(getExcention());
        System.out.println(getPath());
    }

    public String getName() { return name; }
    public String getArtist() { return artist; }
    public String getAlbum() { return album; }
    public String getPath() { return path; }
    public String getExcention() { return excention; }

    public void setName(String name) { this.name = name; }
    public void setArtist(String artist) { this.artist = artist; }
    public void setAlbum(String album) { this.album = album; }
    public void setPath(String path) { this.path = path; }
    public void setExcention(String excention) { this.excention = excention; }
}