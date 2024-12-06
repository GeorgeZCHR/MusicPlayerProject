package contents;
import components.ArtistBioSearcher;
import general.Util;
import gui.CustomButton;
import gui.CustomTextField;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class BioContent extends JPanel implements Content {
    CustomTextField artistInput = new CustomTextField(Util.orange_color,20,20);
    private CustomButton viewMoreButton = new CustomButton(
            "View More Bio", Util.orange_color,20,20);
    private JScrollPane bioTextAreaSP;
    private JTextArea bioTextArea = new JTextArea();
    private boolean isArtistBioEmpty = true;
    private boolean isArtistBioValid = false;
    private String wikiURL;

    public BioContent(LayoutManager layout) { super(layout); }

    @Override
    public void init() {
        //---Artist Label---
        JLabel artistLabel = new JLabel("Enter the artist's name:");
        artistLabel.setFont(Util.myFont);
        artistLabel.setForeground(Util.blue_dark_color);
        artistLabel.setBounds((int)(0.02 * getWidth()),
                (int)(0.02 * getHeight()), 250, 50);

        //---Artist Input Text Field---
        artistInput.setBounds((int)(0.5 * getWidth()) - 150,
                (int)(0.02 * getHeight()), 400, 50);
        artistInput.setFont(Util.myFont);

        //---Search Bio Button---
        CustomButton searchBio = new CustomButton(
                "Search",Util.orange_color,20,20);
        searchBio.setFocusable(false);
        searchBio.setFont(Util.myFont);
        searchBio.setBounds((int)(0.02 * getWidth()),
                (int)(0.11 * getHeight()), 200, 50);
        searchBio.addActionListener(e -> searchArtistBio());

        //---Bio Text Area---
        bioTextArea.setEnabled(false);
        bioTextArea.setDisabledTextColor(Util.DEFAULT_TEXT_COLOR);
        // Ενεργοποίηση αναδίπλωσης γραμμής
        bioTextArea.setLineWrap(true);
        // Αναδίπλωση ανά λέξη για καλύτερη εμφάνιση
        bioTextArea.setWrapStyleWord(true);
        // Ρυθμιση της γραμματοσειράς για να είναι αναγνώσιμη
        bioTextArea.setFont(Util.myFont);
        bioTextAreaSP = new JScrollPane(bioTextArea);
        bioTextAreaSP.setVisible(false);
        bioTextAreaSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        bioTextAreaSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        bioTextAreaSP.setBounds((int)(0.02 * getWidth()),
                (int)(0.2 * getHeight()), (int)(0.96 * getWidth()),
                (int)(0.8 * getHeight()));

        //---View More Button---
        viewMoreButton.setVisible(false);
        viewMoreButton.setFocusable(false);
        viewMoreButton.setFont(Util.myFont);
        viewMoreButton.setBounds((int)(0.98 * getWidth()) - 200,
                (int)(0.11 * getHeight()), 200, 50);
        viewMoreButton.addActionListener(e -> openWikiPage());

        add(artistLabel);
        add(artistInput);
        add(searchBio);
        add(bioTextAreaSP);
        add(viewMoreButton);
    }

    public void searchArtistBio() {
        ArtistBioSearcher abs;
        /*String artistName = JOptionPane.showInputDialog(this,
                "Enter the artist's name:",
                "containers.Artist Bio Search", JOptionPane.QUESTION_MESSAGE);*/
        String artistName = artistInput.getText();
        if (!artistName.trim().isEmpty()) {
            isArtistBioEmpty = false;
            wikiURL = "https://en.wikipedia.org/wiki/" + artistName.replace(" ", "_");
            abs = new ArtistBioSearcher(artistName);
            String bio = abs.produceBio();
            if (bio != null) {
                isArtistBioValid = true;
                bioTextArea.setText(bio);
                bioTextAreaSP.setVisible(true);
                viewMoreButton.setVisible(true);
            } else {
                isArtistBioValid = false;
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid artist's name.",
                        "Not Valid containers.Artist Entered", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            isArtistBioEmpty = true;
            JOptionPane.showMessageDialog(this,
                    "Please enter an artist's name.",
                    "No containers.Artist Entered", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void openWikiPage() {
        searchArtistBio();
        //String artist = artistInput.getText();
        /*wikiURL = "https://en.wikipedia.org/wiki/" + artist.replace(" ", "_");*/
        if (isArtistBioEmpty) {
            /*JOptionPane.showMessageDialog(this,
                    "Please enter an artist's name.",
                    "No containers.Artist Entered", JOptionPane.WARNING_MESSAGE);*/
            return;
        }
        if (Desktop.isDesktopSupported()) {
            try {
                if (isArtistBioValid) {
                    Desktop.getDesktop().browse(new URI(wikiURL));
                }
            } catch (IOException | URISyntaxException e) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid artist's name.",
                        "Not Valid containers.Artist Entered", JOptionPane.WARNING_MESSAGE);
                /*JOptionPane.showMessageDialog(this,
                        "Error opening browser: " + e.getMessage(),
                        "Browser Error", JOptionPane.ERROR_MESSAGE);*/
            }
        } else {
            JOptionPane.showMessageDialog(this, "Desktop is not supported on this system.", "Unsupported Operation", JOptionPane.ERROR_MESSAGE);
        }
    }
}