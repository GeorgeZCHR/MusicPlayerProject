package components;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private String username;
    private String password;
    private String email;
    private boolean admin;
    private List<String> songs = new ArrayList<>();
    private Map<String,Boolean> lovedMap = new HashMap<>();
    private Map<String,List<String>> playlistsMap = new HashMap<>();
    // Default constructor (required by Firestore)
    public User() {
    }

    public User(String username, String password, String email,
                boolean admin, List<String> songs, Map<String,Boolean> lovedMap,
                Map<String,List<String>> playlistsMap) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.admin = admin;
        this.songs.addAll(songs);
        this.lovedMap = lovedMap;
        this.playlistsMap = playlistsMap;
    }

    public void print() {
        System.out.println("Username  : " + username);
        System.out.println("Password  : " + password);
        System.out.println("Email     : " + email);
        System.out.println("Admin     : " + admin);
        System.out.println("Songs     :");
        for (int i = 0; i < songs.size(); i++) {
            System.out.println("            Song "+(i + 1)+"   : "+songs.get(i)+" "+lovedMap.get(songs.get(i)));
        }
        int i = 1;
        for (Map.Entry<String, List<String>> playlist : playlistsMap.entrySet()) {
            System.out.println("            Playlist "+i+" : "+playlist.getKey());
            List<String> names = playlist.getValue();
            for (int j = 0; j < names.size(); j++) {
                System.out.println("                                  "+names.get(j));
            }
            i++;
        }
    }


    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public List<String> getSongs() {
        return songs;
    }

    public void setData(List<String> songs) {
        this.songs.addAll(songs);
    }

    public void setSongs(List<String> songs) {
        this.songs.addAll(songs);
    }

    public Map<String,Boolean> getLovedMap() {
        return lovedMap;
    }

    public void setLovedMap(Map<String,Boolean> lovedMap) {
        this.lovedMap = lovedMap;
    }

    public Map<String,List<String>> getPlaylistsMap() {
        return playlistsMap;
    }

    public void setPlaylistsMap(Map<String,List<String>> playlistsMap) {
        this.playlistsMap = playlistsMap;
    }

}