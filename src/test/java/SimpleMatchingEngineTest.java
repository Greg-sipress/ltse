import com.sun.tools.javac.util.Assert;
import org.junit.jupiter.api.Test;

public class SimpleMatchingEngineTest {


    @Test
    public void engineTest() {

        SimpleMatchingEngine sme = new SimpleMatchingEngine();
        sme.init();

        Assert.checkNonNull(sme.books);

    }
}
