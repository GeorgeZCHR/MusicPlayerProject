package components;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONObject;

public class GetTopAlbums {

    private static final String API_KEY = "eac20d41d85963c4201d881aff79d53d"; // Replace with your API key
    private static final String BASE_URL = "http://ws.audioscrobbler.com/2.0/";

    /**
     * Fetch the top albums for a specific artist.
     * @param artistName The name of the artist.
     * @return A JSONObject containing the top albums data.
     */
    public JSONObject getTopAlbumsByArtist(String artistName) {
        // Build the API URL with the artist name
        String urlString = BASE_URL + "?method=artist.gettopalbums&artist="
                + artistName.replace(" ", "%20") // Encode spaces
                + "&api_key=" + API_KEY
                + "&format=json";
        StringBuilder response = new StringBuilder();

        try {
            System.out.println("Request URL: " + urlString); // Debugging: Print the URL
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Check for a successful response
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();
            } else {
                System.out.println("Failed. Server responded with: " + connection.getResponseCode());
                return null;
            }

            connection.disconnect();
            return new JSONObject(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // If there's an error
    }
}