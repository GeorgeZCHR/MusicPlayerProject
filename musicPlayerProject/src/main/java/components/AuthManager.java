package components;
import general.MusicPlayerFrame;
import general.Util;
import gui.CustomPasswordField;
import gui.CustomTextField;
import gui.RoundButton;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.ContentType;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class AuthManager extends JFrame {
    private static final String FIREBASE_API_KEY = "AIzaSyBe0CSxR3GiTWsLdFEEnnSplGep9SMRho4"; // Replace with your API key
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private CustomTextField emailField, registerEmailField;
    private CustomPasswordField passwordField, registerPasswordField;
    private FirestoreManager fr;
    public User user;
    private boolean loginSaccessful = false;

    public AuthManager(FirestoreManager fr) {
        this.fr = fr;

        setTitle("Firebase Authentication");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add Login and Register Panels
        mainPanel.add(createLoginPanel(), "login");
        mainPanel.add(createRegisterPanel(), "register");

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Util.blue_color.brighter());

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(Util.myFont);
        emailLabel.setForeground(Util.blue_dark_color);

        emailField = new CustomTextField(Util.orange_color,20,20);
        emailField.setFont(Util.myFont);
        emailField.setForeground(Util.blue_dark_color);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(Util.myFont);
        passwordLabel.setForeground(Util.blue_dark_color);

        passwordField = new CustomPasswordField(Util.orange_color,20,20);
        passwordField.setFont(Util.myFont);
        passwordField.setForeground(Util.blue_dark_color);

        RoundButton loginButton = new RoundButton("Login",Util.orange_color,
                20,20);
        loginButton.setFont(Util.myFont);
        loginButton.addActionListener(e -> loginUser());

        RoundButton switchToRegisterButton = new RoundButton("Go to Register",
                Util.orange_color,20,20);
        switchToRegisterButton.setFont(Util.myFont);
        switchToRegisterButton.addActionListener(e -> cardLayout.show(mainPanel, "register"));

        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(switchToRegisterButton);

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Util.blue_color.brighter());

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(Util.myFont);
        emailLabel.setForeground(Util.blue_dark_color);

        registerEmailField = new CustomTextField(Util.orange_color,20,20);
        registerEmailField.setFont(Util.myFont);
        registerEmailField.setForeground(Util.blue_dark_color);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(Util.myFont);
        passwordLabel.setForeground(Util.blue_dark_color);

        registerPasswordField = new CustomPasswordField(Util.orange_color,20,20);
        registerPasswordField.setFont(Util.myFont);
        registerPasswordField.setForeground(Util.blue_dark_color);

        RoundButton registerButton = new RoundButton("Register",Util.orange_color,
                20,20);
        registerButton.setFont(Util.myFont);
        registerButton.addActionListener(e -> registerUser());

        RoundButton switchToLoginButton = new RoundButton("Go to Login",
                Util.orange_color,20,20);
        switchToLoginButton.setFont(Util.myFont);
        switchToLoginButton.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        panel.add(emailLabel);
        panel.add(registerEmailField);
        panel.add(passwordLabel);
        panel.add(registerPasswordField);
        panel.add(registerButton);
        panel.add(switchToLoginButton);

        return panel;
    }

    private void loginUser() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        String url = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + FIREBASE_API_KEY;
        String jsonPayload = String.format(
                "{\"email\":\"%s\", \"password\":\"%s\", \"returnSecureToken\":true}",
                email, password
        );

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(url);
            post.setEntity(new StringEntity(jsonPayload, ContentType.APPLICATION_JSON));

            try (CloseableHttpResponse response = client.execute(post)) {
                int statusCode = response.getCode();
                String responseBody = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);

                if (statusCode == 200) {
                    JOptionPane.showMessageDialog(this, "Login successful ");
                    saveCredentialsLocally(email, password);
                    user = fr.getUser(email);
                    SwingUtilities.invokeLater(() -> new MusicPlayerFrame(user,fr,1080,720));
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Login failed: " + responseBody, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registerUser() {
        String email = registerEmailField.getText();
        String password = new String(registerPasswordField.getPassword());

        String url = "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + FIREBASE_API_KEY;
        String jsonPayload = String.format(
                "{\"email\":\"%s\", \"password\":\"%s\", \"returnSecureToken\":true}",
                email, password
        );

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(url);
            post.setEntity(new StringEntity(jsonPayload, ContentType.APPLICATION_JSON));

            try (CloseableHttpResponse response = client.execute(post)) {
                int statusCode = response.getCode();
                String responseBody = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);

                if (statusCode == 200) {
                    JOptionPane.showMessageDialog(this, "Registration successful");
                    String username = email.substring(0,email.indexOf("@"));
                    user = new User(username,password,email,false,new ArrayList<>());
                    fr.setNewUser(user);
                    cardLayout.show(mainPanel, "login");
                } else {
                    JOptionPane.showMessageDialog(this, "Registration failed: " + responseBody, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveCredentialsLocally(String email, String password) {
        JSONObject userCredentials = new JSONObject();

        userCredentials.put("email", email);
        userCredentials.put("password", password);

        String filePath = "userCredentials.json";

        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(userCredentials.toString(4));
            System.out.println("JSON file created: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing JSON file: " + e.getMessage());
        }
    }
}