import com.opencsv.CSVReader;

import java.io.FileReader;
import java.util.*;


public class SimpleMatchingEngine {

    //HashMap validMap = new HashMap<String, Boolean>;
    Set validSet;


    HashMap <String, OrderBook> books;

    public SimpleMatchingEngine() {

    }

    public void init() {


        books = new HashMap<>();
        validSet = getSymbols();

        System.out.println("VALID SYMBOLS");
        for ( Object temp : validSet) {

            System.out.println(temp.toString());
            books.put(temp.toString(), new OrderBook());

        }


    }

    public boolean processIncoming(TradeOrder to) {

        boolean success = false;

        if (validSet.contains(to.getSymbol())) {


            books.get(to.getSymbol()).processOrder(to);

        }

        return success;
    }

    public Set getSymbols() {

        Set result = new HashSet<String>();
        try  {


            CSVReader reader = new CSVReader(new FileReader( System.getProperty("user.dir")+"/target/classes/symbols.csv"));
            String [] nextLine;

            while ((nextLine = reader.readNext()) != null)
            {

                //System.out.println(nextLine);
                try {
                    if (new Boolean(nextLine[1])) {
                        result.add(nextLine[0]);
                    }
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
               /* for(String token : nextLine)
                {
                    System.out.print(token);
                }
                System.out.print("\n");*/
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public void getData() {



        try  {

            int orderCount=0;

            System.out.println("Working Directory = " + System.getProperty("user.dir"));
            //Path path = new
            CSVReader reader = new CSVReader(new FileReader( System.getProperty("user.dir")+"/target/classes/Orders.csv"));
            String [] nextLine;


            while ((nextLine = reader.readNext()) != null)
            {


                //skip the header
                if(orderCount++ ==0) {
                    continue;
                }

                //System.out.println(Arrays.asList(nextLine));

                if (isValid(nextLine)) {

                    TradeOrder to = new TradeOrder(nextLine);

                    processIncoming(to);


                }

                else {

                    //write to error list

                }
            }
            System.out.println("Orders read " + orderCount);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        books.forEach( (id, book) -> {
            book.showBook();
            book.showTrades();
            book.bookToFile(id+"_OrderBook.txt");
            book.toFile(id + "_Errors.txt", book.errors);
            book.tradesToFile(id+"_Trades.txt", book.trades);

            //book.showErrors();
        });

    }


    public boolean isValid(String[] in) {
        //boolean valid = true;

        if (in[0].isEmpty()) {
            System.err.println("NOT VALID SYMBOL" + Arrays.asList(in));
            return false;

        }

        if (! (in[1].equalsIgnoreCase("BUY") || in[1].equalsIgnoreCase("SELL"))) {
            System.err.println("NOT VALID SIDE (buy / sell) " + Arrays.asList(in));
            return false;
        }

        if ( !(in[2].equalsIgnoreCase("MARKET") || in[2].equalsIgnoreCase("LIMIT"))) {

            System.err.println("NOT VALID TYPE (Market / limit) " + Arrays.asList(in));
            return false;

        }
        if (in[2].equalsIgnoreCase("LIMIT") && in[3].isEmpty()) {
            System.err.println("NOT VALID ORDER - no limit order price " + Arrays.asList(in));
            return false;
        }



        return true;
    }

    public static void main (String [] args) {

        SimpleMatchingEngine sme = new SimpleMatchingEngine();
        sme.init();
        sme.getData();


    }
}
