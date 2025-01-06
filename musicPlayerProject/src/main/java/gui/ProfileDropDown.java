package gui;
import contents.MusicContent;
import general.MusicPlayerFrame;
import general.Util;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ProfileDropDown extends JPanel {
    private Color color;
    private int cornerRadius;
    private MusicPlayerFrame mpf;
    public ProfileDropDown(Color color, int cornerRadius, MusicPlayerFrame mpf) {
        this.color = color;
        this.cornerRadius = cornerRadius;
        this.mpf = mpf;
        setOpaque(false);
        setLayout(null);

        //Width 127
        //Height 216

        // me button to see into a JDialog all the info about this user
        SpecialRoundButton MEButton = new SpecialRoundButton("About me",Util.orange_color,
                this.cornerRadius,this.cornerRadius);
        MEButton.setBounds(13,18,112,50);
        //MEButton.setFont(Util.myFont);
        MEButton.setFocusable(false);
        MEButton.addActionListener(e -> {
            this.mpf.profileShowing = false;
            setVisible(false);
            mpf.showContent(Util.ABOUT_ME_CONTENT);
        });

        // logout button
        SpecialRoundButton logoutButton = new SpecialRoundButton("Logout",Util.orange_color,
                this.cornerRadius,this.cornerRadius);
        logoutButton.setBounds(13,80,112,50);
        //logoutButton.setFont(Util.myFont);
        logoutButton.setFocusable(false);
        logoutButton.addActionListener(e -> {
            this.mpf.profileShowing = false;
            setVisible(false);
            MusicContent mc = (MusicContent)mpf.getContent(Util.MUSIC_CONTENT);
            mc.clearMusicContent();
            Util.writeExampleUserCredentials();
            ExitDialog exitDialog = new ExitDialog(mpf,"Exit",true);
        });

        // logout & exit button
        SpecialRoundButton logoutExitButton = new SpecialRoundButton("Logout & exit",Util.orange_color,
                this.cornerRadius,this.cornerRadius);
        logoutExitButton.setBounds(13,142,112,50);
        //logoutButton.setFont(Util.myFont);
        logoutExitButton.setFocusable(false);
        logoutExitButton.addActionListener(e -> {
            this.mpf.profileShowing = false;
            setVisible(false);
            MusicContent mc = (MusicContent)mpf.getContent(Util.MUSIC_CONTENT);
            mc.clearMusicContent();
            Util.writeExampleUserCredentials();
            mpf.dispose();
            System.exit(0);
        });

        add(MEButton);
        add(logoutButton);
        add(logoutExitButton);
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