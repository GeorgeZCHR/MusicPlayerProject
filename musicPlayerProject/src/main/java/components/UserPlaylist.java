package components;

import java.util.List;

public class UserPlaylist {
    private String ownerEmail;  // Email of the user who owns the playlist
    private String playlistName;  // Name of the playlist
    private List<String> songs;  // List of song names or IDs
    private String creationDate;  // Date when the playlist was created

    // Constructor to initialize the playlist
    public UserPlaylist(String ownerEmail, String playlistName, List<String> songs, String creationDate) {
        this.ownerEmail = ownerEmail;
        this.playlistName = playlistName;
        this.songs = songs;
        this.creationDate = creationDate;
    }

    // Getter and Setter for ownerEmail
    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    // Getter and Setter for playlistName
    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    // Getter and Setter for songs
    public List<String> getSongs() {
        return songs;
    }

    public void setSongs(List<String> songs) {
        this.songs = songs;
    }

    // Getter and Setter for creationDate
    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    // Method to add a new song to the playlist
    public void addSong(String song) {
        this.songs.add(song);
    }

    // Method to remove a song from the playlist
    public void removeSong(String song) {
        this.songs.remove(song);
    }

    // ToString method to display the playlist details
    @Override
    public String toString() {
        return "UserPlaylist{" +
                "ownerEmail='" + ownerEmail + '\'' +
                ", playlistName='" + playlistName + '\'' +
                ", songs=" + songs +
                ", creationDate='" + creationDate + '\'' +
                '}';
    }
}