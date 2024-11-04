import javax.swing.*;
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


}

