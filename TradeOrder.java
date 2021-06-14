import java.util.Objects;

public class TradeOrder {

    private String symbol;
    private int volume;
    private String marketLimit;
    private String side;
    private Double price;
    private double timestamp;

    private String status;   //open, partial, filled
    private String error = "";


    public TradeOrder (String[] in) {

        if (in.length == 5) {
            this.symbol = in[0];
            this.side = in[1];
            this.setMarketLimit(in[2]);
            if (in[2].equalsIgnoreCase("LIMIT")) {
                this.price = new Double(in[3]);
            }
            else {
                this.price = null;
            }
            this.volume = 1;
            this.timestamp = new Double(in[4]);

        }
    }


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    public String toString() {
        String out = "";

        out+=this.getSymbol() + ",";
        out+=this.getSide() + ",";
        out+=this.getMarketLimit() + ",";

        if (getMarketLimit().equalsIgnoreCase("LIMIT")) {
            out += getPrice() + ",";
        }
        out+=this.getTimestamp();

        if (Objects.nonNull(error)) {
            out += "," + this.getError();
        }


        return out;


    }

    public String getMarketLimit() {
        return marketLimit;
    }

    public void setMarketLimit(String marketLimit) {
        this.marketLimit = marketLimit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
