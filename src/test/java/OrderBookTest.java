import org.junit.jupiter.api.Test;

public class OrderBookTest {

    @Test
    public void processLimitOrder() {

        String [] raw = { "FB", "sell", "limit","265.57","1608917403.013782"};
        TradeOrder to = new TradeOrder(raw);


    }

    @Test
    public void processMarketOrder() {

        String [] raw = { "FB", "sell", "market","","1608917403.013782"};
        TradeOrder to = new TradeOrder(raw);


    }
}
