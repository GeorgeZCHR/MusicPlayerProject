
import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class SongLoader {
    // path from src to the folder of songs
    public static List<String> loadFromFolder(String path) {
        List<String> pathsOfSongs = new ArrayList<>();
        File folder = new File(path);
        if (folder.exists() && folder.isDirectory()) {
            File[] songsInsideFolder = folder.listFiles();
            if (songsInsideFolder == null) {
                System.out.println("There are not any songs here!");
            } else {
                for (File file : songsInsideFolder) {
                    pathsOfSongs.add(path + file.getName());
                }
            }
        } else {
            System.out.println("Folder " + path + " does not exist or is not accessible!");
        }
        return pathsOfSongs;
    }
    public static List<String> loadFromDataBase(String path) {
        // TODO
        return null;
    }
    public static List<String> loadFromAPI(String path) {
        // TODO
        return null;
    }
}