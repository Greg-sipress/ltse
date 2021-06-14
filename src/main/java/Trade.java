public class Trade {

    int buyTradeID;
    int sellTradeID;
    double price;
    int vol;
    TradeOrder limitOrder;
    TradeOrder marketOrder;
    double timeStamp;
    String symbol;



    public Trade (TradeOrder limit, TradeOrder market) {


        limitOrder = limit;
        marketOrder = market;
        price = limit.getPrice();
        timeStamp = market.getTimestamp();
        symbol = market.getSymbol();


        System.out.println("TRADE generated");
        System.out.println(toString());

    }

    public String toString() {
        String out ="";

        //out += limitOrder.toString() + " ";
        //out+= marketOrder.toString() + " ";
        out+=symbol+" ";
        out+=price+ " ";
        out+=timeStamp;


        return out;
    }


}
