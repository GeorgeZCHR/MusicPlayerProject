package gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SpecialRoundButton extends JButton {
    Color color;
    int arcWidth;
    int arcHeight;
    public SpecialRoundButton(String label, Color color, int arcWidth, int arcHeight) {
        super(label);
        this.color = color;
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        setUp();
        setColor(color);
        setForeground(this.color.darker());
        useMouse();
    }

    private void setUp() {
        // Set some default properties for the button
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
    }

    private void useMouse() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setForeground(getColor());
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setForeground(getColor().darker());
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = paintSmoother(g);

        if (getModel().isPressed()) {
            g2d.setColor(color.brighter().brighter());
        } else if (getModel().isRollover()) {
            g2d.setColor(color.brighter());
        } else {
            g2d.setColor(color);
        }

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