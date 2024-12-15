package components;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirestoreManager {
    private String serviceAccountPath;
    private String databaseURL;
    private Firestore db;
    public FirestoreManager(String serviceAccountPath, String databaseURL) {
        this.serviceAccountPath = serviceAccountPath;
        this.databaseURL = databaseURL;
        this.db = init();
    }

    private Firestore init() {
        Firestore db = null;
        try {
            InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("serviceAccountKey.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(databaseURL)
                    .build();

            FirebaseApp.initializeApp(options);

            // Initialize Firestore using FirestoreClient
            db = FirestoreClient.getFirestore();

            System.out.println("Firebase Initialized Successfully!");
        } catch (IOException e) {
            System.err.println("Error initializing Firebase: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
        return db;
    }

    public User getUser(String email) {
        DocumentReference docRef = db.collection("users").document(email);

        ApiFuture<DocumentSnapshot> future = docRef.get(); // Asynchronous operation
        try {
            // Wait for the operation to complete
            DocumentSnapshot documentSnapshot = future.get();

            if (documentSnapshot.exists()) {
                Map<String,Object> userMap = documentSnapshot.getData();
                System.out.println();

                return new User(
                        userMap.get("username").toString(),
                        userMap.get("password").toString(),
                        userMap.get("email").toString(),
                        Boolean.parseBoolean(userMap.get("admin").toString()),
                        extractSongs(userMap.get("songs").toString())
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addSong(String email,String songName) {
        DocumentReference docRef = db.collection("users").document(email);

        // Update the songs array by adding a new song
        ApiFuture<WriteResult> future = docRef
                .update("songs", FieldValue.arrayUnion(songName));
        try {
            // Wait for the update operation to complete
            future.get();
            System.out.println("Songs array updated!");
        } catch (Exception e) {
            // Handle any errors
            System.err.println("Error updating document: " + e.getMessage());
        }
    }

    public void setNewUser(User user) {
        DocumentReference docRef = db.collection("users")
                .document(user.getEmail());

        // Asynchronous write
        ApiFuture<WriteResult> future = docRef.set(user);

        try {
            // Ensure the writing is completed
            future.get();
            System.out.println("Document successfully written!");
        } catch (Exception e) {
            System.err.println("Error writing document: " + e.getMessage());
        }
    }

    public void deleteSong(String email, String songName) {
        DocumentReference docRef = db.collection("users")
                .document(email);

        ApiFuture<WriteResult> future = docRef
                .update("songs", FieldValue.arrayRemove(songName));
        try {
            // Wait for the update operation to complete
            future.get();
            System.out.println("Songs array updated!");
        } catch (Exception e) {
            // Handle any errors
            System.err.println("Error updating document: " + e.getMessage());
        }
    }

    public void deleteUser(String email) {
        DocumentReference docRef = db.collection("users").document(email);

        // Delete the document
        ApiFuture<WriteResult> future = docRef.delete();

        try {
            // Wait for the delete operation to complete
            future.get();
            System.out.println("User with email '"+ email + "' deleted successfully!");
        } catch (Exception e) {
            // Handle any errors
            System.err.println("Error deleting document: " + e.getMessage());
        }
    }

    public void savePlaylist(String email, String playlistName, List<String> songs) {
        DocumentReference docRef = db.collection("playlists").document(playlistName);

        // Create or update the playlist with a list of songs
        ApiFuture<WriteResult> future = docRef.set(new UserPlaylist(email, playlistName, songs, LocalDate.now().toString()));

        try {
            future.get();
            System.out.println("Playlist saved successfully!");
        } catch (Exception e) {
            System.err.println("Error saving playlist: " + e.getMessage());
        }
    }

    public UserPlaylist getPlaylist(String playlistName) {
        DocumentReference docRef = db.collection("playlists").document(playlistName);

        ApiFuture<DocumentSnapshot> future = docRef.get();
        try {
            DocumentSnapshot documentSnapshot = future.get();

            if (documentSnapshot.exists()) {
                Map<String, Object> data = documentSnapshot.getData();
                String email = (String) data.get("email");
                String name = (String) data.get("name");
                List<String> songs = (List<String>) data.get("songs");

                return new UserPlaylist(email, name, songs,LocalDate.now().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> extractSongs(String input) {
        int start = input.indexOf('[');
        int end = input.indexOf(']');

        if (start != -1 && end != -1 && start < end) {
            String songsString = input.substring(start + 1, end);
            String[] songsArray = songsString.split(",");
            List<String> songsList = new ArrayList<>();
            for (String song : songsArray) {
                songsList.add(song.trim());
            }
            return songsList;
        }
        return new ArrayList<>();
    }
}