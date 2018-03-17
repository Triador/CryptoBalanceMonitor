import com.google.gson.JsonObject;
import org.apache.http.impl.client.HttpClients;
import service.*;

public class App {

    public static void main(String[] args) throws Exception {

        if (args.length != 1) {
            throw new Exception("Wrong number of arguments");
        } 
        
        else if ("total".equals(args[0])) {
            System.out.println("total balance = " + BalanceCalculation.getTotalBalance() + " USD");
        }
        
        else if ("bitfinex".equals(args[0])) {
            System.out.println("bitfinex balance = " + BalanceCalculation.getBitfinexBalance() + " USD");
        }

        else if ("binance".equals(args[0])) {
            System.out.println("binance balance = " + BalanceCalculation.getBinanceBalance() + " USD");
        }

        else if ("bittrex".equals(args[0])) {
            System.out.println("bittrex balance = " + BalanceCalculation.getBittrexBalance() + " USD");
        }

        else if ("hitbtc".equals(args[0])) {
            System.out.println("hitBTC balance = " + BalanceCalculation.getHitBTCBalance() + " USD");
        }

        else if ("myetherwallet".equals(args[0])) {
            System.out.println("myEtherWallet balance = " + BalanceCalculation.getMyEtherWalletBalance() + " USD");
        }
        
        else throw new Exception("Wrong argument");

    }
}