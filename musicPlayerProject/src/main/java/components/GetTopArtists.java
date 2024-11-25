package components;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.google.gson.Gson;
import org.json.JSONObject;

public class GetTopArtists {

    private static final String API_KEY = "eac20d41d85963c4201d881aff79d53d"; // Replace with your API key
    private static final String BASE_URL = "http://ws.audioscrobbler.com/2.0/";

    public JSONObject getTopArtists() {
        String urlString = BASE_URL + "?method=chart.gettopartists&api_key=" + API_KEY + "&format=json";
        StringBuilder response = new StringBuilder();
        Gson gson = new Gson(); // Create a Gson instance

        try {
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
            }

            connection.disconnect();
            return new JSONObject(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // If there's an error
    }
}