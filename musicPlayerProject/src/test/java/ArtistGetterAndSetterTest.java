import containers.Artist;
import containers.ImageHolder;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class ArtistGetterAndSetterTest {
    @Test

    public void testing(){
    String expectedname="Skrillex";
    long expectedlisteners=2;
        String name="Skrillex";
        long playCount=1;
        long listeners=1;
        String mbid="idk";
        String url ="something@gmail.com";
        long streamable = 0;
        ImageHolder imageHolder= null;

    Artist testArtist = new Artist(name,playCount,listeners,mbid,url,streamable,imageHolder);

    testArtist.setName("Skrillex");
    String artistname = testArtist.getName();

        testArtist.setListeners(4);
        long listenerscount = testArtist.getListeners();


    assertEquals(expectedname, artistname, "The name should be the same after being set.");
    assertEquals(expectedlisteners, listenerscount, "The count should be the same after being set.");
    }
}
