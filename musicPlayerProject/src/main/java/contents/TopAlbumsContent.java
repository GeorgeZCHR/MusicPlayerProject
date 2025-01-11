package contents;
import components.GetTopAlbums;
import containers.Album;
import containers.ImageHolder;
import general.Util;
import gui.CustomTextField;
import gui.RoundButton;
import gui.WarningFrame;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TopAlbumsContent extends JPanel implements Content {

    private List<Album> albumsList = new ArrayList<>();
    private int topAlbumsNum = 0;
    public JLabel topAlbumsImage, topAlbumsNumLabel;
    public JLabel topAlbumsName, topAlbumsPlayCount;
    public JLabel topAlbumsArtistName, topAlbumsURL, topAlbumsArtistURL;
    public RoundButton topAlbumsNext, topAlbumsBack;
    public JLabel topAlbumsNameLabel, topAlbumsPlayCountLabel;
    public JLabel topAlbumsArtistNameLabel, topAlbumsURLLabel;
    public JLabel topAlbumsArtistURLLabel;
    public CustomTextField content;
    public RoundButton search;
    public BufferedImage image;
    public ImageIcon icon;

    public TopAlbumsContent(LayoutManager layout) {
        super(layout);
    }

    @Override
    public void init() {
        content = new CustomTextField(Util.orange_color,20,20);
        content.setBounds((int) (0.02 * getWidth()),
                (int) (0.01 * getHeight()), 400,(int) (0.08 * getHeight()));
        content.setFont(Util.myFont);

        search = new RoundButton("Search Artist",Util.orange_color,20,20);
        search.setBounds((int) (0.02 * getWidth()) + 420,(int) (0.01 * getHeight()),
                200,(int) (0.08 * getHeight()));
        search.setFont(Util.myFont);
        search.addActionListener(e -> {
            if (content.getText().isEmpty()) {
                WarningFrame wf = new WarningFrame("Empty Search",
                        "You have to write some Artist!");
            } else {
                if (!albumsList.isEmpty()) {
                    albumsList.clear();
                }
                fillAlbumList(content.getText());
                topAlbumsNum = 0;
                setAlbum();

                topAlbumsImage.setVisible(true);
                topAlbumsNumLabel.setVisible(true);
                topAlbumsNameLabel.setVisible(true);
                topAlbumsName.setVisible(true);
                topAlbumsPlayCountLabel.setVisible(true);
                topAlbumsPlayCount.setVisible(true);
                topAlbumsArtistNameLabel.setVisible(true);
                topAlbumsArtistName.setVisible(true);
                topAlbumsURLLabel.setVisible(true);
                topAlbumsURL.setVisible(true);
                topAlbumsArtistURLLabel.setVisible(true);
                topAlbumsArtistURL.setVisible(true);
                topAlbumsNext.setVisible(true);
                topAlbumsBack.setVisible(true);
            }
        });

        // για αρχικοποίηση
        String artist = "Skrillex";
        fillAlbumList(artist);

        //---Top Album Image---
        URL url;
        try {
            url = new URL(albumsList.get(topAlbumsNum).getImageHolder().getExtraLargeImage());
            image = ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        icon = new ImageIcon(image);
        topAlbumsImage = new JLabel(icon);
        // Optional: Center align the image in the JLabel
        topAlbumsImage.setHorizontalAlignment(JLabel.CENTER);
        topAlbumsImage.setVerticalAlignment(JLabel.CENTER);
        topAlbumsImage.setBounds((int) (0.02 * getWidth()),
                (int) (0.12 * getHeight()), icon.getIconWidth(),
                icon.getIconHeight());


        //---Top Album Number---
        topAlbumsNumLabel = new JLabel(String.valueOf(topAlbumsNum + 1));
        topAlbumsNumLabel.setFont(Util.headerFont);
        topAlbumsNumLabel.setForeground(Util.blue_dark_color);
        topAlbumsNumLabel.setBounds((int) (0.5 * getWidth()) - 25,
                (int) (0.9 * getHeight()), 50, 50);


        //---Next Button---
        topAlbumsNext = new RoundButton(
                "Next", Util.orange_color, 20, 20);
        topAlbumsNext.setFocusable(false);
        topAlbumsNext.setFont(Util.myFont);
        topAlbumsNext.setBounds(
                (int) (0.98 * getWidth()) - 100,
                (int) (0.9 * getHeight()), 100, 50);
        //Info code
        topAlbumsNext.addActionListener(e -> {
            if (!(topAlbumsNum + 1 < albumsList.size())) {
                topAlbumsNum = 0;
            } else {
                topAlbumsNum++;
            }
            setAlbum();
        });

        //---Back Button---
        topAlbumsBack = new RoundButton(
                "Back", Util.orange_color, 20, 20);
        topAlbumsBack.setFocusable(false);
        topAlbumsBack.setFont(Util.myFont);
        topAlbumsBack.setBounds((int) (0.02 * getWidth()),
                (int) (0.9 * getHeight()), 100, 50);
        //Info code
        topAlbumsBack.addActionListener(e -> {
            if (!(topAlbumsNum - 1 > -1)) {
                topAlbumsNum = albumsList.size() - 1;
            } else {
                topAlbumsNum--;
            }
            setAlbum();
        });

        //---Top Album Name---
        //UI code
        topAlbumsNameLabel = new JLabel("Album Name    :");
        topAlbumsNameLabel.setBounds(icon.getIconWidth() + 40,
                (int) (0.12 * getHeight()), 150, 50);
        topAlbumsNameLabel.setFont(Util.myFont);
        topAlbumsNameLabel.setForeground(Util.blue_dark_color);
        //Info code
        topAlbumsName = new JLabel(albumsList.get(topAlbumsNum).getName());
        topAlbumsName.setBounds(icon.getIconWidth() + 190,
                (int) (0.12 * getHeight()), 400, 50);
        topAlbumsName.setFont(Util.myFont);
        topAlbumsName.setForeground(Util.blue_dark_color);

        //---Top Albums Play Count---
        topAlbumsPlayCountLabel = new JLabel("Play Count     :");
        topAlbumsPlayCountLabel.setBounds(icon.getIconWidth() + 40,
                (int) (0.12 * getHeight()) + 50, 150, 50);
        topAlbumsPlayCountLabel.setFont(Util.myFont);
        topAlbumsPlayCountLabel.setForeground(Util.blue_dark_color);

        //Info code
        topAlbumsPlayCount = new JLabel(String.valueOf(albumsList.get(topAlbumsNum).getPlayCount()));
        topAlbumsPlayCount.setBounds(icon.getIconWidth() + 190,
                (int) (0.12 * getHeight()) + 50, 400, 50);
        topAlbumsPlayCount.setFont(Util.myFont);
        topAlbumsPlayCount.setForeground(Util.blue_dark_color);

        //---Top Album Artis Name---
        topAlbumsArtistNameLabel = new JLabel("Artist Name    :");
        topAlbumsArtistNameLabel.setBounds(icon.getIconWidth() + 40,
                (int) (0.12 * getHeight()) + 100, 150, 50);
        topAlbumsArtistNameLabel.setFont(Util.myFont);
        topAlbumsArtistNameLabel.setForeground(Util.blue_dark_color);

        //Info code
        topAlbumsArtistName = new JLabel(albumsList.get(topAlbumsNum).getArtistName());
        topAlbumsArtistName.setBounds(icon.getIconWidth() + 190,
                (int) (0.12 * getHeight()) + 100, 400, 50);
        topAlbumsArtistName.setFont(Util.myFont);
        topAlbumsArtistName.setForeground(Util.blue_dark_color);

        //---Top Albums URL---
        topAlbumsURLLabel = new JLabel("Album URL :");
        topAlbumsURLLabel.setBounds((int) (0.02 * getWidth()),
                icon.getIconHeight() + 100, 150, 50);
        topAlbumsURLLabel.setFont(Util.myFont);
        topAlbumsURLLabel.setForeground(Util.blue_dark_color);

        //Info code
        topAlbumsURL = new JLabel(albumsList.get(topAlbumsNum).getURL());
        topAlbumsURL.setBounds((int) (0.02 * getWidth()) + 150,
                icon.getIconHeight() + 100, 1200, 50);
        topAlbumsURL.setFont(Util.myFont);
        topAlbumsURL.setForeground(Util.blue_dark_color);

        //---Top Albums Artist URL---
        topAlbumsArtistURLLabel = new JLabel("Artist URL  :");
        topAlbumsArtistURLLabel.setBounds((int) (0.02 * getWidth()),
                icon.getIconHeight() + 150, 150, 50);
        topAlbumsArtistURLLabel.setFont(Util.myFont);
        topAlbumsArtistURLLabel.setForeground(Util.blue_dark_color);

        //Info code
        topAlbumsArtistURL = new JLabel(albumsList.get(topAlbumsNum).getArtistURL());
        topAlbumsArtistURL.setBounds((int) (0.02 * getWidth()) + 150,
                icon.getIconHeight() + 150, 1200, 50);
        topAlbumsArtistURL.setFont(Util.myFont);
        topAlbumsArtistURL.setForeground(Util.blue_dark_color);

        topAlbumsImage.setVisible(false);
        topAlbumsNumLabel.setVisible(false);
        topAlbumsNameLabel.setVisible(false);
        topAlbumsName.setVisible(false);
        topAlbumsPlayCountLabel.setVisible(false);
        topAlbumsPlayCount.setVisible(false);
        topAlbumsArtistNameLabel.setVisible(false);
        topAlbumsArtistName.setVisible(false);
        topAlbumsURLLabel.setVisible(false);
        topAlbumsURL.setVisible(false);
        topAlbumsArtistURLLabel.setVisible(false);
        topAlbumsArtistURL.setVisible(false);
        topAlbumsNext.setVisible(false);
        topAlbumsBack.setVisible(false);

        add(content);
        add(search);
        add(topAlbumsImage);
        add(topAlbumsNumLabel);
        add(topAlbumsNameLabel);
        add(topAlbumsName);
        add(topAlbumsPlayCountLabel);
        add(topAlbumsPlayCount);
        add(topAlbumsArtistNameLabel);
        add(topAlbumsArtistName);
        add(topAlbumsURLLabel);
        add(topAlbumsURL);
        add(topAlbumsArtistURLLabel);
        add(topAlbumsArtistURL);
        add(topAlbumsNext);
        add(topAlbumsBack);
    }

    private void setAlbum() {
        //---Top Track Image---
        URL url;
        BufferedImage image;
        ImageIcon icon;
        try {
            url = new URL(albumsList.get(topAlbumsNum).getImageHolder().getExtraLargeImage());
            image = ImageIO.read(url);
            icon = new ImageIcon(image);
        } catch (IOException e) {
            try {
                image = ImageIO.read(new File("img/error_warning.png"));
                icon = new ImageIcon(image.getScaledInstance(300,300, Image.SCALE_SMOOTH));
            } catch (Exception ex) {
                icon = new ImageIcon("img/error_warning.png");
            }
        }
        topAlbumsImage.setIcon(icon);
        topAlbumsImage.setHorizontalAlignment(JLabel.CENTER);
        topAlbumsImage.setVerticalAlignment(JLabel.CENTER);
        topAlbumsImage.setBounds((int) (0.02 * getWidth()),
                (int) (0.12 * getHeight()), icon.getIconWidth(),
                icon.getIconHeight());

        //---Top Track Number---
        topAlbumsNumLabel.setText(String.valueOf(topAlbumsNum + 1));

        //---Top Track Name---
        topAlbumsName.setText(albumsList.get(topAlbumsNum).getName());

        //---Top Track Play Count---
        topAlbumsPlayCount.setText(String.valueOf(albumsList.get(topAlbumsNum).getPlayCount()));

        //---Top Albums Artists---
        topAlbumsArtistName.setText(String.valueOf(albumsList.get(topAlbumsNum).getArtistName()));

        //---Top Album URL---
        topAlbumsURL.setText(albumsList.get(topAlbumsNum).getURL());

        //---Top Album Artist URL---
        topAlbumsArtistURL.setText(albumsList.get(topAlbumsNum).getArtistURL());
    }

    public void fillAlbumList(String artist) {
        GetTopAlbums topAlbums = new GetTopAlbums();
        JSONObject jsonObject = topAlbums.getTopAlbumsByArtist(artist);
        System.out.println(jsonObject);

        try {
            JSONObject topAlbumsObj = jsonObject.getJSONObject("topalbums");

            //JSONObject attr = artists.getJSONObject("@attr");

            JSONArray albumsArray = topAlbumsObj.getJSONArray("album");
            for (int i = 0; i < albumsArray.length(); i++) {
                JSONObject album = albumsArray.getJSONObject(i);

                JSONArray images = album.getJSONArray("image");
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
                    }/* else if (images.getJSONObject(j).getString("size").equals("mega")) {
                    imageHolder.setMegaImage(images.getJSONObject(j).getString("#text"));
                }*/
                }

                albumsList.add(new Album(
                        album.getString("name"),
                        album.getLong("playcount"),
                        album.getString("url"),
                        album.getJSONObject("artist").getString("mbid"),
                        album.getJSONObject("artist").getString("name"),
                        album.getJSONObject("artist").getString("url"),
                        imageHolder
                ));
                //albumsList.get(i).print();
            }
        } catch (Exception e) {
            WarningFrame wf = new WarningFrame("Artist not found",
                    "The artist you supplied could not be found");
        }
    }
}