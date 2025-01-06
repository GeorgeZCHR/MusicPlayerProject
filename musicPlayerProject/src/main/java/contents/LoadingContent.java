package contents;

import general.MusicPlayerFrame;
import general.Util;

import javax.swing.*;
import java.awt.*;

public class LoadingContent extends JPanel implements Content{
    private MusicPlayerFrame mpf;
    private JLabel waitingLabel;
    private Timer timer;
    private int counter = 0;

    public LoadingContent(LayoutManager layout, MusicPlayerFrame mpf) {
        super(layout);
        this.mpf = mpf;
    }

    @Override
    public void init() {

        waitingLabel = new JLabel("Loading");
        waitingLabel.setBounds((int)(0.5 * getWidth()) - 50,(int)(0.5 * getHeight()),
                100,50);
        waitingLabel.setForeground(Util.blue_dark_color);
        waitingLabel.setFont(Util.myFont);

        timer = new Timer(1000, e -> {
            counter++;
            if (counter > 0) {
                waitingLabel.setText(waitingLabel.getText() + ".");
            }
            if (counter == 3) {
                waitingLabel.setText("Loading");
                counter = 0;
            }
        });

        add(waitingLabel);
    }

    public void startTimer() {
        timer.start();
    }

    public void stopTimer() {
        if (timer.isRunning()) {
            timer.stop();
        }
    }
}