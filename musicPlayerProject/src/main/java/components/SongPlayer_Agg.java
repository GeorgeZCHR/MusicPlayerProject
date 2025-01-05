package components;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import org.json.JSONArray;
import org.json.JSONObject;
import gui.*;

public class SongPlayer_Agg {
    private static final String API_BASE_URL = "https://discoveryprovider.audius.co/v1";

    /**
     * Search for a song on Audius
     * @param query The name of the song or artist
     * @return The JSON response containing search results
     */
    public JSONObject searchArtist(String query) {
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

    public String downloadSongToFolder(String folder, String id, String mp3SongNamePath) {
        try {
            URL url = new URL(API_BASE_URL + "/tracks/" + id + "/stream");

            // Replace with your input MP3 file and desired output WAV file
            String outputWAV = folder + mp3SongNamePath.substring(0,mp3SongNamePath.lastIndexOf(".")) + ".wav";

            try {
                MP3ToWAVConverter.convertMP3ToWAV(url, outputWAV);
                System.out.println("Conversion complete : " + outputWAV);
                return outputWAV;
            } catch (Exception e) {
                System.out.println("Error with the Conversion : " + e.getMessage());
                e.printStackTrace();
                new WarningFrame("Error with Download",
                        "There was some error with the "+mp3SongNamePath+"!");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error with the URL : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Play a song from its stream URL
     * @param id The song id from the api
     * @param mp3SongNamePath The original name path of the mp3 song
     */
    public void playSong(String id, String mp3SongNamePath) {
        try {
            URL url = new URL(API_BASE_URL + "/tracks/" + id + "/stream");

            // Replace with your input MP3 file and desired output WAV file
            String outputWAV = "music/" + mp3SongNamePath.substring(0,mp3SongNamePath.lastIndexOf(".")) + ".wav";

            try {
                MP3ToWAVConverter.convertMP3ToWAV(url, outputWAV);
                System.out.println("Conversion complete: " + outputWAV);
            } catch (Exception e) {
                e.printStackTrace();
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(outputWAV));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            System.out.println("Playing song...");
            Thread.sleep(30000); // Wait for 30 seconds (just testing)
        } catch (Exception e) {
            System.out.println("Error playing song: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SongPlayer_Agg player = new SongPlayer_Agg();
        String query = "Skrillex"; // Replace with your search query

        // Search for a song
        JSONObject results = player.searchArtist(query);
        System.out.println(results);
        if (results != null) {
            JSONArray tracks = results.getJSONArray("data");
            if (tracks.length() > 0) {
                JSONObject firstTrack = tracks.getJSONObject(0); // Take the first result
                System.out.println("Playing: " + firstTrack.getString("title") + " by " +
                        firstTrack.getJSONObject("user").getString("name"));

                // Play the song
                player.playSong(firstTrack.getString("id"),firstTrack.getString("orig_filename"));
            } else {
                System.out.println("No results found.");
            }
        }
    }
}