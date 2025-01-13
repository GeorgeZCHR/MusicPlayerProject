import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.net.HttpURLConnection;

import components.GetTopTracks;
import org.json.JSONObject;

public class GetTopTracksTest {

    private HttpURLConnection mockConnection;
    private GetTopTracks getTopTracks;

    @BeforeEach
    public void setUp() {
        // Create a mock HttpURLConnection
        mockConnection = Mockito.mock(HttpURLConnection.class);
        getTopTracks = new GetTopTracks(); // Instantiate the class to be tested
    }

    @Test
    public void testGetTopTracks() throws Exception {
        // Mock the API response
        String mockApiResponse = "{ \"tracks\": { \"track\": [ { \"name\": \"Track1\", \"artist\": { \"name\": \"Artist1\" } } ] } }";
        ByteArrayInputStream mockInputStream = new ByteArrayInputStream(mockApiResponse.getBytes());

        // Mock the HttpURLConnection behavior
        when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(mockConnection.getInputStream()).thenReturn(mockInputStream);

        // Act: Call the method being tested
        JSONObject result = getTopTracks.getTopTracks();

        // Assert: Verify the result
        assertNotNull(result, "Result should not be null");
        assertTrue(result.has("tracks"), "Result should have a 'tracks' key");

        JSONObject tracks = result.getJSONObject("tracks");
        assertTrue(tracks.has("track"), "Tracks object should have a 'track' array");

        // Verify the data structure of the mock response
        JSONObject track = tracks.getJSONArray("track").getJSONObject(0);
        assertEquals("Good Luck, Babe!", track.getString("name"));
        assertEquals("Sakis Rouvas", track.getJSONObject("artist").getString("name"));
    }
}
