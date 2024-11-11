import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ArtistBioSearcher {
    private String artistName;
    private String wikiURL;
    ArtistBioSearcher(String artistName) {
        this.artistName = artistName;
        wikiURL = "https://en.wikipedia.org/wiki/" + artistName.replace(" ", "_");
    }

    public String produceBio() {
        // Construct the Wikipedia API URL
        String wikipediaApiUrl = "https://en.wikipedia.org/api/rest_v1/page/summary/" + artistName.replace(" ", "_");
        try {
            return fetchFirstParagraph(wikipediaApiUrl);

        } catch (IOException ex) {
            System.err.println("Error fetching bio: ");
            return null;
        }
    }

    private String fetchFirstParagraph(String apiUrl) throws IOException {
        // Connect to Wikipedia API and fetch the summary
        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // Parse the JSON response to get the first paragraph
            String json = response.toString();
            int startIdx = json.indexOf("\"extract\":\"") + 11;
            int endIdx = json.indexOf("\",\"extract_html\"");
            if (startIdx > 0 && endIdx > startIdx) {
                return json.substring(startIdx, endIdx).replace("\\n", "\n");
            }
            return "No bio available for this artist.";
        }
    }
}