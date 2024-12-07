package gui;
import general.Util;
import javax.swing.*;

public class WarningFrame extends JFrame {
    public WarningFrame(String title, String message) {
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(title);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(null);
        panel.setBackground(Util.blue_color.brighter());
        panel.setBounds(0, 0, getWidth(), getHeight());

        JTextArea textArea = new JTextArea(message);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(Util.blue_color.brighter());
        textArea.setForeground(Util.blue_dark_color);
        textArea.setFont(Util.myFont);
        textArea.setCaretColor(Util.blue_color.brighter());
        textArea.setBounds(10, 20, getWidth() - 20, 80);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(10, 20, getWidth() - 20, 80);
        scrollPane.setBorder(null);

        CustomButton button = new CustomButton("Ok", Util.orange_color, 20, 20);
        button.setBounds((int) (0.5 * getWidth()) - 100, 100, 200, 50);
        button.addActionListener(e -> dispose());

        panel.add(scrollPane);
        panel.add(button);
        add(panel);

        setVisible(true);
    }
}