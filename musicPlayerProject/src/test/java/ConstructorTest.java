import containers.Album;
import containers.ImageHolder;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ConstructorTest {
    @Test

    public void testing(){
        String expectedname="Skrillex";
        String name= "Skrillex Album";
        long playCount = 1;
        String URL = "something@gmail.com";
        String artistmbid = "idk";
        String artistName ="Skrill";
        String artistURL ="skrillex@artist.com";
        ImageHolder imageHolder = null;

        Album testalbum = new Album(name,playCount,URL,artistmbid,artistName,artistURL,imageHolder);

        String albumname = testalbum.getName();

        assertEquals(expectedname, albumname, "The name should be the same after being set.");
    }
}
