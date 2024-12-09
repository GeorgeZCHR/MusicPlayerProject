package components;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private String email;
    private boolean admin;
    private List<String> songs = new ArrayList<>();

    // Default constructor (required by Firestore)
    public User() {
    }

    public User(String username, String password, String email,
                boolean admin, List<String> songs) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.admin = admin;
        this.songs.addAll(songs);
    }

    public void print() {
        System.out.println("Username : " + username);
        System.out.println("Password : " + password);
        System.out.println("Email    : " + email);
        System.out.println("Admin    : " + admin);
        System.out.println("Songs    :");
        for (int i = 0; i < songs.size(); i++) {
            System.out.println("           Song " + (i + 1) + "   : " + songs.get(i));
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
}
