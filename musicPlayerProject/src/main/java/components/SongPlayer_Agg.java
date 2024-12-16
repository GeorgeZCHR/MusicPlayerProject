package components;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import org.json.JSONArray;
import org.json.JSONObject;

public class SongPlayer_Agg {
    private static final String API_BASE_URL = "https://discoveryprovider.audius.co/v1";

    /**
     * Search for a song on Audius
     * @param query The name of the song or artist
     * @return The JSON response containing search results
     */
    public JSONObject searchSong(String query) {
        try {
            String urlString = API_BASE_URL + "/tracks/search?query=" + query.replace(" ", "%20");
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            if (connection.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP Error code : " + connection.getResponseCode());
            }

            InputStream responseStream = connection.getInputStream();
            Scanner scanner = new Scanner(responseStream).useDelimiter("\\A");
            String response = scanner.hasNext() ? scanner.next() : "";

            connection.disconnect();
            return new JSONObject(response);
        } catch (Exception e) {
            System.out.println("Error fetching song: " + e.getMessage());
            return null;
        }
    }

    /**
     * Play a song from its stream URL
     * @param streamUrl The URL of the song's stream
     */
    public void playSong(String streamUrl) {
        try {
            URL url = new URL(streamUrl);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            System.out.println("Playing song...");
            Thread.sleep(clip.getMicrosecondLength() / 1000); // Wait for the song to finish
        } catch (Exception e) {
            System.out.println("Error playing song: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SongPlayer_Agg player = new SongPlayer_Agg();
        String query = "Skrillex"; // Replace with your search query

        // Search for a song
        JSONObject results = player.searchSong(query);
        if (results != null) {
            JSONArray tracks = results.getJSONArray("data");
            if (tracks.length() > 0) {
                JSONObject firstTrack = tracks.getJSONObject(0); // Take the first result
                System.out.println("Playing: " + firstTrack.getString("title") + " by " +
                        firstTrack.getJSONObject("user").getString("name"));

                // Play the song
                player.playSong(firstTrack.getString("stream_url"));
            } else {
                System.out.println("No results found.");
            }
        }
    }
}