package contents;
import components.GetTopAlbums;
import general.Util;
import gui.RoundButton;
import org.json.JSONObject;
import javax.swing.*;
import java.awt.*;

public class TopAlbumsContent extends JPanel implements Content{
    private RoundButton getTopAlbums = new RoundButton(
        "Get Top Albums", Util.orange_color, 20,20);
        public TopAlbumsContent(LayoutManager layout) {
        super(layout);
    }
    @Override
        public void init() {
            //--- show JSon Object to console with the button getTopAlbums ---
            getTopAlbums.setBounds((int)(getWidth() * 0.1),
                    (int)(getHeight() * 0.1),
                    (int)(getWidth() * 0.2), 50);
            getTopAlbums.setFocusable(false);
            getTopAlbums.addActionListener(e -> {
            GetTopAlbums albums = new GetTopAlbums();
            JSONObject jsonObject = albums.getTopAlbumsByArtist("Coldplay");
            System.out.println(jsonObject);
            });
        add(getTopAlbums);
    }
}