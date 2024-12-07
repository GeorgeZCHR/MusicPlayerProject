package contents;
import components.ArtistBioSearcher;
import general.Util;
import gui.CustomTextField;
import gui.RoundButton;
import gui.WarningFrame;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class BioContent extends JPanel implements Content {
    CustomTextField artistInput = new CustomTextField(Util.orange_color,20,20);
    private RoundButton viewMoreButton = new RoundButton(
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
        RoundButton searchBio = new RoundButton(
                "Search",Util.orange_color,20,20);
        searchBio.setFocusable(false);
        searchBio.setFont(Util.myFont);
        searchBio.setBounds((int)(0.02 * getWidth()),
                (int)(0.11 * getHeight()), 200, 50);
        searchBio.addActionListener(e -> searchArtistBio());

        //---Bio Text Area---
        bioTextArea.setEnabled(false);
        bioTextArea.setOpaque(false);
        bioTextArea.setFont(Util.myFont);
        bioTextAreaSP = Util.createScrollPane(bioTextArea,
                new Rectangle((int)(0.02 * getWidth()),(int)(0.2 * getHeight()),
                        (int)(0.96 * getWidth()),(int)(0.8 * getHeight())),
                Util.blue_color,Util.blue_color.brighter());

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
                WarningFrame wf = new WarningFrame("Not Valid Artist Entered",
                        "Please enter a valid artist's name.");
            }
        } else {
            isArtistBioEmpty = true;
            WarningFrame wf = new WarningFrame("No Artist Entered",
                    "Please enter an artist's name.");
        }
    }

    public void openWikiPage() {
        searchArtistBio();
        //String artist = artistInput.getText();
        /*wikiURL = "https://en.wikipedia.org/wiki/" + artist.replace(" ", "_");*/
        if (isArtistBioEmpty) {
            return;
        }
        if (Desktop.isDesktopSupported()) {
            try {
                if (isArtistBioValid) {
                    Desktop.getDesktop().browse(new URI(wikiURL));
                }
            } catch (IOException | URISyntaxException e) {
                WarningFrame wf = new WarningFrame("Not Valid containers.Artist Entered",
                        "Please enter a valid artist's name.");
            }
        } else {
            WarningFrame wf = new WarningFrame("Unsupported Operation",
                    "Desktop is not supported on this system.");
        }
    }
}