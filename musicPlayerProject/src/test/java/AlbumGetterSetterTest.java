import containers.Album;
import containers.ImageHolder;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class AlbumGetterSetterTest {
    @Test

    public void testing(){
        String expectedname="Skrillex";
        String name= "";
        long playCount = 1;
        String URL = "something@gmail.com";
        String artistmbid = "idk";
        String artistName ="Skrill";
        String artistURL ="skrillex@artist.com";
        ImageHolder imageHolder = null;

        Album testalbum = new Album(name,playCount,URL,artistmbid,artistName,artistURL,imageHolder);

        testalbum.setName("Skrillex");
        String albumname = testalbum.getName();

        assertEquals(expectedname, albumname, "The name should be the same after being set.");
    }
}
