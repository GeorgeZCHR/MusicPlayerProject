import components.GetTopArtists;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.net.HttpURLConnection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TopArtistsAPITest {

    private GetTopArtists getTopArtists;
    private HttpURLConnection mockConnection;

    @BeforeEach
    public void setUp() {
        // Create an instance of the class to be tested
        getTopArtists = new GetTopArtists();

        // Mock the HttpURLConnection
        mockConnection = mock(HttpURLConnection.class);
    }

    @Test
    public void testGetTopArtists() throws Exception {
        // Simulate a JSON response from the API
        String mockApiResponse = """
                {
                    "artists": {
                        "artist": [
                            {"name": "Artist 1", "listeners": "12345"},
                            {"name": "Artist 2", "listeners": "67890"}
                        ]
                    }
                }
                """;

        // Mock the input stream to return the simulated response
        ByteArrayInputStream mockInputStream = new ByteArrayInputStream(mockApiResponse.getBytes());
        when(mockConnection.getInputStream()).thenReturn(mockInputStream);

        // Mock the response code
        when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

        // Use reflection or modify the `GetTopArtists` class to inject the mock connection
        JSONObject result = getTopArtists.getTopArtists();

        // Assertions
        assertNotNull(result);
        assertTrue(result.has("artists"));
        assertEquals("Pantazis", result.getJSONObject("artists")
                .getJSONArray("artist")
                .getJSONObject(0)
                .getString("name"));
    }
}
