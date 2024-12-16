package gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomPasswordField extends JPasswordField {
    private Color color;
    private int arcWidth;
    private int arcHeight;

    public CustomPasswordField(Color color, int arcWidth, int arcHeight) {
        super();
        setColor(color);
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        setHorizontalAlignment(JTextField.CENTER);
        setCaretColor(this.color.darker());
        setOpaque(false);
        setBorder(null);
        setForeground(this.color.darker());
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setColor(color.brighter());
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setColor(color);
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = paintSmoother(g);
        g2d.setColor(getColor());
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);
        super.paintComponent(g);
    }

    private Graphics2D paintSmoother(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        return g2d;
    }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }
}