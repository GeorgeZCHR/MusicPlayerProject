package contents;
import java.awt.*;

public interface Content {
    void init();
    void setBounds(int x, int y, int width, int height);
    void setBackground(Color color);
    void setVisible(boolean visible);
}