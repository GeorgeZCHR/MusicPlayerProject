/*import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.awt.Desktop;

public class SearchArtistBio extends JFrame {

    public SearchArtistBio() {
        // Set up the frame
        setTitle("Artist Bio Search");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create and add the search button
        JButton searchBioButton = new JButton("Search Bio of Artist");
        add(searchBioButton, BorderLayout.CENTER);

        // Add action listener to the button
        searchBioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchArtistBio();
            }
        });

        // Set frame visibility
        setVisible(true);
    }

    public void searchArtistBio() {
        // Prompt the user to input an artist name
        String artistName = JOptionPane.showInputDialog(this, "Enter the artist's name:", "Artist Bio Search", JOptionPane.QUESTION_MESSAGE);

        if (artistName != null && !artistName.trim().isEmpty()) {
            // Construct the Wikipedia search URL
            String wikipediaSearchURL = "https://en.wikipedia.org/wiki/" + artistName.replace(" ", "_");
            openURLInBrowser(wikipediaSearchURL, "Searching bio for \"" + artistName + "\" on Wikipedia...");
        } else {
            JOptionPane.showMessageDialog(this, "Please enter an artist's name.", "No Artist Entered", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Method to open a URL in the default browser
    public void openURLInBrowser(String url, String message) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(url));
                JOptionPane.showMessageDialog(this, message, "Opening Browser", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException | URISyntaxException e) {
                JOptionPane.showMessageDialog(this, "Error opening browser: " + e.getMessage(), "Browser Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Desktop is not supported on this system.", "Unsupported Operation", JOptionPane.ERROR_MESSAGE);
        }
    }


}*/


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.awt.Desktop;

public class SearchArtistBio extends JFrame {

    private JTextArea bioTextArea; // To display the first paragraph of the bio

    public SearchArtistBio() {
        // Set up the frame
        setTitle("Artist Bio Search");
        setSize(500, 300);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create and add the search button
        JButton searchBioButton = new JButton("Search Bio of Artist");
        add(searchBioButton, BorderLayout.NORTH);

        // Text area to display the artist's bio
        bioTextArea = new JTextArea();
        bioTextArea.setWrapStyleWord(true);
        bioTextArea.setLineWrap(true);
        bioTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(bioTextArea);
        add(scrollPane, BorderLayout.CENTER);

        // Button to open the full Wikipedia page
        JButton viewMoreButton = new JButton("View More Bio");
        viewMoreButton.setEnabled(false); // Initially disabled
        add(viewMoreButton, BorderLayout.SOUTH);

        // Add action listeners
        searchBioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchArtistBio(viewMoreButton);
            }
        });

        viewMoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openWikiPage();
            }
        });

        // Set frame visibility
        setVisible(true);
    }

    private void searchArtistBio(JButton viewMoreButton) {
        // Prompt the user to input an artist name
        String artistName = JOptionPane.showInputDialog(this, "Enter the artist's name:", "Artist Bio Search", JOptionPane.QUESTION_MESSAGE);

        if (artistName != null && !artistName.trim().isEmpty()) {
            // Construct the Wikipedia API URL
            String wikipediaApiUrl = "https://en.wikipedia.org/api/rest_v1/page/summary/" + artistName.replace(" ", "_");

            try {
                // Fetch and display the first paragraph of the bio
                String bio = fetchFirstParagraph(wikipediaApiUrl);
                bioTextArea.setText(bio);

                // Enable the "View More Bio" button and store the Wikipedia page URL
                viewMoreButton.setEnabled(true);
                viewMoreButton.putClientProperty("wikiURL", "https://en.wikipedia.org/wiki/" + artistName.replace(" ", "_"));

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error fetching bio: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter an artist's name.", "No Artist Entered", JOptionPane.WARNING_MESSAGE);
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

    private void openWikiPage() {
        String wikiURL = (String) ((JButton) getContentPane().getComponent(2)).getClientProperty("wikiURL");
        if (wikiURL != null && Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(wikiURL));
            } catch (IOException | URISyntaxException e) {
                JOptionPane.showMessageDialog(this, "Error opening browser: " + e.getMessage(), "Browser Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Desktop is not supported on this system.", "Unsupported Operation", JOptionPane.ERROR_MESSAGE);
        }
    }


}


