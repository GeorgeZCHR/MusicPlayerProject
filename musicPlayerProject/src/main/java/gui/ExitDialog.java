package gui;
import components.AuthManager;
import general.MusicPlayerFrame;
import general.Util;

import javax.swing.*;
import java.awt.*;

public class ExitDialog extends JDialog {
    private MusicPlayerFrame mpf;
    private String title;
    private boolean modal;
    private JLabel label;
    private RoundButton yesButton, noButton;
    public ExitDialog(MusicPlayerFrame mpf,String title, boolean modal) {
        super(mpf,title,modal);

        this.mpf = mpf;
        this.title = title;
        this.modal = modal;

        setSize(426, 400);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(mpf);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBounds(0,0,426,400);
        panel.setOpaque(true);
        panel.setBackground(Util.blue_color.brighter());

        label = new JLabel("Are you sure you want to exit?");
        label.setFont(Util.myFont);
        label.setForeground(Util.blue_dark_color);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(true);
        buttonPanel.setBackground(Util.blue_color.brighter());

        yesButton = new RoundButton("Yes",Util.orange_color,20,20);
        yesButton.addActionListener(e -> setAccept());
        yesButton.setFont(Util.myFont);

        noButton = new RoundButton("No",Util.orange_color,20,20);
        noButton.addActionListener(e -> setDecline());
        noButton.setFont(Util.myFont);

        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

    public void setAccept() {
        AuthManager authManager = new AuthManager(mpf.fr);
        mpf.dispose();
    }
    public void setDecline() {
        dispose();
    }

    public void setLabelText(String text) {
        label.setText(text);
    }

    public void setYesText(String text) {
        yesButton.setText(text);
    }

    public void setNoText(String text) {
        noButton.setText(text);
    }
}