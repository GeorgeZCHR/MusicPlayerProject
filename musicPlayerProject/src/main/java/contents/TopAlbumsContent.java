package contents;
import components.GetTopAlbums;
import containers.Album;
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

public class TopAlbumsContent extends JPanel implements Content {

    private List<Album> albumsList;
    private int topAlbumsNum = 0;
    private JLabel topAlbumsImage, topAlbumsNumLabel;
    private JLabel topAlbumsName, topAlbumsPlayCount;
    private JLabel topAlbumsGenre, topAlbumsURL;

    public TopAlbumsContent(LayoutManager layout) { super(layout); }
    @Override
    public void init() {
        String artist="Skrillex";

        //---Show JSon Object to console with the button getTopArtists---
        albumsList = new ArrayList<>();
        GetTopAlbums topAlbums = new GetTopAlbums();
        JSONObject jsonObject = topAlbums.getTopAlbumsByArtist(artist);//look into this

        JSONObject albums = jsonObject.getJSONObject("albums");

        //JSONObject attr = artists.getJSONObject("@attr");

        JSONArray albumsArray = albums.getJSONArray("albums");
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
                } else if (images.getJSONObject(j).getString("size").equals("mega")) {
                    imageHolder.setMegaImage(images.getJSONObject(j).getString("#text"));
                }
            }

            albumsList.add(new Album(
                    album.getString("title"),
                    Long.parseLong(album.getString("playcount")),
                    album.getString("artist name"),
                    album.getString("genre"),
                    album.getString("url"),
                    imageHolder,
                    Long.parseLong(album.getString("track count")),
                    album.getString("date")


            ));

            //albumsList.get(i).print();


        }

        //---Top Album Image---
        URL url;
        BufferedImage image;
        try {
            url = new URL(albumsList.get(topAlbumsNum).getImageHolder().getMegaImage());
            image = ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ImageIcon icon = new ImageIcon(image);
        topAlbumsImage = new JLabel(icon);
        // Optional: Center align the image in the JLabel
        topAlbumsImage.setHorizontalAlignment(JLabel.CENTER);
        topAlbumsImage.setVerticalAlignment(JLabel.CENTER);
        topAlbumsImage.setBounds((int)(0.02 * getWidth()),
                (int)(0.05 * getHeight()),icon.getIconWidth(),
                icon.getIconHeight());


        //---Top Album Number---
        //topAlbumsNumLabel = new JLabel(String.valueOf(topAlbumsNum + 1));
        topAlbumsNumLabel.setFont(Util.headerFont);
        topAlbumsNumLabel.setForeground(Util.blue_dark_color);
        topAlbumsNumLabel.setBounds((int)(0.5 * getWidth()) - 25,
                (int)(0.9 * getHeight()),50,50);



        //---Next Button---
        RoundButton topAlbumsNext = new RoundButton(
                "Next", Util.orange_color, 20,20);
        topAlbumsNext.setFocusable(false);
        topAlbumsNext.setFont(Util.myFont);
        topAlbumsNext.setBounds(
                (int)(0.98 * getWidth()) - 100,
                (int)(0.9 * getHeight()),100,50);
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
        RoundButton topAlbumsBack = new RoundButton(
                "Back",Util.orange_color, 20,20);
        topAlbumsBack.setFocusable(false);
        topAlbumsBack.setFont(Util.myFont);
        topAlbumsBack.setBounds((int)(0.02 * getWidth()),
                (int)(0.9 * getHeight()),100,50);
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
        JLabel topAlbumsNameLabel = new JLabel("Name          :");
        topAlbumsNameLabel.setBounds(icon.getIconWidth() + 40,
                (int)(0.05 * getHeight()),150,50);
        topAlbumsNameLabel.setFont(Util.myFont);
        topAlbumsNameLabel.setForeground(Util.blue_dark_color);
        //Info code
        topAlbumsName = new JLabel(albumsList.get(topAlbumsNum).getTitle());
        topAlbumsName.setBounds(icon.getIconWidth() + 190,
                (int)(0.05 * getHeight()),400,50);
        topAlbumsName.setFont(Util.myFont);
        topAlbumsName.setForeground(Util.blue_dark_color);

        //---Top Albums Play Count---
        JLabel topAlbumsPlayCountLabel = new JLabel("Play Count :");
        topAlbumsPlayCountLabel.setBounds(icon.getIconWidth() + 40,
                (int)(0.05 * getHeight())+50,150,50);
        topAlbumsPlayCountLabel.setFont(Util.myFont);
        topAlbumsPlayCountLabel.setForeground(Util.blue_dark_color);
        //Info code
        topAlbumsPlayCount = new JLabel(String.valueOf(albumsList.get(topAlbumsNum).getPlayCount()));
        topAlbumsPlayCount.setBounds(icon.getIconWidth() + 190,
                (int)(0.05 * getHeight())+50,400,50);
        topAlbumsPlayCount.setFont(Util.myFont);
        topAlbumsPlayCount.setForeground(Util.blue_dark_color);

        /*//---Top Album Listeners---
        JLabel topAlbumsListenersLabel = new JLabel("Listeners   :");
        topAlbumsListenersLabel.setBounds(icon.getIconWidth() + 40,
                (int)(0.05 * getHeight())+100,150,50);
        topAlbumsListenersLabel.setFont(Util.myFont);
        topAlbumsListenersLabel.setForeground(Util.blue_dark_color);
        topAlbumsListeners = new JLabel(String.valueOf(albumsList.get(topAlbumsNum).getListeners()));
        topAlbumsListeners.setBounds(icon.getIconWidth() + 190,
                (int)(0.05 * getHeight())+100,400,50);
        topAlbumsListeners.setFont(Util.myFont);
        topAlbumsListeners.setForeground(Util.blue_dark_color);
        */

        //Top Album Genre
        JLabel topAlbumsGenreLabel = new JLabel("Genre   :");
        topAlbumsGenreLabel.setBounds(icon.getIconWidth() + 40,
                (int)(0.05 * getHeight())+100,150,50);
        topAlbumsGenreLabel.setFont(Util.myFont);
        topAlbumsGenreLabel.setForeground(Util.blue_dark_color);
        topAlbumsGenre = new JLabel(String.valueOf(albumsList.get(topAlbumsNum).getGenre()));
        topAlbumsGenre.setBounds(icon.getIconWidth() + 190,
                (int)(0.05 * getHeight())+100,400,50);
        topAlbumsGenre.setFont(Util.myFont);
        topAlbumsGenre.setForeground(Util.blue_dark_color);

        
        //---Top Albums URL---
        JLabel topAlbumsURLLabel = new JLabel("URL :");
        topAlbumsURLLabel.setBounds((int)(0.02 * getWidth()),
                icon.getIconHeight()+50,100,50);
        topAlbumsURLLabel.setFont(Util.myFont);
        topAlbumsURLLabel.setForeground(Util.blue_dark_color);
        //Info code
        topAlbumsURL = new JLabel(albumsList.get(topAlbumsNum).getUrl());
        topAlbumsURL.setBounds((int)(0.02 * getWidth()) + 100,
                icon.getIconHeight()+50,1200,50);
        topAlbumsURL.setFont(Util.myFont);
        topAlbumsURL.setForeground(Util.blue_dark_color);

        add(topAlbumsImage);
        add(topAlbumsNumLabel);
        add(topAlbumsNameLabel);
        add(topAlbumsName);
        add(topAlbumsPlayCountLabel);
        add(topAlbumsPlayCount);
        add(topAlbumsURLLabel);
        add(topAlbumsURL);
        add(topAlbumsNext);
        add(topAlbumsBack);
    }

    private void setAlbum() {
        //---Top Artist Image---
        URL url;
        BufferedImage image;
        try {
            url = new URL(albumsList.get(topAlbumsNum).getImageHolder().getMegaImage());
            image = ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ImageIcon icon = new ImageIcon(image);
        topAlbumsImage = new JLabel(icon);
        topAlbumsImage.setHorizontalAlignment(JLabel.CENTER);
        topAlbumsImage.setVerticalAlignment(JLabel.CENTER);
        topAlbumsImage.setBounds((int)(0.02 * getWidth()),
                (int)(0.05 * getHeight()),icon.getIconWidth(),
                icon.getIconHeight());

        //---Top Artist Number---
        topAlbumsNumLabel.setText(String.valueOf(topAlbumsNum + 1));

        //---Top Artist Name---
        topAlbumsName.setText(albumsList.get(topAlbumsNum).getTitle());

        //---Top Artist Play Count---
        topAlbumsPlayCount.setText(String.valueOf(albumsList.get(topAlbumsNum).getPlayCount()));



        //---Top Artist URL---
        topAlbumsURL.setText(albumsList.get(topAlbumsNum).getUrl());
    }
}