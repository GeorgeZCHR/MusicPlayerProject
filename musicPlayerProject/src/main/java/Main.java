import components.AuthManager;
import components.FirestoreManager;
import components.User;
import general.MusicPlayerFrame;
import org.json.JSONObject;

import javax.swing.SwingUtilities;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        FirestoreManager fr = new FirestoreManager(
                "serviceAccountKey.json",
                "https://musicplayerproject-ee0b1-default-rtdb.europe-west1.firebasedatabase.app"
        );
        File file = new File("userCredentials.json");
        if (!file.exists()) {
            JSONObject userCredentials = new JSONObject();

            userCredentials.put("email", "blablabla");
            userCredentials.put("password", "blablabla");

            String filePath = "userCredentials.json";

            try (FileWriter fileWriter = new FileWriter(filePath)) {
                fileWriter.write(userCredentials.toString(4));
                System.out.println("JSON file created: " + filePath);
            } catch (IOException e) {
                System.err.println("Error writing JSON file: " + e.getMessage());
            }
        } else {
            String email = "";
            String password = "";
            try {
                String content = new String(Files.readAllBytes(Paths.get("userCredentials.json")));

                JSONObject userCredentials = new JSONObject(content);

                email = userCredentials.getString("email");
                password = userCredentials.getString("password");

            } catch (IOException e) {
                System.err.println("Error reading the JSON file: " + e.getMessage());
            }
            User user = fr.getUser(email);
            if (user != null) {
                if (email.equals(user.getEmail()) && password.equals(user.getPassword())) {
                    SwingUtilities.invokeLater(() -> new MusicPlayerFrame(user,fr,1080,720));
                }
            } else {
                SwingUtilities.invokeLater(() -> new AuthManager(fr));
            }
        }
    }
}