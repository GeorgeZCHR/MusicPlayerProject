package components;
import com.google.api.core.ApiFuture;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FirebaseDatabaseManager {

    private final DatabaseReference databaseReference;

    public FirebaseDatabaseManager() {
        // Initialize the DatabaseReference
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public CompletableFuture<Void> saveSong(String userId, String songUrl) {
        DatabaseReference songsRef = databaseReference.child("Users").child(userId).child("songs");
        String songId = songsRef.push().getKey();

        if (songId != null) {
            ApiFuture<Void> apiFuture = songsRef.child(songId).setValueAsync(songUrl);
            return apiFutureToCompletableFuture(apiFuture);
        }

        return CompletableFuture.failedFuture(new IllegalArgumentException("Song ID could not be generated."));
    }

    public CompletableFuture<Void> saveArtist(String userId, String artistUrl) {
        DatabaseReference artistsRef = databaseReference.child("Users").child(userId).child("artists");
        String artistId = artistsRef.push().getKey();

        if (artistId != null) {
            ApiFuture<Void> apiFuture = artistsRef.child(artistId).setValueAsync(artistUrl);
            return apiFutureToCompletableFuture(apiFuture);
        }

        return CompletableFuture.failedFuture(new IllegalArgumentException("Artist ID could not be generated."));
    }

    public CompletableFuture<Void> saveAlbum(String userId, String albumUrl) {
        DatabaseReference albumsRef = databaseReference.child("Users").child(userId).child("albums");
        String albumId = albumsRef.push().getKey();

        if (albumId != null) {
            ApiFuture<Void> apiFuture = albumsRef.child(albumId).setValueAsync(albumUrl);
            return apiFutureToCompletableFuture(apiFuture);
        }

        return CompletableFuture.failedFuture(new IllegalArgumentException("Album ID could not be generated."));
    }

    // Function to get songs for a specific userId
    public CompletableFuture<List<String>> getSongs(String userId) {
        return getItemsForUser(userId, "songs");
    }

    // Function to get albums for a specific userId
    public CompletableFuture<List<String>> getAlbums(String userId) {
        return getItemsForUser(userId, "albums");
    }

    // Function to get artists for a specific userId
    public CompletableFuture<List<String>> getArtists(String userId) {
        return getItemsForUser(userId, "artists");
    }

    // Generic function to fetch items
    private CompletableFuture<List<String>> getItemsForUser(String userId, String category) {
        CompletableFuture<List<String>> future = new CompletableFuture<>();
        Query query = databaseReference.child("Users").child(userId).child(category);

        query.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<String> items = new ArrayList<>();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    String itemUrl = itemSnapshot.getValue(String.class);
                    if (itemUrl != null) {
                        items.add(itemUrl);
                    }
                }
                future.complete(items);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(new Exception("Data fetch cancelled: " + error.getMessage()));
            }
        });

        return future;
    }

    // Inline conversion method
    private <T> CompletableFuture<T> apiFutureToCompletableFuture(ApiFuture<T> apiFuture) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        apiFuture.addListener(() -> {
            try {
                completableFuture.complete(apiFuture.get());
            } catch (Exception e) {
                completableFuture.completeExceptionally(e);
            }
        }, Runnable::run); // Run on the current thread
        return completableFuture;
    }
}