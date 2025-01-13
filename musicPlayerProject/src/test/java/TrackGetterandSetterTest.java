import containers.Track;
import containers.ImageHolder;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class TrackGetterandSetterTest {
    @Test

    public void testing(){
        String expectedname="Skrillex";
        String expectedurl="Skrillex@gmail.com";
        String name="Take On Me";
        long playCount=1;
        long listeners=1;
        String mbid="idk";
        String url="something@gmail.com";
        ImageHolder imageHolder=null;
        long duration=1;
        long streamableText=1;
        long streamableFulltrack=1;
        String artistMBID="idk2";
        String artistName="Skrillex";
        String artistURL="sth@gmail.com";

        Track testTrack = new Track(name,playCount,listeners,mbid,url,
                imageHolder,duration,streamableText,
         streamableFulltrack,artistMBID,artistName,
                artistURL);

        testTrack.setName("Take On Me");
        String Trackname = testTrack.getUrl();

        testTrack.setUrl("Take On Me");
        String TestUrl = testTrack.getUrl();

        assertEquals(expectedname, Trackname, "The name should be the same after being set.");
        assertEquals(expectedname, TestUrl, "The url should be the same after being set.");
    }
}
