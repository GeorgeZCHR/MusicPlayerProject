package contents;
import components.GetTopArtists;
import containers.Artist;
import containers.ImageHolder;
import general.Util;
import gui.RoundButton;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TopArtistsContent extends JPanel implements Content {
    private List<Artist> artistList;
    private int topArtistNum = 0;
    public JLabel topArtistImage, topArtistNumLabel, topArtistNameLabel;
    public JLabel topArtistName, topArtistPlayCount, topArtistPlayCountLabel;
    public JLabel topArtistListeners, topArtistURL, topArtistListenersLabel;
    public JLabel topArtistURLLabel;
    public RoundButton topArtistsNext, topArtistsBack;
    public ImageIcon icon;
    public BufferedImage image;

    public TopArtistsContent(LayoutManager layout) { super(layout); }
    @Override
    public void init() {
        //---Show JSon Object to console with the button getTopArtists---
        artistList = new ArrayList<>();
        GetTopArtists topArtists = new GetTopArtists();
        JSONObject jsonObject = topArtists.getTopArtists();

        JSONObject artists = jsonObject.getJSONObject("artists");

        //JSONObject attr = artists.getJSONObject("@attr");

        JSONArray artistArray = artists.getJSONArray("artist");
        for (int i = 0; i < artistArray.length(); i++) {
            JSONObject artist = artistArray.getJSONObject(i);

            JSONArray images = artist.getJSONArray("image");
            ImageHolder imageHolder = new ImageHolder();
            for (int j = 0; j < images.length(); j++) {
                if (images.getJSONObject(j).getString("size").equals("small")) {
                    imageHolder.setSmallImage(images.getJSONObject(j).getString("#text"));
                } else if (images.getJSONObject(j).getString("size").equals("medium")) {
                    imageHolder.setMediumImage(images.getJSONObject(j).getString("#text"));
                } else if (images.getJSONObject(j).getString("size").equals("large")) {
                    imageHolder.setLargeImage(images.getJSONObject(j).getString("#text"));
                } else if (images.getJSONObject(j).getString("size").equals("extralarge")) {
                    imageHolder.setExtraLargeImage(images.getJSONObject(j).getString("#text"));
                } else if (images.getJSONObject(j).getString("size").equals("mega")) {
                    imageHolder.setMegaImage(images.getJSONObject(j).getString("#text"));
                }
            }

            artistList.add(new Artist(
                    artist.getString("name"),
                    Long.parseLong(artist.getString("playcount")),
                    Long.parseLong(artist.getString("listeners")),
                    artist.getString("mbid"),
                    artist.getString("url"),
                    Long.parseLong(artist.getString("streamable")),
                    imageHolder
            ));
            //artistList.get(i).print();
        }

        //---Top Artist Image---
        URL url;
        try {
            url = new URL(artistList.get(topArtistNum).getImageHolder().getMegaImage());
            image = ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        icon = new ImageIcon(image);
        topArtistImage = new JLabel(icon);
        // Optional: Center align the image in the JLabel
        topArtistImage.setHorizontalAlignment(JLabel.CENTER);
        topArtistImage.setVerticalAlignment(JLabel.CENTER);
        topArtistImage.setBounds((int)(0.02 * getWidth()),
                (int)(0.05 * getHeight()),icon.getIconWidth(),
                icon.getIconHeight());

        //---Top Artist Number---
        topArtistNumLabel = new JLabel(String.valueOf(topArtistNum + 1));
        topArtistNumLabel.setFont(Util.headerFont);
        topArtistNumLabel.setForeground(Util.blue_dark_color);
        topArtistNumLabel.setBounds((int)(0.5 * getWidth()) - 25,
                (int)(0.9 * getHeight()),50,50);

        //---Next Button---
        topArtistsNext = new RoundButton(
                "Next", Util.orange_color, 20,20);
        topArtistsNext.setFocusable(false);
        topArtistsNext.setFont(Util.myFont);
        topArtistsNext.setBounds(
                (int)(0.98 * getWidth()) - 100,
                (int)(0.9 * getHeight()),100,50);
        topArtistsNext.addActionListener(e -> {
            if (!(topArtistNum + 1 < artistList.size())) {
                topArtistNum = 0;
            } else {
                topArtistNum++;
            }
            setArtist();
        });

        //---Back Button---
        topArtistsBack = new RoundButton(
                "Back",Util.orange_color, 20,20);
        topArtistsBack.setFocusable(false);
        topArtistsBack.setFont(Util.myFont);
        topArtistsBack.setBounds((int)(0.02 * getWidth()),
                (int)(0.9 * getHeight()),100,50);
        topArtistsBack.addActionListener(e -> {
            if (!(topArtistNum - 1 > -1)) {
                topArtistNum = artistList.size() - 1;
            } else {
                topArtistNum--;
            }
            setArtist();
        });

        //---Top Artist Name---
        topArtistNameLabel = new JLabel("Name          :");
        topArtistNameLabel.setBounds(icon.getIconWidth() + 40,
                (int)(0.05 * getHeight()),150,50);
        topArtistNameLabel.setFont(Util.myFont);
        topArtistNameLabel.setForeground(Util.blue_dark_color);
        topArtistName = new JLabel(artistList.get(topArtistNum).getName());
        topArtistName.setBounds(icon.getIconWidth() + 190,
                (int)(0.05 * getHeight()),400,50);
        topArtistName.setFont(Util.myFont);
        topArtistName.setForeground(Util.blue_dark_color);

        //---Top Artist Play Count---
        topArtistPlayCountLabel = new JLabel("Play Count :");
        topArtistPlayCountLabel.setBounds(icon.getIconWidth() + 40,
                (int)(0.05 * getHeight())+50,150,50);
        topArtistPlayCountLabel.setFont(Util.myFont);
        topArtistPlayCountLabel.setForeground(Util.blue_dark_color);
        topArtistPlayCount = new JLabel(String.valueOf(artistList.get(topArtistNum).getPlayCount()));
        topArtistPlayCount.setBounds(icon.getIconWidth() + 190,
                (int)(0.05 * getHeight())+50,400,50);
        topArtistPlayCount.setFont(Util.myFont);
        topArtistPlayCount.setForeground(Util.blue_dark_color);

        //---Top Artist Listeners---
        topArtistListenersLabel = new JLabel("Listeners   :");
        topArtistListenersLabel.setBounds(icon.getIconWidth() + 40,
                (int)(0.05 * getHeight())+100,150,50);
        topArtistListenersLabel.setFont(Util.myFont);
        topArtistListenersLabel.setForeground(Util.blue_dark_color);
        topArtistListeners = new JLabel(String.valueOf(artistList.get(topArtistNum).getListeners()));
        topArtistListeners.setBounds(icon.getIconWidth() + 190,
                (int)(0.05 * getHeight())+100,400,50);
        topArtistListeners.setFont(Util.myFont);
        topArtistListeners.setForeground(Util.blue_dark_color);

        //---Top Artist URL---
        topArtistURLLabel = new JLabel("URL :");
        topArtistURLLabel.setBounds((int)(0.02 * getWidth()),
                icon.getIconHeight()+50,100,50);
        topArtistURLLabel.setFont(Util.myFont);
        topArtistURLLabel.setForeground(Util.blue_dark_color);
        topArtistURL = new JLabel(artistList.get(topArtistNum).getUrl());
        topArtistURL.setBounds((int)(0.02 * getWidth()) + 100,
                icon.getIconHeight()+50,1200,50);
        topArtistURL.setFont(Util.myFont);
        topArtistURL.setForeground(Util.blue_dark_color);

        add(topArtistImage);
        add(topArtistNumLabel);
        add(topArtistNameLabel);
        add(topArtistName);
        add(topArtistPlayCountLabel);
        add(topArtistPlayCount);
        add(topArtistListenersLabel);
        add(topArtistListeners);
        add(topArtistURLLabel);
        add(topArtistURL);
        add(topArtistsNext);
        add(topArtistsBack);
    }

    private void setArtist() {
        //---Top Artist Image---
        URL url;
        BufferedImage image;
        try {
            url = new URL(artistList.get(topArtistNum).getImageHolder().getMegaImage());
            image = ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ImageIcon icon = new ImageIcon(image);
        topArtistImage = new JLabel(icon);
        topArtistImage.setHorizontalAlignment(JLabel.CENTER);
        topArtistImage.setVerticalAlignment(JLabel.CENTER);
        topArtistImage.setBounds((int)(0.02 * getWidth()),
                (int)(0.05 * getHeight()),icon.getIconWidth(),
                icon.getIconHeight());

        //---Top Artist Number---
        topArtistNumLabel.setText(String.valueOf(topArtistNum + 1));

        //---Top Artist Name---
        topArtistName.setText(artistList.get(topArtistNum).getName());

        //---Top Artist Play Count---
        topArtistPlayCount.setText(String.valueOf(artistList.get(topArtistNum).getPlayCount()));

        //---Top Artist Listeners---
        topArtistListeners.setText(String.valueOf(artistList.get(topArtistNum).getListeners()));

        //---Top Artist URL---
        topArtistURL.setText(artistList.get(topArtistNum).getUrl());
    }
}