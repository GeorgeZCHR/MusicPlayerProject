package components;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
            FileInputStream serviceAccount =
                    new FileInputStream(serviceAccountPath);

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
                        extractSongs(userMap.get("songs").toString()),
                        extractLovedList(userMap.get("loved").toString()),
                        extractPlaylists(userMap.get("playlists").toString())
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addSong(String email,String songName, boolean loved) {
        DocumentReference docRef = db.collection("users").document(email);

        Map<String, Object> updates = new HashMap<>();
        updates.put("songs", FieldValue.arrayUnion(songName)); // Add songs to the array
        updates.put("loved." + songName, loved); // Update the loved map


        // Perform the updates
        ApiFuture<WriteResult> future = docRef.update(updates);
        try {
            // Wait for the update operation to complete
            future.get();
            System.out.println(songName+" updated from base!");
        } catch (Exception e) {
            // Handle any errors
            System.err.println("Error updating document: " + e.getMessage());
        }
    }
    public void addSongs(String email,List<String> songNames,Map<String,Boolean> lovedMap) {
        DocumentReference docRef = db.collection("users").document(email);

        Map<String, Object> updates = new HashMap<>();
        updates.put("songs", FieldValue.arrayUnion(songNames.toArray()));// Add songs to the array
        for (int i = 0; i < songNames.size(); i++) {
            updates.put("loved." + songNames.get(i), lovedMap.get(songNames.get(i)));
        }
        //updates.put("loved", Fie); // Update the loved map

        // Perform the updates
        ApiFuture<WriteResult> future = docRef.update(updates);
        try {
            // Wait for the update operation to complete
            future.get();
            System.out.println("\nSongs array updated from base!\n");
        } catch (Exception e) {
            // Handle any errors
            System.err.println("Error updating document: " + e.getMessage());
        }
    }

    public void updateLovedList(String email,List<String> songNames,List<Boolean> lovedList) {
        DocumentReference docRef = db.collection("users").document(email);

        Map<String, Object> updates = new HashMap<>();
        for (int i = 0; i < songNames.size(); i++) {
            updates.put("loved." + songNames.get(i), lovedList.get(i));
        }

        // Perform the updates
        ApiFuture<WriteResult> future = docRef.update(updates);
        try {
            // Wait for the update operation to complete
            future.get();
            System.out.println("Songs array updated!");
        } catch (Exception e) {
            // Handle any errors
            System.err.println("Error updating document: " + e.getMessage());
        }
    }

    public void updateLoved(String email,String songName,Boolean loved) {
        DocumentReference docRef = db.collection("users").document(email);

        // Perform the update
        ApiFuture<WriteResult> future = docRef.update("loved."+songName,loved);
        try {
            // Wait for the update operation to complete
            future.get();
            System.out.println(songName + "'s loved updated from base!");
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

    public void deleteSongs(String email, List<String> songNames) {
        DocumentReference docRef = db.collection("users")
                .document(email);

        ApiFuture<WriteResult> future = docRef
                .update("songs", FieldValue.arrayRemove(songNames.toArray()));
        try {
            // Wait for the update operation to complete
            future.get();
            System.out.println("Songs array updated!");
        } catch (Exception e) {
            // Handle any errors
            System.err.println("Error updating document: " + e.getMessage());
        }
    }

    public void deleteSongs(String email) {
        DocumentReference docRef = db.collection("users")
                .document(email);
        User user = getUser(email);
        if (!user.getSongs().isEmpty()) {
            ApiFuture<WriteResult> future = docRef
                    .update("songs", FieldValue.arrayRemove(user.getSongs().toArray()));
            try {
                // Wait for the update operation to complete
                future.get();
                System.out.println("All Songs deleted from base!");
            } catch (Exception e) {
                // Handle any errors
                System.err.println("Error updating document: " + e.getMessage());
            }
        } else {
            System.out.println(user.getUsername()+" have 0 songs\nDeletion cannot happen.");
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

    public void updateUser(String email) {
        DocumentReference docRef = db.collection("users").document(email);

        // Update the document
        /*ApiFuture<WriteResult> future = docRef
                .update("songs", FieldValue.arrayUnion(songName));*/

        try {
            // Wait for the delete operation to complete
            //future.get();
            System.out.println("User with email '"+ email + "' updated successfully!");
        } catch (Exception e) {
            // Handle any errors
            System.err.println("Error updating document: " + e.getMessage());
        }
    }

    public void addPlaylist(String email,String playlistName,List<String> songsNames) {
        DocumentReference docRef = db.collection("users").document(email);
        if (playlistName.isEmpty()) {
            System.out.println("You are trying to add a nameless playlist.\nIt must have at least 1 char for title.");
            return;
        }
        if (songsNames.isEmpty()) {
            System.out.println("You are trying to add an empty playlist.\nIt must have at least 1 song inside.");
            return;
        }

        ApiFuture<WriteResult> future = docRef
                .update("playlists." + playlistName, FieldValue.arrayUnion(songsNames.toArray()));

        try {
            // Wait for the delete operation to complete
            future.get();
            System.out.println("Playlist : "+playlistName+" with songs :"+songsNames+" was added to base!");
        } catch (Exception e) {
            // Handle any errors
            System.err.println("Error updating document: " + e.getMessage());
        }
    }

    public void deletePlaylist(String email,String playlistName) {
        DocumentReference docRef = db.collection("users").document(email);

        ApiFuture<WriteResult> future = docRef
                .update("playlists." + playlistName, FieldValue.delete());

        try {
            // Wait for the delete operation to complete
            future.get();
            System.out.println("Playlist : "+playlistName+" was deleted from base!");
        } catch (Exception e) {
            // Handle any errors
            System.err.println("Error updating document: " + e.getMessage());
        }
    }

    public List<String> extractSongs(String input) {
        // Find the indices of '[' and ']'
        int start = input.indexOf('[');
        int end = input.indexOf(']');

        // Extract the substring containing the songs
        if (input.equals("[]")) {
            return new ArrayList<>();
        } else if (start != -1 && end != -1 && start < end)  {
            String songsString = input.substring(start + 1, end);

            // Split the string by commas and trim whitespace
            String[] songsArray = songsString.split(",");
            List<String> songsList = new ArrayList<>();
            for (String song : songsArray) {
                songsList.add(song.trim());
            }

            return songsList;
        }
        // Return an empty list if the format is incorrect
        return new ArrayList<>();
    }

    public Map<String,Boolean> extractLovedList(String input) {
        int start = input.indexOf('{');
        int end = input.indexOf('}');

        Map<String,Boolean> map = new HashMap<>();
        if (input.equals("{}")) {
            return map;
        } else if (start != -1 && end != -1 && start < end) {
            String lovedString = input.substring(start + 1, end);

            String[] lovedArray = lovedString.split(",");
            for (int i = 0; i < lovedArray.length; i++) {
                String[] lovedArray2 = lovedArray[i].trim().split("=");
                boolean temp = lovedArray2[1].equals("true");
                map.put(lovedArray2[0],temp);
            }
            return map;
        }
        // Return an empty list if the format is incorrect
        return map;
    }

    public Map<String,List<String>> extractPlaylists(String input) {
        int start = input.indexOf('{');
        int end = input.indexOf('}');

        Map<String,List<String>> map = new HashMap<>();
        if (input.equals("{}")) {
            return map;
        } else if (start != -1 && end != -1 && start < end) {
            String playlistsString = input.substring(start + 1, end); // removes {}

            String[] playlistsArray = playlistsString.split("]");
            for (int i = 0; i < playlistsArray.length; i++) {
                String[] playlistArray = playlistsArray[i].split("=");
                if (playlistArray[0].contains(",")) {
                    playlistArray[0] = playlistArray[0].replace(",","").trim();
                }
                playlistArray[1] = playlistArray[1].replace("[","");
                String[] names = playlistArray[1].split(",");
                List<String> nameList = new ArrayList<>();
                for (int j = 0; j < names.length; j++) {
                    nameList.add(names[j].trim());
                }
                map.put(playlistArray[0],nameList);
            }
            return map;
        }
        // Return an empty list if the format is incorrect
        return map;
    }
}