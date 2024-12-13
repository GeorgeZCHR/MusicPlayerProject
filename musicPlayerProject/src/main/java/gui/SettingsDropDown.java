package gui;
import general.Util;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class SettingsDropDown extends JPanel {
    private Color color;
    private int cornerRadius;
    private String songName;
    public SettingsDropDown(Color color, int cornerRadius, String songName) {
        this.color = color;
        this.cornerRadius = cornerRadius;
        this.songName = songName;
        setOpaque(false);
        setLayout(null);

        // add button(+) to some playlist
        SpecialRoundButton addButton = new SpecialRoundButton("+",Util.blue_color,
                this.cornerRadius,this.cornerRadius);
        addButton.setBounds(10,10,80,60);
        addButton.setFont(Util.myFont);
        addButton.setFocusable(false);
        addButton.addActionListener(e -> {
            System.out.println("Trying to add " + this.songName + "!");
        });

        // remove button(-) to remove from all songs
        SpecialRoundButton removeButton = new SpecialRoundButton("-",Util.blue_color,
                this.cornerRadius,this.cornerRadius);
        removeButton.setBounds(10,70,80,60);
        removeButton.setFont(Util.myFont);
        removeButton.setFocusable(false);
        removeButton.addActionListener(e -> {
            System.out.println("Trying to remove " + this.songName + "!");
        });

        // edit button(⚙) to remove from all songs
        SpecialRoundButton editButton = new SpecialRoundButton("⚙",Util.blue_color,
                this.cornerRadius,this.cornerRadius);
        editButton.setBounds(10,130,80,60);
        editButton.setFont(Util.myFont);
        editButton.setFocusable(false);
        editButton.addActionListener(e -> {
            System.out.println("Trying to edit " + this.songName + "!");
        });

        add(addButton);
        add(removeButton);
        add(editButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(color);
        g2.fill(new RoundRectangle2D.Float(
                0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));

        g2.dispose();
    }
}