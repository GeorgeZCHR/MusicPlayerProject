package gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CustomButton extends JButton {
    private final int RECT = 0;
    private final int OVAL = 1;
    private final int ROUND = 2;
    private final Color blue_color = new Color(0x2a9d8f);
    private Color color;
    private int form;
    private int arcWidth = 0 , arcHeight = 0;

    public CustomButton(String label, Color color, int form) {
        super(label);
        this.color = blue_color;
        if (form != ROUND) {
            this.form = form;
        }
        setUp();
        setColor(color);
        setForeground(this.color.darker());
        useMouse();
    }
    public CustomButton(String label, Color color, int arcWidth, int arcHeight) {
        super(label);
        this.color = blue_color;
        this.form = ROUND;
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
                setForeground(getColor()); // Change color when hovered
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setForeground(getColor().darker()); // Reset color when not hovered
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = paintSmoother(g);

        if (getModel().isPressed()) {
            g2d.setColor(color.brighter().brighter()); // Button press color
        } else if (getModel().isRollover()) {
            g2d.setColor(color.brighter());            // Hover color
        } else {
            g2d.setColor(color);                       // Default color
        }

        fill(g2d);
        super.paintComponent(g);
    }

    private Graphics2D paintSmoother(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        return g2d;
    }

    private void fill(Graphics g) {
        switch (form) {
            case RECT:
                g.fillRect(0, 0, getWidth(), getHeight());
                break;
            case ROUND:
                g.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);
                break;
            case OVAL:
                g.fillOval(0, 0, getWidth(), getHeight());
                break;
        }
    }

    // Getters
    public Color getColor() { return color; }
    public int getForm() { return form; }

    // Setters
    public void setColor(Color color) {
        if (color != null) {
            this.color = color;
        }
    }
    public void setForm(int form) { this.form = form; }
}