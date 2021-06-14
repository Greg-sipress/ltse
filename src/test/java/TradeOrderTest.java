import com.sun.tools.javac.util.Assert;
import org.junit.jupiter.api.Test;

public class TradeOrderTest {

    @Test
    public void tradeOrderTest() {

        try {
            String [] raw = { "", "sell", "limit","265.57","1608917403.013782"};
            TradeOrder to = new TradeOrder(raw);

            System.out.println(to.toString());
        }

        catch(Exception e) {
            Assert.error();
        }

/*
        String [] raw2 = { "FB", "", "limit","265.57","1608917403.013782"};
        TradeOrder to2 = new TradeOrder(raw2);

        String [] raw3 = { "FB", "sell", "","265.57","1608917403.013782"};
        TradeOrder to3 = new TradeOrder(raw3);

        String [] raw4 = { "FB", "sell", "limit","","1608917403.013782"};
        TradeOrder to4 = new TradeOrder(raw4);

        String [] raw5 = { "FB", "sell", "limit","265.57",""};
        TradeOrder to5 = new TradeOrder(raw5);

        String [] raw6 = { "FB","sell","market","","1608917443.5627713"};
        TradeOrder to6 = new TradeOrder(raw6);*/


    }
}
