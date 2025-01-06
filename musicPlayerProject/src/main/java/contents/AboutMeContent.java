package contents;

import general.MusicPlayerFrame;
import general.Util;

import javax.swing.*;
import java.awt.*;

public class AboutMeContent extends JPanel implements Content {
    private MusicPlayerFrame mpf;
    private JLabel accountImage,name,password,email,admin;

    public AboutMeContent(LayoutManager layout, MusicPlayerFrame mpf) {
        super(layout);
        this.mpf = mpf;
    }

    @Override
    public void init() {

        //---Account Image---
        accountImage = new JLabel(new ImageIcon("img/circle-user-blue.png"));
        accountImage.setBounds((int)(0.02 * getWidth()),(int)(0.02 * getHeight()),
                256,256);
        //accountImage.setToolTipText("Account");

        //---Name Label---
        JLabel nameLabel = new JLabel("Username : ");
        nameLabel.setBounds((int)(0.02 * getWidth()),280,150,50);
        nameLabel.setFont(Util.myFont);
        nameLabel.setForeground(Util.blue_dark_color);

        name = new JLabel(mpf.getUser().getUsername());
        name.setBounds((int)(0.02 * getWidth()) + 160,280,300,50);
        name.setFont(Util.myFont);
        name.setForeground(Util.blue_dark_color);

        //---Password Label---
        JLabel passwordLabel = new JLabel("Password : ");
        passwordLabel.setBounds((int)(0.02 * getWidth()),330,150,50);
        passwordLabel.setFont(Util.myFont);
        passwordLabel.setForeground(Util.blue_dark_color);

        password = new JLabel(mpf.getUser().getPassword());
        password.setBounds((int)(0.02 * getWidth()) + 160,330,300,50);
        password.setFont(Util.myFont);
        password.setForeground(Util.blue_dark_color);

        //---Email Label---
        JLabel emailLabel = new JLabel("Email        : ");
        emailLabel.setBounds((int)(0.02 * getWidth()),380,150,50);
        emailLabel.setFont(Util.myFont);
        emailLabel.setForeground(Util.blue_dark_color);

        email = new JLabel(mpf.getUser().getEmail());
        email.setBounds((int)(0.02 * getWidth()) + 160,380,300,50);
        email.setFont(Util.myFont);
        email.setForeground(Util.blue_dark_color);

        //---Admin Label---
        JLabel adminLabel = new JLabel("Admin       : ");
        adminLabel.setBounds((int)(0.02 * getWidth()),430,150,50);
        adminLabel.setFont(Util.myFont);
        adminLabel.setForeground(Util.blue_dark_color);

        admin = new JLabel(Boolean.toString(mpf.getUser().isAdmin()));
        admin.setBounds((int)(0.02 * getWidth()) + 160,430,300,50);
        admin.setFont(Util.myFont);
        admin.setForeground(Util.blue_dark_color);

        add(accountImage);
        add(nameLabel);
        add(name);
        add(passwordLabel);
        add(password);
        add(emailLabel);
        add(email);
        add(adminLabel);
        add(admin);

        /*System.out.println("Songs    :");
        for (int i = 0; i < songs.size(); i++) {
            System.out.println("           Song " + (i + 1) + "   : " + songs.get(i));
        }*/
    }
}