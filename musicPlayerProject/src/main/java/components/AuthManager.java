package components;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.ContentType;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AuthManager extends JFrame {
    private static final String FIREBASE_API_KEY = "APIKEY"; // Replace with your API key
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JTextField emailField, registerEmailField;
    private JPasswordField passwordField, registerPasswordField;

    public AuthManager() {
        setTitle("Firebase Authentication");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> loginUser());

        JButton switchToRegisterButton = new JButton("Go to Register");
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

        JLabel emailLabel = new JLabel("Email:");
        registerEmailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        registerPasswordField = new JPasswordField();

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> registerUser());

        JButton switchToLoginButton = new JButton("Go to Login");
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

    /*public static void main(String[] args) {
        SwingUtilities.invokeLater(components.AuthManager::new);
    }*/
}