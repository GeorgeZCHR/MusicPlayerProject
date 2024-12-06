package contents;
import components.GetTopTracks;
import containers.ImageHolder;
import containers.Track;
import general.Util;
import gui.CustomButton;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TopTracksContent extends JPanel implements Content{
    private List<Track> trackList;
    private int topTrackNum = 0;
    private JLabel topTrackImage, topTrackNumLabel;
    private JLabel topTrackName;
    private JLabel topTrackPlayCount;
    private JLabel topTrackListeners;
    private JLabel topTrackURL;

    public TopTracksContent(LayoutManager layout) { super(layout); }

    @Override
    public void init() {
        //---show JSon Object to console with the button getTopTracks ---
        trackList = new ArrayList<>();
        GetTopTracks topTracks = new GetTopTracks();
        JSONObject jsonObject = topTracks.getTopTracks();

        JSONObject tracks = jsonObject.getJSONObject("tracks");

        //JSONObject attr = tracks.getJSONObject("@attr");

        JSONArray tracksArray = tracks.getJSONArray("track");
        for (int i = 0; i < tracksArray.length(); i++) {
            JSONObject track = tracksArray.getJSONObject(i);

            JSONArray images = track.getJSONArray("image");
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

            String streamableText = track.getJSONObject("streamable").getString("#text");
            String streamableFulltrack = track.getJSONObject("streamable").getString("fulltrack");
            String artistMBID = track.getJSONObject("artist").getString("mbid");
            String artistName = track.getJSONObject("artist").getString("name");
            String artistURL = track.getJSONObject("artist").getString("url");

            trackList.add(new Track(
                    track.getString("name"),
                    Long.parseLong(track.getString("playcount")),
                    Long.parseLong(track.getString("listeners")),
                    track.getString("mbid"),
                    track.getString("url"),
                    imageHolder,
                    Long.parseLong(track.getString("duration")),
                    Long.parseLong(streamableText),
                    Long.parseLong(streamableFulltrack),
                    artistMBID,
                    artistName,
                    artistURL
            ));
            //trackList.get(i).print();
        }

        //---Top Track Image---
        URL url;
        BufferedImage image;
        try {
            //System.out.println(trackList.get(topTrackNum).getImageHolder().getMegaImage());
            url = new URL(trackList.get(topTrackNum).getImageHolder().getExtraLargeImage());
            image = ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ImageIcon icon = new ImageIcon(image);
        topTrackImage = new JLabel(icon);
        // Optional: Center align the image in the JLabel
        topTrackImage.setHorizontalAlignment(JLabel.CENTER);
        topTrackImage.setVerticalAlignment(JLabel.CENTER);
        topTrackImage.setBounds((int)(0.02 * getWidth()),
                (int)(0.05 * getHeight()),icon.getIconWidth(),
                icon.getIconHeight());

        //---Top Track Number---
        topTrackNumLabel = new JLabel(String.valueOf(topTrackNum + 1));
        topTrackNumLabel.setFont(Util.headerFont);
        topTrackNumLabel.setForeground(Util.blue_dark_color);
        topTrackNumLabel.setBounds((int)(0.5 * getWidth()) - 25,
                (int)(0.9 * getHeight()),50,50);

        //---Next Button---
        CustomButton topTracksNext = new CustomButton(
                "Next", Util.orange_color, 20,20);
        topTracksNext.setFocusable(false);
        topTracksNext.setFont(Util.myFont);
        topTracksNext.setBounds(
                (int)(0.98 * getWidth()) - 100,
                (int)(0.9 * getHeight()),100,50);
        topTracksNext.addActionListener(e -> {
            if (!(topTrackNum + 1 < trackList.size())) {
                topTrackNum = 0;
            } else {
                topTrackNum++;
            }
            setTrack();
        });

        //---Back Button---
        CustomButton topTracksBack = new CustomButton(
                "Back",Util.orange_color, 20,20);
        topTracksBack.setFocusable(false);
        topTracksBack.setFont(Util.myFont);
        topTracksBack.setBounds((int)(0.02 * getWidth()),
                (int)(0.9 * getHeight()),100,50);
        topTracksBack.addActionListener(e -> {
            if (!(topTrackNum - 1 > -1)) {
                topTrackNum = trackList.size() - 1;
            } else {
                topTrackNum--;
            }
            setTrack();
        });

        //---Top Track Name---
        JLabel topTrackNameLabel = new JLabel("Name          :");
        topTrackNameLabel.setBounds(icon.getIconWidth() + 40,
                (int)(0.05 * getHeight()),150,50);
        topTrackNameLabel.setFont(Util.myFont);
        topTrackNameLabel.setForeground(Util.blue_dark_color);
        topTrackName = new JLabel(trackList.get(topTrackNum).getName());
        topTrackName.setBounds(icon.getIconWidth() + 190,
                (int)(0.05 * getHeight()),400,50);
        topTrackName.setFont(Util.myFont);
        topTrackName.setForeground(Util.blue_dark_color);

        //---Top Track Play Count---
        JLabel topTrackPlayCountLabel = new JLabel("Play Count :");
        topTrackPlayCountLabel.setBounds(icon.getIconWidth() + 40,
                (int)(0.05 * getHeight())+50,150,50);
        topTrackPlayCountLabel.setFont(Util.myFont);
        topTrackPlayCountLabel.setForeground(Util.blue_dark_color);
        topTrackPlayCount = new JLabel(String.valueOf(trackList.get(topTrackNum).getPlayCount()));
        topTrackPlayCount.setBounds(icon.getIconWidth() + 190,
                (int)(0.05 * getHeight())+50,400,50);
        topTrackPlayCount.setFont(Util.myFont);
        topTrackPlayCount.setForeground(Util.blue_dark_color);

        //---Top Track Listeners---
        JLabel topTrackListenersLabel = new JLabel("Listeners   :");
        topTrackListenersLabel.setBounds(icon.getIconWidth() + 40,
                (int)(0.05 * getHeight())+100,150,50);
        topTrackListenersLabel.setFont(Util.myFont);
        topTrackListenersLabel.setForeground(Util.blue_dark_color);
        topTrackListeners = new JLabel(String.valueOf(trackList.get(topTrackNum).getListeners()));
        topTrackListeners.setBounds(icon.getIconWidth() + 190,
                (int)(0.05 * getHeight())+100,400,50);
        topTrackListeners.setFont(Util.myFont);
        topTrackListeners.setForeground(Util.blue_dark_color);

        //---Top Track URL---
        JLabel topTrackURLLabel = new JLabel("URL :");
        topTrackURLLabel.setBounds((int)(0.02 * getWidth()),
                icon.getIconHeight()+50,100,50);
        topTrackURLLabel.setFont(Util.myFont);
        topTrackURLLabel.setForeground(Util.blue_dark_color);
        topTrackURL = new JLabel(trackList.get(topTrackNum).getUrl());
        topTrackURL.setBounds((int)(0.02 * getWidth()) + 100,
                icon.getIconHeight()+50,1200,50);
        topTrackURL.setFont(Util.myFont);
        topTrackURL.setForeground(Util.blue_dark_color);

        add(topTrackImage);
        add(topTrackNumLabel);
        add(topTrackNameLabel);
        add(topTrackName);
        add(topTrackPlayCountLabel);
        add(topTrackPlayCount);
        add(topTrackListenersLabel);
        add(topTrackListeners);
        add(topTrackURLLabel);
        add(topTrackURL);
        add(topTracksNext);
        add(topTracksBack);
    }

    private void setTrack() {
        //---Top Track Image---
        URL url;
        BufferedImage image;
        try {
            url = new URL(trackList.get(topTrackNum).getImageHolder().getExtraLargeImage());
            image = ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ImageIcon icon = new ImageIcon(image);
        topTrackImage = new JLabel(icon);
        topTrackImage.setHorizontalAlignment(JLabel.CENTER);
        topTrackImage.setVerticalAlignment(JLabel.CENTER);
        topTrackImage.setBounds((int)(0.02 * getWidth()),
                (int)(0.05 * getHeight()),icon.getIconWidth(),
                icon.getIconHeight());

        //---Top Track Number---
        topTrackNumLabel.setText(String.valueOf(topTrackNum + 1));

        //---Top Track Name---
        topTrackName.setText(trackList.get(topTrackNum).getName());

        //---Top Track Play Count---
        topTrackPlayCount.setText(String.valueOf(trackList.get(topTrackNum).getPlayCount()));

        //---Top Track Listeners---
        topTrackListeners.setText(String.valueOf(trackList.get(topTrackNum).getListeners()));

        //---Top Track URL---
        topTrackURL.setText(trackList.get(topTrackNum).getUrl());
    }
}