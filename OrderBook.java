import javax.sound.midi.SysexMessage;
import java.util.*;
import java.util.stream.Collectors;
import java.io.*;

public class OrderBook {


    //String symbol;
    //HashMap<String, ArrayList<TradeOrder>> buyBook;
    //TreeMap<String, ArrayList<TradeOrder>> buyBook;
    //TreeMap<String, ArrayList<TradeOrder>> sellBook;

    TreeMap<String, PriorityQueue<TradeOrder>> buyBook;
    TreeMap<String, PriorityQueue<TradeOrder>> sellBook;

    ArrayList<Trade> trades;
    ArrayList<TradeOrder> errors;

    double bestBid, bestOffer;
    int bestBidVol, bestOfferVol;


    public OrderBook() {

        //this.symbol = sym;
        buyBook = new TreeMap<>(new Comparator<String>()
        {
            public int compare(String o1, String o2)
            {
                return new Double(o2).compareTo(new Double(o1));
                //return o1.compareTo(o2);
            }
        });
        ;
        sellBook = new TreeMap<>(new Comparator<String>()
        {
            public int compare(String o1, String o2)
            {
                return new Double(o1).compareTo(new Double(o2));
                //return o1.compareTo(o2);
            }
        });

        trades = new ArrayList<>();
        errors = new ArrayList<>();
    }

    public void processOrder(TradeOrder to) {

        if (to.getMarketLimit().equalsIgnoreCase("MARKET")) {
            processMarketOrder(to);
        }
        else if (to.getMarketLimit().equalsIgnoreCase("LIMIT")) {
            processLimitOrder(to);
        }

        //showBook();


    }

    public boolean processMarketOrder(TradeOrder to) {
        boolean success = false;

        showBook();



        if (to.getSide().equalsIgnoreCase("BUY")) {


            //remove from sellBook
            //System.out.println("Process Market Buy Order " + to.getSymbol());
            try {

                for (Map.Entry<String, PriorityQueue<TradeOrder>>
                        entry : sellBook.entrySet() ){

                    if (entry.getValue().size() == 0)
                        continue;

                    if (Objects.nonNull(entry.getValue().peek())) {
                        trades.add(new Trade( entry.getValue().poll(), to));
                        success = true;
                        break;
                    }


                }

                if (!success) {
                    to.setError("No Limit SELL orders to trade with");
                    errors.add(to);
                }


            }
            catch(Exception e) {
                //System.err.println("No Limit SELL orders to trade with");
                to.setError("No Limit SELL orders to trade with");
                errors.add(to);
                e.printStackTrace();
            }


        }

        else  if (to.getSide().equalsIgnoreCase("SELL")) {


            //System.out.println("Process Market Sell Order" + to.getSymbol());
            //Trade t  = new Trade(to, buyBook.firstEntry().getValue().poll());

            try {
                for (Map.Entry<String, PriorityQueue<TradeOrder>>
                        entry : buyBook.entrySet() ){

                    if (entry.getValue().size() == 0)
                        continue;

                    if (Objects.nonNull(entry.getValue().peek())) {
                        trades.add(new Trade( entry.getValue().poll(), to));
                        success = true;
                        break;
                    }


                }

                if (!success) {
                    to.setError("No Limit BUY orders to trade with");
                    errors.add(to);
                }
            }
            catch (Exception e) {
                //System.err.println("No Limit BUY orders to trade with");
                to.setError("No Limit BUY  orders to trade with");
                errors.add(to);
                e.printStackTrace();
            }





            //showBook();
            //remove from BuyBook

        }


        return success;

    }


    public boolean processLimitOrder (TradeOrder to) {

        boolean success = false;

        //can it execute?


        if (to.getSide().equalsIgnoreCase("BUY")) {

            //can it trade with a sell on the book?

            for (Map.Entry<String, PriorityQueue<TradeOrder>> entry : sellBook.entrySet()) {
                if (entry.getValue().size() == 0)
                    continue;

                if ( new Double (entry.getKey()).compareTo(to.getPrice())  < 0 ) {
                    System.out.println(entry.getKey() + " " + to.getPrice());
                    System.out.println("BOOK IS CROSSED");
                }
                if (Objects.nonNull(entry.getValue().peek())) {
                    trades.add(new Trade( entry.getValue().poll(), to));
                    success = true;
                    break;
                }
            }


            if (!success) {
                if (Objects.isNull(buyBook.get(to.getPrice().toString()))) {
                    buyBook.put(to.getPrice().toString(), new PriorityQueue<TradeOrder> (10, Comparator.comparingDouble(TradeOrder::getTimestamp)));
                }

                // System.out.println("Add Limit Buy Order " + to.getSymbol());
                buyBook.get(to.getPrice().toString()).add(to);
                success = true;
            }


        }
        else if (to.getSide().equalsIgnoreCase("SELL")) {


            for (Map.Entry<String, PriorityQueue<TradeOrder>> entry : buyBook.entrySet()) {
                if (entry.getValue().size() == 0)
                    continue;

                if ( new Double (entry.getKey()).compareTo(to.getPrice())  > 0 ) {
                    System.out.println(entry.getKey() + " " + to.getPrice());
                    System.out.println("BOOK IS CROSSED");
                }
                if (Objects.nonNull(entry.getValue().peek())) {
                    trades.add(new Trade( entry.getValue().poll(), to));
                    success = true;
                    break;
                }
            }

            //if not, just add to order book
            if (!success) {
                if (Objects.isNull(sellBook.get(to.getPrice().toString()))) {
                    sellBook.put(to.getPrice().toString(), new PriorityQueue<TradeOrder> (10, Comparator.comparingDouble(TradeOrder::getTimestamp)));
                }

                // System.out.println("Add Limit Sell Order " + to.getSymbol());
                sellBook.get(to.getPrice().toString()).add(to);
                success = true;

            }

        }



        return success;

    }

    public void showBook() {

        //String out="";
        //buyBook.forEach((price, volume) -> out+= price; out += volume);
        System.out.println("BUY ORDERS");

        int size=0;


            buyBook.entrySet().stream().filter(map -> map.getValue().size() >  0).limit(4).collect(Collectors.toList()).forEach( s-> System.out.println(s.getKey() +  " " + s.getValue().size() ));
            //System.out.println(size);

        System.out.println("SELL ORDERS");
        sellBook.entrySet().stream().filter(map -> map.getValue().size() >  0).limit(4).collect(Collectors.toList()).forEach( s-> System.out.println(s.getKey() +  " " + s.getValue().size() ));


    }

    public void showTrades () {
        System.out.println("TRADES");
        trades.forEach( t -> System.out.println(t.toString()));
    }

    public void showErrors () {

        System.out.println("ERRORS");
        errors.forEach( t -> System.err.println(t.toString()));
    }


    public void toFile(String fileName, ArrayList<TradeOrder> output) {

        try {

            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
                output.forEach(t -> {
                    try {
                        bw.write(t.toString()+'\n');
                    }
                    catch(IOException e) {

                    }

                });


            bw.close();

            System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void tradesToFile(String fileName, ArrayList<Trade> output) {

        try {

            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            output.forEach(t -> {
                try {
                    bw.write(t.toString()+'\n');
                }
                catch(IOException e) {

                }

            });


            bw.close();

            System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void bookToFile(String fileName) {


        try {

            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            buyBook.values().stream().flatMap(pq -> pq.stream()).collect(Collectors.toList()).forEach( to -> {
                try {
                    bw.write(to.toString()+'\n');
                }
                catch(Exception e) {

                }
            });

            sellBook.values().stream().flatMap(pq -> pq.stream()).collect(Collectors.toList()).forEach( to -> {
                try {
                    bw.write(to.toString());
                }
                catch(Exception e) {

                }
            });




            bw.close();

            System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
